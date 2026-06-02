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
import toast from "react-hot-toast";
import { useLanguage } from "@/context/LanguageContext.jsx";
import { useProfileData } from "@/hooks/useProfileData";
import {
profileAPI,
interestAPI
}
from "@/services/api";
import {

connectNotifications,

disconnectNotifications

}

from "@/services/websocket";


const HomeFixed = () => {

const navigate = useNavigate();

const { userName, logout } = useAuth();

const { startLoading, stopLoading } =
useLoading();

const {
isLiked,
isBookmarked,
toggleLike,
toggleBookmark
} = useLikeBookmark();

const { t } = useLanguage();

const {
profileData,
isLoading: profileLoading
} = useProfileData();

const [isSidebarOpen,setIsSidebarOpen] =
useState(true);

const [profiles,setProfiles] =
useState([]);

const [

sentInterests,

setSentInterests

] = useState([]);


const [

dashboardStats,

setDashboardStats

] = useState({

totalMatches:0,

interestsSent:0,

interestsReceived:0,

bookmarkedProfiles:0,

profileViews:0,

messages:0

});
const [

notificationCount,

setNotificationCount

] = useState(0);


useEffect(()=>{

loadSentInterests();

const currentUser =

JSON.parse(

localStorage.getItem(
"user"
)

);

connectNotifications(

currentUser.id,

(notification)=>{

console.log(

"NOTIFICATION ARRIVED:",

notification

);

toast.success(

notification.message ||

"New Notification 🔔"

);

setNotificationCount(

prev => prev + 1

);

}
);

return ()=>{

disconnectNotifications();

};

},[]);

const loadSentInterests = async()=>{

try{

const currentUser =
JSON.parse(
localStorage.getItem("user")
);
console.log(
"CURRENT USER:",
currentUser
);
const sent =
await interestAPI.getSentInterests(
currentUser.id
);
const received =

await interestAPI
.getReceivedInterests(
currentUser.id
);
setSentInterests(

sent.map(

item=>item.receiverId

)

);

setDashboardStats({

totalMatches:0,

interestsSent:

sent.length,

interestsReceived:

received.filter(

item=>

item.status==="PENDING"

).length,

bookmarkedProfiles:0,

profileViews:0,

messages:0

});

}catch(err){

console.log(err);

}

};

const [loadingProfiles,setLoadingProfiles] =
useState(true);

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
  const data =
  await profileAPI.getProfiles();

  console.log(
  "Profiles API Response:",
 JSON.stringify(data,null,2)
  );
 const currentUserEmail =
 profileData?.email;


 const filteredProfiles =
 Array.isArray(data)

 ? data.filter(
 (profile)=>

 profile.email !==
 currentUserEmail

 )

 : [];

 setProfiles(
 filteredProfiles
 );

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
const handleSendInterest =
async(receiverId)=>{

try{

const currentUser =
JSON.parse(
localStorage.getItem("user")
);

await interestAPI.sendInterest(

currentUser.id,

receiverId

);

setSentInterests(

prev=>[
...prev,
receiverId
]

);
toast.success(
"Interest sent successfully ❤️"
);
}
catch(error){

if(

error.message
.toLowerCase()
.includes("already")

){

setSentInterests(

prev=>[
...prev,
receiverId
]

);

toast.error(
"Interest already sent ❤️"

);

return;

}
toast.error(
"Interest already sent ❤️"

);

}

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
              <span className="absolute -top-1 -right-1 h-4 w-4 bg-primary text-primary-foreground text-[10px] font-bold rounded-full flex items-center justify-center">{notificationCount}</span>
            </button>
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
            <DashboardStats

            stats={dashboardStats}

            />            </div>

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
                     className="
                     bg-white
                     rounded-2xl
                     shadow-lg
                     overflow-hidden
                     cursor-pointer
                     transition-all
                     duration-300
                     hover:shadow-xl
                   hover:scale-[1.03]
                   hover:-translate-y-1
                     "
                      onClick={() => navigate(`/profile/${profile.id}`)}
                    >
                 <div className="relative h-[320px] overflow-hidden">

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

                 e.target.parentElement.innerHTML =
                 `
                 <div class="w-full h-full flex items-center justify-center bg-gray-100 text-gray-400">

                 No Image

                 </div>
                 `;

                 }}

                 />

                 <div className="
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
                 ">

                 ❤️ {profile.matchPercentage || 0}% Match

                 </div>

                 </>

                 )

                 :
                   (
                    <div className="
                    w-full
                    h-full
                    flex
                    items-center
                    justify-center
                    bg-gray-100
                    text-gray-400
                    ">

                    No Image

                    </div>

                    )

                    }

                    </div>
                  <div className="p-5 pb-6">
                   <h3 className="text-xl font-bold">

                   {profile.firstName}
                   {" "}
                   {profile.lastName}

                   </h3>

                   <p className="text-gray-600 mt-1">

                   {calculateAge(profile.dateOfBirth)}
                   yrs • {profile.cityName || "City"}

                   </p>



                  <div className="mt-4 flex flex-col gap-3">

                  <button

       className="
       w-full
       bg-[#E94057]
       hover:bg-[#D6334C]
       text-white
       py-3
       rounded-xl
       font-semibold
       shadow-md
       transition
       "

          onClick={(e)=>{

          e.stopPropagation();

        handleSendInterest(

        profile.userId

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
      py-3
      rounded-xl
      font-semibold
      shadow-sm
      hover:bg-white
      transition
      "

                  onClick={(e)=>{

                  e.stopPropagation();

                  navigate(`/profile/${profile.id}`);

                  }}

                  >

                  👤 View Profile

                  </button>

                  </div>
<div className="
mt-4
flex
justify-center
gap-4
">
                 <LikeBookmarkButtons
                 profileId={profile.id || i}
                 isLiked={isLiked(profile.id || i)}
                 isBookmarked={isBookmarked(profile.id || i)}
                 onLike={toggleLike}
                 onBookmark={toggleBookmark}
                 size="sm"
                 />
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
  );
};

export default HomeFixed;
