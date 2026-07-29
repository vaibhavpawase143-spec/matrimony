import {
  FaCheckCircle,
  FaTimesCircle,
  FaBan,
  FaLockOpen,
  FaEnvelope,
  FaMobileAlt,
  FaUndo,
  FaTrashAlt,
} from "react-icons/fa";

export default function UserQuickActions({
  user,
  onActivate,
  onDeactivate,
  onBlock,
  onUnblock,
  onVerifyEmail,
  onVerifyPhone,
  onRestore,
  onSoftDelete,
  onDelete,
}) {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200">
      {/* Header */}
      <div className="border-b border-gray-100 px-6 py-4">
        <h2 className="text-lg font-semibold text-gray-800">
          Quick Actions
        </h2>
      </div>

      {/* Actions */}
      <div className="p-6 flex flex-wrap gap-3">

        {/* Activate / Deactivate */}
        {!user?.active ? (
          <button
            onClick={onActivate}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-green-600 hover:bg-green-700 text-white transition"
          >
            <FaCheckCircle />
            Activate
          </button>
        ) : (
          <button
            onClick={onDeactivate}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-yellow-500 hover:bg-yellow-600 text-white transition"
          >
            <FaTimesCircle />
            Deactivate
          </button>
        )}

        {/* Block / Unblock */}
        {!user?.blocked ? (
          <button
            onClick={onBlock}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-red-600 hover:bg-red-700 text-white transition"
          >
            <FaBan />
            Block
          </button>
        ) : (
          <button
            onClick={onUnblock}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-green-600 hover:bg-green-700 text-white transition"
          >
            <FaLockOpen />
            Unblock
          </button>
        )}

        {/* Verify Email */}
        {!user?.emailVerified && (
          <button
            onClick={onVerifyEmail}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-blue-600 hover:bg-blue-700 text-white transition"
          >
            <FaEnvelope />
            Verify Email
          </button>
        )}

        {/* Verify Phone */}
        {!user?.phoneVerified && (
          <button
            onClick={onVerifyPhone}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-700 text-white transition"
          >
            <FaMobileAlt />
            Verify Phone
          </button>
        )}

        {/* Restore / Soft Delete */}
        {user?.deleted ? (
          <button
            onClick={onRestore}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-emerald-600 hover:bg-emerald-700 text-white transition"
          >
            <FaUndo />
            Restore
          </button>
        ) : (
          <button
            onClick={onSoftDelete}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-orange-600 hover:bg-orange-700 text-white transition"
          >
            <FaTrashAlt />
            Soft Delete
          </button>
        )}

        {/* Permanent Delete */}
        <button
          onClick={onDelete}
          className="flex items-center gap-2 px-4 py-2 rounded-lg bg-red-800 hover:bg-red-900 text-white transition"
        >
          <FaTrashAlt />
          Delete Permanently
        </button>
      </div>
    </div>
  );
}