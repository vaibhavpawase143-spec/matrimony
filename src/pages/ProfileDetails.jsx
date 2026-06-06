import { useParams, Link } from "react-router-dom";
import { useState, useEffect } from "react";
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

import Navbar from "@/components/Navbar";
import { useLanguage } from "@/context/LanguageContext";
import { profileAPI,interestAPI } from "@/services/api";
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

const {id}=useParams();

const [profile,setProfile]=
useState(null);
const [interestSent,
setInterestSent]=
useState(false);
useEffect(()=>{

const loadProfile =
async()=>{

try{

const data =
await profileAPI
.getProfileById(id);

setProfile(data);

const currentUser =
JSON.parse(
localStorage.getItem(
"user"
)
);

const sentInterests =
await interestAPI
.getSentInterests(

currentUser.profile.userId

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
const handleSendInterest =
async()=>{

try{

const currentUser =
JSON.parse(
localStorage.getItem(
"user"
)
);

if(
!currentUser ||
!profile
){

toast.error(
"User not found"
);

return;

}

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

setInterestSent(
true
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

<button className="
w-full
bg-yellow-600
rounded-lg
py-3
flex
justify-center
gap-2
">

<Star size={18}/>

Shortlist

</button>

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

);

};

export default ProfileDetails;