import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { toast } from "sonner";
import { formatDate } from "../utils/dateUtils";
import UserGallery from "../components/user-details/UserGallery";
import {
  FaArrowLeft,
  FaEnvelope,
  FaPhone,
  FaUser,
  FaHeart,
  FaClock,
  FaCalendarAlt,
} from "react-icons/fa";

import {
  getUserById,
  activateUser,
  deactivateUser,
  blockUser,
  unblockUser,
  verifyEmail,
  verifyPhone,
  restoreUser,
  softDeleteUser,
  deleteUser,
} from "../services/adminUserService";

import UserHeader from "../components/user-details/UserHeader";
import UserInfoCard from "../components/user-details/UserInfoCard";
import UserQuickActions from "../components/user-details/UserQuickActions";
import StatusBadge from "../components/user-details/StatusBadge";
import LoadingSkeleton from "../components/user-details/LoadingSkeleton";

import ConfirmModal from "../components/common/ConfirmModal";

export default function UserDetails() {
  const { id } = useParams();

  const [user, setUser] = useState(null);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const [selectedAction, setSelectedAction] = useState("");

  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    loadUser();
  }, [id]);

  async function loadUser() {
    try {
      setLoading(true);
      setError("");

      const response = await getUserById(id);

      setUser(response.data);
    } catch (err) {
      setError(
        err?.message ||
          "Unable to load user details."
      );
    } finally {
      setLoading(false);
    }
  }

  function openConfirm(action) {
    setSelectedAction(action);
    setIsModalOpen(true);
  }

  async function handleConfirmAction() {
    try {
      switch (selectedAction) {
        case "activate":
          await activateUser(user.id);
          toast.success("User activated successfully.");
          break;

        case "deactivate":
          await deactivateUser(user.id);
          toast.success("User deactivated successfully.");
          break;

        case "block":
          await blockUser(user.id);
          toast.success("User blocked successfully.");
          break;

        case "unblock":
          await unblockUser(user.id);
          toast.success("User unblocked successfully.");
          break;

        case "verifyEmail":
          await verifyEmail(user.id);
          toast.success("Email verified successfully.");
          break;

        case "verifyPhone":
          await verifyPhone(user.id);
          toast.success("Phone verified successfully.");
          break;

        case "restore":
          await restoreUser(user.id);
          toast.success("User restored successfully.");
          break;

        case "softDelete":
          await softDeleteUser(user.id);
          toast.success("User soft deleted successfully.");
          break;



        default:
          break;
      }

      setIsModalOpen(false);

      setSelectedAction("");

      await loadUser();
    } catch (err) {
      toast.error(
        err?.message ||
          "Action failed."
      );
    }
  }

  if (loading) {
    return (
      <div className="p-6">
        <LoadingSkeleton />
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-6">

        <div className="bg-red-50 border border-red-200 rounded-xl p-8">

          <h2 className="text-2xl font-bold text-red-600">
            {error}
          </h2>

          <Link
            to="/users"
            className="inline-flex mt-5 text-purple-600 font-medium"
          >
            <FaArrowLeft className="mr-2 mt-1" />
            Back to Users
          </Link>

        </div>

      </div>
    );
  }
  return (
    <div className="p-6 space-y-6">

      {/* Back Button */}

      <Link
        to="/users"
        className="inline-flex items-center gap-2 text-purple-600 hover:text-purple-800 font-medium"
      >
        <FaArrowLeft />
        Back to Users
      </Link>

      {/* Header */}

      <UserHeader user={user} />

      {/* Information */}

      <div className="grid grid-cols-1 xl:grid-cols-2 gap-6">

        {/* Personal Information */}

        <UserInfoCard title="Personal Information">

          <div className="space-y-5">

            <div className="flex items-center gap-3">
              <FaEnvelope className="text-purple-600" />
              <div>
                <p className="text-sm text-gray-500">Email</p>
                <p className="font-medium">{user?.email || "-"}</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <FaPhone className="text-purple-600" />
              <div>
                <p className="text-sm text-gray-500">Phone</p>
                <p className="font-medium">{user?.phone || "-"}</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <FaUser className="text-purple-600" />
              <div>
                <p className="text-sm text-gray-500">Gender</p>
                <p className="font-medium">{user?.gender || "-"}</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <FaHeart className="text-purple-600" />
              <div>
                <p className="text-sm text-gray-500">Religion</p>
                <p className="font-medium">{user?.religion || "Not Available-"}</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <FaHeart className="text-purple-600" />
              <div>
                <p className="text-sm text-gray-500">Caste</p>
                <p className="font-medium">{user?.caste || "Not Available-"}</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <FaHeart className="text-purple-600" />
              <div>
                <p className="text-sm text-gray-500">City</p>
                <p className="font-medium">{user?.city || "-"}</p>
              </div>
            </div>

          </div>

        </UserInfoCard>

        {/* Verification */}

{/* Gallery */}

<UserGallery user={user} />
        {/* Account Status */}

        <UserInfoCard title="Account Status">

          <div className="space-y-5">

            <div className="flex justify-between">

              <span>Account</span>

              <StatusBadge
                label={
                  user?.active
                    ? "Active"
                    : "Inactive"
                }
                type={
                  user?.active
                    ? "success"
                    : "danger"
                }
              />

            </div>

            <div className="flex justify-between">

              <span>Blocked</span>

              <StatusBadge
                label={
                  user?.blocked
                    ? "Blocked"
                    : "No"
                }
                type={
                  user?.blocked
                    ? "danger"
                    : "success"
                }
              />

            </div>

            <div className="flex justify-between">

              <span>Deleted</span>

              <StatusBadge
                label={
                  user?.deleted
                    ? "Yes"
                    : "No"
                }
                type={
                  user?.deleted
                    ? "danger"
                    : "success"
                }
              />

            </div>

            <div className="flex justify-between">

              <span>Profile Completed</span>

              <StatusBadge
                label={
                  user?.profileCompleted
                    ? "Completed"
                    : "Incomplete"
                }
                type={
                  user?.profileCompleted
                    ? "success"
                    : "warning"
                }
              />

            </div>

          </div>

        </UserInfoCard>

        {/* Activity */}

        <UserInfoCard title="Activity">

          <div className="space-y-5">

            <div className="flex items-center gap-3">

              <FaClock className="text-purple-600" />

              <div>

                <p className="text-sm text-gray-500">
                  Last Login
                </p>

                <p className="font-medium">
                  {formatDate(user?.lastLogin)}
                </p>

              </div>

            </div>

            <div className="flex items-center gap-3">

              <FaClock className="text-purple-600" />

              <div>

                <p className="text-sm text-gray-500">
                  Last Seen
                </p>

                <p className="font-medium">
                  {formatDate(user?.lastSeen)}
                </p>

              </div>

            </div>

          </div>

        </UserInfoCard>

      </div>

      {/* Audit */}

      <UserInfoCard title="Audit Information">

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

          <div className="flex items-center gap-3">

            <FaCalendarAlt className="text-purple-600" />

            <div>

              <p className="text-sm text-gray-500">
                Created At
              </p>

              <p className="font-medium">
                {formatDate(user?.createdAt)}
              </p>

            </div>

          </div>

          <div className="flex items-center gap-3">

            <FaCalendarAlt className="text-purple-600" />

            <div>

              <p className="text-sm text-gray-500">
                Updated At
              </p>

              <p className="font-medium">
               {formatDate(user?.updatedAt)}
              </p>

            </div>

          </div>

        </div>

      </UserInfoCard>

      {/* Quick Actions */}

      <UserQuickActions
        user={user}
        onActivate={() => openConfirm("activate")}
        onDeactivate={() => openConfirm("deactivate")}
        onBlock={() => openConfirm("block")}
        onUnblock={() => openConfirm("unblock")}
        onVerifyEmail={() => openConfirm("verifyEmail")}
        onVerifyPhone={() => openConfirm("verifyPhone")}
        onRestore={() => openConfirm("restore")}
        onSoftDelete={() => openConfirm("softDelete")}

      />
            {/* Confirmation Modal */}

            <ConfirmModal
              isOpen={isModalOpen}
              title="Confirm Action"
              message={
                user
                  ? `Are you sure you want to ${selectedAction} "${user.fullName}"?`
                  : ""
              }
              confirmText="Yes"
              cancelText="Cancel"
         confirmButtonClass={
             selectedAction === "softDelete"
                 ? "bg-orange-600 hover:bg-orange-700"
                 : "bg-purple-600 hover:bg-purple-700"
         }
              onConfirm={handleConfirmAction}
              onCancel={() => {
                setIsModalOpen(false);
                setSelectedAction("");
              }}
            />

          </div>
        );
      }