import { useState, useEffect } from "react";
import { User, Lock, Bell, Save, Upload, X } from "lucide-react";
import { motion } from "framer-motion";

import { useToast } from "@/components/Toast";
import { useProfileData } from "@/hooks/useProfileData";
import { partnerPreferenceAPI } from "@/services/api";
import { masterDataAPI } from "@/services/api";
import { photoAPI } from "@/services/api";

const tabs = [
  { id: "profile", label: "Profile", icon: <User className="h-4 w-4" /> },
  { id: "password", label: "Password", icon: <Lock className="h-4 w-4" /> },
  { id: "notifications", label: "Notifications", icon: <Bell className="h-4 w-4" /> },
];

const SettingsPage = () => {
  const [activeTab, setActiveTab] = useState("profile");
  const { success, error, info } = useToast();
  const { profileData: savedProfileData, saveProfileData } = useProfileData();

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
   profileTypes:[],

   manglikStatuses:[],

   familyTypes:[],

   familyStatuses:[],

   familyValues:[],

   qualifications:[],

   fieldsOfStudy:[],

   employmentStatuses:[],

   disabilityStatuses:[],

   bloodGroups:[]
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
profileTypeId:null,

manglikStatusId:null,

familyTypeId:null,

familyStatusId:null,

familyValueId:null,

qualificationId:null,

fieldOfStudyId:null,

employedId:null,

disabilityStatusId:null,

bloodGroupId:null,
    age: "",
    
    // For UI purposes
    profilePhoto: null,
    profilePhotoUrl: ""
  });
  const [galleryPhotos, setGalleryPhotos] = useState([]);
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: ""
  });
const [partnerPreference,setPartnerPreference]=
useState({

minAge:"",
maxAge:"",

minHeight:"",
maxHeight:"",

minWeight:"",
maxWeight:"",

religionId:null,
casteId:null,

cityId:null,

educationLevelId:null,

occupationId:null,

maritalStatusId:null,

smokingId:null,

drinkingId:null,

dietId:null,

otherExpectations:""

});
useEffect(()=>{

const loadPartnerPreference =
async()=>{

try{

const userId =

savedProfileData?.userId ||
savedProfileData?.id;

if(!userId){

console.log(
"No user id yet"
);

return;

}

const pref =

await partnerPreferenceAPI
.getMyPreference(
userId
);

console.log(
"Partner Preference:",
pref
);

setPartnerPreference({

minAge:
pref?.minAge ?? "",

maxAge:
pref?.maxAge ?? "",

minHeight:
pref?.minHeight ?? "",

maxHeight:
pref?.maxHeight ?? "",

minWeight:
pref?.minWeight ?? "",

maxWeight:
pref?.maxWeight ?? "",
religionId:
pref?.religionId
? Number(pref.religionId)
: null,

casteId:
pref?.casteId
? Number(pref.casteId)
: null,

cityId:
pref?.cityId
? Number(pref.cityId)
: null,

educationLevelId:
pref?.educationLevelId
? Number(pref.educationLevelId)
: null,

occupationId:
pref?.occupationId
? Number(pref.occupationId)
: null,

maritalStatusId:
pref?.maritalStatusId
? Number(pref.maritalStatusId)
: null,

smokingId:
pref?.smokingId
? Number(pref.smokingId)
: null,

drinkingId:
pref?.drinkingId
? Number(pref.drinkingId)
: null,

dietId:
pref?.dietId
? Number(pref.dietId)
: null,

otherExpectations:
pref?.otherExpectations ?? ""

});

}catch(err){

console.log(
"Partner Preference Error:",
err
);

}

};

const userId =
savedProfileData?.userId ||
savedProfileData?.id;

if(userId){

loadPartnerPreference();

}

},[

savedProfileData,

]);

// Load master data options from backend
  useEffect(() => {
    const loadMasterData = async () => {
      try {
        console.log('🔍 Loading master data from APIs...');
        
   const [
     incomes,
     diets,
     smokingOptions,
     drinkingOptions,
     profileTypes,

     manglikStatuses,

     familyTypes,

     familyStatuses,

     familyValues,

     qualifications,

     fieldsOfStudy,

     employmentStatuses,

     disabilityStatuses,

     bloodGroups,
     religions,
     genders,
     cities,
     educationLevels,
     occupations,
     maritalStatuses,
     heights,
     complexions,
     bodyTypes,
     weights,
     motherTongues,
     countries,
     states
   ] = await Promise.all([

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

masterDataAPI.getFieldsOfStudy(),

masterDataAPI.getEmploymentStatuses(),

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
        
        // Ensure all data are arra
        const safeData = {

                                   profileTypes:
                                   Array.isArray(profileTypes?.data)
                                   ? profileTypes.data
                                   : Array.isArray(profileTypes)
                                   ? profileTypes
                                   : [],

                                   manglikStatuses:
                                   Array.isArray(manglikStatuses?.data)
                                   ? manglikStatuses.data
                                   : Array.isArray(manglikStatuses)
                                   ? manglikStatuses
                                   : [],

                                   familyTypes:
                                   Array.isArray(familyTypes?.data)
                                   ? familyTypes.data
                                   : Array.isArray(familyTypes)
                                   ? familyTypes
                                   : [],

                                   familyStatuses:
                                   Array.isArray(familyStatuses?.data)
                                   ? familyStatuses.data
                                   : Array.isArray(familyStatuses)
                                   ? familyStatuses
                                   : [],

                                   familyValues:
                                   Array.isArray(familyValues?.data)
                                   ? familyValues.data
                                   : Array.isArray(familyValues)
                                   ? familyValues
                                   : [],

                                   qualifications:
                                   Array.isArray(qualifications?.data)
                                   ? qualifications.data
                                   : Array.isArray(qualifications)
                                   ? qualifications
                                   : [],

                                   fieldsOfStudy:
                                   Array.isArray(fieldsOfStudy?.data)
                                   ? fieldsOfStudy.data
                                   : Array.isArray(fieldsOfStudy)
                                   ? fieldsOfStudy
                                   : [],

                                   employmentStatuses:
                                   Array.isArray(employmentStatuses?.data)
                                   ? employmentStatuses.data
                                   : Array.isArray(employmentStatuses)
                                   ? employmentStatuses
                                   : [],

                                   disabilityStatuses:
                                   Array.isArray(disabilityStatuses?.data)
                                   ? disabilityStatuses.data
                                   : Array.isArray(disabilityStatuses)
                                   ? disabilityStatuses
                                   : [],

                                   bloodGroups:
                                   Array.isArray(bloodGroups?.data)
                                   ? bloodGroups.data
                                   : Array.isArray(bloodGroups)
                                   ? bloodGroups
                                   : [],

                                   religions:
                                   Array.isArray(religions?.data)
                                   ? religions.data
                                   : Array.isArray(religions)
                                   ? religions
                                   : [],

                                   genders:
                                   Array.isArray(genders?.data)
                                   ? genders.data
                                   : Array.isArray(genders)
                                   ? genders
                                   : [],

                                   partnerCastes: [],

                                   cities:
                                   Array.isArray(cities?.data)
                                   ? cities.data
                                   : Array.isArray(cities)
                                   ? cities
                                   : [],

                                   educationLevels:
                                   Array.isArray(educationLevels?.data)
                                   ? educationLevels.data
                                   : Array.isArray(educationLevels)
                                   ? educationLevels
                                   : [],

                                   occupations:
                                   Array.isArray(occupations?.data)
                                   ? occupations.data
                                   : Array.isArray(occupations)
                                   ? occupations
                                   : [],

                                   maritalStatuses:
                                   Array.isArray(maritalStatuses?.data)
                                   ? maritalStatuses.data
                                   : Array.isArray(maritalStatuses)
                                   ? maritalStatuses
                                   : [],

                                   heights:
                                   Array.isArray(heights?.data)
                                   ? heights.data
                                   : Array.isArray(heights)
                                   ? heights
                                   : [],

                                   weights:
                                   Array.isArray(weights?.data)
                                   ? weights.data
                                   : Array.isArray(weights)
                                   ? weights
                                   : [],

                                   complexions:
                                   Array.isArray(complexions?.data)
                                   ? complexions.data
                                   : Array.isArray(complexions)
                                   ? complexions
                                   : [],

                                   bodyTypes:
                                   Array.isArray(bodyTypes?.data)
                                   ? bodyTypes.data
                                   : Array.isArray(bodyTypes)
                                   ? bodyTypes
                                   : [],

                                   castes: [],

                                   subCastes: [],

                                   motherTongues:
                                   Array.isArray(motherTongues?.data)
                                   ? motherTongues.data
                                   : Array.isArray(motherTongues)
                                   ? motherTongues
                                   : [],

                                   countries:
                                   Array.isArray(countries?.data)
                                   ? countries.data
                                   : Array.isArray(countries)
                                   ? countries
                                   : [],

                                   states:
                                   Array.isArray(states?.data)
                                   ? states.data
                                   : Array.isArray(states)
                                   ? states
                                   : [],

                                   incomes:
                                   Array.isArray(incomes?.data)
                                   ? incomes.data
                                   : Array.isArray(incomes)
                                   ? incomes
                                   : [],

                                   diets:
                                   Array.isArray(diets?.data)
                                   ? diets.data
                                   : Array.isArray(diets)
                                   ? diets
                                   : [],

                                   smokingOptions:
                                   Array.isArray(smokingOptions?.data)
                                   ? smokingOptions.data
                                   : Array.isArray(smokingOptions)
                                   ? smokingOptions
                                   : [],

                                   drinkingOptions:
                                   Array.isArray(drinkingOptions?.data)
                                   ? drinkingOptions.data
                                   : Array.isArray(drinkingOptions)
                                   ? drinkingOptions
                                   : []

                                   };

                                   setMasterOptions(safeData);
console.log(
"Disability:",
safeData.disabilityStatuses
);
                                   console.log("Blood Groups:", safeData.bloodGroups);
                                   console.log("Family Types:", safeData.familyTypes);
                                   console.log("Employment:", safeData.employmentStatuses);
        
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
  states: []

  });    }
    };
    
    loadMasterData();
  }, []);

 // Load castes when partner religion changes

 useEffect(() => {

 const loadCastes = async () => {

 if (partnerPreference.religionId) {

 try {

 console.log(
 "Loading castes:",
 partnerPreference.religionId
 );

 const castes =
 await masterDataAPI.getCastes(

 partnerPreference.religionId

 );

 setMasterOptions(prev=>({

 ...prev,

partnerCastes:
Array.isArray(castes)
? castes
: []

 }));

 }catch(error){

 console.log(error);

 setMasterOptions(prev=>({

 ...prev,

partnerCastes:[]

 }));

 }

 }else{

 setMasterOptions(prev=>({

 ...prev,

 partnerCastes:[]

 }));

 }

 };

 loadCastes();

 }, [

 partnerPreference.religionId

 ]);
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

// Load castes when profile religion changes

useEffect(()=>{

const loadProfileCastes=
async()=>{

if(formData.religionId){

try{

const castes=
await masterDataAPI.getCastes(
formData.religionId
);

setMasterOptions(prev=>({

...prev,

castes:
Array.isArray(castes)
? castes
: []

}));

}catch(err){

console.log(err);

}

}else{

setMasterOptions(prev=>({

...prev,

castes:[]

}));

}

};

loadProfileCastes();

},[
formData.religionId
]);
// Load cities when state changes
useEffect(()=>{

const loadCities=async()=>{

try{

const cities=
await masterDataAPI
.getCities();

setMasterOptions(prev=>({

...prev,

cities:
Array.isArray(cities?.data)
? cities.data
: Array.isArray(cities)
? cities
: []

}));

}catch(err){

console.log(err);

}

};

loadCities();

},[]); // Load saved profile data on mount
  useEffect(() => {
    if (savedProfileData && Object.keys(savedProfileData).length > 0) {
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
     genderId:
     savedProfileData.genderId
     ? Number(savedProfileData.genderId)
     : null,
       dateOfBirth: savedProfileData.dateOfBirth || '',
       age: savedProfileData.age || calculateAge(savedProfileData.dateOfBirth),
       profilePhotoUrl:
         savedProfileData.imageUrl ||
         savedProfileData.profilePhotoUrl ||
         '',

       // RELIGION
   religionId:
   savedProfileData.religionId
   ? Number(savedProfileData.religionId)
   : savedProfileData.religion?.id
   ? Number(savedProfileData.religion.id)
   : null,
     casteId:

     savedProfileData.casteId
     ? Number(savedProfileData.casteId)

     : savedProfileData.caste?.id
     ? Number(savedProfileData.caste.id)

     : null,
       subCasteId:

       savedProfileData.subCasteId
       ? Number(savedProfileData.subCasteId)

       : savedProfileData.subCaste?.id
       ? Number(savedProfileData.subCaste.id)

       : null,
       motherTongueId:

       savedProfileData.motherTongueId
       ? Number(savedProfileData.motherTongueId)

       : savedProfileData.motherTongue?.id
       ? Number(savedProfileData.motherTongue.id)

       : null,

       // PERSONAL
     maritalStatusId:

     savedProfileData.maritalStatusId
     ? Number(savedProfileData.maritalStatusId)

     : savedProfileData.maritalStatus?.id
     ? Number(savedProfileData.maritalStatus.id)

     : null,
    heightId:

    savedProfileData.heightId
    ? Number(savedProfileData.heightId)

    : savedProfileData.height?.id
    ? Number(savedProfileData.height.id)

    : null,
    weightId:

    savedProfileData.weightId
    ? Number(savedProfileData.weightId)

    : savedProfileData.weight?.id
    ? Number(savedProfileData.weight.id)

    : null,

complexionId:
savedProfileData.complexionId
? Number(savedProfileData.complexionId)
: null,

bodyTypeId:
savedProfileData.bodyTypeId
? Number(savedProfileData.bodyTypeId)
: null,

aboutMe:
  savedProfileData.aboutMe ||
  savedProfileData.about ||
  savedProfileData.about_me ||
  '',
  about:
    savedProfileData.about || '',

       // EDUCATION
      educationLevelId:

      savedProfileData.educationLevelId
      ? Number(savedProfileData.educationLevelId)

      : null,
     occupationId:

     savedProfileData.occupationId
     ? Number(savedProfileData.occupationId)

     : null,
       incomeId:
       savedProfileData.incomeId
       ? Number(savedProfileData.incomeId)
       : null,

       companyName:
         savedProfileData.companyName || '',
profileTypeId:
savedProfileData.profileTypeId
? Number(savedProfileData.profileTypeId)
: null,

manglikStatusId:
savedProfileData.manglikStatusId
? Number(savedProfileData.manglikStatusId)
: null,

familyTypeId:
savedProfileData.familyTypeId
? Number(savedProfileData.familyTypeId)
: null,

familyStatusId:
savedProfileData.familyStatusId
? Number(savedProfileData.familyStatusId)
: null,

familyValueId:
savedProfileData.familyValueId
? Number(savedProfileData.familyValueId)
: null,

qualificationId:
savedProfileData.qualificationId
? Number(savedProfileData.qualificationId)
: null,

fieldOfStudyId:
savedProfileData.fieldOfStudyId
? Number(savedProfileData.fieldOfStudyId)
: null,

employedId:
savedProfileData.employedStatusId
? Number(savedProfileData.employedStatusId)
: null,

disabilityStatusId:
savedProfileData.disabilityStatusId
? Number(savedProfileData.disabilityStatusId)
: null,

bloodGroupId:
savedProfileData.bloodGroupId
? Number(savedProfileData.bloodGroupId)
: null,
       // LOCATION
countryId:

savedProfileData.countryId
? Number(savedProfileData.countryId)

: savedProfileData.country?.id
? Number(savedProfileData.country.id)

: null,
stateId:

savedProfileData.stateId
? Number(savedProfileData.stateId)

: savedProfileData.state?.id
? Number(savedProfileData.state.id)

: null,
cityId:

savedProfileData.cityId
? Number(savedProfileData.cityId)

: savedProfileData.city?.id
? Number(savedProfileData.city.id)

: null,
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
     dietId:
     savedProfileData.dietId
     ? Number(savedProfileData.dietId)
     : null,

     smokingId:
     savedProfileData.smokingId
     ? Number(savedProfileData.smokingId)
     : null,

     drinkingId:
     savedProfileData.drinkingId
     ? Number(savedProfileData.drinkingId)
     : null,
 // PARTNER PREFERENCE


 };
      
    console.log(
    "Mapped Data:",
    mappedData
    );

    console.log(
    "Master Options:",
    masterOptions
    );
            setFormData(prev => ({ ...prev, ...mappedData }));
    }
}, [
savedProfileData
]);

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
      'profileTypeId',
      'manglikStatusId',
      'familyTypeId',
      'familyStatusId',
      'familyValueId',
      'qualificationId',
      'fieldOfStudyId',
      'employedId',
      'disabilityStatusId',
      'bloodGroupId',
      'drinkingId'
    ];
      const finalValue = idFields.includes(field) && value !== '' 
        ? (typeof value === 'string' ? parseInt(value, 10) : value)
        : value;
      
      setFormData(prev => ({ ...prev, [field]: finalValue }));
    }
  };
const handlePartnerPreferenceChange =
(field,value)=>{

const numericFields=[

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

 "dietId"

 ];
setPartnerPreference(prev=>{

const updated={

...prev,

[field]:
numericFields.includes(field)
? (
value === "" ||
value === null
? null
: Number(value)
)
: value
};

if(field==="religionId"){

updated.casteId=null;

}

return updated;

});
};  const handleProfilePhotoUpload = (e) => {
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
const handleGalleryUpload = (e) => {

  const files = Array.from(
    e.target.files || []
  );

  if (!files.length) return;

  const currentCount =
    galleryPhotos.filter(
      p => p.id || p.file
    ).length;

  if (
    currentCount + files.length > 8
  ) {

    error(
      "Maximum 8 photos allowed"
    );

    return;
  }

  const photos = files.map(file => ({

    file,

    preview:
      URL.createObjectURL(file),

    uploaded: false

  }));

  setGalleryPhotos(prev => [

    ...prev,

    ...photos

  ]);

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
if(
partnerPreference.minAge &&
partnerPreference.maxAge &&
Number(partnerPreference.minAge)
>
Number(partnerPreference.maxAge)
){

error(
"Minimum age cannot be greater than maximum age"
);

return false;

}
const totalPhotos =
  galleryPhotos.length;

if (totalPhotos < 4) {

  error(
    "Please upload minimum 4 photos"
  );

  return false;
}
    return true;
  };
const removeGalleryPhoto = async (index) => {

  try {

    const photo = galleryPhotos[index];

    if (photo.id) {

      await photoAPI.deletePhoto(
        photo.id
      );

    }

    setGalleryPhotos(prev =>
      prev.filter((_, i) =>
        i !== index
      )
    );

    success(
      "Photo deleted successfully"
    );

  } catch (err) {

    console.error(err);

    error(
      "Failed to delete photo"
    );

  }

};
const loadGalleryPhotos = async () => {
  try {
    const photos = await photoAPI.getMyPhotos();

    setGalleryPhotos(
      photos.map(photo => ({
        id: photo.id,
        preview: photo.photoUrl,
        photoUrl: photo.photoUrl,
        uploaded: true
      }))
    );
  } catch (err) {
    console.error(err);
  }
};

useEffect(() => {

  loadGalleryPhotos();

}, []);


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

     genderId: formData.genderId,
       dateOfBirth: formData.dateOfBirth,

       // ABOUT
       aboutMe: formData.aboutMe || formData.about,
       about: formData.aboutMe || formData.about,

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

       complexionId: formData.complexionId,
       bodyTypeId: formData.bodyTypeId,

       // EDUCATION
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


     };
     console.log(
     '📤 Data to save to backend:',
     dataToSave
     );

     console.log(
     "BLOOD GROUP:",
     dataToSave.bloodGroupId
     );

     console.log(
     "DISABILITY:",
     dataToSave.disabilityStatusId
     );

     console.log(
     "FULL PAYLOAD:",
     JSON.stringify(
     dataToSave,
     null,
     2
     )
     );
const newPhotos = galleryPhotos.filter(
  photo => photo.file && !photo.uploaded
);

if (newPhotos.length > 0) {

  const photoFormData = new FormData();

  newPhotos.forEach(photo => {

    photoFormData.append(
      "files",
      photo.file
    );

  });

  console.log(
    "Uploading New Photos:",
    newPhotos.length
  );

  await photoAPI.uploadMultiple(
    photoFormData
  );

  await loadGalleryPhotos();
}

const result =
     await saveProfileData(
     dataToSave
     );
   const partnerData={

   userId:
   savedProfileData?.userId ||
   savedProfileData?.id,

   minAge:
   partnerPreference.minAge,

   maxAge:
   partnerPreference.maxAge,
minWeight:
partnerPreference.minWeight,

maxWeight:
partnerPreference.maxWeight,
   minHeight:
   partnerPreference.minHeight,

   maxHeight:
   partnerPreference.maxHeight,

   religionId:
   Number(partnerPreference.religionId)||null,

   casteId:
   Number(partnerPreference.casteId)||null,

   cityId:
   Number(partnerPreference.cityId)||null,

   educationLevelId:
   Number(partnerPreference.educationLevelId)||null,

   occupationId:
   Number(partnerPreference.occupationId)||null,

   maritalStatusId:
   Number(partnerPreference.maritalStatusId)||null,

   dietId:
   Number(partnerPreference.dietId)||null,

   smokingId:
   Number(partnerPreference.smokingId)||null,

   drinkingId:
   Number(partnerPreference.drinkingId)||null,
   otherExpectations:
   partnerPreference.otherExpectations,

   isActive:true

   };
console.log(
"PARTNER DATA:",
partnerData
);
    try{

    await partnerPreferenceAPI
    .update(

    partnerData.userId,

    partnerData

    );

    }catch{

    await partnerPreferenceAPI
    .save(
    partnerData
    );

    }
      
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
fieldOptions = masterOptions.profileTypes || [];
}

else if (key === "manglikStatusId") {
fieldOptions = masterOptions.manglikStatuses || [];
}

else if (key === "familyTypeId") {
fieldOptions = masterOptions.familyTypes || [];
}

else if (key === "familyStatusId") {
fieldOptions = masterOptions.familyStatuses || [];
}

else if (key === "familyValueId") {
fieldOptions = masterOptions.familyValues || [];
}

else if (key === "qualificationId") {
fieldOptions = masterOptions.qualifications || [];
}

else if (key === "fieldOfStudyId") {
fieldOptions = masterOptions.fieldsOfStudy || [];
}

else if (key === "employedId") {
fieldOptions = masterOptions.employmentStatuses || [];
}

else if (key === "disabilityStatusId") {
fieldOptions = masterOptions.disabilityStatuses || [];
}

else if (key === "bloodGroupId") {
fieldOptions = masterOptions.bloodGroups || [];
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
      value={formData[key] ?? ""}

      onChange={(e) => {

      const value = e.target.value;

      console.log(
      `Changed ${key}:`,
      value
      );

      handleInputChange(
      key,
      value
      );

      // reset dependent dropdowns

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

      {fieldOptions?.map((opt)=>{

      const optionValue =
      opt?.id ??
      opt?.cityId ??
      opt?.stateId ??
      opt?.countryId ??
      opt?.casteId ??
      opt?.subCasteId ??
      opt?.name ??
      opt;

   const optionLabel =

   opt?.type ||

   opt?.bloodGroup ||

   opt?.bloodGroupName ||

   opt?.groupName ||

   opt?.disabilityName ||

   opt?.familyTypeName ||

   opt?.familyStatusName ||

   opt?.familyValueName ||

   opt?.qualificationName ||

   opt?.fieldOfStudyName ||

   opt?.employedStatusName ||

   opt?.value ||

   opt?.name ||

   opt?.status ||

   opt?.cityName ||

   opt?.stateName ||

   opt?.countryName ||

   opt?.casteName ||

   opt?.subCasteName ||

   opt?.range ||

   opt?.label ||

   String(opt);
      return(

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

     value={formData[key] ?? ""}

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
                    label: "Religion", 
                    key: "religionId", 
                    type: "select" 
                  })}
                  {renderField({ label: "Caste", key: "casteId", type: "select" })}
                  {renderField({ label: "Sub-caste", key: "subCasteId", type: "select" })}
                  {renderField({ label: "Mother Tongue", key: "motherTongueId", type: "select" })}

                  {renderField({
                  label:"Manglik Status",
                  key:"manglikStatusId",
                  type:"select"
                  })}

                  {renderField({
                  label:"Blood Group",
                  key:"bloodGroupId",
                  type:"select"
                  })}

                  {renderField({
                  label:"Disability Status",
                  key:"disabilityStatusId",
                  type:"select"
                  })}
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
label:"Family Value",
key:"familyValueId",
type:"select"
})}
                </div>
              </div>

              {/* Partner Preferences Section */}
              <div>
               <h3 className="text-sm font-semibold mb-3">

               Partner Preferences

               </h3>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">

            {/* MIN AGE */}

          <div>

          <label className="text-xs font-medium mb-1 block">
          Preferred Age Min
          </label>

          <input
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
          "
          value={partnerPreference.minAge ?? ""}
          onChange={(e)=>
          handlePartnerPreferenceChange(
          "minAge",
          e.target.value
          )}
          placeholder="Enter minimum age"
          />

          </div>


          <div>

          <label className="text-xs font-medium mb-1 block">
          Preferred Age Max
          </label>

          <input
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
          "
          value={partnerPreference.maxAge ?? ""}
          onChange={(e)=>
          handlePartnerPreferenceChange(
          "maxAge",
          e.target.value
          )}
          placeholder="Enter maximum age"
          />

          </div>
           {/* HEIGHT MIN */}

           <div>

           <label className="text-xs font-medium mb-1 block">
           Preferred Height Min
           </label>

           <select
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
           "
           value={partnerPreference.minHeight ?? ""}
           onChange={(e)=>
           handlePartnerPreferenceChange(
           "minHeight",
           e.target.value
           )
           }
           >

           <option value="">
           Select Minimum Height
           </option>

           {masterOptions.heights.map((h)=>(

           <option
           key={h.id}
           value={h.id}
           >

           {h.name || h.value || h.height}

           </option>

           ))}

           </select>

           </div>


           {/* HEIGHT MAX */}

           <div>

           <label className="text-xs font-medium mb-1 block">
           Preferred Height Max
           </label>

           <select
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
           "
           value={partnerPreference.maxHeight ?? ""}
           onChange={(e)=>
           handlePartnerPreferenceChange(
           "maxHeight",
           e.target.value
           )
           }
           >

           <option value="">
           Select Maximum Height
           </option>

           {masterOptions.heights.map((h)=>(

           <option
           key={h.id}
           value={h.id}
           >

           {h.name || h.value || h.height}

           </option>

           ))}

           </select>

           </div>
<div>

<label className="text-xs font-medium mb-1 block">
Preferred Weight Min
</label>

<select

value={partnerPreference.minWeight ?? ""}

onChange={(e)=>

handlePartnerPreferenceChange(
"minWeight",
e.target.value
)

}

className="
w-full
bg-background
border
border-border
rounded-lg
px-4
py-2.5
"

>

<option value="">
Select Minimum Weight
</option>

{

masterOptions.weights.map((w)=>(

<option

key={w.id}

value={w.id}

>

{w.name || w.value || w.weight}

</option>

))

}

</select>

</div>

<div>

<label className="text-xs font-medium mb-1 block">
Preferred Weight Max
</label>

<select

value={partnerPreference.maxWeight ?? ""}

onChange={(e)=>

handlePartnerPreferenceChange(
"maxWeight",
e.target.value
)

}

className="
w-full
bg-background
border
border-border
rounded-lg
px-4
py-2.5
"

>

<option value="">
Select Maximum Weight
</option>

{

masterOptions.weights.map((w)=>(

<option

key={w.id}

value={w.id}

>

{w.name || w.value || w.weight}

</option>

))

}

</select>

</div>



           {/* RELIGION */}

           <div>

           <label className="text-xs font-medium mb-1 block">

           Preferred Religion

           </label>


              <select
              className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground"
              value={partnerPreference.religionId || ""}
              onChange={(e)=>
              handlePartnerPreferenceChange(
              "religionId",
              e.target.value
              )}
              >

              <option value="">
              Select Religion
              </option>

              {
              masterOptions.religions.map(r=>(
              <option
              key={r.id}
              value={r.id}
              >
              {r.name}
              </option>
              ))
              }

              </select>


</div>




            <div>

            <label className="text-xs font-medium mb-1 block">

            Preferred Caste

            </label>

            <select

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
            "

            value={partnerPreference.casteId || ""}

            onChange={(e)=>

            handlePartnerPreferenceChange(
            "casteId",
            e.target.value
            )

            }

            >

            <option value="">
            Select Caste
            </option>

            {

            masterOptions.partnerCastes.map((c)=>(

            <option
            key={c.id}
            value={c.id}
            >

            {c.name}

            </option>

            ))

            }

            </select>

            </div>
<div>

<label className="text-xs font-medium mb-1 block">
Preferred City
</label>

<select
className="w-full bg-background border border-border rounded-lg px-4 py-2.5"
value={partnerPreference.cityId || ""}
onChange={(e)=>
handlePartnerPreferenceChange(
"cityId",
e.target.value
)}
>

<option value="">
Select City
</option>

{masterOptions.cities.map((city)=>(
<option
key={city.id || city.cityId}
value={city.id || city.cityId}
>
{city.name || city.cityName}
</option>
))}

</select>

</div>

            <div>

            <label className="text-xs font-medium mb-1 block">

            Preferred Education

            </label>

            <select

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
            "

            value={partnerPreference.educationLevelId || ""}

            onChange={(e)=>

            handlePartnerPreferenceChange(
            "educationLevelId",
            e.target.value
            )

            }

            >

            <option value="">
            Select Education
            </option>

            {

            masterOptions.educationLevels.map((ed)=>(

            <option
            key={ed.id}
            value={ed.id}
            >

            {ed.name}

            </option>

            ))

            }

            </select>

            </div>

           <div>

           <label className="text-xs font-medium mb-1 block">

           Preferred Occupation

           </label>

           <select

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
           "

           value={partnerPreference.occupationId || ""}

           onChange={(e)=>

           handlePartnerPreferenceChange(
           "occupationId",
           e.target.value
           )

           }

           >

           <option value="">
           Select Occupation
           </option>

           {

           masterOptions.occupations.map((op)=>(

           <option
           key={op.id}
           value={op.id}
           >

           {op.name}

           </option>

           ))

           }

           </select>

           </div>

           <div>

           <label className="text-xs font-medium mb-1 block">

           Preferred Marital Status

           </label>

           <select

           className="
           w-full
           bg-background
           border
           border-border
           rounded-lg
           px-4
           py-2.5
           "

           value={partnerPreference.maritalStatusId || ""}

           onChange={(e)=>

           handlePartnerPreferenceChange(
           "maritalStatusId",
           e.target.value
           )

           }

           >

           <option value="">
           Select Marital Status
           </option>

           {

           masterOptions.maritalStatuses.map((ms)=>(

           <option
           key={ms.id}
           value={ms.id}
           >

           {ms.name}

           </option>

           ))

           }

           </select>

           </div>

           <div>

           <label className="text-xs font-medium mb-1 block">

           Smoking Preference

           </label>

           <select

           className="
           w-full
           bg-background
           border
           border-border
           rounded-lg
           px-4
           py-2.5
           "

           value={partnerPreference.smokingId || ""}

           onChange={(e)=>

           handlePartnerPreferenceChange(
           "smokingId",
           e.target.value
           )

           }

           >

           <option value="">
           Select Smoking Preference
           </option>

          {

          masterOptions.smokingOptions.map((sm)=>{

          return(

          <option
          key={sm.id}
          value={sm.id}
          >

          {

          sm.smokingType ||

          sm.smokingStatus ||

          sm.smokingPreference ||

          sm.type ||

          sm.name ||

          sm.value ||

          JSON.stringify(sm)

          }

          </option>

          );

          })

          }

           </select>

           </div>

         <div>

         <label className="text-xs font-medium mb-1 block">
         Drinking Preference
         </label>

         <select

         className="
         w-full
         bg-background
         border
         border-border
         rounded-lg
         px-4
         py-2.5
         "

         value={partnerPreference.drinkingId || ""}

         onChange={(e)=>
         handlePartnerPreferenceChange(
         "drinkingId",
         e.target.value
         )
         }

         >

         <option value="">
         Select Drinking
         </option>

         {
         masterOptions.drinkingOptions?.map((dr)=>(
         <option
         key={dr.id}
         value={dr.id}
         >

         {dr.value}

         </option>
         ))
         }

         </select>

         </div>
            <div>

            <label className="text-xs font-medium mb-1 block">

            Diet Preference

            </label>

            <select

            className="
            w-full
            bg-background
            border
            border-border
            rounded-lg
            px-4
            py-2.5
            "

            value={partnerPreference.dietId || ""}

            onChange={(e)=>

            handlePartnerPreferenceChange(
            "dietId",
            e.target.value
            )

            }

            >

            <option value="">
            Select Diet Preference
            </option>

           {

           masterOptions.diets.map((d)=>{

           return(

           <option
           key={d.id}
           value={d.id}
           >

           {

           d.dietType ||

           d.dietName ||

           d.name ||

           d.value ||

           JSON.stringify(d)

           }

           </option>

           );

           })

           }
            </select>

            </div>
<div className="sm:col-span-2">

<label className="
text-xs
font-medium
mb-1
block
">

Other Expectations

</label>

<textarea

rows={4}

value={
partnerPreference.otherExpectations
}

onChange={(e)=>

handlePartnerPreferenceChange(
"otherExpectations",
e.target.value
)

}

placeholder="
Any other expectations...
"

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
"

/>

</div>

</div>
</div>


{/* Contact Information Section */}
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
{/* Profile Photo Upload */}
              <div className="border rounded-lg p-4">

                <div className="flex justify-between mb-3">

                  <h3 className="font-semibold">
                    Photo Gallery
                  </h3>

                  <span>
                    {galleryPhotos.length}/8
                  </span>

                </div>

                <p className="text-red-500 text-xs mb-3">
                  Minimum 4 photos required
                </p>

                <label className="
                  bg-primary
                  text-white
                  px-4
                  py-2
                  rounded-lg
                  cursor-pointer
                  inline-block
                ">

                  Add Gallery Photos

                  <input
                    type="file"
                    multiple
                    accept="image/*"
                    className="hidden"
                    onChange={handleGalleryUpload}
                  />

                </label>

                <div className="
                  grid
                  grid-cols-2
                  md:grid-cols-4
                  gap-3
                  mt-4
                ">

                  {galleryPhotos.map((photo,index)=>(

                    <div
                      key={index}
                      className="relative"
                    >

                      <img
                        src={photo.preview}
                        alt=""
                        className="
                          h-32
                          w-full
                          object-cover
                          rounded-lg
                        "
                      />

                      <button
                        type="button"
                        onClick={() =>
                          removeGalleryPhoto(index)
                        }
                        className="
                          absolute
                          top-1
                          right-1
                          bg-red-500
                          text-white
                          rounded-full
                          p-1
                        "
                      >
                        <X size={14}/>
                      </button>

                    </div>

                  ))}

                </div>

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
