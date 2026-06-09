import { useState } from "react";
import { Star } from "lucide-react";
import useShortlist from "@/hooks/useShortlist";
import toast from "react-hot-toast";

const ShortlistButton = ({
profileId,
size="md",
showLabel=true

})=>{

const {

isShortlisted,
add,
remove,
loading

}=useShortlist();

const [

localLoading,
setLocalLoading

]=useState(false);

const shortlisted =
isShortlisted(profileId);

const handleClick =
async(e)=>{

e.stopPropagation();

if(
localLoading ||
loading
){

return;

}

setLocalLoading(
true
);

try{

if(
shortlisted
){

await remove(
profileId
);

window.dispatchEvent(

new Event(

"shortlist:updated"

)

);

toast.success(
"Removed from shortlist"
);

}else{

await add(
profileId
);

window.dispatchEvent(

new Event(

"shortlist:updated"

)

);

toast.success(
"Added to shortlist"
);

}

}catch(err){

console.log(err);

toast.error(

err.message ||

"Action failed"

);

}finally{

setLocalLoading(
false
);

}

};

const iconSizes={

sm:"h-4 w-4",

md:"h-5 w-5",

lg:"h-6 w-6"

};

return(

<button

onClick={handleClick}

disabled={
localLoading ||
loading
}

className={`

${

showLabel

?

`

w-full

rounded-lg

py-3

flex

items-center

justify-center

gap-2

bg-yellow-600

hover:bg-yellow-700

`

:

`

w-12

h-12

rounded-xl

bg-gradient-to-br

from-purple-500

to-indigo-600

hover:scale-110

`

}

text-white

shadow-md

transition-all

duration-300

disabled:opacity-70

`}

title={

shortlisted

?

"Shortlisted"

:

"Shortlist"

}

>

<Star

className={`

${iconSizes[size]}

${

shortlisted

?

"fill-current"

:

""

}

`}

/>

{

showLabel && (

<span>

{

shortlisted

?

"Shortlisted"

:

"Shortlist"

}

</span>

)

}

</button>

);

};

export default ShortlistButton;