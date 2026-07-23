import { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  FaBars,
  FaUser,
  FaLock,
  FaSignOutAlt,
} from "react-icons/fa";

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

  const menuRef = useRef(null);

  // ==========================================
  // Toggle Dropdown
  // ==========================================

  const toggleMenu = () => {
    setMenuOpen((prev) => !prev);
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

  useEffect(() => {
    const loadProfile = async () => {
      try {
        const response = await getAdminProfile();

        // Supports both:
        // {success:true,data:{...}}
        // and direct profile object
        setProfile(response.data || response);
      } catch (error) {
        console.error(
          "Failed to load admin profile",
          error
        );
      } finally {
        setLoading(false);
      }
    };

    loadProfile();
  }, []);

  // ==========================================
  // Logout
  // ==========================================

  const handleLogout = () => {
    localStorage.removeItem("adminToken");
    localStorage.removeItem("authToken");
    localStorage.removeItem("admin");
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
  // Profile Image
  // ==========================================

  const profileImage =
    profile?.profilePhoto &&
    profile.profilePhoto.trim() !== ""
      ? profile.profilePhoto.startsWith("http")
        ? profile.profilePhoto
        : `http://localhost:9090${profile.profilePhoto}`
      : `https://ui-avatars.com/api/?background=7C3AED&color=fff&name=${encodeURIComponent(
          profile?.name || "Admin"
        )}`;

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