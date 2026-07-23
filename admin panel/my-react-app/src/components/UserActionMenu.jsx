import { useEffect, useRef, useState } from "react";
import {
  FaEllipsisV,
  FaCheckCircle,
  FaTimesCircle,
  FaBan,
  FaEye,
  FaMoneyBillWave,
  FaHistory,
} from "react-icons/fa";

export default function SubscriptionActionMenu({
  subscription,
  onView,
  onActivate,
  onExpire,
  onCancel,
  onRefund,
  onHistory,
}) {
  const [open, setOpen] = useState(false);

  const menuRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        menuRef.current &&
        !menuRef.current.contains(event.target)
      ) {
        setOpen(false);
      }
    };

    document.addEventListener(
      "mousedown",
      handleClickOutside
    );

    return () =>
      document.removeEventListener(
        "mousedown",
        handleClickOutside
      );
  }, []);
  return (
    <div className="relative inline-block text-left" ref={menuRef}>
      <button
        onClick={() => setOpen(!open)}
        className="p-2 rounded-lg hover:bg-gray-100 transition"
      >
        <FaEllipsisV className="text-gray-600" />
      </button>

      {open && (
        <div className="absolute right-0 mt-2 w-56 rounded-xl bg-white shadow-xl border border-gray-200 z-50 overflow-hidden">

          {/* View */}
          <button
            onClick={() => {
              setOpen(false);
              onView?.(subscription);
            }}
            className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-gray-50"
          >
            <FaEye className="text-violet-600" />
            View Details
          </button>

          <div className="border-t" />

          {/* Activate */}
          {subscription.status !== "ACTIVE" && (
            <button
              onClick={() => {
                setOpen(false);
                onActivate?.(subscription);
              }}
              className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-green-50"
            >
              <FaCheckCircle className="text-green-600" />
              Activate
            </button>
          )}

          {/* Expire */}
          {subscription.status === "ACTIVE" && (
            <button
              onClick={() => {
                setOpen(false);
                onExpire?.(subscription);
              }}
              className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-yellow-50"
            >
              <FaTimesCircle className="text-yellow-600" />
              Expire
            </button>
          )}

          {/* Cancel */}
          {subscription.status === "ACTIVE" && (
            <button
              onClick={() => {
                setOpen(false);
                onCancel?.(subscription);
              }}
              className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-red-50"
            >
              <FaBan className="text-red-600" />
              Cancel
            </button>
          )}
                <div className="border-t" />

                {/* Refund */}
                {onRefund && (
                  <button
                    onClick={() => {
                      setOpen(false);
                      onRefund(subscription);
                    }}
                    className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-blue-50"
                  >
                    <FaMoneyBillWave className="text-blue-600" />
                    Refund
                  </button>
                )}

                {/* History */}
                {onHistory && (
                  <button
                    onClick={() => {
                      setOpen(false);
                      onHistory(subscription);
                    }}
                    className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-gray-50"
                  >
                    <FaHistory className="text-gray-600" />
                    View History
                  </button>
                )}
              </div>
            )}
          </div>
        );
      }