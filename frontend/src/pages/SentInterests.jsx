import { useEffect,useState } from "react";
import profile1 from "@/assets/profile1.jpg";
import { interestAPI, profileAPI } from "@/services/api";
import { useNavigate } from "react-router-dom";

const SentInterests = () => {

    const navigate = useNavigate();

    const [profiles, setProfiles] = useState([]);


useEffect(()=>{

loadSentInterests();

window.addEventListener(
"interestUpdated",
loadSentInterests
);

return ()=>{

window.removeEventListener(
"interestUpdated",
loadSentInterests
);

};

},[]);

const loadSentInterests = async()=>{

try{

const currentUser =
JSON.parse(
localStorage.getItem("user")
);

const senderId =
Number(
currentUser.profile.userId
);

console.log(
"SENDER ID:",
senderId
);

const interests =
await interestAPI.getSentInterests(
senderId
);

console.log(
"INTERESTS:",
interests
);

if(
!interests ||
interests.length===0
){

setProfiles([]);

return;

}

const loadedProfiles =
await Promise.all(

interests.map(

async(item)=>{

const profile =

await profileAPI
.getProfileByUserId(
item.receiverId
);

return{

...profile,

interestStatus:
item.status

};

}

)

);

console.log(
"PROFILES:",
loadedProfiles
);

setProfiles(
loadedProfiles
);

}catch(err){

console.log(
"LOAD ERROR:",
err
);

}

};
return(

<div className="min-h-screen bg-muted/30">



<div className="container mx-auto p-6">

<div className="
flex
items-center
gap-4
mb-6
">

<button

onClick={()=>{

window.location.href =

"/home";

}}

className="
px-4
py-2
rounded-lg
border
hover:bg-muted
transition
"

>

← Back

</button>

<h1 className="
text-3xl
font-bold
">

Sent Interests

</h1>

</div>

<div className="grid gap-4">

{

profiles.length===0 && (

<div className="

bg-card
border
rounded-xl
p-10
text-center

">

<h2 className="text-xl font-bold">

No Sent Interests Yet

</h2>

<p className="text-muted-foreground mt-2">

Start sending interests to discover matches ❤️

</p>

</div>

)

}

{

profiles.map(profile=>(

<div

key={profile.id}

className="
bg-white
rounded-2xl
border
shadow-sm
hover:shadow-lg
transition-all
duration-300
p-6
flex
justify-between
items-center
"

>

<div className="flex items-center gap-4">

<img
  src={profile.imageUrl || profile1}
  alt={profile.firstName}
  className="
    h-16
    w-16
    rounded-full
    object-cover
    border-2
    border-purple-200
  "
/>

  <div>

    <h2
      className="
      text-xl
      font-bold
      text-slate-800
      "
    >
      {profile.firstName} {profile.lastName}
    </h2>



    <div className="mt-2">
<span

className={`

px-3
py-1
rounded-full
text-sm
font-semibold

${

profile.interestStatus==="PENDING"

?

'bg-yellow-500/20 text-yellow-400'

:

profile.interestStatus==="ACCEPTED"

?

'bg-green-500/20 text-green-400'

:

'bg-red-500/20 text-red-400'

}

`}

>

{

profile.interestStatus==="PENDING"

?

"🟡 Pending"

:

profile.interestStatus==="ACCEPTED"

?

"🟢 Accepted"

:

"🔴 Rejected"

}

</span>

</div>
</div>
</div>
<div className="flex gap-3">

    <button
        onClick={() => navigate(`/profile/${profile.id}`)}
        className="
        px-4
        py-2
        rounded-lg
        bg-primary
        text-white
        "
    >
        View Profile
    </button>

    {
        profile.interestStatus === "ACCEPTED" && (

            <button
 onClick={() => {
     navigate(`/messages?receiverId=${profile.userId}`);
 }}
                className="
                px-4
                py-2
                rounded-lg
                bg-pink-600
                text-white
                "
            >
                💬 Message
            </button>

        )
    }

</div>

</div>

))

}

</div>


</div>
</div>

);

};

export default SentInterests;