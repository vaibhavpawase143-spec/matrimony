import { useEffect, useRef, useState } from "react";
import {
  FaEllipsisV,
  FaCheckCircle,
  FaTimesCircle,
  FaBan,
  FaEye,
} from "react-icons/fa";

export default function SubscriptionActionMenu({
  subscription,
  onView,
  onActivate,
  onExpire,
  onCancel,
}) {
  const [open, setOpen] = useState(false);
  const menuRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <div className="relative" ref={menuRef}>
      <button
        onClick={() => setOpen(!open)}
        className="p-2 rounded hover:bg-gray-100"
      >
        <FaEllipsisV />
      </button>

      {open && (
        <div className="absolute right-0 mt-2 w-52 bg-white rounded-lg shadow-lg border z-50">

          <button
            onClick={() => {
              setOpen(false);
              onView?.(subscription);
            }}
            className="w-full flex items-center gap-3 px-4 py-3 hover:bg-gray-50"
          >
            <FaEye className="text-violet-600" />
            View Details
          </button>

{["EXPIRED", "CANCELLED"].includes(subscription.status) && (
            <button
              onClick={() => {
                setOpen(false);
                onActivate?.(subscription);
              }}
              className="w-full flex items-center gap-3 px-4 py-3 hover:bg-green-50"
            >
              <FaCheckCircle className="text-green-600" />
              Activate
            </button>
          )}

          {subscription.status === "ACTIVE" && (
            <>
              <button
                onClick={() => {
                  setOpen(false);
                  onExpire?.(subscription);
                }}
                className="w-full flex items-center gap-3 px-4 py-3 hover:bg-yellow-50"
              >
                <FaTimesCircle className="text-yellow-600" />
                Expire
              </button>

              <button
                onClick={() => {
                  setOpen(false);
                  onCancel?.(subscription);
                }}
                className="w-full flex items-center gap-3 px-4 py-3 hover:bg-red-50"
              >
                <FaBan className="text-red-600" />
                Cancel
              </button>
            </>
          )}
        </div>
      )}
    </div>
  );
}