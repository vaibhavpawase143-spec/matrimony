import { Search as SearchIcon, ChevronDown, SlidersHorizontal } from "lucide-react";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import Navbar from "@/components/Navbar";
import { useLoading } from "@/hooks/useLoading";
import { useToast } from "@/components/Toast";
import { searchAPI, masterDataAPI } from "@/services/api";
import { useMatrimonyOptions } from "@/hooks/useMatrimonyOptions";
import { useLanguage } from "@/context/LanguageContext.jsx";

const SelectField = ({ label, options, value, onChange, placeholder = "Select" }) => (
  <div>
    <label className="text-xs font-medium text-foreground mb-1 block">{label}</label>
    <div className="relative">
      <select 
        value={value} 
        onChange={(e) => onChange(e.target.value)}
        className="w-full appearance-none bg-background border border-border rounded-lg px-4 py-2.5 pr-10 text-sm text-muted-foreground font-body focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
      >
        <option value="">{placeholder}</option>
        {options.map((o) => (
          <option key={o.value || o} value={o.value || o}>
            {o.label || o}
          </option>
        ))}
      </select>
      <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-muted-foreground pointer-events-none" />
    </div>
  </div>
);

const SearchPage = () => {
  const { startLoading, stopLoading } = useLoading();
  const { success, error } = useToast();
  const { getOptions } = useMatrimonyOptions();
  const { t } = useLanguage();
  
  // State for search results
  const [searchResults, setSearchResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [totalResults, setTotalResults] = useState(0);
  
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

  // Load master data on component mount
  useEffect(() => {
    loadMasterData();
    performSearch(); // Initial search
  }, []);

  const loadMasterData = async () => {
    try {
      const [
        religionsRes,
        citiesRes,
        educationLevelsRes,
        occupationsRes,
        maritalStatusesRes
      ] = await Promise.all([
        masterDataAPI.getReligions(),
        masterDataAPI.getCities(),
        masterDataAPI.getEducationLevels(),
        masterDataAPI.getOccupations(),
        masterDataAPI.getMaritalStatuses()
      ]);

      setMasterData({
        religions: religionsRes.data || [],
        castes: [], // Load based on selected religion
        cities: citiesRes.data || [],
        educationLevels: educationLevelsRes.data || [],
        occupations: occupationsRes.data || [],
        heights: [], // Add if needed
        weights: [], // Add if needed
        maritalStatuses: maritalStatusesRes.data || []
      });
    } catch (err) {
      console.error('Failed to load master data:', err);
      // Fallback to local options if API fails
      setMasterData({
        religions: getOptions('religion').map(r => ({ value: r, label: r })),
        cities: getOptions('city').map(c => ({ value: c, label: c })),
        educationLevels: getOptions('education').map(e => ({ value: e, label: e })),
        occupations: getOptions('occupation').map(o => ({ value: o, label: o })),
        maritalStatuses: getOptions('maritalStatus').map(m => ({ value: m, label: m })),
        castes: [],
        heights: [],
        weights: []
      });
    }
  };

  const performSearch = async () => {
    setLoading(true);
    try {
      // TODO: call backend API
      const response = await searchAPI.searchProfiles(filters);
      setSearchResults(response.data || []);
      setTotalResults(response.total || response.data?.length || 0);
    } catch (err) {
      console.error('Search failed:', err);
      error('Search failed. Please try again.');
      setSearchResults([]);
      setTotalResults(0);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (field, value) => {
    setFilters(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleSearch = () => {
    // TODO: call backend API
    performSearch();
  };

  const handleReset = () => {
    setFilters({
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
      sort: 'relevance'
    });
    // Perform search after reset
    setTimeout(() => performSearch(), 100);
  };
  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      {/* Header */}
      <div className="py-8 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">{t.search.title}</h1>
        <p className="text-primary-foreground/70 text-sm">{t.search.subtitle}</p>
      </div>

      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          {/* Filters sidebar */}
          <div className="lg:col-span-1">
            <div className="bg-card rounded-xl border border-border p-5 sticky top-20">
              <div className="flex items-center gap-2 mb-5">
                <SlidersHorizontal className="h-4 w-4 text-primary" />
                <h2 className="text-sm font-semibold text-foreground">{t.search.filters.title}</h2>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">{t.search.filters.ageRange}</label>
                  <div className="grid grid-cols-2 gap-2">
                    <input 
                      type="number" 
                      placeholder={t.search.placeholders.min} 
                      value={filters.min_age}
                      onChange={(e) => handleFilterChange('min_age', e.target.value)}
                      className="w-full bg-background border border-border rounded-lg px-3 py-2.5 text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" 
                    />
                    <input 
                      type="number" 
                      placeholder="Max" 
                      value={filters.max_age}
                      onChange={(e) => handleFilterChange('max_age', e.target.value)}
                      className="w-full bg-background border border-border rounded-lg px-3 py-2.5 text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" 
                    />
                  </div>
                </div>

                <SelectField 
                  label={t.search.filters.religion} 
                  options={masterData.religions}
                  value={filters.religion_id}
                  onChange={(value) => handleFilterChange('religion_id', value)}
                  placeholder={t.search.placeholders.allReligions}
                />
                
                <SelectField 
                  label={t.search.filters.caste} 
                  options={masterData.castes}
                  value={filters.caste_id}
                  onChange={(value) => handleFilterChange('caste_id', value)}
                  placeholder={t.search.placeholders.allCastes}
                />
                
                <SelectField 
                  label={t.search.filters.location} 
                  options={masterData.cities}
                  value={filters.city_id}
                  onChange={(value) => handleFilterChange('city_id', value)}
                  placeholder={t.search.placeholders.allCities}
                />
                
                <SelectField 
                  label={t.search.filters.education} 
                  options={masterData.educationLevels}
                  value={filters.education_level_id}
                  onChange={(value) => handleFilterChange('education_level_id', value)}
                  placeholder={t.search.placeholders.allEducation}
                />
                
                <SelectField 
                  label={t.search.filters.profession} 
                  options={masterData.occupations}
                  value={filters.occupation_id}
                  onChange={(value) => handleFilterChange('occupation_id', value)}
                  placeholder={t.search.placeholders.allProfessions}
                />

                <SelectField 
                  label={t.search.filters.maritalStatus} 
                  options={masterData.maritalStatuses}
                  value={filters.marital_status_id}
                  onChange={(value) => handleFilterChange('marital_status_id', value)}
                  placeholder={t.search.placeholders.allStatus}
                />

                <button 
                  onClick={handleSearch}
                  disabled={loading}
                  className="w-full flex items-center justify-center gap-2 bg-primary hover:bg-primary/90 disabled:bg-primary/50 text-primary-foreground font-semibold py-2.5 rounded-lg text-sm transition-colors"
                >
                  <SearchIcon className="h-4 w-4" />
                  {loading ? t.search.messages.searchingProfiles : t.search.buttons.search}
                </button>

                <button 
                  onClick={handleReset}
                  disabled={loading}
                  className="w-full text-xs text-muted-foreground hover:text-foreground transition-colors py-1 disabled:opacity-50"
                >
                  {t.search.buttons.reset}
                </button>
              </div>
            </div>
          </div>

          {/* Results */}
          <div className="lg:col-span-3">
            <div className="flex items-center justify-between mb-5">
              <p className="text-sm text-muted-foreground">
                <span className="font-semibold text-foreground">{totalResults}</span> {t.search.results.profilesFound}
              </p>
              <div className="relative">
                <select 
                  value={filters.sort}
                  onChange={(e) => handleFilterChange('sort', e.target.value)}
                  className="appearance-none bg-card border border-border rounded-lg px-4 py-2 pr-10 text-xs text-muted-foreground font-body focus:outline-none"
                >
                  <option value="relevance">{t.search.sort.relevance}</option>
                  <option value="newest">{t.search.sort.newest}</option>
                  <option value="age_low_high">{t.search.sort.ageLowHigh}</option>
                  <option value="age_high_low">{t.search.sort.ageHighLow}</option>
                </select>
                <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 h-3 w-3 text-muted-foreground pointer-events-none" />
              </div>
            </div>

            {loading ? (
              <div className="flex items-center justify-center py-12">
                <div className="text-center">
                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>
                  <p className="text-sm text-muted-foreground">{t.search.messages.searchingProfiles}</p>
                </div>
              </div>
            ) : searchResults.length === 0 ? (
              <div className="text-center py-12">
                <div className="text-muted-foreground mb-4">
                  <SearchIcon className="h-12 w-12 mx-auto mb-4 opacity-50" />
                  <h3 className="text-lg font-semibold text-foreground mb-2">{t.search.messages.noProfilesFound}</h3>
                  <p className="text-sm">{t.search.messages.noProfilesMessage}</p>
                </div>
                <button 
                  onClick={handleReset}
                  className="mt-4 text-primary text-sm font-semibold hover:underline"
                >
                  {t.search.buttons.clearFilters}
                </button>
              </div>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-5">
                {searchResults.map((profile) => (
                  <Link to={`/profile/${profile.id}`} key={profile.id} className="bg-card rounded-xl overflow-hidden border border-border shadow-sm hover:shadow-md transition-shadow group">
                    <div className="aspect-[3/4] overflow-hidden bg-muted">
                      {profile.photo_url ? (
                        <img 
                          src={profile.photo_url} 
                          alt={profile.name} 
                          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                          onError={(e) => {
                            // TODO: add fallback image
                          }}
                        />
                      ) : (
                        <div className="w-full h-full flex items-center justify-center bg-muted">
                          <div className="text-center">
                            <div className="w-16 h-16 bg-muted-foreground/20 rounded-full mx-auto mb-2 flex items-center justify-center">
                              <span className="text-muted-foreground text-xl">
                                {profile.name?.charAt(0)?.toUpperCase() || '?'}
                              </span>
                            </div>
                            <p className="text-xs text-muted-foreground">No Photo</p>
                          </div>
                        </div>
                      )}
                    </div>
                    <div className="p-4">
                      <h3 className="text-sm font-semibold text-foreground">
                        {profile.name || 'Unknown'}, <span className="text-primary">{profile.age || '?'}</span>
                      </h3>
                      <p className="text-xs text-muted-foreground mt-0.5">
                        {profile.occupation || 'Profession not specified'} · {profile.city || 'Location not specified'}
                      </p>
                      <p className="text-xs text-muted-foreground">
                        {profile.education_level || 'Education not specified'} · {profile.religion || 'Religion not specified'}
                      </p>
                      <div className="flex gap-2 mt-3">
                        <button className="flex-1 bg-accent/10 text-accent text-xs font-semibold py-1.5 rounded-lg hover:bg-accent/20 transition-colors">
                          {t.search.profileActions.viewProfile}
                        </button>
                        <button className="flex-1 bg-orange-cta/10 text-orange-cta text-xs font-semibold py-1.5 rounded-lg hover:bg-orange-cta/20 transition-colors">
                          {t.search.profileActions.sendInterest}
                        </button>
                      </div>
                    </div>
                  </Link>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SearchPage;
