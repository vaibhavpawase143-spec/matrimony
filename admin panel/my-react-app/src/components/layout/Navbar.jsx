import { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { logoutAdmin } from "../../services/authService";
import { trackEvent } from "../../utils/analytics";
import {
  FaBars,
  FaUser,
  FaLock,
  FaSignOutAlt,
  FaBell,
  FaTimes,
  FaCheckDouble,
  FaExclamationTriangle,
  FaTicketAlt,
  FaUserPlus,
} from "react-icons/fa";
import {
  getUnreadCount,
  getNotificationHistory,
  markAsRead,
  markAllAsRead,
  deleteNotification,
} from "../../services/notificationService";
import {
  connectAdminNotifications,
  disconnectAdminNotifications,
} from "../../services/adminNotificationSocket";
import ChangePasswordModal from "../profile/ChangePasswordModal";
import { getAdminProfile } from "../../services/adminProfileService";

export default function Navbar({ onMenuToggle, sidebarOpen }) {
  const navigate = useNavigate();

  // ==========================================
  // State
  // ==========================================

  const [menuOpen, setMenuOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [profile, setProfile] = useState(null);
  const [showChangePassword, setShowChangePassword] =
    useState(false);
const [unreadCount, setUnreadCount] = useState(0);
  const menuRef = useRef(null);
const [notificationOpen, setNotificationOpen] = useState(false);
  // ==========================================
  // Toggle Dropdown
  // ==========================================
const [notifications, setNotifications] = useState([]);
const [notificationLoading, setNotificationLoading] = useState(false);
  const toggleMenu = () => {
    setMenuOpen((prev) => !prev);
  };

 const toggleNotification = async () => {
   const nextState = !notificationOpen;

   setNotificationOpen(nextState);

   if (nextState) {
     await loadNotifications();
   }
 };

  // ==========================================
  // Close Dropdown on Outside Click
  // ==========================================

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        menuRef.current &&
        !menuRef.current.contains(event.target)
      ) {
        setMenuOpen(false);
      }
    };

    document.addEventListener(
      "mousedown",
      handleClickOutside
    );

    return () => {
      document.removeEventListener(
        "mousedown",
        handleClickOutside
      );
    };
  }, []);

  // ==========================================
  // Load Admin Profile
  // ==========================================
const handleMarkAllRead = async () => {
  try {
    await markAllAsRead();

    setNotifications((prev) =>
      prev.map((n) => ({
        ...n,
        read: true,
      }))
    );

    setUnreadCount(0);
  } catch (error) {
    console.error(error);
  }
};
const handleDelete = async (id) => {
  try {
    await deleteNotification(id);

    setNotifications((prev) =>
      prev.filter((n) => n.id !== id)
    );

    await loadUnreadCount();
  } catch (error) {
    console.error(error);
  }
};

 useEffect(() => {
   const loadProfile = async () => {
     try {
       const response = await getAdminProfile();
       setProfile(response.data || response);
console.log("Admin Profile:", response.data || response);
       await loadUnreadCount();
     } catch (error) {
       console.error("Failed to load admin profile", error);
     } finally {
       setLoading(false);
     }
   };

   loadProfile();
 }, []);
useEffect(() => {
  if (!profile?.id) return;

connectAdminNotifications(profile.id, (notification) => {
    setNotifications((prev) => [notification, ...prev]);

    setUnreadCount((prev) => prev + 1);
});

  return () => {
    disconnectAdminNotifications();
  };
}, [profile]);
const loadNotifications = async () => {
  try {
    setNotificationLoading(true);

    const response = await getNotificationHistory("", 0, 5);

    setNotifications(response.data.content || []);
  } catch (error) {
    console.error("Failed to load notifications", error);
  } finally {
    setNotificationLoading(false);
  }
};
const handleNotificationClick = async (notification) => {
  try {
    if (!notification.read) {
      await markAsRead(notification.id);

      setNotifications((prev) =>
        prev.map((item) =>
          item.id === notification.id
            ? { ...item, read: true }
            : item
        )
      );

      setUnreadCount((prev) => Math.max(prev - 1, 0));
    }

    setNotificationOpen(false);

    switch (notification.type) {
      case "NEW_USER":
        navigate("/users");
        break;

      case "REPORT":
        navigate("/reported-profiles");
        break;

      case "SUPPORT":
        navigate("/support-tickets");
        break;

      case "SUBSCRIPTION":
        navigate("/subscriptions");
        break;

      case "WARNING":
        navigate("/users");
        break;

      case "SYSTEM":
      case "ANNOUNCEMENT":
      case "ADMIN":
      default:
        navigate("/notifications");
        break;
    }
  } catch (error) {
    console.error("Failed to open notification", error);
  }
};
  // ==========================================
  // Logout
  // ==========================================


const handleLogout = () => {
  // Google Analytics Event
  trackEvent("admin_logout");

  // Clear admin session
  logoutAdmin();

  // Remove old keys (only if they're still used elsewhere)
  localStorage.removeItem("authToken");
  localStorage.removeItem("user");

  navigate("/");
};

  // ==========================================
  // Navigation
  // ==========================================

  const goToProfile = () => {
    setMenuOpen(false);
    navigate("/profile");
  };

  const changePassword = () => {
    setMenuOpen(false);
    setShowChangePassword(true);
  };
// ==========================================
// Load Unread Notification Count
// ==========================================


  // ==========================================
  // Profile Image
  // ==========================================

  const profileImage =
    profile?.profilePhoto &&
    profile.profilePhoto.trim() !== ""
      ? profile.profilePhoto.startsWith("http")
        ? profile.profilePhoto
        : `https://localhost:9090${profile.profilePhoto}`
      : `https://ui-avatars.com/api/?background=7C3AED&color=fff&name=${encodeURIComponent(
          profile?.name || "Admin"
        )}`;
const loadUnreadCount = async () => {
  try {
    const response = await getUnreadCount();
    setUnreadCount(response.data || 0);
  } catch (error) {
    console.error("Failed to load unread notifications", error);
  }
};
  // ==========================================
  // Render
  // ==========================================

  return (
    <>
          <header className="sticky top-0 z-50 h-20 bg-purple-100 border-b border-purple-200 shadow-sm flex items-center justify-between px-4 md:px-8">

            {/* ==========================
                Left Section
            ========================== */}

            <div className="flex items-center gap-4">

              <button
                onClick={onMenuToggle}
                aria-expanded={sidebarOpen}
                className="md:hidden text-xl text-violet-900"
              >
                <FaBars />
              </button>

              <div>

                <p className="text-xs text-gray-500">
                  Welcome
                </p>

                <h2 className="text-2xl font-bold text-gray-800">

                  {loading
                    ? "Loading..."
                    : profile?.name || "Admin"}

                </h2>

              </div>

            </div>

            {/* ==========================
                Right Section
            ========================== */}

        <div className="flex items-center gap-4">
{/* Notification Bell */}

<div className="relative">
 <button
   onClick={toggleNotification}
   className="
     relative
     p-3
     rounded-full
     bg-white
     hover:bg-violet-50
     transition
     shadow-sm
   "
 >
    <FaBell className="text-xl text-violet-700" />

    {unreadCount > 0 && (
      <span
        className="
          absolute
          -top-1
          -right-1
          min-w-[20px]
          h-5
          px-1
          rounded-full
          bg-red-500
          text-white
          text-xs
          flex
          items-center
          justify-center
          font-bold
        "
      >
        {unreadCount > 99 ? "99+" : unreadCount}
      </span>
    )}

  </button>
  {notificationOpen && (
   <div
     className="
       absolute
       right-0
       mt-3
       w-[420px]
       bg-white
       rounded-2xl
       shadow-2xl
       border border-gray-200
       overflow-hidden
       z-50
   "
   >
<div className="flex items-center justify-between px-5 py-4 border-b border-gray-200 bg-white">

    <h3 className="text-lg font-semibold text-gray-900">
        Notifications
    </h3>

    <div className="flex items-center gap-3">

        <button
            onClick={handleMarkAllRead}
            className="flex items-center gap-1 text-sm font-medium text-violet-600 hover:text-violet-800"
        >
            <FaCheckDouble size={14} />
            Mark All Read
        </button>

        <button
            onClick={() => setNotificationOpen(false)}
            className="text-gray-500 hover:text-black"
        >
            <FaTimes />
        </button>

    </div>

</div>

 {notificationLoading ? (
   <div className="py-6 text-center text-gray-500">
     Loading...
   </div>
 ) : notifications.length === 0 ? (
   <div className="py-6 text-center text-gray-500">
     No notifications found.
   </div>
 ) : (
<div className="max-h-[420px] overflow-y-auto p-3 space-y-3">
     {notifications.map((notification) => (
    <div
        key={notification.id}
        onClick={() => handleNotificationClick(notification)}
        className={`
            flex
            justify-between
            items-start
            rounded-xl
            border
            p-4
            cursor-pointer
            transition-all
            duration-200
            hover:shadow-md
            ${
                notification.read
                    ? "bg-white border-gray-200"
                    : "bg-violet-50 border-violet-300"
            }
        `}
    >
<p
    className={`text-sm ${
        notification.read
            ? "font-medium text-gray-800"
            : "font-semibold text-violet-800"
    }`}
>
           {notification.title || notification.type}
         </p>

<p className="mt-2 text-sm text-gray-600 leading-6">
           {notification.message}
         </p>

         <p className="mt-3 text-xs text-gray-400">
           {new Date(notification.createdAt).toLocaleString()}
         </p>
       </div>
     ))}
   </div>
 )}
    </div>
  )}
</div>
             <div
               className="relative"
               ref={menuRef}
             >
              <button
                onClick={toggleMenu}
                className="
                  flex
                  items-center
                  gap-3
                  rounded-xl
                  px-3
                  py-2
                  hover:bg-white
                  transition-all
                  duration-300
                "
              >

                <img
                  src={profileImage}
                  alt="Admin"
                  className="
                    w-11
                    h-11
                    rounded-full
                    object-cover
                    border-2
                    border-violet-300
                  "
                />

                <div className="hidden md:block text-left">

                  <p className="font-semibold text-gray-800">

                    {loading
                      ? "Loading..."
                      : profile?.name || "Admin"}

                  </p>

                  <p className="text-sm text-gray-500">

                    {loading
                      ? ""
                      : profile?.role || "ADMIN"}

                  </p>

                </div>

                <svg
                  className={`w-4 h-4 transition-transform ${
                    menuOpen ? "rotate-180" : ""
                  }`}
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M19 9l-7 7-7-7"
                  />
                </svg>

              </button>

              {menuOpen && (

                <div className="absolute right-0 mt-3 w-80 bg-white rounded-2xl border border-gray-200 shadow-2xl overflow-hidden">

                  {/* Profile Header */}

                  <div className="bg-gradient-to-r from-violet-700 to-purple-700 px-6 py-5 text-white">

                    <div className="flex items-center gap-4">

                      <img
                        src={profileImage}
                        alt="Admin"
                        className="w-16 h-16 rounded-full border-4 border-white object-cover"
                      />

                      <div>

                        <h3 className="font-bold text-lg">

                          {loading
                            ? "Loading..."
                            : profile?.name || "Admin"}

                        </h3>

                        <p className="text-sm text-violet-100">

                          {loading
                            ? ""
                            : profile?.email || ""}

                        </p>

                        <span className="inline-block mt-2 rounded-full bg-white/20 px-3 py-1 text-xs">

                          {loading
                            ? ""
                            : profile?.role || "ADMIN"}

                        </span>

                      </div>

                    </div>

                  </div>

                  {/* Menu */}

                  <div className="py-2">

                    <button
                      onClick={goToProfile}
                      className="flex w-full items-center gap-3 px-5 py-3 hover:bg-violet-50 transition"
                    >
                      <FaUser className="text-violet-700" />

                      <span>
                        Profile Details
                      </span>
                    </button>

                    <button
                      onClick={changePassword}
                      className="flex w-full items-center gap-3 px-5 py-3 hover:bg-violet-50 transition"
                    >
                      <FaLock className="text-violet-700" />

                      <span>
                        Change Password
                      </span>
                    </button>

                    <hr className="my-2" />

                    <button
                      onClick={handleLogout}
                      className="flex w-full items-center gap-3 px-5 py-3 text-red-600 hover:bg-red-50 transition"
                    >
                      <FaSignOutAlt />

                      <span>
                        Logout
                      </span>
                    </button>

                  </div>

                  <div className="border-t bg-gray-50 px-5 py-3 text-center text-xs text-gray-400">

                    Gathbandhan Matrimony Admin Panel

                  </div>

                </div>

              )}
                  </div>
</div>
                </header>

                {/* ==========================
                    Change Password Modal
                ========================== */}

                <ChangePasswordModal
                  open={showChangePassword}
                  onClose={() => setShowChangePassword(false)}
                />

              </>
            );
          }