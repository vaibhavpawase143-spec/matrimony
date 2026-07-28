import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { FaArrowLeft } from "react-icons/fa";
import { toast } from "sonner";

import { getUserSubscriptionHistory } from "../services/subscriptionService";

export default function UserSubscriptionHistory() {
  const { userId } = useParams();
  const navigate = useNavigate();

  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadHistory();
  }, [userId]);

  const loadHistory = async () => {
    try {
      setLoading(true);

      const data = await getUserSubscriptionHistory(userId);

console.log("API Response:", data);
console.log("Is Array:", Array.isArray(data));
console.log("Length:", data?.length);
console.log("History Before Set:", history);

      setHistory(data || []);
    } catch (err) {
      console.error(err);
      toast.error("Failed to load subscription history.");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="p-6 text-center text-gray-500">
        Loading subscription history...
      </div>
    );
  }
console.log("History State:", history);
console.log("History Is Array:", Array.isArray(history));
console.log("History Length:", history?.length);
  const user = history.length ? history[0] : null;
    return (
      <div className="p-6 space-y-6">

        {/* Header */}
        <div className="flex items-center justify-between">

          <div>

            <button
              onClick={() => navigate(-1)}
              className="flex items-center gap-2 text-violet-600 hover:text-violet-800 mb-2"
            >
              <FaArrowLeft />
              Back
            </button>

            <h1 className="text-3xl font-bold">
              Subscription History
            </h1>

            <p className="text-gray-500">
              View all subscriptions purchased by this user.
            </p>

          </div>

        </div>

        {!user ? (

          <div className="bg-white rounded-xl shadow p-8 text-center text-gray-500">
            No subscription history found.
          </div>

        ) : (

          <>
            {/* User Card */}
            <div className="bg-white rounded-2xl shadow-md border border-purple-100 p-6">

              <div className="flex flex-col md:flex-row items-center gap-6">

                <img
                  src={
                    user.imageUrl ||
                    "https://placehold.co/150x150?text=No+Photo"
                  }
                  alt={user.userName}
                  className="w-28 h-28 rounded-full object-cover border-4 border-violet-200"
                />

                <div className="flex-1 space-y-2">

                  <h2 className="text-2xl font-bold">
                    {user.userName}
                  </h2>

                  <p>
                    <span className="font-semibold">
                      Email:
                    </span>{" "}
                    {user.email || "-"}
                  </p>

                  <p>
                    <span className="font-semibold">
                      Phone:
                    </span>{" "}
                    {user.phone || "-"}
                  </p>

                  <p>
                    <span className="font-semibold">
                      User ID:
                    </span>{" "}
                    {user.userId}
                  </p>

                </div>

              </div>

            </div>
              {/* History Table */}
              <div className="bg-white rounded-2xl shadow-md border border-purple-100 overflow-hidden">

                <table className="w-full">

                  <thead className="bg-gradient-to-r from-violet-700 to-purple-600 text-white">

                    <tr>
                      <th className="px-6 py-4 text-left">Plan</th>
                      <th className="px-6 py-4 text-left">Status</th>
                      <th className="px-6 py-4 text-left">Active</th>
                      <th className="px-6 py-4 text-left">Start Date</th>
                      <th className="px-6 py-4 text-left">End Date</th>
                    </tr>

                  </thead>

                  <tbody className="divide-y divide-gray-200">

                    {history.map((subscription) => (

                      <tr
                        key={subscription.id}
                        className="hover:bg-violet-50"
                      >

                        <td className="px-6 py-4 font-medium">
                          {subscription.planName}
                        </td>

                        <td className="px-6 py-4">
                          <span
                            className={`px-3 py-1 rounded-full text-xs font-semibold ${
                              subscription.status === "ACTIVE"
                                ? "bg-green-100 text-green-700"
                                : subscription.status === "EXPIRED"
                                ? "bg-yellow-100 text-yellow-700"
                                : subscription.status === "CANCELLED"
                                ? "bg-red-100 text-red-700"
                                : "bg-gray-100 text-gray-700"
                            }`}
                          >
                            {subscription.status}
                          </span>
                        </td>

                        <td className="px-6 py-4">
                          {subscription.isActive ? "Yes" : "No"}
                        </td>

                        <td className="px-6 py-4">
                          {subscription.startDate
                            ? new Date(subscription.startDate).toLocaleDateString()
                            : "-"}
                        </td>

                        <td className="px-6 py-4">
                          {subscription.endDate
                            ? new Date(subscription.endDate).toLocaleDateString()
                            : "-"}
                        </td>

                      </tr>

                    ))}

                  </tbody>

                </table>

              </div>

            </>

          )}

        </div>
      );
    }