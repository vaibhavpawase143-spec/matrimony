import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { FaSearch, FaFilter, FaChevronDown, FaChevronUp, FaUndo, FaCheck } from "react-icons/fa";
import { toast } from "sonner";
import ExportDropdown from "../components/common/ExportDropdown";
import { exportToPDF } from "../utils/export/pdfExport";
import { exportToCSV } from "../utils/export/csvExport";
import { exportToExcel } from "../utils/export/excelExport";
import {
  getUsers,
  activateUser,
  deactivateUser,
  blockUser,
  unblockUser,
  restoreUser,
  softDeleteUser,

  // Bulk Operations
  bulkActivateUsers,

  bulkSoftDeleteUsers,
} from "../services/adminUserService";
import {
  getAllGenders,
  getAllReligions,
  getCastesByReligion,
  getAllCastes,
  getSubCastesByCaste,
  getAllSubCastes,
  getCountries,
  getStates,
  getAllStates,
  getCities,
  getAllCities,
  getMaritalStatuses,
  getEducationLevels,
  getOccupations,
} from "../services/masterDataService";
import UserActionMenu from "../components/users/UserActionMenu";
import ConfirmModal from "../components/common/ConfirmModal";

export default function Users() {
  // Helper to filter ACTIVE items only for user filters
  const filterActiveOnly = (list) => {
    if (!Array.isArray(list)) return [];
    return list.filter((item) => {
      if (!item) return false;
      if (typeof item === "string" || typeof item === "number") return true;
      if (item.active === false) return false;
      if (item.isActive === false) return false;
      if (item.status === "Inactive" || item.status === "DELETED") return false;
      return true;
    });
  };

  // ===========================
  // STATES
  // ===========================

  const [search, setSearch] = useState("");

  const [users, setUsers] = useState([]);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const [page, setPage] = useState(0);

  const [size, setSize] = useState(10);

  const [totalPages, setTotalPages] = useState(0);

  const [totalUsers, setTotalUsers] = useState(0);

  const [selectedUser, setSelectedUser] = useState(null);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [selectedAction, setSelectedAction] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isBulkAction, setIsBulkAction] = useState(false);

  // Filter Panel Toggle State
  const [showFilters, setShowFilters] = useState(false);

  // ===========================
  // MASTER DATA STATES
  // ===========================

  const [genders, setGenders] = useState([]);
  const [religions, setReligions] = useState([]);
  const [allCastesList, setAllCastesList] = useState([]);
  const [castes, setCastes] = useState([]);
  const [allSubCastesList, setAllSubCastesList] = useState([]);
  const [subCastes, setSubCastes] = useState([]);
  const [countries, setCountries] = useState([]);
  const [allStatesList, setAllStatesList] = useState([]);
  const [statesList, setStatesList] = useState([]);
  const [allCitiesList, setAllCitiesList] = useState([]);
  const [cities, setCities] = useState([]);
  const [maritalStatuses, setMaritalStatuses] = useState([]);
  const [educationLevels, setEducationLevels] = useState([]);
  const [occupations, setOccupations] = useState([]);

  // ===========================
  // FILTERS STATE
  // ===========================

  const [filters, setFilters] = useState({
    genderId: "",
    religionId: "",
    casteId: "",
    subCasteId: "",
    countryId: "",
    stateId: "",
    cityId: "",
    maritalStatusId: "",
    educationLevelId: "",
    occupationId: "",
    isPremium: "",
    active: "",
    blocked: "",
    emailVerified: "",
    phoneVerified: "",
    role: "",
    minAge: "",
    maxAge: "",
    registeredFrom: "",
    registeredTo: "",
  });

  const openConfirmModal = (user, action) => {
    setSelectedUser(user);
    setSelectedAction(action);
    setIsModalOpen(true);
  };

  const navigate = useNavigate();

  // ===========================
  // LOAD MASTER DATA ON MOUNT (Active items only for user filters)
  // ===========================

  useEffect(() => {
    const fetchMasterData = async () => {
      try {
        const [
          gData,
          rData,
          cData,
          scData,
          cntData,
          stData,
          ctData,
          mData,
          eData,
          oData,
        ] = await Promise.all([
          getAllGenders(),
          getAllReligions(),
          getAllCastes(),
          getAllSubCastes(),
          getCountries(),
          getAllStates(),
          getAllCities(),
          getMaritalStatuses(),
          getEducationLevels(),
          getOccupations(),
        ]);

        setGenders(filterActiveOnly(gData));
        setReligions(filterActiveOnly(rData));

        const activeCastes = filterActiveOnly(cData);
        setAllCastesList(activeCastes);
        setCastes(activeCastes);

        const activeSubCastes = filterActiveOnly(scData);
        setAllSubCastesList(activeSubCastes);
        setSubCastes(activeSubCastes);

        setCountries(filterActiveOnly(cntData));

        const activeStates = filterActiveOnly(stData);
        setAllStatesList(activeStates);
        setStatesList(activeStates);

        const activeCities = filterActiveOnly(ctData);
        setAllCitiesList(activeCities);
        setCities(activeCities);

        setMaritalStatuses(filterActiveOnly(mData));
        setEducationLevels(filterActiveOnly(eData));
        setOccupations(filterActiveOnly(oData));
      } catch (err) {
        console.error("Error loading master data:", err);
      }
    };

    fetchMasterData();
  }, []);

  // ===========================
  // DEPENDENT DROPDOWNS FETCHING
  // ===========================

  // Fetch Castes when Religion changes
  useEffect(() => {
    if (filters.religionId) {
      getCastesByReligion(filters.religionId).then((data) => {
        const active = filterActiveOnly(data);
        setCastes(active.length > 0 ? active : allCastesList);
      });
    } else {
      setCastes(allCastesList);
    }
  }, [filters.religionId, allCastesList]);

  // Fetch Sub-Castes when Caste changes
  useEffect(() => {
    if (filters.casteId) {
      getSubCastesByCaste(filters.casteId).then((data) => {
        const active = filterActiveOnly(data);
        setSubCastes(active.length > 0 ? active : allSubCastesList);
      });
    } else {
      setSubCastes(allSubCastesList);
    }
  }, [filters.casteId, allSubCastesList]);

  // Fetch States when Country changes
  useEffect(() => {
    if (filters.countryId) {
      getStates(filters.countryId).then((data) => {
        const active = filterActiveOnly(data);
        setStatesList(active.length > 0 ? active : allStatesList);
      });
    } else {
      setStatesList(allStatesList);
    }
  }, [filters.countryId, allStatesList]);

  // Fetch Cities when State changes
  useEffect(() => {
    if (filters.stateId) {
      getCities(filters.stateId).then((data) => {
        const active = filterActiveOnly(data);
        setCities(active.length > 0 ? active : allCitiesList);
      });
    } else {
      setCities(allCitiesList);
    }
  }, [filters.stateId, allCitiesList]);

  // ===========================
  // FILTER HANDLERS
  // ===========================

  const handleFilterChange = (e) => {
    const { name, value } = e.target;

    setFilters((prev) => {
      const nextFilters = { ...prev, [name]: value };

      // Clear child dropdowns when parent changes
      if (name === "religionId") {
        nextFilters.casteId = "";
        nextFilters.subCasteId = "";
      } else if (name === "casteId") {
        nextFilters.subCasteId = "";
      } else if (name === "countryId") {
        nextFilters.stateId = "";
        nextFilters.cityId = "";
      } else if (name === "stateId") {
        nextFilters.cityId = "";
      }

      return nextFilters;
    });
  };

  const handleApplyFilters = () => {
    setPage(0);
    loadUsers(0, search, filters);
  };

  const handleResetFilters = () => {
    const initialFilters = {
      genderId: "",
      religionId: "",
      casteId: "",
      subCasteId: "",
      countryId: "",
      stateId: "",
      cityId: "",
      maritalStatusId: "",
      educationLevelId: "",
      occupationId: "",
      isPremium: "",
      active: "",
      blocked: "",
      emailVerified: "",
      phoneVerified: "",
      role: "",
      minAge: "",
      maxAge: "",
      registeredFrom: "",
      registeredTo: "",
    };

    setFilters(initialFilters);
    setSearch("");
    setPage(0);

    setCastes(allCastesList);
    setSubCastes(allSubCastesList);
    setStatesList(allStatesList);
    setCities(allCitiesList);

    loadUsers(0, "", initialFilters);
  };

  const activeFilterCount = Object.values(filters).filter(
    (val) => val !== "" && val !== null && val !== undefined
  ).length;

  // ===========================
  // HANDLE USER ACTION
  // ===========================

  const handleConfirmAction = async () => {
    if (!selectedUser) return;

    try {
      switch (selectedAction) {
        case "activate":
          await activateUser(selectedUser.id);
          break;

        case "deactivate":
          await deactivateUser(selectedUser.id);
          break;


        case "restore":
          await restoreUser(selectedUser.id);
          break;

        case "softDelete":
          await softDeleteUser(selectedUser.id);
          break;

        default:
          return;
      }

      // Reload latest data
      await loadUsers(page, search, filters);

      // Close modal
      setIsModalOpen(false);
      setSelectedUser(null);
      setSelectedAction("");

      toast.success("Action completed successfully.");
    } catch (err) {
      console.error(err);
      const message =
        err?.response?.data?.message ||
        err?.message ||
        "Something went wrong.";

      toast.error(message);
    }
  };

  // ===========================
  // LOAD USERS
  // ===========================

  const loadUsers = async (
    currentPage = page,
    currentSearch = search,
    currentFilters = filters
  ) => {
    try {
      setLoading(true);

      const response = await getUsers(
        currentPage,
        size,
        currentSearch,
        currentFilters
      );

      const pageData = response.data;

      setUsers(pageData.content || []);

      setTotalPages(pageData.totalPages || 0);

      setTotalUsers(pageData.totalElements || 0);

      setError("");
    } catch (err) {
      console.error(err);

      setError(err.message || "Failed to load users.");
    } finally {
      setLoading(false);
    }
  };

  const userColumns = [
    {
      label: "ID",
      value: (user) => user.id,
    },
    {
      label: "Name",
      value: (user) => user.fullName,
    },
    {
      label: "Email",
      value: (user) => user.email,
    },
    {
      label: "Phone",
      value: (user) => user.phone || "-",
    },
    {
      label: "Gender",
      value: (user) => user.gender || "-",
    },
    {
      label: "Plan",
      value: (user) =>
        user.premium
          ? user.premiumPlan || "Premium"
          : "Free Plan",
    },
    {
      label: "Status",
      value: (user) =>
        user.blocked
          ? "Blocked"
          : user.active
          ? "Active"
          : "Inactive",
    },
    {
      label: "City",
      value: (user) => user.city || "-",
    },
  ];

  const handleExportCSV = () => {
    exportToCSV({
      data: users,
      columns: userColumns,
      fileName: "Users_Report",
    });
  };

  const handleExportExcel = () => {
    exportToExcel({
      data: users,
      columns: userColumns,
      fileName: "Users_Report",
    });
  };

  // ===========================
  // HANDLE BULK ACTION
  // ===========================

  const handleBulkAction = async (action) => {
    if (selectedUsers.length === 0) {
      toast.error("Please select at least one user.");
      return;
    }

    try {
      switch (action) {
        case "activate":
          await bulkActivateUsers(selectedUsers);
          toast.success("Selected users activated successfully.");
          break;



        case "softDelete":
          await bulkSoftDeleteUsers(selectedUsers);
          toast.success("Selected users soft deleted successfully.");
          break;

        default:
          return;
      }

      // Clear selected checkboxes
      setSelectedUsers([]);
      setIsModalOpen(false);
      setIsBulkAction(false);
      setSelectedAction("");
      // Reload current page
      await loadUsers(page, search, filters);
    } catch (err) {
      console.error("Bulk action failed:", err);

      toast.error(
        err?.message ||
        "Bulk operation failed."
      );
    }
  };

  const handleModalConfirm = async () => {
    // Bulk action
    if (isBulkAction) {
      await handleBulkAction(selectedAction);

      setIsModalOpen(false);
      setIsBulkAction(false);
      setSelectedAction("");

      return;
    }

    // Normal single-user action
    await handleConfirmAction();
  };

  // ===========================
  // INITIAL LOAD
  // ===========================

  useEffect(() => {
    loadUsers(page, search, filters);
  }, [page, size]);

  // ===========================
  // SEARCH (Debounce)
  // ===========================

  useEffect(() => {
    const timer = setTimeout(() => {
      setPage(0);
      loadUsers(0, search, filters);
    }, 500);

    return () => clearTimeout(timer);
  }, [search]);

  // ===========================
  // LOADING
  // ===========================

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-lg font-medium text-gray-600">
          Loading users...
        </div>
      </div>
    );
  }

  // ===========================
  // ERROR
  // ===========================

  if (error) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-red-600 font-medium">
          {error}
        </div>
      </div>
    );
  }

  // Helper for rendering option names dynamically
  const getItemLabel = (item) => {
    if (item === null || item === undefined) return "";
    if (typeof item === "string" || typeof item === "number") return String(item);
    return (
      item.name ||
      item.religionName ||
      item.casteName ||
      item.subCasteName ||
      item.countryName ||
      item.stateName ||
      item.cityName ||
      item.genderName ||
      item.occupationName ||
      item.degreeName ||
      item.maritalStatusName ||
      item.educationLevelName ||
      item.title ||
      item.label ||
      item.value ||
      (item.id ? `ID #${item.id}` : "")
    );
  };

  const getItemValue = (item) => {
    if (item === null || item === undefined) return "";
    if (typeof item === "string" || typeof item === "number") return String(item);
    return item.id !== undefined && item.id !== null ? String(item.id) : String(item.value || getItemLabel(item));
  };

  // ===========================
  // JSX STARTS HERE
  // ===========================

  return (
    <div className="p-6">
      {/* ================= HEADER ================= */}
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">
            User Management
          </h1>

          <p className="text-gray-500">
            Manage all registered users.
          </p>
        </div>

        <ExportDropdown
          onPDF={() =>
            exportToPDF({
              data: users,
              columns: userColumns,
              title: "User Management Report",
              fileName: "Users_Report",
            })
          }
          onExcel={handleExportExcel}
          onCSV={handleExportCSV}
        />
      </div>

      {/* ================= SEARCH & FILTER TOGGLE BAR ================= */}

      <div className="bg-white shadow-md rounded-xl p-4 mb-6 flex flex-col md:flex-row items-center justify-between gap-4 border border-gray-200">
        <div className="flex items-center gap-3 w-full md:w-auto flex-1">
          <FaSearch className="text-gray-400" />
          <input
            type="text"
            placeholder="Search by Name or Email..."
            className="w-full outline-none text-sm text-gray-700 placeholder-gray-400"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <button
          type="button"
          onClick={() => setShowFilters((prev) => !prev)}
          className="flex items-center gap-2 px-4 py-2 bg-purple-50 text-purple-700 border border-purple-200 hover:bg-purple-100 rounded-lg text-sm font-medium transition cursor-pointer shrink-0"
        >
          <FaFilter className="text-purple-600" />
          <span>Filters</span>
          {activeFilterCount > 0 && (
            <span className="bg-purple-600 text-white text-xs font-bold px-2 py-0.5 rounded-full">
              {activeFilterCount}
            </span>
          )}
          {showFilters ? <FaChevronUp className="text-xs" /> : <FaChevronDown className="text-xs" />}
        </button>
      </div>

      {/* ================= ADVANCED FILTER PANEL ================= */}

      {showFilters && (
        <div className="bg-white shadow-md rounded-2xl p-6 mb-6 border border-purple-100 transition-all duration-300">
          <div className="flex justify-between items-center mb-4 pb-3 border-b border-gray-100">
            <h3 className="text-lg font-semibold text-gray-800 flex items-center gap-2">
              <FaFilter className="text-purple-600 text-sm" />
              Advanced Filters
            </h3>
            {activeFilterCount > 0 && (
              <span className="text-xs text-purple-600 font-medium">
                {activeFilterCount} active {activeFilterCount === 1 ? "filter" : "filters"}
              </span>
            )}
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 mb-6">
            {/* 1. Gender */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Gender
              </label>
              <select
                name="genderId"
                value={filters.genderId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Genders</option>
                {genders.length > 0 ? (
                  genders.map((g, idx) => (
                    <option key={g.id || idx} value={getItemValue(g)}>
                      {getItemLabel(g)}
                    </option>
                  ))
                ) : (
                  <>
                    <option value="1">Male</option>
                    <option value="2">Female</option>
                    <option value="3">Other</option>
                  </>
                )}
              </select>
            </div>

            {/* 2. Religion */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Religion
              </label>
              <select
                name="religionId"
                value={filters.religionId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Religions</option>
                {religions.map((r, idx) => (
                  <option key={r.id || idx} value={getItemValue(r)}>
                    {getItemLabel(r)}
                  </option>
                ))}
              </select>
            </div>

            {/* 3. Caste */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Caste
              </label>
              <select
                name="casteId"
                value={filters.casteId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Castes</option>
                {castes.map((c, idx) => (
                  <option key={c.id || idx} value={getItemValue(c)}>
                    {getItemLabel(c)}
                  </option>
                ))}
              </select>
            </div>

            {/* 4. Sub Caste */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Sub Caste
              </label>
              <select
                name="subCasteId"
                value={filters.subCasteId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Sub Castes</option>
                {subCastes.map((sc, idx) => (
                  <option key={sc.id || idx} value={getItemValue(sc)}>
                    {getItemLabel(sc)}
                  </option>
                ))}
              </select>
            </div>

            {/* 5. Country */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Country
              </label>
              <select
                name="countryId"
                value={filters.countryId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Countries</option>
                {countries.map((cnt, idx) => (
                  <option key={cnt.id || idx} value={getItemValue(cnt)}>
                    {getItemLabel(cnt)}
                  </option>
                ))}
              </select>
            </div>

            {/* 6. State */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                State
              </label>
              <select
                name="stateId"
                value={filters.stateId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All States</option>
                {statesList.map((st, idx) => (
                  <option key={st.id || idx} value={getItemValue(st)}>
                    {getItemLabel(st)}
                  </option>
                ))}
              </select>
            </div>

            {/* 7. City */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                City
              </label>
              <select
                name="cityId"
                value={filters.cityId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Cities</option>
                {cities.map((ct, idx) => (
                  <option key={ct.id || idx} value={getItemValue(ct)}>
                    {getItemLabel(ct)}
                  </option>
                ))}
              </select>
            </div>

            {/* 8. Marital Status */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Marital Status
              </label>
              <select
                name="maritalStatusId"
                value={filters.maritalStatusId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Marital Statuses</option>
                {maritalStatuses.map((ms, idx) => (
                  <option key={ms.id || idx} value={getItemValue(ms)}>
                    {getItemLabel(ms)}
                  </option>
                ))}
              </select>
            </div>

            {/* 9. Education Level */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Education Level
              </label>
              <select
                name="educationLevelId"
                value={filters.educationLevelId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Education Levels</option>
                {educationLevels.map((ed, idx) => (
                  <option key={ed.id || idx} value={getItemValue(ed)}>
                    {getItemLabel(ed)}
                  </option>
                ))}
              </select>
            </div>

            {/* 10. Occupation */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Occupation
              </label>
              <select
                name="occupationId"
                value={filters.occupationId}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Occupations</option>
                {occupations.map((occ, idx) => (
                  <option key={occ.id || idx} value={getItemValue(occ)}>
                    {getItemLabel(occ)}
                  </option>
                ))}
              </select>
            </div>

            {/* 11. Premium */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Premium
              </label>
              <select
                name="isPremium"
                value={filters.isPremium}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All</option>
                <option value="true">Premium Only</option>
                <option value="false">Free Only</option>
              </select>
            </div>

            {/* 12. Active */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Active Status
              </label>
              <select
                name="active"
                value={filters.active}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All</option>
                <option value="true">Active</option>
                <option value="false">Inactive</option>
              </select>
            </div>

            {/* 13. Blocked */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Blocked Status
              </label>
              <select
                name="blocked"
                value={filters.blocked}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All</option>

              </select>
            </div>

            {/* 14. Email Verified */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Email Verified
              </label>
              <select
                name="emailVerified"
                value={filters.emailVerified}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All</option>
                <option value="true">Verified</option>
                <option value="false">Unverified</option>
              </select>
            </div>

            {/* 15. Phone Verified */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Phone Verified
              </label>
              <select
                name="phoneVerified"
                value={filters.phoneVerified}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All</option>
                <option value="true">Verified</option>
                <option value="false">Unverified</option>
              </select>
            </div>

            {/* 16. Role */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Role
              </label>
              <select
                name="role"
                value={filters.role}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              >
                <option value="">All Roles</option>
                <option value="USER">User</option>
                <option value="ADMIN">Admin</option>
              </select>
            </div>

            {/* 17. Minimum Age */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Minimum Age
              </label>
              <input
                type="number"
                name="minAge"
                placeholder="e.g. 18"
                min="18"
                max="100"
                value={filters.minAge}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              />
            </div>

            {/* 18. Maximum Age */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Maximum Age
              </label>
              <input
                type="number"
                name="maxAge"
                placeholder="e.g. 60"
                min="18"
                max="100"
                value={filters.maxAge}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white"
              />
            </div>

            {/* 19. Registration From */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Registration From
              </label>
              <input
                type="date"
                name="registeredFrom"
                value={filters.registeredFrom}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white text-gray-700"
              />
            </div>

            {/* 20. Registration To */}
            <div>
              <label className="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                Registration To
              </label>
              <input
                type="date"
                name="registeredTo"
                value={filters.registeredTo}
                onChange={handleFilterChange}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 bg-white text-gray-700"
              />
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex items-center justify-end gap-3 pt-4 border-t border-gray-100">
            <button
              type="button"
              onClick={handleResetFilters}
              className="flex items-center gap-2 px-5 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-700 text-sm font-medium hover:bg-gray-50 transition cursor-pointer"
            >
              <FaUndo className="text-xs" />
              Reset Filters
            </button>
            <button
              type="button"
              onClick={handleApplyFilters}
              className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-purple-600 text-white text-sm font-medium hover:bg-purple-700 transition cursor-pointer shadow-sm"
            >
              <FaCheck className="text-xs" />
              Apply Filters
            </button>
          </div>
        </div>
      )}

      {/* ================= TABLE ================= */}

      <div className="bg-white rounded-2xl shadow-md overflow-visible border border-purple-100">
        {/* ================= BULK ACTION TOOLBAR ================= */}

        {selectedUsers.length > 0 && (
          <div className="bg-violet-50 border border-violet-200 rounded-xl p-4 mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div className="text-sm font-semibold text-violet-800">
              {selectedUsers.length}{" "}
              {selectedUsers.length === 1 ? "user" : "users"} selected
            </div>

            <div className="flex flex-wrap items-center gap-2">
              <button
                type="button"
                onClick={() => handleBulkAction("activate")}
                className="px-4 py-2 rounded-lg bg-green-600 text-white text-sm font-medium hover:bg-green-700 transition"
              >
                Activate
              </button>



              <button
                type="button"
                onClick={() => {
                  setSelectedAction("softDelete");
                  setIsBulkAction(true);
                  setIsModalOpen(true);
                }}
                className="px-4 py-2 rounded-lg bg-orange-600 text-white text-sm font-medium hover:bg-orange-700 transition"
              >
                Soft Delete
              </button>

              <button
                type="button"
                onClick={() => setSelectedUsers([])}
                className="px-4 py-2 rounded-lg border border-gray-300 bg-white text-gray-700 text-sm font-medium hover:bg-gray-50 transition"
              >
                Clear
              </button>
            </div>
          </div>
        )}

        <table className="w-full">
          <thead className="bg-gradient-to-r from-violet-700 to-purple-600 text-white">
            <tr>
              <th className="px-4 py-4 text-center">
                <input
                  type="checkbox"
                  checked={
                    users.length > 0 &&
                    selectedUsers.length === users.length
                  }
                  onChange={(e) => {
                    if (e.target.checked) {
                      setSelectedUsers(users.map((user) => user.id));
                    } else {
                      setSelectedUsers([]);
                    }
                  }}
                  className="w-4 h-4 cursor-pointer accent-violet-600"
                />
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                ID
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Name
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Email
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Phone
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Gender
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Plan
              </th>

              <th className="px-6 py-4 text-left text-sm font-semibold">
                Status
              </th>

              <th className="px-6 py-4 text-center text-sm font-semibold">
                Actions
              </th>
            </tr>
          </thead>

          <tbody className="divide-y divide-gray-200">
            {users.length === 0 ? (
              <tr>
                <td
                  colSpan="9"
                  className="text-center py-12 text-gray-500"
                >
                  No users found.
                </td>
              </tr>
            ) : (
              users.map((user, index) => (
                <tr
                  key={user.id}
                  className={`transition ${
                    index % 2 === 0
                      ? "bg-white"
                      : "bg-gray-50"
                  } hover:bg-purple-50`}
                >
                  <td className="px-4 py-4 text-center">
                    <input
                      type="checkbox"
                      checked={selectedUsers.includes(user.id)}
                      onChange={(e) => {
                        if (e.target.checked) {
                          setSelectedUsers((prev) => [...prev, user.id]);
                        } else {
                          setSelectedUsers((prev) =>
                            prev.filter((id) => id !== user.id)
                          );
                        }
                      }}
                      className="w-4 h-4 cursor-pointer accent-violet-600"
                    />
                  </td>

                  {/* ID */}
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {user.id}
                  </td>

                  {/* Name */}
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                <img
                  src={
                    user?.imageUrl
                      ? `https://localhost:9090${user.imageUrl}`
                      : `https://ui-avatars.com/api/?name=${encodeURIComponent(
                          user?.fullName || "User"
                        )}`
                  }
                  alt={user?.fullName || "User"}
                  className="w-10 h-10 rounded-full object-cover border"
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(
                      user?.fullName || "User"
                    )}`;
                  }}
                />
                      <div>
                        <div className="font-medium text-gray-900">
                          {user.fullName}
                        </div>

                        <div className="text-xs text-gray-500">
                          {user.city || "-"}
                        </div>
                      </div>
                    </div>
                  </td>

                  {/* Email */}
                  <td className="px-6 py-4 text-sm text-gray-600">
                    {user.email}
                  </td>

                  {/* Phone */}
                  <td className="px-6 py-4 text-sm text-gray-600">
                    {user.phone || "-"}
                  </td>

                  {/* Gender */}
                  <td className="px-6 py-4 text-sm text-gray-600">
                    {user.gender || "-"}
                  </td>

                  {/* Plan */}
                  <td className="px-6 py-4">
                    {user.premium ? (
                      <span className="inline-flex items-center whitespace-nowrap bg-violet-100 text-violet-700 px-3 py-1.5 rounded-full text-xs font-semibold">
                        {user.premiumPlan || "Premium"}
                      </span>
                    ) : (
                      <span className="inline-flex items-center whitespace-nowrap bg-gray-100 text-gray-600 px-3 py-1.5 rounded-full text-xs font-medium">
                        Free Plan
                      </span>
                    )}
                  </td>

                  {/* Status */}
                  <td className="px-6 py-4">
                    <span
                      className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${
                        user.blocked
                          ? "bg-red-100 text-red-700"
                          : user.active
                          ? "bg-green-100 text-green-700"
                          : "bg-yellow-100 text-yellow-700"
                      }`}
                    >
                      {user.blocked
                        ? "Blocked"
                        : user.active
                        ? "Active"
                        : "Inactive"}
                    </span>
                  </td>

                  {/* Actions */}
                  <td className="px-6 py-4">
                    <div className="flex justify-center gap-3">
                      <UserActionMenu
                        user={user}
                        onView={(user) => navigate(`/users/${user.id}`)}
                        onActivate={(user) => openConfirmModal(user, "activate")}
                        onDeactivate={(user) => openConfirmModal(user, "deactivate")}

                        onRestore={(user) => openConfirmModal(user, "restore")}
                        onSoftDelete={(user) => openConfirmModal(user, "softDelete")}
                      />
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* ================= FOOTER ================= */}
      <div className="flex flex-col md:flex-row justify-between items-center mt-8 gap-4">
        <div className="flex items-center gap-6">
          <p className="text-sm text-gray-600 font-medium">
            Total Users :
            <span className="ml-2 font-semibold text-gray-900">
              {totalUsers}
            </span>
          </p>

          <div className="flex items-center gap-2">
            <label className="text-sm font-medium text-gray-600">
              Show
            </label>

            <select
              value={size}
              onChange={(e) => {
                setSize(Number(e.target.value));
                setPage(0);
              }}
              className="border border-gray-300 rounded-lg px-3 py-2"
            >
              <option value={10}>10</option>
              <option value={20}>20</option>
              <option value={50}>50</option>
              <option value={100}>100</option>
            </select>
          </div>
        </div>

        <div className="flex items-center gap-3">
          {/* Previous */}
          <button
            onClick={() => setPage((prev) => prev - 1)}
            disabled={page === 0}
            className={`px-4 py-2 rounded-lg text-sm font-medium transition ${
              page === 0
                ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                : "bg-gray-100 hover:bg-gray-200 text-gray-700"
            }`}
          >
            Previous
          </button>

          {/* Current Page */}
          <div className="px-4 py-2 rounded-lg bg-gradient-to-r from-violet-700 to-purple-600 text-white text-sm font-semibold">
            Page {page + 1} of {Math.max(totalPages, 1)}
          </div>

          {/* Next */}
          <button
            onClick={() => setPage((prev) => prev + 1)}
            disabled={page + 1 >= totalPages}
            className={`px-4 py-2 rounded-lg text-sm font-medium transition ${
              page + 1 >= totalPages
                ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                : "bg-gray-100 hover:bg-gray-200 text-gray-700"
            }`}
          >
            Next
          </button>
        </div>
      </div>

      {/* ================= CONFIRM MODAL ================= */}
      <ConfirmModal
        isOpen={isModalOpen}
        title="Confirm Action"
        message={
          isBulkAction
            ? `Are you sure you want to soft delete ${selectedUsers.length} ${
                selectedUsers.length === 1 ? "user" : "users"
              }? This action will remove the selected users from active records.`
            : selectedUser
            ? `Are you sure you want to ${selectedAction} "${selectedUser.fullName}"?`
            : ""
        }
        confirmText="Yes"
        cancelText="Cancel"
        confirmButtonClass={
          selectedAction === "softDelete"
            ? "bg-orange-600 hover:bg-orange-700"
            : "bg-violet-600 hover:bg-violet-700"
        }
        onConfirm={handleModalConfirm}
        onCancel={() => {
          setIsModalOpen(false);
          setSelectedUser(null);
          setSelectedAction("");
        }}
      />
    </div>
  );
}