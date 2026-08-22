import { useEffect } from "react";

const OnlineHeartbeat = () => {
  useEffect(() => {
    const token = sessionStorage.getItem("token") || localStorage.getItem("token");

    if (!token) return;

    const ping = async () => {
      try {
        await fetch("/api/chat/ping", {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
      } catch (e) {
        console.error(e);
      }
    };

    // First ping
    ping();

    // Every 30 seconds
    const interval = setInterval(ping, 30000);

    return () => {
      clearInterval(interval);

      fetch("/api/chat/offline", {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
    };
  }, []);

  return null;
};

export default OnlineHeartbeat;