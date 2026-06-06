import { useEffect,useState } from "react";
import Navbar from "@/components/Navbar";
import { useNavigate } from "react-router-dom";
import { interestAPI,profileAPI } from "@/services/api";

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

new Event(
"interestUpdated"
)

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

<Navbar/>

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
bg-card
rounded-xl
border
p-5
flex
justify-between
items-center
"

>

<div>

<h2 className="font-bold">

{profile.firstName}

{" "}

{profile.lastName}

</h2>

<p>

Status:

{profile.status}

</p>

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