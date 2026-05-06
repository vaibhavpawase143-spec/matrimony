// API service for frontend development
const API_BASE_URL = '/api'; // Will be proxied to backend

export const authAPI = {
  login: async (data, isAdmin = false) => {
    const endpoint = isAdmin ? `${API_BASE_URL}/admin/auth/login` : `${API_BASE_URL}/auth/login`;
    const response = await fetch(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error('Login failed');
    }
    const result = await response.json();
    // Store token in localStorage
    const token = isAdmin ? result.accessToken : (result.data && result.data.accessToken);
    if (token) {
      localStorage.setItem('token', token);
      localStorage.setItem('isAdmin', isAdmin);
    }
    return result;
  },

  register: async (data) => {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error('Registration failed');
    }
    return response.json();
  },

  logout: async () => {
    // TODO: implement logout endpoint if needed
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: async () => {
    const token = localStorage.getItem('token');
    if (!token) return null;

    const response = await fetch(`${API_BASE_URL}/profiles/me`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      throw new Error('Failed to get user profile');
    }
    return response.json();
  }
};

export const profileAPI = {
  getProfile: async (userId) => {
    // TODO: connect backend API
  },

  updateProfile: async (userId, data) => {
    // TODO: connect backend API
  }
};

export const searchAPI = {
  searchProfiles: async (filters) => {
    // TODO: connect backend API
  }
};

export const masterDataAPI = {
  getReligions: async () => {
    const response = await fetch(`${API_BASE_URL}/religions`);
    if (!response.ok) {
      throw new Error('Failed to fetch religions');
    }
    return response.json();
  },

  getCities: async () => {
    const response = await fetch(`${API_BASE_URL}/cities`);
    if (!response.ok) {
      throw new Error('Failed to fetch cities');
    }
    return response.json();
  },

  getEducationLevels: async () => {
    const response = await fetch(`${API_BASE_URL}/education-levels`);
    if (!response.ok) {
      throw new Error('Failed to fetch education levels');
    }
    return response.json();
  },

  getOccupations: async () => {
    const response = await fetch(`${API_BASE_URL}/occupations`);
    if (!response.ok) {
      throw new Error('Failed to fetch occupations');
    }
    return response.json();
  },

  getMaritalStatuses: async () => {
    const response = await fetch(`${API_BASE_URL}/marital-statuses`);
    if (!response.ok) {
      throw new Error('Failed to fetch marital statuses');
    }
    return response.json();
  }
};

export default {
  authAPI,
  profileAPI,
  searchAPI,
  masterDataAPI
};
