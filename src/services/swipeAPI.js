const API_BASE = "/api/swipes";

const getHeaders = () => {

const token =
localStorage.getItem(
"token"
);

return {

Authorization:
`Bearer ${token}`,

"Content-Type":
"application/json"

};

};

export const swipeAPI = {

like: async (userId) => {

const res =
await fetch(

`${API_BASE}/like/${userId}`,

{
method: "POST",
headers: getHeaders()
}

);

return await res.json();

},

remove: async (userId) => {

await fetch(

`${API_BASE}/${userId}`,

{
method: "DELETE",
headers: getHeaders()
}

);

}, // <-- IMPORTANT COMMA

getReceivedLikes: async () => {

const res = await fetch(

`${API_BASE}/received`,

{
method: "GET",
headers: getHeaders()
}

);

return await res.json();

}

};