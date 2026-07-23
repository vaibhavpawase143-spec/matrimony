import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { FaArrowLeft } from "react-icons/fa";
import { getSubscriptionById } from "../services/subscriptionService";
import { toast } from "sonner";

export default function SubscriptionDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [subscription, setSubscription] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadSubscription();
  }, [id]);

  const loadSubscription = async () => {
    try {
      setLoading(true);

      const data = await getSubscriptionById(id);

      console.log("Subscription API Response:", data);

      setSubscription(data);
    } catch (err) {
      console.error(err);
      toast.error("Failed to load subscription details.");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="p-6 text-center text-gray-500">
        Loading subscription details...
      </div>
    );
  }

  if (!subscription) {
    return (
      <div className="p-6 text-center text-red-500">
        Subscription not found.
      </div>
    );
  }

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

          <h1 className="text-3xl font-bold">Subscription Details</h1>

          <p className="text-gray-500">
            View subscription information.
          </p>
        </div>

        <span
          className={`px-4 py-2 rounded-full text-sm font-semibold ${
            subscription.status === "ACTIVE"
              ? "bg-green-100 text-green-700"
              : subscription.status === "EXPIRED"
              ? "bg-yellow-100 text-yellow-700"
              : "bg-red-100 text-red-700"
          }`}
        >
          {subscription.status}
        </span>
      </div>

      {/* Details Card */}
      <div className="bg-white rounded-2xl shadow-md border border-purple-100 p-6">

        {/* User Information */}
        <div className="flex flex-col md:flex-row gap-6 items-center border-b pb-6 mb-6">

          <img
            src={
              subscription.imageUrl ||
              "https://placehold.co/150x150?text=No+Photo"
            }
            alt={subscription.userName}
            className="w-32 h-32 rounded-full object-cover border-4 border-violet-200"
          />

          <div className="space-y-2 flex-1">

            <h2 className="text-2xl font-bold">
              {subscription.userName}
            </h2>

            <p>
              <span className="font-semibold">User ID:</span>{" "}
              {subscription.userId}
            </p>

            <p>
              <span className="font-semibold">Email:</span>{" "}
              {subscription.email || "-"}
            </p>

            <p>
              <span className="font-semibold">Phone:</span>{" "}
              {subscription.phone || "-"}
            </p>

            <p>
              <span className="font-semibold">Gender:</span>{" "}
              {subscription.gender || "-"}
            </p>

            <p>
              <span className="font-semibold">Date of Birth:</span>{" "}
              {subscription.dateOfBirth || "-"}
            </p>

          </div>

        </div>

        {/* Subscription Information */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

          <div>
            <p className="text-sm text-gray-500">Plan Name</p>
            <p className="font-semibold">{subscription.planName}</p>
          </div>

          <div>
            <p className="text-sm text-gray-500">Plan ID</p>
            <p className="font-semibold">{subscription.planId}</p>
          </div>

          <div>
            <p className="text-sm text-gray-500">Active</p>
            <p className="font-semibold">
              {subscription.isActive ? "Yes" : "No"}
            </p>
          </div>

          <div>
            <p className="text-sm text-gray-500">Status</p>
            <p className="font-semibold">{subscription.status}</p>
          </div>

          <div>
            <p className="text-sm text-gray-500">Start Date</p>
            <p className="font-semibold">
              {new Date(subscription.startDate).toLocaleString()}
            </p>
          </div>

          <div>
            <p className="text-sm text-gray-500">End Date</p>
            <p className="font-semibold">
              {new Date(subscription.endDate).toLocaleString()}
            </p>
          </div>

          <div>
            <p className="text-sm text-gray-500">Created At</p>
            <p className="font-semibold">
              {new Date(subscription.createdAt).toLocaleString()}
            </p>
          </div>

          <div>
            <p className="text-sm text-gray-500">Updated At</p>
            <p className="font-semibold">
              {new Date(subscription.updatedAt).toLocaleString()}
            </p>
          </div>

        </div>

      </div>
    </div>
  );
}