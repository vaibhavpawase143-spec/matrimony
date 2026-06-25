import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let client = null;

const playNotificationSound = () => {
  const audio = new Audio(
    "/microsammy-ak-47-firing-8760.mp3"
  );

  audio.volume = 0.35;

  audio.play().catch((error) => {
    console.warn(
      "Notification sound blocked by browser:",
      error
    );
  });
};

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

  const token = localStorage.getItem("token");

  if (!token || !userId) {
    console.warn(
      "WebSocket connection skipped: token or userId missing"
    );
    return;
  }

  const socket = new SockJS(
    `http://localhost:9090/ws?token=${token}`,
    null,
    {
      transports: ["websocket"]
    }
  );

  client = new Client({
    webSocketFactory: () => socket,

    reconnectDelay: 5000,

    debug: (msg) => {
      console.log("STOMP:", msg);
    },

    onConnect: () => {
      console.log("CONNECTED");

      console.log(
        "SUBSCRIBING TO:",
        `/topic/notifications/${userId}`
      );

      client.subscribe(
        `/topic/notifications/${userId}`,
        (message) => {
          try {
            const notification = JSON.parse(
              message.body
            );

            console.log(
              "🔥 LIVE NOTIFICATION OBJECT:",
              notification
            );

          const currentUser = JSON.parse(
            localStorage.getItem("user") || "{}"
          );

          const currentUserId = Number(
            currentUser?.profile?.userId ||
            currentUser?.userId ||
            currentUser?.id
          );

          if (
            currentUserId &&
            Number(notification.receiverId) === currentUserId
          ) {
            playNotificationSound();
          }

          if (onMessage) {
            onMessage(notification);
          }
          } catch (error) {
            console.error(
              "Notification JSON parse failed:",
              error,
              message.body
            );
          }
        }
      );

      console.log("SUBSCRIBED SUCCESS");
    },

    onWebSocketClose: (event) => {
      console.log("WS CLOSED:", event);
    },

    onWebSocketError: (err) => {
      console.log("WS ERROR:", err);
    },

    onStompError: (frame) => {
      console.log("STOMP ERROR:", frame);
    }
  });

  client.activate();
};

export const disconnectNotifications = async () => {
  if (client) {
    await client.deactivate();
    client = null;
  }
};