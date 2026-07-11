// API service for frontend development
import errorHandler from '@/utils/errorHandler';
import EmailVerified from "@/pages/EmailVerified";

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
          Authorization: `Bearer ${localStorage.getItem("token")}`
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
        Authorization:
          `Bearer ${localStorage.getItem("token")}`
      }
    }
  );

  if (!response.ok) {
    throw new Error(
      "Failed to load photos"
    );
  }

  return await response.json();
},
  uploadMultiple: async (formData) => {

    const response = await fetch(
      `${API_BASE_URL}/photos/upload-multiple`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        },
        body: formData
      }
    );

    const text = await response.text();

    console.log(
      "UPLOAD RESPONSE:",
      response.status,
      text
    );

    if (!response.ok) {
      throw new Error(text || "Photo upload failed");
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
          Authorization: `Bearer ${localStorage.getItem("token")}`
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
        '/profiles/search',
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
export const masterDataAPI = {

  // ==========================================
  // RELIGIONS
  // ==========================================

getReligions: async () => {

  try {

    console.log(
      '🔍 Fetching religions...'
    );

    const result =
      await apiClient(
        '/religions'
      );

    console.log(
      '✅ MASTER API RESPONSE - Religions:',
      result
    );

    return Array.isArray(result)
      ? result
      : [];

   } catch(error){

     console.error(
       '❌ Get Religions API error:',
       error
     );

     return [];

   }

 },



// ==========================================
// GENDERS
// ==========================================

getGenders: async () => {

  try {

    console.log(
      '🔍 Fetching genders...'
    );

    const result =
      await apiClient(
        '/genders'
      );

    console.log(
      '✅ MASTER API RESPONSE - Genders:',
      result
    );

    return Array.isArray(result)
      ? result
      : [];

  } catch(error){

    console.error(
      '❌ Get Genders API error:',
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
  getProfileTypes: async () => {

  try {

  const result =

  await apiClient(

  '/profile-types'

  );

  return Array.isArray(result)

  ? result

  : [];

  }catch(error){

  console.error(

  '❌ Get Profile Types API error:',

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
// MANGLIK STATUS
// ==========================================

getManglikStatuses: async () => {

 try{

   console.log("Fetching Manglik Statuses");

  const result =
   await apiClient(
     '/manglik-statuses'
   );

   return Array.isArray(result)
      ? result
      : [];

 }catch(error){

   console.error(error);

   return [];

 }

},
getFamilyTypes: async()=>{

 try{

   const result =
      await apiClient(
        '/master/family-types'
      );

   return Array.isArray(result)
      ? result
      : [];

 }catch(error){

   return [];

 }

},
getFamilyStatuses: async()=>{

 try{

   const result =
      await apiClient(
       '/master/family-status'
      );

   return Array.isArray(result)
      ? result
      : [];

 }catch(error){

   return [];

 }

},
getFamilyValues: async()=>{

 try{

   const result =
      await apiClient(
        '/master/family-values'
      );

   return Array.isArray(result)
      ? result
      : [];

 }catch(error){

   return [];

 }

},

// ==========================================
// QUALIFICATIONS
// ==========================================

getQualifications: async () => {

 try {

   const result =
     await apiClient(
       '/qualifications'
     );

   return Array.isArray(result)
     ? result
     : [];

 } catch(error){

   console.error(error);

   return [];

 }

},

// ==========================================
// FIELD OF STUDIES
// ==========================================

getFieldsOfStudy: async () => {

 try {

   const result =
     await apiClient(
       '/fields-of-study'
     );

   return Array.isArray(result)
     ? result
     : [];

 } catch(error){

   return [];

 }

},// ==========================================
// EMPLOYED
// ==========================================

getEmploymentStatuses: async () => {

 try {

   const result =
     await apiClient(
       '/master/employed'
     );

   return Array.isArray(result)
   ? result
   : [];

 } catch(error){

   console.log(error);

   return [];

 }

},
// ==========================================
// DISABILITY STATUS
// ==========================================

getDisabilityStatuses: async () => {

 try {

  const result =
   await apiClient(
     '/master/disability-statuses'
   );
   return Array.isArray(result)
     ? result
     : [];

 } catch(error){

   return [];

 }

},

// ==========================================
// BLOOD GROUPS
// ==========================================

getBloodGroups: async () => {

 try {

   const result =
     await apiClient(
       '/blood-groups'
     );

   return Array.isArray(result)
     ? result
     : [];

 } catch(error){

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

        return Array.isArray(result)
          ? result
          : [];

      } catch (error) {

        return [];

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

   getTopMatches: async (userId, limit = 20) => {

     return await apiClient(

       `/match/recommend/${userId}?limit=${limit}`

     );

   },
   getMatchDetails: async (userId, partnerId) => {

       return await apiClient(
           `/match/${userId}/details/${partnerId}`
       );

   },
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
       `/razorpay/verify-payment?orderId=${data.orderId}&paymentId=${data.paymentId}&signature=${data.signature}`,
       {
         method: "POST"
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