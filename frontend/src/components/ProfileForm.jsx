import { useState, useEffect } from "react";
import { Upload, X, Save } from "lucide-react";
import { masterDataAPI } from "@/services/api";
import { useLoading } from "@/hooks/useLoading";

/**
 * Reusable Profile Form Component
 * Can be used for both Create and Update profile scenarios
 * 
 * Props:
 * - initialData: Object with profile data to populate form
 * - onSave: Callback function when form is submitted
 * - onError: Callback function when validation fails
 * - isLoading: Boolean to show loading state
 * - showPhotoUpload: Boolean to show profile photo upload section (default: true)
 */
const ProfileForm = ({ 
  initialData = {}, 
  onSave, 
  onError,
  isLoading = false,
  showPhotoUpload = true 
}) => {
  const { startLoading, stopLoading } = useLoading();
  
  const [masterOptions, setMasterOptions] = useState({
    religions: [],
    states: [],
    cities: [],
    educationLevels: [],
    occupations: [],
    heights: [],
    weights: [],
    maritalStatuses: [],
    castes: [],
    subCastes: [],
    motherTongues: [],
    bodyTypes: [],
    complexions: [],
    countries: [],
    diets: [],
    smoking: [],
    drinking: [],
    incomes: []
  });
  
  const [masterDataLoaded, setMasterDataLoaded] = useState(false);
  const [formData, setFormData] = useState({
    // Personal Details
    fullName: "",
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    gender: "",
    dateOfBirth: "",
    age: "",
    maritalStatusId: null,
    religionId: null,
    casteId: null,
    subCasteId: null,
    motherTongueId: null,
    
    // Physical Details
    heightId: null,
    weightId: null,
    complexionId: null,
    bodyTypeId: null,
    
    // Education & Career
    educationLevelId: null,
    occupationId: null,
    incomeId: null,
    companyName: "",
    
    // Location
    countryId: null,
    stateId: null,
    cityId: null,
    address: "",
    
    // Lifestyle
    dietId: null,
    smokingId: null,
    drinkingId: null,
    
    // Family Details
    fatherName: "",
    fatherOccupation: "",
    motherName: "",
    motherOccupation: "",
    siblingsCount: "",
    
    // Partner Preferences
    preferredAgeMin: "",
    preferredAgeMax: "",
    preferredLocation: "",
    preferredEducation: "",
    otherExpectations: "",
    
    // Other
    aboutMe: "",
    profilePhoto: null,
    profilePhotoUrl: "",
    
    ...initialData
  });

  // Load master data options from backend
  useEffect(() => {
    const loadMasterData = async () => {
      try {
        console.log('🔍 Loading master data from APIs in ProfileForm...');
        
        const [
          religions,
          states,
          educationLevels,
          occupations,
          maritalStatuses,
          heights,
          weights,
          motherTongues,
          bodyTypes,
          complexions,
          countries,
          diets,
          smoking,
          drinking,
          incomes
        ] = await Promise.all([
          masterDataAPI.getReligions(),
          masterDataAPI.getStates(),
          masterDataAPI.getEducationLevels(),
          masterDataAPI.getOccupations(),
          masterDataAPI.getMaritalStatuses(),
          masterDataAPI.getHeights(),
          masterDataAPI.getWeights(),
          masterDataAPI.getMotherTongues(),
          masterDataAPI.getBodyTypes(),
          masterDataAPI.getComplexions(),
          masterDataAPI.getCountries(),
          masterDataAPI.getDiets(),
          masterDataAPI.getSmoking(),
          masterDataAPI.getDrinking(),
          masterDataAPI.getIncomes()
        ]);
        
        console.log('📊 Master data loaded in ProfileForm:', {
          religions: religions?.length || 0,
          states: states?.length || 0,
          educationLevels: educationLevels?.length || 0,
          occupations: occupations?.length || 0
        });
        
        const safeData = {
          religions: Array.isArray(religions) ? religions : [],
          states: Array.isArray(states) ? states : [],
          cities: [],
          educationLevels: Array.isArray(educationLevels) ? educationLevels : [],
          occupations: Array.isArray(occupations) ? occupations : [],
          maritalStatuses: Array.isArray(maritalStatuses) ? maritalStatuses : [],
          heights: Array.isArray(heights) ? heights : [],
          weights: Array.isArray(weights) ? weights : [],
          castes: [],
          subCastes: [],
          motherTongues: Array.isArray(motherTongues) ? motherTongues : [],
          bodyTypes: Array.isArray(bodyTypes) ? bodyTypes : [],
          complexions: Array.isArray(complexions) ? complexions : [],
          countries: Array.isArray(countries) ? countries : [],
          diets: Array.isArray(diets) ? diets : [],
          smoking: Array.isArray(smoking) ? smoking : [],
          drinking: Array.isArray(drinking) ? drinking : [],
          incomes: Array.isArray(incomes) ? incomes : []
        };
        
        setMasterOptions(safeData);
        setMasterDataLoaded(true);
        console.log('✅ Master data set successfully in ProfileForm');
      } catch (error) {
        console.error('❌ Failed to load master data in ProfileForm:', error);
        setMasterDataLoaded(true); // Set to true even on error to allow form to render
      }
    };

    loadMasterData();
  }, []);

  // Load castes when religion changes
  useEffect(() => {
    const loadCastes = async () => {
      if (formData.religionId) {
        try {
          console.log('🔍 Loading castes for religion in ProfileForm:', formData.religionId);
          const castes = await masterDataAPI.getCastes(formData.religionId);
          const safeCastes = Array.isArray(castes) ? castes : [];
          setMasterOptions(prev => ({ ...prev, castes: safeCastes }));
        } catch (error) {
          console.error('❌ Failed to load castes in ProfileForm:', error);
          setMasterOptions(prev => ({ ...prev, castes: [] }));
        }
      } else {
        setMasterOptions(prev => ({ ...prev, castes: [] }));
      }
    };
    
    loadCastes();
  }, [formData.religionId]);

  // Load sub castes when caste changes
  useEffect(() => {
    const loadSubCastes = async () => {
      if (formData.casteId) {
        try {
          console.log('🔍 Loading sub castes for caste in ProfileForm:', formData.casteId);
          const subCastes = await masterDataAPI.getSubCastes(formData.casteId);
          const safeSubCastes = Array.isArray(subCastes) ? subCastes : [];
          setMasterOptions(prev => ({ ...prev, subCastes: safeSubCastes }));
        } catch (error) {
          console.error('❌ Failed to load sub castes in ProfileForm:', error);
          setMasterOptions(prev => ({ ...prev, subCastes: [] }));
        }
      } else {
        setMasterOptions(prev => ({ ...prev, subCastes: [] }));
      }
    };
    
    loadSubCastes();
  }, [formData.casteId]);

  // Load cities when state changes
  useEffect(() => {
    const loadCities = async () => {
      if (formData.stateId) {
        try {
          console.log('🔍 Loading cities for state in ProfileForm:', formData.stateId);
          const cities = await masterDataAPI.getCities(formData.stateId);
          setMasterOptions(prev => ({
            ...prev,
            cities: Array.isArray(cities) ? cities : []
          }));
        } catch (error) {
          console.error('❌ Failed to load cities in ProfileForm:', error);
          setMasterOptions(prev => ({ ...prev, cities: [] }));
        }
      } else {
        setMasterOptions(prev => ({ ...prev, cities: [] }));
      }
    };
    
    loadCities();
  }, [formData.stateId]);

  // Calculate age from dateOfBirth on mount or when initialData changes
  useEffect(() => {
    if (masterDataLoaded && initialData.dateOfBirth && !initialData.age) {
      const calculatedAge = calculateAge(initialData.dateOfBirth);
      setFormData(prev => ({ ...prev, age: calculatedAge }));
    }
  }, [initialData.dateOfBirth, initialData.age, masterDataLoaded]);

  // Auto-calculate age from DOB
  const calculateAge = (dob) => {
    if (!dob) return "";
    const birthDate = new Date(dob);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    return age > 0 ? age.toString() : "";
  };

  const handleInputChange = (field, value) => {
    if (field === "dateOfBirth") {
      const calculatedAge = calculateAge(value);
      setFormData(prev => ({
        ...prev,
        dateOfBirth: value,
        age: calculatedAge
      }));
    } else {
      // Convert ID fields to numbers for backend compatibility
      const idFields = [
        'religionId', 'casteId', 'subCasteId', 'motherTongueId',
        'maritalStatusId', 'educationLevelId', 'occupationId',
        'heightId', 'weightId', 'stateId', 'cityId',
        'bodyTypeId', 'complexionId', 'countryId', 'dietId',
        'smokingId', 'drinkingId', 'incomeId'
      ];
      
      const finalValue = idFields.includes(field) && value !== '' 
        ? (typeof value === 'string' ? parseInt(value, 10) : value)
        : value;
      
      setFormData(prev => ({ ...prev, [field]: finalValue }));
    }
  };

  const handleProfilePhotoUpload = (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // Validate file type
    if (!file.type.startsWith("image/")) {
      onError && onError("Please select an image file");
      return;
    }

    // Validate file size (max 5MB)
    if (file.size > 5 * 1024 * 1024) {
      onError && onError("File size should be less than 5MB");
      return;
    }

    // Create preview URL
    const reader = new FileReader();
    reader.onloadend = () => {
      setFormData(prev => ({
        ...prev,
        profilePhoto: file,
        profilePhotoUrl: reader.result
      }));
    };
    reader.readAsDataURL(file);
  };

  const removeProfilePhoto = () => {
    setFormData(prev => ({
      ...prev,
      profilePhoto: null,
      profilePhotoUrl: ""
    }));
  };

  const validateForm = () => {
    const requiredFields = [
      'fullName', 'gender', 'dateOfBirth', 'maritalStatusId', 
      'religionId', 'motherTongueId', 'educationLevelId', 'occupationId', 'cityId'
    ];
    
    for (let field of requiredFields) {
      if (!formData[field] || formData[field].toString().trim() === "") {
        const fieldLabel = field.replace(/([A-Z])/g, ' $1').replace('Id', '').trim();
        onError && onError(`${fieldLabel} is required`);
        return false;
      }
    }

    if (formData.email && !formData.email.includes("@")) {
      onError && onError("Please enter a valid email address");
      return false;
    }

    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    startLoading('Saving profile...');

    try {
      // Remove File object before sending (can't serialize)
      const dataToSave = { ...formData };
      delete dataToSave.profilePhoto;
      
      await onSave && onSave(dataToSave);
    } catch (error) {
      onError && onError('Failed to save profile');
    } finally {
      stopLoading();
    }
  };

  // Helper function to render form fields
  const renderField = (field) => {
    const { label, placeholder, type = "text", key, options, readOnly = false } = field;
    
    if (type === "select") {
      let fieldOptions = options || [];
      
      // Dynamic master data
      if (key === 'stateId') {
        fieldOptions = masterOptions.states;
      } else if (key === 'cityId') {
        fieldOptions = masterOptions.cities;
      } else if (key === 'religionId') {
        fieldOptions = masterOptions.religions;
      } else if (key === 'casteId') {
        fieldOptions = masterOptions.castes;
      } else if (key === 'subCasteId') {
        fieldOptions = masterOptions.subCastes;
      } else if (key === 'motherTongueId') {
        fieldOptions = masterOptions.motherTongues;
      } else if (key === 'educationLevelId') {
        fieldOptions = masterOptions.educationLevels;
      } else if (key === 'occupationId') {
        fieldOptions = masterOptions.occupations;
      } else if (key === 'maritalStatusId') {
        fieldOptions = masterOptions.maritalStatuses;
      } else if (key === 'heightId') {
        fieldOptions = masterOptions.heights;
      } else if (key === 'weightId') {
        fieldOptions = masterOptions.weights;
      } else if (key === 'bodyTypeId') {
        fieldOptions = masterOptions.bodyTypes;
      } else if (key === 'complexionId') {
        fieldOptions = masterOptions.complexions;
      } else if (key === 'countryId') {
        fieldOptions = masterOptions.countries;
      } else if (key === 'dietId') {
        fieldOptions = masterOptions.diets;
      } else if (key === 'smokingId') {
        fieldOptions = masterOptions.smoking;
      } else if (key === 'drinkingId') {
        fieldOptions = masterOptions.drinking;
      } else if (key === 'incomeId') {
        fieldOptions = masterOptions.incomes;
      }
      
      return (
        <div key={key}>
          <label className="text-xs font-medium text-foreground mb-1 block">{label}</label>
          <select
            value={formData[key]?.toString() || ''}
            onChange={(e) => {
              const value = e.target.value;
              handleInputChange(key, value);
              // Reset dependent fields
              if (key === 'religionId') {
                handleInputChange('casteId', '');
                handleInputChange('subCasteId', '');
              }
              if (key === 'casteId') {
                handleInputChange('subCasteId', '');
              }
              if (key === 'stateId') {
                handleInputChange('cityId', '');
              }
            }}
            className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary"
          >
            <option value="">Select {label.toLowerCase()}</option>
            {fieldOptions?.map((opt) => {
              const optionValue = (opt.id || opt.value || opt).toString();
              const optionLabel = opt.name || opt.value || opt.range || opt;
              return (
                <option key={optionValue} value={optionValue}>
                  {optionLabel}
                </option>
              );
            })}
          </select>
        </div>
      );
    }

    return (
      <div key={key}>
        <label className="text-xs font-medium text-foreground mb-1 block">{label}</label>
        <input
          type={type}
          value={formData[key] || ""}
          onChange={(e) => handleInputChange(key, e.target.value)}
          placeholder={placeholder}
          readOnly={readOnly}
          className={`w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary ${readOnly ? 'bg-muted cursor-not-allowed' : ''}`}
        />
      </div>
    );
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {/* Profile Photo Upload */}
      {showPhotoUpload && (
        <div className="border border-dashed border-border rounded-lg p-4 bg-muted/30">
          <label className="text-sm font-medium text-foreground mb-3 block">Profile Photo</label>
          {formData.profilePhotoUrl ? (
            <div className="relative w-20 h-20 rounded-full overflow-hidden mb-3">
              <img src={formData.profilePhotoUrl} alt="Profile" className="w-full h-full object-cover" />
              <button
                type="button"
                onClick={removeProfilePhoto}
                className="absolute inset-0 bg-black/50 flex items-center justify-center opacity-0 hover:opacity-100 transition-opacity"
              >
                <X className="h-4 w-4 text-white" />
              </button>
            </div>
          ) : null}
          <label className="flex items-center gap-2 px-4 py-2 bg-primary hover:bg-primary/90 text-primary-foreground rounded-lg text-sm font-medium cursor-pointer transition-colors inline-block">
            <Upload className="h-4 w-4" />
            {formData.profilePhotoUrl ? "Change Photo" : "Upload Photo"}
            <input type="file" accept="image/*" onChange={handleProfilePhotoUpload} className="hidden" />
          </label>
        </div>
      )}

      {/* Personal Details Section */}
      <div>
        <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Personal Details</h3>
        
        {/* Pre-populated fields from registration (read-only) */}
        {(formData.firstName || formData.lastName || formData.email || formData.phone) && (
          <div className="mb-4 p-3 bg-muted/30 rounded-lg border border-border">
            <p className="text-xs font-medium text-muted-foreground mb-2">Information provided during registration:</p>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              {formData.email && (
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">Email</label>
                  <input 
                    type="email" 
                    value={formData.email}
                    readOnly
                    className="w-full bg-muted border border-border rounded-lg px-4 py-2 text-sm text-muted-foreground cursor-not-allowed" 
                  />
                </div>
              )}
              {formData.phone && (
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">Phone</label>
                  <input 
                    type="tel" 
                    value={formData.phone}
                    readOnly
                    className="w-full bg-muted border border-border rounded-lg px-4 py-2 text-sm text-muted-foreground cursor-not-allowed" 
                  />
                </div>
              )}
              {(formData.firstName || formData.lastName) && (
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">Name</label>
                  <input 
                    type="text" 
                    value={`${formData.firstName || ""} ${formData.lastName || ""}`.trim()}
                    readOnly
                    className="w-full bg-muted border border-border rounded-lg px-4 py-2 text-sm text-muted-foreground cursor-not-allowed" 
                  />
                </div>
              )}
            </div>
          </div>
        )}
        
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {renderField({ label: "Full Name", placeholder: "Your full name", key: "fullName" })}
          {renderField({ label: "Gender", key: "gender", type: "select", options: ["Male", "Female", "Other"] })}
          {renderField({ label: "Date of Birth", type: "date", key: "dateOfBirth" })}
          {renderField({ label: "Age", type: "number", key: "age", placeholder: "Auto-calculated", readOnly: true })}
          {renderField({ 
            label: "Marital Status", 
            key: "maritalStatusId", 
            type: "select" 
          })}
          {renderField({ 
            label: "Religion", 
            key: "religionId", 
            type: "select" 
          })}
          {renderField({ label: "Caste", key: "casteId", type: "select" })}
          {renderField({ label: "Sub-caste", key: "subCasteId", type: "select" })}
          {renderField({ label: "Mother Tongue", key: "motherTongueId", type: "select" })}
        </div>
      </div>

      {/* Physical Details Section */}
      <div>
        <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Physical Details</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {renderField({ label: "Height", key: "heightId", type: "select" })}
          {renderField({ label: "Weight", key: "weightId", type: "select" })}
          {renderField({ 
            label: "Complexion", 
            key: "complexionId", 
            type: "select"
          })}
          {renderField({ 
            label: "Body Type", 
            key: "bodyTypeId", 
            type: "select"
          })}
        </div>
      </div>

      {/* Education & Career Section */}
      <div>
        <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Education & Career</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {renderField({ 
            label: "Highest Education", 
            key: "educationLevelId", 
            type: "select" 
          })}
          {renderField({ label: "Profession/Occupation", key: "occupationId", type: "select" })}
          {renderField({ label: "Annual Income", key: "incomeId", type: "select" })}
          {renderField({ label: "Company Name", placeholder: "Your company", key: "companyName" })}
        </div>
      </div>

      {/* Location Section */}
      <div>
        <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Location Details</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {renderField({ label: "Country", key: "countryId", type: "select" })}
          {renderField({
  label: "State/Province",
  key: "stateId",
  type: "select"
})}
          {renderField({ label: "City", key: "cityId", type: "select" })}
          {renderField({ label: "Address", placeholder: "Your address", key: "address" })}
        </div>
      </div>

      {/* Lifestyle Section */}
      <div>
        <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Lifestyle</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {renderField({ 
            label: "Diet", 
            key: "dietId", 
            type: "select"
          })}
          {renderField({ 
            label: "Smoking", 
            key: "smokingId", 
            type: "select"
          })}
          {renderField({ 
            label: "Drinking", 
            key: "drinkingId", 
            type: "select"
          })}
        </div>
      </div>

      {/* Family Details Section */}
      <div>
        <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Family Details</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {renderField({ label: "Father's Name", placeholder: "Your father's name", key: "fatherName" })}
          {renderField({ label: "Father's Occupation", placeholder: "Your father's occupation", key: "fatherOccupation" })}
          {renderField({ label: "Mother's Name", placeholder: "Your mother's name", key: "motherName" })}
          {renderField({ label: "Mother's Occupation", placeholder: "Your mother's occupation", key: "motherOccupation" })}
          {renderField({ label: "Number of Siblings", key: "siblingsCount", type: "number", placeholder: "0" })}
        </div>
      </div>

      {/* Partner Preferences Section */}
      <div>
        <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Partner Preferences</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {renderField({ label: "Preferred Age (Min)", type: "number", key: "preferredAgeMin" })}
          {renderField({ label: "Preferred Age (Max)", type: "number", key: "preferredAgeMax" })}
          {renderField({ label: "Preferred Location", placeholder: "Preferred location", key: "preferredLocation" })}
          {renderField({ label: "Preferred Education", placeholder: "Preferred education", key: "preferredEducation" })}
        </div>
      </div>

      {/* Contact Information Section */}
      <div>
        <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Contact Information</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {renderField({ label: "Email", type: "email", placeholder: "your@email.com", key: "email" })}
          {renderField({ label: "Phone", type: "tel", placeholder: "+91 98765 43210", key: "phone" })}
        </div>
      </div>

      {/* About Me Section */}
      <div>
        <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">About Me</h3>
        <div>
          <label className="text-xs font-medium text-foreground mb-1 block">About Me</label>
          <textarea 
            rows={4} 
            value={formData.aboutMe || ""}
            onChange={(e) => handleInputChange("aboutMe", e.target.value)}
            placeholder="Tell us about yourself, your interests, personality and what you are looking for..." 
            className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary resize-none" 
          />
        </div>
      </div>

      {/* Other Expectations */}
      <div>
        <label className="text-xs font-medium text-foreground mb-1 block">Other Expectations</label>
        <textarea 
          rows={3} 
          value={formData.otherExpectations}
          onChange={(e) => handleInputChange("otherExpectations", e.target.value)}
          placeholder="Any other expectations or preferences..." 
          className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary resize-none" 
        />
      </div>

      {/* Submit Button */}
      <button 
        type="submit" 
        disabled={isLoading}
        className="flex items-center gap-2 bg-primary hover:bg-primary/90 disabled:bg-primary/50 disabled:cursor-not-allowed text-primary-foreground font-semibold px-6 py-2.5 rounded-lg text-sm transition-colors"
      >
        <Save className="h-4 w-4" /> 
        {isLoading ? "Saving..." : "Save Profile"}
      </button>
    </form>
  );
};

export default ProfileForm;
