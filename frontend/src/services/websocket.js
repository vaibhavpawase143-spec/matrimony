import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let client = null;
let typingCallback = null;
let heartbeatInterval = null;

export const connectNotifications=(

userId,

onMessage,

onTyping,

onStatus

)=>{

if(client){

client.deactivate();

}

const token = localStorage.getItem(

"token"

);

const socket = new SockJS(

`http://localhost:9090/ws?token=${token}`,

null,

{

transports:["websocket"]

}

);

client = new Client({

    webSocketFactory:()=>socket,

    debug:(msg)=>{

        console.log("STOMP:",msg);

    },

    reconnectDelay:5000,

    heartbeatIncoming:10000,

    heartbeatOutgoing:10000,

    onConnect:()=>{

    console.log("CONNECTED");

    console.log("SUBSCRIBING...");

    // ================= NOTIFICATIONS =================

   client.subscribe(

       `/topic/notifications/${userId}`,

       (message) => {

           const body = JSON.parse(message.body);

           console.log("LIVE NOTIFICATION =", body);

           if (onMessage) {

               onMessage(body);

           }

       }

   );
    // ================= TYPING =================

    typingCallback = onTyping;

    client.subscribe(

        "/user/queue/typing",

        (message) => {

            const body = JSON.parse(message.body);

            if (typingCallback) {

                typingCallback(body);

            }

        }

    );

    // ================= ONLINE / OFFLINE STATUS =================

    client.subscribe(

        "/topic/status",

        (message) => {

            const body = JSON.parse(message.body);

            console.log("STATUS EVENT =", body);

            if (onStatus) {

                onStatus(body);

            }

        }

    );
// ================= LIVE CHAT =================

client.subscribe(

    "/user/queue/messages",

    (message) => {

        const body = JSON.parse(message.body);

        console.log("NEW MESSAGE =", body);

        if (onMessage) {

            onMessage(body);

        }

    }

);
    console.log("SUBSCRIBED SUCCESS");

   // ================= HEARTBEAT =================

   heartbeatInterval = setInterval(() => {

       if (client && client.connected) {

           fetch("http://localhost:9090/api/heartbeat", {
               method: "POST",
               headers: {
                   "Authorization": `Bearer ${token}`
               }
           })
           .then((response) => {

               if (!response.ok) {
                   console.error("Heartbeat Failed:", response.status);
               }

           })
           .catch((err) => {

               console.error("Heartbeat Error:", err);

           });

       }

   }, 20000);

},

onWebSocketClose:(event)=>{

console.log(

"WS CLOSED:",

event

);

},

onWebSocketError:(err)=>{

console.log(

"WS ERROR:",

err

);

},

onStompError:(frame)=>{

console.log(

"STOMP ERROR:",

frame

);

}

});

client.activate();

};
export const sendTyping = (

receiverEmail

)=>{

if(

client &&

client.connected

){

client.publish({

destination:"/app/typing",

body:JSON.stringify({

receiver:receiverEmail

})

});

}

};
export const stopTyping = (

receiverEmail

)=>{

if(

client &&

client.connected

){

client.publish({

destination:"/app/stop-typing",

body:JSON.stringify({

receiver:receiverEmail

})

});

}

};

export const disconnectNotifications = async () => {

    if (heartbeatInterval) {

        clearInterval(heartbeatInterval);

        heartbeatInterval = null;

    }

    if (client) {

        await client.deactivate();

        client = null;

    }

};
window.addEventListener("beforeunload", () => {

    if (heartbeatInterval) {

        clearInterval(heartbeatInterval);

        heartbeatInterval = null;

    }

    if (client) {

        client.deactivate();

    }

});