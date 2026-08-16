import { Heart, User, Search, Settings, LogOut, ChevronDown, Bell, MessageSquare, Star, Menu } from "lucide-react";
import RecentActivity
from "@/components/RecentActivity";
import { Link, useNavigate } from "react-router-dom";
import HeartAnimation
from "@/components/HeartAnimation";
 import { trackEvent } from "@/utils/analytics";
import useLikes
from "@/hooks/useLikes";
import { swipeAPI } from "@/services/swipeAPI";
import { getConversations } from "@/services/chatApi";
import { motion } from "framer-motion";
import ReportModal from "../components/ReportModal";
import { useState, useEffect,useCallback,useMemo ,useRef  } from "react";
import profile1 from "@/assets/profile1.jpg";


import { useAuth } from "@/hooks/useAuth";

import { useLoading } from "@/hooks/useLoading";



import ThemeToggle from "@/components/ThemeToggle";

import ProfileCompletionBar from "@/components/ProfileCompletionBar";

import DashboardStats from "@/components/DashboardStats";

import LikeBookmarkButtons from "@/components/LikeBookmarkButtons";

import ShortlistButton from "@/components/ShortlistButton";

import toast from "react-hot-toast";

import { useLanguage } from "@/context/LanguageContext.jsx";

import { useProfileData } from "@/hooks/useProfileData";

import {
  profileAPI,
  interestAPI,
  profileVisitorAPI,
  blockAPI,
  reportAPI,
  subscriptionAPI,
  matchAPI,
  dashboardAPI
} from "@/services/api";

import {

shortlistAPI

}

from "@/services/shortlistAPI";




const HomeFixed = () => {

const navigate = useNavigate();
const {
  isLiked,
  toggleLike,
  loading: likesLoading
} = useLikes();
const [page, setPage] = useState(0);
const [dashboardRefreshing, setDashboardRefreshing] = useState(false);
const [hasMore, setHasMore] = useState(true);

const [loadingMore, setLoadingMore] = useState(false);
const [showReportModal, setShowReportModal] = useState(false);
const [reportedUsers, setReportedUsers] = useState({});

const [selectedProfile, setSelectedProfile] = useState(null);
const [selectedReason, setSelectedReason] = useState("");
const [customReason, setCustomReason] = useState("");
const [blockedUsers, setBlockedUsers] = useState([]);
const [showUpgradePopup, setShowUpgradePopup] = useState(false);
const [premiumFeature, setPremiumFeature] = useState("Chat");
const { userName, logout } = useAuth();
const loadMoreRef = useRef(null);
const loadingMoreRef = useRef(false);
const currentPageRef = useRef(0);
const loadingStartedAtRef = useRef(0);
const hideLoaderAfterAppendRef = useRef(false);

const { startLoading, stopLoading } =
useLoading();



const { t } = useLanguage();

const {
profileData,
isLoading: profileLoading
} = useProfileData();

const [isSidebarOpen,setIsSidebarOpen] =
useState(true);

const [profiles,setProfiles] =
useState([]);
const [dashboardStats, setDashboardStats] = useState(null);

const [visitors, setVisitors] = useState([]);
const [receivedInterests, setReceivedInterests] = useState([]);
const [shortlists, setShortlists] = useState([]);
const [sentInterests, setSentInterests] = useState([]);
const [activityLoading, setActivityLoading] = useState(true);

const loadRecentActivity = useCallback(async () => {
    try {
        setActivityLoading(true);
        const currentUserStr = localStorage.getItem("user");
        if (!currentUserStr) {
            setActivityLoading(false);
            return;
        }

        const currentUser = JSON.parse(currentUserStr);
        const userId = Number(
            currentUser?.userId ||
            currentUser?.id ||
            currentUser?.profile?.userId
        );

        if (!userId) {
            setActivityLoading(false);
            return;
        }

        const [visitorsRes, receivedRes, sentRes, shortlistsRes] = await Promise.allSettled([
            profileVisitorAPI.getMyVisitors(),
            interestAPI.getReceivedInterests(userId),
            interestAPI.getSentInterests(userId),
            shortlistAPI.getMyShortlists(0, 10)
        ]);

        if (visitorsRes.status === "fulfilled" && visitorsRes.value) {
            const raw = Array.isArray(visitorsRes.value)
                ? visitorsRes.value
                : visitorsRes.value?.data || [];
            setVisitors(raw);
        }

        if (receivedRes.status === "fulfilled" && receivedRes.value) {
            const raw = Array.isArray(receivedRes.value)
                ? receivedRes.value
                : receivedRes.value?.data || [];
            setReceivedInterests(raw);
        }

        if (sentRes.status === "fulfilled" && sentRes.value) {
            const raw = Array.isArray(sentRes.value)
                ? sentRes.value
                : sentRes.value?.data || [];
            setSentInterests(raw);
        }

        if (shortlistsRes.status === "fulfilled" && shortlistsRes.value) {
            const raw = Array.isArray(shortlistsRes.value)
                ? shortlistsRes.value
                : shortlistsRes.value?.content || shortlistsRes.value?.data || [];
            setShortlists(raw);
        }
    } catch (err) {
        console.error("Recent Activity load error:", err);
    } finally {
        setActivityLoading(false);
    }
}, []);

const refreshDashboard = useCallback(async () => {
    console.log("REFRESH DASHBOARD START");

    if (dashboardRefreshing) return;

    setDashboardRefreshing(true);

    try {
        const summary = await dashboardAPI.getSummary();
        setDashboardStats(summary);
    } catch (error) {
        console.log("STEP ERROR", error);
    } finally {
        setDashboardRefreshing(false);
    }

}, [dashboardRefreshing]);

useEffect(() => {
    const handleDashboardUpdated = () => {
        console.log("🔄 Dashboard update event received");
        refreshDashboard();
        loadRecentActivity();
    };

    const handleActivityOnlyUpdate = () => {
        loadRecentActivity();
    };

    window.addEventListener("dashboardUpdated", handleDashboardUpdated);
    window.addEventListener("interestUpdated", handleActivityOnlyUpdate);
    window.addEventListener("shortlist:updated", handleActivityOnlyUpdate);
    window.addEventListener("visitorUpdated", handleActivityOnlyUpdate);

    return () => {
        window.removeEventListener("dashboardUpdated", handleDashboardUpdated);
        window.removeEventListener("interestUpdated", handleActivityOnlyUpdate);
        window.removeEventListener("shortlist:updated", handleActivityOnlyUpdate);
        window.removeEventListener("visitorUpdated", handleActivityOnlyUpdate);
    };
}, [refreshDashboard, loadRecentActivity]);




const [loadingProfiles,setLoadingProfiles] =
useState(true);
const [showProfilePopup, setShowProfilePopup] = useState(false);
useEffect(() => {
  if (profileLoading || !profileData) {
    setShowProfilePopup(false);
    return;
  }

  const isCompleted = Boolean(
    profileData?.profileCompleted ||
    (profileData?.profileCompletionPercentage >= 80)
  );

  if (!isCompleted) {
    setShowProfilePopup(true);
  } else {
    setShowProfilePopup(false);
  }
}, [profileData, profileLoading]);

const [showHeart,setShowHeart] =
useState(null);


const calculateAge = (dob) => {

 if (!dob) return "Age";

 const birthDate = new Date(dob);

 const today = new Date();

 let age =
 today.getFullYear() -
 birthDate.getFullYear();

 const monthDiff =
 today.getMonth() -
 birthDate.getMonth();

 if(
   monthDiff < 0 ||
   (
    monthDiff===0 &&
    today.getDate() <
    birthDate.getDate()
   )
 ){

   age--;

 }

 return age;

};


  // Load real profiles from API
// Load real profiles from API
const [initialized, setInitialized] = useState(false);


const PAGE_SIZE = 20;

const loadProfiles = useCallback(
    async (pageNumber = 0, append = false) => {

        if (append) {

            if (loadingMoreRef.current) {
                return;
            }

            loadingMoreRef.current = true;
            hideLoaderAfterAppendRef.current = false;
            setLoadingMore(true);

        } else {

            startLoading("Loading dashboard...");
            setLoadingProfiles(true);
        }

        try {

            const currentUser = JSON.parse(
                localStorage.getItem("user") || "{}"
            );

            const userId = Number(
                currentUser?.profile?.userId ||
                currentUser?.userId ||
                currentUser?.id
            );

            if (!userId) {
                throw new Error(
                    "Current user not found"
                );
            }


            // =============================================
            // Load blocked users only on first page
            // =============================================

            let currentBlockedUsers =
                blockedUsers;

            if (pageNumber === 0) {

                currentBlockedUsers =
                    await blockAPI
                        .getMyBlockedUsers(userId);

                setBlockedUsers(
                    currentBlockedUsers
                );
            }


            const blockedIds =
                currentBlockedUsers.map(
                    u => u.blockedId
                );


            // =============================================
            // Load profiles
            // =============================================

            console.time(
                `MATCH API page ${pageNumber}`
            );

            const data =
                await matchAPI.getTopMatches(
                    userId,
                    pageNumber,
                    PAGE_SIZE
                );
            if (append) {
                currentPageRef.current = pageNumber;
            }

            console.timeEnd(
                `MATCH API page ${pageNumber}`
            );


            // =============================================
            // Filter blocked
            // =============================================

            const filteredProfiles =
                data.filter(
                    profile =>
                        !blockedIds.includes(
                            profile.userId
                        )
                );


           // =============================================
           // Determine whether more exists
           // =============================================

           if (data.length === 0) {
               setHasMore(false);
           } else {
               setHasMore(true);
           }


           // =============================================
           // Append profiles
           // =============================================

           setProfiles(prev => {

               if (!append) {
                   return filteredProfiles;
               }

               const existingIds = new Set(
                   prev.map(
                       profile => profile.userId
                   )
               );

               const uniqueProfiles =
                   filteredProfiles.filter(
                       profile =>
                           !existingIds.has(
                               profile.userId
                           )
                   );

               return [
                   ...prev,
                   ...uniqueProfiles
               ];
           });
              } catch (error) {

                  console.error(
                      "Failed to load profiles:",
                      error
                  );

              } finally {

                  if (append) {

                      loadingMoreRef.current = false;
                      setLoadingMore(false);

                  } else {

                      setLoadingProfiles(false);
                      stopLoading();

                  }
              }
    },
    [
        blockedUsers,
        startLoading,
        stopLoading
    ]
);
useEffect(() => {

    if (!hideLoaderAfterAppendRef.current) {
        return;
    }

    hideLoaderAfterAppendRef.current = false;
    loadingMoreRef.current = false;

    setLoadingMore(false);

}, [profiles.length]);
const loadInitialData = useCallback(async () => {
     currentPageRef.current = 0;
     Promise.allSettled([
         loadProfiles(0, false),
         refreshDashboard(),
         loadRecentActivity()
     ]);
 }, [loadProfiles, refreshDashboard, loadRecentActivity]);

useEffect(() => {
    if (initialized) return;

    setInitialized(true);
    loadInitialData();
}, [initialized, loadInitialData]);

useEffect(() => {

    const element = loadMoreRef.current;

    if (!element) {
        return;
    }

    const observer =
        new IntersectionObserver(
            entries => {

                const firstEntry =
                    entries[0];

                if (
                    !firstEntry.isIntersecting ||
                    loadingMoreRef.current ||
                    !hasMore ||
                    loadingProfiles
                ) {
                    return;
                }

                const nextPage =
                    currentPageRef.current + 1;

                loadProfiles(
                    nextPage,
                    true
                );

            },
            {
                root: null,

                // Load when user is reasonably close to bottom
                rootMargin: "100px 0px",

                threshold: 0
            }
        );

    observer.observe(element);

    return () => {
        observer.disconnect();
    };

}, [
    loadProfiles,
    hasMore,
    loadingProfiles
]);

  // Use real profile data for completion tracking
const profileCompletion = {
  completionPercentage:
      profileData?.profileCompletionPercentage || 0,

 message:
 (profileData?.currentStep || 0) >= 100
   ? "Profile completed successfully"
   : "Click here to complete your profile"
};



 const handleLogout = () => {
     trackEvent("user_logout");

     logout();

     navigate("/login");
 };
const handleSendInterest =
async(profile)=>{

try{

const currentUser = JSON.parse(
    localStorage.getItem("user") || "{}"
);

const senderId = Number(
    currentUser?.userId ||
    currentUser?.id ||
    currentUser?.profile?.userId
);

if (!senderId) {
    toast.error("Current user not found");
    return;
}

const receiverId =
Number(
profile.userId
);



if(
senderId === receiverId
){

toast.error(
"You cannot send interest to yourself"
);

return;

}
await interestAPI.sendInterest(
    senderId,
    receiverId
);

setSentInterests(prev => [
    ...prev,
    receiverId
]);

window.dispatchEvent(
    new Event("dashboardUpdated")
);

toast.success(
    "Interest Sent Successfully ❤️"
);
}catch(err){



if(

err?.message?.includes(

"Daily limit reached"

)

){

toast.error(

"Daily limit reached.\nUpgrade to Premium for unlimited interests."

);

return;

}

toast.error(

err?.message ||

"Failed"

);

}

};



return (

    <>
    {showProfilePopup && (
      <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-[9999]">

        <div className="bg-white rounded-2xl p-8 max-w-md w-full mx-4 shadow-2xl">

          <div className="text-center">

            <div className="text-6xl mb-4">
              ⚠️
            </div>

            <h2 className="text-2xl font-bold mb-3">
              Complete Your Profile
            </h2>

        <p className="text-gray-600 mb-6">
          Please complete your profile to continue.
          You are not allowed to access this feature until your profile is completed.
        </p>

            <div className="flex gap-3 justify-center">

              <button
                className="bg-pink-600 text-white px-6 py-3 rounded-xl"
                onClick={() =>
                  navigate("/settings")
                }
              >
                Complete Profile
              </button>

              <button
                className="border px-6 py-3 rounded-xl"
                onClick={() =>
                  setShowProfilePopup(false)
                }
              >
                Later
              </button>

            </div>

          </div>

        </div>

      </div>
    )}
{showUpgradePopup && (

<div className="fixed inset-0 bg-black/50 flex items-center justify-center z-[99999]">

    <div className="bg-white rounded-3xl p-8 w-[420px] text-center">

        <div className="text-6xl mb-4">
            👑
        </div>

        <h2 className="text-2xl font-bold mb-3">
            Premium Required
        </h2>

        <p className="text-gray-600 mb-6">
            {premiumFeature} is available only for Premium members.
            Upgrade your plan to continue.
        </p>

        <div className="flex justify-center gap-3">

            <button
                onClick={() => {
                    setShowUpgradePopup(false);
                }}
                className="px-5 py-2 rounded-xl bg-gray-200"
            >
                Home
            </button>

            <button
                onClick={() => {
                    setShowUpgradePopup(false);
                    navigate("/upgrade");
                }}
                className="px-5 py-2 rounded-xl bg-pink-600 text-white"
            >
                Upgrade Premium
            </button>

        </div>

    </div>

</div>

)}

    <div className="h-screen bg-muted/30 flex overflow-hidden">
      {/* Sidebar */}
      <aside
          className={`
              hidden
              md:flex
              flex-col
              bg-card
              border-r
              border-border
              h-full
              shrink-0
              transition-all
              duration-300
              ${isSidebarOpen ? "w-64" : "w-20"}
          `}
      >
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
              to={
                (profileData?.profileCompleted || (profileData?.profileCompletionPercentage >= 80))
                  ? item.to
                  : item.label === "Dashboard"
                  ? "/home"
                  : "#"
              }
             onClick={async (e) => {
                 const isCompleted = Boolean(
                   profileData?.profileCompleted ||
                   (profileData?.profileCompletionPercentage >= 80)
                 );
                 // Profile completion check
                 if (
                     !isCompleted &&
                     item.label !== "Dashboard" &&
                     item.label !== "Settings"
                 ) {
                     e.preventDefault();
                     setShowProfilePopup(true);
                     return;
                 }

                 // Premium check for Messages & Matches
                 if (
                     item.label === "Messages" ||
                     item.label === "Matches"
                 ) {

                     e.preventDefault();

                     try {

                         const subscription =
                             await subscriptionAPI.getMySubscription();

                         if (subscription?.isActive) {

                             navigate(item.to);

                         } else {

                             setPremiumFeature(item.label);
                             setShowUpgradePopup(true);

                         }

                     }catch (error) {

                          setPremiumFeature(item.label);
                          setShowUpgradePopup(true);

                      }
                     return;

                 }

             }}              className={`w-full flex items-center px-4 py-2.5 rounded-lg text-sm font-medium transition-colors ${
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
      <div
          className="
              flex-1
              min-w-0
              h-full
              overflow-y-auto
              overflow-x-hidden
              flex
              flex-col
          "
      >
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

            <button
              onClick={() => navigate("/account")}
              className="h-9 w-9 rounded-full bg-accent/20 hover:bg-accent/30 flex items-center justify-center text-accent font-bold text-sm cursor-pointer transition-colors"
              title="Account"
            >
              {
              (profileData?.imageUrl || profileData?.profilePhotoUrl) ? (
              <img
                src={
                  profileData.imageUrl ||
                  profileData.profilePhotoUrl
                }

                alt="Profile"

                className="
                h-9
                w-9
                rounded-full
                object-cover
                "

                onError={(e)=>{

                  e.target.style.display =
                  "none";

                  e.target.parentElement
                  .querySelector(
                    ".profile-initials"
                  )
                  .style.display="flex";

                }}

              />

              ) : null
              }

              <span

              className="
              profile-initials
              flex
              items-center
              justify-center
              "

              style={{

              display:
              (profileData?.imageUrl ||
              profileData?.profilePhotoUrl)

              ? "none"

              : "flex"

              }}

              >

              {
              profileData?.firstName &&
              profileData?.lastName

              ?

              `${profileData.firstName[0]}${profileData.lastName[0]}`

              :

              (profileData?.fullName ||
              userName ||
              "U")

              .charAt(0)

              .toUpperCase()

              }

              </span>
</button>

</div>

</header>
        {/* Hero banner */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mx-6 mt-6 rounded-2xl px-8 py-7 min-h-[140px] shrink-0 flex flex-col justify-center text-primary-foreground relative overflow-hidden"
          style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}
        >
          <Heart className="absolute top-4 right-8 h-8 w-8 text-pink-soft/40 fill-pink-soft/30 animate-float-heart" />
          <h2 className="text-3xl md:text-4xl font-display font-bold mb-2">{t?.home?.hero?.title || "Find Your Perfect Match"}</h2>
          <p className="text-primary-foreground/70 text-sm max-w-md">{t?.home?.hero?.subtitle || "Connect with like-minded individuals seeking meaningful relationships"}</p>
        </motion.div>

        <div className="px-6 py-6 pb-24 md:pb-6">
          {/* Left column */}
          <div className="lg:col-span-3 space-y-6">
            {/* Profile Completion */}
          {profileLoading ? (

          <div className="
bg-card
rounded-2xl
border
border-border
p-8
">

          <div className="animate-pulse">

          <div className="h-4 bg-muted rounded w-1/4 mb-2"></div>

          <div className="h-2 bg-muted rounded w-full"></div>

          </div>

          </div>

          ) : (

          <ProfileCompletionBar

          completionPercentage={
          profileCompletion.completionPercentage
          }

          message={
          profileCompletion.message
          }

          />

          )}

            {/* Dashboard Stats */}
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-4">{t?.home?.overviewTitle || "Overview"}</h3>
         <DashboardStats stats={dashboardStats} />
                     </div>


<RecentActivity
  visitors={visitors}
  receivedInterests={receivedInterests}
  shortlists={shortlists}
  sentInterests={sentInterests}
  loading={activityLoading}
/>
            {/* Real Profiles Section */}
            <div className="mb-8">
              <h2 className="text-2xl font-bold mb-6">Discover Profiles</h2>
              {loadingProfiles ? (
                <div className="text-center py-8">
                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>
                  <p className="text-muted-foreground">Loading profiles...</p>
                </div>
              ) : profiles.length > 0 ? (
                  <>
                    <div className="
                        grid
                        grid-cols-1
                        sm:grid-cols-2
                        md:grid-cols-3
                        lg:grid-cols-4
                        xl:grid-cols-5
                        gap-5
                        w-full
                    ">
                  {profiles.map((profile, i) => (

                    <motion.div
                      key={profile.profileId || i}
                      initial={{ opacity: 0, y: 20 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ delay: i * 0.1 }}
                      whileHover={{ scale: 1.02 }}
                     className="
                     bg-white
                     rounded-2xl
                     shadow-lg
                     overflow-hidden
                     flex
                     flex-col
                     h-full
                     "
onClick={(e)=>{

if(

e.target.closest(

"button"

)

){

return;

}

setTimeout(()=>{

if(

e.detail===1

){

navigate(`/profile/${profile.profileId}`);

}

},220);

}}

>

<div

className="

relative

h-[180px]


"
onDoubleClick={async (e) => {
  e.stopPropagation();

  if (likesLoading) {
    toast("Loading likes...");
    return;
  }

  try {
    const likedBefore = isLiked(profile.userId);

    if (!likedBefore) {
      setShowHeart(profile.userId);
    }

    await toggleLike(profile.userId);

    if (likedBefore) {
      toast("Like removed");
    } else {
      toast.success("Liked ❤️");
    }
  } catch (error) {

    toast.error("Failed to update like");
  }
}}
>

<HeartAnimation
  show={showHeart===profile.userId}
  onComplete={()=>{
    setShowHeart(null);
  }}
/>

{
  profile.imageUrl ? (
    <>
      <img
        src={
          profile.imageUrl.startsWith("http")
            ? profile.imageUrl
            : `http://localhost:9090${profile.imageUrl}`
        }
        alt={profile.name || "Profile"}
        className="w-full h-full object-cover"
        onError={(e) => {
          e.currentTarget.onerror = null;
          e.currentTarget.src = profile1;
        }}
      />

      <div className="absolute bottom-3 left-3 bg-white/90 px-3 py-1 rounded-full text-sm font-medium shadow">
        ❤️ {profile.matchPercentage || 0} Match
      </div>
    </>
  ) : (
    <div className="w-full h-full flex items-center justify-center bg-gray-100 text-gray-400">
      No Image
    </div>
  )
}
</div>
                  <div className="p-4 flex flex-col flex-1">
                  <h3 className="text-lg font-semibold leading-tight">
                    {profile.name || "Unknown User"}
                    <span className="text-pink-600">
                      {profile.age ? ` ${profile.age}yrs` : ""}
                    </span>

                    {profile.isPremium && (
                      <span className="ml-2 text-yellow-500">👑</span>
                    )}
                  </h3>

                   <p className="text-gray-600 mt-0.5">
                     {profile.occupation || "Profession not specified"} • {profile.city || "Location not specified"}
                   </p>
                   <div className="flex-1"></div>

                  <div className="mt-3 flex flex-col gap-2">

                  <button

           disabled={
             sentInterests.includes(
               profile.userId
             )
           }

                  className="
                  w-full
                  bg-[#E94057]
                  disabled:opacity-70
                  text-white
                  py-2
                  rounded-xl
                  font-semibold
                  shadow-md
                  transition
                  "
     onClick={() => {

       if (
         !(profileData?.profileCompleted || (profileData?.profileCompletionPercentage >= 80))
       ) {

         setShowProfilePopup(true);

         return;
       }



       handleSendInterest(
         profile
       );

     }}


                  >

              {

              sentInterests.includes(

              profile.userId

              )

              ?

              "❤️ Interest Sent"

              :

              "💌 Send Interest"

              }

                  </button>

                 <button

                 className="
                 w-full
                 bg-[#F8F9FA]
                 border
                 border-[#E9ECEF]
                 text-[#343A40]
                 py-2
                 rounded-xl
                 font-semibold
                 shadow-sm
                 hover:bg-white
                 transition
                 "

                 onClick={(e) => {

                   e.stopPropagation();

                   if (
                     !(profileData?.profileCompleted || (profileData?.profileCompletionPercentage >= 80))
                   ) {

                     setShowProfilePopup(true);

                     return;
                   }

                   navigate(
                     `/profile/${profile.profileId}`
                   );

                 }}

                 >

                 👤 View Profile

                 </button>
                   </div>
<div className="mt-3 flex justify-center gap-3">

<button
  title="Like"
  disabled={likesLoading}
  onClick={async (e) => {
    e.stopPropagation();

    if (likesLoading) {
      toast("Loading likes...");
      return;
    }

    try {
      const likedBefore = isLiked(profile.userId);

      if (!likedBefore) {
        setShowHeart(profile.userId);
      }

      await toggleLike(profile.userId);

      if (likedBefore) {
        toast("Like removed");
      } else {
        toast.success("Liked ❤️");
      }
    } catch (error) {
      console.error("Like update failed:", error);
      toast.error("Failed to update like");
    }
  }}
  className="
    group
    w-12
    h-12
    rounded-full
    bg-gradient-to-br
    from-pink-500
    to-rose-600
    shadow-lg
    hover:scale-125
    active:scale-95
    transition-all
    duration-300
    disabled:opacity-50
    disabled:cursor-not-allowed
    flex
    items-center
    justify-center
  "
>
  <span
    className={`
      text-2xl
      transition-all
      duration-300
      ${isLiked(profile.userId) ? "scale-125" : ""}
    `}
  >
    {isLiked(profile.userId) ? "❤️" : "🤍"}
  </span>
</button>
<div
className="
group
w-12
h-12
rounded-full
bg-white
border
border-amber-200
shadow-lg
hover:scale-125
active:scale-95
transition-all
duration-300
flex
items-center
justify-center
"
>

<ShortlistButton
profileId={profile.profileId || i}
size="sm"
showLabel={false}
/>

</div>
<button
  title={
    blockedUsers.includes(profile.userId)
      ? "Unblock User"
      : "Block User"
  }
  onClick={async (e) => {
    e.stopPropagation();

    try {
      const currentUser = JSON.parse(
        localStorage.getItem("user") || "{}"
      );

      const blockerId = Number(
        currentUser?.profile?.userId ||
        currentUser?.userId ||
        currentUser?.id
      );

      const blockedId = Number(profile.userId);

      if (blockedUsers.includes(blockedId)) {

        await blockAPI.unblockUser(
          blockerId,
          blockedId
        );

        toast.success("User unblocked");

        setBlockedUsers(prev =>
          prev.filter(id => id !== blockedId)
        );

      } else {

        const confirmBlock = window.confirm(
          "Are you sure you want to block this user?"
        );

        if (!confirmBlock) return;

        await blockAPI.blockUser(
          blockerId,
          blockedId
        );

        toast.success("User blocked");

        setBlockedUsers(prev => [
          ...prev,
          blockedId
        ]);

        setProfiles(prev =>
          prev.filter(
            p => p.userId !== blockedId
          )
        );
      }

    } catch (err) {

      console.error(err);

      toast.error(
        err.message || "Operation failed"
      );

    }
  }}
  className="
    group
    w-12
    h-12
    rounded-full
    bg-red-100
    border
    border-red-200
    shadow-lg
    hover:scale-125
    transition-all
    duration-300
    flex
    items-center
    justify-center
  "
>
  {blockedUsers.includes(profile.userId)
    ? "🔓"
    : "🚫"}
</button>

<button
  title={
    reportedUsers[profile.userId]
      ? "Already Reported"
      : "Report User"
  }
  disabled={reportedUsers[profile.userId]}
  onClick={(e) => {

    e.stopPropagation();

    if (reportedUsers[profile.userId]) {
      return;
    }

    setSelectedProfile(profile);
    setShowReportModal(true);

  }}
  className={`
    group
    w-12
    h-12
    rounded-full
    border
    shadow-lg
    transition-all
    duration-300
    flex
    items-center
    justify-center
    text-xl

    ${
      reportedUsers[profile.userId]
        ? "bg-gray-200 border-gray-300 cursor-not-allowed opacity-70"
        : "bg-orange-100 border-orange-200 hover:scale-125 active:scale-95"
    }
  `}
>
  {reportedUsers[profile.userId] ? "✔️" : "⚠️"}
</button>
</div>
                   </div>
                    </motion.div>
                                   ))}
                                 </div>


{/* Loading More Profiles */}
{loadingMore && (
    <div className="w-full flex justify-center items-center py-8">
        <div className="flex items-center gap-3 text-muted-foreground">

            <div
                className="
                    animate-spin
                    rounded-full
                    h-6
                    w-6
                    border-2
                    border-primary
                    border-t-transparent
                "
            />

            <span className="text-sm font-medium">
                Loading more profiles...
            </span>

        </div>
    </div>
)}

{/* Load More Sentinel */}
{hasMore && (
    <div
        ref={loadMoreRef}
        className="h-4 w-full"
        aria-hidden="true"
    />
)}

                                 {!hasMore && profiles.length > 0 && (
                                   <div className="text-center py-8">
                                     <p className="text-sm text-muted-foreground">
                                       You've reached the end of available profiles.
                                     </p>
                                   </div>
                                 )}
                                 </>
                               ) : (
              <div className="text-center py-8">

                <p className="text-muted-foreground">

                  No profiles found. Be the first to create one!

                </p>

                <button

                  onClick={() => navigate('/profile/create')}

                  className="
                  mt-4
                  bg-primary
                  text-white
                  px-6
                  py-2
                  rounded-lg
                  hover:opacity-90
                  transition
                  "

                >

                  Create Profile

                </button>

              </div>
              )}

            </div>

          </div>

        </div>


    </div>
        </div>

        <ReportModal
          open={showReportModal}
          onClose={() => {
            setShowReportModal(false);
            setSelectedReason("");
            setCustomReason("");
          }}
          selectedReason={selectedReason}
          setSelectedReason={setSelectedReason}
          customReason={customReason}
          setCustomReason={setCustomReason}
         onSubmit={async () => {

           try {

             if (!selectedReason) {

               toast.error("Please select a reason");

               return;

             }

             const finalReason =
               selectedReason === "Other"
                 ? customReason
                 : selectedReason;

             if (
               selectedReason === "Other" &&
               !customReason.trim()
             ) {

               toast.error(
                 "Please enter a reason"
               );

               return;

             }

             const result =
               await reportAPI.reportUser(
                 selectedProfile.userId,
                 finalReason
               );

             toast.success(
               result || "User reported successfully"
             );
setReportedUsers(prev => ({
    ...prev,
    [selectedProfile.userId]: true
}));
             setShowReportModal(false);

             setSelectedReason("");

             setCustomReason("");

             setSelectedProfile(null);

           } catch (err) {

             toast.error(
               err.message || "Failed to report user"
             );

           }

         }}
        />


    </>
  );
};

export default HomeFixed;