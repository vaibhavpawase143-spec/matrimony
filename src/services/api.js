// Simple API service for frontend development
// All calls return mock data for testing

const delay = (ms = 1000) => new Promise(resolve => setTimeout(resolve, ms));

export const authAPI = {
  login: async (credentials) => {
    await delay(1000);
    return {
      token: 'mock-token',
      user: {
        id: 1,
        first_name: 'Test',
        last_name: 'User',
        email: credentials.email,
        role: 'user',
        profile_completed: false
      }
    };
  },

  register: async (userData) => {
    await delay(1000);
    return {
      token: 'mock-token',
      user: {
        id: 1,
        first_name: userData.firstName,
        last_name: userData.lastName,
        email: userData.email,
        role: 'user',
        profile_completed: false
      }
    };
  },

  logout: async () => {
    await delay(500);
  },

  getCurrentUser: async () => {
    await delay(500);
    return {
      id: 1,
      first_name: 'Test',
      last_name: 'User',
      email: 'test@example.com',
      role: 'user',
      profile_completed: false
    };
  }
};

export const profileAPI = {
  getProfile: async (userId) => {
    await delay(800);
    return {
      data: {
        id: 1,
        name: "Test User",
        age: 25,
        city: "Mumbai",
        religion: "Hindu",
        education: "Bachelor's Degree",
        profession: "Software Engineer"
      }
    };
  },

  updateProfile: async (userId, data) => {
    await delay(1000);
    return { success: true };
  }
};

export const searchAPI = {
  searchProfiles: async (filters) => {
    await delay(1000);
    return {
      data: [
        {
          id: 1,
          name: "Test User 1",
          age: 25,
          city: "Mumbai",
          religion: "Hindu",
          education: "Bachelor's Degree",
          profession: "Software Engineer"
        },
        {
          id: 2,
          name: "Test User 2", 
          age: 24,
          city: "Delhi",
          religion: "Muslim",
          education: "Master's Degree",
          profession: "Doctor"
        }
      ],
      total: 2
    };
  }
};

export const masterDataAPI = {
  getReligions: async () => {
    await delay(300);
    return { data: ['Hindu', 'Muslim', 'Christian', 'Sikh'] };
  },

  getCities: async () => {
    await delay(300);
    return { data: ['Mumbai', 'Delhi', 'Bangalore', 'Chennai'] };
  },

  getEducationLevels: async () => {
    await delay(300);
    return { data: ['High School', 'Bachelor\'s Degree', 'Master\'s Degree', 'PhD'] };
  },

  getOccupations: async () => {
    await delay(300);
    return { data: ['Software Engineer', 'Doctor', 'Teacher', 'Business'] };
  }
};

export default {
  authAPI,
  profileAPI,
  searchAPI,
  masterDataAPI
};
