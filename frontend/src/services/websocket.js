import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let client = null;

export const connectNotifications = (

userId,

onMessage


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

console.log(

"STOMP:",

msg

);

},

reconnectDelay:5000,

onConnect:()=>{

console.log(

"CONNECTED"

);

console.log(

"SUBSCRIBING..."

);

client.subscribe(

`/topic/notifications/${userId}`,

(message)=>{

console.log(

"MESSAGE RECEIVED:",

message.body

);

console.log(

"RAW MESSAGE:",

message.body

);

onMessage({

message:

message.body

});

}

);

console.log(

"SUBSCRIBED SUCCESS"

);

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

export const disconnectNotifications = ()=>{

if(client){

client.deactivate();

client = null;

}

};