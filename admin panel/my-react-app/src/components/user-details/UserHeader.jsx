import StatusBadge from "./StatusBadge";
import { formatDate } from "../../utils/dateUtils";
export default function UserHeader({ user }) {
  const image =
    user?.imageUrl ||
    "https://ui-avatars.com/api/?name=User&background=7c3aed&color=fff&size=200";

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-6">
        {/* Left */}
        <div className="flex items-center gap-5">
          <img
            src={image}
            alt={user?.fullName}
            className="w-24 h-24 rounded-full object-cover border-4 border-purple-100"
          />

          <div>
            <h1 className="text-2xl font-bold text-gray-900">
              {user?.fullName}
            </h1>

        <p className="text-gray-500 mt-1">
          User ID : #{user?.id}
        </p>

        <p className="text-gray-500">
          {user?.email}
        </p>

        <p className="text-sm text-gray-400 mt-2">
          Member Since
        </p>

        <p className="font-medium text-gray-700">
          {formatDate(user?.createdAt)}
        </p>
          </div>
        </div>

        {/* Right */}
        <div className="flex flex-wrap gap-2">
 <StatusBadge
     label={
         user?.premium
             ? user?.premiumPlan || "Premium"
             : "Free Plan"
     }
     type={
         user?.premium
             ? "premium"
             : "default"
     }
 />

          <StatusBadge
            label={user?.active ? "Active" : "Inactive"}
            type={user?.active ? "success" : "danger"}
          />

          <StatusBadge
            label={user?.blocked ? "Blocked" : "Not Blocked"}
            type={user?.blocked ? "danger" : "success"}
          />

          <StatusBadge
            label={user?.online ? "Online" : "Offline"}
            type={user?.online ? "info" : "default"}
          />
        </div>
      </div>
    </div>
  );
}