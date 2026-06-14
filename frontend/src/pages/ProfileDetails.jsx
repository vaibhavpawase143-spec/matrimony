import { useParams, Link } from "react-router-dom";
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
import { useNavigate } from "react-router-dom";
import Navbar from "@/components/Navbar";
import { useLanguage } from "@/context/LanguageContext";
import {
  profileAPI,
  interestAPI,
  profileVisitorAPI
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
const [interestSent,
setInterestSent]=
useState(false);
const [canViewContact,setCanViewContact] =
useState(false);

useEffect(()=>{

const loadProfile =
async()=>{

try{

const data =
await profileAPI
.getProfileById(id);

setProfile(data);
const photos =
await photoAPI.getUserPhotos(
  data.userId
);

console.log(
  "PHOTOS = ",
  photos
);
console.log(
  "Gallery Photos =",
  photos
);

console.log(
  "Gallery Length =",
  photos.length
);
console.log(
  "FIRST PHOTO =",
  photos[0]
);
setGalleryPhotos(
  photos
);
const currentUser =
JSON.parse(
  localStorage.getItem("user")
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

//   await profileVisitorAPI.saveVisit(
//     data.userId
//   );

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

}catch(err){

console.log(err);

}

};

if(id){

loadProfile();

}

},[id]);
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

  } catch (err) {

    console.log(err);

    toast.error(
      err?.message || "Failed"
    );

  }

};
if(!profile){

return(

<div className="
min-h-screen
flex
items-center
justify-center
">

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

<Navbar/>

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
bg-card
rounded-xl
overflow-hidden
border
border-border
">

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

{

interestSent

? "Interest Sent"

: "Send Interest"

}

</button>
<button className="
w-full
bg-purple-700
rounded-lg
py-3
flex
justify-center
gap-2
">

<MessageSquare size={18}/>

Message

</button>
<button
  onClick={() => {

    if (
      !galleryPhotos ||
      galleryPhotos.length === 0
    ) {

      toast(
        "This user has not uploaded any gallery photos"
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

<h1 className="
text-3xl
font-bold
">

{profile.firstName}

{" "}

{profile.lastName}

</h1>
{
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
canViewContact
? profile.email
: "🔒 Send Interest & Get Accepted"
}
/>

<InfoRow
label="Phone"
value={
canViewContact
? profile.phone
: "🔒 Send Interest & Get Accepted"
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

<button
onClick={() =>
setCurrentPhotoIndex(prev =>
prev === 0
? galleryPhotos.length - 1
: prev - 1
)
}
className="
bg-gray-200
px-4
py-2
rounded
"
>
◀ Prev
</button>

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
<button
onClick={() =>
setCurrentPhotoIndex(prev =>
prev === galleryPhotos.length - 1
? 0
: prev + 1
)
}
className="
bg-gray-200
px-4
py-2
rounded
"
>
Next ▶
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