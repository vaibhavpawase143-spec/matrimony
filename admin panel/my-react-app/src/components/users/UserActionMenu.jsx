import { useEffect, useRef, useState } from "react";
import {
  FaEllipsisV,
  FaEye,
  FaUserCheck,
  FaUserSlash,
  FaBan,
  FaUnlock,

  FaTrash,
  FaUndo,

} from "react-icons/fa";

export default function UserActionMenu({
  user,
  onView,
  onActivate,
  onDeactivate,
  onBlock,
  onUnblock,
  onVerifyEmail,
  onVerifyPhone,
  onRestore,
  onSoftDelete,

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

    document.addEventListener("mousedown", handleClickOutside);

    return () =>
      document.removeEventListener(
        "mousedown",
        handleClickOutside
      );
  }, []);

  const closeMenu = (callback) => {
    setOpen(false);
    callback?.();
  };

  return (
    <div
      className="relative inline-block text-left"
      ref={menuRef}
    >
      <button
        onClick={() => setOpen(!open)}
        className="p-2 rounded-lg hover:bg-gray-100"
      >
        <FaEllipsisV />
      </button>

      {open && (
        <div className="absolute right-0 mt-2 w-56 bg-white border rounded-xl shadow-lg z-50">

          <button
            onClick={() => closeMenu(() => onView(user))}
            className="w-full flex items-center gap-3 px-4 py-3 hover:bg-gray-100"
          >
            <FaEye />
            View Profile
          </button>

          <hr />

          {!user.active && (
            <button
              onClick={() =>
                closeMenu(() => onActivate(user))
              }
              className="w-full flex items-center gap-3 px-4 py-3 hover:bg-green-50 text-green-700"
            >
              <FaUserCheck />
              Activate
            </button>
          )}

          {user.active && (
            <button
              onClick={() =>
                closeMenu(() => onDeactivate(user))
              }
              className="w-full flex items-center gap-3 px-4 py-3 hover:bg-yellow-50 text-yellow-700"
            >
              <FaUserSlash />
              Deactivate
            </button>
          )}

          {!user.blocked ? (
            <button
              onClick={() =>
                closeMenu(() => onBlock(user))
              }
              className="w-full flex items-center gap-3 px-4 py-3 hover:bg-red-50 text-red-700"
            >
              <FaBan />
              Block
            </button>
          ) : (
            <button
              onClick={() =>
                closeMenu(() => onUnblock(user))
              }
              className="w-full flex items-center gap-3 px-4 py-3 hover:bg-green-50 text-green-700"
            >
              <FaUnlock />
              Unblock
            </button>
          )}


          <hr />

          <button
            onClick={() =>
              closeMenu(() => onRestore(user))
            }
            className="w-full flex items-center gap-3 px-4 py-3 hover:bg-blue-50 text-blue-700"
          >
            <FaUndo />
            Restore
          </button>

          <button
            onClick={() =>
              closeMenu(() => onSoftDelete(user))
            }
            className="w-full flex items-center gap-3 px-4 py-3 hover:bg-orange-50 text-orange-700"
          >
            <FaTrash />
            Soft Delete
          </button>


        </div>
      )}
    </div>
  );
}