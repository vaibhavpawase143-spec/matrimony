import { useNavigate  } from "react-router-dom";
import { useEffect, useState } from "react";
import { FaSearch } from "react-icons/fa";
import { toast } from "sonner";
import ExportDropdown from "../components/common/ExportDropdown";
import { exportToPDF } from "../utils/export/pdfExport";
import { exportToCSV } from "../utils/export/csvExport";
import { exportToExcel } from "../utils/export/excelExport";
import {
  getUsers,
  activateUser,
  deactivateUser,
  blockUser,
  unblockUser,
  restoreUser,
  softDeleteUser,


  // Bulk Operations
  bulkActivateUsers,
  bulkBlockUsers,
  bulkUnblockUsers,
  bulkSoftDeleteUsers,
} from "../services/adminUserService";
import UserActionMenu from "../components/users/UserActionMenu";
import ConfirmModal from "../components/common/ConfirmModal";

export default function Users() {

  // ===========================
  // STATES
  // ===========================

  const [search, setSearch] = useState("");

  const [users, setUsers] = useState([]);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const [page, setPage] = useState(0);

  const [size,setSize] = useState(10);

  const [totalPages, setTotalPages] = useState(0);

  const [totalUsers, setTotalUsers] = useState(0);
const [selectedUser, setSelectedUser] = useState(null);
const [selectedUsers, setSelectedUsers] = useState([]);
const [selectedAction, setSelectedAction] = useState("");
const [isModalOpen, setIsModalOpen] = useState(false);
const [isBulkAction, setIsBulkAction] = useState(false);
const openConfirmModal = (user, action) => {
  setSelectedUser(user);
  setSelectedAction(action);
  setIsModalOpen(true);
};
const navigate = useNavigate();
  // ===========================
  // LOAD USERS
  // ===========================
// ===========================
// HANDLE USER ACTION
// ===========================

const handleConfirmAction = async () => {
  if (!selectedUser) return;

  try {
    switch (selectedAction) {
      case "activate":
        await activateUser(selectedUser.id);
        break;

      case "deactivate":
        await deactivateUser(selectedUser.id);
        break;

      case "block":
        await blockUser(selectedUser.id);
        break;

      case "unblock":
        await unblockUser(selectedUser.id);
        break;



      case "restore":
        await restoreUser(selectedUser.id);
        break;

      case "softDelete":
        await softDeleteUser(selectedUser.id);
        break;



      default:
        return;
    }

    // Reload latest data
    await loadUsers(page, search);

    // Close modal
    setIsModalOpen(false);
    setSelectedUser(null);
    setSelectedAction("");

  toast.success("Action completed successfully.");
  } catch (err) {
    console.error(err);
const message =
  err?.response?.data?.message ||
  err?.message ||
  "Something went wrong.";

toast.error(message);
  }
};
  const loadUsers = async (
    currentPage = page,
    currentSearch = search
  ) => {

    try {

      setLoading(true);

      const response = await getUsers(
        currentPage,
        size,
        currentSearch
      );

      const pageData = response.data;

      setUsers(pageData.content || []);

      setTotalPages(pageData.totalPages || 0);

      setTotalUsers(pageData.totalElements || 0);

      setError("");

    } catch (err) {

      console.error(err);

      setError(err.message || "Failed to load users.");

    } finally {

      setLoading(false);

    }
  };
const userColumns = [
  {
    label: "ID",
    value: (user) => user.id,
  },
  {
    label: "Name",
    value: (user) => user.fullName,
  },
  {
    label: "Email",
    value: (user) => user.email,
  },
  {
    label: "Phone",
    value: (user) => user.phone || "-",
  },
  {
    label: "Gender",
    value: (user) => user.gender || "-",
  },
  {
    label: "Plan",
    value: (user) =>
      user.premium
        ? user.premiumPlan || "Premium"
        : "Free Plan",
  },
  {
    label: "Status",
    value: (user) =>
      user.blocked
        ? "Blocked"
        : user.active
        ? "Active"
        : "Inactive",
  },
  {
    label: "City",
    value: (user) => user.city || "-",
  },
];
const handleExportCSV = () => {
  exportToCSV({
    data: users,
    columns: userColumns,
    fileName: "Users_Report",
  });
};

const handleExportExcel = () => {
  exportToExcel({
    data: users,
    columns: userColumns,
    fileName: "Users_Report",
  });
};
// ===========================
// HANDLE BULK ACTION
// ===========================

const handleBulkAction = async (action) => {

  if (selectedUsers.length === 0) {
    toast.error("Please select at least one user.");
    return;
  }

  try {

    switch (action) {

      case "activate":
        await bulkActivateUsers(selectedUsers);
        toast.success("Selected users activated successfully.");
        break;

      case "block":
        await bulkBlockUsers(selectedUsers);
        toast.success("Selected users blocked successfully.");
        break;

      case "unblock":
        await bulkUnblockUsers(selectedUsers);
        toast.success("Selected users unblocked successfully.");
        break;

      case "softDelete":
        await bulkSoftDeleteUsers(selectedUsers);
        toast.success("Selected users soft deleted successfully.");
        break;

      default:
        return;
    }

    // Clear selected checkboxes
    setSelectedUsers([]);
setIsModalOpen(false);
setIsBulkAction(false);
setSelectedAction("");
    // Reload current page
    await loadUsers(page, search);

  } catch (err) {

    console.error("Bulk action failed:", err);

    toast.error(
      err?.message ||
      "Bulk operation failed."
    );
  }
};
const handleModalConfirm = async () => {

  // Bulk action
  if (isBulkAction) {
    await handleBulkAction(selectedAction);

    setIsModalOpen(false);
    setIsBulkAction(false);
    setSelectedAction("");

    return;
  }

  // Normal single-user action
  await handleConfirmAction();
};
  // ===========================
  // INITIAL LOAD
  // ===========================

  useEffect(() => {

    loadUsers(page, search);

  }, [page,size]);

  // ===========================
  // SEARCH (Debounce)
  // ===========================

  useEffect(() => {

    const timer = setTimeout(() => {

      setPage(0);

      loadUsers(0, search);

    }, 500);

    return () => clearTimeout(timer);

  }, [search]);

  // ===========================
  // BLOCK USER
  // ===========================



  // ===========================
  // LOADING
  // ===========================

  if (loading) {

    return (

      <div className="flex items-center justify-center h-96">

        <div className="text-lg font-medium text-gray-600">

          Loading users...

        </div>

      </div>

    );

  }

  // ===========================
  // ERROR
  // ===========================

  if (error) {

    return (

      <div className="flex items-center justify-center h-96">

        <div className="text-red-600 font-medium">

          {error}

        </div>

      </div>

    );

  }

  // ===========================
  // JSX STARTS HERE
  // ===========================

  return (
    <div className="p-6">

      {/* ================= HEADER ================= */}
<div className="flex justify-between items-center mb-6">

    <div>
        <h1 className="text-3xl font-bold text-gray-800">
            User Management
        </h1>

        <p className="text-gray-500">
            Manage all registered users.
        </p>
    </div>

   <ExportDropdown
onPDF={() =>
  exportToPDF({
    data: users,
    columns: userColumns,
    title: "User Management Report",
    fileName: "Users_Report",
  })
}
       onExcel={handleExportExcel}
       onCSV={handleExportCSV}
   />

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
          {/* ================= BULK ACTION TOOLBAR ================= */}

          {selectedUsers.length > 0 && (
            <div className="bg-violet-50 border border-violet-200 rounded-xl p-4 mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">

              <div className="text-sm font-semibold text-violet-800">
                {selectedUsers.length}{" "}
                {selectedUsers.length === 1 ? "user" : "users"} selected
              </div>

              <div className="flex flex-wrap items-center gap-2">

                <button
                  type="button"
                   onClick={() => handleBulkAction("activate")}
                  className="px-4 py-2 rounded-lg bg-green-600 text-white text-sm font-medium hover:bg-green-700 transition"
                >
                  Activate
                </button>

                <button
                  type="button"
                  onClick={() => handleBulkAction("block")}
                  className="px-4 py-2 rounded-lg bg-red-600 text-white text-sm font-medium hover:bg-red-700 transition"
                >
                  Block
                </button>

                <button
                  type="button"
                  onClick={() => handleBulkAction("unblock")}
                  className="px-4 py-2 rounded-lg bg-blue-600 text-white text-sm font-medium hover:bg-blue-700 transition"
                >
                  Unblock
                </button>

               <button
                 type="button"
                 onClick={() => {
                   setSelectedAction("softDelete");
                   setIsBulkAction(true);
                   setIsModalOpen(true);
                 }}
                 className="px-4 py-2 rounded-lg bg-orange-600 text-white text-sm font-medium hover:bg-orange-700 transition"
               >
                 Soft Delete
               </button>

                <button
                  type="button"
                  onClick={() => setSelectedUsers([])}
                  className="px-4 py-2 rounded-lg border border-gray-300 bg-white text-gray-700 text-sm font-medium hover:bg-gray-50 transition"
                >
                  Clear
                </button>

              </div>
            </div>
          )}

        <table className="w-full">

          <thead className="bg-gradient-to-r from-violet-700 to-purple-600 text-white">

            <tr>

         <th className="px-4 py-4 text-center">
           <input
             type="checkbox"
             checked={
               users.length > 0 &&
               selectedUsers.length === users.length
             }
             onChange={(e) => {
               if (e.target.checked) {
                 setSelectedUsers(users.map((user) => user.id));
               } else {
                 setSelectedUsers([]);
               }
             }}
             className="w-4 h-4 cursor-pointer accent-violet-600"
           />
         </th>
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
                Gender
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Plan
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Status
              </th>

              <th className="px-6 py-4 text-center text-sm font-semibold">
                Actions
              </th>

            </tr>

          </thead>

          <tbody className="divide-y divide-gray-200">

            {users.length === 0 ? (

              <tr>

                <td
                  colSpan="9"
                  className="text-center py-12 text-gray-500"
                >

                  No users found.

                </td>

              </tr>

            ) : (

             users.map((user, index) => (

               <tr
                 key={user.id}
                 className={`transition ${
                   index % 2 === 0
                     ? "bg-white"
                     : "bg-gray-50"
                 } hover:bg-purple-50`}
               >

                 <td className="px-4 py-4 text-center">
                   <input
                     type="checkbox"
                     checked={selectedUsers.includes(user.id)}
                     onChange={(e) => {
                       if (e.target.checked) {
                         setSelectedUsers((prev) => [...prev, user.id]);
                       } else {
                         setSelectedUsers((prev) =>
                           prev.filter((id) => id !== user.id)
                         );
                       }
                     }}
                     className="w-4 h-4 cursor-pointer accent-violet-600"
                   />
                 </td>

                 {/* ID */}
                 <td className="px-6 py-4 text-sm text-gray-900">
                   {user.id}
                 </td>

                 {/* Name */}
                  {/* ID */}


                  {/* Name */}

                  <td className="px-6 py-4">

                    <div className="flex items-center gap-3">

                      <img
                        src={
                          user.imageUrl ||
                          "https://ui-avatars.com/api/?name=" +
                            encodeURIComponent(
                              user.fullName || "User"
                            )
                        }
                        alt={user.fullName}
                        className="w-10 h-10 rounded-full object-cover border"
                      />

                      <div>

                        <div className="font-medium text-gray-900">

                          {user.fullName}

                        </div>

                        <div className="text-xs text-gray-500">

                          {user.city || "-"}

                        </div>

                      </div>

                    </div>

                  </td>

                  {/* Email */}

                  <td className="px-6 py-4 text-sm text-gray-600">

                    {user.email}

                  </td>

                  {/* Phone */}

                  <td className="px-6 py-4 text-sm text-gray-600">

                    {user.phone || "-"}

                  </td>

                  {/* Gender */}

                  <td className="px-6 py-4 text-sm text-gray-600">

                    {user.gender || "-"}

                  </td>

                {/* Plan */}
                <td className="px-6 py-4">
                  {user.premium ? (
                    <span className="inline-flex items-center whitespace-nowrap bg-violet-100 text-violet-700 px-3 py-1.5 rounded-full text-xs font-semibold">
                      {user.premiumPlan || "Premium"}
                    </span>
                  ) : (
                    <span className="inline-flex items-center whitespace-nowrap bg-gray-100 text-gray-600 px-3 py-1.5 rounded-full text-xs font-medium">
                      Free Plan
                    </span>
                  )}
                </td>
                  {/* Status */}

                  <td className="px-6 py-4">

                    <span
                      className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${
                        user.blocked
                          ? "bg-red-100 text-red-700"
                          : user.active
                          ? "bg-green-100 text-green-700"
                          : "bg-yellow-100 text-yellow-700"
                      }`}
                    >

                      {user.blocked
                        ? "Blocked"
                        : user.active
                        ? "Active"
                        : "Inactive"}

                    </span>

                  </td>

                  {/* Actions */}

                  <td className="px-6 py-4">

                    <div className="flex justify-center gap-3">

                   <UserActionMenu
                       user={user}
                       onView={(user) => navigate(`/users/${user.id}`)}
                       onActivate={(user) => openConfirmModal(user, "activate")}
                       onDeactivate={(user) => openConfirmModal(user, "deactivate")}
                       onBlock={(user) => openConfirmModal(user, "block")}
                       onUnblock={(user) => openConfirmModal(user, "unblock")}

                       onRestore={(user) => openConfirmModal(user, "restore")}
                       onSoftDelete={(user) => openConfirmModal(user, "softDelete")}

                   />
                    </div>

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
    Total Users :
    <span className="ml-2 font-semibold text-gray-900">
      {totalUsers}
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

                {/* Previous */}

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

                {/* Current Page */}

                <div className="px-4 py-2 rounded-lg bg-gradient-to-r from-violet-700 to-purple-600 text-white text-sm font-semibold">

                  Page {page + 1} of {Math.max(totalPages, 1)}

                </div>

                {/* Next */}

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
{/* ================= CONFIRM MODAL ================= */}

<ConfirmModal
  isOpen={isModalOpen}
  title="Confirm Action"
 message={
   isBulkAction
     ? `Are you sure you want to soft delete ${selectedUsers.length} ${
         selectedUsers.length === 1 ? "user" : "users"
       }? This action will remove the selected users from active records.`
     : selectedUser
     ? `Are you sure you want to ${selectedAction} "${selectedUser.fullName}"?`
     : ""
 }
  confirmText="Yes"
  cancelText="Cancel"
 confirmButtonClass={
     selectedAction === "softDelete"
         ? "bg-orange-600 hover:bg-orange-700"
         : "bg-violet-600 hover:bg-violet-700"
 }
onConfirm={() => {
  if (isBulkAction) {
    handleBulkAction("softDelete");
  } else {
    handleConfirmAction();
  }
}}
  onCancel={() => {
    setIsModalOpen(false);
    setSelectedUser(null);
    setSelectedAction("");
  }}
/>
          </div>

        );

      }