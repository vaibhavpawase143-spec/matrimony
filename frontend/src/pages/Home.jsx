import { Heart, User, Search, Settings, LogOut, ChevronDown, Bell, MessageSquare, Star, Menu } from "lucide-react";

import { Link, useNavigate } from "react-router-dom";
import HeartAnimation
from "@/components/HeartAnimation";
import { swipeAPI } from "@/services/swipeAPI";
import useLikes
from "@/hooks/useLikes";
import { motion } from "framer-motion";

import { useState, useEffect } from "react";

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
profileVisitorAPI
} from "@/services/api";

import {

shortlistAPI

}

from "@/services/shortlistAPI";

import {

connectNotifications,

disconnectNotifications

} from "@/services/websocket";


const HomeFixed = () => {

const navigate = useNavigate();
const {

isLiked,

toggleLike

}=useLikes();


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

shortlists:0,

profileViews:0,
likesReceived:0,
messages:0

});

const loadDashboard = async()=>{

try{

const currentUser =
JSON.parse(
localStorage.getItem("user")
);

const senderId =
currentUser.profile.userId;

const sentInterests =
await interestAPI.getSentInterests(
senderId
);

const receivedInterests =
await interestAPI
.getReceivedPendingInterests(
senderId
);

const shortlistData =
await shortlistAPI
.getMyShortlists(
0,
100
);
const likesReceived =
await swipeAPI.getReceivedLikes();
const visitors =
await profileVisitorAPI
.getMyVisitors();
setDashboardStats(prev=>({

...prev,

interestsSent:
sentInterests.length,

interestsReceived:
receivedInterests.length,

shortlists:
(
shortlistData.content ||
[]
).length,

likesReceived:
likesReceived.length,

profileViews:
visitors.length

}));
}catch(err){

console.log(err);

}

};

useEffect(()=>{

loadDashboard();

const refreshDashboard=()=>{

loadDashboard();

};

window.addEventListener(

"interestUpdated",

refreshDashboard

);

window.addEventListener(

"shortlist:updated",

refreshDashboard

);
window.addEventListener(
"like:updated",
refreshDashboard
);
return ()=>{

window.removeEventListener(

"interestUpdated",

refreshDashboard

);

window.removeEventListener(

"shortlist:updated",

refreshDashboard

);

};

},[]);const [

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
  const data =
  await profileAPI.getProfiles();

  console.log(
  "Profiles API Response:",
 JSON.stringify(data,null,2)
  );
 const currentUser =
 JSON.parse(
 localStorage.getItem("user")
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

setSentInterests(
prev => [

...prev,

receiverId

]
);

setDashboardStats(
prev => ({

...prev,

interestsSent:

prev.interestsSent + 1

})
);

toast.success(
"Interest Sent Successfully ❤️"
);
}catch(err){

console.log(err);

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
              onClick={(e) => {

                if (
                  !profileData?.profileCompleted &&
                  item.label !== "Dashboard" &&
                  item.label !== "Settings"
                ) {

                  e.preventDefault();

                  setShowProfilePopup(true);

                }

              }}
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

navigate(

`/profile/${profile.id}`

);

}

},220);

}}

>

<div

className="

relative

h-[320px]


"
onDoubleClick={async(e)=>{

e.stopPropagation();

try{

if(

!isLiked(
profile.userId
)

){

setShowHeart(
profile.userId
);

await toggleLike(
profile.userId
);
window.dispatchEvent(
new Event(
"like:updated"
)
);
toast.success(
"Liked ❤️"
);

}else{

toast(
"Already liked ❤️"
);

}

}catch(err){

toast.error(
"Failed"
);

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

❤️ {profile.matchPercentage || 0}% Match

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
                  <div className="p-5 pb-6">
                  <h3 className="text-xl font-bold">

                    {profile.firstName}
                    {" "}
                    {profile.lastName}

                    {profile.isPremium && (
                      <span className="ml-2 text-yellow-500">
                        👑
                      </span>
                    )}

                  </h3>

                   <p className="text-gray-600 mt-1">

                   {calculateAge(profile.dateOfBirth)}
                   yrs • {profile.cityName || "City"}

                   </p>



                  <div className="mt-4 flex flex-col gap-3">

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
                  py-3
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
                 py-3
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
                     `/profile/${profile.id}`
                   );

                 }}

                 >

                 👤 View Profile

                 </button>
                   </div>
<div className="mt-5 flex justify-center gap-3">

<button

title="Like"

onClick={async(e)=>{

e.stopPropagation();

try{

const likedBefore =

isLiked(
profile.userId
);

setShowHeart(
profile.userId
);

await toggleLike(
profile.userId
);

if(

likedBefore

){

toast(
"Like removed"
);

}else{

toast.success(
"Liked ❤️"
);

}

}catch{

toast.error(
"Failed"
);

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

${

isLiked(
profile.userId
)

?

"scale-125"

:

""

}

`}

>

{

isLiked(
profile.userId
)

?

"❤️"

:

"🤍"

}

</span>
</button>
<div title="Shortlist">

<ShortlistButton

profileId={profile.id || i}

size="md"

showLabel={false}

/>

</div>

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
    </>
  );
};

export default HomeFixed;
