import { useState, useEffect } from "react";
import { User, Lock, Bell, Save, Upload, X } from "lucide-react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import { useToast } from "@/components/Toast";
import { useProfileData } from "@/hooks/useProfileData";
import { useMatrimonyOptions } from "@/hooks/useMatrimonyOptions";

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
  
  // Profile form state - all matrimony fields
  const [formData, setFormData] = useState({
    // Personal Details
    fullName: "",
    gender: "",
    dateOfBirth: "",
    age: "",
    maritalStatus: "",
    religion: "",
    caste: "",
    subCaste: "",
    motherTongue: "",
    
    // Physical Details
    height: "",
    weight: "",
    complexion: "",
    bodyType: "",
    
    // Education & Career
    highestEducation: "",
    profession: "",
    annualIncome: "",
    companyName: "",
    
    // Location
    country: "India",
    state: "",
    city: "",
    address: "",
    
    // Lifestyle
    diet: "",
    smoking: "",
    drinking: "",
    
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
    email: "",
    phone: "",
    profilePhoto: null,
    profilePhotoUrl: ""
  });
  
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: ""
  });

  // Load saved profile data on mount
  useEffect(() => {
    if (savedProfileData && Object.keys(savedProfileData).length > 0) {
      setFormData(prev => ({ ...prev, ...savedProfileData }));
    }
  }, []);

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
      setFormData(prev => ({ ...prev, [field]: value }));
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
    const requiredFields = ['fullName', 'gender', 'dateOfBirth', 'maritalStatus', 'religion', 'motherTongue', 'highestEducation', 'profession', 'city'];
    
    for (let field of requiredFields) {
      if (!formData[field] || formData[field].toString().trim() === "") {
        error(`${field.replace(/([A-Z])/g, ' $1').trim()} is required`);
        return false;
      }
    }

    if (!formData.email.includes("@")) {
      error("Please enter a valid email address");
      return false;
    }

    return true;
  };

  const handleProfileUpdate = async () => {
    if (!validateProfileForm()) return;

    try {
      // Save profile data to localStorage
      const dataToSave = { ...formData };
      delete dataToSave.profilePhoto; // Don't save File object
      
      await saveProfileData(dataToSave);
      success("Profile updated successfully!");
      
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
    const { label, placeholder, type = "text", key, options } = field;
    
    if (type === "select") {
      let fieldOptions = options;
      
      // Handle city dropdown based on selected state
      if (key === 'city') {
        fieldOptions = getOptions(key, formData.state);
      } else if (!options) {
        fieldOptions = getOptions(key);
      }
      
      return (
        <div key={key}>
          <label className="text-xs font-medium text-foreground mb-1 block">{label}</label>
          <select 
            value={formData[key]}
            onChange={(e) => {
              handleInputChange(key, e.target.value);
              // Reset city when state changes
              if (key === 'state') {
                handleInputChange('city', '');
              }
            }}
            className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary"
          >
            <option value="">Select {label.toLowerCase()}</option>
            {fieldOptions.map(opt => (
              <option key={opt} value={opt}>{opt}</option>
            ))}
          </select>
        </div>
      );
    }

    return (
      <div key={key}>
        <label className="text-xs font-medium text-foreground mb-1 block">{label}</label>
        <input 
          type={type} 
          value={formData[key]}
          onChange={(e) => handleInputChange(key, e.target.value)}
          placeholder={placeholder} 
          className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" 
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
                    type: "select", 
                    options: ["Male", "Female", "Other"] 
                  })}
                  {renderField({ label: "Date of Birth", type: "date", key: "dateOfBirth" })}
                  {renderField({ label: "Age", type: "number", key: "age", placeholder: "Auto-calculated" })}
                  {renderField({ 
                    label: "Marital Status", 
                    key: "maritalStatus", 
                    type: "select", 
                    options: ["Single", "Divorced", "Widowed", "Married"] 
                  })}
                  {renderField({ 
                    label: "Religion", 
                    key: "religion", 
                    type: "select", 
                    options: ["Hindu", "Muslim", "Christian", "Sikh", "Buddhist", "Other"] 
                  })}
                  {renderField({ label: "Caste", key: "caste", type: "select" })}
                  {renderField({ label: "Sub-caste", key: "subCaste", type: "select" })}
                  {renderField({ label: "Mother Tongue", key: "motherTongue", type: "select" })}
                </div>
              </div>

              {/* Physical Details Section */}
              <div>
                <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Physical Details</h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({ label: "Height", key: "height", type: "select" })}
                  {renderField({ label: "Weight", key: "weight", type: "select" })}
                  {renderField({ 
                    label: "Complexion", 
                    key: "complexion", 
                    type: "select", 
                    options: ["Fair", "Wheatish", "Wheatish Brown", "Brown", "Dark"] 
                  })}
                  {renderField({ 
                    label: "Body Type", 
                    key: "bodyType", 
                    type: "select", 
                    options: ["Slim", "Average", "Athletic", "Heavy"] 
                  })}
                </div>
              </div>

              {/* Education & Career Section */}
              <div>
                <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Education & Career</h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({ 
                    label: "Highest Education", 
                    key: "highestEducation", 
                    type: "select", 
                    options: ["10th", "12th", "Bachelor's", "Master's", "PhD", "Professional Degree"] 
                  })}
                  {renderField({ label: "Profession/Occupation", placeholder: "Your profession", key: "profession" })}
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
                  {renderField({ label: "City", key: "city", type: "select" })}
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
