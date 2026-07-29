import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import { FaArrowLeft } from "react-icons/fa";
import { getAdminById,deleteAdmin, } from "../services/adminManagementService";
import { IMAGE_BASE_URL } from "../services/api";
import EditAdminModal from "../components/admin/EditAdminModal";
import ChangeRoleModal from "../components/admin/ChangeRoleModal";
import AdminStatusModal from "../components/admin/AdminStatusModal";
import ResetPasswordModal from "../components/admin/ResetPasswordModal";

import {
  resetAdminPassword,
} from "../services/adminManagementService";
import {
  activateAdmin,
  deactivateAdmin,
} from "../services/adminManagementService";
export default function AdminDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
const [editOpen, setEditOpen] = useState(false);
const [roleOpen, setRoleOpen] = useState(false);
const [statusLoading, setStatusLoading] = useState(false);


const [statusOpen, setStatusOpen] = useState(false);

const [passwordOpen, setPasswordOpen] = useState(false);
const [passwordLoading, setPasswordLoading] = useState(false);
const [deleteOpen, setDeleteOpen] = useState(false);
const [deleteLoading, setDeleteLoading] = useState(false);
  const [admin, setAdmin] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadAdmin = async () => {
    try {
      setLoading(true);

      const response = await getAdminById(id);

      setAdmin(response.data);
      console.log("Admin:", response.data);
      setError("");
    } catch (err) {
      console.error(err);

      setError(err.message || "Failed to load admin.");

      toast.error(err.message || "Failed to load admin.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAdmin();
  }, [id]);
const handleStatusChange = async () => {
  try {
    setStatusLoading(true);

    if (admin.isActive) {
      await deactivateAdmin(admin.id);
      toast.success("Admin deactivated successfully.");
    } else {
      await activateAdmin(admin.id);
      toast.success("Admin activated successfully.");
    }

    setStatusOpen(false);

    await loadAdmin();
  } catch (error) {
    console.error(error);

    toast.error(
      error?.message || "Failed to update admin status."
    );
  } finally {
    setStatusLoading(false);
  }
};
const handleResetPassword = async (data) => {
  try {
    setPasswordLoading(true);

    await resetAdminPassword(admin.id, data);

    toast.success("Password reset successfully.");

    setPasswordOpen(false);
  } catch (error) {
    console.error(error);

    toast.error(
      error?.message || "Failed to reset password."
    );
  } finally {
    setPasswordLoading(false);
  }
};
const handleDelete = async () => {
  const confirmed = window.confirm(
    `Are you sure you want to delete "${admin.name}"?`
  );

  if (!confirmed) return;

  try {
    setDeleteLoading(true);

    await deleteAdmin(admin.id);

    toast.success("Admin deleted successfully.");

    navigate("/admin-management");
  } catch (error) {
    console.error(error);

    toast.error(
      error?.message || "Failed to delete admin."
    );
  } finally {
    setDeleteLoading(false);
  }
};
  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-lg font-medium text-gray-600">
          Loading admin details...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-red-600 font-medium">
          {error}
        </div>
      </div>
    );
  }

 return (
   <div className="p-6">
 {/* Back Button */}
    <button
      onClick={() => navigate(-1)}
      className="flex items-center gap-2 mb-6 text-violet-600 hover:text-violet-800 font-medium"
    >
      <FaArrowLeft />
      Back to Admin Management
    </button>
     {/* Header */}
     <div className="mb-6">
       <h1 className="text-3xl font-bold text-gray-800">
         Admin Details
       </h1>

       <p className="text-gray-500 mt-1">
         View administrator information.
       </p>
     </div>

     {/* Profile Card */}
     <div className="bg-white rounded-2xl shadow border border-gray-200 p-8">

       <div className="flex flex-col md:flex-row items-center md:items-start gap-8">
{/* ==========================================
   SUPER ADMIN ACTIONS
========================================== */}

<div className="mt-8 bg-white rounded-2xl shadow border border-gray-200 p-6">

    <h2 className="text-xl font-bold text-gray-800">
        Super Admin Actions
    </h2>

    <p className="text-sm text-gray-500 mt-1">
        Manage administrator permissions and account.
    </p>

    <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-4">

       <button
           onClick={() => setEditOpen(true)}
           className="rounded-lg bg-violet-600 text-white py-3 font-medium hover:bg-violet-700 transition"
       >
           Edit Profile
       </button>
       <button
           onClick={() => setRoleOpen(true)}
           className="rounded-lg bg-blue-600 text-white py-3 font-medium hover:bg-blue-700 transition"
       >
           Change Role
       </button>
        {admin.isActive ? (
           <button
               onClick={() => setStatusOpen(true)}
               className="rounded-lg bg-amber-500 text-white py-3 font-medium hover:bg-amber-600 transition"
           >
               Deactivate Account
           </button>
        ) : (
           <button
               onClick={() => setStatusOpen(true)}
               className="rounded-lg bg-green-600 text-white py-3 font-medium hover:bg-green-700 transition"
           >
               Activate Account
           </button>
        )}

       <button
           onClick={() => setPasswordOpen(true)}
           className="rounded-lg bg-red-600 text-white py-3 font-medium hover:bg-red-700 transition"
       >
           Reset Password
       </button>

    </div>

    <div className="border-t mt-8 pt-6">

        <h3 className="text-lg font-semibold text-red-600">
            Danger Zone
        </h3>

        <p className="text-sm text-gray-500 mt-1">
            Permanently deactivate this administrator using Soft Delete.
        </p>

        <button
            onClick={handleDelete}
            disabled={deleteLoading}
            className="mt-4 rounded-lg bg-red-600 text-white px-6 py-3 hover:bg-red-700 transition disabled:opacity-50"
        >
            {deleteLoading ? "Deleting..." : "Delete Admin"}
        </button>

    </div>

</div>
         {/* Profile Photo */}
       {/* Profile Photo */}
       <div className="flex-shrink-0">
         {admin.profilePhoto ? (
           <img
             src={
               admin.profilePhoto.startsWith("http")
                 ? admin.profilePhoto
                 : `${IMAGE_BASE_URL}${admin.profilePhoto}`
             }
             alt={admin.name}
             className="w-36 h-36 rounded-full object-cover border-4 border-violet-200"
           />
         ) : (
           <div className="w-36 h-36 rounded-full bg-violet-600 text-white flex items-center justify-center text-5xl font-bold">
             {admin.name?.charAt(0).toUpperCase()}
           </div>
         )}
       </div>
         {/* Profile Info */}
         <div className="flex-1">

           <div className="flex flex-wrap items-center gap-3">

             <h2 className="text-3xl font-bold text-gray-900">
               {admin.name}
             </h2>

             <span
               className={`px-3 py-1 rounded-full text-sm font-semibold ${
                 admin.isActive
                   ? "bg-green-100 text-green-700"
                   : "bg-red-100 text-red-700"
               }`}
             >
               {admin.isActive ? "Active" : "Inactive"}
             </span>

           </div>

           <p className="text-gray-500 mt-2">
             @{admin.username}
           </p>

           <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-6">

             <div>
               <p className="text-sm text-gray-500">Email</p>

               <p className="font-medium text-gray-900">
                 {admin.email}
               </p>
             </div>

             <div>
               <p className="text-sm text-gray-500">Phone</p>

               <p className="font-medium text-gray-900">
                 {admin.phone || "-"}
               </p>
             </div>

             <div>
               <p className="text-sm text-gray-500">Role</p>

               <p className="font-medium">
                 {admin.role === "ROLE_SUPER_ADMIN"
                   ? "Super Admin"
                   : "Admin"}
               </p>
             </div>

             <div>
               <p className="text-sm text-gray-500">
                 Last Login
               </p>

               <p className="font-medium">
                 {admin.lastLogin
                   ? new Date(admin.lastLogin).toLocaleString()
                   : "-"}
               </p>
             </div>

           </div>

         </div>

       </div>

     </div>
<EditAdminModal
    open={editOpen}
    admin={admin}
    onClose={() => setEditOpen(false)}
    onSuccess={() => {
        setEditOpen(false);
        loadAdmin();
    }}
/>
<ChangeRoleModal
    open={roleOpen}
    admin={admin}
    onClose={() => setRoleOpen(false)}
    onSuccess={() => {
        setRoleOpen(false);
        loadAdmin();
    }}
/>
<AdminStatusModal
    open={statusOpen}
    admin={admin}
    loading={statusLoading}
    onClose={() => setStatusOpen(false)}
    onConfirm={handleStatusChange}
/>
<ResetPasswordModal
    open={passwordOpen}
    loading={passwordLoading}
    onClose={() => setPasswordOpen(false)}
    onSubmit={handleResetPassword}
/>
   </div>
 );
}