import { useEffect, useState } from "react";
import { FaPaperPlane, FaSearch, FaBullhorn } from "react-icons/fa";
import { toast } from "sonner";

import {
  sendNotification,
  broadcastNotification,
  getNotificationHistory,
} from "../services/notificationService";

export default function Notifications() {

  // =========================================================
  // FORM STATES
  // =========================================================

  const [title, setTitle] = useState("");
  const [message, setMessage] = useState("");
  const [type, setType] = useState("SYSTEM");

  // Send to all OR selected users
  const [broadcast, setBroadcast] = useState(true);

  // Example:
  // 1,5,10
  const [receiverIds, setReceiverIds] = useState("");

  // =========================================================
  // HISTORY
  // =========================================================

  const [notifications, setNotifications] = useState([]);

  const [loading, setLoading] = useState(false);

  const [search, setSearch] = useState("");

  // =========================================================
  // PAGINATION
  // =========================================================

  const [page, setPage] = useState(0);

  const [size, setSize] = useState(10);

  const [totalPages, setTotalPages] = useState(0);

  const [totalElements, setTotalElements] = useState(0);

  // =========================================================
  // LOAD HISTORY
  // =========================================================

  const loadHistory = async (
    currentPage = page,
    currentKeyword = search
  ) => {

    try {

      setLoading(true);

     const response = await getNotificationHistory(
       currentKeyword,
       currentPage,
       size
     );

     const pageData = response.data;
      setNotifications(pageData.content || []);

      setTotalPages(pageData.totalPages || 0);

      setTotalElements(pageData.totalElements || 0);

    } catch (error) {

      console.error(error);

      toast.error(
        error?.response?.data?.message ||
        "Failed to load notification history."
      );

    } finally {

      setLoading(false);

    }
  };

  // =========================================================
  // SEND NOTIFICATION
  // =========================================================

  const handleSend = async () => {

    if (!title.trim()) {
      toast.error("Title is required.");
      return;
    }

    if (!message.trim()) {
      toast.error("Message is required.");
      return;
    }

    try {

      if (broadcast) {

        await broadcastNotification({
          title,
          message,
          type,
        });

      } else {

        const ids = receiverIds
          .split(",")
          .map((id) => Number(id.trim()))
          .filter((id) => !Number.isNaN(id));

        if (ids.length === 0) {
          toast.error("Enter at least one receiver ID.");
          return;
        }

        await sendNotification({
          receiverIds: ids,
          title,
          message,
          type,
        });

      }

      toast.success("Notification sent successfully.");

      setTitle("");
      setMessage("");
      setReceiverIds("");
      setType("SYSTEM");

      loadHistory();

    } catch (error) {

      console.error(error);

      toast.error(
        error?.response?.data?.message ||
        "Failed to send notification."
      );
    }
  };

  // =========================================================
  // INITIAL LOAD
  // =========================================================

  useEffect(() => {

    loadHistory();

  }, [page, size]);

  // =========================================================
  // SEARCH
  // =========================================================

  useEffect(() => {

    const timer = setTimeout(() => {

      setPage(0);

      loadHistory(0, search);

    }, 500);

    return () => clearTimeout(timer);

  }, [search]);
  // =========================================================
  // JSX
  // =========================================================

  return (
    <div className="p-6 space-y-6">

      {/* ================= HEADER ================= */}

      <div>
        <h1 className="text-3xl font-bold text-gray-800">
          Notifications
        </h1>

        <p className="text-gray-500">
          Send notifications to selected users or broadcast to all users.
        </p>
      </div>

      {/* ================= SEND NOTIFICATION ================= */}

      <div className="bg-white rounded-2xl shadow-md border border-purple-100 p-6">

        <h2 className="text-xl font-semibold mb-6">
          Send Notification
        </h2>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">

          {/* Title */}

          <div>
            <label className="block text-sm font-medium mb-2">
              Title
            </label>

            <input
              type="text"
              className="w-full border rounded-lg px-4 py-3 focus:ring-2 focus:ring-violet-500 outline-none"
              placeholder="Notification title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </div>

          {/* Type */}

          <div>
            <label className="block text-sm font-medium mb-2">
              Notification Type
            </label>

           <select
               value={type}
               onChange={(e) => setType(e.target.value)}
           >
               <option value="SYSTEM">System</option>
               <option value="ANNOUNCEMENT">Announcement</option>
               <option value="MAINTENANCE">Maintenance</option>
               <option value="MESSAGE">Message</option>
               <option value="WARNING">Warning</option>
               <option value="SUBSCRIPTION">Subscription</option>
           </select>
          </div>

        </div>

        {/* Message */}

        <div className="mt-5">

          <label className="block text-sm font-medium mb-2">
            Message
          </label>

          <textarea
            rows={5}
            className="w-full border rounded-lg px-4 py-3 resize-none"
            placeholder="Write your notification..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />

        </div>

        {/* Broadcast */}

        <div className="mt-6 flex items-center gap-3">

          <input
            type="checkbox"
            checked={broadcast}
            onChange={(e) => setBroadcast(e.target.checked)}
          />

          <label className="text-sm font-medium">
            Broadcast to all active users
          </label>

        </div>

        {/* Selected Users */}

        {!broadcast && (

          <div className="mt-5">

            <label className="block text-sm font-medium mb-2">
              Receiver IDs
            </label>

            <input
              type="text"
              placeholder="Example: 1,5,10"
              value={receiverIds}
              onChange={(e) => setReceiverIds(e.target.value)}
              className="w-full border rounded-lg px-4 py-3"
            />

          </div>

        )}

        {/* Send */}

        <div className="mt-6">

          <button
            onClick={handleSend}
            className="bg-violet-600 hover:bg-violet-700 text-white px-6 py-3 rounded-lg flex items-center gap-2 transition"
          >
            {broadcast ? <FaBullhorn /> : <FaPaperPlane />}
            {broadcast ? "Broadcast Notification" : "Send Notification"}
          </button>

        </div>

      </div>

      {/* ================= HISTORY ================= */}

      <div className="bg-white rounded-2xl shadow-md border border-purple-100">

        <div className="p-5 flex flex-col md:flex-row justify-between items-center gap-4">

          <h2 className="text-xl font-semibold">
            Notification History
          </h2>

          <div className="flex items-center gap-2 border rounded-lg px-3 py-2 w-full md:w-80">

            <FaSearch className="text-gray-400" />

            <input
              type="text"
              placeholder="Search..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full outline-none"
            />

          </div>

        </div>

        <table className="w-full">

          <thead className="bg-gradient-to-r from-violet-700 to-purple-600 text-white">

            <tr>

              <th className="px-6 py-4 text-left">
                Title
              </th>

              <th className="px-6 py-4 text-left">
                Message
              </th>

              <th className="px-6 py-4 text-left">
                Type
              </th>

              <th className="px-6 py-4 text-left">
                Receiver
              </th>

              <th className="px-6 py-4 text-left">
                Date
              </th>

            </tr>

          </thead>

          <tbody>

            {loading ? (

              <tr>

                <td
                  colSpan="5"
                  className="text-center py-12"
                >
                  Loading...
                </td>

              </tr>

            ) : notifications.length === 0 ? (

              <tr>

                <td
                  colSpan="5"
                  className="text-center py-12 text-gray-500"
                >
                  No notifications found.
                </td>

              </tr>

            ) : (

              notifications.map((item) => (

                <tr
                  key={item.id}
                  className="border-b hover:bg-purple-50"
                >

                  <td className="px-6 py-4">
                    {item.title}
                  </td>

                  <td className="px-6 py-4">
                    {item.message}
                  </td>

                  <td className="px-6 py-4">

                    <span className="bg-violet-100 text-violet-700 px-3 py-1 rounded-full text-xs font-semibold">

                      {item.type}

                    </span>

                  </td>

                  <td className="px-6 py-4">
                    {item.receiverId}
                  </td>

                  <td className="px-6 py-4">
                    {new Date(item.createdAt).toLocaleString()}
                  </td>

                </tr>

              ))

            )}

          </tbody>

        </table>

      </div>

      {/* ================= FOOTER ================= */}

      <div className="flex flex-col md:flex-row justify-between items-center gap-4">

        <p className="text-sm text-gray-600">
          Total Notifications :
          <span className="font-semibold ml-2">
            {totalElements}
          </span>
        </p>

        <div className="flex items-center gap-3">

          <button
            disabled={page === 0}
            onClick={() => setPage((prev) => prev - 1)}
            className="px-4 py-2 rounded-lg bg-gray-100 disabled:opacity-50"
          >
            Previous
          </button>

          <div className="px-4 py-2 bg-violet-600 text-white rounded-lg">

            Page {page + 1} of {Math.max(totalPages, 1)}

          </div>

          <button
            disabled={page + 1 >= totalPages}
            onClick={() => setPage((prev) => prev + 1)}
            className="px-4 py-2 rounded-lg bg-gray-100 disabled:opacity-50"
          >
            Next
          </button>

        </div>

      </div>

    </div>
  );
}