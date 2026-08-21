import { useEffect } from "react";

const WebsiteVisitorTracker = () => {
  useEffect(() => {
    const key = "gathbandhan_visitor_hit";

    // Same browser session already counted
    if (sessionStorage.getItem(key) === "true") {
      return;
    }

    const recordVisitor = async () => {
      try {
        const response = await fetch("/api/analytics/visitor", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (response.ok) {
          sessionStorage.setItem(key, "true");
        }
      } catch (error) {
        console.error("Visitor tracking failed:", error);
      }
    };

    recordVisitor();
  }, []);

  return null;
};

export default WebsiteVisitorTracker;