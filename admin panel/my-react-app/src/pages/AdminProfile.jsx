import { useEffect, useState } from "react";
import { toast } from "sonner";
import EditProfileModal from "../components/profile/EditProfileModal";
import { getAdminProfile } from "../services/adminProfileService";
import ProfileCard from "../components/profile/ProfileCard";
import ChangePasswordModal from "../components/profile/ChangePasswordModal";
export default function AdminProfile() {
  // ===========================
  // STATES
  // ===========================
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
const [showChangePassword, setShowChangePassword] = useState(false);
  // For next steps
  const [showEditModal, setShowEditModal] = useState(false);


  // ===========================
  // LOAD PROFILE
  // ===========================
  const loadProfile = async () => {
    try {
      setLoading(true);

      const data = await getAdminProfile();

      setProfile(data);
      setError("");
    } catch (err) {
      console.error(err);

      setError(err.message || "Failed to load profile.");
      toast.error(err.message || "Failed to load profile.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProfile();
  }, []);

  // ===========================
  // LOADING
  // ===========================
  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-lg font-medium text-gray-600">
          Loading profile...
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
  // JSX
  // ===========================
  return (
    <div className="p-6">

      {/* Header */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800">
          Admin Profile
        </h1>

        <p className="text-gray-500 mt-1">
          View and manage your profile information.
        </p>
      </div>

      {/* Profile Card */}
    <ProfileCard
      profile={profile}
      onEdit={() => setShowEditModal(true)}
      onChangePassword={() => setShowChangePassword(true)}
    />

    <ChangePasswordModal
      open={showChangePassword}
      onClose={() => setShowChangePassword(false)}
    />

    <EditProfileModal
      isOpen={showEditModal}
      onClose={() => setShowEditModal(false)}
      profile={profile}
      onSuccess={loadProfile}
    />

    </div>
  );
}