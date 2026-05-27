import { Heart, User, Search, Settings, LogOut, ChevronDown, Bell, MessageSquare, Star, Menu } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import { useState, useEffect } from "react";
import { useAuth } from "@/hooks/useAuth";
import { useLoading } from "@/hooks/useLoading";
import { useLikeBookmark } from "@/hooks/useLikeBookmark";
import ThemeToggle from "@/components/ThemeToggle";
import ProfileCompletionBar from "@/components/ProfileCompletionBar";
import DashboardStats from "@/components/DashboardStats";
import LikeBookmarkButtons from "@/components/LikeBookmarkButtons";

import { useLanguage } from "@/context/LanguageContext.jsx";
import { useProfileData } from "@/hooks/useProfileData";
import { profileAPI } from "@/services/api";
import profile1 from "@/assets/profile1.jpg";
import success1 from "@/assets/success-couple1.jpg";
import success2 from "@/assets/success-couple2.jpg";
import success3 from "@/assets/success-couple3.jpg";

const successStories = [
  { image: success1, names: "Rahul & Priya", city: "Mumbai", date: "Dec 2025" },
  { image: success2, names: "Vikram & Ananya", city: "Delhi", date: "Nov 2025" },
  { image: success3, names: "Arjun & Meera", city: "Pune", date: "Oct 2025" },
];

const HomeFixed = () => {
  const navigate = useNavigate();
  const { userName, logout } = useAuth();
  const { startLoading, stopLoading } = useLoading();
  const { isLiked, isBookmarked, toggleLike, toggleBookmark } = useLikeBookmark();
  const { t } = useLanguage();
  const { profileData, isLoading: profileLoading } = useProfileData();
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [profiles, setProfiles] = useState([]);
  const [loadingProfiles, setLoadingProfiles] = useState(true);

  // Load real profiles from API
  useEffect(() => {
    loadProfiles();
  }, []);

  const loadProfiles = async () => {
    try {
      setLoadingProfiles(true);
      const data = await profileAPI.getProfiles();
      setProfiles(data.content || []);
    } catch (error) {
      console.warn('Failed to load profiles:', error.message);
      setProfiles([]);
    } finally {
      setLoadingProfiles(false);
    }
  };

  // Use real profile data for completion tracking
const profileCompletion = {
  completionPercentage:
    profileData?.currentStep || 0,

  message:
    (profileData?.currentStep || 0) >= 100
      ? "Profile completed"
      : "Complete your profile"
};

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  useEffect(() => {
    startLoading('Loading dashboard...');
    const timer = setTimeout(() => {
      stopLoading();
    }, 1000);
    return () => clearTimeout(timer);
  }, []);

  return (
    <div className="min-h-screen bg-muted/30 flex">
      {/* Sidebar */}
      <aside className={`hidden md:flex flex-col bg-card border-r border-border min-h-screen sticky top-0 transition-all duration-300 ${isSidebarOpen ? 'w-64' : 'w-20'}`}>
        <div className="p-5 border-b border-border">
          <Link to="/home" className="flex items-center gap-2">
            <Heart className="h-6 w-6 text-primary fill-primary" />
            {isSidebarOpen && <span className="text-xl font-display font-bold text-foreground">Gathbandhan</span>}
          </Link>
        </div>
        <nav className="flex-1 p-4 space-y-1">
          {[
            { icon: <User className="h-4 w-4" />, label: "Dashboard", active: true, to: "/home" },
            { icon: <Heart className="h-4 w-4" />, label: "Matches", to: "/matches" },
            { icon: <Search className="h-4 w-4" />, label: "Search", to: "/search" },
            { icon: <MessageSquare className="h-4 w-4" />, label: "Messages", to: "/messages" },
            { icon: <Star className="h-4 w-4" />, label: "Kundli", to: "/kundli" },
            { icon: <Settings className="h-4 w-4" />, label: "Settings", to: "/settings" },
          ].map((item) => (
            <Link
              key={item.label}
              to={item.to}
              className={`w-full flex items-center px-4 py-2.5 rounded-lg text-sm font-medium transition-colors ${
                isSidebarOpen ? 'gap-3' : 'justify-center'
              } ${
                item.active
                  ? "bg-primary text-primary-foreground"
                  : "text-muted-foreground hover:bg-muted hover:text-foreground"
              }`}
            >
              {item.icon}
              {isSidebarOpen && item.label}
            </Link>
          ))}
        </nav>
        <div className="p-4 border-t border-border">
          <button onClick={handleLogout} className={`w-full flex items-center px-4 py-2.5 rounded-lg text-sm font-medium text-muted-foreground hover:bg-muted hover:text-foreground transition-colors ${
            isSidebarOpen ? 'gap-3' : 'justify-center'
          }`}>
            <LogOut className="h-4 w-4" />
            {isSidebarOpen && 'Logout'}
          </button>
        </div>
      </aside>

      {/* Main content */}
      <div className={`flex-1 flex flex-col min-h-screen transition-all duration-300 ${isSidebarOpen ? 'md:ml-0' : 'md:ml-0'}`}>
        <header className="sticky top-0 z-40 bg-card/95 backdrop-blur border-b border-border px-6 py-3 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <button 
              onClick={() => setIsSidebarOpen(!isSidebarOpen)}
              className="text-foreground hover:text-primary transition-colors"
            >
              <Menu className="h-5 w-5" />
            </button>
            <div>
              <p className="text-muted-foreground text-sm">{t?.home?.header?.welcome}</p>
              <h1 className="text-xl font-display font-bold text-foreground capitalize">
                {profileData?.firstName && profileData?.lastName 
                  ? `${profileData.firstName} ${profileData.lastName}`
                  : profileData?.fullName || userName || "User"}!
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <ThemeToggle />
            <button className="relative text-muted-foreground hover:text-foreground transition-colors">
              <Bell className="h-5 w-5" />
              <span className="absolute -top-1 -right-1 h-4 w-4 bg-primary text-primary-foreground text-[10px] font-bold rounded-full flex items-center justify-center">3</span>
            </button>
            <button
              onClick={() => navigate("/account")}
              className="h-9 w-9 rounded-full bg-accent/20 hover:bg-accent/30 flex items-center justify-center text-accent font-bold text-sm cursor-pointer transition-colors"
              title="Account"
            >
              {profileData?.imageUrl || profileData?.profilePhotoUrl ? (
                <img 
                  src={profileData.imageUrl || profileData.profilePhotoUrl} 
                  alt="Profile" 
                  className="h-9 w-9 rounded-full object-cover"
                  onError={(e) => {
                    e.target.style.display = 'none';
                    e.target.nextSibling.style.display = 'flex';
                  }}
                />
              ) : null}
              <span style={{ display: (profileData?.imageUrl || profileData?.profilePhotoUrl) ? 'none' : 'flex' }}>
                {profileData?.firstName && profileData?.lastName 
                  ? `${profileData.firstName.charAt(0)}${profileData.lastName.charAt(0)}`
                  : (profileData?.fullName || userName || "User").charAt(0).toUpperCase()
                }
              </span>
            </button>
          </div>
        </header>

        {/* Hero banner */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mx-6 mt-6 rounded-2xl p-8 text-primary-foreground relative overflow-hidden"
          style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}
        >
          <Heart className="absolute top-4 right-8 h-8 w-8 text-pink-soft/40 fill-pink-soft/30 animate-float-heart" />
          <h2 className="text-3xl md:text-4xl font-display font-bold mb-2">{t?.home?.hero?.title || "Find Your Perfect Match"}</h2>
          <p className="text-primary-foreground/70 text-sm max-w-md">{t?.home?.hero?.subtitle || "Connect with like-minded individuals seeking meaningful relationships"}</p>
        </motion.div>

        <div className="px-6 py-6 grid grid-cols-1 lg:grid-cols-3 gap-6 pb-24 md:pb-6">
          {/* Left column */}
          <div className="lg:col-span-2 space-y-6">
            {/* Profile Completion */}
            {profileLoading ? (
              <div className="bg-card rounded-xl border border-border p-6">
                <div className="animate-pulse">
                  <div className="h-4 bg-muted rounded w-1/4 mb-2"></div>
                  <div className="h-2 bg-muted rounded w-full"></div>
                </div>
              </div>
            ) : (
              <ProfileCompletionBar
                completionPercentage={profileCompletion.completionPercentage}
                message={profileCompletion.message}
              />
            )}

            {/* Dashboard Stats */}
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-4">{t?.home?.overviewTitle || "Overview"}</h3>
              <DashboardStats />
            </div>

            {/* Real Profiles Section */}
            <div className="mb-8">
              <h2 className="text-2xl font-bold mb-6">Discover Profiles</h2>
              {loadingProfiles ? (
                <div className="text-center py-8">
                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>
                  <p className="text-muted-foreground">Loading profiles...</p>
                </div>
              ) : profiles.length > 0 ? (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {profiles.map((profile, i) => (
                    <motion.div
                      key={profile.id || i}
                      initial={{ opacity: 0, y: 20 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ delay: i * 0.1 }}
                      whileHover={{ scale: 1.02 }}
                      className="bg-card rounded-xl shadow-lg overflow-hidden cursor-pointer"
                      onClick={() => navigate(`/profile/${profile.id}`)}
                    >
                      <div className="aspect-[3/4] overflow-hidden relative">
                        <img 
                          src={profile.profilePhotoUrl || profile.imageUrl || profile1} 
                          alt={profile.fullName || profile.name} 
                          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" 
                          onError={(e) => {
                            e.target.src = profile1;
                          }}
                        />
                      </div>
                      <div className="p-3">
                        <p className="text-sm font-semibold text-foreground">
                          <span className="text-primary">{profile.fullName}</span>, {profile.age || 'Age'}, {profile.city || 'City'}
                        </p>
                        <LikeBookmarkButtons
                          profileId={profile.id || i}
                          isLiked={isLiked(profile.id || i)}
                          isBookmarked={isBookmarked(profile.id || i)}
                          onLike={toggleLike}
                          onBookmark={toggleBookmark}
                          size="sm"
                        />
                      </div>
                    </motion.div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-8">
                  <p className="text-muted-foreground">No profiles found. Be the first to create one!</p>
                  <button
                    onClick={() => navigate('/profile/create')}
                    className="mt-4 bg-primary text-white px-6 py-2 rounded-lg hover:opacity-90 transition"
                  >
                    Create Profile
                  </button>
                </div>
              )}
            </div>

            {/* Success Stories - vertical */}
            <div>
              <h3 className="text-lg font-display font-bold text-foreground mb-4">{t?.home?.successStoriesTitle || "Success Stories"}</h3>
              <div className="space-y-3">
                {successStories.map((s, i) => (
                  <motion.div
                    key={i}
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: i * 0.1 }}
                    className="bg-card rounded-xl border border-border p-4 flex items-center gap-4 hover:shadow-md transition-shadow"
                  >
                    <img src={s.image} alt={s.names} className="h-16 w-16 rounded-lg object-cover flex-shrink-0" />
                    <div>
                      <p className="text-sm font-semibold text-foreground">{s.names}</p>
                      <p className="text-xs text-muted-foreground">{s.city} · {s.date}</p>
                    </div>
                    <Heart className="h-4 w-4 text-primary fill-primary ml-auto flex-shrink-0" />
                  </motion.div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomeFixed;
