import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FaSearch,
  FaSyncAlt,
  FaTicketAlt,
  FaExclamationCircle,
  FaSpinner,
  FaCheckCircle,
} from "react-icons/fa";
import { toast } from "sonner";

import {
  getSupportTickets,
} from "../services/adminSupportService";

export default function SupportTickets() {
  const navigate = useNavigate();

  // ==========================================
  // STATE
  // ==========================================

  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [priorityFilter, setPriorityFilter] = useState("ALL");

  const [page, setPage] = useState(1);
  const pageSize = 10;

  const [exportMessage, setExportMessage] = useState("");

  // ==========================================
  // LOAD DATA
  // ==========================================

  const loadTickets = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await getSupportTickets();

      setTickets(Array.isArray(response) ? response : []);
    } catch (err) {
      console.error(err);

      setError(
        err?.message || "Failed to load support tickets."
      );

      toast.error("Failed to load support tickets.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTickets();
  }, []);

  // ==========================================
  // FILTERS
  // ==========================================

  const filteredTickets = useMemo(() => {
    return tickets.filter((ticket) => {
      const matchesSearch =
        ticket.ticketNumber
          ?.toLowerCase()
          .includes(search.toLowerCase()) ||
        ticket.userName
          ?.toLowerCase()
          .includes(search.toLowerCase()) ||
        ticket.subject
          ?.toLowerCase()
          .includes(search.toLowerCase());

      const matchesStatus =
        statusFilter === "ALL" ||
        ticket.status === statusFilter;

      const matchesPriority =
        priorityFilter === "ALL" ||
        ticket.priority === priorityFilter;

      return (
        matchesSearch &&
        matchesStatus &&
        matchesPriority
      );
    });
  }, [
    tickets,
    search,
    statusFilter,
    priorityFilter,
  ]);

  // ==========================================
  // PAGINATION
  // ==========================================

  const totalPages = Math.max(
    1,
    Math.ceil(filteredTickets.length / pageSize)
  );

  const paginatedTickets = filteredTickets.slice(
    (page - 1) * pageSize,
    page * pageSize
  );

  useEffect(() => {
    setPage(1);
  }, [
    search,
    statusFilter,
    priorityFilter,
  ]);

  // ==========================================
  // STATISTICS
  // ==========================================

  const stats = {
    total: tickets.length,

    open: tickets.filter(
      (t) => t.status === "OPEN"
    ).length,

    progress: tickets.filter(
      (t) => t.status === "IN_PROGRESS"
    ).length,

    resolved: tickets.filter(
      (t) =>
        t.status === "RESOLVED" ||
        t.status === "CLOSED"
    ).length,
  };

  // ==========================================
  // EXPORT CSV
  // ==========================================

  const exportTickets = () => {
    if (!filteredTickets.length) {
      toast.error("No tickets available.");
      return;
    }

    const rows = [
      [
        "Ticket Number",
        "User",
        "Subject",
        "Category",
        "Priority",
        "Status",
        "Created Date",
      ],
      ...filteredTickets.map((ticket) => [
        ticket.ticketNumber,
        ticket.userName,
        ticket.subject,
        ticket.category,
        ticket.priority,
        ticket.status,
        ticket.createdAt
          ? new Date(
              ticket.createdAt
            ).toLocaleString()
          : "-",
      ]),
    ];

    const csv = rows
      .map((row) =>
        row
          .map(
            (cell) =>
              `"${String(cell ?? "").replace(
                /"/g,
                '""'
              )}"`
          )
          .join(",")
      )
      .join("\n");

    const blob = new Blob([csv], {
      type: "text/csv;charset=utf-8;",
    });

    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");

    link.href = url;
    link.download = "support-tickets.csv";

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    URL.revokeObjectURL(url);

    toast.success("Support tickets exported.");

    setExportMessage(
      `Exported ${filteredTickets.length} ticket${
        filteredTickets.length > 1 ? "s" : ""
      }.`
    );
  };

  // ==========================================
  // UI
  // ==========================================

  return (
    <div className="p-6">

      {/* Header */}

      <div className="flex flex-col lg:flex-row justify-between items-center mb-6 gap-4">

        <div>

          <h1 className="text-3xl font-bold text-gray-800">
            Support Tickets
          </h1>

          <p className="text-gray-500">
            Manage customer support requests.
          </p>

        </div>

        <button
          onClick={loadTickets}
          className="bg-violet-600 hover:bg-violet-700 text-white px-5 py-2 rounded-lg flex items-center gap-2"
        >
          <FaSyncAlt />
          Refresh
        </button>

      </div>

      {/* Statistics */}

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-5 mb-6">

        <div className="bg-white rounded-xl shadow border p-5">
          <FaTicketAlt className="text-3xl text-violet-600 mb-2" />
          <p className="text-gray-500 text-sm">
            Total Tickets
          </p>
          <h2 className="text-3xl font-bold">
            {stats.total}
          </h2>
        </div>

        <div className="bg-white rounded-xl shadow border p-5">
          <FaExclamationCircle className="text-3xl text-blue-600 mb-2" />
          <p className="text-gray-500 text-sm">
            Open
          </p>
          <h2 className="text-3xl font-bold">
            {stats.open}
          </h2>
        </div>

        <div className="bg-white rounded-xl shadow border p-5">
          <FaSpinner className="text-3xl text-yellow-500 mb-2" />
          <p className="text-gray-500 text-sm">
            In Progress
          </p>
          <h2 className="text-3xl font-bold">
            {stats.progress}
          </h2>
        </div>

        <div className="bg-white rounded-xl shadow border p-5">
          <FaCheckCircle className="text-3xl text-green-600 mb-2" />
          <p className="text-gray-500 text-sm">
            Resolved
          </p>
          <h2 className="text-3xl font-bold">
            {stats.resolved}
          </h2>
        </div>

      </div>

      {/* Filter Bar */}

      <div className="bg-white rounded-xl shadow border p-4 mb-6 flex flex-wrap gap-4">

        <div className="flex items-center gap-2 flex-1 min-w-[260px]">

          <FaSearch className="text-gray-400" />

          <input
            type="text"
            placeholder="Search by Ticket, User or Subject..."
            className="w-full outline-none"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />

        </div>

        <select
          value={statusFilter}
          onChange={(e) =>
            setStatusFilter(e.target.value)
          }
          className="border rounded-lg px-3 py-2"
        >
          <option value="ALL">All Status</option>
          <option value="OPEN">Open</option>
          <option value="IN_PROGRESS">
            In Progress
          </option>
          <option value="RESOLVED">
            Resolved
          </option>
          <option value="CLOSED">Closed</option>
        </select>

        <select
          value={priorityFilter}
          onChange={(e) =>
            setPriorityFilter(e.target.value)
          }
          className="border rounded-lg px-3 py-2"
        >
          <option value="ALL">All Priority</option>
          <option value="LOW">Low</option>
          <option value="MEDIUM">Medium</option>
          <option value="HIGH">High</option>
          <option value="URGENT">Urgent</option>
        </select>

      </div>

      {/* TABLE STARTS HERE */}
      <div className="bg-white rounded-xl shadow border overflow-hidden">

        <div className="overflow-x-auto">

          <table className="min-w-full">

            <thead className="bg-violet-600 text-white">

              <tr>

                <th className="px-6 py-4 text-left font-semibold">
                  Ticket No
                </th>

                <th className="px-6 py-4 text-left font-semibold">
                  User
                </th>

                <th className="px-6 py-4 text-left font-semibold">
                  Subject
                </th>

                <th className="px-6 py-4 text-center font-semibold">
                  Category
                </th>

                <th className="px-6 py-4 text-center font-semibold">
                  Priority
                </th>

                <th className="px-6 py-4 text-center font-semibold">
                  Status
                </th>

                <th className="px-6 py-4 text-center font-semibold">
                  Created
                </th>

                <th className="px-6 py-4 text-center font-semibold">
                  Actions
                </th>

              </tr>

            </thead>

            <tbody>

              {/* ================= LOADING ================= */}

              {loading && (

                <tr>

                  <td
                    colSpan={8}
                    className="py-20 text-center"
                  >

                    <div className="flex flex-col items-center gap-4">

                      <FaSpinner className="animate-spin text-4xl text-violet-600" />

                      <p className="text-gray-500">

                        Loading support tickets...

                      </p>

                    </div>

                  </td>

                </tr>

              )}

              {/* ================= ERROR ================= */}

              {!loading && error && (

                <tr>

                  <td
                    colSpan={8}
                    className="py-16 text-center text-red-500 font-medium"
                  >

                    {error}

                  </td>

                </tr>

              )}

              {/* ================= EMPTY ================= */}

              {!loading &&
                !error &&
                paginatedTickets.length === 0 && (

                <tr>

                  <td
                    colSpan={8}
                    className="py-20 text-center text-gray-500"
                  >

                    No support tickets found.

                  </td>

                </tr>

              )}

              {/* ================= DATA ================= */}

              {!loading &&
                !error &&
                paginatedTickets.map((ticket) => (

                <tr
                  key={ticket.ticketNumber}
                  className="border-b hover:bg-violet-50 transition-colors"
                >

                  <td className="px-6 py-4 font-semibold">

                    {ticket.ticketNumber}

                  </td>

                  <td className="px-6 py-4">

                    <div>

                      <p className="font-semibold text-gray-800">

                        {ticket.userName}

                      </p>

                      <p className="text-xs text-gray-500">

                        User ID : {ticket.userId}

                      </p>

                    </div>

                  </td>

                  <td className="px-6 py-4">

                    <div className="max-w-xs truncate">

                      {ticket.subject}

                    </div>

                  </td>

                  <td className="px-6 py-4 text-center">

                    {ticket.category}

                  </td>

                  {/* Priority */}

                  <td className="px-6 py-4 text-center">

                    <span
                      className={`px-3 py-1 rounded-full text-xs font-semibold text-white

                      ${
                        ticket.priority === "LOW"
                          ? "bg-green-500"
                          : ""
                      }

                      ${
                        ticket.priority === "MEDIUM"
                          ? "bg-yellow-500"
                          : ""
                      }

                      ${
                        ticket.priority === "HIGH"
                          ? "bg-orange-500"
                          : ""
                      }

                      ${
                        ticket.priority === "URGENT"
                          ? "bg-red-600"
                          : ""
                      }

                      `}
                    >

                      {ticket.priority}

                    </span>

                  </td>

                  {/* Status */}

                  <td className="px-6 py-4 text-center">

                    <span
                      className={`px-3 py-1 rounded-full text-xs font-semibold text-white

                      ${
                        ticket.status === "OPEN"
                          ? "bg-blue-600"
                          : ""
                      }

                      ${
                        ticket.status === "IN_PROGRESS"
                          ? "bg-yellow-500"
                          : ""
                      }

                      ${
                        ticket.status === "RESOLVED"
                          ? "bg-green-600"
                          : ""
                      }

                      ${
                        ticket.status === "CLOSED"
                          ? "bg-gray-600"
                          : ""
                      }

                      `}
                    >

                      {ticket.status.replace("_", " ")}

                    </span>

                  </td>

                  <td className="px-6 py-4 text-center">

                    {ticket.createdAt
                      ? new Date(
                          ticket.createdAt
                        ).toLocaleDateString()
                      : "-"}

                  </td>

                  {/* Actions */}

                  <td className="px-6 py-4">

                    <div className="flex justify-center">

                      <button
                        onClick={() =>
                          navigate(
                            `/support-tickets/${ticket.ticketNumber}`
                          )
                        }
                        className="bg-violet-100 hover:bg-violet-200 text-violet-700 px-3 py-2 rounded-lg transition"
                      >

                        View Details

                      </button>

                    </div>

                  </td>

                </tr>

              ))}

            </tbody>

          </table>

        </div>

      </div>
            {/* ================= FOOTER ================= */}

            <div className="flex flex-col lg:flex-row justify-between items-center gap-4 mt-6">

              <div>

                <p className="text-gray-700 font-medium">
                  Showing{" "}
                  <span className="font-bold">
                    {paginatedTickets.length}
                  </span>{" "}
                  of{" "}
                  <span className="font-bold">
                    {filteredTickets.length}
                  </span>{" "}
                  support tickets
                </p>

                {exportMessage && (
                  <p className="text-green-600 text-sm mt-2">
                    {exportMessage}
                  </p>
                )}

              </div>

              {/* Pagination */}

              <div className="flex items-center gap-3">

                <button
                  disabled={page === 1}
                  onClick={() => setPage((prev) => prev - 1)}
                  className={`px-4 py-2 rounded-lg border transition

                    ${
                      page === 1
                        ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                        : "bg-white hover:bg-violet-50"
                    }

                  `}
                >
                  Previous
                </button>

                <span className="font-medium">
                  Page {page} of {totalPages}
                </span>

                <button
                  disabled={page === totalPages}
                  onClick={() => setPage((prev) => prev + 1)}
                  className={`px-4 py-2 rounded-lg border transition

                    ${
                      page === totalPages
                        ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                        : "bg-white hover:bg-violet-50"
                    }

                  `}
                >
                  Next
                </button>

              </div>

              {/* Export */}

              <button
                onClick={exportTickets}
                className="bg-violet-600 hover:bg-violet-700 text-white px-5 py-2 rounded-lg transition"
              >
                Export CSV
              </button>

            </div>

          </div>

        );

      }