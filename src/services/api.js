// API service for frontend development
import errorHandler from '@/utils/errorHandler';

const API_BASE_URL = '/api'; // Will be proxied to backend

// Helper function to parse validation errors from backend response
// Backend can send validation errors in two formats:
// 1. String format: "field1: message1, field2: message2"
// 2. Object format: { "errors": { "email": "Invalid email", "phone": "Invalid phone" } }
const parseValidationErrors = (errorData) => {
  if (!errorData) return {};
  
  // Handle object format: { errors: { field: message } }
  if (typeof errorData === 'object' && errorData.errors) {
    return errorData.errors;
  }
  
  // Handle string format: "field1: message1, field2: message2"
  if (typeof errorData === 'string') {
    const errors = {};
    const fieldErrors = errorData.split(',').map(err => err.trim());
    
    fieldErrors.forEach(fieldError => {
      const colonIndex = fieldError.indexOf(':');
      if (colonIndex > -1) {
        const field = fieldError.substring(0, colonIndex).trim();
        const errorMsg = fieldError.substring(colonIndex + 1).trim();
        if (field && errorMsg) {
          errors[field] = errorMsg;
        }
      }
    });
    
    return errors;
  }
  
  return {};
};

// Token validation helper
const validateToken = () => {
  const token = localStorage.getItem('token');
  if (!token) {
    throw new Error('No authentication token found');
  }
  
  try {
    // Basic JWT token validation (you can enhance this)
    const payload = JSON.parse(atob(token.split('.')[1]));
    const now = Date.now() / 1000;
    if (payload.exp && payload.exp < now) {
      throw new Error('Token expired');
    }
    return token;
  } catch (e) {
    localStorage.removeItem('token');
    throw new Error('Invalid or expired token');
  }
};

// Centralized API client with proper auth and error handling
export const apiClient = async (endpoint, options = {}) => {
  try {
    const token = localStorage.getItem('token');
    
    // Don't attach Authorization header for public auth endpoints
    const isPublicAuthEndpoint = endpoint.startsWith('/auth/') || 
                              endpoint.startsWith('/users/login') || 
                              endpoint.startsWith('/users/register');
    
    const defaultOptions = {
      headers: {
        'Content-Type': 'application/json',
        ...(token && !isPublicAuthEndpoint && { 'Authorization': `Bearer ${token}` }),
        ...options.headers
      },
      ...options
    };

    const fullUrl = `${API_BASE_URL}${endpoint}`;
    console.log('🌐 API Request URL:', fullUrl);
    console.log('🌐 API Request - Token in Authorization header:', token && typeof token === 'string' ? token.substring(0, Math.min(50, token.length)) + '...' : 'null');
    console.log('🌐 Request options:', defaultOptions);
    
    const response = await fetch(fullUrl, defaultOptions);
    
    console.log('🌐 Response status:', response.status, response.statusText);
    console.log('🌐 Response headers:', Object.fromEntries(response.headers.entries()));

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      console.error('❌ API Error Response:', errorData);
      
      // Extract error message from backend ErrorResponse structure
      // Priority: message > error > generic fallback
      const errorMessage = errorData.message || errorData.error || `API call failed: ${endpoint}`;
      const errorCode = errorData.errorCode || errorData.error || `ERR_${response.status}`;
      
      // Create enhanced error object with backend data
      const error = new Error(errorMessage);
      error.status = response.status;
      error.endpoint = endpoint;
      error.url = fullUrl;
      error.errorCode = errorCode;
      error.backendError = errorData.error || null;
      error.timestamp = errorData.timestamp || null;
      
      // Extract validation errors if present (for field-wise display)
      // Handle both object format { errors: { field: message } } and string format
      // Also handle direct validationErrors field from backend
      if (response.status === 400) {
        error.validationErrors = errorData.validationErrors || parseValidationErrors(errorData);
      }
      
      throw error;
    }

    const result = await response.json();
    console.log('✅ API Success Response:', result);
    return result;
  } catch (error) {
    console.error('❌ API Request Failed:', error);
    console.error('❌ Error details:', {
      message: error.message,
      endpoint,
      url: `${API_BASE_URL}${endpoint}`,
      stack: error.stack
    });
    
    // Handle network errors and other exceptions
    if (error.name === 'TypeError' || error.message.includes('Failed to fetch') || error.message.includes('Network Error')) {
      const networkError = new Error('No internet connection. Please check your internet connection.');
      networkError.type = 'NETWORK_ERROR';
      networkError.status = null;
      networkError.errorCode = 'ERR_NETWORK';
      throw networkError;
    }
    throw error;
  }
};

export const authAPI = {
  login: async (data, isAdmin = false) => {
    try {
      console.log('🔐 Frontend Login - Email:', data.email);
      console.log('🔐 Frontend Login - IsAdmin:', isAdmin);
      
      const endpoint = isAdmin ? '/admin/auth/login' : '/auth/login';
      const result = await apiClient(endpoint, {
        method: 'POST',
        body: JSON.stringify(data),
      });
      
      console.log('🔐 Frontend Login - API Response:', result);
      console.log('🔐 Frontend Login - Response structure:', JSON.stringify(result, null, 2));
      
      // Handle different response formats
      // Backend returns: { success: true, data: { accessToken, refreshToken, profile } }
      const token = result.data?.accessToken || result.accessToken || result.token || result.data?.token;
      const profileData = result.data?.profile || result.profile;
      
      console.log('🔐 Frontend Login - Extracted token:', token && typeof token === 'string' ? token.substring(0, Math.min(50, token.length)) + '...' : 'null');
      console.log('🔐 Frontend Login - Profile data from login response:', profileData);
      console.log('🔐 Frontend Login - Profile data keys:', profileData ? Object.keys(profileData) : 'null');
      
      // Don't store token here - let useAuth.login handle it to avoid duplication
      // Don't store user data here either - use the profile data from backend response
      
      return {
        success: true,
        data: profileData || null,
        token: token
      };
    } catch (error) {
      errorHandler.handle(error, 'Login API', false); // Don't show toast here
      throw error;
    }
  },

  register: async (data) => {
    try {
      const result = await apiClient('/auth/register', {
        method: 'POST',
        body: JSON.stringify(data),
      });
      
      // Registration should NOT return token - user needs to login separately
      const userData = result.data || result;
      
      return {
        success: true,
        user: userData,
        token: null // Explicitly no token from registration
      };
    } catch (error) {
      errorHandler.handle(error, 'Registration API', false); // Don't show toast here
      throw error;
    }
  },

  logout: async () => {
    try {
      // Call backend logout endpoint if available
      if (localStorage.getItem('token')) {
        await apiClient('/users/logout', { method: 'POST' }).catch(() => {
          // Ignore errors on logout - just clear local storage
        });
      }
    } catch (error) {
      // Ignore logout errors and proceed with cleanup
    } finally {
      // Always clear local storage
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('role');
      localStorage.removeItem('isAdmin');
    }
  },

  getCurrentUser: async () => {
    try {
      const token = localStorage.getItem('token');
      console.log('👤 Frontend getCurrentUser - Token from localStorage:', token && typeof token === 'string' ? token.substring(0, Math.min(50, token.length)) + '...' : 'null');
      
      validateToken(); // Validate token before making request
      const profileData = await apiClient('/profiles/me');
      console.log('👤 Frontend getCurrentUser - Profile data received:', profileData);
      return profileData;
    } catch (error) {
      console.error('❌ Frontend getCurrentUser - Error:', error);
      // Return null instead of throwing for getCurrentUser
      return null;
    }
  },

  forgotPassword: async (email) => {
    try {
      const result = await apiClient('/auth/forgot-password', {
        method: 'POST',
        body: JSON.stringify({ email }),
      });
      return {
        success: true,
        message: result.message || 'Password reset link sent to email'
      };
    } catch (error) {
      errorHandler.handle(error, 'Forgot Password API', false);
      throw error;
    }
  },

  resetPassword: async (token, newPassword) => {
    try {
      const result = await apiClient('/auth/reset-password', {
        method: 'POST',
        body: JSON.stringify({ token, newPassword }),
      });
      return {
        success: true,
        message: result.message || 'Password reset successfully'
      };
    } catch (error) {
      errorHandler.handle(error, 'Reset Password API', false);
      throw error;
    }
  }
};

export const profileAPI = {
  getProfile: async (userId) => {
    try {
      const endpoint = userId ? `/profiles/${userId}` : '/profiles/me';
      return await apiClient(endpoint);
    } catch (error) {
      errorHandler.handle(error, 'Get Profile API', false); // Don't show toast here
      throw error;
    }
  },

  updateProfile: async (userId, data) => {
    try {
      const endpoint = userId ? `/profiles/${userId}` : '/profiles/me';
      return await apiClient(endpoint, {
        method: 'PUT',
        body: JSON.stringify(data),
      });
    } catch (error) {
      errorHandler.handle(error, 'Update Profile API', false); // Don't show toast here
      throw error;
    }
  },

  getProfiles: async (page = 0, size = 10, filters = {}) => {
    try {
      const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
        ...filters
      });
      return await apiClient(`/profiles?${params}`);
    } catch (error) {
      errorHandler.handle(error, 'Get Profiles API', false); // Don't show toast here
      throw error;
    }
  }
};

export const searchAPI = {
  searchProfiles: async (filters) => {
    try {
      const params = new URLSearchParams(filters);
      return await apiClient(`/profiles/search?${params}`);
    } catch (error) {
      errorHandler.handle(error, 'Search Profiles API', false); // Don't show toast here
      throw error;
    }
  }
};

export const masterDataAPI = {
  getReligions: async () => {
    try {

      console.log('🔍 Fetching religions...');

      const result = await apiClient('/religions');

      console.log('✅ MASTER API RESPONSE - Religions:', result);

      console.log(
        '📊 Religions data type:',
        typeof result,
        ' isArray:',
        Array.isArray(result)
      );

      console.log('📋 First religion item:', result?.[0]);

      return Array.isArray(result) ? result : [];

    } catch (error) {

      console.error('❌ Get Religions API error:', error);

      return [];

    }
  },

  getCities: async (stateId) => {
    try {
      console.log('🔍 Fetching cities...', stateId);
      const endpoint = stateId ? `/cities?stateId=${stateId}` : '/cities';
      const result = await apiClient(endpoint);
      console.log('✅ MASTER API RESPONSE - Cities:', result);
      console.log('📊 Cities data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First city item:', result?.[0]);
      return result;
    } catch (error) {
      console.error('❌ Get Cities API error:', error);
      errorHandler.handle(error, 'Get Cities API', false); // Don't show toast here
      return [];
    }
  },

  getEducationLevels: async () => {
    try {
      console.log('🔍 Fetching education levels...');
      const result = await apiClient('/master/education-levels');
      console.log('✅ MASTER API RESPONSE - Education Levels:', result);
      console.log('📊 Education Levels data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First education level item:', result?.[0]);
      return result;
    } catch (error) {
      console.error('❌ Get Education Levels API error:', error);
      errorHandler.handle(error, 'Get Education Levels API', false); // Don't show toast here
      return [];
    }
  },

  getOccupations: async () => {
    try {
      console.log('🔍 Fetching occupations...');
      const result = await apiClient('/occupations');
      console.log('✅ MASTER API RESPONSE - Occupations:', result);
      console.log('📊 Occupations data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First occupation item:', result?.[0]);
      return result;
    } catch (error) {
      console.error('❌ Get Occupations API error:', error);
      errorHandler.handle(error, 'Get Occupations API');
      return [];
    }
  },

  getMaritalStatuses: async () => {
    try {
      console.log('🔍 Fetching marital statuses...');
      const result = await apiClient('/marital-status');
      console.log('✅ MASTER API RESPONSE - Marital Statuses:', result);
      console.log('📊 Marital Statuses data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First marital status item:', result?.[0]);
      return result;
    } catch (error) {
      console.error('❌ Get Marital Statuses API error:', error);
      errorHandler.handle(error, 'Get Marital Statuses API');
      return [];
    }
  },

 getCastes: async (religionId) => {

   try {

     // If no religion selected
     if (!religionId) {
       return [];
     }

     console.log('🔍 Fetching castes...', religionId);

     // Static admin id
     const adminId = 1;

     // API call
     const result = await apiClient(
       `/admins/${adminId}/castes/religion/${religionId}`
     );

     console.log('✅ MASTER API RESPONSE - Castes:', result);

     console.log(
       '📊 Castes data type:',
       typeof result,
       ' isArray:',
       Array.isArray(result)
     );

     console.log('📋 First caste item:', result?.[0]);

     // Safe return
     return Array.isArray(result) ? result : [];

   } catch (error) {

     console.error('❌ Get Castes API error:', error);

     return [];

   }
 },

 getSubCastes: async (casteId) => {

   try {

     if (!casteId) {
       return [];
     }

     console.log('🔍 Fetching sub castes...', casteId);

     const adminId = 1;

     const result = await apiClient(
       `/master/sub-castes/caste/${casteId}/admin/${adminId}`
     );

     console.log('✅ MASTER API RESPONSE - Sub Castes:', result);

     console.log(
       '📊 Sub Castes data type:',
       typeof result,
       ' isArray:',
       Array.isArray(result)
     );

     console.log('📋 First sub caste item:', result?.[0]);

     return Array.isArray(result) ? result : [];

   } catch (error) {

     console.error('❌ Get Sub Castes API error:', error);

     return [];

   }
 },
  getHeights: async () => {
    try {
      console.log('🔍 Fetching heights...');
      const result = await apiClient('/heights');
      console.log('✅ MASTER API RESPONSE - Heights:', result);
      console.log('📊 Heights data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First height item:', result?.[0]);
      return Array.isArray(result) ? result : [];
    } catch (error) {
      console.error('❌ Get Heights API error:', error);
      errorHandler.handle(error, 'Get Heights API');
      return [];
    }
  },

  getWeights: async () => {
    try {
      console.log('🔍 Fetching weights...');
      const result = await apiClient('/master/weights');
      console.log('✅ MASTER API RESPONSE - Weights:', result);
      console.log('📊 Weights data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First weight item:', result?.[0]);
      return Array.isArray(result) ? result : [];
    } catch (error) {
      console.error('❌ Get Weights API error:', error);
      errorHandler.handle(error, 'Get Weights API');
      return [];
    }
  },

  getMotherTongues: async () => {
    try {
      console.log('🔍 Fetching mother tongues...');
      const result = await apiClient('/mother-tongues');
      console.log('✅ MASTER API RESPONSE - Mother Tongues:', result);
      console.log('📊 Mother Tongues data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First mother tongue item:', result?.[0]);
      return result;
    } catch (error) {
      console.error('❌ Get Mother Tongues API error:', error);
      errorHandler.handle(error, 'Get Mother Tongues API');
      return [];
    }
  }
};

export default {
  authAPI,
  profileAPI,
  searchAPI,
  masterDataAPI
};
