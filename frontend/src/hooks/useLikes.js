import {

useState,

useCallback

}

from "react";

import {

swipeAPI

}

from "@/services/swipeAPI";

const useLikes=()=>{

const [

likedIds,

setLikedIds

]=useState(

new Set()

);

const isLiked=

useCallback(

(id)=>{

return likedIds.has(

Number(id)

);

},

[likedIds]

);

const toggleLike=

useCallback(

async(id)=>{

const num=

Number(id);

const liked=

likedIds.has(

num

);

if(liked){

await swipeAPI.remove(

num

);

setLikedIds(

prev=>{

const next=

new Set(prev);

next.delete(

num

);

return next;

}

);

}else{

await swipeAPI.like(

num

);

setLikedIds(

prev=>

new Set([

...prev,

num

])

);

}

},

[likedIds]

);

return{

isLiked,

toggleLike

};

};

export default useLikes;