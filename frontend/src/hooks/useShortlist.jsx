import { useState, useEffect, useCallback } from 'react';
import { shortlistAPI } from '@/services/shortlistAPI';

const CACHE_KEY = '__shortlist_cache_v1';

if (!window[CACHE_KEY]) {

window[CACHE_KEY] = {

items: [],

loaded: false,

profileIds: new Set()

};

}

export const useShortlist = () => {

const [items,setItems] =
useState(
()=>window[CACHE_KEY].items || []
);

const [loading,setLoading] =
useState(false);

const [error,setError] =
useState(null);

const [page,setPage] =
useState(0);

const load = useCallback(

async()=>{

setLoading(true);

setError(null);

try{

const data =
await shortlistAPI
.getMyShortlists(
page,
20
);

const list =
data?.content ||
data ||
[];

window[CACHE_KEY].items =
list;

window[CACHE_KEY].profileIds =
new Set(

list.map(

s=>Number(
s.profileId
)

)

);

window[CACHE_KEY].loaded =
true;

setItems(list);

window.dispatchEvent(

new CustomEvent(
"shortlist:updated"
)

);

}catch(err){

console.error(
"Failed to load shortlists:",
err
);

setError(err);

}finally{

setLoading(false);

}

},

[page]

);

useEffect(()=>{

const handler=()=>{

setItems(
[...window[CACHE_KEY].items]
);

};

window.addEventListener(

"shortlist:updated",

handler

);

if(
!window[CACHE_KEY]
.loaded
){

load();

}

return ()=>{

window.removeEventListener(

"shortlist:updated",

handler

);

};

},[load]);

const isShortlisted=(profileId)=>{

return window[CACHE_KEY]

.profileIds

.has(

Number(profileId)

);

};

const add = async(profileId)=>{

if(

window[CACHE_KEY]

.profileIds

.has(

Number(profileId)

)

){

return;

}

setLoading(true);

try{

const res =

await shortlistAPI.add(

profileId

);

window[CACHE_KEY]

.profileIds

.add(

Number(profileId)

);

window[CACHE_KEY]

.items = [

res,

...window[CACHE_KEY]

.items.filter(

s=>

Number(
s.profileId
)

!==

Number(
profileId
)

)

];

setItems(
[...window[CACHE_KEY].items]
);

window.dispatchEvent(

new CustomEvent(

"shortlist:updated"

)

);

return res;

}finally{

setLoading(false);

}

};

const remove=async(profileId)=>{

setLoading(true);

try{

await shortlistAPI
.remove(profileId);

window[CACHE_KEY]

.items =

window[CACHE_KEY]

.items

.filter(

s=>

Number(
s.profileId
)

!==

Number(
profileId
)

);

window[CACHE_KEY]

.profileIds

.delete(

Number(
profileId
)

);

setItems(
[...window[CACHE_KEY].items]
);

window.dispatchEvent(

new CustomEvent(

"shortlist:updated"

)

);

}finally{

setLoading(false);

}

};

return {

items,

loading,

error,

page,

setPage,

isShortlisted,

add,

remove,

refresh:load,

count:

(window[CACHE_KEY]

.items || [])

.length

};

};

export default useShortlist;