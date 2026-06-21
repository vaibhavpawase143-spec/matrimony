import { apiClient as api } from "./api";

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

export const getConversations = () => {

return api(

"/chat/conversations"

);

};
export const getMessages = (

    otherUserId,

    page = 0,

    size = 100

) => {

   return api(

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

export const markSeen = (conversationId) => {

    return api(

        `/chat/seen/${conversationId}`,

        {

            method: "PUT"

        }

    );

};
export const uploadImage = async (file) => {

    const formData = new FormData();

    formData.append(
        "file",
        file
    );

    const response = await fetch(

        "/api/chat/upload-image",

        {

            method: "POST",

            headers: {

                Authorization:

                `Bearer ${localStorage.getItem("token")}`

            },

            body: formData

        }

    );

    return await response.json();

};
export const uploadAudio = async (file) => {

    const formData = new FormData();

    formData.append(
        "file",
        file
    );

    const response = await fetch(

        "/api/chat/upload-audio",

        {

            method:"POST",

            headers:{

                Authorization:
                `Bearer ${localStorage.getItem("token")}`

            },

            body:formData

        }

    );

    return await response.json();

};
export const uploadVideo = async (file) => {

    const formData = new FormData();

    formData.append(
        "file",
        file
    );

    const response =await fetch(
                        "http://localhost:9090/api/chat/upload-video",
        {

            method: "POST",

            headers: {

                Authorization:
                `Bearer ${localStorage.getItem("token")}`

            },

            body: formData

        }

    );

    return await response.json();

};

export const uploadDocument =
async(file)=>{

const formData =
new FormData();

formData.append(
"file",
file
);

const response =
await fetch(

"/api/chat/upload-document",

{
method:"POST",

headers:{
Authorization:
`Bearer ${localStorage.getItem("token")}`
},

body:formData
}

);

return await response.json();

};

export const deleteForMe = (id) =>
  api(
    `/chat/messages/${id}/me`,
    {
      method: "DELETE"
    }
  );

export const deleteForEveryone = (id) =>
  api(
    `/chat/messages/${id}/everyone`,
    {
      method: "DELETE"
    }
  );
  export const pinMessage = (id) =>
    api(
      `/chat/messages/${id}/pin`,
      {
        method: "PUT"
      }
    );

  export const unpinMessage = (id) =>
    api(
      `/chat/messages/${id}/unpin`,
      {
        method: "PUT"
      }
    );
    export const starMessage = (id) =>
        api(
            `/chat/messages/${id}/star`,
            {
                method: "PUT"
            }
        );

    export const unstarMessage = (id) =>
        api(
            `/chat/messages/${id}/unstar`,
            {
                method: "PUT"
            }
        );
    export const forwardMessage = (messageId, receiverId) =>
            api(
                "/chat/forward",
                {
                    method: "POST",
                    body: JSON.stringify({
                        messageId,
                        receiverId
                    })
                }
            );

    export const getChatUser = (id) =>
        api(`/chat/user/${id}`);