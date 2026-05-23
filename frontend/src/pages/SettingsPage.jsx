import { useState, useEffect } from "react";
import { User, Lock, Bell, Save, Upload, X } from "lucide-react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import { useToast } from "@/components/Toast";
import { useProfileData } from "@/hooks/useProfileData";
import { useMatrimonyOptions } from "@/hooks/useMatrimonyOptions";
import { masterDataAPI } from "@/services/api";

const tabs = [
  { id: "profile", label: "Profile", icon: <User className="h-4 w-4" /> },
  { id: "password", label: "Password", icon: <Lock className="h-4 w-4" /> },
  { id: "notifications", label: "Notifications", icon: <Bell className="h-4 w-4" /> },
];

const SettingsPage = () => {
  const [activeTab, setActiveTab] = useState("profile");
  const { success, error, info } = useToast();
  const { profileData: savedProfileData, saveProfileData } = useProfileData();
  const { getOptions } = useMatrimonyOptions();
  const [masterOptions, setMasterOptions] = useState({
    religions: [],
    genders: [],
    cities: [],
    educationLevels: [],
    occupations: [],
    heights: [],
    weights: [],
    maritalStatuses: [],
    castes: [],
    complexions: [],
    bodyTypes: [],
    motherTongues: []
  });
  
  // Profile form state - matching backend DTO structure
  const [formData, setFormData] = useState({
    // Basic fields from User entity
    firstName: "",
    lastName: "",
    fullName: "", // Combined field for UI
    email: "",
    phone: "",
    
    // Profile fields matching backend DTO
    gender: "",
    dateOfBirth: "",
    about: "",
    imageUrl: "",
    
    // Relational field IDs
    religionId: null,
    casteId: null,
    subCasteId: null,
    motherTongueId: null,
    maritalStatusId: null,
    educationLevelId: null,
    occupationId: null,
    heightId: null,
    complexions: null,
    bodyTypes: null,
    weightId: null,
    cityId: null,
    
    // For UI purposes
    profilePhoto: null,
    profilePhotoUrl: ""
  });
  
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: ""
  });

  // Load master data options from backend
  useEffect(() => {
    const loadMasterData = async () => {
      try {
        console.log('🔍 Loading master data from APIs...');
        
        const [
          religions,
          genders,
          cities,
          educationLevels,
          occupations,
          maritalStatuses,
          heights,
          weights,
          complexions,
          bodyTypes,
          motherTongues
        ] = await Promise.all([
          masterDataAPI.getReligions(),
          masterDataAPI.getGenders(),
          masterDataAPI.getCities(),
          masterDataAPI.getEducationLevels(),
          masterDataAPI.getOccupations(),
          masterDataAPI.getMaritalStatuses(),
          masterDataAPI.getHeights(),
          masterDataAPI.getComplexions(),
          masterDataAPI.getBodyTypes(),
          masterDataAPI.getWeights(),
          masterDataAPI.getMotherTongues()
        ]);
        
        console.log('📊 Master data loaded:', {
          religions: religions?.length || 0,
          genders: genders?.length || 0,
          cities: cities?.length || 0,
          educationLevels: educationLevels?.length || 0,
          occupations: occupations?.length || 0,
          maritalStatuses: maritalStatuses?.length || 0,
          heights: heights?.length || 0,
          weights: weights?.length || 0,
          complexions: complexions?.length || 0,
          bodyTypes: bodyTypes?.length || 0,
          motherTongues: motherTongues?.length || 0
        });
        
        // Ensure all data are arrays
        const safeData = {
          religions: Array.isArray(religions) ? religions : [],
          genders: Array.isArray(genders) ? genders : [],
          cities: Array.isArray(cities) ? cities : [],
          educationLevels: Array.isArray(educationLevels) ? educationLevels : [],
          occupations: Array.isArray(occupations) ? occupations : [],
          maritalStatuses: Array.isArray(maritalStatuses) ? maritalStatuses : [],
          heights: Array.isArray(heights) ? heights : [],
          weights: Array.isArray(weights) ? weights : [],
          complexions: Array.isArray(complexions) ? complexions : [],
          bodyTypes: Array.isArray(bodyTypes) ? bodyTypes : [],
          castes: [], // Will be loaded based on selected religion
          motherTongues: Array.isArray(motherTongues) ? motherTongues : []
        };
        
        setMasterOptions(safeData);
        
      } catch (error) {
        console.error('❌ Failed to load master data:', error);
        // Set empty arrays on error to prevent UI crashes
        setMasterOptions({
          religions: [],
          genders: [],
          cities: [],
          educationLevels: [],
          occupations: [],
          maritalStatuses: [],
          heights: [],
          weights: [],
         castes: [],
         complexions: [],
         bodyTypes: [],
         subCastes: [],
         motherTongues: []
        });
      }
    };
    
    loadMasterData();
  }, []);

  // Load castes when religion changes
  useEffect(() => {
    const loadCastes = async () => {
      if (formData.religionId) {
        try {
          console.log('🔍 Loading castes for religion:', formData.religionId);
          const castes = await masterDataAPI.getCastes(formData.religionId);
          console.log('✅ Castes loaded:', castes);
          const safeCastes = Array.isArray(castes) ? castes : [];
          setMasterOptions(prev => ({ ...prev, castes: safeCastes }));
        } catch (error) {
          console.error('❌ Failed to load castes:', error);
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

        console.log(
          '🔍 Loading sub castes for caste:',
          formData.casteId
        );

        const subCastes =
          await masterDataAPI.getSubCastes(
            formData.casteId
          );

        console.log(
          '✅ Sub Castes loaded:',
          subCastes
        );

        const safeSubCastes =
          Array.isArray(subCastes)
            ? subCastes
            : [];

        setMasterOptions(prev => ({
          ...prev,
          subCastes: safeSubCastes
        }));

      } catch (error) {

        console.error(
          '❌ Failed to load sub castes:',
          error
        );

        setMasterOptions(prev => ({
          ...prev,
          subCastes: []
        }));

      }

    } else {

      setMasterOptions(prev => ({
        ...prev,
        subCastes: []
      }));

    }

  };

  loadSubCastes();

}, [formData.casteId]);

  // Load saved profile data on mount
  useEffect(() => {
    if (savedProfileData && Object.keys(savedProfileData).length > 0) {
      console.log('🔧 Loading profile data into settings:', savedProfileData);
      console.log('🔧 Checking specific fields:', {
        address: savedProfileData.address,
        smoking: savedProfileData.smoking,
        drinking: savedProfileData.drinking,
        country: savedProfileData.country,
        state: savedProfileData.state,
        cityId: savedProfileData.cityId,
        cityName: savedProfileData.cityName,
        companyName: savedProfileData.companyName,
        email: savedProfileData.email,
        phone: savedProfileData.phone
      });
      
      // Map backend API fields to frontend form fields
     const mappedData = {

       // BASIC
       firstName: savedProfileData.firstName || '',
       middleName: savedProfileData.middleName || '',
       lastName: savedProfileData.lastName || '',
       fullName:
         `${savedProfileData.firstName || ''} ${savedProfileData.lastName || ''}`.trim(),
       email: savedProfileData.email || '',
       phone: savedProfileData.phone || '',
       gender: savedProfileData.gender || '',
       dateOfBirth: savedProfileData.dateOfBirth || '',
       age: savedProfileData.age || calculateAge(savedProfileData.dateOfBirth),
       profilePhotoUrl:
         savedProfileData.imageUrl ||
         savedProfileData.profilePhotoUrl ||
         '',

       // RELIGION
       religionId: savedProfileData.religionId || null,
       casteId: savedProfileData.casteId || null,
       subCasteId: savedProfileData.subCasteId || null,
       motherTongueId: savedProfileData.motherTongueId || null,

       // PERSONAL
       maritalStatusId:
         savedProfileData.maritalStatusId || null,

       heightId: savedProfileData.heightId || null,

       weightId: savedProfileData.weightId || null,

       complexion: savedProfileData.complexion || '',

       bodyType: savedProfileData.bodyType || '',

aboutMe:
  savedProfileData.aboutMe ||
  savedProfileData.about ||
  savedProfileData.about_me ||
  '',

       // EDUCATION
       educationLevelId:
         savedProfileData.educationLevelId || null,

       occupationId:
         savedProfileData.occupationId || null,

       annualIncome:
         savedProfileData.annualIncome || '',

       companyName:
         savedProfileData.companyName || '',

       // LOCATION
      country: savedProfileData.country || '',
      state: savedProfileData.state || '',
      cityId: savedProfileData.cityId || null,
      city: savedProfileData.cityName || savedProfileData.city || '',
       address: savedProfileData.address || '',

       // FAMILY
       fatherName:
         savedProfileData.fatherName || '',

       fatherOccupation:
         savedProfileData.fatherOccupation || '',

       motherName:
         savedProfileData.motherName || '',

       motherOccupation:
         savedProfileData.motherOccupation || '',

       siblingsCount:
         savedProfileData.siblingsCount ||
         savedProfileData.siblings ||
         '',
       // DIET
       diet: savedProfileData.diet || '',
       smoking: savedProfileData.smoking || '',
       drinking: savedProfileData.drinking || '',

       // PARTNER PREFERENCE
       preferredAgeMin:
         savedProfileData.preferredAgeMin || '',

       preferredAgeMax:
         savedProfileData.preferredAgeMax || '',

       preferredLocation:
         savedProfileData.preferredLocation || '',

       preferredEducation:
         savedProfileData.preferredEducation || '',

       otherExpectations:
         savedProfileData.otherExpectations || ''

     };
      
      console.log('🔧 Mapped data for form:', mappedData);
            setFormData(prev => ({ ...prev, ...mappedData }));
    }
  }, [savedProfileData]);

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
        'heightId', 'weightId', 'cityId'
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
      error("Please select an image file");
      return;
    }

    // Validate file size (max 5MB)
    if (file.size > 5 * 1024 * 1024) {
      error("File size should be less than 5MB");
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

  const validateProfileForm = () => {
    // Only validate basic required fields
    if (!formData.firstName || formData.firstName.trim() === "") {
      error("First name is required");
      return false;
    }
    
    if (!formData.lastName || formData.lastName.trim() === "") {
      error("Last name is required");
      return false;
    }
    
    if (!formData.gender) {
      error("Gender is required");
      return false;
    }
    
    if (!formData.dateOfBirth) {
      error("Date of birth is required");
      return false;
    }

    if (formData.email && !formData.email.includes("@")) {
      error("Please enter a valid email address");
      return false;
    }

    return true;
  };

  const handleProfileUpdate = async () => {
    if (!validateProfileForm()) return;

    try {
      console.log('💾 Saving profile data:', formData);
      
      // Prepare data for backend API with correct field mapping
      // Parse fullName into firstName and lastName for backend
      const nameParts = (formData.fullName || '').trim().split(' ');
      const firstNameFromFull = nameParts[0] || formData.firstName;
      const lastNameFromFull = nameParts.slice(1).join(' ') || formData.lastName;
      
     const dataToSave = {

       // BASIC
       firstName: firstNameFromFull,
       middleName: formData.middleName,
       lastName: lastNameFromFull,

       email: formData.email,
       phone: formData.phone,

       gender: formData.gender,
       dateOfBirth: formData.dateOfBirth,

       // ABOUT
       aboutMe: formData.aboutMe || formData.about,

       imageUrl: formData.profilePhotoUrl,

       // RELIGION
       religionId: formData.religionId,
       casteId: formData.casteId,
       subCasteId: formData.subCasteId,
       motherTongueId: formData.motherTongueId,

       // PERSONAL
       maritalStatusId: formData.maritalStatusId,
       heightId: formData.heightId,
       weightId: formData.weightId,

       complexion: formData.complexion,
       bodyType: formData.bodyType,

       // EDUCATION
       educationLevelId: formData.educationLevelId,
       occupationId: formData.occupationId,

       annualIncome: formData.annualIncome,
       companyName: formData.companyName,

       // LOCATION
       country: formData.country,
       state: formData.state,
       cityId: formData.cityId,
       address: formData.address,

       // LIFESTYLE
       diet: formData.diet,
       smoking: formData.smoking,
       drinking: formData.drinking,

       // FAMILY
       fatherName: formData.fatherName,
       fatherOccupation: formData.fatherOccupation,

       motherName: formData.motherName,
       motherOccupation: formData.motherOccupation,

       siblingsCount: formData.siblingsCount,

       // PARTNER PREFERENCE
       preferredAgeMin: formData.preferredAgeMin,
       preferredAgeMax: formData.preferredAgeMax,

       preferredLocation: formData.preferredLocation,
       preferredEducation: formData.preferredEducation,

       otherExpectations: formData.otherExpectations

     };
      console.log('📤 Data to save to backend:', dataToSave);
      
      const result = await saveProfileData(dataToSave);
      
      if (result) {
        success("Profile updated successfully!");
        console.log('✅ Profile saved successfully');
      } else {
        error("Failed to update profile. Please try again.");
      }
      
      // Scroll to top
      window.scrollTo({ top: 0, behavior: "smooth" });
    } catch (err) {
      error("Failed to update profile. Please try again.");
      console.error("Profile update error:", err);
    }
  };

  const handlePasswordUpdate = () => {
    if (!passwordData.currentPassword || !passwordData.newPassword || !passwordData.confirmPassword) {
      error("All password fields are required");
      return;
    }
    if (passwordData.newPassword.length < 4) {
      error("New password must be at least 4 characters");
      return;
    }
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      error("New passwords do not match");
      return;
    }
    success("Password updated successfully!");
    setPasswordData({ currentPassword: "", newPassword: "", confirmPassword: "" });
  };

  const handleNotificationToggle = (label, isChecked) => {
    info(`${label} ${isChecked ? 'enabled' : 'disabled'}`);
  };

  // Helper function to render form fields
const renderField = (field) => {

  const {
    label,
    placeholder,
    type = "text",
    key,
    options,
    readOnly = false
  } = field;

  // =========================================
  // SELECT DROPDOWNS
  // =========================================
  if (type === "select") {

    let fieldOptions = [];

    // =========================================
    // BACKEND MASTER DATA OPTIONS
    // =========================================

    if (key === "gender") {

      fieldOptions =
        masterOptions.genders || [];

    }

    else if (key === "religionId") {

      fieldOptions =
        masterOptions.religions || [];

    }

    else if (key === "cityId") {

      fieldOptions =
        masterOptions.cities || [];

    }

    else if (key === "educationLevelId") {

      fieldOptions =
        masterOptions.educationLevels || [];

    }

    else if (key === "occupationId") {

      fieldOptions =
        masterOptions.occupations || [];

    }

    else if (key === "maritalStatusId") {

      fieldOptions =
        masterOptions.maritalStatuses || [];

    }

    else if (key === "casteId") {

      fieldOptions =
        masterOptions.castes || [];

    }

    else if (key === "subCasteId") {

      fieldOptions =
        masterOptions.subCastes || [];

    }

    else if (key === "motherTongueId") {

      fieldOptions =
        masterOptions.motherTongues || [];

    }

    else if (key === "heightId") {

      fieldOptions =
        masterOptions.heights || [];

    }

    else if (key === "weightId") {

      fieldOptions =
        masterOptions.weights || [];

    }

    // =========================================
    // STATIC OPTIONS
    // =========================================

    else if (key === "city") {

      fieldOptions =
        getOptions(key, formData.state) || [];

    }
else if (key === "complexionId") {

  fieldOptions =
    masterOptions.complexions || [];

}

else if (key === "bodyTypeId") {

  fieldOptions =
    masterOptions.bodyTypes || [];

}

    else {

      fieldOptions =
        options || getOptions(key) || [];

    }

    console.log(
      `📋 Dropdown options for ${key}:`,
      fieldOptions
    );

    return (

      <div key={key}>

        <label className="text-xs font-medium text-foreground mb-1 block">
          {label}
        </label>

        <select
          value={formData[key] || ""}

          onChange={(e) => {

            const value = e.target.value;

            console.log(
              `🔄 Changed ${key}:`,
              value
            );

            handleInputChange(
              key,
              value
            );

            // =========================================
            // RESET DEPENDENT DROPDOWNS
            // =========================================

            if (key === "religionId") {

              handleInputChange(
                "casteId",
                ""
              );

              handleInputChange(
                "subCasteId",
                ""
              );

            }

            if (key === "casteId") {

              handleInputChange(
                "subCasteId",
                ""
              );

            }

            if (key === "state") {

              handleInputChange(
                "city",
                ""
              );

            }

          }}

          className="
            w-full
            bg-background
            border
            border-border
            rounded-lg
            px-4
            py-2.5
            text-sm
            text-foreground
            focus:outline-none
            focus:ring-2
            focus:ring-primary/20
            focus:border-primary
          "
        >

          <option value="">
            Select {label.toLowerCase()}
          </option>

          {fieldOptions &&
            fieldOptions.length > 0 &&
            fieldOptions.map((opt) => {

              const optionValue =
                opt?.id || opt;

              const optionLabel =
                opt?.name || opt;

              return (

                <option
                  key={optionValue}
                  value={optionValue}
                >
                  {optionLabel}
                </option>

              );

            })}

        </select>

      </div>

    );

  }

  // =========================================
  // NORMAL INPUT FIELD
  // =========================================

  return (

    <div key={key}>

      <label className="text-xs font-medium text-foreground mb-1 block">
        {label}
      </label>

      <input
        type={type}

        value={formData[key] || ""}

        onChange={(e) =>
          handleInputChange(
            key,
            e.target.value
          )
        }

        placeholder={placeholder}

        readOnly={readOnly}

        className={`
          w-full
          bg-background
          border
          border-border
          rounded-lg
          px-4
          py-2.5
          text-sm
          text-foreground
          placeholder:text-muted-foreground
          focus:outline-none
          focus:ring-2
          focus:ring-primary/20
          focus:border-primary
          ${
            readOnly
              ? "bg-muted cursor-not-allowed"
              : ""
          }
        `}
      />

    </div>

  );

};

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      <div className="py-8 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Settings</h1>
        <p className="text-primary-foreground/70 text-sm">Manage your profile and preferences</p>
      </div>

      <div className="container mx-auto px-4 py-8 max-w-3xl">
        <div className="flex gap-2 mb-6">
          {tabs.map((t) => (
            <button key={t.id} onClick={() => setActiveTab(t.id)} className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-colors ${activeTab === t.id ? "bg-primary text-primary-foreground" : "bg-card border border-border text-muted-foreground hover:text-foreground"}`}>
              {t.icon} {t.label}
            </button>
          ))}
        </div>

        <motion.div key={activeTab} initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="bg-card rounded-xl border border-border p-6">
          {activeTab === "profile" && (
            <div className="space-y-6">
              <h2 className="text-lg font-display font-bold text-foreground mb-4">Update Profile</h2>

              {/* Profile Photo Upload */}
              <div className="border border-dashed border-border rounded-lg p-4 bg-muted/30">
                <label className="text-sm font-medium text-foreground mb-3 block">Profile Photo</label>
                {formData.profilePhotoUrl ? (
                  <div className="relative w-20 h-20 rounded-full overflow-hidden mb-3">
                    <img src={formData.profilePhotoUrl} alt="Profile" className="w-full h-full object-cover" />
                    <button
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

              {/* Personal Details Section */}
              <div>
                <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Personal Details</h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({ label: "Full Name", placeholder: "Your full name", key: "fullName" })}
                  {renderField({
                    label: "Gender",
                    key: "gender",
                    type: "select"
                  })}
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
                    key: "complexion", 
                    type: "select",
                  })}
                  {renderField({ 
                    label: "Body Type", 
                    key: "bodyType", 
                    type: "select",
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
                  {renderField({ label: "Annual Income", key: "annualIncome", type: "select" })}
                  {renderField({ label: "Company Name", placeholder: "Your company", key: "companyName" })}
                </div>
              </div>

              {/* Location Section */}
              <div>
                <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Location Details</h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({ label: "Country", key: "country", type: "select", options: ["India", "USA", "UK", "Canada", "Australia"] })}
                  {renderField({ label: "State/Province", key: "state", type: "select" })}
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
                    key: "diet", 
                    type: "select", 
                    options: ["Vegetarian", "Non-Vegetarian", "Vegan"] 
                  })}
                  {renderField({ 
                    label: "Smoking", 
                    key: "smoking", 
                    type: "select", 
                    options: ["No", "Yes", "Occasionally"] 
                  })}
                  {renderField({ 
                    label: "Drinking", 
                    key: "drinking", 
                    type: "select", 
                    options: ["No", "Yes", "Occasionally"] 
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
                  {renderField({ label: "Number of Siblings", key: "siblingsCount", type: "select" })}
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

              {/* Other Details Section */}
              <div>
                <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">About Me</h3>
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">About Me</label>
                  <textarea 
                    rows={4} 
                    value={formData.aboutMe}
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

              <button onClick={handleProfileUpdate} className="flex items-center gap-2 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold px-6 py-2.5 rounded-lg text-sm transition-colors">
                <Save className="h-4 w-4" /> Save Changes
              </button>
            </div>
          )}

          {activeTab === "password" && (
            <div className="space-y-4 max-w-md">
              <h2 className="text-lg font-display font-bold text-foreground mb-4">Change Password</h2>
              {[
                { label: "Current Password", key: "currentPassword" },
                { label: "New Password", key: "newPassword" },
                { label: "Confirm New Password", key: "confirmPassword" }
              ].map((field) => (
                <div key={field.key}>
                  <label className="text-xs font-medium text-foreground mb-1 block">{field.label}</label>
                  <input 
                    type="password" 
                    value={passwordData[field.key]}
                    onChange={(e) => setPasswordData({...passwordData, [field.key]: e.target.value})}
                    placeholder="••••••••" 
                    className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" 
                  />
                </div>
              ))}
              <button onClick={handlePasswordUpdate} className="flex items-center gap-2 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold px-6 py-2.5 rounded-lg text-sm transition-colors">
                <Lock className="h-4 w-4" /> Update Password
              </button>
            </div>
          )}

          {activeTab === "notifications" && (
            <div className="space-y-5">
              <h2 className="text-lg font-display font-bold text-foreground mb-4">Notification Settings</h2>
              {[
                { label: "New match notifications", desc: "Get notified when someone matches your preferences" },
                { label: "Interest received", desc: "Alerts when someone sends you an interest" },
                { label: "Message notifications", desc: "Notifications for new messages" },
                { label: "Profile views", desc: "Know when someone views your profile" },
                { label: "Promotional emails", desc: "Offers, tips, and Gathbandhan updates" },
              ].map((n, i) => (
                <div key={n.label} className="flex items-center justify-between py-2">
                  <div>
                    <p className="text-sm font-medium text-foreground">{n.label}</p>
                    <p className="text-xs text-muted-foreground">{n.desc}</p>
                  </div>
                  <label className="relative inline-flex items-center cursor-pointer">
                    <input 
                      type="checkbox" 
                      defaultChecked={i < 3} 
                      onChange={(e) => handleNotificationToggle(n.label, e.target.checked)}
                      className="sr-only peer" 
                    />
                    <div className="w-9 h-5 bg-muted rounded-full peer peer-checked:bg-primary transition-colors after:content-[''] after:absolute after:top-0.5 after:left-0.5 after:bg-background after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:after:translate-x-4" />
                  </label>
                </div>
              ))}
            </div>
          )}
        </motion.div>
      </div>
    </div>
  );
};

export default SettingsPage;
