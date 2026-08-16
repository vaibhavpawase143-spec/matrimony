import { useNavigate } from "react-router-dom";
import { useMemo } from "react";
import profile1 from "@/assets/profile1.jpg";

function formatTimeAgo(dateString) {
  if (!dateString) return "";
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return "";

  const now = new Date();
  const diffInSeconds = Math.floor((now - date) / 1000);

  if (diffInSeconds < 60) return "Just now";
  if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}m ago`;
  if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)}h ago`;
  if (diffInSeconds < 604800) return `${Math.floor(diffInSeconds / 86400)}d ago`;
  return date.toLocaleDateString();
}

const RecentActivity = ({
  visitors = [],
  receivedInterests = [],
  shortlists = [],
  sentInterests = [],
  activities: providedActivities = null,
  loading = false
}) => {
  const navigate = useNavigate();

  const activities = useMemo(() => {
    if (providedActivities && Array.isArray(providedActivities)) {
      return providedActivities;
    }

    const items = [];

    // 1. Visitors
    (Array.isArray(visitors) ? visitors : []).forEach((v) => {
      const name =
        v.fullName ||
        `${v.firstName || ""} ${v.lastName || ""}`.trim() ||
        "Someone";
      const profileId = v.profileId || v.userId;
      items.push({
        id: `visitor-${v.userId || v.profileId}-${v.viewedAt || Math.random()}`,
        type: "VISITOR",
        name,
        imageUrl: v.imageUrl || null,
        title: `${name} viewed your profile`,
        icon: "👀",
        badge: "View",
        timestamp: v.viewedAt || v.createdAt || v.updatedAt,
        route: profileId ? `/profile/${profileId}` : "/profile-visitors"
      });
    });

    // 2. Received Interests
    (Array.isArray(receivedInterests) ? receivedInterests : []).forEach((r) => {
      const name =
        r.senderName ||
        r.senderFullName ||
        (r.senderFirstName ? `${r.senderFirstName} ${r.senderLastName || ""}`.trim() : null) ||
        (r.senderId ? `User #${r.senderId}` : "Someone");
      const profileId = r.senderProfileId || r.profileId || r.senderId;
      items.push({
        id: `received-${r.id || r.interestId || Math.random()}`,
        type: "RECEIVED_INTEREST",
        name,
        imageUrl: r.senderImageUrl || r.imageUrl || null,
        title: `${name} sent you an interest`,
        icon: "❤️",
        badge: "Interest",
        timestamp: r.createdAt || r.updatedAt,
        route: profileId ? `/profile/${profileId}` : "/received-interests"
      });
    });

    // 3. Sent Interests
    (Array.isArray(sentInterests) ? sentInterests : []).forEach((s) => {
      const name =
        s.receiverName ||
        s.receiverFullName ||
        (s.receiverFirstName ? `${s.receiverFirstName} ${s.receiverLastName || ""}`.trim() : null) ||
        (s.receiverId ? `User #${s.receiverId}` : "Someone");
      const profileId = s.receiverProfileId || s.profileId || s.receiverId;
      items.push({
        id: `sent-${s.id || s.interestId || Math.random()}`,
        type: "SENT_INTEREST",
        name,
        imageUrl: s.receiverImageUrl || s.imageUrl || null,
        title: `You sent an interest to ${name}`,
        icon: "💕",
        badge: "Sent",
        timestamp: s.createdAt || s.updatedAt,
        route: profileId ? `/profile/${profileId}` : "/sent-interests"
      });
    });

    // 4. Shortlists
    (Array.isArray(shortlists) ? shortlists : []).forEach((sl) => {
      const name =
        sl.profileName ||
        sl.fullName ||
        (sl.firstName ? `${sl.firstName} ${sl.lastName || ""}`.trim() : null) ||
        (sl.profileId ? `Profile #${sl.profileId}` : "a profile");
      const profileId = sl.profileId || sl.shortlistedUserId;
      items.push({
        id: `shortlist-${sl.id || sl.profileId || Math.random()}`,
        type: "SHORTLIST",
        name,
        imageUrl: sl.imageUrl || null,
        title: `Added ${name} to shortlist`,
        icon: "⭐",
        badge: "Shortlist",
        timestamp: sl.createdAt || sl.updatedAt,
        route: profileId ? `/profile/${profileId}` : "/shortlists"
      });
    });

    // Sort descending by timestamp (newest -> oldest)
    items.sort((a, b) => {
      const timeA = a.timestamp ? new Date(a.timestamp).getTime() : 0;
      const timeB = b.timestamp ? new Date(b.timestamp).getTime() : 0;
      return timeB - timeA;
    });

    return items.slice(0, 5);
  }, [visitors, receivedInterests, shortlists, sentInterests, providedActivities]);

  return (
    <div className="bg-card rounded-2xl border border-border p-6 shadow-sm">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-lg font-semibold text-foreground flex items-center gap-2">
          <span>⚡</span> Recent Activity
        </h3>
        {activities.length > 0 && (
          <span className="text-xs text-muted-foreground bg-muted px-2.5 py-1 rounded-full font-medium">
            {activities.length} recent
          </span>
        )}
      </div>

      {loading ? (
        <div className="space-y-3 animate-pulse">
          {[1, 2, 3].map((n) => (
            <div key={n} className="flex items-center justify-between p-3 rounded-xl bg-muted/50">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-muted"></div>
                <div className="space-y-1">
                  <div className="h-4 w-40 bg-muted rounded"></div>
                  <div className="h-3 w-20 bg-muted/70 rounded"></div>
                </div>
              </div>
              <div className="h-3 w-12 bg-muted rounded"></div>
            </div>
          ))}
        </div>
      ) : activities.length === 0 ? (
        <div className="p-4 rounded-xl bg-muted/40 text-center text-muted-foreground text-sm font-medium">
          No recent activity yet.
        </div>
      ) : (
        <div className="space-y-3">
          {activities.map((item) => (
            <div
              key={item.id}
              onClick={() => navigate(item.route)}
              className="flex items-center justify-between p-3 rounded-xl bg-muted/40 hover:bg-muted/80 cursor-pointer transition-all duration-200 group border border-transparent hover:border-border"
            >
              <div className="flex items-center gap-3 min-w-0">
                <div className="relative flex-shrink-0">
                  <img
                    src={item.imageUrl || profile1}
                    alt={item.name}
                    className="w-10 h-10 rounded-full object-cover border border-border"
                    onError={(e) => {
                      e.currentTarget.src = profile1;
                    }}
                  />
                  <span className="absolute -bottom-1 -right-1 text-xs bg-background rounded-full p-0.5 shadow-sm">
                    {item.icon}
                  </span>
                </div>

                <div className="min-w-0 flex-1">
                  <p className="text-sm font-medium text-foreground truncate group-hover:text-primary transition-colors">
                    {item.title}
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {formatTimeAgo(item.timestamp) || item.badge}
                  </p>
                </div>
              </div>

              <div className="text-xs font-semibold text-primary opacity-0 group-hover:opacity-100 transition-opacity pl-2 flex-shrink-0">
                View →
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default RecentActivity;