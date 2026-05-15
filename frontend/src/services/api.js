// API service for frontend development
import errorHandler from '@/utils/errorHandler';

const API_BASE_URL = '/api'; // Will be proxied to backend

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
const apiClient = async (endpoint, options = {}) => {
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
    console.log('🌐 Request options:', defaultOptions);
    
    const response = await fetch(fullUrl, defaultOptions);
    
    console.log('🌐 Response status:', response.status, response.statusText);
    console.log('🌐 Response headers:', Object.fromEntries(response.headers.entries()));

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      console.error('❌ API Error Response:', errorData);
      const error = new Error(errorData.message || errorData.error || `API call failed: ${endpoint}`);
      error.status = response.status;
      error.endpoint = endpoint;
      error.url = fullUrl;
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
    if (error.name === 'TypeError' || error.message.includes('Failed to fetch')) {
      const networkError = new Error('Network connection error');
      networkError.type = 'NETWORK_ERROR';
      throw networkError;
    }
    throw error;
  }
};

export const authAPI = {
  login: async (data, isAdmin = false) => {
    try {
      const endpoint = isAdmin ? '/admin/auth/login' : '/auth/login';
      const result = await apiClient(endpoint, {
        method: 'POST',
        body: JSON.stringify(data),
      });
      
      // Handle different response formats
      const token = result.accessToken || result.token || result.data?.accessToken || result.data?.token;
      const userData = result.user || result.data || result;
      
      if (token) {
        localStorage.setItem('token', token);
        localStorage.setItem('isAdmin', isAdmin);
        
        // Store user data for immediate access
        if (userData) {
          localStorage.setItem('user', JSON.stringify(userData));
        }
      }
      
      return {
        success: true,
        data: userData,
        token: token
      };
    } catch (error) {
      errorHandler.handle(error, 'Login API');
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
      errorHandler.handle(error, 'Registration API');
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
      validateToken(); // Validate token before making request
      return await apiClient('/profiles/me');
    } catch (error) {
      // Return null instead of throwing for getCurrentUser
      return null;
    }
  }
};

export const profileAPI = {
  getProfile: async (userId) => {
    try {
      const endpoint = userId ? `/profiles/${userId}` : '/profiles/me';
      return await apiClient(endpoint);
    } catch (error) {
      errorHandler.handle(error, 'Get Profile API');
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
      errorHandler.handle(error, 'Update Profile API');
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
      errorHandler.handle(error, 'Get Profiles API');
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
      errorHandler.handle(error, 'Search Profiles API');
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
      console.log('📊 Religions data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First religion item:', result?.[0]);
      return result;
    } catch (error) {
      console.error('❌ Get Religions API error:', error);
      errorHandler.handle(error, 'Get Religions API');
      return []; // Return empty array on error to prevent UI crashes
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
      errorHandler.handle(error, 'Get Cities API');
      return [];
    }
  },

  getEducationLevels: async () => {
    try {
      console.log('🔍 Fetching education levels...');
      const result = await apiClient('/education-levels');
      console.log('✅ MASTER API RESPONSE - Education Levels:', result);
      console.log('📊 Education Levels data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First education level item:', result?.[0]);
      return result;
    } catch (error) {
      console.error('❌ Get Education Levels API error:', error);
      errorHandler.handle(error, 'Get Education Levels API');
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
      const result = await apiClient('/marital-statuses');
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
      console.log('🔍 Fetching castes...', religionId);
      const endpoint = religionId ? `/castes?religionId=${religionId}` : '/castes';
      const result = await apiClient(endpoint);
      console.log('✅ MASTER API RESPONSE - Castes:', result);
      console.log('📊 Castes data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First caste item:', result?.[0]);
      return result;
    } catch (error) {
      console.error('❌ Get Castes API error:', error);
      errorHandler.handle(error, 'Get Castes API');
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
      return result;
    } catch (error) {
      console.error('❌ Get Heights API error:', error);
      errorHandler.handle(error, 'Get Heights API');
      return [];
    }
  },

  getWeights: async () => {
    try {
      console.log('🔍 Fetching weights...');
      const result = await apiClient('/weights');
      console.log('✅ MASTER API RESPONSE - Weights:', result);
      console.log('📊 Weights data type:', typeof result, ' isArray:', Array.isArray(result));
      console.log('📋 First weight item:', result?.[0]);
      return result;
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
