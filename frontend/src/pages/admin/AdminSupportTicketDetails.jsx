import AdminLayout from "@/components/AdminLayout";
import { adminSupportAPI } from "@/services/api";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import toast from "react-hot-toast";

const AdminSupportTicketDetails = () => {

  const navigate = useNavigate();

  const { ticketNumber } = useParams();

  const [ticket, setTicket] = useState(null);

  const [loading, setLoading] = useState(true);

  const [saving, setSaving] = useState(false);

  const [reply, setReply] = useState("");

  const [status, setStatus] = useState("");

  const [error, setError] = useState("");

  useEffect(() => {

    loadTicket();

  }, [ticketNumber]);

  const loadTicket = async () => {

    try {

      setLoading(true);

      setError("");

      const data =
        await adminSupportAPI.getTicket(ticketNumber);

      console.log(
        "ADMIN TICKET =",
        data
      );

      setTicket(data);

      setReply(
        data.adminReply || ""
      );

      setStatus(
        data.status || "OPEN"
      );

    } catch (error) {

      console.error(error);

      setError(
        error.message ||
        "Unable to load support ticket."
      );

      setTicket(null);

    } finally {

      setLoading(false);

    }

  };

  const updateStatus = async () => {

    try {

      setSaving(true);

      await adminSupportAPI.updateStatus(
        ticket.ticketNumber,
        status
      );

      toast.success(
        "Ticket status updated."
      );

      loadTicket();

    } catch (error) {

      console.error(error);

      toast.error(
        error.message ||
        "Failed to update status."
      );

    } finally {

      setSaving(false);

    }

  };

  const sendReply = async () => {

    if (!reply.trim()) {

      toast.error(
        "Please enter reply."
      );

      return;

    }

    try {

      setSaving(true);

      await adminSupportAPI.replyTicket(
        ticket.ticketNumber,
        reply
      );

      toast.success(
        "Reply sent successfully."
      );

      loadTicket();

    } catch (error) {

      console.error(error);

      toast.error(
        error.message ||
        "Failed to send reply."
      );

    } finally {

      setSaving(false);

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

  if (loading) {

    return (

      <AdminLayout>

        <div className="flex justify-center items-center h-96">

          <h2 className="text-xl font-semibold">

            Loading Ticket...

          </h2>

        </div>

      </AdminLayout>

    );

  }

  if (error || !ticket) {

    return (

      <AdminLayout>

        <div className="flex justify-center items-center h-96">

          <div className="text-center">

            <h2 className="text-2xl font-bold">

              Ticket Not Found

            </h2>

            <p className="text-gray-500 mt-3">

              {error}

            </p>

            <button
              onClick={() => navigate("/admin/support")}
              className="mt-6 bg-pink-600 text-white px-6 py-2 rounded-xl"
            >
              Back
            </button>

          </div>

        </div>

      </AdminLayout>

    );

  }
  return (
    <AdminLayout>
      <div className="max-w-6xl mx-auto p-6">

        {/* Header */}
        <div className="flex items-center justify-between mb-8">

          <div>

            <button
              onClick={() => navigate("/admin/support")}
              className="text-pink-600 hover:underline mb-3"
            >
              ← Back
            </button>

            <h1 className="text-3xl font-bold">
              Support Ticket Details
            </h1>

            <p className="text-gray-500 mt-2">
              {ticket.ticketNumber}
            </p>

          </div>

          <div className="flex gap-3">

            <span
              className={`px-4 py-2 rounded-full font-semibold ${badgePriority(ticket.priority)}`}
            >
              {ticket.priority}
            </span>

            <span
              className={`px-4 py-2 rounded-full font-semibold ${badgeStatus(ticket.status)}`}
            >
              {ticket.status}
            </span>

          </div>

        </div>

        <div className="bg-white rounded-2xl shadow border p-8">

          {/* Basic Info */}

          <div className="grid md:grid-cols-2 gap-6">

            <div>

              <p className="text-gray-500 text-sm">
                User
              </p>

              <p className="font-semibold">
                {ticket.userName}
              </p>

            </div>

            <div>

              <p className="text-gray-500 text-sm">
                Category
              </p>

              <p className="font-semibold">
                {ticket.category}
              </p>

            </div>

            <div>

              <p className="text-gray-500 text-sm">
                Subject
              </p>

              <p className="font-semibold">
                {ticket.subject}
              </p>

            </div>

            <div>

              <p className="text-gray-500 text-sm">
                Created
              </p>

              <p className="font-semibold">
                {new Date(ticket.createdAt).toLocaleString("en-IN")}
              </p>

            </div>

          </div>

          {/* Message */}

          <div className="mt-8">

            <h2 className="font-semibold mb-2">
              User Message
            </h2>

            <div className="bg-gray-50 rounded-xl p-4 whitespace-pre-wrap">

              {ticket.message}

            </div>

          </div>

          {/* Attachment */}

          {ticket.attachmentUrl && (

            <div className="mt-8">

              <h2 className="font-semibold mb-2">
                Attachment
              </h2>

              <a
                href={ticket.attachmentUrl}
                target="_blank"
                rel="noreferrer"
                className="text-blue-600 underline"
              >
                View Attachment
              </a>

            </div>

          )}

          {/* Status */}

          <div className="mt-10">

            <label className="font-semibold block mb-2">
              Update Status
            </label>

            <div className="flex gap-4">

              <select
                value={status}
                onChange={(e) => setStatus(e.target.value)}
                className="border rounded-xl px-4 py-3"
              >
                <option value="OPEN">
                  OPEN
                </option>

                <option value="IN_PROGRESS">
                  IN_PROGRESS
                </option>

                <option value="RESOLVED">
                  RESOLVED
                </option>

                <option value="CLOSED">
                  CLOSED
                </option>

              </select>

              <button
                disabled={saving}
                onClick={updateStatus}
                className="bg-indigo-600 hover:bg-indigo-700 text-white px-6 rounded-xl"
              >
                Update
              </button>

            </div>

          </div>

          {/* Reply */}

          <div className="mt-10">

            <label className="font-semibold block mb-2">
              Admin Reply
            </label>

            <textarea
              rows={6}
              value={reply}
              onChange={(e) => setReply(e.target.value)}
              className="w-full border rounded-xl p-4"
              placeholder="Type your reply..."
            />

            <div className="flex justify-end mt-4">

              <button
                disabled={saving}
                onClick={sendReply}
                className="bg-pink-600 hover:bg-pink-700 text-white px-8 py-3 rounded-xl"
              >
                Send Reply
              </button>

            </div>

          </div>

        </div>

      </div>
    </AdminLayout>
  );

};

export default AdminSupportTicketDetails;