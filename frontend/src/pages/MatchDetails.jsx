import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { ArrowLeft, CheckCircle2, XCircle, Heart, User, Sparkles } from "lucide-react";
import { matchAPI } from "@/services/api";
import toast from "react-hot-toast";
import profile1 from "@/assets/profile1.jpg";
import { resolveImageUrl } from "@/utils/urlSecurity";

const MatchDetails = () => {
  const { partnerId } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [match, setMatch] = useState(null);

  useEffect(() => {
    if (partnerId) {
      loadDetails();
    }
  }, [partnerId]);

  const loadDetails = async () => {
    try {
      setLoading(true);
      const currentUser = JSON.parse(
        sessionStorage.getItem("user") ||
        localStorage.getItem("user") || "{}"
      );
      const userId = Number(
        currentUser?.userId ||
        currentUser?.id ||
        currentUser?.profile?.userId
      );

      if (!userId) {
        toast.error("User not found");
        return;
      }

      const response = await matchAPI.getMatchDetails(userId, partnerId);
      console.log("MATCH DETAILS =", response);
      setMatch(response);
    } catch (e) {
      console.error("Failed to load match details:", e);
      toast.error(e?.message || "Unable to load match details");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-muted/30 flex flex-col justify-center items-center gap-4">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        <p className="text-sm font-medium text-muted-foreground">Calculating match compatibility...</p>
      </div>
    );
  }

  if (!match) {
    return (
      <div className="min-h-screen bg-muted/30 flex flex-col items-center justify-center p-6 text-center">
        <div className="w-16 h-16 rounded-full bg-pink-100 flex items-center justify-center text-pink-600 mb-4">
          <Heart className="h-8 w-8" />
        </div>
        <h2 className="text-2xl font-bold text-foreground mb-2">Match Details Not Found</h2>
        <p className="text-muted-foreground mb-6 max-w-md">
          Unable to calculate compatibility breakdown for this profile. Please make sure both profiles and partner preferences are set up.
        </p>
        <button
          onClick={() => navigate("/matches")}
          className="px-6 py-2.5 bg-primary text-primary-foreground font-semibold rounded-xl hover:opacity-90 transition"
        >
          Back to Matches
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-muted/30 pb-16">
      {/* Header Banner */}
      <div
        className="py-10 px-6 text-center text-primary-foreground relative"
        style={{
          background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))"
        }}
      >
        <div className="max-w-4xl mx-auto flex items-center justify-between">
          <button
            onClick={() => navigate(-1)}
            className="flex items-center gap-2 bg-white/20 hover:bg-white/30 text-white px-4 py-2 rounded-xl text-sm font-medium transition backdrop-blur-sm"
          >
            <ArrowLeft size={16} />
            Back
          </button>
          <h1 className="text-xl font-bold flex items-center gap-2">
            <Sparkles className="h-5 w-5 text-amber-300" />
            Compatibility Report
          </h1>
          <div className="w-20"></div>
        </div>
      </div>

      <div className="max-w-4xl mx-auto px-4 -mt-6">
        {/* Profile Card Summary */}
        <div className="bg-card rounded-2xl shadow-xl border border-border p-6 md:p-8 mb-6">
          <div className="flex flex-col md:flex-row items-center justify-between gap-6">
            <div className="flex items-center gap-5">
              <div className="relative h-24 w-24 rounded-2xl overflow-hidden shadow-md border-2 border-primary/20 shrink-0">
                <img
                  src={resolveImageUrl(match.profilePhoto, profile1)}
                  alt={match.fullName || "Partner"}
                  className="w-full h-full object-cover"
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = profile1;
                  }}
                />
              </div>
              <div>
                <h2 className="text-2xl font-bold text-foreground">{match.fullName || "Partner Profile"}</h2>
                <p className="text-muted-foreground text-sm">Partner Compatibility Details</p>
                <div className="mt-3 flex gap-2">
                  <Link
                    to={`/profile/${match.userId || partnerId}`}
                    className="inline-flex items-center gap-1.5 text-xs font-semibold text-primary bg-primary/10 hover:bg-primary/20 px-3 py-1.5 rounded-lg transition"
                  >
                    <User size={14} />
                    View Full Profile
                  </Link>
                </div>
              </div>
            </div>

            {/* Score Ring */}
            <div className="flex flex-col items-center justify-center p-4 bg-gradient-to-br from-pink-500/10 to-rose-500/10 rounded-2xl border border-pink-500/20 min-w-[140px]">
              <div className="text-4xl font-extrabold text-pink-600 font-display">
                {match.matchPercentage ?? 0}%
              </div>
              <div className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mt-1">
                Overall Match
              </div>
            </div>
          </div>

          {/* Quick Stats Grid */}
          <div className="grid grid-cols-2 gap-4 mt-6 pt-6 border-t border-border">
            <div className="bg-emerald-50 dark:bg-emerald-950/30 rounded-xl p-4 text-center border border-emerald-200 dark:border-emerald-800/40">
              <div className="text-2xl font-bold text-emerald-600 dark:text-emerald-400">
                {match.matchedFields ?? 0}
              </div>
              <div className="text-xs font-medium text-muted-foreground mt-0.5">Matched Criteria</div>
            </div>

            <div className="bg-indigo-50 dark:bg-indigo-950/30 rounded-xl p-4 text-center border border-indigo-200 dark:border-indigo-800/40">
              <div className="text-2xl font-bold text-indigo-600 dark:text-indigo-400">
                {match.totalFields ?? 0}
              </div>
              <div className="text-xs font-medium text-muted-foreground mt-0.5">Total Compared Criteria</div>
            </div>
          </div>
        </div>

        {/* Detailed Criteria Breakdown */}
        <div className="bg-card rounded-2xl shadow-lg border border-border overflow-hidden">
          <div className="border-b border-border p-5 bg-muted/40">
            <h3 className="text-lg font-bold text-foreground flex items-center gap-2">
              <CheckCircle2 className="h-5 w-5 text-emerald-500" />
              Preference Criteria Breakdown
            </h3>
            <p className="text-xs text-muted-foreground mt-0.5">
              Comparison between your saved partner preferences and their profile details
            </p>
          </div>

          <div className="divide-y divide-border">
            {match.fieldMatches && match.fieldMatches.length > 0 ? (
              match.fieldMatches.map((field, index) => (
                <div
                  key={index}
                  className="flex flex-col sm:flex-row sm:items-center justify-between p-4 md:p-5 hover:bg-muted/20 transition gap-3"
                >
                  <div className="space-y-1">
                    <h4 className="font-semibold text-foreground text-sm">{field.fieldName}</h4>
                    <div className="flex flex-wrap gap-x-4 gap-y-1 text-xs text-muted-foreground">
                      <span>
                        Your Preference: <strong className="text-foreground">{field.myValue || "Any"}</strong>
                      </span>
                      <span>•</span>
                      <span>
                        Their Detail: <strong className="text-foreground">{field.partnerValue || "Not specified"}</strong>
                      </span>
                    </div>
                  </div>

                  <div className="shrink-0">
                    {field.matched ? (
                      <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-emerald-100 dark:bg-emerald-900/40 text-emerald-700 dark:text-emerald-300 border border-emerald-200 dark:border-emerald-800">
                        <CheckCircle2 className="h-3.5 w-3.5" />
                        Matched
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-rose-100 dark:bg-rose-900/40 text-rose-700 dark:text-rose-300 border border-rose-200 dark:border-rose-800">
                        <XCircle className="h-3.5 w-3.5" />
                        Not Matched
                      </span>
                    )}
                  </div>
                </div>
              ))
            ) : (
              <div className="p-8 text-center text-muted-foreground text-sm">
                No specific preference criteria configured.
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default MatchDetails;
