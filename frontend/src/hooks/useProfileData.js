import { useState, useEffect } from 'react';
import { profileAPI } from '@/services/api';
import errorHandler from '@/utils/errorHandler';

/**
 * @typedef {Object} ProfileData
 */

const defaultProfileData = {
  // Basic fields from User entity
  firstName: "",
  lastName: "",
  email: "",
  phone: "",
  
  // Profile fields matching backend DTO
  gender: "",
  dateOfBirth: "",
  about: "",
  imageUrl: "",
  
  // Relational field IDs
  religionId: null,
  casteId: null,
  motherTongueId: null,
  maritalStatusId: null,
  educationLevelId: null,
  occupationId: null,
  heightId: null,
  weightId: null,
  cityId: null,
};

export const useProfileData = () => {
  const [profileData, setProfileData] = useState(defaultProfileData);
  const [isLoading, setIsLoading] = useState(true);

  // Load profile data on mount
  useEffect(() => {
    const loadProfile = async () => {
      try {
        setIsLoading(true);
        console.log('🔄 Loading profile data...');
        const data = await profileAPI.getProfile(); // Gets current user's profile
        console.log('📥 Profile data received:', data);
        if (data) {
          setProfileData(prev => ({ ...prev, ...data }));
          console.log('✅ Profile data loaded successfully');
        } else {
          console.warn('⚠️ No profile data received');
        }
      } catch (error) {
        // Better error handling with specific cases
        console.error('❌ Profile loading failed:', error);
        console.error('Error details:', {
          message: error.message,
          status: error.status,
          endpoint: error.endpoint
        });
        
        // If profile not found (404), that's expected for new users
        if (error.status === 404) {
          console.log('ℹ️ Profile not found - user needs to create profile');
        } else {
          console.warn('⚠️ Unexpected profile loading error');
        }
      } finally {
        setIsLoading(false);
      }
    };

    loadProfile();
  }, []);

  // Save profile data
  const saveProfileData = async (data) => {
    try {
      const updatedData = { ...profileData, ...data };
      const response = await profileAPI.updateProfile(null, updatedData); // null for current user
      if (response) {
        setProfileData(prev => ({ ...prev, ...response }));
      }
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
      'firstName', 'lastName', 'gender', 'dateOfBirth'
    ];

    const optionalFields = [
      'religionId', 'casteId', 'motherTongueId', 'maritalStatusId',
      'educationLevelId', 'occupationId', 'heightId', 'weightId', 'cityId',
      'about', 'imageUrl'
    ];

    const filledRequiredFields = requiredFields.filter(field => {
      const value = profileData[field];
      return value && value.toString().trim() !== "";
    }).length;

    const filledOptionalFields = optionalFields.filter(field => {
      const value = profileData[field];
      return value && value.toString().trim() !== "";
    }).length;

    // Required fields count for 70%, optional for 30%
    const requiredPercentage = (filledRequiredFields / requiredFields.length) * 70;
    const optionalPercentage = (filledOptionalFields / optionalFields.length) * 30;
    
    const totalPercentage = Math.round(requiredPercentage + optionalPercentage);
    
    return totalPercentage;
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