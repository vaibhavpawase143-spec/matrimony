import { useParams, Link,useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import ShortlistButton from "@/components/ShortlistButton";

import {
Heart,
MapPin,
GraduationCap,
Briefcase,
Calendar,
ArrowLeft,
Star,
MessageSquare
} from "lucide-react";
import { photoAPI } from "@/services/api";


import { useLanguage } from "@/context/LanguageContext";
import {
  profileAPI,
  interestAPI,
  profileVisitorAPI,
    blockAPI,
    subscriptionAPI
} from "@/services/api";
import toast from "react-hot-toast";


const InfoRow = ({label,value}) => (

<div className="
flex
justify-between
py-2.5
border-b
border-border
last:border-0
">

<span className="
text-xs
text-muted-foreground
">

{label}

</span>

<span className="
text-xs
font-medium
text-foreground
text-right
">

{value || "-"}

</span>

</div>

);

const ProfileDetails=()=>{
const navigate = useNavigate();
const {t}=useLanguage();
const [currentPhotoIndex, setCurrentPhotoIndex] =
useState(0);
const calculateAge=(dob)=>{

if(!dob) return "-";

const birth=new Date(dob);

const today=new Date();

let age=
today.getFullYear()-
birth.getFullYear();

const month=
today.getMonth()-
birth.getMonth();

if(
month<0 ||
(
month===0 &&
today.getDate()<birth.getDate()
)
){

age--;

}

return age;

};

const [galleryPhotos,setGalleryPhotos] =
useState([]);

const [showGallery,setShowGallery] =
useState(false);
const {id}=useParams();

const [profile,setProfile]=
useState(null);
const [blockedProfile,
setBlockedProfile] =
useState(false);

const [interestSent,
setInterestSent]=
useState(false);
const [canViewContact,setCanViewContact] =
useState(false);
const [isPremiumUser, setIsPremiumUser] =
useState(false);
const [showUpgradePopup, setShowUpgradePopup] = useState(false);
useEffect(()=>{

const loadProfile =
async()=>{

try{

const data =
await profileAPI
.getProfileById(id);


const currentUser =
JSON.parse(
  localStorage.getItem("user")
);
const myProfile =
    await profileAPI.getProfile();

console.log(myProfile);


console.log("MY PROFILE =", myProfile);
console.log("MY PROFILE PREMIUM =", myProfile.isPremium);

setIsPremiumUser(
    Boolean(myProfile.isPremium)
);const blockedUsers =
await blockAPI.getMyBlockedUsers(
  currentUser.profile.userId
);

const blockedIds =
blockedUsers.map(
  user => user.blockedId
);
if (
  blockedIds.includes(
    data.userId
  )
) {

  setBlockedProfile(true);

  return;

}
if (
  blockedIds.includes(
    data.userId
  )
) {

  toast.error(
    "This profile is blocked"
  );

  return;

}
const galleryResponse =
    await photoAPI.getUserPhotos(data.userId);

console.log("Gallery Response =", galleryResponse);

console.log(
    "Gallery Photos =",
    galleryResponse.photos
);

console.log(
    "Gallery Length =",
    galleryResponse.photos.length
);

setGalleryPhotos(
    galleryResponse.photos
);
console.log(
  "CURRENT USER",
  currentUser
);

console.log(
  "PROFILE DATA",
  data
);
if (
  currentUser?.profile?.userId !==
  data.userId
) {

  console.log(
    "VISITOR SAVE START",
    data.userId
  );

await profileVisitorAPI.saveVisit(
    data.userId
);
window.dispatchEvent(
    new Event("dashboardUpdated")
);
  console.log(
    "VISITOR SAVE SUCCESS"
  );
}
const sentInterests =
await interestAPI
.getSentInterests(

currentUser.profile.userId

);console.log(
  "SENT INTERESTS =",
  JSON.stringify(
    sentInterests,
    null,
    2
  )
);
const acceptedInterest =
sentInterests.find(

item =>

Number(item.receiverId) ===
Number(data.userId)

&&

item.status ===
"ACCEPTED"

);

setCanViewContact(
!!acceptedInterest
);
const alreadySent =
sentInterests.some(

item =>

Number(
item.receiverId
)

===

Number(
data.userId
)

);

setInterestSent(
alreadySent
);
setProfile(data);
}catch(err){

console.log(err);

}

};

if(id){

loadProfile();

}

},[id]);

const handleMessageClick = async () => {

    try {

        const subscription =
            await subscriptionAPI.getMySubscription();

        if (subscription?.isActive) {

            navigate(`/messages?receiverId=${profile.userId}`);

       } else {

           setShowUpgradePopup(true);

       }
   } catch {

       setShowUpgradePopup(true);

   }
};

const handleSendInterest = async () => {

  try {

    const currentUser =
      JSON.parse(
        localStorage.getItem("user")
      );

    if (!currentUser || !profile) {

      toast.error("User not found");
      return;

    }

    const senderId =
      Number(
        currentUser.profile.userId
      );

    if (interestSent) {

      toast(
        "Interest already sent ❤️"
      );

      return;

    }

    const receiverId =
      Number(profile.userId);

    if (senderId === receiverId) {

      toast.error(
        "You cannot send interest to yourself"
      );

      return;

    }

    await interestAPI.sendInterest(
      senderId,
      receiverId
    );

    setInterestSent(true);

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
if (blockedProfile) {

  return (

    <div
      className="
      min-h-screen
      flex
      items-center
      justify-center
      text-2xl
      font-bold
      "
    >

      🚫 This profile is blocked

    </div>

  );

}
if (!profile) {

  return (

    <div
      className="
      min-h-screen
      flex
      items-center
      justify-center
      "
    >

      Loading Profile...

    </div>

  );

}
return(
<>
<div className="
min-h-screen
bg-muted/30
">



<div className="
container
mx-auto
px-4
py-8
">

<Link

to="/search"

className="
inline-flex
items-center
gap-2
mb-6
"

>

<ArrowLeft size={16}/>

Back

</Link>

<div className="
grid
grid-cols-1
lg:grid-cols-3
gap-6
">

{/* LEFT */}

<div>

<div className="
relative
bg-card
rounded-xl
overflow-hidden
border
border-border
">
{
profile.isPremium && (

<div
className="
absolute
top-4
left-4
z-10
bg-gradient-to-r
from-yellow-400
to-amber-500
text-white
px-4
py-1
rounded-full
text-sm
font-bold
shadow-lg
"
>

👑 PREMIUM

</div>

)
}
<img

src={
profile.imageUrl ||
"/default-profile.png"
}

className="
w-full
aspect-[3/4]
object-cover
"

/>

<div className="p-4 space-y-2">
<button

onClick={handleSendInterest}

disabled={interestSent}

className={`
w-full
text-white
rounded-lg
py-3
flex
justify-center
gap-2

${interestSent
? "bg-green-600"
: "bg-red-600"}

`}

>

<Heart size={18}/>
{interestSent ? "Interest Sent" : "Send Interest"}
</button>



<button
onClick={handleMessageClick}
className="
w-full
bg-purple-700
hover:bg-purple-800
text-white
rounded-lg
py-3
flex
justify-center
gap-2
transition
"
>

<MessageSquare size={18}/>

Message

</button>
<button
onClick={() => {

    if (!isPremiumUser) {

        toast.error(
            "Upgrade to Premium to view this user's photo gallery."
        );

        return;
    }

    if (!galleryPhotos || galleryPhotos.length === 0) {

        toast(
            "This user has not uploaded any gallery photos."
        );

        return;
    }

    setCurrentPhotoIndex(0);

    setShowGallery(true);

}}
  className="
  w-full
  bg-blue-600
  hover:bg-blue-700
  text-white
  rounded-lg
  py-3
  font-medium
  transition
  "
>
  View Photo Gallery
</button>

<ShortlistButton

profileId={profile.id}

size="lg"

showLabel={true}

/>

</div>

</div>

</div>

{/* RIGHT */}

<div className="
lg:col-span-2
space-y-5
">

<div className="
bg-card
rounded-xl
p-6
border
border-border
">

<h1
className="
text-3xl
font-bold
flex
items-center
gap-2
"
>

{profile.firstName}

{" "}

{profile.lastName}

{
profile.isPremium && (

<span
className="
bg-yellow-100
text-yellow-700
text-sm
px-3
py-1
rounded-full
font-semibold
"
>

👑 Premium

</span>

)

}

</h1>{
profile.verified && (

<div className="
mt-2
inline-flex
items-center
bg-green-100
text-green-700
px-3
py-1
rounded-full
text-sm
font-medium
">

🛡 Verified Profile

</div>

)
}
{/* TOP SECTION */}

<div className="
flex
flex-wrap
gap-4
mt-3
text-sm
text-muted-foreground
">

<span className="flex gap-1">

<Calendar size={15}/>

{calculateAge(profile.dateOfBirth)}

yrs

</span>

<span className="flex gap-1">

<MapPin size={15}/>

{profile.cityName}

</span>

<span className="flex gap-1">

<GraduationCap size={15}/>

{profile.educationLevelName}

</span>

<span className="flex gap-1">

<Briefcase size={15}/>

{profile.occupationName}

</span>

</div>
<p className="mt-5">

{profile.aboutMe}

</p>

</div>


{/* PERSONAL */}

<div className="
bg-card
rounded-xl
border
border-border
p-6
">

<h2 className="
font-bold
mb-4
">

Personal Details

</h2>

<InfoRow
label="Gender"
value={profile.genderName}
/>

<InfoRow
label="Religion"
value={profile.religionName}
/>

<InfoRow
label="Caste"
value={profile.casteName}
/>

<InfoRow
label="Sub Caste"
value={profile.subCasteName}
/>

<InfoRow
label="Mother Tongue"
value={profile.motherTongueName}
/>

<InfoRow
label="Marital Status"
value={profile.maritalStatusName}
/>

<InfoRow
label="Height"
value={profile.heightValue}
/>

<InfoRow
label="Weight"
value={profile.weightValue}
/>

<InfoRow
label="Blood Group"
value={profile.bloodGroupName}
/>

<InfoRow
label="Manglik"
value={profile.manglikStatusName}
/>

<InfoRow
label="Disability"
value={profile.disabilityStatusName}
/>
</div>


{/* EDUCATION */}

<div className="
bg-card
rounded-xl
border
border-border
p-6
">

<h2 className="
font-bold
mb-4
">

Education & Career

</h2>

<InfoRow
label="Qualification"
value={profile.qualificationName}
/>

<InfoRow
label="Field Of Study"
value={profile.fieldOfStudyName}
/>

<InfoRow
label="Education"
value={profile.educationLevelName}
/>
<InfoRow
label="Occupation"
value={profile.occupationName}
/>

<InfoRow
label="Employment"
value={profile.employedStatusName}
/>

<InfoRow
label="Income"
value={profile.incomeValue}
/>

<InfoRow
label="Company"
value={profile.companyName}
/>

</div>


{/* FAMILY */}

<div className="
bg-card
rounded-xl
border
border-border
p-6
">

<h2 className="
font-bold
mb-4
">

Family Details

</h2>

<InfoRow
label="Father Name"
value={profile.fatherName}
/>

<InfoRow
label="Father Occupation"
value={profile.fatherOccupation}
/>

<InfoRow
label="Mother Name"
value={profile.motherName}
/>

<InfoRow
label="Mother Occupation"
value={profile.motherOccupation}
/>

<InfoRow
label="Siblings"
value={profile.siblingsCount}
/>

<InfoRow
label="Family Type"
value={profile.familyTypeName}
/>

<InfoRow
label="Family Status"
value={profile.familyStatusName}
/>

<InfoRow
label="Family Value"
value={profile.familyValueName}
/>

</div>


{/* LOCATION */}

<div className="
bg-card
rounded-xl
border
border-border
p-6
">

<h2 className="
font-bold
mb-4
">

Location

</h2>

<InfoRow
label="Country"
value={profile.countryName}
/>

<InfoRow
label="State"
value={profile.stateName}
/>

<InfoRow
label="City"
value={profile.cityName}
/>

<InfoRow
label="Address"
value={profile.address}
/>

</div>

<div
className="
bg-card
rounded-xl
border
border-border
p-6
"
>

<h2
className="
font-bold
mb-4
"
>

Contact Details

</h2>

<InfoRow
label="Email"
value={
!canViewContact
? "🔒 Send Interest & Get Accepted"

: isPremiumUser

? profile.email

: "👑 Upgrade to Premium to view Email"
}
/>

<InfoRow
label="Phone"
value={
!canViewContact
? "🔒 Send Interest & Get Accepted"

: isPremiumUser

? profile.phone

: "👑 Upgrade to Premium to view Phone"
}
/>
</div>
{/* LIFESTYLE */}

<div className="
bg-card
rounded-xl
border
border-border
p-6
">

<h2 className="
font-bold
mb-4
">

Lifestyle

</h2>

<InfoRow
label="Diet"
value={profile.dietValue}
/>

<InfoRow
label="Smoking"
value={profile.smokingValue}
/>

<InfoRow
label="Drinking"
value={profile.drinkingValue}
/>

</div>

</div>

</div>

</div>

</div>
{
showGallery && (

<div
className="
fixed
inset-0
bg-black/80
z-50
flex
items-center
justify-center
p-4
"
>

<div
className="
bg-white
rounded-xl
max-w-6xl
w-full
p-5
max-h-[90vh]
overflow-auto
"
>

<div
className="
flex
justify-between
items-center
mb-4
"
>

<h2 className="text-xl font-bold">
Photo Gallery
</h2>

<button
onClick={() =>
setShowGallery(false)
}
>
✕
</button>

</div>
<div className="
flex
items-center
justify-center
gap-4
">

{galleryPhotos.length > 1 && (

<>
    <button
        onClick={() =>
            setCurrentPhotoIndex(prev =>
                prev === 0
                    ? galleryPhotos.length - 1
                    : prev - 1
            )
        }
        className="bg-gray-200 px-4 py-2 rounded"
    >
        ◀ Prev
    </button>

    <button
        onClick={() =>
            setCurrentPhotoIndex(prev =>
                prev === galleryPhotos.length - 1
                    ? 0
                    : prev + 1
            )
        }
        className="bg-gray-200 px-4 py-2 rounded"
    >
        Next ▶
    </button>
</>

)}

{galleryPhotos.length > 0 && (

<img
src={
galleryPhotos[
currentPhotoIndex
]?.photoUrl ||

galleryPhotos[
currentPhotoIndex
]?.imageUrl ||

galleryPhotos[
currentPhotoIndex
]?.url
}
alt=""
className="
max-h-[70vh]
max-w-[70vw]
object-contain
rounded-lg
"
/>

)}

</div>
</div>

</div>

)
}
{
showUpgradePopup && (

<div className="fixed inset-0 bg-black/50 flex items-center justify-center z-[999999]">

    <div className="bg-white rounded-3xl p-8 w-[420px] text-center">

        <div className="text-6xl mb-4">
            👑
        </div>

        <h2 className="text-2xl font-bold mb-3">
            Premium Required
        </h2>

        <p className="text-gray-600 mb-6">
            Chat is available only for Premium members.
        </p>

        <div className="flex justify-center gap-3">

            <button
                onClick={() => {
                    setShowUpgradePopup(false);
                    navigate("/home");
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

)
}
</>
);

};

export default ProfileDetails;