// API service for frontend development
import errorHandler from '@/utils/errorHandler';
import EmailVerified from "@/pages/EmailVerified";

const API_BASE_URL = '/api'; // Will be proxied to backend

// Tab-isolated Token Retrieval Helper
export const getAuthToken = () => {
  return sessionStorage.getItem('token') || localStorage.getItem('token');
};

// Token validation helper
const validateToken = () => {
  const token = getAuthToken();
  if (!token) {
    throw new Error('No authentication token found');
  }

  try {
    // Basic JWT token validation
    const payload = JSON.parse(atob(token.split('.')[1]));
    const now = Date.now() / 1000;
    if (payload.exp && payload.exp < now) {
      throw new Error('Token expired');
    }
    return token;
  } catch (e) {
    sessionStorage.removeItem('token');
    localStorage.removeItem('token');
    throw new Error('Invalid or expired token');
  }
};

// Centralized API client with proper auth and error handling
export const apiClient = async (endpoint, options = {}) => {
  try {
    const token = getAuthToken();

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

    const response = await fetch(fullUrl, defaultOptions);
    // Auto logout if backend says session is invalid (401 on protected endpoints)
    if (response.status === 401 && !isPublicAuthEndpoint) {
      let message = "Your session has expired. Please log in again.";

      try {
        const errorData = await response.json();
        if (errorData.message) {
          message = errorData.message;
        }
      } catch {
        // Ignore JSON parsing errors
      }

      localStorage.clear();
      sessionStorage.clear();

      localStorage.setItem("sessionExpiredMessage", message);

      if (!window.location.pathname.includes("/login")) {
        window.location.replace("/login");
      }

      const err = new Error(message);
      err.status = 401;
      err.code = "TOKEN_EXPIRED";
      throw err;
    }

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      const errorMsg = errorData.message || errorData.error || `Request failed (${response.status})`;
      const error = new Error(errorMsg);
      error.status = response.status;
      error.code = errorData.code || errorData.errorCode || "API_ERROR";
      error.field = errorData.field;
      error.fieldErrors = errorData.fieldErrors;
      error.data = errorData;
      error.endpoint = endpoint;
      throw error;
    }

   const text = await response.text();

   if (!text) {
     return {};
   }

   try {
     return JSON.parse(text);
   } catch {
     return text;
   }
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




export const photoAPI = {

  getMyPhotos: async () => {
    const response = await fetch(
      `${API_BASE_URL}/photos/me`,
      {
        headers: {
          Authorization: `Bearer ${getAuthToken()}`
        }
      }
    );

    if (!response.ok) {
      throw new Error("Failed to load photos");
    }

    return await response.json();
  },

  getUserPhotos: async (userId) => {
    const response = await fetch(
      `${API_BASE_URL}/photos/user/${userId}`,
      {
        headers: {
          Authorization: `Bearer ${getAuthToken()}`
        }
      }
    );

    if (!response.ok) {
      throw new Error("Failed to load photos");
    }

    return await response.json();
  },

  upload: async (file, type = "PROFILE") => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("type", type);

    const response = await fetch(
      `${API_BASE_URL}/photos/upload`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${getAuthToken()}`
        },
        body: formData
      }
    );

    const text = await response.text();
    if (!response.ok) {
      throw new Error(text || "Photo upload failed");
    }

    try {
      return JSON.parse(text);
    } catch {
      return text;
    }
  },

  uploadMultiple: async (formData) => {
    const response = await fetch(
      `${API_BASE_URL}/photos/upload-multiple`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${getAuthToken()}`
        },
        body: formData
      }
    );

    const text = await response.text();
    if (!response.ok) {
      throw new Error(text || "Photo upload failed");
    }

    try {
      return JSON.parse(text);
    } catch {
      return text;
    }
  },

  setPrimary: async (photoId) => {
    const response = await fetch(
      `${API_BASE_URL}/photos/primary/${photoId}`,
      {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${getAuthToken()}`
        }
      }
    );

    const text = await response.text();
    if (!response.ok) {
      throw new Error(text || "Failed to set primary photo");
    }

    try {
      return JSON.parse(text);
    } catch {
      return text;
    }
  },

  deletePhoto: async (photoId) => {
    const response = await fetch(
      `${API_BASE_URL}/photos/${photoId}`,
      {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${getAuthToken()}`
        }
      }
    );

    if (!response.ok) {
      throw new Error("Failed to delete photo");
    }

    return true;
  }

};

export const notificationAPI = {

  getAll: async (userId) => {

    return await apiClient(
      `/notifications?userId=${userId}`
    );

  },

  unreadCount: async (userId) => {

    return await apiClient(
      `/notifications/unread?userId=${userId}`
    );

  },

  markRead: async (id) => {

    return await apiClient(
      `/notifications/read/${id}`,
      {
        method: "PUT"
      }
    );

  },
markAllRead: async(userId)=>{

  return await apiClient(

    `/notifications/read-all/${userId}`,

    {

      method:"PUT"

    }

  );

},
getById: async (id) => {

    return await apiClient(
        `/notifications/${id}`
    );

},
  delete: async (id) => {

    return await apiClient(
      `/notifications/${id}`,
      {
        method: "DELETE"
      }
    );

  }

};
export const authAPI = {
  login: async (data, isAdmin = false) => {
    try {
    const endpoint = isAdmin ? '/admins/login' : '/auth/login';
      const result = await apiClient(endpoint, {
        method: 'POST',
        body: JSON.stringify(data),
      });
console.log(result);
      // Handle different response formats
 const token =
     result.data?.accessToken ||
     result.accessToken ||
     result.token;

 const userData = isAdmin
     ? (result.data?.admin || result.admin)
     : (result.data?.profile || result.profile || result.user || result.data || result);

      if (token) {
        sessionStorage.setItem('token', token);
        sessionStorage.setItem('isAdmin', isAdmin);

        // Store user data for immediate access
        if (userData) {
          sessionStorage.setItem('user', JSON.stringify(userData));
        }

        // Clean legacy global auth keys to prevent multi-tab collision
        localStorage.removeItem('token');
        localStorage.removeItem('isAdmin');
        localStorage.removeItem('user');
        localStorage.removeItem('role');
      }

    return {
        success: true,
        data: userData,
        token,
        role: result.data?.role
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
      if (getAuthToken()) {
        await apiClient('/users/logout', { method: 'POST' }).catch(() => {
          // Ignore errors on logout - just clear local storage
        });
      }
    } catch (error) {
      // Ignore logout errors and proceed with cleanup
    } finally {
      // Clear tab-isolated and local storage
      sessionStorage.removeItem('token');
      sessionStorage.removeItem('user');
      sessionStorage.removeItem('role');
      sessionStorage.removeItem('isAdmin');
      sessionStorage.removeItem('refreshToken');

      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('role');
      localStorage.removeItem('isAdmin');
      localStorage.removeItem('refreshToken');
    }
  },

getCurrentUser: async () => {

    const isAdmin =
        (sessionStorage.getItem("isAdmin") || localStorage.getItem("isAdmin")) === "true";

    if (isAdmin) {

        const admin =
            sessionStorage.getItem("user") || localStorage.getItem("user");

        return admin
            ? JSON.parse(admin)
            : null;
    }

    try {

        validateToken();

        return await apiClient("/profiles/me");

    } catch {

        return null;

    }
},
 forgotPassword: async (email) => {

   return await apiClient(

     "/auth/forgot-password",

     {
       method: "POST",

       body: JSON.stringify({
         email
       })

     }

   );

 },
resetPassword: async (
  token,
  newPassword
) => {

  return await apiClient(

    "/auth/reset-password",

    {
      method: "POST",

      body: JSON.stringify({

        token,

        newPassword

      })

    }

  );

},
changePassword: async (data) => {
  return await apiClient("/users/change-password", {
    method: "PUT",
    body: JSON.stringify(data),
  });
},

};

export const otpAPI = {
  sendOTP: async (target, channel = null, purpose = "VERIFICATION") => {
    return await apiClient("/auth/otp/send", {
      method: "POST",
      body: JSON.stringify({ target, channel, purpose }),
    });
  },

  resendOTP: async (target, purpose = "VERIFICATION") => {
    return await apiClient(`/auth/otp/resend?target=${encodeURIComponent(target)}&purpose=${encodeURIComponent(purpose)}`, {
      method: "POST",
    });
  },

  verifyOTP: async (target, otp, purpose = "VERIFICATION") => {
    return await apiClient("/auth/otp/verify", {
      method: "POST",
      body: JSON.stringify({ target, otp, purpose }),
    });
  },

  sendLoginOTP: async (target) => {
    return await apiClient(`/auth/otp/login-send?target=${encodeURIComponent(target)}`, {
      method: "POST",
    });
  },

  loginWithOTP: async (target, otp) => {
    const result = await apiClient("/auth/otp/login-verify", {
      method: "POST",
      body: JSON.stringify({ target, otp, purpose: "LOGIN" }),
    });

    const token = result.data?.accessToken || result.accessToken || result.token;
    const userData = result.data?.profile || result.profile || result.user || result.data || result;

    if (token) {
      sessionStorage.setItem("token", token);
      sessionStorage.setItem("isAdmin", "false");
      if (userData) {
        sessionStorage.setItem("user", JSON.stringify(userData));
      }

      localStorage.removeItem("token");
      localStorage.removeItem("isAdmin");
      localStorage.removeItem("user");
      localStorage.removeItem("role");
    }

    return {
      success: true,
      data: userData,
      token,
      role: result.data?.role,
    };
  },
};

export const profileVisitorAPI = {

  saveVisit: async (visitedUserId) => {

    return await apiClient(
      `/profile-visitors/${visitedUserId}`,
      {
        method: "POST"
      }
    );

  },

  getMyVisitors: async () => {

    return await apiClient(
      "/profile-visitors/me"
    );

  }

};
export const blockAPI = {

  blockUser: async (
    blockerId,
    blockedId
  ) => {

    return await apiClient(

      `/block?blockerId=${blockerId}&blockedId=${blockedId}`,

      {
        method: "POST"
      }

    );

  },


  getMyBlockedUsers: async (blockerId) => {

    return await apiClient(

      `/block/my-blocked-users?blockerId=${blockerId}`,

      {
        method: "GET"
      }

    );

  },

  unblockUser: async (
    blockerId,
    blockedId
  ) => {

    return await apiClient(

      `/block?blockerId=${blockerId}&blockedId=${blockedId}`,

      {
        method: "DELETE"
      }

    );

  },

  checkBlocked: async (
    user1,
    user2
  ) => {

    return await apiClient(

      `/block/check?user1=${user1}&user2=${user2}`

    );

  }

};
export const reportAPI = {

  reportUser: async (
    reportedUserId,
    reason = "Inappropriate profile"
  ) => {

    return await apiClient(
      `/report?reportedUserId=${reportedUserId}&reason=${encodeURIComponent(reason)}`,
      {
        method: "POST"
      }
    );

  },


hasReported: async (reportedUserId) => {

  return await apiClient(
    `/report/check/${reportedUserId}`
  );

},
};

export const profileAPI = {

getProfileByUserId:

async(userId)=>{

return await apiClient(

`/profiles/user/${userId}`

);

},

getProfile: async (userId) => {

try {

const endpoint =
userId
? `/profiles/${userId}`
: '/profiles/me';

return await apiClient(endpoint);

} catch(error){

console.error(
'Profile API Error:',
error
);

throw new Error(
error?.message ||
'Something went wrong'
);

}

},

getProfileById: async(id)=>{

try{

return await apiClient(

`/profiles/${id}`

);

}catch(error){

console.error(
'Profile API Error:',
error
);

throw error;

}

},

updateProfile: async(
userId,
data
)=>{

try{

const endpoint =
userId
? `/profiles/${userId}`
: '/profiles/me';

return await apiClient(
endpoint,
{
method:'PUT',
body:JSON.stringify(data)
}
);

}catch(error){

console.error(
'Profile API Error:',
error
);

throw new Error(
error?.message ||
'Something went wrong'
);

}

},

getProfiles: async()=>{

try{

return await apiClient(
'/profiles'
);

}catch(error){

console.error(
'Profile API Error:',
error
);

throw new Error(
error?.message ||
'Something went wrong'
);

}

}

};
export const supportAPI = {

  createTicket: async (data) => {

    return await apiClient(
      "/support",
      {
        method: "POST",
        body: JSON.stringify(data)
      }
    );

  },

  getMyTickets: async () => {

    return await apiClient(
      "/support/me"
    );

  },

  getTicket: async (ticketNumber) => {

    return await apiClient(
      `/support/${ticketNumber}`
    );

  },

  closeTicket: async (ticketNumber) => {

    return await apiClient(
      `/support/${ticketNumber}/close`,
      {
        method: "PUT"
      }
    );

  }

};

export const successStoryAPI = {
  getPublishedStories: async (page = 0, size = 9) => {
    return await apiClient(`/success-stories?page=${page}&size=${size}`);
  },

  getStoryById: async (id) => {
    return await apiClient(`/success-stories/${id}`);
  },
};
export const adminSupportAPI = {

  // All tickets
  getAllTickets: async () => {
    return await apiClient(
      "/admin/support"
    );
  },

  // Ticket details
  getTicket: async (ticketNumber) => {
    return await apiClient(
      `/admin/support/${ticketNumber}`
    );
  },

  // Update status
  updateStatus: async (ticketNumber, status) => {
    return await apiClient(
      `/admin/support/${ticketNumber}/status`,
      {
        method: "PUT",
        body: JSON.stringify({ status })
      }
    );
  },

  // Reply to ticket
  replyTicket: async (ticketNumber, reply) => {
    return await apiClient(
      `/admin/support/${ticketNumber}/reply`,
      {
        method: "PUT",
        body: JSON.stringify({ reply })
      }
    );
  }

};
 export const interestAPI = {

getReceivedPendingInterests:

async(receiverId)=>{

return await apiClient(

`/interests/received/${receiverId}/pending`

);

},
getReceivedInterests:

async(receiverId)=>{

return await apiClient(

`/interests/received/${receiverId}`

);

},

acceptInterest:

async(id)=>{

return await apiClient(

`/interests/accept/${id}`,

{

method:"PUT"

}

);

},

rejectInterest:

async(id)=>{

return await apiClient(

`/interests/reject/${id}`,

{

method:"PUT"

}

);

},
sendInterest: async (
senderId,
receiverId
)=>{

try{

return await apiClient(

'/interests/send',

{

method:'POST',

body:JSON.stringify({

senderId: senderId,

receiverId: receiverId

})

}

);

}catch(error){

console.error(
'Interest API Error:',
error
);

throw error;

}

},

getSentInterests: async(
senderId
)=>{

try{

return await apiClient(

`/interests/sent/${senderId}`

);

}catch(error){

console.error(
'Interest API Error:',
error
);

return [];

}

}

};
export const searchAPI = {

  searchProfiles: async (filters = {}) => {

    try {

      return await apiClient(
        '/search/profiles',
        {
          method: 'POST',
          body: JSON.stringify(filters)
        }
      );

    } catch (error) {

      console.error(
        'Search API Error:',
        error
      );

      throw new Error(
        error?.message ||
        'Something went wrong'
      );

    }

  }

};
let bulkMasterMemoryCache = null;
let bulkMasterFetchPromise = null;

const getBulkCache = () => {
  if (bulkMasterMemoryCache && Object.keys(bulkMasterMemoryCache).length > 0) {
    return bulkMasterMemoryCache;
  }
  try {
    const stored = sessionStorage.getItem("gathbandhan_bulk_master_v2");
    if (stored) {
      bulkMasterMemoryCache = JSON.parse(stored);
      return bulkMasterMemoryCache;
    }
  } catch (e) {}
  return null;
};

const getCachedKeyOrFetch = async (key, fallbackPath) => {
  let cache = getBulkCache();
  if (!cache || Object.keys(cache).length === 0) {
    cache = await masterDataAPI.getAllMasterData();
  }
  if (cache && cache[key] && Array.isArray(cache[key])) {
    return cache[key];
  }
  if (fallbackPath) {
    try {
      const res = await apiClient(fallbackPath);
      const data = Array.isArray(res?.data) ? res.data : Array.isArray(res) ? res : [];
      return data;
    } catch (e) {
      console.error(`Failed to fetch fallback path ${fallbackPath}:`, e);
      return [];
    }
  }
  return [];
};

export const masterDataAPI = {
  getAllMasterData: async () => {
    const cached = getBulkCache();
    if (cached && Object.keys(cached).length > 0) {
      return cached;
    }
    if (bulkMasterFetchPromise) {
      return bulkMasterFetchPromise;
    }
    bulkMasterFetchPromise = (async () => {
      try {
        const result = await apiClient('/master/all');
        const data = result?.data || result || {};
        if (data && typeof data === 'object' && Object.keys(data).length > 0) {
          bulkMasterMemoryCache = data;
          try {
            sessionStorage.setItem("gathbandhan_bulk_master_v2", JSON.stringify(data));
          } catch (e) {}
        }
        return data;
      } catch (error) {
        console.error('❌ Get All Master Data API error:', error);
        return {};
      } finally {
        bulkMasterFetchPromise = null;
      }
    })();
    return bulkMasterFetchPromise;
  },

  getReligions: async () => getCachedKeyOrFetch('religions', '/master/religions'),
  getGenders: async () => getCachedKeyOrFetch('genders', '/genders'),
  getEducationLevels: async () => getCachedKeyOrFetch('educationLevels', '/master/education-levels'),
  getOccupations: async () => getCachedKeyOrFetch('occupations', '/master/occupations'),
  getProfileTypes: async () => getCachedKeyOrFetch('profileTypes', '/master/profile-types'),
  getMaritalStatuses: async () => getCachedKeyOrFetch('maritalStatuses', '/master/marital-status'),
  getHeights: async () => getCachedKeyOrFetch('heights', '/heights'),
  getWeights: async () => getCachedKeyOrFetch('weights', '/master/weights'),
  getComplexions: async () => getCachedKeyOrFetch('complexions', '/complexions'),
  getBodyTypes: async () => getCachedKeyOrFetch('bodyTypes', '/body-types'),
  getCountries: async () => getCachedKeyOrFetch('countries', '/countries'),
  getStates: async () => getCachedKeyOrFetch('states', '/master/states'),
  getCities: async () => getCachedKeyOrFetch('cities', '/cities'),
  getCitiesByState: async (stateId) => {
    if (!stateId) return [];
    const allCities = await getCachedKeyOrFetch('cities', '/cities');
    if (Array.isArray(allCities)) {
      return allCities.filter(c => Number(c.stateId) === Number(stateId) || Number(c.state?.id) === Number(stateId));
    }
    return [];
  },
  getCastes: async (religionId) => {
    const allCastes = await getCachedKeyOrFetch('castes', '/master/castes');
    if (religionId && Array.isArray(allCastes)) {
      return allCastes.filter(c => Number(c.religionId) === Number(religionId) || Number(c.religion?.id) === Number(religionId));
    }
    return allCastes;
  },
  getSubCastes: async (casteId) => {
    const allSubCastes = await getCachedKeyOrFetch('subCastes', '/master/sub-castes');
    if (casteId && Array.isArray(allSubCastes)) {
      return allSubCastes.filter(sc => Number(sc.casteId) === Number(casteId) || Number(sc.caste?.id) === Number(casteId));
    }
    return allSubCastes;
  },
  getMotherTongues: async () => getCachedKeyOrFetch('motherTongues', '/master/mother-tongues'),
  getIncomes: async () => getCachedKeyOrFetch('incomes', '/master/incomes'),
  getDiets: async () => getCachedKeyOrFetch('diets', '/diets'),
  getSmokingOptions: async () => getCachedKeyOrFetch('smokingOptions', '/master/smoking'),
  getDrinkingOptions: async () => getCachedKeyOrFetch('drinkingOptions', '/master/drinking'),
  getManglikStatuses: async () => getCachedKeyOrFetch('manglikStatuses', '/master/manglik-status'),
  getFamilyTypes: async () => getCachedKeyOrFetch('familyTypes', '/master/family-types'),
  getFamilyStatuses: async () => getCachedKeyOrFetch('familyStatuses', '/master/family-status'),
  getFamilyValues: async () => getCachedKeyOrFetch('familyValues', '/master/family-values'),
  getQualifications: async () => getCachedKeyOrFetch('qualifications', '/master/qualifications'),
  getFieldsOfStudy: async () => getCachedKeyOrFetch('fieldsOfStudy', '/master/fields-of-study'),
  getEmploymentStatuses: async () => getCachedKeyOrFetch('employmentStatuses', '/master/employed'),
  getDisabilityStatuses: async () => getCachedKeyOrFetch('disabilityStatuses', '/disability-statuses'),
  getBloodGroups: async () => getCachedKeyOrFetch('bloodGroups', '/blood-groups'),
};
  export const faqAPI = {

    getPublishedFaqs: async () => {

      try {

        const result = await apiClient("/faqs");

        return result?.data ?? [];

      } catch (error) {

        console.error("FAQ API Error:", error);

        return [];

      }

    }

  };
  export const cmsAPI = {

    getPage: async (pageKey) => {

      try {

        const result = await apiClient(`/cms/${pageKey}`);

        return result?.data ?? result;

      } catch (error) {

        console.error("CMS API Error:", error);

        return null;

      }

    }

  };

 export const partnerPreferenceAPI = {

getMyPreference: async(userId)=>{

return await apiClient(

`/partner-preferences/user/${userId}`

);

},
 save: async (data) => {

 return await apiClient(

 '/partner-preferences',

 {

 method:'POST',

 body:JSON.stringify(data)

 }

 );

 },

 update: async(userId,data)=>{

 return await apiClient(

 `/partner-preferences/${userId}`,

 {

 method:"PUT",

 body:JSON.stringify(data)

 }

 );

 },

 getByUserId: async(userId)=>{

 return await apiClient(

 `/partner-preferences/user/${userId}`

 );

 }

 };
 export const matchAPI = {

     getTopMatches: async (
         userId,
         page = 0,
         size = 20
     ) => {

         return await apiClient(
             `/match/recommend/${userId}?page=${page}&size=${size}`
         );

     },

     getMatchDetails: async (
         userId,
         partnerId
     ) => {

         return await apiClient(
             `/match/${userId}/details/${partnerId}`
         );

     },

 };
 export const notificationPreferenceAPI = {

   getMyPreferences: async () => {
     return await apiClient("/notification-preferences/me");
   },

   updatePreferences: async (data) => {
     return await apiClient("/notification-preferences", {
       method: "PUT",
       body: JSON.stringify(data),
     });
   },

 };
 export const dashboardAPI = {

     getSummary: async () => {
         return await apiClient("/dashboard/summary");
     }

 };
 export const subscriptionAPI = {

   // ==========================
   // GET ALL PLANS
   // ==========================
   getPlans: async () => {

     try {

       return await apiClient("/subscription/plans");

     } catch (error) {

       console.error("Subscription API Error:", error);

       return [];

     }

   },

   // ==========================
   // BUY PLAN
   // ==========================
   subscribe: async (data) => {

     try {

       return await apiClient(
         "/subscription/subscribe",
         {
           method: "POST",
           body: JSON.stringify(data)
         }
       );

     } catch (error) {

       console.error("Subscription API Error:", error);

       throw error;

     }

   },

   // ==========================
   // CREATE ORDER
   // ==========================
   createOrder: async (planId) => {

     return await apiClient(
       `/razorpay/create-order?planId=${planId}`,
       {
         method: "POST"
       }
     );

   },

   // ==========================
   // VERIFY PAYMENT
   // ==========================
   verifyPayment: async (data) => {

     return await apiClient(
       "/razorpay/verify-payment",
       {
         method: "POST",
         body: JSON.stringify(data)
       }
     );

   },

   // ==========================
   // PAYMENT STATUS
   // ==========================
   getPaymentStatus: async (orderId) => {

     return await apiClient(
       `/razorpay/payment-status/${orderId}`
     );

   },

   // ==========================
   // MY SUBSCRIPTION
   // ==========================
   getMySubscription: async () => {

     try {

       return await apiClient("/subscription/me");

     } catch (error) {

       console.error("Subscription API Error:", error);

       return null;

     }

   },

   // ==========================
   // HISTORY
   // ==========================
   getHistory: async () => {

     try {

       return await apiClient("/subscription/history");

     } catch (error) {

       console.error("Subscription API Error:", error);

       return [];

     }

   },

   // ==========================
   // CANCEL SUBSCRIPTION
   // ==========================
   cancelSubscription: async () => {

     try {

       return await apiClient(
         "/subscription/cancel",
         {
           method: "PUT"
         }
       );

     } catch (error) {

       console.error("Subscription API Error:", error);

       throw error;

     }

   }

 };