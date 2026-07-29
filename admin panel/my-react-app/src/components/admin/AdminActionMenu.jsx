import { useEffect, useRef, useState } from "react";
import {
  FaEllipsisV,
  FaEye,

} from "react-icons/fa";

function AdminActionMenu({
  admin,
  onViewProfile,

}) {
  const [open, setOpen] = useState(false);
  const menuRef = useRef(null);

  useEffect(() => {
    function handleClickOutside(event) {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setOpen(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);

    return () =>
      document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div className="relative inline-block text-left" ref={menuRef}>
      <button
        onClick={() => setOpen(!open)}
        className="flex items-center gap-2 bg-violet-600 hover:bg-violet-700 text-white text-xs font-medium px-3 py-2 rounded-lg transition"
      >
        <FaEllipsisV size={12} />
        Manage
      </button>

      {open && (
        <div className="absolute right-0 mt-2 w-56 bg-white border rounded-xl shadow-xl z-50 overflow-hidden">
          <button
            onClick={() => {
              setOpen(false);
              onViewProfile(admin);
            }}
            className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-gray-100"
          >
            <FaEye className="text-indigo-600" />
            View Profile
          </button>


        </div>
      )}
    </div>
  );
}

export default AdminActionMenu;