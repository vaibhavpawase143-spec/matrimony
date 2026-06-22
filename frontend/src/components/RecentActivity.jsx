import { useNavigate } from "react-router-dom";
const RecentActivity = ({
  visitors = [],
  receivedInterests = [],
  shortlists = [],
  sentInterests = []
}) => {
const navigate = useNavigate();
  return (

    <div
      className="
      bg-card
      rounded-2xl
      border
      border-border
      p-6
      "
    >

      <h3
        className="
        text-lg
        font-semibold
        mb-4
        "
      >
        Recent Activity
      </h3>

    <div className="space-y-3">

    {visitors.length > 0 && (
    <div
      onClick={() =>
        navigate("/profile-visitors")
      }
      className="
      p-3
      rounded-lg
      bg-muted
      cursor-pointer
      hover:bg-muted/80
      transition
      "
    >
      👀 {visitors[0]?.fullName} viewed your profile
    </div>
    )}

      {receivedInterests.length > 0 && (
    <div
      onClick={() =>
        navigate("/received-interests")
      }
      className="
      p-3
      rounded-lg
      bg-muted
      cursor-pointer
      hover:bg-muted/80
      transition
      "
    >
      ❤️ {receivedInterests.length} interest(s) received
    </div>
      )}

      {shortlists.length > 0 && (
     <div
       onClick={() =>
         navigate("/shortlists")
       }
       className="
       p-3
       rounded-lg
       bg-muted
       cursor-pointer
       hover:bg-muted/80
       transition
       "
     >
       ⭐ {shortlists.length} shortlist(s)
     </div>
      )}
{sentInterests.length > 0 && (
  <div
    onClick={() =>
      navigate("/sent-interests")
    }
    className="
    p-3
    rounded-lg
    bg-muted
    cursor-pointer
    hover:bg-muted/80
    transition
    "
  >
    💕 {sentInterests.length} interest(s) sent
  </div>
)}
    </div>

    </div>

  );

};

export default RecentActivity;