import api from "./api";

const API_BASE = "/api/chat";

export const getMyChats = async () => {

    const response = await fetch(

        `${API_BASE}/my-conversations`,

        {

            headers: {

                Authorization:

                    `Bearer ${localStorage.getItem("token")}`

            }

        }

    );

    return await response.json();

};

export const getConversations = ()=>{

return api.get(

"/chat/conversations"

);

};

export const getMessages = (

    otherUserId,

    page = 0,

    size = 100

) => {

    return api.get(

        `/chat/messages?otherUserId=${otherUserId}&page=${page}&size=${size}`

    );

};

export const sendMessage = async (data) => {

    const response = await fetch(

        `${API_BASE}/send`,

        {

            method: "POST",

            headers: {

                "Content-Type": "application/json",

                Authorization:

                    `Bearer ${localStorage.getItem("token")}`

            },

            body: JSON.stringify(data)

        }

    );

    return await response.json();

};