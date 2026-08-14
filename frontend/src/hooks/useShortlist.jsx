import { useState, useEffect, useCallback } from 'react';
import { shortlistAPI } from '@/services/shortlistAPI';

const CACHE_KEY = "__shortlist_cache_v2";

if (!window[CACHE_KEY]) {
    window[CACHE_KEY] = {
        items: [],
        loaded: false,
        loading: false,
        loadPromise: null,
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

const load = useCallback(async (force = false) => {

    const cache = window[CACHE_KEY];

    // Already loaded → API call नको
    if (cache.loaded && !force) {
        setItems([...cache.items]);
        return;
    }

    // दुसरा component आधीच API call करत असेल
    if (cache.loadPromise) {
        await cache.loadPromise;
        setItems([...cache.items]);
        return;
    }

    setLoading(true);
    setError(null);
    cache.loading = true;

    cache.loadPromise = (async () => {

        try {

            const data = await shortlistAPI.getMyShortlists(0, 20);

            const list =
                data?.content ||
                data ||
                [];

            cache.items = list;

            cache.profileIds = new Set(
                list
                    .map(s => Number(s.profileId))
                    .filter(Boolean)
            );

            cache.loaded = true;

        } catch (err) {

            console.error(
                "Failed to load shortlists:",
                err
            );

            setError(err);

            throw err;

        } finally {

            cache.loading = false;
            cache.loadPromise = null;

        }

    })();

    try {
        await cache.loadPromise;
        setItems([...cache.items]);
    } catch {
        // error already handled
    } finally {
        setLoading(false);
    }

}, []);

useEffect(() => {

    const handler = () => {
        setItems([...window[CACHE_KEY].items]);
    };

    window.addEventListener(
        "shortlist:updated",
        handler
    );

    load();

    return () => {
        window.removeEventListener(
            "shortlist:updated",
            handler
        );
    };

}, [load]);

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
window.dispatchEvent(
    new Event("dashboardUpdated")
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
window.dispatchEvent(
    new Event("dashboardUpdated")
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