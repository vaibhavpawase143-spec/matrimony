import { useEffect, useState } from "react";
import {
  NavLink,
  useLocation,
  useNavigate,
} from "react-router-dom";

import { ADMIN_PERMISSIONS } from "../../config/adminPermissions";

import {
  FaTachometerAlt,
  FaUsers,
  FaMoneyBillWave,
  FaCrown,
  FaArrowLeft,
  FaBell,
  FaFlag,
  FaHeadset,
  FaFileAlt,
  FaQuestionCircle,
  FaDatabase,
  FaClipboardList,
  FaSignOutAlt,
  FaChevronDown,
  FaChevronRight,
} from "react-icons/fa";

export default function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation();

  // Logged in admin
  const admin = JSON.parse(localStorage.getItem("admin"));
  const role = admin?.role || "ROLE_ADMIN";
  const permissions = ADMIN_PERMISSIONS[role] || {};

  // ==========================================
  // SUPPORT CENTER
  // ==========================================

  const supportRoutes = [
    "/reported-profiles",
    "/support-tickets",
    "/faqs",
  ];

  const isSupportRoute = supportRoutes.some(
    (route) =>
      location.pathname === route ||
      location.pathname.startsWith(`${route}/`)
  );

  const [supportOpen, setSupportOpen] =
    useState(isSupportRoute);

  useEffect(() => {
    if (isSupportRoute) {
      setSupportOpen(true);
    }
  }, [isSupportRoute]);

  // ==========================================
  // MAIN MENU
  // ==========================================

  const mainMenus = [
    {
      name: "Dashboard",
      icon: <FaTachometerAlt />,
      path: "/dashboard",
      permission: "dashboard",
    },
    {
      name: "Users",
      icon: <FaUsers />,
      path: "/users",
      permission: "users",
    },
    {
      name: "Payments",
      icon: <FaMoneyBillWave />,
      path: "/payments",
      permission: "payments",
    },
    {
      name: "Subscriptions",
      icon: <FaCrown />,
      path: "/subscriptions",
      permission: "subscriptions",
    },
    {
      name: "Notifications",
      icon: <FaBell />,
      path: "/notifications",
      permission: "notifications",
    },
  ];

  // ==========================================
  // SUPPORT MENUS
  // ==========================================

  const supportMenus = [
    {
      name: "Reported Profiles",
      icon: <FaFlag />,
      path: "/reported-profiles",
      permission: "reportedProfiles",
    },
    {
      name: "Support Tickets",
      icon: <FaHeadset />,
      path: "/support-tickets",
      permission: "supportTickets",
    },
    {
      name: "FAQ",
      icon: <FaQuestionCircle />,
      path: "/faqs",
      permission: "faqs",
    },
  ];

  // ==========================================
  // BOTTOM MENUS
  // ==========================================

  const bottomMenus = [
    {
      name: "CMS",
      icon: <FaFileAlt />,
      path: "/cms-pages",
      permission: "cmsPages",
    },
    {
      name: "Master Data",
      icon: <FaDatabase />,
      path: "/master-data",
      permission: "masterData",
    },
    {
      name: "Audit Logs",
      icon: <FaClipboardList />,
      path: "/audit-logs",
      permission: "auditLogs",
    },
  ];

  const visibleSupportMenus = supportMenus.filter(
    (menu) => permissions[menu.permission]
  );

  const getMenuClass = ({ isActive }) => {
    return `
      flex
      items-center
      gap-4
      px-5
      py-3
      rounded-xl
      transition-all
      duration-300
      ${
        isActive
          ? "bg-white text-violet-900 font-semibold shadow-lg"
          : "text-white hover:bg-violet-700 hover:translate-x-2"
      }
    `;
  };

  const handleLogout = () => {
    localStorage.removeItem("adminToken");
    localStorage.removeItem("authToken");
    localStorage.removeItem("admin");
    localStorage.removeItem("user");

    navigate("/");
  };

  return (
      <aside className="w-72 h-full bg-gradient-to-b from-violet-950 via-violet-900 to-purple-800 text-white flex flex-col shadow-2xl">

        {/* ==========================================
            LOGO
        ========================================== */}

        <div className="p-4 sm:p-6 border-b border-violet-700">

          <h1 className="text-2xl sm:text-3xl font-extrabold tracking-wide">
            💜 Gathbandhan
          </h1>

          <p className="text-xs sm:text-sm text-violet-200 mt-1">
            Matrimony Admin Panel
          </p>

          <div className="mt-3">

            <button
              type="button"
              onClick={() => navigate(-1)}
              className="flex items-center gap-2 text-xs sm:text-sm bg-purple-200 text-purple-900 hover:bg-purple-300 px-3 py-2 rounded-md transition"
            >
              <FaArrowLeft />
              Back
            </button>

          </div>

        </div>

        {/* ==========================================
            NAVIGATION
        ========================================== */}

        <nav className="flex-1 overflow-y-auto p-4 space-y-2">

          {/* Main Menu */}

          {mainMenus
            .filter(menu => permissions[menu.permission])
            .map(menu => (

              <NavLink
                key={menu.name}
                to={menu.path}
                className={getMenuClass}
              >

                <span className="text-lg">
                  {menu.icon}
                </span>

                <span>
                  {menu.name}
                </span>

              </NavLink>

            ))}

          {/* Support Center */}

          {visibleSupportMenus.length > 0 && (

            <div>

              <button
                type="button"
                onClick={() => setSupportOpen(!supportOpen)}
                className={`
                  w-full
                  flex
                  items-center
                  justify-between
                  gap-4
                  px-5
                  py-3
                  rounded-xl
                  transition-all
                  duration-300

                  ${
                    isSupportRoute
                      ? "bg-white text-violet-900 font-semibold shadow-lg"
                      : "text-white hover:bg-violet-700"
                  }
                `}
              >

                <div className="flex items-center gap-4">

                  <span className="text-lg">
                    <FaHeadset />
                  </span>

                  <span>
                    Support Center
                  </span>

                </div>

                <span>

                  {supportOpen
                    ? <FaChevronDown />
                    : <FaChevronRight />
                  }

                </span>

              </button>

              <div
                className={`
                  overflow-hidden
                  transition-all
                  duration-300

                  ${
                    supportOpen
                      ? "max-h-72 opacity-100 mt-2"
                      : "max-h-0 opacity-0"
                  }
                `}
              >

                <div className="ml-5 pl-4 border-l border-violet-600 space-y-1">

                  {visibleSupportMenus.map(menu => (

                    <NavLink
                      key={menu.name}
                      to={menu.path}
                      className={({ isActive }) => `
                        flex
                        items-center
                        gap-3
                        px-4
                        py-2.5
                        rounded-lg
                        text-sm
                        transition-all

                        ${
                          isActive
                            ? "bg-violet-200 text-violet-950 font-semibold"
                            : "text-violet-100 hover:bg-violet-700 hover:text-white"
                        }
                      `}
                    >

                      <span>
                        {menu.icon}
                      </span>

                      <span>
                        {menu.name}
                      </span>

                    </NavLink>

                  ))}

                </div>

              </div>

            </div>

          )}

          {/* Bottom Menu */}

          {bottomMenus
            .filter(menu => permissions[menu.permission])
            .map(menu => (

              <NavLink
                key={menu.name}
                to={menu.path}
                className={getMenuClass}
              >

                <span className="text-lg">
                  {menu.icon}
                </span>

                <span>
                  {menu.name}
                </span>

              </NavLink>

            ))}

        </nav>
              {/* ==========================================
                  LOGOUT
              ========================================== */}

              <div className="p-4 border-t border-violet-700">

                <button
                  type="button"
                  onClick={handleLogout}
                  className="
                    w-full
                    bg-violet-700
                    text-white
                    py-3
                    rounded-xl
                    font-semibold
                    hover:bg-violet-600
                    transition-all
                    duration-300
                    shadow-lg
                    flex
                    justify-center
                    items-center
                    gap-2
                  "
                >
                  <FaSignOutAlt />
                  Logout
                </button>

              </div>

            </aside>
          );
        }
