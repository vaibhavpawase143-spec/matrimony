import { FaExclamationTriangle, FaRedo } from "react-icons/fa";

export default function AdminErrorAlert({
  title = "Admin Request Error",
  error,
  onRetry,
  className = "",
}) {
  const message =
    typeof error === "string"
      ? error
      : error?.message || "Failed to complete the requested administrative operation.";

  return (
    <div
      className={`bg-rose-50/90 border border-rose-200/80 rounded-2xl p-5 sm:p-6 shadow-xs ${className}`}
    >
      <div className="flex items-start gap-4">
        <div className="p-3 bg-rose-100 text-rose-600 rounded-xl flex-shrink-0">
          <FaExclamationTriangle className="text-xl" />
        </div>

        <div className="flex-1">
          <h3 className="text-base font-semibold text-rose-900 tracking-tight">
            {title}
          </h3>
          <p className="text-sm text-rose-700/90 mt-1 leading-relaxed">
            {message}
          </p>

          {error?.status ? (
            <span className="inline-block mt-2 px-2.5 py-0.5 text-xs font-mono font-medium text-rose-800 bg-rose-200/60 rounded-md border border-rose-300/40">
              HTTP Code: {error.status}
            </span>
          ) : null}

          {onRetry && (
            <div className="mt-4">
              <button
                type="button"
                onClick={onRetry}
                className="inline-flex items-center gap-2 px-3.5 py-2 text-xs font-semibold text-rose-700 bg-white hover:bg-rose-100/70 border border-rose-300/80 rounded-xl shadow-xs transition-all cursor-pointer hover:shadow-sm"
              >
                <FaRedo className="text-xs" />
                <span>Retry Operation</span>
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
