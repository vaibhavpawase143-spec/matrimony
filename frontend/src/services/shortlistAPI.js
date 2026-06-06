// Simple shortlist API client (uses token from localStorage)
const API_BASE = '/api/shortlists';

const getHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
};

const handleResponse = async (res) => {
  const text = await res.text();
  const data = text ? JSON.parse(text) : null;
  if (!res.ok) {
    const err = new Error(data?.message || data?.error || `Request failed: ${res.status}`);
    err.status = res.status;
    throw err;
  }
  return data;
};

export const shortlistAPI = {
  // Add shortlist - POST /api/shortlists with { profileId }
  add: async (profileId) => {
    const res = await fetch(`${API_BASE}`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({ profileId }),
    });
    return await handleResponse(res);
  },

  // Remove shortlist - DELETE /api/shortlists/{shortlistedUserId}
 remove: async (profileId) => {

 const res = await fetch(
 `${API_BASE}/${profileId}`,
 {
 method:'DELETE',
 headers:getHeaders()
 }
 );

 return await handleResponse(res);

 },
  // Get my shortlists - GET /api/shortlists/me?page=0&size=20
  getMyShortlists: async (page = 0, size = 20) => {
    const res = await fetch(`${API_BASE}/me?page=${page}&size=${size}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return await handleResponse(res);
  },

  // Check if shortlisted - GET /api/shortlists/check/{shortlistedUserId}
  checkStatus: async (shortlistedUserId) => {
    const res = await fetch(`${API_BASE}/check/${shortlistedUserId}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return await handleResponse(res);
  }
};

