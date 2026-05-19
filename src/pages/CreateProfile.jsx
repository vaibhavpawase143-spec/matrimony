import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import { useAuth } from "@/hooks/useAuth";
import { useLanguage } from "@/context/LanguageContext.jsx";
import ProfileForm from "@/components/ProfileForm";
import { authAPI, profileAPI } from "@/services/api";

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
        const userData = response.data;
        
        // Map user registration data to profile form fields
        const mappedProfileData = {
          // Personal Details - pre-populate from registration
          fullName: userData.first_name && userData.last_name 
            ? `${userData.first_name} ${userData.last_name}` 
            : userData.first_name || "",
          firstName: userData.first_name || "",
          lastName: userData.last_name || "",
          email: userData.email || "",
          phone: userData.phone || "",
          gender: userData.gender || "",
          dateOfBirth: userData.dob || "",
          religion: userData.religion || "",
          
          // Other fields will remain empty for user to fill
          maritalStatus: "",
          caste: "",
          subCaste: "",
          motherTongue: "",
          height: "",
          weight: "",
          complexion: "",
          bodyType: "",
          highestEducation: "",
          profession: "",
          annualIncome: "",
          companyName: "",
          location: "",
          city: "",
          state: "",
          country: "",
          aboutYourself: "",
          familyValues: "",
          familyStatus: "",
          familyType: "",
          fatherOccupation: "",
          motherOccupation: "",
          siblings: "",
          diet: "",
          drinking: "",
          smoking: "",
          hobbies: "",
          interests: "",
          partnerPreferences: {
            minAge: "",
            maxAge: "",
            minHeight: "",
            maxHeight: "",
            religion: "",
            caste: "",
            education: "",
            profession: "",
            location: "",
            diet: "",
            drinking: "",
            smoking: ""
          }
        };
        
        setProfileData(mappedProfileData);
      }
    } catch (err) {
      console.log("No existing profile found, creating new one");
      
      // If no profile data exists, create basic profile from auth context
      if (user) {
        const basicProfileData = {
          fullName: user.first_name && user.last_name 
            ? `${user.first_name} ${user.last_name}` 
            : user.first_name || "",
          firstName: user.first_name || "",
          lastName: user.last_name || "",
          email: user.email || "",
          phone: user.phone || "",
          gender: user.gender || "",
          dateOfBirth: user.dob || "",
          religion: user.religion || "",
        };
        setProfileData(basicProfileData);
      }
    }
  };

  const handleSaveProfile = async (formData) => {
    try {
      startLoading("Saving profile...");
      
      // Call backend API to save profile using api.js client
      const result = await profileAPI.updateProfile(null, formData);
      
      success("Profile saved successfully!");
      stopLoading();
      
      // Redirect to home after successful save
      setTimeout(() => {
        navigate("/home");
      }, 1000);
      
    } catch (err) {
      console.error("Profile save error:", err);
      
      // Use backend error message directly
      const errorMessage = err.message || "Failed to save profile";
      
      // Handle specific error cases using error codes and status from backend
      if (err.status === 400) {
        // Validation error - check for field-specific errors
        if (err.validationErrors && typeof err.validationErrors === 'object') {
          error("Please fix the validation errors");
          // Could set field-specific errors here if needed
        } else {
          error(errorMessage);
        }
      } else if (err.status === 401) {
        error(errorMessage);
      } else if (err.status === 409) {
        error(errorMessage);
      } else if (err.status === 500) {
        error("Server error. Please try again later.");
      } else {
        error(errorMessage);
      }
      
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
