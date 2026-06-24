import { Heart, Bookmark, User } from "lucide-react";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";
import { useState } from "react";

// Default fallback image
import defaultProfile from "@/assets/profile1.jpg";

const ProfileCard = ({ profile, onSendInterest, onSave, isSaved = false, isInterestSent = false }) => {
  const [imageError, setImageError] = useState(false);
  const [saved, setSaved] = useState(isSaved);
  const [interestSent, setInterestSent] = useState(isInterestSent);

  // Calculate age from dateOfBirth
  const calculateAge = (dob) => {
    if (!dob) return "N/A";
    
    const birthDate = new Date(dob);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    
    return age;
  };

  // Handle image error
  const handleImageError = () => {
    setImageError(true);
  };

  // Get profile image URL
  const getImageUrl = () => {
    if (imageError) return defaultProfile;
    if (profile.imageUrl) {
      return profile.imageUrl.startsWith('http') ? profile.imageUrl : `http://localhost:9090${profile.imageUrl}`;
    }
    if (profile.profilePhotoUrl) {
      return profile.profilePhotoUrl.startsWith('http') ? profile.profilePhotoUrl : `http://localhost:9090${profile.profilePhotoUrl}`;
    }
    return defaultProfile;
  };

  // Get display name
  const getDisplayName = () => {
    if (profile.userName) return profile.userName;
    if (profile.fullName) return profile.fullName;
    if (profile.firstName && profile.lastName) return `${profile.firstName} ${profile.lastName}`;
    return "Unknown User";
  };

  // Get city name with fallback
  const getCityName = () => {
    return profile.cityName || profile.city || "City not available";
  };

  // Get profession/education
  const getProfession = () => {
    return profile.occupationName || profile.educationLevelName || profile.occupation || profile.education || "";
  };

  // Handle save/shortlist
  const handleSave = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setSaved(!saved);
    if (onSave) onSave(profile);
  };

  // Handle send interest
  const handleSendInterest = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (!interestSent) {
      setInterestSent(true);
      if (onSendInterest) onSendInterest(profile);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      whileHover={{ y: -4 }}
      className="bg-white rounded-2xl shadow-md overflow-hidden hover:shadow-xl transition-all duration-300 h-full flex flex-col"
    >
      {/* Profile Image */}
      <div className="aspect-[4/5] overflow-hidden relative bg-muted">
        <img
          src={getImageUrl()}
          alt={getDisplayName()}
          className="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
          onError={handleImageError}
        />
        
        {/* Save/Shortlist Button */}
        <button
          onClick={handleSave}
          className="absolute top-3 right-3 h-9 w-9 flex items-center justify-center rounded-full bg-white/90 backdrop-blur-sm shadow-md hover:bg-white transition-colors z-10"
          title={saved ? "Remove from shortlist" : "Save to shortlist"}
        >
          <Heart
            className={`h-5 w-5 transition-colors ${
              saved ? "fill-red-500 text-red-500" : "text-gray-600"
            }`}
          />
        </button>
      </div>

      {/* Card Content */}
      <div className="p-4 flex-1 flex flex-col">
        {/* Name */}
        <h2 className="text-lg font-bold text-foreground mb-1 truncate">
          {getDisplayName()}
        </h2>

        {/* Age + City */}
        <p className="text-sm text-muted-foreground mb-1">
          {calculateAge(profile.dateOfBirth)} yrs • {getCityName()}
        </p>

        {/* Profession/Education */}
        {getProfession() && (
          <p className="text-sm text-muted-foreground mb-4 truncate">
            {getProfession()}
          </p>
        )}

        {/* Spacer to push buttons to bottom */}
        <div className="flex-1"></div>

        {/* Buttons */}
        <div className="flex gap-2 mt-auto">
          {/* View Profile */}
          <Link
            to={`/profile/${profile.id}`}
            className="flex-1 flex items-center justify-center gap-2 bg-primary hover:bg-primary/90 text-white text-sm font-semibold py-2.5 rounded-lg transition-colors"
          >
            <User className="h-4 w-4" />
            View
          </Link>

          {/* Send Interest */}
          <button
            onClick={handleSendInterest}
            disabled={interestSent}
            className={`flex-1 flex items-center justify-center gap-2 text-sm font-semibold py-2.5 rounded-lg transition-colors ${
              interestSent
                ? "bg-green-100 text-green-600 cursor-default"
                : "bg-pink-100 text-pink-600 hover:bg-pink-200"
            }`}
          >
            <Heart className={`h-4 w-4 ${interestSent ? "fill-current" : ""}`} />
            {interestSent ? "Sent" : "Interest"}
          </button>
        </div>
      </div>
    </motion.div>
  );
};

export default ProfileCard;
