import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let client = null;

export const connectNotifications = (
  userId,
  onMessage
) => {

  console.log(
    "CONNECTING TO WS:",
    userId
  );

  if (client) {

    client.deactivate();

    client = null;

  }

  const token =
    localStorage.getItem("token");

  const socket = new SockJS(
    `http://localhost:9090/ws?token=${token}`
  );

  client = new Client({

    webSocketFactory: () => socket,

    reconnectDelay: 5000,

    debug: (msg) => {

      console.log(
        "STOMP:",
        msg
      );

    },

    onConnect: () => {

      console.log(
        "CONNECTED"
      );

      console.log(
        "SUBSCRIBING TO:",
        `/topic/notifications/${userId}`
      );

     client.subscribe(

       `/topic/notifications/${userId}`,

       (message) => {

         alert(
           "MESSAGE RECEIVED = " +
           message.body
         );

         console.log(
           "🔥 MESSAGE RECEIVED:",
           message.body
         );

         onMessage({
           message: message.body
         });

       }

     );
      console.log(
        "SUBSCRIBED SUCCESS"
      );

    },

    onWebSocketClose: (event) => {

      console.log(
        "WS CLOSED:",
        event
      );

    },

    onWebSocketError: (err) => {

      console.log(
        "WS ERROR:",
        err
      );

    },

    onStompError: (frame) => {

      console.log(
        "STOMP ERROR:",
        frame
      );

    }

  });

  client.activate();

};

export const disconnectNotifications = () => {

  if (client) {

    client.deactivate();

    client = null;

  }

};