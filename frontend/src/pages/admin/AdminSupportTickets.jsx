import AdminLayout from "@/components/AdminLayout";
import { adminSupportAPI } from "@/services/api";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Eye } from "lucide-react";
import toast from "react-hot-toast";

const AdminSupportTickets = () => {

  const navigate = useNavigate();

  const [tickets, setTickets] = useState([]);

  const [loading, setLoading] = useState(true);

  const [search, setSearch] = useState("");

  useEffect(() => {

    loadTickets();

  }, []);

  const loadTickets = async () => {

    try {

      setLoading(true);

      const data =
        await adminSupportAPI.getAllTickets();

      console.log(
        "ADMIN SUPPORT TICKETS =",
        data
      );

      setTickets(
        Array.isArray(data)
          ? data
          : []
      );

    } catch (error) {

      console.error(error);

      toast.error(
        "Failed to load support tickets."
      );

    } finally {

      setLoading(false);

    }

  };

  const badgeStatus = (status) => {

    switch (status) {

      case "OPEN":
        return "bg-green-100 text-green-700";

      case "IN_PROGRESS":
        return "bg-yellow-100 text-yellow-700";

      case "RESOLVED":
        return "bg-blue-100 text-blue-700";

      case "CLOSED":
        return "bg-red-100 text-red-700";

      default:
        return "bg-gray-100 text-gray-700";

    }

  };

  const badgePriority = (priority) => {

    switch (priority) {

      case "HIGH":
        return "bg-red-100 text-red-700";

      case "MEDIUM":
        return "bg-yellow-100 text-yellow-700";

      case "LOW":
        return "bg-green-100 text-green-700";

      default:
        return "bg-gray-100 text-gray-700";

    }

  };

  const filteredTickets = tickets.filter((ticket) => {

    const keyword = search.toLowerCase();

    return (

      ticket.ticketNumber
        ?.toLowerCase()
        .includes(keyword)

      ||

      ticket.userName
        ?.toLowerCase()
        .includes(keyword)

      ||

      ticket.subject
        ?.toLowerCase()
        .includes(keyword)

    );

  });

  if (loading) {

    return (

      <AdminLayout>

        <div className="flex justify-center items-center h-96">

          <div className="text-lg font-semibold">

            Loading Support Tickets...

          </div>

        </div>

      </AdminLayout>

    );

  }
  return (
    <AdminLayout>
      <div className="p-6">

        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">

          <div>
            <h1 className="text-3xl font-bold">
              Support Tickets
            </h1>

            <p className="text-gray-500 mt-1">
              Manage user support requests
            </p>
          </div>

          <input
            type="text"
            placeholder="Search ticket..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="border rounded-xl px-4 py-2 w-full md:w-80"
          />

        </div>

        {/* Empty State */}

        {filteredTickets.length === 0 ? (

          <div className="bg-white rounded-xl shadow border p-12 text-center">

            <h2 className="text-xl font-semibold">
              No Support Tickets
            </h2>

            <p className="text-gray-500 mt-2">
              No support tickets found.
            </p>

          </div>

        ) : (

          <div className="bg-white rounded-xl shadow border overflow-x-auto">

            <table className="w-full">

              <thead className="bg-gray-100">

                <tr className="text-left">

                  <th className="p-4">Ticket</th>

                  <th className="p-4">User</th>

                  <th className="p-4">Category</th>

                  <th className="p-4">Subject</th>

                  <th className="p-4">Priority</th>

                  <th className="p-4">Status</th>

                  <th className="p-4">Created</th>

                  <th className="p-4 text-center">
                    Action
                  </th>

                </tr>

              </thead>

              <tbody>

                {filteredTickets.map((ticket) => (

                  <tr
                    key={ticket.id}
                    className="border-t hover:bg-gray-50"
                  >

                    <td className="p-4 font-medium">
                      {ticket.ticketNumber}
                    </td>

                    <td className="p-4">
                      {ticket.userName}
                    </td>

                    <td className="p-4">
                      {ticket.category}
                    </td>

                    <td className="p-4">
                      {ticket.subject}
                    </td>

                    <td className="p-4">

                      <span
                        className={`
                        px-3
                        py-1
                        rounded-full
                        text-sm
                        font-medium
                        ${badgePriority(ticket.priority)}
                        `}
                      >
                        {ticket.priority}
                      </span>

                    </td>

                    <td className="p-4">

                      <span
                        className={`
                        px-3
                        py-1
                        rounded-full
                        text-sm
                        font-medium
                        ${badgeStatus(ticket.status)}
                        `}
                      >
                        {ticket.status}
                      </span>

                    </td>

                    <td className="p-4">

                      {
                        new Date(ticket.createdAt)
                          .toLocaleDateString(
                            "en-IN",
                            {
                              day: "2-digit",
                              month: "short",
                              year: "numeric"
                            }
                          )
                      }

                    </td>

                    <td className="p-4 text-center">

                      <button
                        onClick={() =>
                          navigate(
                            `/admin/support/${ticket.ticketNumber}`
                          )
                        }
                        className="
                        flex
                        items-center
                        gap-2
                        mx-auto
                        bg-pink-600
                        hover:bg-pink-700
                        text-white
                        px-4
                        py-2
                        rounded-lg
                        "
                      >
                        <Eye size={18} />

                        View

                      </button>

                    </td>

                  </tr>

                ))}

              </tbody>

            </table>

          </div>

        )}

      </div>
    </AdminLayout>
  );

};

export default AdminSupportTickets;