import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import {
  FaArrowLeft,
  FaUser,
  FaTicketAlt,
  FaSpinner,
  FaSyncAlt,
} from "react-icons/fa";

import { toast } from "sonner";

import {
  getSupportTicket,
  updateSupportStatus,
  replySupportTicket,
} from "../services/adminSupportService";

export default function SupportTicketDetails() {

  const navigate = useNavigate();
  const { ticketNumber } = useParams();

  // ==========================================
  // STATE
  // ==========================================

  const [ticket, setTicket] = useState(null);

  const [loading, setLoading] = useState(true);

  const [savingStatus, setSavingStatus] = useState(false);

  const [sendingReply, setSendingReply] = useState(false);

  const [status, setStatus] = useState("");

  const [reply, setReply] = useState("");

  // ==========================================
  // LOAD TICKET
  // ==========================================

  const loadTicket = async () => {

    try {

      setLoading(true);

      const data = await getSupportTicket(ticketNumber);

      setTicket(data);

      setStatus(data.status);

      setReply(data.adminReply || "");

    } catch (error) {

      console.error(error);

      toast.error(
        error.message ||
        "Failed to load support ticket."
      );

    } finally {

      setLoading(false);

    }

  };

  useEffect(() => {

    loadTicket();

  }, [ticketNumber]);

  // ==========================================
  // UPDATE STATUS
  // ==========================================

  const handleStatusUpdate = async () => {

    try {

      setSavingStatus(true);

      await updateSupportStatus(
        ticketNumber,
        status
      );

      toast.success(
        "Status updated successfully."
      );

      loadTicket();

    } catch (error) {

      toast.error(
        error.message ||
        "Failed to update status."
      );

    } finally {

      setSavingStatus(false);

    }

  };

  // ==========================================
  // SEND REPLY
  // ==========================================

  const handleReply = async () => {

    if (!reply.trim()) {

      toast.error(
        "Reply cannot be empty."
      );

      return;

    }

    try {

      setSendingReply(true);

      await replySupportTicket(
        ticketNumber,
        reply
      );

      toast.success(
        "Reply sent successfully."
      );

      loadTicket();

    } catch (error) {

      toast.error(
        error.message ||
        "Failed to send reply."
      );

    } finally {

      setSendingReply(false);

    }

  };

  // ==========================================
  // LOADING
  // ==========================================

  if (loading) {

    return (

      <div className="h-[70vh] flex justify-center items-center">

        <div className="text-center">

          <FaSpinner className="animate-spin text-5xl text-violet-600 mx-auto mb-4" />

          <p className="text-gray-500">

            Loading Ticket...

          </p>

        </div>

      </div>

    );

  }

  // ==========================================
  // UI
  // ==========================================

  return (

    <div className="p-6">

      {/* Header */}

      <div className="flex justify-between items-center mb-6">

        <div className="flex items-center gap-3">

          <button

            onClick={() => navigate(-1)}

            className="bg-gray-100 hover:bg-gray-200 p-3 rounded-lg"

          >

            <FaArrowLeft />

          </button>

          <div>

            <h1 className="text-3xl font-bold">

              Support Ticket

            </h1>

            <p className="text-gray-500">

              {ticket.ticketNumber}

            </p>

          </div>

        </div>

        <button

          onClick={loadTicket}

          className="bg-violet-600 hover:bg-violet-700 text-white px-5 py-2 rounded-lg flex items-center gap-2"

        >

          <FaSyncAlt />

          Refresh

        </button>

      </div>

      {/* Status Cards */}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">

        <div className="bg-white rounded-xl shadow border p-5">

          <FaTicketAlt className="text-violet-600 text-3xl mb-3" />

          <p className="text-gray-500 text-sm">

            Ticket Status

          </p>

          <span
            className={`inline-block mt-2 px-4 py-2 rounded-full text-white

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

        </div>

        <div className="bg-white rounded-xl shadow border p-5">

          <FaUser className="text-violet-600 text-3xl mb-3" />

          <p className="text-gray-500 text-sm">

            Priority

          </p>

          <span
            className={`inline-block mt-2 px-4 py-2 rounded-full text-white

            ${
              ticket.priority === "LOW"
                ? "bg-green-600"
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

        </div>

      </div>

      {/* CONTENT STARTS HERE */}
            {/* ================= USER INFORMATION ================= */}

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">

              <div className="bg-white rounded-xl shadow border p-6">

                <h2 className="text-xl font-bold mb-5">

                  User Information

                </h2>

                <div className="space-y-4">

                  <div>

                    <p className="text-sm text-gray-500">

                      User Name

                    </p>

                    <p className="font-semibold">

                      {ticket.userName || "-"}

                    </p>

                  </div>

                  <div>

                    <p className="text-sm text-gray-500">

                      User ID

                    </p>

                    <p className="font-semibold">

                      {ticket.userId || "-"}

                    </p>

                  </div>

                  <div>

                    <p className="text-sm text-gray-500">

                      Category

                    </p>

                    <p className="font-semibold">

                      {ticket.category}

                    </p>

                  </div>

                </div>

              </div>

              {/* ================= TICKET INFORMATION ================= */}

              <div className="bg-white rounded-xl shadow border p-6">

                <h2 className="text-xl font-bold mb-5">

                  Ticket Information

                </h2>

                <div className="space-y-4">

                  <div>

                    <p className="text-sm text-gray-500">

                      Ticket Number

                    </p>

                    <p className="font-semibold">

                      {ticket.ticketNumber}

                    </p>

                  </div>

                  <div>

                    <p className="text-sm text-gray-500">

                      Subject

                    </p>

                    <p className="font-semibold">

                      {ticket.subject}

                    </p>

                  </div>

                  <div>

                    <p className="text-sm text-gray-500">

                      Priority

                    </p>

                    <p className="font-semibold">

                      {ticket.priority}

                    </p>

                  </div>

                </div>

              </div>

            </div>

            {/* ================= USER MESSAGE ================= */}

            <div className="bg-white rounded-xl shadow border p-6 mb-6">

              <h2 className="text-xl font-bold mb-4">

                Support Message

              </h2>

              <div className="bg-gray-50 rounded-lg p-5 border">

                <p className="whitespace-pre-wrap leading-7">

                  {ticket.message}

                </p>

              </div>

            </div>


            {/* ================= TIMELINE ================= */}

            <div className="bg-white rounded-xl shadow border p-6 mb-6">

              <h2 className="text-xl font-bold mb-5">

                Timeline

              </h2>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-5">

                <div>

                  <p className="text-sm text-gray-500">

                    Created At

                  </p>

                  <p className="font-medium">

                    {

                      ticket.createdAt

                        ? new Date(ticket.createdAt)

                            .toLocaleString()

                        : "-"

                    }

                  </p>

                </div>

                <div>

                  <p className="text-sm text-gray-500">

                    Updated At

                  </p>

                  <p className="font-medium">

                    {

                      ticket.updatedAt

                        ? new Date(ticket.updatedAt)

                            .toLocaleString()

                        : "-"

                    }

                  </p>

                </div>

                <div>

                  <p className="text-sm text-gray-500">

                    Resolved At

                  </p>

                  <p className="font-medium">

                    {

                      ticket.resolvedAt

                        ? new Date(ticket.resolvedAt)

                            .toLocaleString()

                        : "-"

                    }

                  </p>

                </div>

              </div>

            </div>

            {/* ======= ADMIN ACTIONS START HERE ======= */}
                  <div className="bg-white rounded-xl shadow border p-6 mb-6">

                    <h2 className="text-xl font-bold mb-6">

                      Admin Actions

                    </h2>

                    {/* Status Update */}

                    <div className="mb-8">

                      <label className="block text-sm font-medium mb-2">

                        Ticket Status

                      </label>

                      <div className="flex flex-col md:flex-row gap-4">

                        <select
                          value={status}
                          onChange={(e) => setStatus(e.target.value)}
                          className="border rounded-lg px-4 py-3 w-full md:w-80"
                        >
                          <option value="OPEN">OPEN</option>
                          <option value="IN_PROGRESS">IN PROGRESS</option>
                          <option value="RESOLVED">RESOLVED</option>
                          <option value="CLOSED">CLOSED</option>
                        </select>

                        <button
                          onClick={handleStatusUpdate}
                          disabled={savingStatus}
                          className="bg-violet-600 hover:bg-violet-700 disabled:opacity-60 text-white px-6 py-3 rounded-lg font-medium flex items-center justify-center gap-2"
                        >
                          {savingStatus ? (
                            <>
                              <FaSpinner className="animate-spin" />
                              Updating...
                            </>
                          ) : (
                            <>
                              Save Status
                            </>
                          )}
                        </button>

                      </div>

                    </div>

                    {/* Reply */}

                    <div>

                      <label className="block text-sm font-medium mb-2">

                        Admin Reply

                      </label>

                      <textarea
                        rows={8}
                        value={reply}
                        onChange={(e) => setReply(e.target.value)}
                        placeholder="Write your reply to the user..."
                        className="w-full border rounded-lg p-4 resize-none"
                      />

                      <div className="flex justify-end mt-4">

                        <button
                          onClick={handleReply}
                          disabled={sendingReply}
                          className="bg-green-600 hover:bg-green-700 disabled:opacity-60 text-white px-6 py-3 rounded-lg font-medium flex items-center gap-2"
                        >
                          {sendingReply ? (
                            <>
                              <FaSpinner className="animate-spin" />
                              Sending...
                            </>
                          ) : (
                            <>
                              Send Reply
                            </>
                          )}
                        </button>

                      </div>

                    </div>

                  </div>

                </div>

              );

            }