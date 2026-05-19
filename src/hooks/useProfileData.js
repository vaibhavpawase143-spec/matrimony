import { useState, useEffect } from 'react';
import { profileAPI } from '@/services/api';
import { useAuth } from './useAuth';
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
  const { token } = useAuth();

  // Load profile data on mount and when token changes
  useEffect(() => {
    const loadProfile = async () => {
      // Only load if token exists
      if (!token) {
        console.log('🔄 No token found, skipping profile load');
        setProfileData(defaultProfileData);
        setIsLoading(false);
        return;
      }

      try {
        setIsLoading(true);
        console.log('🔄 Loading profile data...');
        const data = await profileAPI.getProfile(); // Gets current user's profile
        console.log('📥 Profile data received:', data);
        if (data) {
          setProfileData(defaultProfileData); // Reset first to avoid stale data
          setProfileData(prev => ({ ...prev, ...data }));
          console.log('✅ Profile data loaded successfully');
        } else {
          console.warn('⚠️ No profile data received');
          setProfileData(defaultProfileData);
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
          // Reset to default when profile not found
          setProfileData(defaultProfileData);
        } else {
          console.warn('⚠️ Unexpected profile loading error');
          setProfileData(defaultProfileData);
        }
      } finally {
        setIsLoading(false);
      }
    };

    loadProfile();
  }, [token]); // Reload when token changes (login/logout)

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
      // Throw the error so caller can handle it with proper backend message
      throw error;
    }
  };

  // Update specific field
  const updateField = (field, value) => {
    setProfileData(prev => ({ ...prev, [field]: value }));
  };

  // Clear all profile data
  const clearProfileData = () => {
    console.log('🗑️ Clearing profile data');
    setProfileData(defaultProfileData);
  };

  // Refresh profile data
  const refreshProfileData = async () => {
    try {
      setIsLoading(true);
      console.log('🔄 Refreshing profile data...');
      const data = await profileAPI.getProfile();
      console.log('📥 Profile data received on refresh:', data);
      if (data) {
        setProfileData(defaultProfileData); // Reset first
        setProfileData(prev => ({ ...prev, ...data }));
        console.log('✅ Profile data refreshed successfully');
      } else {
        console.warn('⚠️ No profile data received on refresh');
        setProfileData(defaultProfileData);
      }
    } catch (error) {
      console.error('❌ Profile refresh failed:', error);
      if (error.status === 404) {
        console.log('ℹ️ Profile not found on refresh');
        setProfileData(defaultProfileData);
      }
    } finally {
      setIsLoading(false);
    }
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
    refreshProfileData,
    getProfileCompletion,
  };
};