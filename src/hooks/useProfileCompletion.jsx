export const useProfileCompletion = (profileData = {}) => {
  const requiredFields = [
    'fullName',
    'gender',
    'dateOfBirth',
    'religion',
    'maritalStatus',
    'highestEducation',
    'profession',
    'city',
    'profilePhotoUrl',
    'aboutMe',
  ];

  const completedFields = requiredFields.filter(field => {
    const value = profileData[field];
    return value && String(value).trim() !== '';
  });

  const completionPercentage = Math.round((completedFields.length / requiredFields.length) * 100);

  const getMissingFields = () => {
    return requiredFields.filter(field => {
      const value = profileData[field];
      return !value || String(value).trim() === '';
    });
  };

  const getCompletionMessage = () => {
    if (completionPercentage === 100) {
      return '🎉 Profile Complete! You are all set to find your perfect match.';
    } else if (completionPercentage >= 80) {
      return '✨ Almost there! Complete your profile to get better matches.';
    } else if (completionPercentage >= 50) {
      return '📝 Complete your profile to unlock better recommendations.';
    } else {
      return '👤 Fill in more details to complete your profile.';
    }
  };

  return {
    completionPercentage,
    completedFields,
    missingFields: getMissingFields(),
    message: getCompletionMessage(),
    isComplete: completionPercentage === 100,
  };
};