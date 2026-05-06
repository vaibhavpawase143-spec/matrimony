import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import { useAuth } from "@/hooks/useAuth";
import { useLanguage } from "@/context/LanguageContext.jsx";
import ProfileForm from "@/components/ProfileForm";
import { authAPI } from "@/services/api";

const CreateProfile = () => {
  const navigate = useNavigate();
  const { user, token } = useAuth();
  const { success, error } = useToast();
  const { startLoading, stopLoading } = useLoading();
  const { t } = useLanguage();

  const [profileData, setProfileData] = useState(null);

  useEffect(() => {
    // If user already has profile, redirect to home
    if (user && user.profile) {
      navigate("/home");
      return;
    }

    // Load existing profile data if any
    if (user) {
      loadProfileData();
    }
  }, [user, navigate]);

  const loadProfileData = async () => {
    try {
      const response = await authAPI.getCurrentUser();
      if (response.success && response.data) {
        setProfileData(response.data);
      }
    } catch (err) {
      console.log("No existing profile found, creating new one");
    }
  };

  const handleSaveProfile = async (formData) => {
    try {
      startLoading("Saving profile...");
      
      // Call backend API to save profile
      const response = await fetch('/api/profiles', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        throw new Error('Failed to save profile');
      }

      const result = await response.json();
      
      success("Profile saved successfully!");
      stopLoading();
      
      // Redirect to home after successful save
      setTimeout(() => {
        navigate("/home");
      }, 1000);
      
    } catch (err) {
      console.error("Profile save error:", err);
      error(err.message || "Failed to save profile");
      stopLoading();
    }
  };

  const handleError = (message) => {
    error(message);
  };

  return (
    <div className="min-h-screen bg-muted/30">
      <div className="container mx-auto py-8 px-4">
        <div className="max-w-4xl mx-auto">
          {/* Header */}
          <div className="text-center mb-8">
            <h1 className="text-3xl font-bold mb-2">
              {t?.profile?.createTitle || "Create Your Profile"}
            </h1>
            <p className="text-muted-foreground">
              {t?.profile?.createSubtitle || "Tell us about yourself to find better matches"}
            </p>
          </div>

          {/* Profile Form */}
          <div className="bg-card rounded-xl shadow-lg p-6">
            <ProfileForm
              initialData={profileData || {}}
              onSave={handleSaveProfile}
              onError={handleError}
              isLoading={false}
              showPhotoUpload={true}
            />
          </div>

          {/* Skip for now option */}
          <div className="text-center mt-6">
            <button
              onClick={() => navigate("/home")}
              className="text-muted-foreground hover:text-foreground underline"
            >
              {t?.profile?.skipForNow || "Skip for now"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CreateProfile;
