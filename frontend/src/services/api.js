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
      console.error('Login API Error:', error);

      throw new Error(
        error?.message || 'Something went wrong'
      );
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
      console.error('Login API Error:', error);

      throw new Error(
        error?.message || 'Something went wrong'
      );
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
     console.error('Login API Error:', error);

     throw new Error(
       error?.message || 'Something went wrong'
     );
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
      console.error('Login API Error:', error);

      throw new Error(
        error?.message || 'Something went wrong'
      );
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
      console.error('Login API Error:', error);

      throw new Error(
        error?.message || 'Something went wrong'
      );
    }
  }
};

export const searchAPI = {
  searchProfiles: async (filters) => {
    try {
      const params = new URLSearchParams(filters);
      return await apiClient(`/profiles/search?${params}`);
    } catch (error) {
      console.error('Login API Error:', error);

      throw new Error(
        error?.message || 'Something went wrong'
      );
    }
  }
};

export const masterDataAPI = {

  // ==========================================
  // RELIGIONS
  // ==========================================

  getReligions: async () => {
    try {

      console.log('🔍 Fetching religions...');

      const result = await apiClient('/religions');

      console.log('✅ MASTER API RESPONSE - Religions:', result);

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error('❌ Get Religions API error:', error);

      return [];

    }
  },

  // ==========================================
  // GENDERS
  // ==========================================

  getGenders: async () => {
    try {

      console.log('🔍 Fetching genders...');

      const result = await apiClient('/genders');

      console.log('✅ MASTER API RESPONSE - Genders:', result);

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error('❌ Get Genders API error:', error);

      return [];

    }
  },
  getAnnualIncomes: async () => {

    try {

      console.log('🔍 Fetching annual incomes...');

      const result =
        await apiClient('/annual-incomes');

      console.log(
        '✅ MASTER API RESPONSE - Annual Incomes:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Annual Incomes API error:',
        error
      );

      return [];

    }

  },

  // ==========================================
  // EDUCATION LEVELS
  // ==========================================

  getEducationLevels: async () => {
    try {

      console.log('🔍 Fetching education levels...');

      const result =
        await apiClient('/master/education-levels');

      console.log(
        '✅ MASTER API RESPONSE - Education Levels:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Education Levels API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // OCCUPATIONS
  // ==========================================

  getOccupations: async () => {
    try {

      console.log('🔍 Fetching occupations...');

      const result =
        await apiClient('/occupations');

      console.log(
        '✅ MASTER API RESPONSE - Occupations:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Occupations API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // MARITAL STATUS
  // ==========================================

  getMaritalStatuses: async () => {
    try {

      console.log('🔍 Fetching marital statuses...');

      const result =
        await apiClient('/marital-status');

      console.log(
        '✅ MASTER API RESPONSE - Marital Statuses:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Marital Statuses API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // HEIGHTS
  // ==========================================

  getHeights: async () => {
    try {

      console.log('🔍 Fetching heights...');

      const result =
        await apiClient('/heights');

      console.log(
        '✅ MASTER API RESPONSE - Heights:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Heights API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // WEIGHTS
  // ==========================================

  getWeights: async () => {
    try {

      console.log('🔍 Fetching weights...');

      const result =
        await apiClient('/master/weights');

      console.log(
        '✅ MASTER API RESPONSE - Weights:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Weights API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // COMPLEXIONS
  // ==========================================

  getComplexions: async () => {
    try {

      const result =
        await apiClient('/complexions');

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Complexions API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // BODY TYPES
  // ==========================================

  getBodyTypes: async () => {
    try {

      const result =
        await apiClient('/body-types');

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Body Types API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // COUNTRIES
  // ==========================================

  getCountries: async () => {
    try {

      console.log('🔍 Fetching countries...');

      const result =
        await apiClient('/countries');

      console.log('✅ Countries:', result);

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Countries API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // STATES
  // ==========================================

  getStates: async () => {
    try {

      console.log('🔍 Fetching states...');

      const result =
        await apiClient('/master/states');

      console.log('✅ States:', result);

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get States API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // CITIES
  // ==========================================

  getCities: async () => {
    try {

      console.log('🔍 Fetching cities...');

      const result =
        await apiClient('/cities');

      console.log(
        '✅ MASTER API RESPONSE - Cities:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Cities API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // CITIES BY STATE
  // ==========================================

  getCitiesByState: async (stateId) => {
    try {

      console.log(
        '🔍 Fetching cities by state...',
        stateId
      );

      const result =
        await apiClient(
         `/cities/state/${stateId}`
        );

      console.log(
        '✅ Cities By State:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Cities By State API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // CASTES
  // ==========================================

  getCastes: async (religionId) => {

    try {

      if (!religionId) {
        return [];
      }

      console.log(
        '🔍 Fetching castes...',
        religionId
      );

      const adminId = 1;

      const result =
        await apiClient(
          `/admins/${adminId}/castes/religion/${religionId}`
        );

      console.log(
        '✅ MASTER API RESPONSE - Castes:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Castes API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // SUB CASTES
  // ==========================================

  getSubCastes: async (casteId) => {

    try {

      if (!casteId) {
        return [];
      }

      console.log(
        '🔍 Fetching sub castes...',
        casteId
      );

      const adminId = 1;

      const result =
        await apiClient(
          `/master/sub-castes/caste/${casteId}/admin/${adminId}`
        );

      console.log(
        '✅ MASTER API RESPONSE - Sub Castes:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Sub Castes API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // MOTHER TONGUES
  // ==========================================

  getMotherTongues: async () => {
    try {

      console.log('🔍 Fetching mother tongues...');

      const result =
        await apiClient('/mother-tongues');

      console.log(
        '✅ MASTER API RESPONSE - Mother Tongues:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Mother Tongues API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // INCOMES
  // ==========================================

  getIncomes: async () => {
    try {

      console.log('🔍 Fetching incomes...');

      const result =
        await apiClient('/incomes');

      console.log(
        '✅ MASTER API RESPONSE - Incomes:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Incomes API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // DIETS
  // ==========================================

  getDiets: async () => {
    try {

      console.log('🔍 Fetching diets...');

      const result =
        await apiClient('/diets');

      console.log(
        '✅ MASTER API RESPONSE - Diets:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Diets API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // SMOKING
  // ==========================================

  getSmokingOptions: async () => {
    try {

      console.log('🔍 Fetching smoking options...');

      const result =
        await apiClient('/master/smoking');

      console.log(
        '✅ MASTER API RESPONSE - Smoking:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Smoking API error:',
        error
      );

      return [];

    }
  },

  // ==========================================
  // DRINKING
  // ==========================================

  getDrinkingOptions: async () => {
    try {

      console.log('🔍 Fetching drinking options...');

      const result =
        await apiClient('/master/drinking');

      console.log(
        '✅ MASTER API RESPONSE - Drinking:',
        result
      );

      return Array.isArray(result)
        ? result
        : [];

    } catch (error) {

      console.error(
        '❌ Get Drinking API error:',
        error
      );

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
