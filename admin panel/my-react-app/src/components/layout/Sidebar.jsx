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
  FaUserShield,
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
  name: "Admin Management",
  icon: <FaUserShield />,
  path: "/admin-management",
  permission: "adminManagement",
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
      gap-3.5
      px-4
      py-3
      rounded-xl
      text-sm
      font-medium
      transition-all
      duration-200
      cursor-pointer
      ${
        isActive
          ? "bg-gradient-to-r from-violet-600 to-indigo-600 text-white shadow-md shadow-violet-900/30 font-semibold"
          : "text-slate-300 hover:bg-slate-800/80 hover:text-white hover:translate-x-1"
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
      <aside className="w-72 h-full bg-slate-900 border-r border-slate-800/80 text-slate-100 flex flex-col shadow-xl select-none">

        {/* ==========================================
            LOGO
        ========================================== */}

        <div className="p-5 border-b border-slate-800/80 flex items-center justify-between">

          <div>
            <h1 className="text-2xl font-black tracking-tight bg-gradient-to-r from-violet-400 via-purple-300 to-indigo-300 bg-clip-text text-transparent">
              💜 Gathbandhan
            </h1>

            <p className="text-xs text-slate-400 font-medium mt-0.5 tracking-wider uppercase">
              Admin Portal
            </p>
          </div>

          <button
            type="button"
            onClick={() => navigate(-1)}
            title="Go Back"
            className="flex items-center justify-center w-8 h-8 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300 hover:text-white transition-all cursor-pointer border border-slate-700/60"
          >
            <FaArrowLeft className="text-xs" />
          </button>

        </div>

        {/* ==========================================
            NAVIGATION
        ========================================== */}

        <nav className="flex-1 overflow-y-auto p-4 space-y-1.5 scrollbar-thin scrollbar-thumb-slate-800">

          {/* Main Menu */}

          {mainMenus
            .filter(menu => permissions[menu.permission])
            .map(menu => (

              <NavLink
                key={menu.name}
                to={menu.path}
                className={getMenuClass}
              >

                <span className="text-base">
                  {menu.icon}
                </span>

                <span>
                  {menu.name}
                </span>

              </NavLink>

            ))}

          {/* Support Center */}

          {visibleSupportMenus.length > 0 && (

            <div className="pt-2">

              <button
                type="button"
                onClick={() => setSupportOpen(!supportOpen)}
                className={`
                  w-full
                  flex
                  items-center
                  justify-between
                  gap-3.5
                  px-4
                  py-3
                  rounded-xl
                  text-sm
                  font-medium
                  transition-all
                  duration-200
                  cursor-pointer

                  ${
                    isSupportRoute
                      ? "bg-slate-800 text-violet-400 font-semibold border border-slate-700/60"
                      : "text-slate-300 hover:bg-slate-800/80 hover:text-white"
                  }
                `}
              >

                <div className="flex items-center gap-3.5">

                  <span className="text-base">
                    <FaHeadset />
                  </span>

                  <span>
                    Support Center
                  </span>

                </div>

                <span className="text-xs text-slate-400">

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
                      ? "max-h-72 opacity-100 mt-1"
                      : "max-h-0 opacity-0"
                  }
                `}
              >

                <div className="ml-5 pl-3 border-l border-slate-800 space-y-1 my-1">

                  {visibleSupportMenus.map(menu => (

                    <NavLink
                      key={menu.name}
                      to={menu.path}
                      className={({ isActive }) => `
                        flex
                        items-center
                        gap-3
                        px-3.5
                        py-2
                        rounded-lg
                        text-xs
                        font-medium
                        transition-all
                        cursor-pointer

                        ${
                          isActive
                            ? "bg-violet-600/20 text-violet-300 font-semibold border border-violet-500/30"
                            : "text-slate-400 hover:bg-slate-800/60 hover:text-slate-200"
                        }
                      `}
                    >

                      <span className="text-sm">
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

                <span className="text-base">
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

        <div className="p-4 border-t border-slate-800/80 bg-slate-900/50">

          <button
            type="button"
            onClick={handleLogout}
            className="
              w-full
              bg-slate-800/80
              hover:bg-rose-600
              text-slate-300
              hover:text-white
              border
              border-slate-700/60
              hover:border-rose-600
              py-2.5
              rounded-xl
              text-sm
              font-semibold
              transition-all
              duration-200
              shadow-sm
              flex
              justify-center
              items-center
              gap-2.5
              cursor-pointer
            "
          >
            <FaSignOutAlt className="text-sm" />
            Logout
          </button>

        </div>

      </aside>
          );
        }
