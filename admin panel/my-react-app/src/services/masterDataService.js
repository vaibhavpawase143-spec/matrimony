import { apiClient } from "./api";
const SUPPORTS_DELETED = [
  "Religion",
  "Caste",
  "Sub Caste",
  "Country",
  "State",
  "City",
];
// Helper to safely extract list from API responses (handles array, response.data, or response.content)
const extractData = (res) => {
  if (!res) return [];
  if (Array.isArray(res)) return res;
  if (Array.isArray(res.data)) return res.data;
  if (res.data && Array.isArray(res.data.content)) return res.data.content;
  if (Array.isArray(res.content)) return res.content;
  return [];
};

/* ==========================================================
   MASTER ENDPOINTS MAPPING
========================================================== */

const TAB_ENDPOINTS = {
  Religion: ["/master/religions"],
Caste: ["/admins/1/castes"],
  "Sub Caste": ["/master/sub-castes", "/sub-castes"],
  Country: ["/countries", "/master/countries"],
  State: ["/master/states", "/states"],
  City: ["/cities", "/master/cities"],
  Occupation: ["/master/occupations", "/occupations"],
  Qualification: ["/master/qualifications", "/qualifications"],
  Income: ["/master/incomes", "/incomes"],
  Education: ["/master/education-levels", "/education-levels"],
  Height: ["/master/heights", "/heights"],
  Weight: ["/master/weights", "/weights"],
  Diet: ["/diets", "/master/diets"],
  Smoking: ["/master/smoking", "/smoking"],
  Drinking: ["/master/drinking", "/drinking"],
  "Body Type": ["/body-types", "/master/body-types"],
  Complexion: ["/complexions", "/master/complexions"],
  "Marital Status": ["/master/marital-status", "/marital-statuses"],
  "Mother Tongue": ["/master/mother-tongues", "/mother-tongues"],
  Employment: ["/master/employed", "/employed"],
  "Family Type": ["/master/family-types", "/family-types"],
  Gender: ["/genders", "/master/genders"],
};

const masterCache = new Map();

export const clearMasterCache = (tabName) => {
  if (tabName) {
    masterCache.delete(tabName);
  } else {
    masterCache.clear();
  }
};

export const getMasterItems = async (tabName) => {
  if (masterCache.has(tabName)) {
    return masterCache.get(tabName);
  }
  const endpoints = TAB_ENDPOINTS[tabName] || [`/master/${tabName.toLowerCase().replace(/\s+/g, "-")}`];
  let allItems = [];

  for (const endpoint of endpoints) {
    try {
      const response = await apiClient(endpoint);
      const data = extractData(response);
      if (data && data.length > 0) {
        allItems = [...data];
        break;
      }
      if (Array.isArray(response) && response.length > 0) {
        allItems = [...response];
        break;
      }
    } catch {
      // Continue to fallback endpoint
    }
  }

  // Also query deleted/inactive endpoint if available to merge soft-deleted items into admin view
  if (SUPPORTS_DELETED.includes(tabName)) {
    for (const endpoint of endpoints) {
      try {
        const deletedResponse = await apiClient(`${endpoint}/deleted`);
        const deletedData = extractData(deletedResponse);

        if (deletedData.length > 0) {
          const existingIds = new Set(allItems.map(i => i.id));
          deletedData.forEach(item => {
            if (!existingIds.has(item.id)) {
              allItems.push({
                ...item,
                isActive: false,
                active: false,
                status: "Inactive",
              });
            }
          });
        }
      } catch (e) {
        console.warn(`${endpoint}/deleted not supported`);
      }
    }
  }
  masterCache.set(tabName, allItems);
  return allItems;
};

export const createMasterItem = async (tabName, itemData) => {
  clearMasterCache(tabName);
  const endpoints = TAB_ENDPOINTS[tabName] || [`/master/${tabName.toLowerCase().replace(/\s+/g, "-")}`];
  const endpoint = endpoints[0];
  const response = await apiClient(endpoint, {
    method: "POST",
    body: JSON.stringify(itemData),
  });
  return response?.data || response;
};

export const updateMasterItem = async (tabName, id, itemData) => {
  clearMasterCache(tabName);
  const endpoints = TAB_ENDPOINTS[tabName] || [`/master/${tabName.toLowerCase().replace(/\s+/g, "-")}`];
  const endpoint = endpoints[0];
  const response = await apiClient(`${endpoint}/${id}`, {
    method: "PUT",
    body: JSON.stringify(itemData),
  });
  return response?.data || response;
};

export const deleteMasterItem = async (tabName, id) => {
  clearMasterCache(tabName);
  const endpoints = TAB_ENDPOINTS[tabName] || [`/master/${tabName.toLowerCase().replace(/\s+/g, "-")}`];
  const endpoint = endpoints[0];
  return apiClient(`${endpoint}/${id}`, {
    method: "DELETE",
  });
};

export const restoreMasterItem = async (tabName, id) => {
  clearMasterCache(tabName);

  const endpoints =
    TAB_ENDPOINTS[tabName] ||
    [`/master/${tabName.toLowerCase().replace(/\s+/g, "-")}`];

  const endpoint = endpoints[0];

  try {
    return await apiClient(`${endpoint}/${id}/restore`, {
      method: "PUT",
    });
  } catch (error) {
    console.error(
      `[Restore ${tabName}] Failed for ID ${id}:`,
      error
    );
    throw error;
  }
};

/* ==========================================================
   SPECIFIC MASTER DATA FETCHERS (For Users.jsx Filters)
========================================================== */

export const getAllGenders = async () => {
  return getMasterItems("Gender");
};

export const getReligions = async () => {
  return getMasterItems("Religion");
};

export const getAllReligions = async () => {
  return getMasterItems("Religion");
};

export const createReligion = async (data) => {
  return createMasterItem("Religion", data);
};

export const updateReligion = async (id, data) => {
  return updateMasterItem("Religion", id, data);
};

export const deactivateReligion = async (id) => {
  return deleteMasterItem("Religion", id);
};

export const restoreReligion = async (id) => {
  return restoreMasterItem("Religion", id);
};

export const getCastesByReligion = async (religionId) => {
  if (!religionId) return getAllCastes();
  try {
    const res1 = await apiClient(`/castes/religion/${religionId}`);
    const data1 = extractData(res1);
    if (data1.length > 0) return data1;
  } catch {
    // ignore
  }
  try {
const res2 = await apiClient(
  `/admins/1/castes/religion/${religionId}`
);
    const data2 = extractData(res2);
    if (data2.length > 0) return data2;
  } catch {
    // ignore
  }
  const all = await getMasterItems("Caste");
  return all.filter((item) => String(item.religionId || item.religion?.id) === String(religionId));
};

export const getAllCastes = async () => {
  return getMasterItems("Caste");
};

export const getSubCastesByCaste = async (casteId) => {
  if (!casteId) return getAllSubCastes();
  try {
    const res1 = await apiClient(`/sub-castes/caste/${casteId}`);
    const data1 = extractData(res1);
    if (data1.length > 0) return data1;
  } catch {
    // ignore
  }
  try {
    const res2 = await apiClient(`/master/sub-castes?casteId=${casteId}`);
    const data2 = extractData(res2);
    if (data2.length > 0) return data2;
  } catch {
    // ignore
  }
  const all = await getMasterItems("Sub Caste");
  return all.filter((item) => String(item.casteId || item.caste?.id) === String(casteId));
};

export const getAllSubCastes = async () => {
  return getMasterItems("Sub Caste");
};

export const getCountries = async () => {
  return getMasterItems("Country");
};

export const getStates = async (countryId) => {
  if (!countryId) return getAllStates();
  try {
    const res1 = await apiClient(`/states/country/${countryId}`);
    const data1 = extractData(res1);
    if (data1.length > 0) return data1;
  } catch {
    // ignore
  }
  try {
    const res2 = await apiClient(`/master/states?countryId=${countryId}`);
    const data2 = extractData(res2);
    if (data2.length > 0) return data2;
  } catch {
    // ignore
  }
  const all = await getMasterItems("State");
  return all.filter((item) => String(item.countryId || item.country?.id) === String(countryId));
};

export const getAllStates = async () => {
  return getMasterItems("State");
};

export const getCities = async (stateId) => {
  if (!stateId) return getAllCities();
  try {
    const res1 = await apiClient(`/cities/state/${stateId}`);
    const data1 = extractData(res1);
    if (data1.length > 0) return data1;
  } catch {
    // ignore
  }
  try {
    const res2 = await apiClient(`/master/cities?stateId=${stateId}`);
    const data2 = extractData(res2);
    if (data2.length > 0) return data2;
  } catch {
    // ignore
  }
  const all = await getMasterItems("City");
  return all.filter((item) => String(item.stateId || item.state?.id) === String(stateId));
};

export const getAllCities = async () => {
  return getMasterItems("City");
};

export const getMaritalStatuses = async () => {
  return getMasterItems("Marital Status");
};

export const getEducationLevels = async () => {
  return getMasterItems("Education");
};

export const getOccupations = async () => {
  return getMasterItems("Occupation");
};