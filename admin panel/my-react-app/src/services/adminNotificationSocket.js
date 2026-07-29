import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let client = null;

const playNotificationSound = () => {
  const audio = new Audio("/microsammy-ak-47-firing-8760.mp3");

  audio.volume = 0.35;

  audio.play().catch((error) => {
    console.warn("Notification sound blocked by browser:", error);
  });
};

export const connectAdminNotifications = (adminId, onMessage) => {
  console.log("CONNECTING ADMIN WS:", adminId);

  const token =
    localStorage.getItem("adminToken") ||
    localStorage.getItem("authToken");

  if (!token || !adminId) {
    console.warn(
      "Admin WebSocket skipped: token or adminId missing"
    );
    return;
  }

  // Prevent duplicate connection
  if (client?.active && client.adminId === Number(adminId)) {
    console.log(
      "Admin Notification WebSocket already connected:",
      adminId
    );
    return;
  }

  // Close previous connection
  if (client) {
    client.deactivate();
    client = null;
  }

  const socket = new SockJS(
    `https://localhost:9090/ws?token=${encodeURIComponent(token)}`,
    null,
    {
      transports: ["websocket"],
    }
  );

  const newClient = new Client({
    webSocketFactory: () => socket,

    reconnectDelay: 5000,

    heartbeatIncoming: 10000,
    heartbeatOutgoing: 10000,

    debug: (msg) => {
      console.log("ADMIN STOMP:", msg);
    },

    onConnect: () => {
      if (client !== newClient) {
        console.log("Ignoring stale admin connection");
        return;
      }

      console.log("ADMIN WS CONNECTED");

      const destination = `/topic/admin-notifications/${adminId}`;

      console.log("SUBSCRIBING:", destination);

      newClient.subscribe(destination, (message) => {
        try {
          const notification = JSON.parse(message.body);

          console.log(
            "🔥 LIVE ADMIN NOTIFICATION:",
            notification
          );

          playNotificationSound();

          if (typeof onMessage === "function") {
            onMessage(notification);
          }
        } catch (error) {
          console.error(
            "Admin notification parse failed",
            error,
            message.body
          );
        }
      });

      console.log("ADMIN SUBSCRIBED");
    },

    onWebSocketClose: (event) => {
      console.log("ADMIN WS CLOSED", event);
    },

    onWebSocketError: (error) => {
      console.log("ADMIN WS ERROR", error);
    },

    onStompError: (frame) => {
      console.log("ADMIN STOMP ERROR", frame);
    },
  });

  newClient.adminId = Number(adminId);

  client = newClient;

  newClient.activate();
};

export const disconnectAdminNotifications = async () => {
  const currentClient = client;

  client = null;

  if (currentClient) {
    await currentClient.deactivate();
  }
};