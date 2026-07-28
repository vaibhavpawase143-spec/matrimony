import { useEffect, useState } from "react";
import { FaSearch } from "react-icons/fa";
import { getAllAdmins, } from "../services/adminManagementService";
import AdminActionMenu from "../components/admin/AdminActionMenu";
import EditAdminModal from "../components/admin/EditAdminModal";
import { useNavigate } from "react-router-dom";

export default function AdminManagement() {
  // ==========================================
  // STATES
  // ==========================================

  const [admins, setAdmins] = useState([]);
const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
const handleViewProfile = (admin) => {
  navigate(`/admin-management/${admin.id}`);
};
  const [error, setError] = useState("");

  const [search, setSearch] = useState("");
const [selectedAdmin, setSelectedAdmin] = useState(null);

const [editOpen, setEditOpen] = useState(false);
  const [page, setPage] = useState(0);

  const [size, setSize] = useState(10);

  const [totalPages, setTotalPages] = useState(0);

  const [totalAdmins, setTotalAdmins] = useState(0);

  // ==========================================
  // LOAD ADMINS
  // ==========================================

  const loadAdmins = async (
    currentPage = page,
    currentSearch = search
  ) => {
    try {
      setLoading(true);

      const response = await getAllAdmins(
        currentPage,
        size,
        currentSearch
      );

      const pageData = response.data;

      setAdmins(pageData.content || []);

      setTotalPages(pageData.totalPages || 0);

      setTotalAdmins(pageData.totalElements || 0);

      setError("");
    } catch (err) {
      console.error(err);

      setError(err.message || "Failed to load admins.");
    } finally {
      setLoading(false);
    }
  };
const handleEdit = (admin) => {
  setSelectedAdmin(admin);
  setEditOpen(true);
};

const handleChangeRole = (admin) => {
  console.log("Change Role:", admin);
};

const handleActivate = (admin) => {
  console.log("Activate:", admin);
};

const handleDeactivate = (admin) => {
  console.log("Deactivate:", admin);
};

const handleResetPassword = (admin) => {
  console.log("Reset Password:", admin);
};

  // ==========================================
  // INITIAL LOAD
  // ==========================================

  useEffect(() => {
    loadAdmins(page, search);
  }, [page, size]);

  // ==========================================
  // SEARCH (Debounce)
  // ==========================================

  useEffect(() => {
    const timer = setTimeout(() => {
      setPage(0);

      loadAdmins(0, search);
    }, 500);

    return () => clearTimeout(timer);
  }, [search]);

  // ==========================================
  // LOADING
  // ==========================================

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-lg font-medium text-gray-600">
          Loading admins...
        </div>
      </div>
    );
  }

  // ==========================================
  // ERROR
  // ==========================================

  if (error) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-red-600 font-medium">
          {error}
        </div>
      </div>
    );
  }
  // ==========================================
  // JSX
  // ==========================================

  return (
    <div className="p-6">

      {/* ================= HEADER ================= */}

      <div className="flex justify-between items-center mb-6">

        <div>
          <h1 className="text-3xl font-bold text-gray-800">
            Admin Management
          </h1>

          <p className="text-gray-500">
            Manage administrator accounts and roles.
          </p>
        </div>

      </div>

      {/* ================= SEARCH ================= */}

      <div className="bg-white shadow-md rounded-xl p-4 mb-6 flex items-center gap-3 border border-gray-200">

        <FaSearch className="text-gray-400" />

        <input
          type="text"
          placeholder="Search by Name or Email..."
          className="w-full outline-none text-sm text-gray-700 placeholder-gray-400"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

      </div>

      {/* ================= TABLE ================= */}

      <div className="bg-white rounded-2xl shadow-md overflow-visible border border-purple-100">

        <table className="w-full">

          <thead className="bg-gradient-to-r from-violet-700 to-purple-600 text-white">

            <tr>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                ID
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Name
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Email
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Phone
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Role
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Status
              </th>

              <th className="px-6 py-4 text-center text-sm font-semibold">
                Actions
              </th>

            </tr>

          </thead>

          <tbody>

            {admins.length === 0 ? (

              <tr>

                <td
                  colSpan="7"
                  className="py-12 text-center text-gray-500"
                >
                  No admins found.
                </td>

              </tr>

            ) : (
              admins.map((admin, index) => (
                            <tr
                              key={admin.id}
                              className={`transition ${
                                index % 2 === 0
                                  ? "bg-white"
                                  : "bg-gray-50"
                              } hover:bg-purple-50`}
                            >
                              {/* ID */}
                              <td className="px-6 py-4 text-sm text-gray-900">
                                {admin.id}
                              </td>

                              {/* Name */}
                              <td className="px-6 py-4">
                                <div className="font-medium text-gray-900">
                                  {admin.name}
                                </div>
                              </td>

                              {/* Email */}
                              <td className="px-6 py-4 text-sm text-gray-600">
                                {admin.email}
                              </td>

                              {/* Phone */}
                              <td className="px-6 py-4 text-sm text-gray-600">
                                {admin.phone || "-"}
                              </td>

                              {/* Role */}
                              <td className="px-6 py-4">
                                <span
                                  className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${
                                    admin.role === "ROLE_SUPER_ADMIN"
                                      ? "bg-red-100 text-red-700"
                                      : "bg-blue-100 text-blue-700"
                                  }`}
                                >
                                  {admin.role === "ROLE_SUPER_ADMIN"
                                    ? "Super Admin"
                                    : "Admin"}
                                </span>
                              </td>

                              {/* Status */}
                              <td className="px-6 py-4">
                                <span
className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${
    admin.isActive
        ? "bg-green-100 text-green-700"
        : "bg-yellow-100 text-yellow-700"
}`}
>
    {admin.isActive ? "Active" : "Inactive"}
                                </span>
                              </td>

                              {/* Actions */}
<td className="px-6 py-4 text-center">
<AdminActionMenu
    admin={admin}
    onViewProfile={handleViewProfile}
/>
</td>
                            </tr>
                          ))
                        )}

                      </tbody>

                    </table>

                  </div>

                  {/* ================= FOOTER ================= */}

                  <div className="flex flex-col md:flex-row justify-between items-center mt-8 gap-4">

                    <div className="flex items-center gap-6">

                      <p className="text-sm text-gray-600 font-medium">
                        Total Admins :
                        <span className="ml-2 font-semibold text-gray-900">
                          {totalAdmins}
                        </span>
                      </p>

                      <div className="flex items-center gap-2">

                        <label className="text-sm font-medium text-gray-600">
                          Show
                        </label>

                        <select
                          value={size}
                          onChange={(e) => {
                            setSize(Number(e.target.value));
                            setPage(0);
                          }}
                          className="border border-gray-300 rounded-lg px-3 py-2"
                        >
                          <option value={10}>10</option>
                          <option value={20}>20</option>
                          <option value={50}>50</option>
                          <option value={100}>100</option>
                        </select>

                      </div>

                    </div>

                    <div className="flex items-center gap-3">

                      <button
                        onClick={() => setPage((prev) => prev - 1)}
                        disabled={page === 0}
                        className={`px-4 py-2 rounded-lg text-sm font-medium transition ${
                          page === 0
                            ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                            : "bg-gray-100 hover:bg-gray-200 text-gray-700"
                        }`}
                      >
                        Previous
                      </button>

                      <div className="px-4 py-2 rounded-lg bg-gradient-to-r from-violet-700 to-purple-600 text-white text-sm font-semibold">
                        Page {page + 1} of {Math.max(totalPages, 1)}
                      </div>

                      <button
                        onClick={() => setPage((prev) => prev + 1)}
                        disabled={page + 1 >= totalPages}
                        className={`px-4 py-2 rounded-lg text-sm font-medium transition ${
                          page + 1 >= totalPages
                            ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                            : "bg-gray-100 hover:bg-gray-200 text-gray-700"
                        }`}
                      >
                        Next
                      </button>

                    </div>

                  </div>
<EditAdminModal
  open={editOpen}
  admin={selectedAdmin}
  onClose={() => setEditOpen(false)}
  onSuccess={() => {
    setEditOpen(false);
    loadAdmins();
  }}
/>
                </div>
              );
            }