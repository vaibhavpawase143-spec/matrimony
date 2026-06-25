import { useEffect,useState } from "react";

import { useNavigate } from "react-router-dom";
import { interestAPI,profileAPI } from "@/services/api";
import profile1 from "@/assets/profile1.jpg";
import toast from "react-hot-toast";

const ReceivedInterests = ()=>{

const navigate = useNavigate();

const [profiles,setProfiles] =
useState([]);

useEffect(()=>{

loadReceived();

},[]);

const loadReceived = async()=>{

try{

const currentUser =
JSON.parse(
localStorage.getItem("user")
);

const receiverId =
Number(
currentUser.profile.userId
);

console.log(
"RECEIVER ID:",
receiverId
);

const interests =

await interestAPI
.getReceivedInterests(
receiverId
);

console.log(
"RECEIVED INTERESTS:",
interests
);

console.log(

"RECEIVED INTERESTS:",

interests

);


const pending =

interests.filter(

item=>

item.status==="PENDING"

);

const loaded =
await Promise.all(

pending.map(

async(item)=>{

const profile =
await profileAPI
.getProfileByUserId(
item.senderId
);

return{

...profile,

interestId:
item.id,

status:
item.status

};

}

)

);

setProfiles(
loaded
);

}catch(err){

console.log(err);

}

};
const updateStatus = async(

interestId,

action

)=>{

try{

if(

action==="accept"

){

await interestAPI
.acceptInterest(

interestId

);

}

else{

await interestAPI
.rejectInterest(

interestId

);

}

toast.success(

`Interest ${action}ed`

);

setProfiles(

prev =>

prev.filter(

item =>

item.interestId !== interestId

)

);

// refresh dashboard values

window.dispatchEvent(
    new Event("interestUpdated")
);

window.dispatchEvent(
    new Event("dashboardUpdated")
);

}

catch(err){

console.log(err);

toast.error(

"Failed"

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
border
rounded-lg
"

>

← Back

</button>

<h1 className="text-3xl font-bold">

Received Interests

</h1>

</div>

<div className="grid gap-4">

{

profiles.map(profile=>(

<div

key={profile.id}

className="
bg-white
rounded-2xl
border-l-4
border-pink-500
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
    border-pink-200
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



    <span
      className="
      inline-flex
      items-center
      mt-2
      px-3
      py-1
      rounded-full
      text-xs
      font-semibold
      bg-amber-100
      text-amber-700
      "
    >
      ⏳ Pending Request
    </span>

  </div>

</div>
<div className="flex gap-3">

<button

onClick={()=>navigate(

`/profile/${profile.id}`

)}

className="
px-4
py-2
bg-blue-600
text-white
rounded-lg
"

>

View Profile

</button>

<button

onClick={()=>updateStatus(

profile.interestId,

"accept"

)}

className="
px-4
py-2
bg-green-600
text-white
rounded-lg
"

>

Accept

</button>

<button

onClick={()=>updateStatus(

profile.interestId,

"reject"

)}

className="
px-4
py-2
bg-red-600
text-white
rounded-lg
"

>

Reject

</button>

</div>
</div>

))

}

</div>

</div>

</div>

);

};

export default ReceivedInterests;