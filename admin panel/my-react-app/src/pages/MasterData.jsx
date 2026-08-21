import { useState, useEffect } from "react";
import {
  FaPlus,
  FaEdit,
  FaSearch,
  FaTimes,
  FaCheck,
  FaSpinner,
  FaToggleOn,
  FaToggleOff,
} from "react-icons/fa";
import { toast } from "sonner";
import {
  getMasterItems,
  createMasterItem,
  updateMasterItem,
  deleteMasterItem,
  restoreMasterItem,
  getAllReligions,
  getAllCastes,
  getCountries,
  getAllStates,
} from "../services/masterDataService";

export default function MasterData() {
  const tabs = [
    "Religion",
    "Caste",
    "Sub Caste",
    "Country",
    "State",
    "City",
    "Occupation",
    "Qualification",
    "Income",
    "Education",
    "Height",
    "Weight",
    "Diet",
    "Smoking",
    "Drinking",
    "Body Type",
    "Complexion",
    "Marital Status",
    "Mother Tongue",
    "Employment",
    "Family Type",
  ];

  const [activeTab, setActiveTab] = useState("Religion");
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState("");

  // Bulk Selection State (keeps existing row/status/action logic unchanged)
  const [selectedIds, setSelectedIds] = useState([]);

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [formData, setFormData] = useState({
    name: "",
    religionId: "",
    casteId: "",
    countryId: "",
    stateId: "",
    isActive: true,
  });
  const [submitting, setSubmitting] = useState(false);

  // Parent Options State for Dependent Masters
  const [religionsList, setReligionsList] = useState([]);
  const [castesList, setCastesList] = useState([]);
  const [countriesList, setCountriesList] = useState([]);
  const [statesList, setStatesList] = useState([]);

  // =========================================================
  // FETCH MASTER DATA (Retrieves ALL active & inactive data)
  // =========================================================

  const fetchTabItems = async (tab) => {
    setLoading(true);
    try {
      const data = await getMasterItems(tab);
      setItems(data || []);
    } catch (err) {
      console.error(`Failed to fetch ${tab}:`, err);
      toast.error(`Failed to load ${tab} data.`);
      setItems([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTabItems(activeTab);
  }, [activeTab]);

  // Load Parent Dropdown Options when Modal Opens for Child Masters
  useEffect(() => {
    if (isModalOpen) {
      if (activeTab === "Caste") {
        getAllReligions().then(setReligionsList);
      } else if (activeTab === "Sub Caste") {
        getAllCastes().then(setCastesList);
      } else if (activeTab === "State") {
        getCountries().then(setCountriesList);
      } else if (activeTab === "City") {
        getAllStates().then(setStatesList);
      }
    }
  }, [isModalOpen, activeTab]);

  // =========================================================
  // HELPER TO EXTRACT NAME & PARENT FROM ANY DTO
  // =========================================================

  const getItemName = (item) => {
    if (!item) return "-";
    if (typeof item === "string") return item;
    return (
      item.name ||
      item.religionName ||
      item.casteName ||
      item.subCasteName ||
      item.countryName ||
      item.stateName ||
      item.cityName ||
      item.title ||
      item.label ||
      item.value ||
      item.degreeName ||
      item.range ||
      item.occupationName ||
      `ID #${item.id}`
    );
  };

  const getParentName = (item) => {
    if (!item) return null;
    if (activeTab === "Caste") {
      return item.religionName || item.religion?.name || (item.religionId ? `Religion #${item.religionId}` : null);
    }
    if (activeTab === "Sub Caste") {
      return item.casteName || item.caste?.name || (item.casteId ? `Caste #${item.casteId}` : null);
    }
    if (activeTab === "State") {
      return item.countryName || item.country?.name || (item.countryId ? `Country #${item.countryId}` : null);
    }
    if (activeTab === "City") {
      return item.stateName || item.state?.name || (item.stateId ? `State #${item.stateId}` : null);
    }
    return null;
  };

  const isItemActive = (item) => {
    if (!item) return false;
    if (item.active !== undefined) return Boolean(item.active);
    if (item.isActive !== undefined) return Boolean(item.isActive);
    if (item.status !== undefined) return item.status === "Active" || item.status === true;
    return true;
  };

  // =========================================================
  // HANDLERS (ADD / EDIT / TOGGLE ACTIVE-INACTIVE)
  // =========================================================

  const handleOpenAddModal = () => {
    setEditingItem(null);
    setFormData({
      name: "",
      religionId: "",
      casteId: "",
      countryId: "",
      stateId: "",
      isActive: true,
    });
    setIsModalOpen(true);
  };

  const handleOpenEditModal = (item) => {
    setEditingItem(item);
    setFormData({
      name: getItemName(item),
      religionId: item.religionId || item.religion?.id || "",
      casteId: item.casteId || item.caste?.id || "",
      countryId: item.countryId || item.country?.id || "",
      stateId: item.stateId || item.state?.id || "",
      isActive: isItemActive(item),
    });
    setIsModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name.trim()) {
      toast.error("Name is required.");
      return;
    }

    setSubmitting(true);
    try {
      const payload = {
        name: formData.name.trim(),
        isActive: formData.isActive,
      };

      if (activeTab === "Height") {
        payload.height = formData.name.trim();
        delete payload.name;
      }

      if (
        activeTab === "Weight" ||
        activeTab === "Smoking" ||
        activeTab === "Drinking"
      ) {
        payload.value = formData.name.trim();
        delete payload.name;
      }

      if (activeTab === "Caste" && formData.religionId) {
        payload.religionId = Number(formData.religionId);
      } else if (activeTab === "Sub Caste" && formData.casteId) {
        payload.casteId = Number(formData.casteId);
      } else if (activeTab === "State" && formData.countryId) {
        payload.countryId = Number(formData.countryId);
      } else if (activeTab === "City" && formData.stateId) {
        payload.stateId = Number(formData.stateId);
      }

      if (editingItem) {
        await updateMasterItem(activeTab, editingItem.id, payload);
        toast.success(`${activeTab} updated successfully.`);
      } else {
        await createMasterItem(activeTab, payload);
        toast.success(`${activeTab} created successfully.`);
      }

      setIsModalOpen(false);
      fetchTabItems(activeTab);
    } catch (err) {
      console.error(err);
      toast.error(err?.message || `Failed to save ${activeTab}.`);
    } finally {
      setSubmitting(false);
    }
  };

  // Toggle Active / Inactive Status (DOES NOT REMOVE ITEM FROM LIST)
  const handleToggleStatus = async (item) => {
    const currentlyActive = isItemActive(item);
    const newStatus = !currentlyActive;

    // Optimistic UI update
    setItems((prevItems) =>
      prevItems.map((i) =>
        i.id === item.id
          ? {
              ...i,
              isActive: newStatus,
              active: newStatus,
              status: newStatus ? "Active" : "Inactive",
            }
          : i
      )
    );

    try {
     const adminId = item.adminId || item.admin?.id || 1;

     const payload = {
       adminId: Number(adminId),
       isActive: !item.isActive,
     };

      // ==========================================
      // MASTER-SPECIFIC VALUE
      // ==========================================

      if (activeTab === "Income") {
        payload.range = item.range || item.name;
      } else if (activeTab === "Height") {
        payload.height = item.height || item.name;
      } else if (
               activeTab === "Weight" ||
               activeTab === "Smoking" ||
               activeTab === "Drinking"
             ) {
               payload.value = item.value || item.name;

               if (activeTab === "Drinking") {
                 payload.name = item.name || item.value;
               }
             } else {
        payload.name = getItemName(item);
      }

      // ==========================================
      // ADMIN ID
      // ==========================================

      if (adminId) {
        payload.adminId = Number(adminId);
      }

      // ==========================================
      // PARENT IDS
      // ==========================================

      if (activeTab === "Caste") {
        const religionId = item.religionId || item.religion?.id;

        if (religionId) {
          payload.religionId = Number(religionId);
        }
      }

      if (activeTab === "Sub Caste") {
        const casteId = item.casteId || item.caste?.id;

        if (casteId) {
          payload.casteId = Number(casteId);
        }
      }

      if (activeTab === "State") {
        const countryId = item.countryId || item.country?.id;

        if (countryId) {
          payload.countryId = Number(countryId);
        }
      }

      if (activeTab === "City") {
        const stateId = item.stateId || item.state?.id;

        if (stateId) {
          payload.stateId = Number(stateId);
        }
      }

      console.log("Updating master status:", {
        tab: activeTab,
        id: item.id,
        payload,
      });

      await updateMasterItem(activeTab, item.id, payload);

      toast.success(
        `${getItemName(item)} is now ${
          newStatus ? "Active" : "Inactive"
        }.`
      );

      await fetchTabItems(activeTab);
    } catch (err) {
      console.error("STATUS UPDATE ERROR:", {
        message: err?.message,
        serverMessage: err?.serverMessage,
        status: err?.status,
        error: err,
      });

      // Rollback UI
      setItems((prevItems) =>
        prevItems.map((i) =>
          i.id === item.id
            ? {
                ...i,
                isActive: currentlyActive,
                active: currentlyActive,
                status: currentlyActive ? "Active" : "Inactive",
              }
            : i
        )
      );

      toast.error(
        err?.message ||
          err?.serverMessage ||
          `Failed to update status for ${getItemName(item)}.`
      );
    }
  };

  // =========================================================
  // BULK SELECTION / BULK STATUS ACTIONS
  // =========================================================

  const toggleRowSelection = (id) => {
    setSelectedIds((prev) =>
      prev.includes(id)
        ? prev.filter((selectedId) => selectedId !== id)
        : [...prev, id]
    );
  };

  const clearSelectedRows = () => {
    setSelectedIds([]);
  };

  const handleBulkStatusUpdate = async (newStatus) => {
    if (selectedIds.length === 0) {
      return;
    }

    const selectedItems = items.filter((item) =>
      selectedIds.includes(item.id)
    );

    if (selectedItems.length === 0) {
      setSelectedIds([]);
      return;
    }

    const actionText = newStatus ? "activated" : "deactivated";

    try {
      setLoading(true);

      await Promise.all(
        selectedItems.map(async (item) => {
          const adminId = item.adminId || item.admin?.id || 1;

          const payload = {
            adminId: Number(adminId),
            isActive: newStatus,
          };

          if (activeTab === "Income") {
            payload.range = item.range || item.name;
          } else if (activeTab === "Height") {
            payload.height = item.height || item.name;
          } else if (
            activeTab === "Weight" ||
            activeTab === "Smoking" ||
            activeTab === "Drinking"
          ) {
            payload.value = item.value || item.name;

            if (activeTab === "Drinking") {
              payload.name = item.name || item.value;
            }
          } else {
            payload.name = getItemName(item);
          }

          if (activeTab === "Caste") {
            const religionId = item.religionId || item.religion?.id;
            if (religionId) {
              payload.religionId = Number(religionId);
            }
          }

          if (activeTab === "Sub Caste") {
            const casteId = item.casteId || item.caste?.id;
            if (casteId) {
              payload.casteId = Number(casteId);
            }
          }

          if (activeTab === "State") {
            const countryId = item.countryId || item.country?.id;
            if (countryId) {
              payload.countryId = Number(countryId);
            }
          }

          if (activeTab === "City") {
            const stateId = item.stateId || item.state?.id;
            if (stateId) {
              payload.stateId = Number(stateId);
            }
          }

          return updateMasterItem(activeTab, item.id, payload);
        })
      );

      toast.success(
        `${selectedItems.length} ${activeTab} record${
          selectedItems.length > 1 ? "s" : ""
        } ${actionText} successfully.`
      );

      setSelectedIds([]);
      await fetchTabItems(activeTab);
    } catch (err) {
      console.error("BULK STATUS UPDATE ERROR:", err);

      toast.error(
        err?.message ||
          err?.serverMessage ||
          `Failed to ${newStatus ? "activate" : "deactivate"} selected ${activeTab} records.`
      );

      await fetchTabItems(activeTab);
    } finally {
      setLoading(false);
    }
  };

  // =========================================================
  // FILTERED DATA (Admin sees ALL items: active + inactive)
  // =========================================================

  const filteredItems = items.filter((item) => {
    const name = getItemName(item).toLowerCase();
    const idStr = String(item.id || "");
    const parent = (getParentName(item) || "").toLowerCase();
    const q = search.toLowerCase();
    return name.includes(q) || idStr.includes(q) || parent.includes(q);
  });

  return (
    <div className="p-6">
      {/* Header - Only Add button */}
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Master Data</h1>
          <p className="text-gray-500">
            Manage all master data categories from one place.
          </p>
        </div>

        <button
          onClick={handleOpenAddModal}
          className="bg-purple-600 hover:bg-purple-700 text-white px-5 py-2.5 rounded-xl flex items-center gap-2 font-semibold shadow-md transition cursor-pointer"
        >
          <FaPlus />
          Add {activeTab}
        </button>
      </div>

      {/* Tabs */}
      <div className="flex gap-2 overflow-x-auto mb-6 pb-2 scrollbar-thin">
        {tabs.map((tab) => (
          <button
            key={tab}
            onClick={() => {
              setActiveTab(tab);
              setSearch("");
              setSelectedIds([]);
            }}
            className={`px-4 py-2 rounded-xl text-sm font-medium whitespace-nowrap transition cursor-pointer ${
              activeTab === tab
                ? "bg-purple-600 text-white shadow-sm"
                : "bg-white border border-gray-200 text-gray-700 hover:bg-purple-50 hover:text-purple-600"
            }`}
          >
            {tab}
          </button>
        ))}
      </div>

      {/* Search */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-4 flex items-center gap-3 mb-6">
        <FaSearch className="text-gray-400" />
        <input
          className="w-full outline-none text-sm text-gray-700 placeholder-gray-400"
          placeholder={`Search in ${activeTab}...`}
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      {/* ================= BULK ACTION BAR ================= */}
      {selectedIds.length > 0 && (
        <div className="mb-4 rounded-xl border border-blue-200 bg-blue-50 px-4 py-3 shadow-sm">
          <div className="flex flex-wrap items-center justify-between gap-3">
            <div className="flex items-center gap-2 text-sm font-semibold text-blue-800">
              <span className="inline-flex items-center justify-center min-w-8 h-8 px-2 rounded-full bg-blue-600 text-white">
                {selectedIds.length}
              </span>
              <span>
                {selectedIds.length === 1 ? "Record" : "Records"} Selected
              </span>
            </div>

            <div className="flex items-center gap-2">
              <button
                type="button"
                onClick={() => handleBulkStatusUpdate(true)}
                disabled={loading}
                className="px-4 py-2 rounded-lg text-sm font-semibold bg-green-600 text-white hover:bg-green-700 transition cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Activate
              </button>

              <button
                type="button"
                onClick={() => handleBulkStatusUpdate(false)}
                disabled={loading}
                className="px-4 py-2 rounded-lg text-sm font-semibold bg-amber-500 text-white hover:bg-amber-600 transition cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Deactivate
              </button>

              <button
                type="button"
                onClick={clearSelectedRows}
                disabled={loading}
                className="px-4 py-2 rounded-lg text-sm font-semibold bg-gray-600 text-white hover:bg-gray-700 transition cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Table */}
      <div className="bg-white rounded-2xl shadow-md overflow-hidden border border-purple-100">
        <table className="w-full">
          <thead className="bg-gradient-to-r from-violet-700 to-purple-600 text-white">
            <tr>
              <th className="p-4 text-left text-sm font-semibold">ID</th>
              <th className="p-4 text-left text-sm font-semibold">{activeTab} Name</th>
              {["Caste", "Sub Caste", "State", "City"].includes(activeTab) && (
                <th className="p-4 text-left text-sm font-semibold">Parent Category</th>
              )}
              <th className="p-4 text-center text-sm font-semibold">Status</th>
              <th className="p-4 text-center text-sm font-semibold">Actions</th>
              <th className="p-4 text-center text-sm font-semibold">
                  <input
                    type="checkbox"
                    checked={
                      filteredItems.length > 0 &&
                      filteredItems.every((item) => selectedIds.includes(item.id))
                    }
                    onChange={(e) => {
                      if (e.target.checked) {
                        setSelectedIds(filteredItems.map((item) => item.id));
                      } else {
                        setSelectedIds([]);
                      }
                    }}
                    className="w-4 h-4 accent-purple-600 cursor-pointer"
                    title="Select all"
                  />
                </th>
            </tr>
          </thead>

          <tbody className="divide-y divide-gray-200">
            {loading ? (
              <tr>
                <td
                  colSpan={["Caste", "Sub Caste", "State", "City"].includes(activeTab) ? 7 : 6}
                  className="text-center py-12 text-gray-500"
                >
                  <div className="flex items-center justify-center gap-3 text-purple-600 font-medium">
                    <FaSpinner className="animate-spin text-xl" />
                    Loading {activeTab} data...
                  </div>
                </td>
              </tr>
            ) : filteredItems.length === 0 ? (
              <tr>
                <td
                  colSpan={["Caste", "Sub Caste", "State", "City"].includes(activeTab) ? 7 : 6}
                  className="text-center py-12 text-gray-500"
                >
                  No {activeTab} records found.
                </td>
              </tr>
            ) : (
              filteredItems.map((item, index) => {
                const active = isItemActive(item);
                const parentName = getParentName(item);

                return (
                  <tr
                    key={item.id || index}
                    className={`transition ${
                      index % 2 === 0 ? "bg-white" : "bg-gray-50"
                    } hover:bg-purple-50`}
                  >
                    <td className="p-4 text-sm font-medium text-gray-900">{item.id || index + 1}</td>

                    <td className="p-4 text-sm font-semibold text-gray-800">{getItemName(item)}</td>

                    {["Caste", "Sub Caste", "State", "City"].includes(activeTab) && (
                      <td className="p-4 text-sm text-gray-600">{parentName || "-"}</td>
                    )}

                    <td className="p-4 text-center">
                      <span
                        className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${
                          active
                            ? "bg-green-100 text-green-700 border border-green-200"
                            : "bg-red-100 text-red-700 border border-red-200"
                        }`}
                      >
                        {active ? "Active" : "Inactive"}
                      </span>
                    </td>

                    <td className="p-4 text-center">
                      <div className="flex items-center justify-center gap-3">
                        {/* Edit Button */}
                        <button
                          onClick={() => handleOpenEditModal(item)}
                          className="p-2 text-blue-600 hover:bg-blue-50 rounded-lg transition cursor-pointer"
                          title="Edit"
                        >
                          <FaEdit className="text-base" />
                        </button>

                        {/* Active / Inactive Toggle Button (Keeps item in table list) */}
                        <button
                          onClick={() => handleToggleStatus(item)}
                          className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition cursor-pointer ${
                            active
                              ? "bg-amber-50 text-amber-700 border border-amber-200 hover:bg-amber-100"
                              : "bg-green-50 text-green-700 border border-green-200 hover:bg-green-100"
                          }`}
                          title={active ? "Set to Inactive" : "Set to Active"}
                        >
                          {active ? (
                            <>
                              <FaToggleOn className="text-lg text-green-600" />
                              Deactivate
                            </>
                          ) : (
                            <>
                              <FaToggleOff className="text-lg text-gray-400" />
                              Activate
                            </>
                          )}
                        </button>
                      </div>
                    </td>

                    {/* Last Column: Row Selection Checkbox */}
                    <td className="p-4 text-center">
                      <input
                        type="checkbox"
                        checked={selectedIds.includes(item.id)}
                        onChange={() => toggleRowSelection(item.id)}
                        className="w-5 h-5 accent-blue-600 cursor-pointer"
                        aria-label={`Select ${getItemName(item)}`}
                      />
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>

      {/* Footer Info */}
      <div className="flex justify-between items-center mt-6 text-sm text-gray-600 font-medium">
        <p>Total Records : {filteredItems.length}</p>
      </div>

      {/* ================= EDIT / ADD MODAL ================= */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black/40 backdrop-blur-xs flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl shadow-xl w-full max-w-md p-6 border border-purple-100 animate-in fade-in duration-200">
            <div className="flex justify-between items-center pb-3 border-b border-gray-100 mb-4">
              <h3 className="text-lg font-bold text-gray-800">
                {editingItem ? `Edit ${activeTab}` : `Add New ${activeTab}`}
              </h3>
              <button
                onClick={() => setIsModalOpen(false)}
                className="text-gray-400 hover:text-gray-600 p-1"
              >
                <FaTimes />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              {/* Name Input */}
              <div>
                <label className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                  {activeTab} Name *
                </label>
                <input
                  type="text"
                  required
                  placeholder={`Enter ${activeTab} name...`}
                  value={formData.name}
                  onChange={(e) => setFormData((prev) => ({ ...prev, name: e.target.value }))}
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500"
                />
              </div>

              {/* Dependent Parent Dropdowns */}
              {activeTab === "Caste" && (
                <div>
                  <label className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                    Select Religion
                  </label>
                  <select
                    value={formData.religionId}
                    onChange={(e) => setFormData((prev) => ({ ...prev, religionId: e.target.value }))}
                    className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500"
                  >
                    <option value="">Select Religion</option>
                    {religionsList.map((r) => (
                      <option key={r.id} value={r.id}>
                        {getItemName(r)}
                      </option>
                    ))}
                  </select>
                </div>
              )}

              {activeTab === "Sub Caste" && (
                <div>
                  <label className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                    Select Caste
                  </label>
                  <select
                    value={formData.casteId}
                    onChange={(e) => setFormData((prev) => ({ ...prev, casteId: e.target.value }))}
                    className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500"
                  >
                    <option value="">Select Caste</option>
                    {castesList.map((c) => (
                      <option key={c.id} value={c.id}>
                        {getItemName(c)}
                      </option>
                    ))}
                  </select>
                </div>
              )}

              {activeTab === "State" && (
                <div>
                  <label className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                    Select Country
                  </label>
                  <select
                    value={formData.countryId}
                    onChange={(e) => setFormData((prev) => ({ ...prev, countryId: e.target.value }))}
                    className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500"
                  >
                    <option value="">Select Country</option>
                    {countriesList.map((cnt) => (
                      <option key={cnt.id} value={cnt.id}>
                        {getItemName(cnt)}
                      </option>
                    ))}
                  </select>
                </div>
              )}

              {activeTab === "City" && (
                <div>
                  <label className="block text-xs font-semibold text-gray-700 uppercase mb-1">
                    Select State
                  </label>
                  <select
                    value={formData.stateId}
                    onChange={(e) => setFormData((prev) => ({ ...prev, stateId: e.target.value }))}
                    className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-purple-500"
                  >
                    <option value="">Select State</option>
                    {statesList.map((st) => (
                      <option key={st.id} value={st.id}>
                        {getItemName(st)}
                      </option>
                    ))}
                  </select>
                </div>
              )}

              {/* Status Toggle */}
              <div className="flex items-center gap-2 pt-2">
                <input
                  type="checkbox"
                  id="isActive"
                  checked={formData.isActive}
                  onChange={(e) => setFormData((prev) => ({ ...prev, isActive: e.target.checked }))}
                  className="w-4 h-4 accent-purple-600 cursor-pointer"
                />
                <label htmlFor="isActive" className="text-sm font-medium text-gray-700 cursor-pointer">
                  Active Status
                </label>
              </div>

              {/* Actions */}
              <div className="flex justify-end gap-3 pt-4 border-t border-gray-100">
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  className="px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-50 transition cursor-pointer"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={submitting}
                  className="px-5 py-2 bg-purple-600 hover:bg-purple-700 text-white rounded-lg text-sm font-medium transition cursor-pointer flex items-center gap-2 disabled:opacity-50"
                >
                  {submitting ? <FaSpinner className="animate-spin" /> : <FaCheck />}
                  {editingItem ? "Update" : "Save"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}