import { Loader2, X } from "lucide-react";

export default function AdminStatusModal({
  open,
  onClose,
  onConfirm,
  loading,
  admin,
}) {
  if (!open) return null;

  const activating = !admin?.isActive;

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 px-4"
      onClick={() => {
        if (!loading) onClose();
      }}
    >
      <div
        className="w-full max-w-md rounded-xl bg-white shadow-2xl"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div className="flex items-center justify-between border-b px-6 py-4">
          <h2 className="text-xl font-bold text-gray-800">
            {activating ? "Activate Admin" : "Deactivate Admin"}
          </h2>

          <button
            type="button"
            disabled={loading}
            onClick={onClose}
            className="rounded-lg p-2 hover:bg-gray-100"
          >
            <X size={20} />
          </button>
        </div>

        {/* Body */}
        <div className="px-6 py-6">
          <p className="text-gray-600">
            {activating
              ? `Are you sure you want to activate ${admin?.name}?`
              : `Are you sure you want to deactivate ${admin?.name}?`}
          </p>
        </div>

        {/* Footer */}
        <div className="flex justify-end gap-3 border-t px-6 py-4">
          <button
            type="button"
            disabled={loading}
            onClick={onClose}
            className="rounded-lg border border-gray-300 px-5 py-2 hover:bg-gray-100"
          >
            Cancel
          </button>

          <button
            type="button"
            disabled={loading}
            onClick={onConfirm}
            className={`flex min-w-[150px] items-center justify-center rounded-lg px-5 py-2 text-white ${
              activating
                ? "bg-green-600 hover:bg-green-700"
                : "bg-amber-500 hover:bg-amber-600"
            }`}
          >
            {loading ? (
              <>
                <Loader2
                  size={18}
                  className="mr-2 animate-spin"
                />
                Processing...
              </>
            ) : activating ? (
              "Activate"
            ) : (
              "Deactivate"
            )}
          </button>
        </div>
      </div>
    </div>
  );
}