import { useState, useEffect } from 'react';

// Default options for matrimony fields
const defaultOptions = {
  religion: ['Hindu', 'Muslim', 'Christian', 'Sikh', 'Buddhist', 'Jain', 'Other'],
  caste: ['Open', 'OBC', 'SC', 'ST', 'NT', 'SBC', 'Other'],
  subCaste: ['General', 'OBC', 'SC', 'ST', 'Other'],
  education: ['High School', 'Bachelor\'s Degree', 'Master\'s Degree', 'PhD', 'Diploma', 'Other'],
  occupation: ['Software Engineer', 'Doctor', 'Teacher', 'Business', 'Government Job', 'Student', 'Other'],
  maritalStatus: ['Single', 'Married', 'Divorced', 'Widowed'],
  state: [
    'Andhra Pradesh', 'Arunachal Pradesh', 'Assam', 'Bihar', 'Chhattisgarh', 'Goa', 
    'Gujarat', 'Haryana', 'Himachal Pradesh', 'Jharkhand', 'Karnataka', 'Kerala', 
    'Madhya Pradesh', 'Maharashtra', 'Manipur', 'Meghalaya', 'Mizoram', 'Nagaland', 
    'Odisha', 'Punjab', 'Rajasthan', 'Sikkim', 'Tamil Nadu', 'Telangana', 'Tripura', 
    'Uttar Pradesh', 'Uttarakhand', 'West Bengal', 'Other'
  ],
  city: {
    'Andhra Pradesh': ['Visakhapatnam', 'Vijayawada', 'Guntur', 'Nellore', 'Kurnool', 'Other'],
    'Arunachal Pradesh': ['Itanagar', 'Naharlagun', 'Pasighat', 'Other'],
    'Assam': ['Guwahati', 'Silchar', 'Dibrugarh', 'Jorhat', 'Other'],
    'Bihar': ['Patna', 'Gaya', 'Bhagalpur', 'Muzaffarpur', 'Other'],
    'Chhattisgarh': ['Raipur', 'Bhilai', 'Bilaspur', 'Durg', 'Other'],
    'Goa': ['Panaji', 'Margao', 'Vasco da Gama', 'Other'],
    'Gujarat': ['Ahmedabad', 'Surat', 'Vadodara', 'Rajkot', 'Other'],
    'Haryana': ['Gurugram', 'Faridabad', 'Panipat', 'Ambala', 'Other'],
    'Himachal Pradesh': ['Shimla', 'Dharamshala', 'Solan', 'Mandi', 'Other'],
    'Jharkhand': ['Ranchi', 'Jamshedpur', 'Dhanbad', 'Bokaro', 'Other'],
    'Karnataka': ['Bangalore', 'Mysore', 'Hubli', 'Mangalore', 'Other'],
    'Kerala': ['Thiruvananthapuram', 'Kochi', 'Kozhikode', 'Thrissur', 'Other'],
    'Madhya Pradesh': ['Bhopal', 'Indore', 'Gwalior', 'Jabalpur', 'Other'],
    'Maharashtra': ['Mumbai', 'Pune', 'Nagpur', 'Nashik', 'Other'],
    'Manipur': ['Imphal', 'Thoubal', 'Kakching', 'Other'],
    'Meghalaya': ['Shillong', 'Tura', 'Nongstoin', 'Other'],
    'Mizoram': ['Aizawl', 'Lunglei', 'Champhai', 'Other'],
    'Nagaland': ['Kohima', 'Dimapur', 'Mokokchung', 'Other'],
    'Odisha': ['Bhubaneswar', 'Cuttack', 'Rourkela', 'Puri', 'Other'],
    'Punjab': ['Chandigarh', 'Ludhiana', 'Amritsar', 'Jalandhar', 'Other'],
    'Rajasthan': ['Jaipur', 'Jodhpur', 'Udaipur', 'Kota', 'Other'],
    'Sikkim': ['Gangtok', 'Namchi', 'Mangan', 'Other'],
    'Tamil Nadu': ['Chennai', 'Coimbatore', 'Madurai', 'Tiruchirappalli', 'Other'],
    'Telangana': ['Hyderabad', 'Warangal', 'Nizamabad', 'Karimnagar', 'Other'],
    'Tripura': ['Agartala', 'Udaipur', 'Dharmanagar', 'Other'],
    'Uttar Pradesh': ['Lucknow', 'Kanpur', 'Ghaziabad', 'Agra', 'Other'],
    'Uttarakhand': ['Dehradun', 'Haridwar', 'Roorkee', 'Haldwani', 'Other'],
    'West Bengal': ['Kolkata', 'Howrah', 'Durgapur', 'Siliguri', 'Other'],
    'Other': ['Other']
  },
  complexion: ['Fair', 'Wheatish', 'Dark'],
  bodyType: ['Slim', 'Athletic', 'Average', 'Heavy'],
  diet: ['Vegetarian', 'Non-Vegetarian', 'Eggetarian'],
  smoking: ['No', 'Occasionally', 'Regularly'],
  drinking: ['No', 'Occasionally', 'Regularly'],
  motherTongue: ['Hindi', 'English', 'Marathi', 'Tamil', 'Telugu', 'Kannada', 'Gujarati', 'Bengali', 'Malayalam', 'Punjabi', 'Other'],
  height: [
    '4\'0"', '4\'1"', '4\'2"', '4\'3"', '4\'4"', '4\'5"', '4\'6"', '4\'7"', '4\'8"', '4\'9"', '4\'10"', '4\'11"',
    '5\'0"', '5\'1"', '5\'2"', '5\'3"', '5\'4"', '5\'5"', '5\'6"', '5\'7"', '5\'8"', '5\'9"', '5\'10"', '5\'11"',
    '6\'0"', '6\'1"', '6\'2"', '6\'3"', '6\'4"', '6\'5"', '6\'6"'
  ],
  weight: ['35 kg', '40 kg', '45 kg', '50 kg', '55 kg', '60 kg', '65 kg', '70 kg', '75 kg', '80 kg', '85 kg', '90 kg', '95 kg', '100 kg', '105 kg', '110 kg', '115 kg', '120 kg'],
  annualIncome: [
    'Below 1 Lakh', '1-3 Lakh', '3-5 Lakh', '5-7 Lakh', '7-10 Lakh', '10-15 Lakh', '15-20 Lakh', '20+ Lakh'
  ],
  siblingsCount: ['0', '1', '2', '3', '4', '5', '6+']
};

export const useMatrimonyOptions = () => {
  const [customOptions, setCustomOptions] = useState(() => {
    // Load from localStorage if available
    const saved = localStorage.getItem('matrimonyCustomOptions');
    return saved ? JSON.parse(saved) : {};
  });

  // Save to localStorage whenever customOptions changes
  useEffect(() => {
    localStorage.setItem('matrimonyCustomOptions', JSON.stringify(customOptions));
  }, [customOptions]);

  const getOptions = (fieldType, selectedState = null) => {
    if (fieldType === 'city' && selectedState) {
      const defaults = defaultOptions[fieldType]?.[selectedState] || ['Other'];
      const customs = customOptions[fieldType]?.[selectedState] || [];
      return [...defaults, ...customs];
    }
    
    const defaults = Array.isArray(defaultOptions[fieldType]) 
      ? defaultOptions[fieldType] 
      : [];
    const customs = customOptions[fieldType] || [];
    return [...defaults, ...customs];
  };

  const addCustomOption = (fieldType, newOption) => {
    setCustomOptions(prev => ({
      ...prev,
      [fieldType]: [...(prev[fieldType] || []), newOption]
    }));
  };

  return {
    getOptions,
    addCustomOption,
  };
};
