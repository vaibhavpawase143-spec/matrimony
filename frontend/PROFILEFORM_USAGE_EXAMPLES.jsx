/**
 * ProfileForm Component - Usage Examples
 * 
 * The ProfileForm component is a reusable, self-contained matrimony profile form
 * that can be used for both creating new profiles and updating existing ones.
 * 
 * Location: src/components/ProfileForm.jsx
 */

// ============================================================================
// EXAMPLE 1: Using ProfileForm in Create Profile Page
// ============================================================================

import { useState } from "react";
import ProfileForm from "@/components/ProfileForm";
import { useToast } from "@/components/Toast";
import Navbar from "@/components/Navbar";

const CreateProfilePage = () => {
  const { success, error } = useToast();
  const [isLoading, setIsLoading] = useState(false);

  const handleSaveProfile = async (profileData) => {
    setIsLoading(true);
    try {
      // Call your backend API to save the profile
      const response = await fetch("/api/profiles/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(profileData)
      });

      if (response.ok) {
        success("Profile created successfully!");
        // Redirect to profile view or dashboard
        // navigate("/profile");
      } else {
        error("Failed to create profile. Please try again.");
      }
    } catch (err) {
      error("An error occurred while creating profile.");
      console.error("Profile creation error:", err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleFormError = (errorMessage) => {
    error(errorMessage);
  };

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />
      
      <div className="py-8 text-center bg-gradient-to-r from-purple-600 to-pink-500">
        <h1 className="text-4xl font-bold text-white mb-2">Create Your Profile</h1>
        <p className="text-white/80">Join Gathbandhan and find your perfect match</p>
      </div>

      <div className="container mx-auto px-4 py-8 max-w-4xl">
        <div className="bg-card rounded-xl border border-border p-6">
          <ProfileForm 
            initialData={{}} // Empty for create
            onSave={handleSaveProfile}
            onError={handleFormError}
            showPhotoUpload={true}
            isLoading={isLoading}
          />
        </div>
      </div>
    </div>
  );
};

// ============================================================================
// EXAMPLE 2: Using ProfileForm in Update Profile Page
// ============================================================================

import { useState, useEffect } from "react";
import ProfileForm from "@/components/ProfileForm";
import { useToast } from "@/components/Toast";
import Navbar from "@/components/Navbar";
import { useProfileData } from "@/hooks/useProfileData";

const UpdateProfilePage = () => {
  const { success, error } = useToast();
  const { profileData: savedProfile } = useProfileData();
  const [isLoading, setIsLoading] = useState(false);

  const handleUpdateProfile = async (updatedProfileData) => {
    setIsLoading(true);
    try {
      // Send to backend API
      const response = await fetch("/api/profiles/update", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedProfileData)
      });

      if (response.ok) {
        success("Profile updated successfully!");
      } else {
        error("Failed to update profile. Please try again.");
      }
    } catch (err) {
      error("An error occurred while updating profile.");
      console.error("Profile update error:", err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleFormError = (errorMessage) => {
    error(errorMessage);
  };

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />
      
      <div className="py-8 text-center bg-gradient-to-r from-purple-600 to-pink-500">
        <h1 className="text-4xl font-bold text-white mb-2">Update Your Profile</h1>
        <p className="text-white/80">Keep your profile information up to date</p>
      </div>

      <div className="container mx-auto px-4 py-8 max-w-4xl">
        <div className="bg-card rounded-xl border border-border p-6">
          <ProfileForm 
            initialData={savedProfile} // Pre-populate with saved data
            onSave={handleUpdateProfile}
            onError={handleFormError}
            showPhotoUpload={true}
            isLoading={isLoading}
          />
        </div>
      </div>
    </div>
  );
};

// ============================================================================
// EXAMPLE 3: Using ProfileForm in a Multi-Step Registration
// ============================================================================

import { useState } from "react";
import ProfileForm from "@/components/ProfileForm";
import { useToast } from "@/components/Toast";
import Navbar from "@/components/Navbar";

const RegistrationWizard = () => {
  const { success, error } = useToast();
  const [step, setStep] = useState(1); // 1: Signup, 2: Profile, 3: Confirm
  const [isLoading, setIsLoading] = useState(false);
  const [profileData, setProfileData] = useState(null);

  const handleProfileCreation = async (data) => {
    setIsLoading(true);
    try {
      // Save profile data temporarily
      setProfileData(data);
      
      // Move to next step (e.g., review/confirm)
      setStep(3);
      
      success("Profile information saved!");
    } catch (err) {
      error("Failed to process profile information.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleFormError = (errorMessage) => {
    error(errorMessage);
  };

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />
      
      {step === 2 && (
        <div className="container mx-auto px-4 py-8 max-w-4xl">
          <div className="bg-card rounded-xl border border-border p-6">
            <h2 className="text-2xl font-bold mb-6">Step 2: Create Your Profile</h2>
            <ProfileForm 
              initialData={{}}
              onSave={handleProfileCreation}
              onError={handleFormError}
              showPhotoUpload={true}
              isLoading={isLoading}
            />
          </div>
        </div>
      )}

      {step === 3 && profileData && (
        <div className="container mx-auto px-4 py-8 max-w-4xl">
          <div className="bg-card rounded-xl border border-border p-6">
            <h2 className="text-2xl font-bold mb-4">Review Your Information</h2>
            <div className="grid grid-cols-2 gap-4">
              <p><strong>Name:</strong> {profileData.fullName}</p>
              <p><strong>Gender:</strong> {profileData.gender}</p>
              <p><strong>Religion:</strong> {profileData.religion}</p>
              <p><strong>City:</strong> {profileData.city}</p>
            </div>
            <button 
              onClick={() => setStep(4)}
              className="mt-6 px-6 py-2 bg-primary text-white rounded-lg font-semibold"
            >
              Complete Registration
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

// ============================================================================
// EXAMPLE 4: ProfileForm with Custom Styling
// ============================================================================

const CustomStyledProfileForm = () => {
  const { success, error } = useToast();

  const handleSave = async (profileData) => {
    success("Profile saved!");
    console.log("Saving profile:", profileData);
  };

  return (
    <div className="bg-white rounded-lg shadow-lg p-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Complete Your Profile</h1>
        <p className="text-gray-600 mt-2">Help us understand you better</p>
      </div>

      <ProfileForm 
        initialData={{}}
        onSave={handleSave}
        onError={(err) => error(err)}
        showPhotoUpload={true}
      />
    </div>
  );
};

// ============================================================================
// EXAMPLE 5: ProfileForm Props Reference
// ============================================================================

/**
 * ProfileForm Props:
 * 
 * @param {Object} initialData - Optional. Pre-populate form with profile data
 *                               Empty object {} for create, populated object for update
 *                               Example: { fullName: "John Doe", gender: "Male", ...}
 * 
 * @param {Function} onSave - Required. Callback when form is submitted successfully
 *                            Receives profileData (all form values except File object)
 *                            Example: (profileData) => { console.log(profileData); }
 * 
 * @param {Function} onError - Optional. Callback for validation errors
 *                             Receives error message string
 *                             Example: (errorMsg) => { showAlert(errorMsg); }
 * 
 * @param {Boolean} isLoading - Optional. Show loading state on save button
 *                              Default: false
 *                              Example: true when API call is in progress
 * 
 * @param {Boolean} showPhotoUpload - Optional. Show profile photo upload section
 *                                    Default: true
 *                                    Example: false to hide photo upload
 * 
 */

// ============================================================================
// EXAMPLE 6: Integration with useProfileData Hook
// ============================================================================

import { useProfileData } from "@/hooks/useProfileData";

const ProfileManagement = () => {
  const { profileData: savedData, saveProfileData } = useProfileData();

  const handleProfileSave = async (formData) => {
    // Save to both localStorage and backend
    await saveProfileData(formData);
    
    // Also save to backend
    // await fetch("/api/profile", { method: "PUT", body: JSON.stringify(formData) });
  };

  return (
    <ProfileForm
      initialData={savedData}
      onSave={handleProfileSave}
      onError={(err) => console.error(err)}
    />
  );
};

// ============================================================================
// NOTES FOR DEVELOPERS
// ============================================================================

/**
 * 1. PHOTO UPLOAD
 *    - Photos are NOT saved to localStorage (File objects can't be serialized)
 *    - To persist photos, implement multipart form upload to backend
 *    - Maximum file size: 5MB
 *    - Accepted formats: image/* (jpg, png, gif, webp, etc.)
 * 
 * 2. VALIDATION
 *    - Form validates required fields on submit only
 *    - Required fields:
 *      - fullName, gender, dateOfBirth, maritalStatus
 *      - religion, motherTongue, highestEducation, profession, city
 *      - email (must contain @)
 *    - Validation errors are passed to onError callback
 * 
 * 3. DATA STRUCTURE
 *    - formData is a flat object with 40+ fields
 *    - File object (profilePhoto) is REMOVED before calling onSave
 *    - profilePhotoUrl contains base64 string for preview
 * 
 * 4. AUTO-CALCULATION
 *    - Age is automatically calculated from dateOfBirth
 *    - User can manually edit age if needed
 * 
 * 5. ERROR HANDLING
 *    - Photo upload errors passed to onError callback
 *    - Form validation errors passed to onError callback
 *    - Network errors should be handled in onSave function
 * 
 * 6. LOADING STATE
 *    - Pass isLoading prop to disable submit button during save
 *    - Button text changes to "Saving..." when isLoading is true
 * 
 * 7. RESPONSIVENESS
 *    - Form uses Tailwind's responsive grid (1 col mobile, 2 col tablet+)
 *    - Optimized for mobile, tablet, and desktop screens
 * 
 */
