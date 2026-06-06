import { useEffect, useState } from 'react';
import Navbar from '@/components/Navbar';
import { useNavigate } from 'react-router-dom';
import useShortlist from '@/hooks/useShortlist';
import { profileAPI } from '@/services/api';
import toast from 'react-hot-toast';

const MyShortlists = () => {

const navigate = useNavigate();

const {
items,
loading,
error,
remove,
count,
refresh
} = useShortlist();

const [profiles,setProfiles] = useState({});

const [localLoading,setLocalLoading] =
useState(false);

const [page,setPage] =
useState(0);

useEffect(()=>{

refresh();

},[
refresh,
page
]);

useEffect(()=>{

const fetchProfileDetails =
async()=>{

const profileMap = {};

for(const item of items){

if(!profileMap[item.profileId]){

try{

const profile =
await profileAPI.getProfileById(
item.profileId
);

profileMap[item.profileId] =
profile;

}catch(err){

console.error(
`Failed profile ${item.profileId}`,
err
);

}

}

}

setProfiles(profileMap);

};

if(items.length>0){

fetchProfileDetails();

}

},[items]);

const openProfile=(profileId)=>{

navigate(
`/profile/${profileId}`
);

};

const handleRemove=
async(profileId)=>{

setLocalLoading(true);

try{

await remove(profileId);

toast.success(
'Removed from shortlists'
);

}catch(err){

console.error(err);

toast.error(
'Failed to remove'
);

}finally{

setLocalLoading(false);

}

};

return (

<div className="min-h-screen bg-muted/30">

<Navbar />

<div className="container mx-auto px-4 py-8">

<h1 className="text-2xl font-bold text-white mb-4">

My Shortlists ({count})

</h1>

{loading && (

<p className="text-muted-foreground">

Loading...

</p>

)}

{error && (

<p className="text-red-500">

{error.message}

</p>

)}

{!loading && items.length===0 && (

<p className="text-muted-foreground">

No shortlists yet

</p>

)}

<div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">

{items.map((s)=>{

const profile =
profiles[s.profileId] || {};

return(

<div

key={s.profileId}

className="
bg-card
rounded-xl
p-4
border
border-border
flex
items-center
gap-4
"

>

<div className="w-20 h-20 rounded overflow-hidden">

<img

src={
profile.imageUrl ||
'/default-profile.png'
}

alt="profile"

className="
w-full
h-full
object-cover
"

/>

</div>

<div className="flex-1">

<div className="font-semibold">

{profile.firstName}

{" "}

{profile.lastName}

</div>

<div className="text-sm text-muted-foreground">

{profile.cityName}

</div>

<div className="flex gap-2 mt-3">

<button

onClick={()=>openProfile(
s.profileId
)}

className="
px-3
py-2
rounded
bg-white
text-black
"

>

View

</button>

<button

disabled={localLoading}

onClick={()=>handleRemove(
s.profileId
)}

className="
px-3
py-2
rounded
bg-red-500
text-white
"

>

Remove

</button>

</div>

</div>

</div>

);

})}

</div>


<div className="flex gap-4 mt-6 justify-center">

<button

disabled={page===0}

onClick={()=>setPage(
prev=>prev-1
)}

className="
px-4
py-2
bg-gray-700
rounded
disabled:opacity-50
"

>

Previous

</button>

<button

onClick={()=>setPage(
prev=>prev+1
)}

className="
px-4
py-2
bg-gray-700
rounded
"

>

Next

</button>

</div>

</div>

</div>

);

};

export default MyShortlists;