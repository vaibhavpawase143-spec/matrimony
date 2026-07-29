import React, { useEffect, useState } from "react";
import { subscriptionAPI } from "../services/api";
import { useNavigate } from "react-router-dom";
const SubscriptionHistory = () => {
  const [currentSubscription, setCurrentSubscription] = useState(null);
  const [subscriptionHistory, setSubscriptionHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
const navigate = useNavigate();
  useEffect(() => {
    loadSubscriptionData();
  }, []);

  const loadSubscriptionData = async () => {
    try {
      setLoading(true);
      setError("");

    const [currentPlan, history] = await Promise.all([
      subscriptionAPI.getMySubscription(),
      subscriptionAPI.getHistory(),
    ]);

      setCurrentSubscription(currentPlan || null);
      setSubscriptionHistory(Array.isArray(history) ? history : []);
    } catch (err) {
      console.error("Error loading subscription data:", err);
      setError("Failed to load subscription details.");
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (date) => {
    if (!date) return "-";

    return new Date(date).toLocaleDateString("en-IN", {
      day: "2-digit",
      month: "short",
      year: "numeric",
    });
  };

  const calculateDaysRemaining = (endDate) => {
    if (!endDate) return 0;

    const today = new Date();
    const expiry = new Date(endDate);

    const diff = expiry - today;

    const days = Math.ceil(diff / (1000 * 60 * 60 * 24));

    return days > 0 ? days : 0;
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "ACTIVE":
        return "bg-green-100 text-green-700";

      case "CANCELLED":
        return "bg-orange-100 text-orange-700";

      case "EXPIRED":
        return "bg-red-100 text-red-700";

      case "REFUNDED":
        return "bg-purple-100 text-purple-700";

      default:
        return "bg-gray-100 text-gray-700";
    }
  };

 if (loading) {
   return (
     <div className="max-w-7xl mx-auto p-6 space-y-6">

       <div className="h-10 w-64 bg-gray-200 rounded animate-pulse"></div>

       <div className="bg-white rounded-xl shadow-md border p-6">

         <div className="h-6 w-56 bg-gray-200 rounded animate-pulse mb-6"></div>

         <div className="grid md:grid-cols-4 gap-6">

           {[1, 2, 3, 4].map((item) => (
             <div key={item}>
               <div className="h-4 w-20 bg-gray-200 rounded animate-pulse mb-2"></div>
               <div className="h-5 w-32 bg-gray-200 rounded animate-pulse"></div>
             </div>
           ))}

         </div>

       </div>

       <div className="bg-white rounded-xl shadow-md border p-6">

         <div className="h-6 w-48 bg-gray-200 rounded animate-pulse mb-6"></div>

         {[1, 2, 3].map((row) => (
           <div
             key={row}
             className="grid grid-cols-6 gap-4 py-4 border-b"
           >
             {[1, 2, 3, 4, 5, 6].map((col) => (
               <div
                 key={col}
                 className="h-4 bg-gray-200 rounded animate-pulse"
               />
             ))}
           </div>
         ))}

       </div>

     </div>
   );
 }
  return (
    <div className="p-6 max-w-7xl mx-auto">

   <div className="flex items-center justify-between mb-8">

     <div>

       <h1 className="text-3xl font-bold text-gray-800">
         My Subscription
       </h1>

       <p className="text-gray-500 mt-1">
         View your current plan and subscription history.
       </p>

     </div>

   </div>

      {error && (
        <div className="mb-6 rounded-lg border border-red-200 bg-red-50 p-4 text-red-700">
          {error}
        </div>
      )}

      {currentSubscription ? (
        <div className="bg-white rounded-xl shadow-md border p-6 mb-8">

          <div className="flex justify-between items-start flex-wrap gap-4">

            <div>

              <h2 className="text-xl font-bold text-gray-800">
              <div className="flex items-center gap-3">

                <div className="text-4xl">
                  👑
                </div>

                <div>

                  <h2 className="text-2xl font-bold text-gray-800">
                    {currentSubscription.planName}
                  </h2>

                  <p className="text-gray-500">
                    Premium Membership
                  </p>

                </div>

              </div>
              </h2>

              <span
                className={`inline-block mt-3 px-3 py-1 rounded-full text-sm font-semibold ${getStatusBadge(
                  currentSubscription.status
                )}`}
              >
                {currentSubscription.status}
              </span>

            </div>

          {currentSubscription &&
           currentSubscription.status !== "ACTIVE" && (
            <button
              onClick={() => navigate("/upgrade")}
              className="px-5 py-2 rounded-lg bg-blue-600 hover:bg-blue-700 text-white transition"
            >
              Renew Plan
            </button>
          )}
          </div>

          <div className="grid md:grid-cols-4 gap-6 mt-6">

            <div>
              <p className="text-gray-500 text-sm">Start Date</p>
              <h3 className="font-semibold">
                {formatDate(currentSubscription.startDate)}
              </h3>
            </div>

            <div>
              <p className="text-gray-500 text-sm">End Date</p>
              <h3 className="font-semibold">
                {formatDate(currentSubscription.endDate)}
              </h3>
            </div>

            <div>
              <p className="text-gray-500 text-sm">Days Remaining</p>
 <h3
   className={`font-semibold ${
     calculateDaysRemaining(currentSubscription.endDate) <= 7
       ? "text-red-600"
       : "text-green-600"
   }`}
 >
                {calculateDaysRemaining(currentSubscription.endDate)} Days
              </h3>
            </div>

          <div>
            <p className="text-gray-500 text-sm">Plan Status</p>
            <h3 className="font-semibold">
              {currentSubscription.status}
            </h3>
          </div>

          </div>

        </div>
      ) : (
        <div className="bg-white rounded-xl shadow-md border p-8 text-center">

       <div className="bg-white rounded-xl shadow-md border p-10 text-center">

         <div className="text-6xl mb-4">
           💎
         </div>

         <h2 className="text-2xl font-bold mb-3">
           No Active Subscription
         </h2>

         <p className="text-gray-500 mb-6">
           Upgrade to Premium and unlock all exclusive features.
         </p>

        <button
          onClick={() => navigate("/upgrade")}
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg transition-colors"
        >
          Upgrade Now
        </button>

       </div>


        </div>
      )}

      {/* Subscription History Table will be added in Part 2 */}
<div className="bg-white rounded-xl shadow-md border">

  <div className="px-6 py-5 border-b">
    <h2 className="text-xl font-bold text-gray-800">
      Subscription History
    </h2>
  </div>

  {subscriptionHistory.length === 0 ? (

    <div className="p-8 text-center text-gray-500">
      No subscription history found.
    </div>

  ) : (

    <div className="overflow-x-auto">

      <table className="min-w-full">

        <thead className="bg-gray-50">

          <tr>

            <th className="px-6 py-3 text-left text-xs font-semibold uppercase text-gray-600">
              Plan
            </th>

            <th className="px-6 py-3 text-left text-xs font-semibold uppercase text-gray-600">
              Start Date
            </th>

            <th className="px-6 py-3 text-left text-xs font-semibold uppercase text-gray-600">
              End Date
            </th>

            <th className="px-6 py-3 text-left text-xs font-semibold uppercase text-gray-600">
              Status
            </th>

            <th className="px-6 py-3 text-left text-xs font-semibold uppercase text-gray-600">
              Active
            </th>

            <th className="px-6 py-3 text-left text-xs font-semibold uppercase text-gray-600">
              Remaining
            </th>

          </tr>

        </thead>

        <tbody className="divide-y divide-gray-200">

          {subscriptionHistory.map((subscription) => (

            <tr
              key={subscription.id}
className="hover:bg-blue-50 transition duration-200"
            >

              <td className="px-6 py-4">
                <div className="font-semibold text-gray-800">
                  {subscription.planName}
                </div>
              </td>

              <td className="px-6 py-4 text-gray-600">
                {formatDate(subscription.startDate)}
              </td>

              <td className="px-6 py-4 text-gray-600">
                {formatDate(subscription.endDate)}
              </td>

              <td className="px-6 py-4">

                <span
                  className={`px-3 py-1 rounded-full text-xs font-semibold ${getStatusBadge(
                    subscription.status
                  )}`}
                >
                  {subscription.status}
                </span>

              </td>

              <td className="px-6 py-4">

                {subscription.isActive ? (
                  <span className="text-green-600 font-semibold">
                    Yes
                  </span>
                ) : (
                  <span className="text-red-500 font-semibold">
                    No
                  </span>
                )}

              </td>

              <td className="px-6 py-4 text-gray-600">

                {subscription.isActive
                  ? `${calculateDaysRemaining(subscription.endDate)} Days`
                  : "-"}

              </td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>

  )}

</div>
    </div>
  );
};

export default SubscriptionHistory;