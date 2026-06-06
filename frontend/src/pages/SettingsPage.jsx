import { useState, useEffect } from "react";
import { User, Lock, Bell, Save, Upload, X } from "lucide-react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import { useToast } from "@/components/Toast";
import { useProfileData } from "@/hooks/useProfileData";

import {

 masterDataAPI,

 partnerPreferenceAPI

} from "@/services/api";

const tabs = [
  { id: "profile", label: "Profile", icon: <User className="h-4 w-4" /> },
  { id: "password", label: "Password", icon: <Lock className="h-4 w-4" /> },
  { id: "notifications", label: "Notifications", icon: <Bell className="h-4 w-4" /> },
];

const SettingsPage = () => {
  const [activeTab, setActiveTab] = useState("profile");
  const { success, error, info } = useToast();
  const { profileData: savedProfileData, saveProfileData } = useProfileData();
const [masterDataLoaded, setMasterDataLoaded] = useState(false)
  const [masterOptions, setMasterOptions] = useState({

    religions: [],

    genders: [],

    cities: [],
 allCities: [],
    educationLevels: [],

    occupations: [],

    heights: [],

    weights: [],

    maritalStatuses: [],

    castes: [],

    subCastes: [],

    complexions: [],

    bodyTypes: [],

    motherTongues: [],

    countries: [],

    states: [],

    incomes: [],

    diets: [],

    smokingOptions: [],

    drinkingOptions: [],

    // ==========================
    // NEW MASTER TABLES
    // ==========================
qualifications: [],
fieldOfStudies: [],
employedStatuses: [],
disabilityStatuses: [],
bloodGroups: [],
    profileTypes: [],

    manglikStatuses: [],

    familyTypes: [],

    familyStatuses: [],

    familyValues: []

  });

  // Profile form state - matching backend DTO structure
  const [formData, setFormData] = useState({

    // Basic fields from User entity

    firstName: "",

    lastName: "",

    fullName: "",

    email: "",

    phone: "",

    // Profile fields

    dateOfBirth: "",

    about: "",

    imageUrl: "",

    // ==========================
    // MASTER TABLE IDS
    // ==========================

    religionId: null,

    casteId: null,

    subCasteId: null,

    motherTongueId: null,

    maritalStatusId: null,

    profileTypeId: null,

    manglikStatusId: null,

    familyTypeId: null,

    familyStatusId: null,

    familyValueId: null,

    educationLevelId: null,

    occupationId: null,

    genderId: null,

    heightId: null,

    weightId: null,

    complexionId: null,

    bodyTypeId: null,

    countryId: null,

    stateId: null,

    cityId: null,

    incomeId: null,

    dietId: null,

    smokingId: null,

    drinkingId: null,
qualificationId: null,
fieldOfStudyId: null,
employedId: null,
disabilityStatusId: null,
bloodGroupId: null,
    // ==========================
    // FAMILY
    // ==========================

    fatherName: "",

    fatherOccupation: "",

    motherName: "",

    motherOccupation: "",

    siblingsCount: "",

    // ==========================
    // CAREER
    // ==========================

    companyName: "",

    // ==========================
    // LOCATION
    // ==========================

    address: "",

    // ==========================
    // PARTNER PREFERENCES
    // ==========================



    preferredAgeMin: "",

    preferredAgeMax: "",

    preferredReligionId: null,
preferredMinHeightId:null,

preferredMaxHeightId:null,
    preferredCasteId: null,

    preferredCityId: null,

    preferredEducationLevelId: null,

    preferredOccupationId: null,

    preferredMaritalStatusId: null,

    preferredSmokingId: null,

    preferredDrinkingId: null,

    preferredDietId: null,

    otherExpectations: "",
    // ==========================
    // OTHER
    // ==========================

    aboutMe: "",

    age: "",

    // UI ONLY

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
        setMasterDataLoaded(false)
      try {
        console.log('🔍 Loading master data from APIs...');
const responses = await Promise.allSettled([

 masterDataAPI.getIncomes(),
 masterDataAPI.getDiets(),
 masterDataAPI.getSmokingOptions(),
 masterDataAPI.getDrinkingOptions(),

 masterDataAPI.getProfileTypes(),
 masterDataAPI.getManglikStatuses(),
 masterDataAPI.getFamilyTypes(),
 masterDataAPI.getFamilyStatuses(),
 masterDataAPI.getFamilyValues(),

 masterDataAPI.getQualifications(),
 masterDataAPI.getFieldOfStudies(),
 masterDataAPI.getEmployedStatuses(),
 masterDataAPI.getDisabilityStatuses(),
 masterDataAPI.getBloodGroups(),

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
 masterDataAPI.getMotherTongues(),
 masterDataAPI.getCountries(),
 masterDataAPI.getStates()

]);

const getData = (index)=>
responses[index]?.status==="fulfilled"
? responses[index].value
:[];

const incomes=getData(0);
const diets=getData(1);
const smokingOptions=getData(2);
const drinkingOptions=getData(3);

const profileTypes=getData(4);
const manglikStatuses=getData(5);
const familyTypes=getData(6);
const familyStatuses=getData(7);
const familyValues=getData(8);

const qualifications=getData(9);
const fieldOfStudies=getData(10);
const employedStatuses=getData(11);
const disabilityStatuses=getData(12);
const bloodGroups=getData(13);

const religions=getData(14);
const genders=getData(15);
const cities=getData(16);
const educationLevels=getData(17);
const occupations=getData(18);
const maritalStatuses=getData(19);
const heights=getData(20);
const complexions=getData(21);
const bodyTypes=getData(22);
const weights=getData(23);
const motherTongues=getData(24);
const countries=getData(25);
const states=getData(26);

        
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
cities: Array.isArray(cities?.data)
 ? cities.data
 : Array.isArray(cities)
 ? cities
 : [],

allCities: Array.isArray(cities?.data)
 ? cities.data
 : Array.isArray(cities)
 ? cities
 : [],
          educationLevels: Array.isArray(educationLevels) ? educationLevels : [],
          occupations: Array.isArray(occupations) ? occupations : [],
          maritalStatuses: Array.isArray(maritalStatuses) ? maritalStatuses : [],
          heights: Array.isArray(heights) ? heights : [],
          weights: Array.isArray(weights) ? weights : [],
          complexions: Array.isArray(complexions) ? complexions : [],
          bodyTypes: Array.isArray(bodyTypes) ? bodyTypes : [],
          castes: [], // Will be loaded based on selected religion
motherTongues: Array.isArray(motherTongues) ? motherTongues : [],
countries: Array.isArray(countries) ? countries : [],
incomes: Array.isArray(incomes) ? incomes : [],
profileTypes:
Array.isArray(profileTypes)
? profileTypes
: [],
qualifications:
Array.isArray(qualifications)
? qualifications
: [],

fieldOfStudies:
Array.isArray(fieldOfStudies)
? fieldOfStudies
: [],

employedStatuses:
Array.isArray(employedStatuses)
? employedStatuses
: [],

disabilityStatuses:
Array.isArray(disabilityStatuses)
? disabilityStatuses
: [],

bloodGroups:
Array.isArray(bloodGroups)
? bloodGroups
: [],
manglikStatuses:
Array.isArray(manglikStatuses)
? manglikStatuses
: [],

familyTypes:
Array.isArray(familyTypes)
? familyTypes
: [],

familyStatuses:
Array.isArray(familyStatuses)
? familyStatuses
: [],

familyValues:
Array.isArray(familyValues)
? familyValues
: [],
smokingOptions: Array.isArray(smokingOptions?.data)
  ? smokingOptions.data
  : Array.isArray(smokingOptions)
  ? smokingOptions
  : [],

diets: Array.isArray(diets?.data)
  ? diets.data
  : Array.isArray(diets)
  ? diets
  : [],


drinkingOptions: Array.isArray(drinkingOptions?.data)
  ? drinkingOptions.data
  : Array.isArray(drinkingOptions)
  ? drinkingOptions
  : [],
states: Array.isArray(states) ? states : []
        };
        
       setMasterOptions(safeData);

       setMasterDataLoaded(true);
        
      } catch (error) {
        console.error('❌ Failed to load master data:', error);
        // Set empty arrays on error to prevent UI crashes
  setMasterOptions({

   religions: [],
   genders: [],
  cities: [],

  allCities: [],
   educationLevels: [],
   occupations: [],
   maritalStatuses: [],
   heights: [],
   weights: [],
   castes: [],
   subCastes: [],
   complexions: [],
   bodyTypes: [],
   motherTongues: [],
   countries: [],
   incomes: [],
   diets: [],
   smokingOptions: [],
   drinkingOptions: [],

   qualifications: [],
   fieldOfStudies: [],
   employedStatuses: [],
   disabilityStatuses: [],
   bloodGroups: [],

   profileTypes: [],
   manglikStatuses: [],
   familyTypes: [],
   familyStatuses: [],
   familyValues: [],
   states: []

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
          "🔍 Loading sub castes for caste:",
          formData.casteId
        );

        const subCastes =
          await masterDataAPI.getSubCastes(
            formData.casteId
          );

        setMasterOptions(prev => ({
          ...prev,
          subCastes: Array.isArray(subCastes)
            ? subCastes
            : []
        }));

      } catch (error) {

        console.error(
          "❌ Failed to load sub castes:",
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
// Load cities when state changes
useEffect(() => {

 const loadCities = async () => {

  if (formData.stateId) {

   try {

    const cities =
     await masterDataAPI.getCitiesByState(
      formData.stateId
     );

    console.log(
     "🏙 Cities API Response:",
     cities
    );

    setMasterOptions(prev => ({

     ...prev,

     cities:
      Array.isArray(cities?.data)
       ? cities.data
       : Array.isArray(cities)
       ? cities
       : []

    }));

   } catch (error) {

    console.error(
     "Failed to load cities",
     error
    );

    setMasterOptions(prev => ({

     ...prev,

     cities: []

    }));

   }

  } else {

   setMasterOptions(prev => ({

    ...prev,

    cities: []

   }));

  }

 };

 loadCities();

}, [formData.stateId]); useEffect(() => {

 if(
 savedProfileData &&
 Object.keys(savedProfileData).length>0 &&
 masterDataLoaded
 ){
const loadProfile = async () => {

let partnerPreference = null;

try{

 partnerPreference =
 await partnerPreferenceAPI.getByUserId(
 savedProfileData.userId
 );

 console.log(
 "Partner Preference:",
 partnerPreference
 );

}catch(err){

 console.log(
 "No partner preference found"
 );

}

      console.log('🔧 Loading profile data into settings:', savedProfileData);
    console.log('🔧 Checking specific fields:', {
      address: savedProfileData.address,

      smokingId: savedProfileData.smokingId,

      drinkingId: savedProfileData.drinkingId,

      countryId:
        savedProfileData.countryId,

      stateId:
        savedProfileData.stateId,

      cityId:
        savedProfileData.cityId,

      religionId:
        savedProfileData.religionId,

      casteId:
        savedProfileData.casteId,

      subCasteId:
        savedProfileData.subCasteId,

      motherTongueId:
        savedProfileData.motherTongueId,

      heightId:
        savedProfileData.heightId,

      weightId:
        savedProfileData.weightId,

      companyName:
        savedProfileData.companyName,

      email:
        savedProfileData.email,

      phone:
        savedProfileData.phone
    });
console.log(savedProfileData)
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
      genderId: savedProfileData.genderId || null,
       dateOfBirth: savedProfileData.dateOfBirth || '',
       age: savedProfileData.age || calculateAge(savedProfileData.dateOfBirth),
       profilePhotoUrl:
         savedProfileData.imageUrl ||
         savedProfileData.profilePhotoUrl ||
         '',

       // RELIGION
       religionId:
         savedProfileData.religionId ||
         savedProfileData.religion?.id ||
         null,
      casteId:
        savedProfileData.casteId ||
        savedProfileData.caste?.id ||
        null,
       subCasteId:
         savedProfileData.subCasteId ||
         savedProfileData.subCaste?.id ||
         null,
       motherTongueId:
         savedProfileData.motherTongueId ||
         savedProfileData.motherTongue?.id ||
         null,

       // PERSONAL
       maritalStatusId:
         savedProfileData.maritalStatusId || null,
profileTypeId:

savedProfileData.profileTypeId ||

savedProfileData.profileType?.id ||

null,
manglikStatusId:
savedProfileData.manglikStatusId ||
savedProfileData.manglikStatus?.id ||
null,

familyTypeId:
savedProfileData.familyTypeId ||
savedProfileData.familyType?.id ||
null,

familyStatusId:
savedProfileData.familyStatusId ||
savedProfileData.familyStatus?.id ||
null,

familyValueId:
savedProfileData.familyValueId ||
savedProfileData.familyValue?.id ||
null,
      heightId:
        savedProfileData.heightId ||
        savedProfileData.height?.id ||
        null,

       weightId:
         savedProfileData.weightId ||
         savedProfileData.weight?.id ||
         null,

     complexionId:
       savedProfileData.complexionId || null,

     bodyTypeId:
       savedProfileData.bodyTypeId || null,

aboutMe:
  savedProfileData.aboutMe ||
  savedProfileData.about ||
  savedProfileData.about_me ||
  '',
  about:
    savedProfileData.about || '',

       // EDUCATION
       educationLevelId:
         savedProfileData.educationLevelId || null,

       occupationId:
         savedProfileData.occupationId || null,

       incomeId:
         savedProfileData.incomeId || '',

       companyName:
         savedProfileData.companyName || '',

       // LOCATION
countryId:
  savedProfileData.countryId ||
  savedProfileData.country?.id ||
  null,

stateId:
  savedProfileData.stateId ||
  savedProfileData.state?.id ||
  null,

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
       // dietId
       dietId: savedProfileData.dietId || '',
       smokingId: savedProfileData.smokingId || '',
       drinkingId: savedProfileData.drinkingId || '',

       // PARTNER PREFERENCE
      // PARTNER PREFERENCE

     // PARTNER PREFERENCE

     preferredAgeMin:
     partnerPreference?.minAge || '',

     preferredAgeMax:
     partnerPreference?.maxAge || '',

     preferredMinHeightId:
     partnerPreference?.minHeight || '',

     preferredMaxHeightId:
     partnerPreference?.maxHeight || '',

     preferredReligionId:
     partnerPreference?.religionId || null,

     preferredCasteId:
     partnerPreference?.casteId || null,

     preferredCityId:
     partnerPreference?.cityId || null,

     preferredEducationLevelId:
     partnerPreference?.educationLevelId || null,

     preferredOccupationId:
     partnerPreference?.occupationId || null,

     preferredMaritalStatusId:
     partnerPreference?.maritalStatusId || null,

     preferredSmokingId:
     partnerPreference?.smokingId || null,

     preferredDrinkingId:
     partnerPreference?.drinkingId || null,

     preferredDietId:
     partnerPreference?.dietId || null,
otherExpectations:
savedProfileData.otherExpectations || '',



otherExpectations:

savedProfileData.otherExpectations ||

savedProfileData.otherExpectation ||

"",


qualificationId:
savedProfileData.qualificationId ||
savedProfileData.qualification?.id ||
savedProfileData.qualification_id ||
null,

fieldOfStudyId:
savedProfileData.fieldOfStudyId ||
savedProfileData.fieldOfStudy?.id ||
savedProfileData.field_of_study_id ||
null,

employedId:
savedProfileData.employedId ||
savedProfileData.employedStatusId ||
savedProfileData.employed?.id ||
null,
disabilityStatusId:
savedProfileData.disabilityStatusId ||
savedProfileData.disability?.id ||
savedProfileData.disabilityStatus?.id ||
null,
bloodGroupId:
savedProfileData.bloodGroupId ||
savedProfileData.bloodGroup?.id ||
savedProfileData.blood_group?.id ||
null,


     };
      
      console.log('🔧 Mapped data for form:', mappedData);
            setFormData(prev => ({ ...prev, ...mappedData }));
            };

            loadProfile();
    }


}, [savedProfileData, masterDataLoaded]);

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
      'religionId',
      'casteId',
      'subCasteId',
      'motherTongueId',
      'maritalStatusId',
      'educationLevelId',
      'occupationId',
      'manglikStatusId',
'qualificationId',
'fieldOfStudyId',
'employedId',
'disabilityStatusId',
'bloodGroupId',
      'familyTypeId',

      'familyStatusId',

      'familyValueId',
      'heightId',
      'weightId',
      'cityId',
      'genderId',
      'bodyTypeId',
      'complexionId',
      'countryId',
      'stateId',

      'incomeId',
      'dietId',
      'smokingId',
      'drinkingId',
      'profileTypeId',
      'preferredReligionId',
      'preferredCasteId',
      'preferredCityId',
      'preferredEducationLevelId',
      'preferredOccupationId',
      'preferredMaritalStatusId',
      'preferredSmokingId',
      'preferredDrinkingId',
      'preferredMinHeightId',

      'preferredMaxHeightId',
      'preferredDietId',
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
    
   if (!formData.genderId) {
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
      const partnerPreferenceData = {

       minAge:
       formData.preferredAgeMin,

       maxAge:
       formData.preferredAgeMax,

     minHeight:
     Number(formData.preferredMinHeightId) || null,

     maxHeight:
     Number(formData.preferredMaxHeightId) || null,

       religionId:
       formData.preferredReligionId,

       casteId:
       formData.preferredCasteId,

       cityId:
       formData.preferredCityId,

       educationLevelId:
       formData.preferredEducationLevelId,

       occupationId:
       formData.preferredOccupationId,

       maritalStatusId:
       formData.preferredMaritalStatusId,

       smokingId:
       formData.preferredSmokingId,

       drinkingId:
       formData.preferredDrinkingId,

       dietId:
       formData.preferredDietId,

       isActive:true

      };

      console.log(
      "PARTNER PREF FINAL",
      JSON.stringify(
      partnerPreferenceData,
      null,
      2
      )
      );

  console.log(
  "PARTNER PREF DATA:",
  partnerPreferenceData
  )
     const dataToSave = {

       // BASIC
       firstName: firstNameFromFull,
       middleName: formData.middleName,
       lastName: lastNameFromFull,

       email: formData.email,
       phone: formData.phone,

     genderId: formData.genderId,
       dateOfBirth: formData.dateOfBirth,
manglikStatusId:
formData.manglikStatusId,

familyTypeId:
formData.familyTypeId,

familyStatusId:
formData.familyStatusId,

familyValueId:
formData.familyValueId,
       // ABOUT
       aboutMe: formData.aboutMe || formData.about,
       about: formData.aboutMe || formData.about,
otherExpectations:
formData.otherExpectations,
 imageUrl: formData.profilePhotoUrl,


       // RELIGION
       religionId: formData.religionId,
       casteId: formData.casteId,
       subCasteId: formData.subCasteId,
       motherTongueId: formData.motherTongueId,

       // PERSONAL
       maritalStatusId: formData.maritalStatusId,
       profileTypeId:

       formData.profileTypeId,
       heightId: formData.heightId,
       weightId: formData.weightId,

       complexionId: formData.complexionId,
       bodyTypeId: formData.bodyTypeId,

       // EDUCATION
       educationLevelId: formData.educationLevelId,
       occupationId: formData.occupationId,

       incomeId: formData.incomeId,
       companyName: formData.companyName,

       // LOCATION
      countryId: formData.countryId,
      stateId: formData.stateId,
       cityId: formData.cityId,
       address: formData.address,

       // LIFESTYLE
       dietId: formData.dietId,
       smokingId: formData.smokingId,
       drinkingId: formData.drinkingId,

       // FAMILY
       fatherName: formData.fatherName,
       fatherOccupation: formData.fatherOccupation,

       motherName: formData.motherName,
       motherOccupation: formData.motherOccupation,

       siblingsCount: formData.siblingsCount,


qualificationId:
formData.qualificationId,

fieldOfStudyId:
formData.fieldOfStudyId,

employedId: formData.employedId,

disabilityStatusId:
formData.disabilityStatusId,
// PARTNER PREFERENCE


     };
      console.log('📤 Data to save to backend:', dataToSave);
      
const result =
await saveProfileData(dataToSave);
      if (result) {
         if(result){

          const userId =
          result.id ||
          savedProfileData?.id;

         try{

          const existingPreference=
          await partnerPreferenceAPI.getByUserId(
          userId
          );

          if(existingPreference){

           await partnerPreferenceAPI.update(
            userId,
            partnerPreferenceData
           );

          }

         }catch{

          await partnerPreferenceAPI.save({

           userId:userId,

           ...partnerPreferenceData

          });

         }
         }        success("Profile updated successfully!");
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

if (key === "genderId") {

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
else if(key==="qualificationId"){
 fieldOptions=
 masterOptions.qualifications||[]
}

else if(key==="fieldOfStudyId"){
 fieldOptions=
 masterOptions.fieldOfStudies||[]
}

else if(key==="employedId"){

 fieldOptions=
 masterOptions.employedStatuses || []

}

else if(key==="disabilityStatusId"){
 fieldOptions=
 masterOptions.disabilityStatuses||[]
}

else if(key==="bloodGroupId"){
 fieldOptions=
 masterOptions.bloodGroups||[]
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


else if (key === "complexionId") {

  fieldOptions =
    masterOptions.complexions || [];

}

else if (key === "bodyTypeId") {

  fieldOptions =
    masterOptions.bodyTypes || [];

}
else if (key === "countryId") {

  fieldOptions =
    masterOptions.countries || [];

}

else if (key === "stateId") {

  fieldOptions =
    masterOptions.states || [];

}
else if (key === "incomeId") {

  fieldOptions =
    masterOptions.incomes || [];

}

else if (key === "dietId") {

  fieldOptions =
    masterOptions.diets || [];

}

else if (key === "smokingId") {

  fieldOptions =
    masterOptions.smokingOptions || [];

}

else if (key === "drinkingId") {

  fieldOptions =
    masterOptions.drinkingOptions || [];

}
else if (key === "profileTypeId") {

fieldOptions =

masterOptions.profileTypes || [];

}
else if (key === "manglikStatusId") {

fieldOptions =

masterOptions.manglikStatuses || [];

}

else if (key === "familyTypeId") {

fieldOptions =

masterOptions.familyTypes || [];

}

else if (key === "familyStatusId") {

fieldOptions =

masterOptions.familyStatuses || [];

}

else if (key === "familyValueId") {

fieldOptions =

masterOptions.familyValues || [];

}
else if(key==="preferredReligionId"){

 fieldOptions=
 masterOptions.religions || []

}

else if(key==="preferredCasteId"){

 fieldOptions=
 masterOptions.castes || []

}

else if(key==="preferredCityId"){

 fieldOptions=
 masterOptions.allCities || []

}

else if(key==="preferredEducationLevelId"){

 fieldOptions=
 masterOptions.educationLevels || []

}

else if(key==="preferredOccupationId"){

 fieldOptions=
 masterOptions.occupations || []

}

else if(key==="preferredMaritalStatusId"){

 fieldOptions=
 masterOptions.maritalStatuses || []

}

else if(key==="preferredSmokingId"){

 fieldOptions=
 masterOptions.smokingOptions || []

}

else if(key==="preferredDrinkingId"){

 fieldOptions=
 masterOptions.drinkingOptions || []

}

else if(key==="preferredDietId"){

 fieldOptions=
 masterOptions.diets || []

}
else if(key==="preferredMinHeightId"){

 fieldOptions =
 masterOptions.heights || []

}

else if(key==="preferredMaxHeightId"){

 fieldOptions =
 masterOptions.heights || []

}
    else {


        fieldOptions =
          options || [];

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

           if (key === "stateId") {

         handleInputChange(
           "cityId",
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
            opt?.id ??
            opt?.cityId ??
            opt?.stateId ??
            opt?.countryId ??
            opt?.casteId ??
            opt?.subCasteId ??
            opt?.value ??
            opt;

     const optionLabel =

     key === "bloodGroupId"
     ? opt?.type

     : key === "disabilityStatusId"
     ? opt?.value

    : key === "drinkingId" || key==="preferredDrinkingId"
    ? opt?.value ?? opt?.name

  : key === "smokingId" || key==="preferredSmokingId"
  ? opt?.value ?? opt?.name

     : key === "dietId"
     ? opt?.name ?? opt?.value

     : opt?.name ??
     opt?.type ??
     opt?.cityName ??
     opt?.stateName ??
     opt?.countryName ??
     opt?.casteName ??
     opt?.subCasteName ??
     opt?.value ??
     opt?.range ??
     opt?.label ??
     "Select Option";
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

        <motion.div key={activeTab} initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="
bg-card
rounded-2xl
border
border-border
p-8
">
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
                   key: "genderId",
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

            label:"Profile Type",

            key:"profileTypeId",

            type:"select"

            })}
{renderField({
label:"Qualification",
key:"qualificationId",
type:"select"
})}

{renderField({
label:"Field Of Study",
key:"fieldOfStudyId",
type:"select"
})}

{renderField({
label:"Employment Status",
key:"employedId",
type:"select"
})}

{renderField({
label:"Disability Status",
key:"disabilityStatusId",
type:"select"
})}

{renderField({
label:"Blood Group",
key:"bloodGroupId",
type:"select"
})}        {renderField({
        label:"Manglik Status",
        key:"manglikStatusId",
        type:"select"
        })}

        {renderField({
        label:"Family Type",
        key:"familyTypeId",
        type:"select"
        })}

        {renderField({
        label:"Family Status",
        key:"familyStatusId",
        type:"select"
        })}

        {renderField({
        label:"Family Values",
        key:"familyValueId",
        type:"select"
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
                    type: "select",
                  })}
                  {renderField({ 
                    label: "Body Type", 
                  key: "bodyTypeId",
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
                  {renderField({ label: "Annual Income", key: "incomeId", type: "select" })}
                  {renderField({ label: "Company Name", placeholder: "Your company", key: "companyName" })}
                </div>
              </div>

              {/* Location Section */}
              <div>
                <h3 className="text-sm font-semibold text-foreground mb-3 pb-2 border-b border-border">Location Details</h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {renderField({
                  label: "Country",
                  key: "countryId",
                  type: "select"
                })}

                {renderField({
                  label: "State",
                  key: "stateId",
                  type: "select"
                })}

                {renderField({
                  label: "City",
                  key: "cityId",
                  type: "select"
                })}

                {renderField({
                  label: "Address",
                  key: "address",
                  placeholder: "Enter address"
                })}

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
{renderField({
  label: "Number of Siblings",
  key: "siblingsCount",
  placeholder: "Enter siblings count"
})}
                </div>
              </div>
{/* Partner Preferences Section */}




            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">

            {renderField({
            label:"Preferred Age (Min)",
            type:"number",
            key:"preferredAgeMin"
            })}

            {renderField({
            label:"Preferred Age (Max)",
            type:"number",
            key:"preferredAgeMax"
            })}
{renderField({

label:"Preferred Height (Min)",

key:"preferredMinHeightId",

type:"select"

})}

{renderField({

label:"Preferred Height (Max)",

key:"preferredMaxHeightId",

type:"select"

})}
            {renderField({
            label:"Preferred Religion",
            key:"preferredReligionId",
            type:"select"
            })}

            {renderField({
            label:"Preferred Caste",
            key:"preferredCasteId",
            type:"select"
            })}

            {renderField({
            label:"Preferred City",
            key:"preferredCityId",
            type:"select"
            })}

            {renderField({
            label:"Preferred Education",
            key:"preferredEducationLevelId",
            type:"select"
            })}

            {renderField({
            label:"Preferred Occupation",
            key:"preferredOccupationId",
            type:"select"
            })}

            {renderField({
            label:"Preferred Marital Status",
            key:"preferredMaritalStatusId",
            type:"select"
            })}

            {renderField({
            label:"Smoking Preference",
            key:"preferredSmokingId",
            type:"select"
            })}

            {renderField({
            label:"Drinking Preference",
            key:"preferredDrinkingId",
            type:"select"
            })}

            {renderField({
            label:"Diet Preference",
            key:"preferredDietId",
            type:"select"
            })}

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
