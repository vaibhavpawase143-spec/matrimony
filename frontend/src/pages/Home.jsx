import { Heart, User, Search, Settings, LogOut, ChevronDown, Bell, MessageSquare, Star, Menu } from "lucide-react";
import RecentActivity
from "@/components/RecentActivity";
import { Link, useNavigate } from "react-router-dom";
import HeartAnimation
from "@/components/HeartAnimation";

import useLikes
from "@/hooks/useLikes";
import { swipeAPI } from "@/services/swipeAPI";
import { getConversations } from "@/services/chatApi";
import { motion } from "framer-motion";
import ReportModal from "../components/ReportModal";
import { useState, useEffect,useCallback  } from "react";


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
  matchAPI
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

const [showReportModal, setShowReportModal] = useState(false);
const [reportedUsers, setReportedUsers] = useState({});
console.log("showReportModal =", showReportModal);
const [selectedProfile, setSelectedProfile] = useState(null);
const [selectedReason, setSelectedReason] = useState("");
const [customReason, setCustomReason] = useState("");
const [blockedUsers, setBlockedUsers] = useState([]);
const [showUpgradePopup, setShowUpgradePopup] = useState(false);
const [premiumFeature, setPremiumFeature] = useState("Chat");
const { userName, logout } = useAuth();

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
const [dashboardStats, setDashboardStats] = useState({
  totalMatches: 0,
  interestsSent: 0,
  interestsReceived: 0,
  shortlists: 0,
  profileViews: 0,
  likesReceived: 0,
  messages: 0
});

const [visitors, setVisitors] = useState([]);
const [receivedInterests, setReceivedInterests] = useState([]);
const [shortlists, setShortlists] = useState([]);
const [sentInterests, setSentInterests] = useState([]);


const refreshDashboard = useCallback(async () => {
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
      console.warn("Dashboard: userId not found");
      return;
    }

   const [
     visitorsResponse,
     receivedResponse,
     sentResponse,
     shortlistResponse,
     conversationsResponse,
     likesResponse
   ] = await Promise.allSettled([
     profileVisitorAPI.getMyVisitors(),
     interestAPI.getReceivedInterests(userId),
     interestAPI.getSentInterests(userId),
     shortlistAPI.getMyShortlists(),
     getConversations(),
     swipeAPI.getReceivedLikes()
   ]);
    const safeArray = (response) => {
      if (response.status !== "fulfilled") {
        console.warn("Dashboard API failed:", response.reason);
        return [];
      }

      const data = response.value;

      if (Array.isArray(data)) {
        return data;
      }

      if (Array.isArray(data?.data)) {
        return data.data;
      }

      if (Array.isArray(data?.content)) {
        return data.content;
      }

      return [];
    };

    const visitorsData = safeArray(visitorsResponse);
    const receivedData = safeArray(receivedResponse);
    const sentData = safeArray(sentResponse);
    const shortlistsData = safeArray(shortlistResponse);
    const conversationsData = safeArray(conversationsResponse);
const likesData = safeArray(likesResponse);
    setVisitors(visitorsData);
    setReceivedInterests(receivedData);
    setSentInterests(sentData);
    setShortlists(shortlistsData);

    const unreadMessages = conversationsData.reduce(
      (total, conversation) =>
        total + Number(
          conversation.unreadCount ||
          conversation.unreadMessages ||
          0
        ),
      0
    );

    const matches =
    await matchAPI.getTopMatches(
    userId,
    50
    );
    const filteredMatches = matches.filter(
        match => match.matchScore >= 75
    );

    const acceptedMatches = receivedData.filter((interest) =>
      String(
        interest.status ||
        interest.interestStatus ||
        ""
      ).toUpperCase() === "ACCEPTED"
    ).length;

    setDashboardStats({
      totalMatches: filteredMatches.length,
      interestsSent: sentData.length,
      interestsReceived: receivedData.length,
      shortlists: shortlistsData.length,
      profileViews: visitorsData.length,
    likesReceived: likesData.length,
      messages: unreadMessages
    });

  } catch (error) {
    console.error("Dashboard refresh failed:", error);
  }
}, []);

useEffect(() => {
  refreshDashboard();

  const handleDashboardUpdated = () => {
    refreshDashboard();
  };

  window.addEventListener(
    "dashboardUpdated",
    handleDashboardUpdated
  );

  return () => {
    window.removeEventListener(
      "dashboardUpdated",
      handleDashboardUpdated
    );
  };
}, [refreshDashboard]);
const [

notificationCount,

setNotificationCount

] = useState(0);





const [loadingProfiles,setLoadingProfiles] =
useState(true);
const [showProfilePopup, setShowProfilePopup] =
useState(false);
useEffect(() => {

  if (
    profileData &&
    profileData.profileCompleted === false
  ) {



    setShowProfilePopup(true);

  }

}, [profileData]);

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
  useEffect(() => {

  if(profileData?.email){

  loadProfiles();

  }

  }, [profileData]);

  const loadProfiles = async () => {
    try {
      setLoadingProfiles(true);
      const currentUser =
       JSON.parse(
       localStorage.getItem("user")
       );


 const userId =
 Number(
 currentUser.profile.userId
 );

 const data =
 await matchAPI.getTopMatches(
 userId,
 50
 );

  console.log(
    "PROFILES API:",
    data
  );
  const blockedUsers =
    await blockAPI.getMyBlockedUsers(
      currentUser.profile.userId
    );


  console.log(
    "BLOCKED USERS:",
    blockedUsers
  );
console.log("FIRST PROFILE:", data[0]);
  console.log(
  "Profiles API Response:",
 JSON.stringify(data,null,2)
  );
const blockedIds =
  blockedUsers.map(
    user => user.blockedId
  );
    setBlockedUsers(blockedIds);

console.log(
  "BLOCKED IDS:",
  blockedIds
);

const filteredProfiles =
  Array.isArray(data)

    ?

    data.filter(profile =>

      profile.profileCompleted === true &&

      String(profile.email)
        .toLowerCase()

      !==
      String(currentUser.email)
        .toLowerCase()

      &&

      !blockedIds.includes(
        profile.userId
      )

    )

    : [];

     console.log(
 "CURRENT USER:",
 currentUser.email
 );

 console.log(
 "FILTERED PROFILES:",
 filteredProfiles
 );
 console.log(
 "CURRENT USER EMAIL:",
 currentUser.email
 );

 console.log(
 "FILTERED:",
 filteredProfiles
 );
filteredProfiles.forEach(profile => {

  console.log(
    "USER:",
    profile.firstName,
    "PREMIUM:",
    profile.isPremium
  );

});
const reportStatus = {};

for (const profile of filteredProfiles) {

  try {

    reportStatus[profile.userId] =
      await reportAPI.hasReported(
        profile.userId
      );

  } catch {

    reportStatus[profile.userId] = false;

  }

}

setReportedUsers(reportStatus);


setProfiles(data);

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
   ? "Profile completed successfully"
   : "Click here to complete your profile"
};

  const handleLogout = () => {
    logout();
    navigate('/login');
  };
const handleSendInterest =
async(profile)=>{

try{

const currentUser =
JSON.parse(
localStorage.getItem(
"user"
)
);

const senderId =
Number(
currentUser.profile.userId
);

const receiverId =
Number(
profile.userId
);

console.log(
"SENDER:",
senderId
);

console.log(
"RECEIVER:",
receiverId
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

console.log(err);

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

useEffect(() => {

startLoading(
"Loading dashboard..."
);

const timer =
setTimeout(()=>{

stopLoading();

},1000);

return ()=>clearTimeout(
timer
);

},[]);

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
              to={
                profileData?.profileCompleted
                  ? item.to
                  : item.label === "Dashboard"
                  ? "/home"
                  : "#"
              }
             onClick={async (e) => {

                 // Profile completion check
                 if (
                     !profileData?.profileCompleted &&
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
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
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
    console.error("Double-click like failed:", error);
    toast.error("Failed to update like");
  }
}}
>

<HeartAnimation

show={

showHeart===profile.userId

}

onComplete={()=>{

setShowHeart(
null
);

}}

/>

{
profile.imageUrl ? (

<>
<img

src={profile.imageUrl}

alt={`${profile.firstName} ${profile.lastName}`}

className="
w-full
h-full
object-cover
"

onError={(e)=>{

e.target.parentElement.innerHTML=
`

<div class="w-full h-full flex items-center justify-center bg-gray-100 text-gray-400">

No Image

</div>

`;

}}

 />

<div

className="
absolute
bottom-3
left-3
bg-white/90
px-3
py-1
rounded-full
text-sm
font-medium
shadow
"

>

❤️ {profile.matchPercentage || 0} Match

</div>

</>

)

:

(

<div

className="
w-full
h-full
flex
items-center
justify-center
bg-gray-100
text-gray-400
"

>

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
         !profileData?.profileCompleted
       ) {

         setShowProfilePopup(true);

         return;
       }

       console.log(
         "PROFILE CLICKED:",
         profile
       );

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
                     !profileData?.profileCompleted
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

             console.error(err);

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
