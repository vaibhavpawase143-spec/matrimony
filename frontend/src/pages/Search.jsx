import { Search as SearchIcon, ChevronDown, SlidersHorizontal } from "lucide-react";
import { Link } from "react-router-dom";
import { useState, useEffect, useRef } from "react";

import { useLoading } from "@/hooks/useLoading";
import { useToast } from "@/components/Toast";
import { searchAPI, masterDataAPI,  blockAPI } from "@/services/api";
import { useMatrimonyOptions } from "@/hooks/useMatrimonyOptions";
import { useLanguage } from "@/context/LanguageContext.jsx";

const getImageUrl = (image) => {
  if (!image) return null;
  if (image.startsWith("http://") || image.startsWith("https://")) return image;
  const backendBase = (
    import.meta.env.VITE_BACKEND_URL ||
    (import.meta.env.VITE_API_BASE_URL ? import.meta.env.VITE_API_BASE_URL.replace(/\/api\/?$/, "") : "") ||
    "http://localhost:9090"
  ).replace(/\/$/, "");
  if (image.startsWith("/")) return `${backendBase}${image}`;
  if (image.startsWith("uploads/")) return `${backendBase}/${image}`;
  return `${backendBase}/uploads/${image}`;
};

const SelectField = ({
  label,
  options = [],
  value,
  onChange,
  placeholder = "Select"
}) => {

  const safeOptions =
    Array.isArray(options)
      ? options
      : [];

  return (
    <div>
      <label className="text-xs font-medium text-foreground mb-1 block">
        {label}
      </label>

      <div className="relative">

        <select
          value={value || ""}
          onChange={(e) => onChange(e.target.value)}
          className="
          w-full
          appearance-none
          bg-background
          border
          border-border
          rounded-lg
          px-4
          py-2.5
          pr-10
          text-sm
          text-muted-foreground
          "
        >

          <option value="">
            {placeholder}
          </option>

          {safeOptions.map((o) => (

            <option
              key={o?.id || o?.value}
              value={o?.id || o?.value}
            >
              {o?.name || o?.label}
            </option>

          ))}

        </select>

        <ChevronDown
          className="
          absolute
          right-3
          top-1/2
          -translate-y-1/2
          h-3.5
          w-3.5
          text-muted-foreground
          pointer-events-none
          "
        />

      </div>
    </div>
  );
};
const SearchPage = () => {
  const { startLoading, stopLoading } = useLoading();
  const { success, error } = useToast();
  const { getOptions } = useMatrimonyOptions();
  const { t } = useLanguage();
  
  // State for search results
  const [searchResults, setSearchResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false);
  const [failedImages, setFailedImages] = useState({});
  const [totalResults, setTotalResults] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(20);
  const [totalPages, setTotalPages] = useState(1);
  const [showAdvancedFilters, setShowAdvancedFilters] = useState(false);
  
  // State for search filters
  const [filters, setFilters] = useState({
    min_age: '',
    max_age: '',
    religion_id: '',
    caste_id: '',
    city_id: '',
    education_level_id: '',
    occupation_id: '',
    height_id: '',
    weight_id: '',
    marital_status_id: '',
    min_weight: '',
    max_weight: '',
    sort: 'relevance'
  });

  // State for master data dropdowns
  const [masterData, setMasterData] = useState({
    religions: [],
    castes: [],
    cities: [],
    educationLevels: [],
    occupations: [],
    heights: [],
    weights: [],
    maritalStatuses: []
  });

useEffect(() => {

  const loadCastes = async () => {

    if (!filters.religion_id) {

      setMasterData(prev => ({
        ...prev,
        castes: [],
        subCastes: []
      }));

      return;
    }

    const data =
      await masterDataAPI.getCastes(
        filters.religion_id
      );

    setMasterData(prev => ({
      ...prev,
      castes: data || []
    }));
  };

  loadCastes();

}, [filters.religion_id]);
useEffect(() => {

  const loadSubCastes = async () => {

    if (!filters.caste_id) {
      setMasterData(prev => ({
        ...prev,
        subCastes: []
      }));
      return;
    }

    try {

      const data =
        await masterDataAPI.getSubCastes(
          filters.caste_id
        );

      console.log(
        "SUB CASTES =",
        data
      );

      setMasterData(prev => ({
        ...prev,
        subCastes: data || []
      }));

    } catch (err) {

      console.error(
        "SUB CASTE ERROR",
        err
      );

    }
  };

  loadSubCastes();

}, [filters.caste_id]);
useEffect(() => {
  console.log("MASTER DATA =", masterData);
}, [masterData]);

  // Load master data on component mount
  const loadMasterData = async () => {
    try {
      const bulkData = await masterDataAPI.getAllMasterData();
      setMasterData({
        religions: Array.isArray(bulkData?.religions) ? bulkData.religions : [],
        castes: [],
        cities: Array.isArray(bulkData?.cities) ? bulkData.cities : [],
        educationLevels: Array.isArray(bulkData?.educationLevels) ? bulkData.educationLevels : [],
        occupations: Array.isArray(bulkData?.occupations) ? bulkData.occupations : [],
        maritalStatuses: Array.isArray(bulkData?.maritalStatuses) ? bulkData.maritalStatuses : [],
        subCastes: [],
        heights: Array.isArray(bulkData?.heights) ? bulkData.heights : [],
        weights: Array.isArray(bulkData?.weights) ? bulkData.weights : [],
      });
      console.log("MASTER DATA LOADED");
    } catch (err) {
      console.error("MASTER DATA ERROR", err);
    }
  };
  const observerTarget = useRef(null);
  const debounceTimerRef = useRef(null);

  useEffect(() => {
    loadMasterData();
    performSearch(0, pageSize); // Initial search
  }, []);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && currentPage + 1 < totalPages && !loading && !loadingMore) {
          performSearch(currentPage + 1, pageSize, true);
        }
      },
      { threshold: 0.1 }
    );

    if (observerTarget.current) {
      observer.observe(observerTarget.current);
    }

    return () => {
      if (observerTarget.current) {
        observer.unobserve(observerTarget.current);
      }
    };
  }, [currentPage, totalPages, loading, loadingMore]);


const performSearch = async (pageToFetch = 0, sizeToFetch = pageSize, isAppend = false, customFilters = null) => {
  const activeFilters = customFilters || filters;

  // Validate Age Range Filters before searching
  if (activeFilters.min_age !== "" && activeFilters.min_age !== null) {
    const minAgeNum = Number(activeFilters.min_age);
    if (isNaN(minAgeNum) || minAgeNum < 18 || minAgeNum > 100) {
      error("Minimum age must be between 18 and 100.");
      return;
    }
  }

  if (activeFilters.max_age !== "" && activeFilters.max_age !== null) {
    const maxAgeNum = Number(activeFilters.max_age);
    if (isNaN(maxAgeNum) || maxAgeNum < 18 || maxAgeNum > 100) {
      error("Maximum age must be between 18 and 100.");
      return;
    }
  }

  if (
    activeFilters.min_age !== "" &&
    activeFilters.max_age !== "" &&
    Number(activeFilters.min_age) > Number(activeFilters.max_age)
  ) {
    error("Minimum age cannot be greater than maximum age.");
    return;
  }

  if (isAppend) {
    setLoadingMore(true);
  } else {
    setLoading(true);
  }

  try {

    const searchPayload = {
      search: activeFilters.search || '',
      religionId: activeFilters.religion_id ? Number(activeFilters.religion_id) : null,
      casteId: activeFilters.caste_id ? Number(activeFilters.caste_id) : null,
      cityId: activeFilters.city_id ? Number(activeFilters.city_id) : null,
      educationLevelId: activeFilters.education_level_id ? Number(activeFilters.education_level_id) : null,
      occupationId: activeFilters.occupation_id ? Number(activeFilters.occupation_id) : null,
      maritalStatusId: activeFilters.marital_status_id ? Number(activeFilters.marital_status_id) : null,
      ageFrom: activeFilters.min_age ? Number(activeFilters.min_age) : null,
      ageTo: activeFilters.max_age ? Number(activeFilters.max_age) : null,
      weightId: activeFilters.weight_id ? Number(activeFilters.weight_id) : null,
      minWeight: activeFilters.min_weight ? Number(activeFilters.min_weight) : null,
      maxWeight: activeFilters.max_weight ? Number(activeFilters.max_weight) : null,
      sortBy: activeFilters.sort || 'relevance',
      page: pageToFetch,
      size: sizeToFetch
    };

    const response = await searchAPI.searchProfiles(searchPayload);
    const newContent = response?.content || [];

    if (isAppend) {
      setSearchResults(prev => [...prev, ...newContent]);
    } else {
      setSearchResults(newContent);
    }

    setTotalResults(response?.totalElements || 0);
    setTotalPages(response?.totalPages || 1);
    setCurrentPage(response?.number || 0);

  } catch (err) {

    console.error("🔍 Search failed:", err);
    error("Search failed. Please try again.");

    if (!isAppend) {
      setSearchResults([]);
      setTotalResults(0);
      setTotalPages(1);
    }

  } finally {
    setLoading(false);
    setLoadingMore(false);
  }
};

const handleLoadMore = () => {
  if (currentPage + 1 < totalPages && !loadingMore && !loading) {
    performSearch(currentPage + 1, pageSize, true);
  }
};

const handleFilterChange = (field, value) => {
  const updated = { ...filters, [field]: value };
  if (field === 'religion_id') {
    updated.caste_id = '';
  }
  setFilters(updated);
};

const handleSearch = () => {
  performSearch(0, pageSize, false);
};

const handlePageChange = (newPage) => {
  if (newPage < 0 || newPage >= totalPages) return;
  performSearch(newPage, pageSize, false);
  window.scrollTo({ top: 300, behavior: 'smooth' });
};

const handlePageSizeChange = (newSize) => {
  const sizeNum = Number(newSize);
  setPageSize(sizeNum);
  performSearch(0, sizeNum, false);
};

  const handleReset = () => {
    const defaultFilters = {
      min_age: '',
      max_age: '',
      religion_id: '',
      caste_id: '',
      city_id: '',
      education_level_id: '',
      occupation_id: '',
      height_id: '',
      weight_id: '',
      marital_status_id: '',
      min_weight: '',
      max_weight: '',
      sort: 'relevance'
    };
    setFilters(defaultFilters);
    performSearch(0, pageSize, false, defaultFilters);
  };
  return (
    <div className="min-h-screen bg-muted/30 pb-16">
      {/* Sleek Gradient Header */}
      <div 
        className="py-10 px-4 text-center text-white relative overflow-hidden shadow-md" 
        style={{ background: "linear-gradient(135deg, hsl(270 65% 35%), hsl(290 60% 45%), hsl(270 55% 55%))" }}
      >
        <div className="max-w-3xl mx-auto relative z-10">
          <h1 className="text-3xl sm:text-4xl md:text-5xl font-display font-bold mb-3 tracking-tight drop-shadow-sm">
            {t.search.title}
          </h1>
          <p className="text-white/80 text-sm md:text-base max-w-xl mx-auto font-body">
            {t.search.subtitle}
          </p>
        </div>
      </div>

      <div className="container mx-auto px-4 py-8 max-w-7xl">
        {/* Horizontal Top Filter Bar */}
        <div className="bg-card rounded-2xl border border-border/70 shadow-sm p-4 md:p-6 mb-8 transition-all">
          <div className="flex items-center justify-between flex-wrap gap-3 pb-4 border-b border-border/50">
            <div className="flex items-center gap-2">
              <SlidersHorizontal className="h-5 w-5 text-primary" />
              <h2 className="text-base font-semibold text-foreground">Filter Profiles</h2>
            </div>

            <div className="flex items-center gap-2">
              <button
                onClick={() => setShowAdvancedFilters(!showAdvancedFilters)}
                className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-1.5 transition-all ${
                  showAdvancedFilters 
                    ? 'bg-primary/10 text-primary border border-primary/30' 
                    : 'bg-muted hover:bg-muted/80 text-muted-foreground'
                }`}
              >
                <SlidersHorizontal className="h-3.5 w-3.5" />
                <span>{showAdvancedFilters ? "Less Filters" : "More Filters"}</span>
                <ChevronDown className={`h-3.5 w-3.5 transition-transform duration-200 ${showAdvancedFilters ? "rotate-180" : ""}`} />
              </button>

              <button
                onClick={handleReset}
                disabled={loading}
                className="px-3 py-1.5 text-xs font-medium text-muted-foreground hover:text-foreground hover:bg-muted rounded-lg transition-colors"
              >
                Reset All
              </button>
            </div>
          </div>

          {/* Primary Quick Filters (Inline Grid) */}
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 pt-4">
            <div>
              <label className="text-xs font-medium text-foreground mb-1 block">Age Range</label>
              <div className="grid grid-cols-2 gap-2">
                <input 
                  type="number" 
                  placeholder="Min Age" 
                  value={filters.min_age}
                  onChange={(e) => handleFilterChange('min_age', e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && performSearch(0, pageSize)}
                  className="w-full bg-background border border-border rounded-xl px-3 py-2 text-xs text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" 
                />
                <input 
                  type="number" 
                  placeholder="Max Age" 
                  value={filters.max_age}
                  onChange={(e) => handleFilterChange('max_age', e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && performSearch(0, pageSize)}
                  className="w-full bg-background border border-border rounded-xl px-3 py-2 text-xs text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" 
                />
              </div>
            </div>

            <SelectField 
              label="Religion" 
              options={masterData.religions}
              value={filters.religion_id}
              onChange={(value) => handleFilterChange('religion_id', value)}
              placeholder="All Religions"
            />

            <SelectField 
              label="Caste" 
              options={masterData.castes}
              value={filters.caste_id}
              onChange={(value) => handleFilterChange('caste_id', value)}
              placeholder="All Castes"
            />

            <SelectField 
              label="City / Location" 
              options={masterData.cities}
              value={filters.city_id}
              onChange={(value) => handleFilterChange('city_id', value)}
              placeholder="All Cities"
            />
          </div>

          {/* Collapsible Advanced Filters Drawer */}
          {showAdvancedFilters && (
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 pt-4 mt-4 border-t border-border/50 animate-in fade-in duration-200">
              <SelectField 
                label="Education Level" 
                options={masterData.educationLevels}
                value={filters.education_level_id}
                onChange={(value) => handleFilterChange('education_level_id', value)}
                placeholder="All Education Levels"
              />

              <SelectField 
                label="Profession / Occupation" 
                options={masterData.occupations}
                value={filters.occupation_id}
                onChange={(value) => handleFilterChange('occupation_id', value)}
                placeholder="All Occupations"
              />

              <SelectField 
                label="Marital Status" 
                options={masterData.maritalStatuses}
                value={filters.marital_status_id}
                onChange={(value) => handleFilterChange('marital_status_id', value)}
                placeholder="All Statuses"
              />

              <div>
                <label className="text-xs font-medium text-foreground mb-1 block">Sort By</label>
                <div className="relative">
                  <select 
                    value={filters.sort}
                    onChange={(e) => handleFilterChange('sort', e.target.value)}
                    className="w-full appearance-none bg-background border border-border rounded-xl px-4 py-2 pr-10 text-xs text-foreground focus:outline-none"
                  >
                    <option value="relevance">Relevance & Premium First</option>
                    <option value="newest">Newest Members First</option>
                    <option value="age_low_high">Age: Low to High</option>
                    <option value="age_high_low">Age: High to Low</option>
                  </select>
                  <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-muted-foreground pointer-events-none" />
                </div>
              </div>
            </div>
          )}

          {/* Action Row with Search Button */}
          <div className="flex items-center justify-between gap-3 pt-5 mt-4 border-t border-border/50 flex-wrap">
            <div className="text-xs text-muted-foreground font-medium">
              Press <span className="px-1.5 py-0.5 bg-muted border border-border rounded text-[10px] font-mono">Enter</span> or click Search to apply filters
            </div>

            <div className="flex items-center gap-3">
              <button
                onClick={handleReset}
                disabled={loading}
                className="px-4 py-2 text-xs font-semibold text-muted-foreground hover:text-foreground hover:bg-muted rounded-xl transition-colors"
              >
                Reset Filters
              </button>

              <button
                onClick={handleSearch}
                disabled={loading}
                className="px-7 py-2.5 bg-primary text-primary-foreground text-xs font-bold rounded-xl hover:bg-primary/90 transition-all shadow-md flex items-center gap-2 active:scale-95"
              >
                <SearchIcon className="h-4 w-4" />
                <span>Search Profiles</span>
              </button>
            </div>
          </div>
        </div>

        {/* Results Header */}
        <div className="flex items-center justify-between mb-6 px-1 flex-wrap gap-4">
          <h3 className="text-lg font-bold text-foreground tracking-tight">
            Matching Profiles
          </h3>

          <div className="flex items-center gap-3">
            <span className="text-xs text-muted-foreground">Results per page:</span>
            <select
              value={pageSize}
              onChange={(e) => handlePageSizeChange(e.target.value)}
              className="bg-card border border-border rounded-lg px-2.5 py-1 text-xs font-semibold text-foreground focus:outline-none"
            >
              <option value={12}>12</option>
              <option value={24}>24</option>
              <option value={48}>48</option>
            </select>
          </div>
        </div>

        {/* Profiles Grid */}
        {loading ? (
          <div className="flex flex-col items-center justify-center py-20 bg-card rounded-2xl border border-border shadow-sm">
            <div className="animate-spin rounded-full h-12 w-12 border-4 border-primary border-t-transparent mb-4"></div>
            <p className="text-sm font-semibold text-muted-foreground">Finding compatible profiles...</p>
          </div>
        ) : searchResults.length === 0 ? (
          <div className="text-center py-20 bg-card rounded-2xl border border-border shadow-sm max-w-2xl mx-auto px-6">
            <SearchIcon className="h-16 w-16 mx-auto mb-4 opacity-40 text-primary" />
            <h3 className="text-xl font-bold text-foreground mb-2">No Matching Profiles Found</h3>
            <p className="text-sm text-muted-foreground max-w-md mx-auto mb-6">
              We couldn't find any profiles matching your criteria. Try adjusting or clearing your filters to see more results.
            </p>
            <button 
              onClick={handleReset}
              className="px-6 py-2.5 bg-primary text-primary-foreground text-xs font-semibold rounded-xl hover:bg-primary/90 transition-all shadow-md"
            >
              Reset Filters
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {searchResults.map((profile) => (
              <Link 
                to={`/profile/${profile.id}`} 
                key={profile.id} 
                className="bg-card rounded-2xl overflow-hidden border border-border/80 shadow-sm hover:shadow-xl transition-all duration-300 group flex flex-col hover:-translate-y-1"
              >
                <div className="relative aspect-[3/4] overflow-hidden bg-muted">
                  {profile.isPremium && (
                    <div className="absolute top-3 left-3 z-10 bg-gradient-to-r from-yellow-400 to-amber-500 text-white px-3 py-1 rounded-full text-[10px] font-extrabold shadow-md tracking-wider">
                      👑 PREMIUM
                    </div>
                  )}

                  {profile.imageUrl && !failedImages[profile.id] ? (
                    <img 
                      src={getImageUrl(profile.imageUrl)}
                      alt={`${profile.firstName || ''} ${profile.lastName || ''}`}
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                      onError={() => {
                        setFailedImages(prev => ({ ...prev, [profile.id]: true }));
                      }}
                    />
                  ) : (
                    <div className="w-full h-full flex items-center justify-center bg-gradient-to-br from-purple-100 to-pink-100 dark:from-purple-950 dark:to-pink-950">
                      <div className="text-center p-4">
                        <div className="w-20 h-20 bg-primary/10 rounded-full mx-auto mb-2 flex items-center justify-center border-2 border-primary/20">
                          <span className="text-primary text-2xl font-bold">
                            {profile.firstName?.charAt(0)?.toUpperCase() || '?'}
                          </span>
                        </div>
                        <p className="text-xs font-semibold text-muted-foreground">
                          {profile.firstName} {profile.lastName}
                        </p>
                      </div>
                    </div>
                  )}
                </div>

                <div className="p-4 flex-1 flex flex-col justify-between">
                  <div>
                    <h3 className="text-base font-bold text-foreground flex items-center gap-1.5 truncate">
                      <span>{profile.firstName} {profile.lastName}</span>
                      {profile.isPremium && <span className="text-yellow-500 text-xs">👑</span>}
                    </h3>

                    <p className="text-xs font-medium text-muted-foreground mt-1 truncate">
                      {profile.occupationName || 'Profession not specified'}
                      {' · '}
                      {profile.cityName || 'Location not specified'}
                    </p>

                    <p className="text-xs text-muted-foreground/80 mt-0.5 truncate">
                      {profile.educationLevelName || 'Education not specified'}
                      {' · '}
                      {profile.religionName || 'Religion not specified'}
                    </p>
                  </div>

                  <div className="flex gap-2 mt-4 pt-3 border-t border-border/50">
                    <button className="flex-1 bg-primary/10 text-primary text-xs font-semibold py-2 rounded-xl hover:bg-primary/20 transition-colors">
                      {t.search.profileActions.viewProfile}
                    </button>
                    <button className="flex-1 bg-pink-500/10 text-pink-600 dark:text-pink-400 text-xs font-semibold py-2 rounded-xl hover:bg-pink-500/20 transition-colors">
                      {t.search.profileActions.sendInterest}
                    </button>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}

        {/* Dynamic Infinite Scroll Sentinel */}
        {searchResults.length > 0 && (
          <div className="mt-8 pt-6">
            <div ref={observerTarget} className="h-12 flex items-center justify-center w-full">
              {loadingMore && (
                <div className="flex items-center gap-3 py-2.5 px-6 bg-card rounded-full border border-border shadow-md">
                  <div className="animate-spin rounded-full h-4 w-4 border-2 border-primary border-t-transparent" />
                  <span className="text-xs font-semibold text-muted-foreground">Loading more profiles...</span>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default SearchPage;
