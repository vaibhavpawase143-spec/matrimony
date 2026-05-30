import {
useParams,
useNavigate
} from "react-router-dom";
import { useState, useEffect } from "react";
import toast from "react-hot-toast";
import {
Heart,
MapPin,
GraduationCap,
Briefcase,
Calendar,
ArrowLeft,
Star,
MessageSquare,
Users,
Languages,
Sparkles,
User,
Building2,
Leaf,
Home
} from "lucide-react";
import Navbar from "@/components/Navbar";
import { useLanguage } from "@/context/LanguageContext";
import {
profileAPI,
interestAPI
}
from "@/services/api";
const InfoRow = ({ label, value }) => {

if(
value===null ||
value===undefined ||
value===""
)
return null;

return(

<div className="
flex
items-center
justify-between
py-2
px-2
gap-8
border-b
border-border
last:border-0
overflow-hidden
">
<span className="
text-sm
text-muted-foreground
font-medium
shrink-0
">

{label}

</span>

<span className="
text-sm
font-semibold
text-white
text-right
capitalize
max-w-[55%]
break-words
">


{value}

</span>

</div>

);

};
const ProfileDetails = () => {
  const { t } = useLanguage();
  const { id } = useParams();
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);

const [

interestSent,

setInterestSent

] = useState(false);

useEffect(()=>{

loadProfile();

},[id]);

useEffect(()=>{

if(profile?.userId){

checkInterest();

}

},[profile?.userId]);


const loadProfile = async () => {

try{

const data =
await profileAPI
.getProfileById(id);

setProfile(data);

console.log(data);
}

catch(error){

console.log(
"Profile loading failed:",
error
);

}

};

const checkInterest = async()=>{



try{



const currentUser =

JSON.parse(

localStorage.getItem("user")

);



const sent =

await interestAPI

.getSentInterests(

currentUser.id

);



const exists = sent.some(

item =>

Number(item.receiverId) ===
Number(profile?.userId)

);

setInterestSent(

exists

);



}

catch(error){



console.log(error);



}



};





const handleSendInterest = async()=>{

if(interestSent){

toast.error(
"Interest already sent ❤️"

);

return;

}


try{



const currentUser =

JSON.parse(

localStorage.getItem("user")

);



await interestAPI.sendInterest(



currentUser.id,



profile.userId



);



setInterestSent(true);
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



setInterestSent(true);



return;



}



alert(

error.message
.toLowerCase()
.includes("already")

?

"Interest already sent"

:

error.message

);



}



};


const calculateAge=(dob)=>{

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
(month===0 &&
today.getDate()<birth.getDate())
){
age--;
}

return age;

};
  if (!profile) {
    return (
      <div className="min-h-screen bg-muted/30 flex items-center justify-center">
        <div className="text-center">
          <p className="text-muted-foreground">Loading profile...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      <div className="container mx-auto px-4 pt-8 pb-16">
   <button

   onClick={() => navigate(-1)}

   className="
   inline-flex
   items-center
   gap-2
   text-base
   font-medium
   text-white
   hover:text-primary
   transition-all
   duration-300
   mb-8
   px-2
   py-1
   rounded-lg
   hover:bg-white/5
   "

   >

   <ArrowLeft className="h-5 w-5"/>

   Back

   </button>
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
          {/* Left - Photo & quick actions */}
<div className="lg:col-span-4 space-y-4 lg:sticky lg:top-24 self-start">
            <div className="bg-card rounded-xl overflow-hidden border border-border">
              <div className="aspect-[3/4] overflow-hidden">
              <img

              src={
              profile.imageUrl ||
              "/default-profile.png"
              }

              alt={
              `${profile.firstName}
              ${profile.lastName}`
              }

              className="
              w-full
              h-full
              object-cover
              "

              />
              </div>
           <div className="p-6 space-y-4">

           <button

           onClick={handleSendInterest}



           className={`

           w-full
           flex
           items-center
           justify-center
           gap-3
           font-semibold
           py-4
           rounded-2xl
           text-base
           shadow-lg
           transition-all
           duration-300

           ${

           interestSent

           ?

           'bg-pink-500 text-white cursor-default'

           :

           'bg-primary hover:bg-primary/90 text-primary-foreground hover:scale-[1.01]'

           }

           `}

           >

           <Heart className="h-4 w-4"/>

           {

           interestSent

           ?

           "💖 Interest Sent"

           :

           t.profileDetails.sendInterest

           }

           </button>

           <button className="
           w-full
           flex
           items-center
           justify-center
           gap-3
           bg-purple-600
           hover:bg-purple-700
           text-white
           font-semibold
           py-4
           rounded-2xl
           text-base
           shadow-lg
           hover:scale-[1.01]
           transition-all
           duration-300
           ">

           <MessageSquare className="h-4 w-4"/>

           {t.profileDetails.message}

           </button>

           <button className="
           w-full
           flex
           items-center
           justify-center
           gap-3
           bg-amber-500/15
           text-amber-300
           hover:bg-amber-500/25
           font-semibold
           py-4
           rounded-2xl
           text-base
           shadow-lg
           hover:scale-[1.01]
           transition-all
           duration-300
           ">

           <Star className="h-4 w-4"/>

           {t.profileDetails.shortlist}

           </button>



              </div>
            </div>
          </div>
<div className="lg:col-span-8 space-y-6">
        <div className="
        bg-card
        rounded-2xl
        border
        border-border
        p-8
        ">

      <h1 className="
      text-4xl
      lg:text-5xl
      font-bold
      capitalize
      text-white
      leading-tight
      ">

       {profile.firstName} {profile.lastName}

       </h1>

     <div className="flex flex-wrap gap-8 mt-5">

     <div className="flex items-center gap-3">

     <Sparkles
     className="
     w-5
     h-5
     text-rose-400
     shrink-0
     "
     />

 <span className="
 text-sm
 font-semibold
 text-white
 pr-4
 tracking-[0.02em]
 text-right
 ">

     {profile.religionName}

     </span>

     </div>


     <div className="flex items-center gap-3">

     <Users
     className="
     w-5
     h-5
     text-purple-400
     shrink-0
     "
     />

     <span className="text-base font-medium text-foreground">

     {profile.casteName}

     </span>

     </div>


     <div className="flex items-center gap-3">

     <Languages
     className="
     w-5
     h-5
     text-blue-400
     shrink-0
     "
     />

     <span className="text-base font-medium text-foreground">

     {profile.motherTongueName}

     </span>

     </div>

     </div>
         <div className="flex flex-wrap gap-5 mt-4 text-sm text-muted-foreground">

            <span className="flex items-center gap-1">

            <Calendar className="h-4 w-4"/>

            {
            profile.dateOfBirth
            ? calculateAge(profile.dateOfBirth)
            : ""
            }

            yrs

            </span>
               <span className="flex items-center gap-1">

                 <MapPin className="h-4 w-4"/>

                {`${profile.cityName || ""}${
                profile.stateName
                ? ", " + profile.stateName
                : ""
                }`}

               </span>

               <span className="flex items-center gap-1">

                 <GraduationCap className="h-4 w-4"/>

                 {profile.educationLevelName}

               </span>

               <span className="flex items-center gap-1">

                 <Briefcase className="h-4 w-4"/>

                 {profile.occupationName}

               </span>

             </div>
          {
          (profile.aboutMe || profile.about) && (

        <div className="
        mt-8
        rounded-xl
        border
        border-border
        bg-background/30
        p-6
        ">

        <h2 className="
       text-2xl font-semibold text-white
        mb-4
        text-foreground
        ">

        About Me

        </h2>

    <p className="
    mt-3
    text-base
    leading-8
    text-foreground/80
    break-words
    max-w-3xl
    ">

        {profile.aboutMe || profile.about}

        </p>

        </div>

          )

          }

          </div>

         <div className="
bg-card
rounded-2xl
border
border-border
p-8
pb-3
">
<div className="flex items-center gap-3 mb-6">

<User className="w-7 h-7 text-blue-400 shrink-0"/>

<h2 className="
text-2xl
font-semibold
text-white
">

{t.profileDetails.personalDetails}

</h2>

</div>
{/* PERSONAL DETAILS */}


<InfoRow
label={t.profileDetails.height}
value={profile.heightValue}
/>
<InfoRow
label="Weight"
value={profile.weightValue}
/>

<InfoRow
label="Gender"
value={profile.genderName}
/>

<InfoRow
label="Marital Status"
value={profile.maritalStatusName}
/>
<InfoRow
label={t.profileDetails.religion}
value={profile.religionName}
/>

<InfoRow
label={t.profileDetails.caste}
value={profile.casteName}
/>
<InfoRow
label="Sub Caste"
value={profile.subCasteName}
/>
<InfoRow
label={t.profileDetails.motherTongue}
value={profile.motherTongueName}
/>





<InfoRow
label={t.profileDetails.location}
value={`${profile.cityName || ""}${
profile.stateName ? ", " + profile.stateName : ""
}${
profile.countryName ? ", " + profile.countryName : ""
}`}
/>

<InfoRow
label="Address"
value={profile.address?.trim()}
/>

</div>

{/* EDUCATION & CAREER */}

<div className="
bg-card/95 backdrop-blur-sm
rounded-2xl
border
border-border
p-8
">

<div className="flex items-center gap-3 mb-6">

<Building2 className="w-7 h-7 text-emerald-400 shrink-0"/>

<h2 className="
text-2xl
font-semibold
text-white
">

Career & Education

</h2>

</div>

<InfoRow
label="Education"
value={profile.educationLevelName}
/>

<InfoRow
label="Profession"
value={profile.occupationName}
/>
<InfoRow
label="Company"
value={
profile.companyName &&
profile.companyName.toLowerCase() !== "na"
? profile.companyName
: null
}
/>
<InfoRow
label="Income"
value={profile.incomeValue}
/>

</div>

<div className="
bg-card
rounded-2xl
border
border-border
p-8
">

<div className="flex items-center gap-3 mb-6">

<Leaf className="w-7 h-7 text-green-400 shrink-0"/>

<h2 className="
text-2xl
font-semibold
text-white
">

Lifestyle

</h2>

</div>

<InfoRow
label="Diet"
value={profile.dietValue}
/>

<InfoRow
label="Body Type"
value={profile.bodyTypeName}
/>

<InfoRow
label="Complexion"
value={profile.complexionName}
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
{/* FAMILY DETAILS */}

<div className="
bg-card
rounded-2xl
border
border-border
p-8
">

<div className="flex items-center gap-3 mb-6">

<Home className="w-7 h-7 text-orange-400 shrink-0"/>

<h2 className="
text-2xl
font-semibold
text-white
">

{t.profileDetails.familyDetails}

</h2>

</div>

<InfoRow
label={t.profileDetails.familyType}
value={profile.familyType}
/>

<InfoRow
label="Father Name"
value={profile.fatherName}
/>

<InfoRow
label={t.profileDetails.fatherOccupation}
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
label={t.profileDetails.siblings}
value={profile.siblingsCount}
/>

</div>
<div className="
bg-card
rounded-2xl
border
border-border
p-10
">

<div className="flex items-center gap-3 mb-6">

<Heart className="w-7 h-7 text-primary shrink-0"/>

<h2 className="
text-xl
font-semibold
text-white
">

Partner Preferences

</h2>

</div>

<InfoRow
label="Preferred Age"
value={
profile.preferredAgeMin &&
profile.preferredAgeMax
? `${profile.preferredAgeMin} - ${profile.preferredAgeMax} Years`
: null
}
/>

<InfoRow
label="Preferred Location"
value={profile.preferredLocation}
/>

<InfoRow
label="Preferred Education"
value={profile.preferredEducation}
/>

<InfoRow
label="Expectations"
value={profile.otherExpectations}
/>

</div>

</div>

</div>
</div>

</div>

);

};

export default ProfileDetails;