import {
  FaUserShield,
  FaEnvelope,
  FaPhone,
  FaUser,
  FaCalendarAlt,
  FaClock,
} from "react-icons/fa";

export default function ProfileCard({
  profile,
  onEdit,
  onChangePassword,
}) {
  return (
    <div className="bg-white rounded-2xl shadow-md border border-purple-100 overflow-hidden">

      {/* Header */}
      <div className="bg-gradient-to-r from-violet-700 to-purple-600 h-28" />

      {/* Avatar */}
      <div className="flex flex-col items-center -mt-14 px-6 pb-6">

        <img
          src={
            profile.profilePhoto ||
            `https://ui-avatars.com/api/?name=${encodeURIComponent(
              profile.name || "Admin"
            )}&background=7C3AED&color=fff`
          }
          alt={profile.name}
          className="w-28 h-28 rounded-full border-4 border-white object-cover shadow-lg"
        />

        <h2 className="mt-4 text-2xl font-bold text-gray-800">
          {profile.name}
        </h2>

        <span className="mt-2 px-4 py-1 rounded-full bg-violet-100 text-violet-700 text-sm font-semibold">
          {profile.role}
        </span>

      </div>

      <div className="border-t border-gray-200" />

      {/* Information */}
      <div className="grid md:grid-cols-2 gap-6 p-6">

        <InfoItem
          icon={<FaUser />}
          label="Username"
          value={profile.username}
        />

        <InfoItem
          icon={<FaEnvelope />}
          label="Email"
          value={profile.email}
        />

        <InfoItem
          icon={<FaPhone />}
          label="Phone"
          value={profile.phone || "-"}
        />

        <InfoItem
          icon={<FaUserShield />}
          label="Status"
          value={profile.isActive ? "Active" : "Inactive"}
        />

        <InfoItem
          icon={<FaClock />}
          label="Last Login"
          value={
            profile.lastLogin
              ? new Date(profile.lastLogin).toLocaleString()
              : "-"
          }
        />

        <InfoItem
          icon={<FaCalendarAlt />}
          label="Created"
          value={
            profile.createdAt
              ? new Date(profile.createdAt).toLocaleDateString()
              : "-"
          }
        />

      </div>

      <div className="border-t border-gray-200 p-6 flex flex-wrap gap-3 justify-end">

        <button
          onClick={onEdit}
          className="px-5 py-2.5 rounded-lg bg-violet-600 hover:bg-violet-700 text-white font-medium transition"
        >
          Edit Profile
        </button>

        <button
          onClick={onChangePassword}
          className="px-5 py-2.5 rounded-lg bg-gray-800 hover:bg-black text-white font-medium transition"
        >
          Change Password
        </button>

      </div>

    </div>
  );
}

function InfoItem({ icon, label, value }) {
  return (
    <div className="flex items-start gap-4">
      <div className="text-violet-600 text-lg mt-1">
        {icon}
      </div>

      <div>
        <p className="text-sm text-gray-500">
          {label}
        </p>

        <p className="font-semibold text-gray-800 break-all">
          {value}
        </p>
      </div>
    </div>
  );
}