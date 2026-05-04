// API Client for React Frontend
// Place this in your React project's src/utils/api.js or src/services/api.js

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:9090';

class ApiClient {
  constructor() {
    this.baseURL = API_BASE_URL;
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const config = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      credentials: 'include', // Important for CORS with credentials
      ...options,
    };

    // Add JWT token if available
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    try {
      const response = await fetch(url, config);

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
      }

      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  // Authentication APIs
  async login(credentials) {
    return this.request('/api/users/login', {
      method: 'POST',
      body: JSON.stringify(credentials),
    });
  }

  async register(userData) {
    return this.request('/api/users/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    });
  }

  async verifyEmail(token) {
    return this.request(`/api/users/verify?token=${token}`);
  }

  // User Profile APIs
  async getUserProfile(userId) {
    return this.request(`/api/users/profile/${userId}`);
  }

  async updateUserProfile(userId, profileData) {
    return this.request(`/api/users/profile/${userId}`, {
      method: 'PUT',
      body: JSON.stringify(profileData),
    });
  }

  // Matching APIs
  async getMatches(userId) {
    return this.request(`/api/matches/${userId}`);
  }

  async getRecommendations(userId) {
    return this.request(`/api/recommendation/${userId}`);
  }

  // Payment APIs
  async getSubscriptionPlans() {
    return this.request('/api/subscription/plans');
  }

  async createPaymentOrder(userId, planId) {
    return this.request('/api/razorpay/create-order', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        userId: userId.toString(),
        planId: planId.toString(),
      }),
    });
  }

  async verifyPayment(orderId, paymentId, signature) {
    return this.request('/api/razorpay/verify-payment', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        orderId,
        paymentId,
        signature,
      }),
    });
  }

  // Chat APIs
  async getChatHistory(userId, otherUserId) {
    return this.request(`/api/chat/history/${userId}/${otherUserId}`);
  }

  async sendMessage(messageData) {
    return this.request('/api/chat/send', {
      method: 'POST',
      body: JSON.stringify(messageData),
    });
  }
}

// Create and export a singleton instance
const apiClient = new ApiClient();
export default apiClient;

// Usage example in React components:
/*
import apiClient from '../utils/api';

// Login
const handleLogin = async (credentials) => {
  try {
    const response = await apiClient.login(credentials);
    localStorage.setItem('token', response.token);
    // Handle success
  } catch (error) {
    // Handle error
  }
};

// Get user profile
const loadUserProfile = async (userId) => {
  try {
    const profile = await apiClient.getUserProfile(userId);
    setUserProfile(profile);
  } catch (error) {
    console.error('Failed to load profile:', error);
  }
};
*/
