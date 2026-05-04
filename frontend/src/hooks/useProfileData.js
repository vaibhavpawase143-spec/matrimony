import { useState, useEffect } from 'react';

/**
 * @typedef {Object} ProfileData
 */

const defaultProfileData = {
  // Personal Details
  fullName: "",
  gender: "",
  dateOfBirth: "",
  age: "",
  maritalStatus: "",
  religion: "",
  caste: "",
  subCaste: "",
  motherTongue: "",

  // Physical Details
  height: "",
  weight: "",
  complexion: "",
  bodyType: "",

  // Education & Career
  highestEducation: "",
  profession: "",
  annualIncome: "",
  companyName: "",

  // Location Details
  country: "India",
  state: "",
  city: "",
  address: "",

  // Lifestyle
  diet: "",
  smoking: "",
  drinking: "",

  // Family Details
  fatherName: "",
  fatherOccupation: "",
  motherName: "",
  motherOccupation: "",
  siblingsCount: "",

  // Partner Preferences
  preferredAgeMin: "",
  preferredAgeMax: "",
  preferredLocation: "",
  preferredEducation: "",
  otherExpectations: "",

  // Other
  aboutMe: "",
  profilePhoto: null,
};

export const useProfileData = () => {
  const [profileData, setProfileData] = useState(defaultProfileData);
  const [isLoading, setIsLoading] = useState(true);

  // Load profile data on mount
  useEffect(() => {
    // TODO: fetch profile from backend
    setIsLoading(false);
  }, []);

  // Save profile data
  const saveProfileData = async (data) => {
    try {
      const updatedData = { ...profileData, ...data };
      // TODO: save profile to backend
      setProfileData(updatedData);
      return true;
    } catch (error) {
      console.error('Error saving profile data:', error);
      return false;
    }
  };

  // Update specific field
  const updateField = (field, value) => {
    setProfileData(prev => ({ ...prev, [field]: value }));
  };

  // Clear all profile data
  const clearProfileData = () => {
    // TODO: clear profile from backend
    setProfileData(defaultProfileData);
  };

  // Get profile completion percentage
  const getProfileCompletion = () => {
    const requiredFields = [
      'fullName', 'gender', 'dateOfBirth', 'maritalStatus',
      'religion', 'motherTongue', 'highestEducation', 'profession',
      'country', 'state', 'city'
    ];

    const filledRequiredFields = requiredFields.filter(field =>
      profileData[field] && profileData[field].toString().trim() !== ""
    ).length;

    return Math.round((filledRequiredFields / requiredFields.length) * 100);
  };

  return {
    profileData,
    isLoading,
    saveProfileData,
    updateField,
    clearProfileData,
    getProfileCompletion,
  };
};