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

  export const connectNotifications = (userId, onMessage) => {
    console.log("CONNECTING TO WS:", userId);

    const token = localStorage.getItem("token");

    if (!token || !userId) {
      console.warn(
        "WebSocket connection skipped: token or userId missing"
      );
      return;
    }

    // Same user already connected असेल तर duplicate connection नको
    if (client?.active && client.userId === Number(userId)) {
      console.log("Notification WebSocket already connected for user:", userId);
      return;
    }

    // जुना connection बंद कर
    if (client) {
      client.deactivate();
      client = null;
    }

    const socket = new SockJS(
      `http://localhost:9090/ws?token=${encodeURIComponent(token)}`,
      null,
      {
        transports: ["websocket"]
      }
    );

    // Local variable वापरतो: callback मध्ये global client null झाला तरी safe राहील
    const newClient = new Client({
      webSocketFactory: () => socket,

      reconnectDelay: 5000,

      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,

      debug: (msg) => {
        console.log("STOMP:", msg);
      },

      onConnect: () => {
        // हा जुना/stale connection असेल तर subscribe करू नको
        if (client !== newClient) {
          console.log("Ignoring stale notification WebSocket connection");
          return;
        }

        console.log("CONNECTED");

        const destination = `/topic/notifications/${userId}`;

        console.log("SUBSCRIBING TO:", destination);

        newClient.subscribe(destination, (message) => {
          try {
            const notification = JSON.parse(message.body);

            console.log("🔥 LIVE NOTIFICATION OBJECT:", notification);

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

            if (typeof onMessage === "function") {
              onMessage(notification);
            }
          } catch (error) {
            console.error(
              "Notification JSON parse failed:",
              error,
              message.body
            );
          }
        });

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

    newClient.userId = Number(userId);
    client = newClient;

    newClient.activate();
  };

  export const disconnectNotifications = async () => {
    const currentClient = client;

    // आधी global reference null करतो,
    // त्यामुळे delayed onConnect stale आहे हे ओळखेल
    client = null;

    if (currentClient) {
      await currentClient.deactivate();
    }
  };