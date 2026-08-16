import { useState, useEffect, useRef } from "react";
import {
  User,
  Lock,
  Bell,
  Save,
  Upload,
  X,
  Ban,
  Eye,
  EyeOff,
  Loader2,
  Plus,
  Minus,
  Camera,
  Ruler,
  GraduationCap,
  MapPin,
  Heart,
  Users,
  FileText,
  HeartHandshake,
  Image as ImageIcon,
} from "lucide-react";
import { motion } from "framer-motion";

import { useToast } from "@/components/Toast";
import { useProfileData } from "@/hooks/useProfileData";
import {
  partnerPreferenceAPI,
  masterDataAPI,
  photoAPI,
  blockAPI,
  notificationPreferenceAPI,
  authAPI,
} from "@/services/api";
import { useAuth } from "@/hooks/useAuth";
import { useNavigate } from "react-router-dom";
import SearchableSelect from "@/components/SearchableSelect";

const tabs = [
  { id: "profile", label: "Profile", icon: <User className="h-4 w-4" /> },
  { id: "password", label: "Password", icon: <Lock className="h-4 w-4" /> },
  { id: "notifications", label: "Notifications", icon: <Bell className="h-4 w-4" /> },
  { id: "blocked", label: "Blocked Users", icon: <Ban className="h-4 w-4" /> },
];

/**
 * Reusable master data sorting helper (Case-insensitive A-Z by display name)
 */
export const getDisplayName = (option) => {
  if (!option) return "";
  if (typeof option === "string" || typeof option === "number") return String(option);
  return (
    option.name ||
    option.value ||
    option.label ||
    option.cityName ||
    option.stateName ||
    option.countryName ||
    option.religionName ||
    option.casteName ||
    option.subCasteName ||
    option.height ||
    option.range ||
    ""
  );
};

export const sortMasterOptions = (options = []) => {
  if (!Array.isArray(options)) return [];
  return [...options].sort((a, b) =>
    getDisplayName(a).localeCompare(
      getDisplayName(b),
      undefined,
      { sensitivity: "base", numeric: true }
    )
  );
};

// Reusable Accordion Settings Section Component
const SettingsSection = ({
  id,
  title,
  icon: Icon,
  isOpen,
  onToggle,
  children,
}) => {
  return (
    <div className="border border-border rounded-xl bg-card overflow-hidden shadow-sm transition-all duration-200 hover:border-primary/30 mb-4">
      <button
        type="button"
        onClick={() => onToggle(id)}
        className="w-full flex items-center justify-between p-4 bg-muted/20 hover:bg-muted/50 transition-colors text-left focus:outline-none focus:ring-2 focus:ring-primary/20 cursor-pointer"
      >
        <div className="flex items-center gap-3">
          <div className="p-2 rounded-lg bg-primary/10 text-primary flex items-center justify-center">
            {Icon && <Icon className="h-5 w-5" />}
          </div>
          <span className="font-semibold text-base text-foreground tracking-tight">
            {title}
          </span>
        </div>
        <div className="p-1.5 rounded-full bg-muted text-muted-foreground hover:text-foreground hover:bg-muted/80 transition-colors flex items-center justify-center">
          {isOpen ? (
            <Minus className="h-4 w-4" />
          ) : (
            <Plus className="h-4 w-4" />
          )}
        </div>
      </button>

      <motion.div
        initial={false}
        animate={{
          height: isOpen ? "auto" : 0,
          opacity: isOpen ? 1 : 0,
        }}
        transition={{ duration: 0.25, ease: "easeInOut" }}
        className="overflow-hidden"
      >
        <div className="p-5 border-t border-border/50">{children}</div>
      </motion.div>
    </div>
  );
};

const SettingsPage = () => {
  const [activeTab, setActiveTab] = useState("profile");
  const { success, error } = useToast();
  const { logout } = useAuth();
  const navigate = useNavigate();

  const [profileSaving, setProfileSaving] = useState(false);
  const [passwordLoading, setPasswordLoading] = useState(false);
  const [notificationLoading, setNotificationLoading] = useState(false);
  const [blockedLoading, setBlockedLoading] = useState(false);

  const [openSections, setOpenSections] = useState({
    profilePhoto: true,
    personalDetails: true,
    physicalDetails: false,
    educationCareer: false,
    locationDetails: false,
    lifestyle: false,
    familyDetails: false,
    aboutMe: false,
    partnerPreferences: false,
    photoGallery: false,
  });

  const toggleSection = (id) => {
    setOpenSections((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

  const [showPassword, setShowPassword] = useState({
    currentPassword: false,
    newPassword: false,
    confirmPassword: false,
  });

  const { profileData: savedProfileData, saveProfileData } = useProfileData();

  // Cache refs for dependent dropdowns & lazy-loaded tabs
  const casteCache = useRef(new Map());
  const subCasteCache = useRef(new Map());
  const partnerCasteCache = useRef(new Map());
  const notificationsLoaded = useRef(false);
  const blockedUsersLoaded = useRef(false);
  const masterLoadedRef = useRef(false);
  const objectUrlsToRevoke = useRef(new Set());

  // Loading state per priority
  const [masterLoading, setMasterLoading] = useState({
    highPriority: true,
    progressive: true,
  });

  const [masterOptions, setMasterOptions] = useState({
    religions: [],
    genders: [],
    cities: [],
    allCities: [],
    userStateCities: [],
    educationLevels: [],
    occupations: [],
    heights: [],
    weights: [],
    maritalStatuses: [],
    castes: [],
    partnerCastes: [],
    subCastes: [],
    complexions: [],
    bodyTypes: [],
    motherTongues: [],
    countries: [],
    incomes: [],
    diets: [],
    smokingOptions: [],
    drinkingOptions: [],
    states: [],
    profileTypes: [],
    manglikStatuses: [],
    familyTypes: [],
    familyStatuses: [],
    familyValues: [],
    qualifications: [],
    fieldsOfStudy: [],
    employmentStatuses: [],
    disabilityStatuses: [],
    bloodGroups: [],
  });

  const [formData, setFormData] = useState({
    firstName: "",
    middleName: "",
    lastName: "",
    fullName: "",
    email: "",
    phone: "",
    dateOfBirth: "",
    about: "",
    imageUrl: "",
    religionId: null,
    casteId: null,
    subCasteId: null,
    motherTongueId: null,
    maritalStatusId: null,
    educationLevelId: null,
    occupationId: null,
    heightId: null,
    genderId: null,
    complexionId: null,
    bodyTypeId: null,
    countryId: null,
    stateId: null,
    weightId: null,
    cityId: null,
    incomeId: null,
    dietId: null,
    smokingId: null,
    drinkingId: null,
    fatherName: "",
    fatherOccupation: "",
    motherName: "",
    motherOccupation: "",
    siblingsCount: "",
    companyName: "",
    address: "",
    aboutMe: "",
    profileTypeId: null,
    manglikStatusId: null,
    familyTypeId: null,
    familyStatusId: null,
    familyValueId: null,
    qualificationId: null,
    fieldOfStudyId: null,
    employedId: null,
    disabilityStatusId: null,
    bloodGroupId: null,
    age: "",
    profilePhoto: null,
    profilePhotoUrl: "",
  });

  const [galleryPhotos, setGalleryPhotos] = useState([]);
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  const [notificationSettings, setNotificationSettings] = useState({
    matchNotifications: true,
    interestNotifications: true,
    messageNotifications: true,
    profileViewNotifications: false,
    promotionalEmails: false,
  });

  const [blockedUsers, setBlockedUsers] = useState([]);
  const [partnerPreferenceId, setPartnerPreferenceId] = useState(null);
  const [partnerPreference, setPartnerPreference] = useState({
    minAge: "",
    maxAge: "",
    minHeight: "",
    maxHeight: "",
    minWeight: "",
    maxWeight: "",
    religionId: null,
    casteId: null,
    cityId: null,
    educationLevelId: null,
    occupationId: null,
    maritalStatusId: null,
    smokingId: null,
    drinkingId: null,
    dietId: null,
    otherExpectations: "",
  });

  // Memory cleanup on unmount for blob URLs created during gallery uploads
  useEffect(() => {
    return () => {
      objectUrlsToRevoke.current.forEach((url) => URL.revokeObjectURL(url));
      objectUrlsToRevoke.current.clear();
    };
  }, []);

  // Priority-based and Progressive Master Data Loading with Error Isolation
  useEffect(() => {
    if (masterLoadedRef.current) return;
    masterLoadedRef.current = true;

    const extractArray = (res) => {
      if (res.status === "fulfilled") {
        const val = res.value;
        if (Array.isArray(val?.data)) return val.data;
        if (Array.isArray(val)) return val;
      }
      return [];
    };

    const loadMasterDataPriority = async () => {
      // 1. High Priority APIs (Loads first)
      try {
        const highPriorityResults = await Promise.allSettled([
          masterDataAPI.getProfileTypes(),
          masterDataAPI.getGenders(),
          masterDataAPI.getReligions(),
          masterDataAPI.getMaritalStatuses(),
          masterDataAPI.getEducationLevels(),
          masterDataAPI.getOccupations(),
        ]);

        setMasterOptions((prev) => ({
          ...prev,
          profileTypes: sortMasterOptions(extractArray(highPriorityResults[0])),
          genders: sortMasterOptions(extractArray(highPriorityResults[1])),
          religions: sortMasterOptions(extractArray(highPriorityResults[2])),
          maritalStatuses: sortMasterOptions(extractArray(highPriorityResults[3])),
          educationLevels: sortMasterOptions(extractArray(highPriorityResults[4])),
          occupations: sortMasterOptions(extractArray(highPriorityResults[5])),
        }));
      } catch (err) {
        console.error("High priority master data load failed:", err);
      } finally {
        setMasterLoading((prev) => ({ ...prev, highPriority: false }));
      }

      // 2. Progressive Master Data APIs (Non-blocking secondary background load)
      try {
        const progressiveResults = await Promise.allSettled([
          masterDataAPI.getQualifications(),
          masterDataAPI.getFieldsOfStudy(),
          masterDataAPI.getEmploymentStatuses(),
          masterDataAPI.getHeights(),
          masterDataAPI.getWeights(),
          masterDataAPI.getComplexions(),
          masterDataAPI.getBodyTypes(),
          masterDataAPI.getMotherTongues(),
          masterDataAPI.getFamilyTypes(),
          masterDataAPI.getFamilyStatuses(),
          masterDataAPI.getFamilyValues(),
          masterDataAPI.getManglikStatuses(),
          masterDataAPI.getBloodGroups(),
          masterDataAPI.getDisabilityStatuses(),
          masterDataAPI.getIncomes(),
          masterDataAPI.getDiets(),
          masterDataAPI.getSmokingOptions(),
          masterDataAPI.getDrinkingOptions(),
          masterDataAPI.getCountries(),
          masterDataAPI.getStates(),
          masterDataAPI.getCities(),
        ]);

        const allCitiesData = sortMasterOptions(extractArray(progressiveResults[20]));
        setMasterOptions((prev) => ({
          ...prev,
          qualifications: sortMasterOptions(extractArray(progressiveResults[0])),
          fieldsOfStudy: sortMasterOptions(extractArray(progressiveResults[1])),
          employmentStatuses: sortMasterOptions(extractArray(progressiveResults[2])),
          heights: sortMasterOptions(extractArray(progressiveResults[3])),
          weights: sortMasterOptions(extractArray(progressiveResults[4])),
          complexions: sortMasterOptions(extractArray(progressiveResults[5])),
          bodyTypes: sortMasterOptions(extractArray(progressiveResults[6])),
          motherTongues: sortMasterOptions(extractArray(progressiveResults[7])),
          familyTypes: sortMasterOptions(extractArray(progressiveResults[8])),
          familyStatuses: sortMasterOptions(extractArray(progressiveResults[9])),
          familyValues: sortMasterOptions(extractArray(progressiveResults[10])),
          manglikStatuses: sortMasterOptions(extractArray(progressiveResults[11])),
          bloodGroups: sortMasterOptions(extractArray(progressiveResults[12])),
          disabilityStatuses: sortMasterOptions(extractArray(progressiveResults[13])),
          incomes: sortMasterOptions(extractArray(progressiveResults[14])),
          diets: sortMasterOptions(extractArray(progressiveResults[15])),
          smokingOptions: sortMasterOptions(extractArray(progressiveResults[16])),
          drinkingOptions: sortMasterOptions(extractArray(progressiveResults[17])),
          countries: sortMasterOptions(extractArray(progressiveResults[18])),
          states: sortMasterOptions(extractArray(progressiveResults[19])),
          cities: allCitiesData,
          allCities: allCitiesData,
        }));
      } catch (err) {
        console.error("Progressive master data load failed:", err);
      } finally {
        setMasterLoading((prev) => ({ ...prev, progressive: false }));
      }
    };

    loadMasterDataPriority();
  }, []);

  // Load Saved Profile Data onto Form
  useEffect(() => {
    if (savedProfileData && Object.keys(savedProfileData).length > 0) {
      const mappedData = {
        firstName: savedProfileData.firstName || "",
        middleName: savedProfileData.middleName || "",
        lastName: savedProfileData.lastName || "",
        fullName: `${savedProfileData.firstName || ""} ${savedProfileData.lastName || ""}`.trim(),
        email: savedProfileData.email || "",
        phone: savedProfileData.phone || "",
        genderId: savedProfileData.genderId ? Number(savedProfileData.genderId) : null,
        dateOfBirth: savedProfileData.dateOfBirth || "",
        age: savedProfileData.age || calculateAge(savedProfileData.dateOfBirth),
        profilePhotoUrl: savedProfileData.imageUrl || savedProfileData.profilePhotoUrl || "",
        religionId: savedProfileData.religionId
          ? Number(savedProfileData.religionId)
          : savedProfileData.religion?.id
          ? Number(savedProfileData.religion.id)
          : null,
        casteId: savedProfileData.casteId
          ? Number(savedProfileData.casteId)
          : savedProfileData.caste?.id
          ? Number(savedProfileData.caste.id)
          : null,
        subCasteId: savedProfileData.subCasteId
          ? Number(savedProfileData.subCasteId)
          : savedProfileData.subCaste?.id
          ? Number(savedProfileData.subCaste.id)
          : null,
        motherTongueId: savedProfileData.motherTongueId
          ? Number(savedProfileData.motherTongueId)
          : savedProfileData.motherTongue?.id
          ? Number(savedProfileData.motherTongue.id)
          : null,
        maritalStatusId: savedProfileData.maritalStatusId
          ? Number(savedProfileData.maritalStatusId)
          : savedProfileData.maritalStatus?.id
          ? Number(savedProfileData.maritalStatus.id)
          : null,
        heightId: savedProfileData.heightId
          ? Number(savedProfileData.heightId)
          : savedProfileData.height?.id
          ? Number(savedProfileData.height.id)
          : null,
        weightId: savedProfileData.weightId
          ? Number(savedProfileData.weightId)
          : savedProfileData.weight?.id
          ? Number(savedProfileData.weight.id)
          : null,
        complexionId: savedProfileData.complexionId ? Number(savedProfileData.complexionId) : null,
        bodyTypeId: savedProfileData.bodyTypeId ? Number(savedProfileData.bodyTypeId) : null,
        aboutMe: savedProfileData.aboutMe || savedProfileData.about || savedProfileData.about_me || "",
        about: savedProfileData.about || "",
        educationLevelId: savedProfileData.educationLevelId ? Number(savedProfileData.educationLevelId) : null,
        occupationId: savedProfileData.occupationId ? Number(savedProfileData.occupationId) : null,
        incomeId: savedProfileData.incomeId ? Number(savedProfileData.incomeId) : null,
        companyName: savedProfileData.companyName || "",
        profileTypeId: savedProfileData.profileTypeId ? Number(savedProfileData.profileTypeId) : null,
        manglikStatusId: savedProfileData.manglikStatusId ? Number(savedProfileData.manglikStatusId) : null,
        familyTypeId: savedProfileData.familyTypeId ? Number(savedProfileData.familyTypeId) : null,
        familyStatusId: savedProfileData.familyStatusId ? Number(savedProfileData.familyStatusId) : null,
        familyValueId: savedProfileData.familyValueId ? Number(savedProfileData.familyValueId) : null,
        qualificationId: savedProfileData.qualificationId ? Number(savedProfileData.qualificationId) : null,
        fieldOfStudyId: savedProfileData.fieldOfStudyId ? Number(savedProfileData.fieldOfStudyId) : null,
        employedId: savedProfileData.employedStatusId
          ? Number(savedProfileData.employedStatusId)
          : savedProfileData.employedId
          ? Number(savedProfileData.employedId)
          : null,
        disabilityStatusId: savedProfileData.disabilityStatusId ? Number(savedProfileData.disabilityStatusId) : null,
        bloodGroupId: savedProfileData.bloodGroupId
          ? Number(savedProfileData.bloodGroupId)
          : savedProfileData.bloodGroup?.id
          ? Number(savedProfileData.bloodGroup.id)
          : null,
        countryId: savedProfileData.countryId
          ? Number(savedProfileData.countryId)
          : savedProfileData.country?.id
          ? Number(savedProfileData.country.id)
          : null,
        stateId: savedProfileData.stateId
          ? Number(savedProfileData.stateId)
          : savedProfileData.state?.id
          ? Number(savedProfileData.state.id)
          : null,
        cityId: savedProfileData.cityId
          ? Number(savedProfileData.cityId)
          : savedProfileData.city?.id
          ? Number(savedProfileData.city.id)
          : null,
        address: savedProfileData.address || "",
        fatherName: savedProfileData.fatherName || "",
        fatherOccupation: savedProfileData.fatherOccupation || "",
        motherName: savedProfileData.motherName || "",
        motherOccupation: savedProfileData.motherOccupation || "",
        siblingsCount: savedProfileData.siblingsCount || savedProfileData.siblings || "",
        dietId: savedProfileData.dietId
          ? Number(savedProfileData.dietId)
          : savedProfileData.diet?.id
          ? Number(savedProfileData.diet.id)
          : null,
        smokingId: savedProfileData.smokingId
          ? Number(savedProfileData.smokingId)
          : savedProfileData.smoking?.id
          ? Number(savedProfileData.smoking.id)
          : savedProfileData.smokingHabitId
          ? Number(savedProfileData.smokingHabitId)
          : null,
        drinkingId: savedProfileData.drinkingId
          ? Number(savedProfileData.drinkingId)
          : savedProfileData.drinking?.id
          ? Number(savedProfileData.drinking.id)
          : savedProfileData.drinkingHabitId
          ? Number(savedProfileData.drinkingHabitId)
          : null,
      };
      setFormData((prev) => ({
        ...prev,
        ...mappedData,
        profilePhotoUrl: savedProfileData.imageUrl || savedProfileData.profilePhotoUrl || prev.profilePhotoUrl || "",
      }));
    }
  }, [savedProfileData]);

  // Load Partner Preference once user profile is available
  useEffect(() => {
    const userId = savedProfileData?.userId || savedProfileData?.id;
    if (!userId) return;

    const loadPartnerPreference = async () => {
      try {
        const pref = await partnerPreferenceAPI.getMyPreference(userId);
        if (pref) {
          setPartnerPreferenceId(pref?.id ?? null);
          setPartnerPreference({
            minAge: pref?.minAge ?? "",
            maxAge: pref?.maxAge ?? "",
            minHeight: pref?.minHeight ?? "",
            maxHeight: pref?.maxHeight ?? "",
            minWeight: pref?.minWeight ?? "",
            maxWeight: pref?.maxWeight ?? "",
            religionId: pref?.religionId ? Number(pref.religionId) : null,
            casteId: pref?.casteId ? Number(pref.casteId) : null,
            cityId: pref?.cityId ? Number(pref.cityId) : null,
            educationLevelId: pref?.educationLevelId ? Number(pref.educationLevelId) : null,
            occupationId: pref?.occupationId ? Number(pref.occupationId) : null,
            maritalStatusId: pref?.maritalStatusId ? Number(pref.maritalStatusId) : null,
            smokingId: pref?.smokingId ? Number(pref.smokingId) : null,
            drinkingId: pref?.drinkingId ? Number(pref.drinkingId) : null,
            dietId: pref?.dietId ? Number(pref.dietId) : null,
            otherExpectations: pref?.otherExpectations ?? "",
          });
        }
      } catch (err) {
        setPartnerPreferenceId(null);
      }
    };

    loadPartnerPreference();
  }, [savedProfileData]);

  // Dependent Cached Loading: Religion -> Caste
  useEffect(() => {
    const religionId = formData.religionId;
    if (!religionId) {
      setMasterOptions((prev) => ({ ...prev, castes: [] }));
      return;
    }

    if (casteCache.current.has(religionId)) {
      setMasterOptions((prev) => ({ ...prev, castes: casteCache.current.get(religionId) }));
      return;
    }

    const loadProfileCastes = async () => {
      try {
        const castes = await masterDataAPI.getCastes(religionId);
        const safeCastes = Array.isArray(castes) ? castes : Array.isArray(castes?.data) ? castes.data : [];
        const sortedCastes = sortMasterOptions(safeCastes);
        casteCache.current.set(religionId, sortedCastes);
        setMasterOptions((prev) => ({ ...prev, castes: sortedCastes }));
      } catch (err) {
        console.error("Failed to load castes:", err);
        setMasterOptions((prev) => ({ ...prev, castes: [] }));
      }
    };

    loadProfileCastes();
  }, [formData.religionId]);

  // Dependent Cached Loading: Caste -> SubCaste (with N/A fallback & auto-selection)
  useEffect(() => {
    const casteId = Number(formData.casteId);
    if (!casteId) {
      setMasterOptions((prev) => ({ ...prev, subCastes: [] }));
      return;
    }

    if (subCasteCache.current.has(casteId)) {
      let cached = subCasteCache.current.get(casteId);
      if (!cached || cached.length === 0) {
        cached = [{ id: "N/A", casteId, name: "N/A" }];
        subCasteCache.current.set(casteId, cached);
      }
      setMasterOptions((prev) => ({ ...prev, subCastes: cached }));
      if (cached.length === 1 && (cached[0].name === "N/A" || cached[0].name === "Not Applicable")) {
        setFormData((prev) => {
          if (!prev.subCasteId || (typeof cached[0].id === 'number' && Number(prev.subCasteId) !== Number(cached[0].id))) {
            return { ...prev, subCasteId: cached[0].id };
          }
          return prev;
        });
      }
      return;
    }

    const loadSubCastes = async () => {
      try {
        const subCastes = await masterDataAPI.getSubCastes(casteId);
        let rawSubCastes = Array.isArray(subCastes) ? subCastes : Array.isArray(subCastes?.data) ? subCastes.data : [];

        // Check if there are real sub-castes for this caste (excluding N/A)
        const realSubCastes = rawSubCastes.filter(
          (sc) =>
            sc.name &&
            sc.name.trim().toUpperCase() !== "N/A" &&
            sc.name.trim().toUpperCase() !== "NOT APPLICABLE"
        );

        let safeSubCastes = [];
        if (realSubCastes.length > 0) {
          // Caste has actual sub-castes: show only actual sub-castes (no N/A)
          safeSubCastes = realSubCastes;
        } else {
          // Caste has NO actual sub-castes: find or generate N/A
          const naOptionInRaw = rawSubCastes.find(
            (sc) =>
              sc.name?.trim().toUpperCase() === "N/A" ||
              sc.name?.trim().toUpperCase() === "NOT APPLICABLE"
          );
          if (naOptionInRaw) {
            safeSubCastes = [naOptionInRaw];
          } else {
            const allSubCastes = await masterDataAPI.getSubCastes(null);
            const globalNa = Array.isArray(allSubCastes)
              ? allSubCastes.find(
                  (sc) =>
                    sc.name?.trim().toUpperCase() === "N/A" ||
                    sc.name?.trim().toUpperCase() === "NOT APPLICABLE"
                )
              : null;
            if (globalNa) {
              safeSubCastes = [{ ...globalNa, casteId }];
            } else {
              safeSubCastes = [{ id: "N/A", casteId, name: "N/A" }];
            }
          }
        }

        const sortedSubCastes = sortMasterOptions(safeSubCastes);
        subCasteCache.current.set(casteId, sortedSubCastes);
        setMasterOptions((prev) => ({ ...prev, subCastes: sortedSubCastes }));

        // Auto-select N/A if it is the only sub-caste option available for this caste
        if (
          sortedSubCastes.length === 1 &&
          (sortedSubCastes[0].name === "N/A" || sortedSubCastes[0].name === "Not Applicable")
        ) {
          setFormData((prev) => ({
            ...prev,
            subCasteId: sortedSubCastes[0].id,
          }));
        }
      } catch (error) {
        console.error("Failed to load sub castes:", error);
        const fallbackNA = [{ id: "N/A", casteId, name: "N/A" }];
        setMasterOptions((prev) => ({ ...prev, subCastes: fallbackNA }));
        setFormData((prev) => ({ ...prev, subCasteId: "N/A" }));
      }
    };

    loadSubCastes();
  }, [formData.casteId]);

  // Dependent Cached Loading: Partner Religion -> Partner Caste
  useEffect(() => {
    const religionId = partnerPreference.religionId;
    if (!religionId) {
      setMasterOptions((prev) => ({ ...prev, partnerCastes: [] }));
      return;
    }

    if (partnerCasteCache.current.has(religionId)) {
      setMasterOptions((prev) => ({ ...prev, partnerCastes: partnerCasteCache.current.get(religionId) }));
      return;
    }

    const loadPartnerCastes = async () => {
      try {
        const castes = await masterDataAPI.getCastes(religionId);
        const safeCastes = Array.isArray(castes) ? castes : Array.isArray(castes?.data) ? castes.data : [];
        const sortedCastes = sortMasterOptions(safeCastes);
        partnerCasteCache.current.set(religionId, sortedCastes);
        setMasterOptions((prev) => ({ ...prev, partnerCastes: sortedCastes }));
      } catch (error) {
        console.error("Failed to load partner castes:", error);
        setMasterOptions((prev) => ({ ...prev, partnerCastes: [] }));
      }
    };

    loadPartnerCastes();
  }, [partnerPreference.religionId]);

  // Dependent Loading: State -> Cities (User Location filtering)
  useEffect(() => {
    if (!formData.stateId) {
      setMasterOptions((prev) => ({ ...prev, userStateCities: [] }));
      return;
    }
    const loadStateCities = async () => {
      try {
        const cities = await masterDataAPI.getCitiesByState(formData.stateId);
        const safeCities = Array.isArray(cities) ? cities : Array.isArray(cities?.data) ? cities.data : [];
        const sortedCities = sortMasterOptions(safeCities);
        setMasterOptions((prev) => ({ ...prev, userStateCities: sortedCities }));
      } catch (err) {
        console.error("Failed to load state cities:", err);
        setMasterOptions((prev) => ({ ...prev, userStateCities: [] }));
      }
    };
    loadStateCities();
  }, [formData.stateId]);

  // Lazy Load Notifications Tab Data
  useEffect(() => {
    if (activeTab === "notifications" && !notificationsLoaded.current) {
      notificationsLoaded.current = true;
      const loadNotificationPreferences = async () => {
        try {
          setNotificationLoading(true);
          const response = await notificationPreferenceAPI.getMyPreferences();
          if (response) {
            setNotificationSettings({
              matchNotifications: response.matchNotifications ?? true,
              interestNotifications: response.interestNotifications ?? true,
              messageNotifications: response.messageNotifications ?? true,
              profileViewNotifications: response.profileViewNotifications ?? false,
              promotionalEmails: response.promotionalEmails ?? false,
            });
          }
        } catch (err) {
          console.error("Notification Preference Error:", err);
          error("Failed to load notification preferences");
        } finally {
          setNotificationLoading(false);
        }
      };
      loadNotificationPreferences();
    }
  }, [activeTab]);

  // Lazy Load Blocked Users Tab Data
  useEffect(() => {
    if (activeTab === "blocked" && !blockedUsersLoaded.current) {
      const userId = savedProfileData?.userId || savedProfileData?.id;
      if (userId) {
        blockedUsersLoaded.current = true;
        const loadBlocked = async () => {
          try {
            setBlockedLoading(true);
            const response = await blockAPI.getMyBlockedUsers(userId);
            setBlockedUsers(Array.isArray(response) ? response : []);
          } catch (err) {
            console.error("Failed to load blocked users", err);
            setBlockedUsers([]);
          } finally {
            setBlockedLoading(false);
          }
        };
        loadBlocked();
      }
    }
  }, [activeTab, savedProfileData]);

  // Load Photo Gallery on Mount & Identify Primary Photo
  useEffect(() => {
    const loadGalleryPhotos = async () => {
      try {
        const photos = await photoAPI.getMyPhotos();
        const rawList = Array.isArray(photos) ? photos : photos?.data || photos?.photos || [];
        if (Array.isArray(rawList)) {
          const mapped = rawList.map((photo) => ({
            id: photo.id,
            preview: photo.photoUrl,
            photoUrl: photo.photoUrl,
            primaryPhoto: Boolean(photo.primaryPhoto || photo.isPrimary),
            isPrimary: Boolean(photo.primaryPhoto || photo.isPrimary),
            photoType: photo.photoType,
            uploaded: true,
          }));
          setGalleryPhotos(mapped);

          // Use authoritative primary photo from backend or latest photo as fallback
          if (mapped.length > 0) {
            const primary = mapped.find((p) => p.primaryPhoto || p.isPrimary) || mapped[mapped.length - 1];
            if (primary && primary.photoUrl) {
              setFormData((prev) => ({
                ...prev,
                profilePhotoUrl: primary.photoUrl,
              }));
            }
          }
        }
      } catch (err) {
        console.error("Failed to load gallery photos:", err);
      }
    };
    loadGalleryPhotos();
  }, []);

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
      setFormData((prev) => ({
        ...prev,
        dateOfBirth: value,
        age: calculatedAge,
      }));
    } else {
      const idFields = [
        "religionId",
        "casteId",
        "subCasteId",
        "motherTongueId",
        "maritalStatusId",
        "educationLevelId",
        "occupationId",
        "heightId",
        "weightId",
        "cityId",
        "genderId",
        "bodyTypeId",
        "complexionId",
        "countryId",
        "stateId",
        "incomeId",
        "dietId",
        "smokingId",
        "profileTypeId",
        "manglikStatusId",
        "familyTypeId",
        "familyStatusId",
        "familyValueId",
        "qualificationId",
        "fieldOfStudyId",
        "employedId",
        "disabilityStatusId",
        "bloodGroupId",
        "drinkingId",
      ];
      const finalValue =
        idFields.includes(field) && value !== ""
          ? typeof value === "string"
            ? parseInt(value, 10)
            : value
          : value;

      setFormData((prev) => ({ ...prev, [field]: finalValue }));
    }
  };

  const handlePartnerPreferenceChange = (field, value) => {
    const numericFields = [
      "minAge",
      "maxAge",
      "minHeight",
      "maxHeight",
      "minWeight",
      "maxWeight",
      "religionId",
      "casteId",
      "cityId",
      "educationLevelId",
      "occupationId",
      "maritalStatusId",
      "smokingId",
      "drinkingId",
      "dietId",
    ];
    setPartnerPreference((prev) => {
      const updated = {
        ...prev,
        [field]: numericFields.includes(field)
          ? value === "" || value === null
            ? null
            : Number(value)
          : value,
      };

      if (field === "religionId") {
        updated.casteId = null;
      }

      return updated;
    });
  };

  const handleProfilePhotoUpload = (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    if (!file.type.startsWith("image/")) {
      error("Please select an image file");
      return;
    }

    if (file.size > 5 * 1024 * 1024) {
      error("File size should be less than 5MB");
      return;
    }

    const previewUrl = URL.createObjectURL(file);
    objectUrlsToRevoke.current.add(previewUrl);

    setFormData((prev) => ({
      ...prev,
      profilePhoto: file,
      profilePhotoUrl: previewUrl,
    }));
  };

  const removeProfilePhoto = () => {
    setFormData((prev) => ({
      ...prev,
      profilePhoto: null,
      profilePhotoUrl: "",
    }));
  };

  const handleMakePrimary = async (photoId, photoUrl) => {
    try {
      const res = await photoAPI.setPrimary(photoId);
      const newUrl = res?.photoUrl || photoUrl;
      const cacheBustedUrl = `${newUrl}?v=${Date.now()}`;

      setFormData((prev) => ({
        ...prev,
        profilePhoto: null,
        profilePhotoUrl: cacheBustedUrl,
      }));

      setGalleryPhotos((prev) =>
        prev.map((p) => ({
          ...p,
          primaryPhoto: p.id === photoId,
          isPrimary: p.id === photoId,
        }))
      );

      if (savedProfileData) {
        savedProfileData.imageUrl = cacheBustedUrl;
      }

      success("Primary photo updated successfully");
    } catch (err) {
      console.error("Failed to set primary photo:", err);
      error("Failed to set primary photo");
    }
  };

  const handleGalleryUpload = async (e) => {
    const files = Array.from(e.target.files || []);
    if (!files.length) return;

    const currentCount = galleryPhotos.length;
    if (currentCount + files.length > 8) {
      error("Maximum 8 photos allowed. Please delete a photo first.");
      return;
    }

    try {
      const photoFormData = new FormData();
      files.forEach((file) => {
        photoFormData.append("files", file);
      });

      const uploadRes = await photoAPI.uploadMultiple(photoFormData);
      const rawList = Array.isArray(uploadRes) ? uploadRes : uploadRes?.data || [];

      if (rawList.length > 0) {
        const newMapped = rawList.map((photo) => ({
          id: photo.id,
          preview: photo.photoUrl,
          photoUrl: photo.photoUrl,
          primaryPhoto: Boolean(photo.primaryPhoto || photo.isPrimary),
          isPrimary: Boolean(photo.primaryPhoto || photo.isPrimary),
          photoType: photo.photoType,
          uploaded: true,
        }));

        setGalleryPhotos((prev) => {
          const existingIds = new Set(prev.map((p) => p.id));
          const additions = newMapped.filter((p) => !existingIds.has(p.id));
          return [...prev, ...additions];
        });

        success("Photos uploaded to gallery successfully!");
      }
    } catch (err) {
      console.error("Failed to upload gallery photos:", err);
      error("Failed to upload gallery photos");
    }
  };

  const removeGalleryPhoto = async (index) => {
    try {
      const photo = galleryPhotos[index];
      if (photo.preview && objectUrlsToRevoke.current.has(photo.preview)) {
        URL.revokeObjectURL(photo.preview);
        objectUrlsToRevoke.current.delete(photo.preview);
      }

      if (photo.id) {
        await photoAPI.deletePhoto(photo.id);
      }

      setGalleryPhotos((prev) => prev.filter((_, i) => i !== index));
      success("Photo deleted successfully");
    } catch (err) {
      console.error("Delete photo error:", err);
      error("Failed to delete photo");
    }
  };

  const ensureSectionOpen = (sectionId) => {
    setExpandedSections((prev) => ({
      ...prev,
      [sectionId]: true,
    }));
  };

  const validateProfileForm = () => {
    if (!formData.firstName || formData.firstName.trim() === "") {
      ensureSectionOpen("personal");
      error("First name is required");
      return false;
    }

    if (!formData.lastName || formData.lastName.trim() === "") {
      ensureSectionOpen("personal");
      error("Last name is required");
      return false;
    }

    if (!formData.genderId) {
      ensureSectionOpen("personal");
      error("Gender is required");
      return false;
    }

    if (!formData.dateOfBirth) {
      ensureSectionOpen("personal");
      error("Date of birth is required");
      return false;
    }

    if (formData.email && !formData.email.includes("@")) {
      ensureSectionOpen("personal");
      error("Please enter a valid email address");
      return false;
    }

    if (
      partnerPreference.minAge &&
      (Number(partnerPreference.minAge) < 18 || Number(partnerPreference.minAge) > 100)
    ) {
      ensureSectionOpen("partner");
      error("Minimum age must be between 18 and 100");
      return false;
    }

    if (
      partnerPreference.maxAge &&
      (Number(partnerPreference.maxAge) < 18 || Number(partnerPreference.maxAge) > 100)
    ) {
      ensureSectionOpen("partner");
      error("Maximum age must be between 18 and 100");
      return false;
    }

    if (
      partnerPreference.minAge &&
      partnerPreference.maxAge &&
      Number(partnerPreference.minAge) > Number(partnerPreference.maxAge)
    ) {
      ensureSectionOpen("partner");
      error("Minimum age cannot be greater than maximum age");
      return false;
    }

    return true;
  };

  const handleProfileUpdate = async () => {
    if (profileSaving) return;
    if (!validateProfileForm()) return;

    setProfileSaving(true);
    try {
      const nameParts = (formData.fullName || "").trim().split(" ");
      const firstNameFromFull = nameParts[0] || formData.firstName;
      const lastNameFromFull = nameParts.slice(1).join(" ") || formData.lastName;

      let currentPhotoUrl = formData.profilePhotoUrl;

      // 1. Upload Profile Photo if user selected a new image file
      if (formData.profilePhoto && formData.profilePhoto instanceof File) {
        try {
          const uploadRes = await photoAPI.upload(formData.profilePhoto, "PROFILE");
          if (uploadRes && uploadRes.photoUrl) {
            const rawUrl = uploadRes.photoUrl;
            const cacheBustedUrl = `${rawUrl}?v=${Date.now()}`;
            currentPhotoUrl = rawUrl;

            setFormData((prev) => ({
              ...prev,
              profilePhoto: null,
              profilePhotoUrl: cacheBustedUrl,
            }));

            if (savedProfileData) {
              savedProfileData.imageUrl = cacheBustedUrl;
            }

            setGalleryPhotos((prev) => [
              {
                id: uploadRes.id,
                preview: cacheBustedUrl,
                photoUrl: cacheBustedUrl,
                uploaded: true,
                primaryPhoto: true,
                isPrimary: true,
                photoType: "PROFILE",
              },
              ...prev.map((p) => ({ ...p, primaryPhoto: false, isPrimary: false })),
            ]);
          }
        } catch (err) {
          console.error("Failed to upload profile photo:", err);
          error("Failed to upload profile photo");
          setProfileSaving(false);
          return;
        }
      }

      const dataToSave = {
        firstName: firstNameFromFull,
        middleName: formData.middleName,
        lastName: lastNameFromFull,
        email: formData.email,
        phone: formData.phone,
        genderId: formData.genderId,
        dateOfBirth: formData.dateOfBirth,
        aboutMe: formData.aboutMe || formData.about,
        about: formData.aboutMe || formData.about,
        imageUrl: currentPhotoUrl,
        religionId: formData.religionId,
        casteId: formData.casteId,
        subCasteId: formData.subCasteId,
        motherTongueId: formData.motherTongueId,
        maritalStatusId: formData.maritalStatusId,
        heightId: formData.heightId,
        weightId: formData.weightId,
        complexionId: formData.complexionId,
        bodyTypeId: formData.bodyTypeId,
        educationLevelId: formData.educationLevelId,
        occupationId: formData.occupationId,
        incomeId: formData.incomeId,
        companyName: formData.companyName,
        profileTypeId: formData.profileTypeId,
        manglikStatusId: formData.manglikStatusId,
        familyTypeId: formData.familyTypeId,
        familyStatusId: formData.familyStatusId,
        familyValueId: formData.familyValueId,
        qualificationId: formData.qualificationId,
        fieldOfStudyId: formData.fieldOfStudyId,
        employedId: formData.employedId,
        disabilityStatusId: formData.disabilityStatusId,
        bloodGroupId: formData.bloodGroupId,
        countryId: formData.countryId,
        stateId: formData.stateId,
        cityId: formData.cityId,
        address: formData.address,
        dietId: formData.dietId,
        smokingId: formData.smokingId,
        drinkingId: formData.drinkingId,
        fatherName: formData.fatherName,
        fatherOccupation: formData.fatherOccupation,
        motherName: formData.motherName,
        motherOccupation: formData.motherOccupation,
        siblingsCount: formData.siblingsCount,
      };

      // 2. Handle Gallery Uploads if new files are present
      const newPhotos = galleryPhotos.filter((photo) => photo.file && !photo.uploaded);
      if (newPhotos.length > 0) {
        const photoFormData = new FormData();
        newPhotos.forEach((photo) => {
          photoFormData.append("files", photo.file);
        });
        const uploadRes = await photoAPI.uploadMultiple(photoFormData);
        if (Array.isArray(uploadRes)) {
          setGalleryPhotos((prev) =>
            prev.map((p) => {
              if (p.file && !p.uploaded) {
                const match = uploadRes.find((u) => u.photoUrl?.includes(p.file.name)) || uploadRes[0];
                if (p.preview && objectUrlsToRevoke.current.has(p.preview)) {
                  URL.revokeObjectURL(p.preview);
                  objectUrlsToRevoke.current.delete(p.preview);
                }
                return {
                  id: match?.id || p.id,
                  preview: match?.photoUrl || p.preview,
                  photoUrl: match?.photoUrl || p.preview,
                  uploaded: true,
                  primaryPhoto: match?.primaryPhoto ?? false,
                  isPrimary: match?.primaryPhoto ?? false,
                  photoType: match?.photoType || "OTHER",
                };
              }
              return p;
            })
          );
        }
      }

      // 3. Partner Preference data
      const userId = savedProfileData?.userId || savedProfileData?.id;
      const partnerData = {
        userId: userId,
        minAge: partnerPreference.minAge,
        maxAge: partnerPreference.maxAge,
        minWeight: partnerPreference.minWeight,
        maxWeight: partnerPreference.maxWeight,
        minHeight: partnerPreference.minHeight,
        maxHeight: partnerPreference.maxHeight,
        religionId: Number(partnerPreference.religionId) || null,
        casteId: Number(partnerPreference.casteId) || null,
        cityId: Number(partnerPreference.cityId) || null,
        educationLevelId: Number(partnerPreference.educationLevelId) || null,
        occupationId: Number(partnerPreference.occupationId) || null,
        maritalStatusId: Number(partnerPreference.maritalStatusId) || null,
        dietId: Number(partnerPreference.dietId) || null,
        smokingId: Number(partnerPreference.smokingId) || null,
        drinkingId: Number(partnerPreference.drinkingId) || null,
        otherExpectations: partnerPreference.otherExpectations,
        isActive: true,
      };

      const savePrefPromise = partnerPreferenceId
        ? partnerPreferenceAPI.update(userId, partnerData)
        : partnerPreferenceAPI.save(partnerData);

      // Execute profile save and partner preference save concurrently!
      const [profileResult, prefResult] = await Promise.all([
        saveProfileData(dataToSave),
        savePrefPromise.catch((e) => {
          console.error("Partner preference save failed:", e);
          return null;
        }),
      ]);

      if (prefResult?.id) {
        setPartnerPreferenceId(prefResult.id);
      }

      if (profileResult) {
        success("Profile updated successfully!");
      } else {
        error("Failed to update profile. Please try again.");
      }

      window.scrollTo({ top: 0, behavior: "smooth" });
    } catch (err) {
      console.error("Profile update error:", err);
      error("Failed to update profile. Please try again.");
    } finally {
      setProfileSaving(false);
    }
  };

  const handlePasswordUpdate = async () => {
    if (!passwordData.currentPassword) {
      error("Current password is required");
      return;
    }

    if (!passwordData.newPassword) {
      error("New password is required");
      return;
    }

    if (!passwordData.confirmPassword) {
      error("Confirm password is required");
      return;
    }

    if (passwordData.newPassword !== passwordData.confirmPassword) {
      error("Passwords do not match");
      return;
    }

    try {
      setPasswordLoading(true);
      await authAPI.changePassword(passwordData);
      success("Password changed successfully");
      setPasswordData({
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
      });
    } catch (err) {
      error(err.message || "Failed to change password");
    } finally {
      setPasswordLoading(false);
    }
  };

  const handleNotificationToggle = (field, checked) => {
    setNotificationSettings((prev) => ({
      ...prev,
      [field]: checked,
    }));
  };

  const handleNotificationSave = async () => {
    try {
      setNotificationLoading(true);
      await notificationPreferenceAPI.updatePreferences(notificationSettings);
      success("Notification preferences updated successfully");
    } catch (err) {
      console.error("Failed notification update:", err);
      error(err.message || "Failed to update notification preferences");
    } finally {
      setNotificationLoading(false);
    }
  };

  const handleUnblock = async (blockedUserId) => {
    const confirmed = window.confirm("Are you sure you want to unblock this user?");
    if (!confirmed) return;

    try {
      const blockerId = savedProfileData?.userId || savedProfileData?.id;
      await blockAPI.unblockUser(blockerId, blockedUserId);
      setBlockedUsers((prev) => prev.filter((user) => user.blockedUserId !== blockedUserId));
      success("User unblocked successfully");
    } catch (err) {
      console.error("Unblock error:", err);
      error("Failed to unblock user");
    }
  };

  const renderField = (field) => {
    const { label, placeholder, type = "text", key, options, readOnly = false } = field;

    if (type === "select") {
      let fieldOptions = [];

      if (key === "genderId") fieldOptions = masterOptions.genders || [];
      else if (key === "religionId") fieldOptions = masterOptions.religions || [];
      else if (key === "cityId") fieldOptions = masterOptions.userStateCities || [];
      else if (key === "educationLevelId") fieldOptions = masterOptions.educationLevels || [];
      else if (key === "occupationId") fieldOptions = masterOptions.occupations || [];
      else if (key === "maritalStatusId") fieldOptions = masterOptions.maritalStatuses || [];
      else if (key === "casteId") fieldOptions = masterOptions.castes || [];
      else if (key === "subCasteId") fieldOptions = masterOptions.subCastes || [];
      else if (key === "motherTongueId") fieldOptions = masterOptions.motherTongues || [];
      else if (key === "heightId") fieldOptions = masterOptions.heights || [];
      else if (key === "weightId") fieldOptions = masterOptions.weights || [];
      else if (key === "complexionId") fieldOptions = masterOptions.complexions || [];
      else if (key === "bodyTypeId") fieldOptions = masterOptions.bodyTypes || [];
      else if (key === "countryId") fieldOptions = masterOptions.countries || [];
      else if (key === "stateId") fieldOptions = masterOptions.states || [];
      else if (key === "incomeId") fieldOptions = masterOptions.incomes || [];
      else if (key === "dietId") fieldOptions = masterOptions.diets || [];
      else if (key === "smokingId") fieldOptions = masterOptions.smokingOptions || [];
      else if (key === "drinkingId") fieldOptions = masterOptions.drinkingOptions || [];
      else if (key === "profileTypeId") fieldOptions = masterOptions.profileTypes || [];
      else if (key === "manglikStatusId") fieldOptions = masterOptions.manglikStatuses || [];
      else if (key === "familyTypeId") fieldOptions = masterOptions.familyTypes || [];
      else if (key === "familyStatusId") fieldOptions = masterOptions.familyStatuses || [];
      else if (key === "familyValueId") fieldOptions = masterOptions.familyValues || [];
      else if (key === "qualificationId") fieldOptions = masterOptions.qualifications || [];
      else if (key === "fieldOfStudyId") fieldOptions = masterOptions.fieldsOfStudy || [];
      else if (key === "employedId") fieldOptions = masterOptions.employmentStatuses || [];
      else if (key === "disabilityStatusId") fieldOptions = masterOptions.disabilityStatuses || [];
      else if (key === "bloodGroupId") fieldOptions = masterOptions.bloodGroups || [];
      else fieldOptions = options || [];

      const sortedOptions = sortMasterOptions(fieldOptions);
      const currentValue = formData[key] ?? "";
      const isHighPriorityKey = [
        "profileTypeId",
        "genderId",
        "religionId",
        "maritalStatusId",
        "educationLevelId",
        "occupationId",
      ].includes(key);
      const isCurrentlyLoading = isHighPriorityKey ? masterLoading.highPriority : masterLoading.progressive;
      const isCityDisabled = key === "cityId" && !formData.stateId;

      return (
        <div key={key}>
          <label className="text-xs font-medium text-foreground mb-1 block">{label}</label>
          <SearchableSelect
            value={currentValue}
            options={sortedOptions}
            placeholder={
              isCityDisabled ? "Select state first" : `Select ${label.toLowerCase()}`
            }
            disabled={isCityDisabled}
            loading={isCurrentlyLoading}
            onChange={(value) => {
              handleInputChange(key, value);

              if (key === "religionId") {
                handleInputChange("casteId", "");
                handleInputChange("subCasteId", "");
              }
              if (key === "casteId") {
                handleInputChange("subCasteId", "");
              }
              if (key === "stateId") {
                handleInputChange("cityId", "");
              }
            }}
          />
        </div>
      );
    }

    return (
      <div key={key}>
        <label className="text-xs font-medium text-foreground mb-1 block">{label}</label>
        <input
          type={type}
          value={formData[key] ?? ""}
          onChange={(e) => handleInputChange(key, e.target.value)}
          placeholder={placeholder}
          readOnly={readOnly}
          className={`w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary ${
            readOnly ? "bg-muted cursor-not-allowed" : ""
          }`}
        />
      </div>
    );
  };

  return (
    <div className="min-h-screen bg-muted/30">
      {/* Header */}
      <div
        className="py-8 text-center"
        style={{
          background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))",
        }}
      >
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Settings</h1>
        <p className="text-primary-foreground/70 text-sm">Manage your profile and preferences</p>
      </div>

      <div className="container mx-auto px-4 py-8 max-w-3xl">
        {/* Navigation Tabs */}
        <div className="flex gap-2 mb-6 overflow-x-auto pb-2">
          {tabs.map((t) => (
            <button
              key={t.id}
              onClick={() => setActiveTab(t.id)}
              className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-colors whitespace-nowrap ${
                activeTab === t.id
                  ? "bg-primary text-primary-foreground"
                  : "bg-card border border-border text-muted-foreground hover:text-foreground"
              }`}
            >
              {t.icon} {t.label}
            </button>
          ))}
        </div>

        {/* Tab Content Container */}
        <motion.div key={activeTab} initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="bg-card rounded-xl border border-border p-6">
          {/* PROFILE TAB */}
          {activeTab === "profile" && (
            <div className="space-y-4">
              <h2 className="text-lg font-display font-bold text-foreground mb-4">
                Update Profile
              </h2>

              {/* 1. Profile Photo & Profile Type */}
              <SettingsSection
                id="profilePhoto"
                title="Profile Photo & Profile Type"
                icon={Camera}
                isOpen={openSections.profilePhoto}
                onToggle={toggleSection}
              >
                <div className="space-y-4">
                  {/* Photo Upload Box */}
                  <div className="border border-dashed border-border rounded-lg p-4 bg-muted/30">
                    <label className="text-sm font-medium text-foreground mb-3 block">
                      Profile Photo
                    </label>
                    {formData.profilePhotoUrl ? (
                      <div className="relative w-20 h-20 rounded-full overflow-hidden mb-3 ring-2 ring-primary/20">
                        <img
                          src={formData.profilePhotoUrl}
                          alt="Profile"
                          className="w-full h-full object-cover"
                        />
                        <button
                          type="button"
                          onClick={removeProfilePhoto}
                          className="absolute inset-0 bg-black/50 flex items-center justify-center opacity-0 hover:opacity-100 transition-opacity"
                        >
                          <X className="h-4 w-4 text-white" />
                        </button>
                      </div>
                    ) : (
                      <div className="w-20 h-20 rounded-full bg-muted flex items-center justify-center mb-3 text-muted-foreground font-semibold text-lg border border-border">
                        {formData.firstName
                          ? formData.firstName.charAt(0).toUpperCase()
                          : "U"}
                      </div>
                    )}
                    <label className="flex items-center gap-2 px-4 py-2 bg-primary hover:bg-primary/90 text-primary-foreground rounded-lg text-sm font-medium cursor-pointer transition-colors inline-block">
                      <Upload className="h-4 w-4" />
                      {formData.profilePhotoUrl
                        ? "Change Photo"
                        : "Upload Photo"}
                      <input
                        type="file"
                        accept="image/*"
                        onChange={handleProfilePhotoUpload}
                        className="hidden"
                      />
                    </label>
                  </div>

                  {/* Profile Type */}
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    {renderField({
                      label: "Profile Type",
                      key: "profileTypeId",
                      type: "select",
                    })}
                  </div>
                </div>
              </SettingsSection>

              {/* 2. Personal Details */}
              <SettingsSection
                id="personalDetails"
                title="Personal Details"
                icon={User}
                isOpen={openSections.personalDetails}
                onToggle={toggleSection}
              >
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({
                    label: "Full Name",
                    placeholder: "Your full name",
                    key: "fullName",
                  })}
                  {renderField({
                    label: "Gender",
                    key: "genderId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Date of Birth",
                    type: "date",
                    key: "dateOfBirth",
                  })}
                  {renderField({
                    label: "Age",
                    type: "number",
                    key: "age",
                    placeholder: "Auto-calculated",
                    readOnly: true,
                  })}
                  {renderField({
                    label: "Marital Status",
                    key: "maritalStatusId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Religion",
                    key: "religionId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Caste",
                    key: "casteId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Sub-caste",
                    key: "subCasteId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Mother Tongue",
                    key: "motherTongueId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Manglik Status",
                    key: "manglikStatusId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Blood Group",
                    key: "bloodGroupId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Disability Status",
                    key: "disabilityStatusId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Email",
                    type: "email",
                    placeholder: "your@email.com",
                    key: "email",
                  })}
                  {renderField({
                    label: "Phone",
                    type: "tel",
                    placeholder: "+91 98765 43210",
                    key: "phone",
                  })}
                </div>
              </SettingsSection>

              {/* 3. Physical Details */}
              <SettingsSection
                id="physicalDetails"
                title="Physical Details"
                icon={Ruler}
                isOpen={openSections.physicalDetails}
                onToggle={toggleSection}
              >
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({
                    label: "Height",
                    key: "heightId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Weight",
                    key: "weightId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Complexion",
                    key: "complexionId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Body Type",
                    key: "bodyTypeId",
                    type: "select",
                  })}
                </div>
              </SettingsSection>

              {/* 4. Education & Career */}
              <SettingsSection
                id="educationCareer"
                title="Education & Career"
                icon={GraduationCap}
                isOpen={openSections.educationCareer}
                onToggle={toggleSection}
              >
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({
                    label: "Highest Education",
                    key: "educationLevelId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Qualification",
                    key: "qualificationId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Field Of Study",
                    key: "fieldOfStudyId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Profession / Occupation",
                    key: "occupationId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Employment Status",
                    key: "employedId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Company Name",
                    placeholder: "Your company",
                    key: "companyName",
                  })}
                  {renderField({
                    label: "Annual Income",
                    key: "incomeId",
                    type: "select",
                  })}
                </div>
              </SettingsSection>

              {/* 5. Location Details */}
              <SettingsSection
                id="locationDetails"
                title="Location Details"
                icon={MapPin}
                isOpen={openSections.locationDetails}
                onToggle={toggleSection}
              >
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({
                    label: "Country",
                    key: "countryId",
                    type: "select",
                  })}
                  {renderField({
                    label: "State",
                    key: "stateId",
                    type: "select",
                  })}
                  {renderField({
                    label: "City",
                    key: "cityId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Address",
                    key: "address",
                    placeholder: "Enter address",
                  })}
                </div>
              </SettingsSection>

              {/* 6. Lifestyle */}
              <SettingsSection
                id="lifestyle"
                title="Lifestyle"
                icon={Heart}
                isOpen={openSections.lifestyle}
                onToggle={toggleSection}
              >
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({
                    label: "Diet",
                    key: "dietId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Smoking",
                    key: "smokingId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Drinking",
                    key: "drinkingId",
                    type: "select",
                  })}
                </div>
              </SettingsSection>

              {/* 7. Family Details */}
              <SettingsSection
                id="familyDetails"
                title="Family Details"
                icon={Users}
                isOpen={openSections.familyDetails}
                onToggle={toggleSection}
              >
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {renderField({
                    label: "Father's Name",
                    placeholder: "Your father's name",
                    key: "fatherName",
                  })}
                  {renderField({
                    label: "Father's Occupation",
                    placeholder: "Your father's occupation",
                    key: "fatherOccupation",
                  })}
                  {renderField({
                    label: "Mother's Name",
                    placeholder: "Your mother's name",
                    key: "motherName",
                  })}
                  {renderField({
                    label: "Mother's Occupation",
                    placeholder: "Your mother's occupation",
                    key: "motherOccupation",
                  })}
                  {renderField({
                    label: "Number of Siblings",
                    key: "siblingsCount",
                    placeholder: "Enter siblings count",
                  })}
                  {renderField({
                    label: "Family Type",
                    key: "familyTypeId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Family Status",
                    key: "familyStatusId",
                    type: "select",
                  })}
                  {renderField({
                    label: "Family Value",
                    key: "familyValueId",
                    type: "select",
                  })}
                </div>
              </SettingsSection>

              {/* 8. About Me */}
              <SettingsSection
                id="aboutMe"
                title="About Me"
                icon={FileText}
                isOpen={openSections.aboutMe}
                onToggle={toggleSection}
              >
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">
                    About Me
                  </label>
                  <textarea
                    rows={4}
                    value={formData.aboutMe}
                    onChange={(e) =>
                      handleInputChange("aboutMe", e.target.value)
                    }
                    placeholder="Tell us about yourself, your interests, personality and what you are looking for..."
                    className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary resize-none"
                  />
                </div>
              </SettingsSection>

              {/* 9. Partner Preferences */}
              <SettingsSection
                id="partnerPreferences"
                title="Partner Preferences"
                icon={HeartHandshake}
                isOpen={openSections.partnerPreferences}
                onToggle={toggleSection}
              >
                <div className="space-y-4">
                  {/* Age */}
                  <div>
                    <h4 className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-2">
                      Age Preferences
                    </h4>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Age Min
                        </label>
                        <input
                          type="number"
                          className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary"
                          value={partnerPreference.minAge ?? ""}
                          onChange={(e) =>
                            handlePartnerPreferenceChange(
                              "minAge",
                              e.target.value
                            )
                          }
                          placeholder="Enter minimum age"
                        />
                      </div>
                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Age Max
                        </label>
                        <input
                          type="number"
                          className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary"
                          value={partnerPreference.maxAge ?? ""}
                          onChange={(e) =>
                            handlePartnerPreferenceChange(
                              "maxAge",
                              e.target.value
                            )
                          }
                          placeholder="Enter maximum age"
                        />
                      </div>
                    </div>
                  </div>

                  {/* Height */}
                  <div>
                    <h4 className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-2">
                      Height Preferences
                    </h4>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Height Min
                        </label>
                        <SearchableSelect
                          value={partnerPreference.minHeight ?? ""}
                          options={masterOptions.heights}
                          placeholder="Select minimum height"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("minHeight", val)
                          }
                        />
                      </div>
                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Height Max
                        </label>
                        <SearchableSelect
                          value={partnerPreference.maxHeight ?? ""}
                          options={masterOptions.heights}
                          placeholder="Select maximum height"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("maxHeight", val)
                          }
                        />
                      </div>
                    </div>
                  </div>

                  {/* Weight */}
                  <div>
                    <h4 className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-2">
                      Weight Preferences
                    </h4>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Weight Min
                        </label>
                        <SearchableSelect
                          value={partnerPreference.minWeight ?? ""}
                          options={masterOptions.weights}
                          placeholder="Select minimum weight"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("minWeight", val)
                          }
                        />
                      </div>
                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Weight Max
                        </label>
                        <SearchableSelect
                          value={partnerPreference.maxWeight ?? ""}
                          options={masterOptions.weights}
                          placeholder="Select maximum weight"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("maxWeight", val)
                          }
                        />
                      </div>
                    </div>
                  </div>

                  {/* Basic Preferences */}
                  <div>
                    <h4 className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-2">
                      Basic Preferences
                    </h4>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Religion
                        </label>
                        <SearchableSelect
                          value={partnerPreference.religionId ?? ""}
                          options={masterOptions.religions}
                          placeholder="Select religion"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("religionId", val)
                          }
                        />
                      </div>

                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Caste
                        </label>
                        <SearchableSelect
                          value={partnerPreference.casteId ?? ""}
                          options={masterOptions.partnerCastes}
                          placeholder="Select caste"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("casteId", val)
                          }
                        />
                      </div>

                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred City
                        </label>
                        <SearchableSelect
                          value={partnerPreference.cityId ?? ""}
                          options={sortMasterOptions(
                            masterOptions.allCities && masterOptions.allCities.length > 0
                              ? masterOptions.allCities
                              : masterOptions.cities
                          )}
                          placeholder="Select city"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("cityId", val)
                          }
                        />
                      </div>

                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Education
                        </label>
                        <SearchableSelect
                          value={partnerPreference.educationLevelId ?? ""}
                          options={masterOptions.educationLevels}
                          placeholder="Select education"
                          onChange={(val) =>
                            handlePartnerPreferenceChange(
                              "educationLevelId",
                              val
                            )
                          }
                        />
                      </div>

                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Occupation
                        </label>
                        <SearchableSelect
                          value={partnerPreference.occupationId ?? ""}
                          options={masterOptions.occupations}
                          placeholder="Select occupation"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("occupationId", val)
                          }
                        />
                      </div>

                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Preferred Marital Status
                        </label>
                        <SearchableSelect
                          value={partnerPreference.maritalStatusId ?? ""}
                          options={masterOptions.maritalStatuses}
                          placeholder="Select marital status"
                          onChange={(val) =>
                            handlePartnerPreferenceChange(
                              "maritalStatusId",
                              val
                            )
                          }
                        />
                      </div>
                    </div>
                  </div>

                  {/* Lifestyle Preferences */}
                  <div>
                    <h4 className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-2">
                      Lifestyle Preferences
                    </h4>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Smoking Preference
                        </label>
                        <SearchableSelect
                          value={partnerPreference.smokingId ?? ""}
                          options={masterOptions.smokingOptions}
                          placeholder="Select smoking preference"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("smokingId", val)
                          }
                        />
                      </div>

                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Drinking Preference
                        </label>
                        <SearchableSelect
                          value={partnerPreference.drinkingId ?? ""}
                          options={masterOptions.drinkingOptions}
                          placeholder="Select drinking preference"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("drinkingId", val)
                          }
                        />
                      </div>

                      <div>
                        <label className="text-xs font-medium text-foreground mb-1 block">
                          Diet Preference
                        </label>
                        <SearchableSelect
                          value={partnerPreference.dietId ?? ""}
                          options={masterOptions.diets}
                          placeholder="Select diet preference"
                          onChange={(val) =>
                            handlePartnerPreferenceChange("dietId", val)
                          }
                        />
                      </div>
                    </div>
                  </div>

                  {/* Additional Expectations */}
                  <div>
                    <label className="text-xs font-medium text-foreground mb-1 block">
                      Other Expectations
                    </label>
                    <textarea
                      rows={4}
                      value={partnerPreference.otherExpectations ?? ""}
                      onChange={(e) =>
                        handlePartnerPreferenceChange(
                          "otherExpectations",
                          e.target.value
                        )
                      }
                      placeholder="Any other expectations..."
                      className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary resize-none"
                    />
                  </div>
                </div>
              </SettingsSection>

              {/* 10. Photo Gallery */}
              <SettingsSection
                id="photoGallery"
                title="Photo Gallery"
                icon={ImageIcon}
                isOpen={openSections.photoGallery}
                onToggle={toggleSection}
              >
                <div>
                  <div className="flex justify-between items-center mb-3">
                    <span className="text-xs text-muted-foreground">
                      {galleryPhotos.length}/8 photos uploaded
                    </span>
                    <span className="text-destructive text-xs">
                      Minimum 4 photos required
                    </span>
                  </div>

                  <label className="bg-primary hover:bg-primary/90 text-primary-foreground px-4 py-2 rounded-lg text-xs font-medium cursor-pointer inline-flex items-center gap-2 transition-colors">
                    <Upload size={14} />
                    Add Gallery Photos
                    <input
                      type="file"
                      multiple
                      accept="image/*"
                      className="hidden"
                      onChange={handleGalleryUpload}
                    />
                  </label>

                  <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mt-4">
                    {galleryPhotos.map((photo, index) => (
                      <div
                        key={photo.id || index}
                        className="relative group rounded-lg overflow-hidden border border-border"
                      >
                        <img
                          src={photo.preview || photo.photoUrl}
                          alt={`Gallery photo ${index + 1}`}
                          className="h-32 w-full object-cover"
                        />

                        {photo.primaryPhoto || photo.isPrimary ? (
                          <span className="absolute bottom-1 left-1 bg-primary text-primary-foreground text-[10px] font-semibold px-2 py-0.5 rounded shadow">
                            Primary Photo
                          </span>
                        ) : photo.id ? (
                          <button
                            type="button"
                            onClick={() =>
                              handleMakePrimary(photo.id, photo.photoUrl)
                            }
                            className="absolute bottom-1 left-1 bg-background/90 hover:bg-background text-foreground text-[10px] font-medium px-2 py-0.5 rounded border border-border transition-colors opacity-90 group-hover:opacity-100 shadow-sm"
                          >
                            Set as Primary
                          </button>
                        ) : null}

                        <button
                          type="button"
                          onClick={() => removeGalleryPhoto(index)}
                          className="absolute top-1 right-1 bg-destructive text-destructive-foreground rounded-full p-1 opacity-90 hover:opacity-100 transition-opacity"
                          title="Delete photo"
                        >
                          <X size={14} />
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
              </SettingsSection>

              {/* Save Changes Button */}
              <div className="pt-2">
                <button
                  type="button"
                  onClick={handleProfileUpdate}
                  disabled={profileSaving}
                  className="flex items-center gap-2 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold px-6 py-2.5 rounded-lg text-sm transition-colors disabled:opacity-50 disabled:cursor-not-allowed shadow-sm"
                >
                  {profileSaving ? (
                    <>
                      <Loader2 className="h-4 w-4 animate-spin" /> Saving...
                    </>
                  ) : (
                    <>
                      <Save className="h-4 w-4" /> Save Changes
                    </>
                  )}
                </button>
              </div>
            </div>
          )}

          {/* PASSWORD TAB */}
          {activeTab === "password" && (
            <div className="space-y-4 max-w-md">
              <h2 className="text-lg font-display font-bold text-foreground mb-4">Change Password</h2>
              {[
                { label: "Current Password", key: "currentPassword" },
                { label: "New Password", key: "newPassword" },
                { label: "Confirm New Password", key: "confirmPassword" },
              ].map((field) => (
                <div key={field.key}>
                  <label className="text-xs font-medium text-foreground mb-1 block">{field.label}</label>
                  <div className="relative">
                    <input
                      type={showPassword[field.key] ? "text" : "password"}
                      value={passwordData[field.key]}
                      onChange={(e) =>
                        setPasswordData({
                          ...passwordData,
                          [field.key]: e.target.value,
                        })
                      }
                      placeholder="••••••••"
                      className="w-full bg-background border border-border rounded-lg px-4 py-2.5 pr-11 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary"
                    />
                    <button
                      type="button"
                      onClick={() =>
                        setShowPassword((prev) => ({
                          ...prev,
                          [field.key]: !prev[field.key],
                        }))
                      }
                      className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                    >
                      {showPassword[field.key] ? <EyeOff size={18} /> : <Eye size={18} />}
                    </button>
                  </div>
                </div>
              ))}
              <button
                type="button"
                onClick={handlePasswordUpdate}
                disabled={passwordLoading}
                className="flex items-center gap-2 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold px-6 py-2.5 rounded-lg text-sm transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {passwordLoading ? (
                  <>
                    <Loader2 className="h-4 w-4 animate-spin" /> Updating Password...
                  </>
                ) : (
                  <>
                    <Save className="h-4 w-4" /> Update Password
                  </>
                )}
              </button>
            </div>
          )}

          {/* NOTIFICATIONS TAB */}
          {activeTab === "notifications" && (
            <div className="space-y-5">
              <h2 className="text-lg font-display font-bold text-foreground mb-4">Notification Settings</h2>

              {notificationLoading && !notificationsLoaded.current ? (
                <div className="py-8 text-center text-sm text-muted-foreground flex items-center justify-center gap-2">
                  <Loader2 className="h-4 w-4 animate-spin text-primary" /> Loading notification preferences...
                </div>
              ) : (
                <>
                  {[
                    {
                      key: "matchNotifications",
                      label: "New match notifications",
                      desc: "Get notified when someone matches your preferences",
                    },
                    {
                      key: "interestNotifications",
                      label: "Interest received",
                      desc: "Alerts when someone sends you an interest",
                    },
                    {
                      key: "messageNotifications",
                      label: "Notifications for new messages",
                      desc: "Alerts when someone sends you a direct message",
                    },
                    {
                      key: "profileViewNotifications",
                      label: "Profile views",
                      desc: "Know when someone views your profile",
                    },
                    {
                      key: "promotionalEmails",
                      label: "Promotional emails",
                      desc: "Offers, tips, and Gathbandhan updates",
                    },
                  ].map((n) => (
                    <div key={n.key} className="flex items-center justify-between py-2 border-b border-border/50 last:border-0">
                      <div>
                        <p className="text-sm font-medium text-foreground">{n.label}</p>
                        {n.desc && <p className="text-xs text-muted-foreground">{n.desc}</p>}
                      </div>
                      <label className="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          checked={!!notificationSettings[n.key]}
                          onChange={(e) => handleNotificationToggle(n.key, e.target.checked)}
                          className="sr-only peer"
                        />
                        <div className="w-9 h-5 bg-muted rounded-full peer peer-checked:bg-primary transition-colors after:content-[''] after:absolute after:top-0.5 after:left-0.5 after:bg-background after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:after:translate-x-4" />
                      </label>
                    </div>
                  ))}

                  <div className="pt-4 border-t border-border">
                    <button
                      type="button"
                      onClick={handleNotificationSave}
                      disabled={notificationLoading}
                      className="flex items-center gap-2 bg-primary hover:bg-primary/90 text-primary-foreground px-5 py-2 rounded-lg text-sm font-medium disabled:opacity-50"
                    >
                      {notificationLoading ? (
                        <>
                          <Loader2 className="h-4 w-4 animate-spin" /> Saving...
                        </>
                      ) : (
                        <>
                          <Save className="h-4 w-4" /> Save Changes
                        </>
                      )}
                    </button>
                  </div>
                </>
              )}
            </div>
          )}

          {/* BLOCKED USERS TAB */}
          {activeTab === "blocked" && (
            <div className="space-y-5">
              <h2 className="text-lg font-display font-bold text-foreground">Blocked Users</h2>
              <p className="text-sm text-muted-foreground">Users you have blocked cannot message or view your profile.</p>

              {blockedLoading ? (
                <div className="py-8 text-center text-sm text-muted-foreground flex items-center justify-center gap-2">
                  <Loader2 className="h-4 w-4 animate-spin text-primary" /> Loading blocked users...
                </div>
              ) : blockedUsers.length === 0 ? (
                <div className="text-center py-10">
                  <Ban className="mx-auto h-12 w-12 text-muted-foreground" />
                  <p className="mt-4 text-muted-foreground">You haven't blocked anyone yet.</p>
                </div>
              ) : (
                <div className="space-y-4">
                  {blockedUsers.map((user) => (
                    <div key={user.blockedUserId} className="flex items-center justify-between border border-border rounded-xl p-4">
                      <div className="flex items-center gap-4">
                        <img
                          src={user.photoUrl || "/placeholder.svg"}
                          alt={user.fullName || "Blocked user"}
                          className="w-14 h-14 rounded-full object-cover border border-border"
                        />
                        <div>
                          <h3 className="font-semibold text-foreground">{user.fullName}</h3>
                          <p className="text-xs text-muted-foreground">
                            Blocked on{" "}
                            {user.blockedDate
                              ? new Date(user.blockedDate).toLocaleString("en-IN", {
                                  day: "2-digit",
                                  month: "2-digit",
                                  year: "numeric",
                                  hour: "2-digit",
                                  minute: "2-digit",
                                  hour12: true,
                                })
                              : "-"}
                          </p>
                        </div>
                      </div>
                      <button
                        type="button"
                        onClick={() => handleUnblock(user.blockedUserId)}
                        className="bg-destructive hover:bg-destructive/90 text-destructive-foreground px-4 py-2 rounded-lg text-xs font-semibold transition-colors"
                      >
                        Unblock
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}
        </motion.div>
      </div>
    </div>
  );
};

export default SettingsPage;