import React, { useEffect, useState } from "react";
import { subscriptionAPI } from "@/services/api";
import { useNavigate } from "react-router-dom";
import { 
  Crown, 
  Calendar, 
  Clock, 
  ShieldCheck, 
  ArrowRight, 
  RefreshCw, 
  Sparkles,
  History,
  AlertCircle
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { motion } from "framer-motion";

const SubscriptionHistory = () => {
  const [currentSubscription, setCurrentSubscription] = useState(null);
  const [subscriptionHistory, setSubscriptionHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    loadSubscriptionData();
  }, []);

  const loadSubscriptionData = async () => {
    try {
      setLoading(true);
      setError("");

      const [currentPlan, history] = await Promise.all([
        subscriptionAPI.getMySubscription(),
        subscriptionAPI.getHistory()
      ]);

      setCurrentSubscription(currentPlan || null);
      setSubscriptionHistory(Array.isArray(history) ? history : []);
    } catch (err) {
      console.error("Error loading subscription data:", err);
      setError("Failed to load subscription details. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (date) => {
    if (!date) return "-";
    return new Date(date).toLocaleDateString("en-IN", {
      day: "2-digit",
      month: "short",
      year: "numeric"
    });
  };

  const calculateDaysRemaining = (endDate) => {
    if (!endDate) return 0;
    const today = new Date();
    const expiry = new Date(endDate);
    const diff = expiry - today;
    const days = Math.ceil(diff / (1000 * 60 * 60 * 24));
    return days > 0 ? days : 0;
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "ACTIVE":
        return "bg-emerald-100 dark:bg-emerald-950/60 text-emerald-700 dark:text-emerald-400 border border-emerald-300 dark:border-emerald-800";
      case "CANCELLED":
        return "bg-amber-100 dark:bg-amber-950/60 text-amber-700 dark:text-amber-400 border border-amber-300 dark:border-amber-800";
      case "EXPIRED":
        return "bg-rose-100 dark:bg-rose-950/60 text-rose-700 dark:text-rose-400 border border-rose-300 dark:border-rose-800";
      case "REFUNDED":
        return "bg-purple-100 dark:bg-purple-950/60 text-purple-700 dark:text-purple-400 border border-purple-300 dark:border-purple-800";
      default:
        return "bg-muted text-muted-foreground border border-border";
    }
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto p-6 space-y-6">
        <div className="h-10 w-64 bg-muted rounded-xl animate-pulse"></div>
        <div className="bg-card rounded-2xl border border-border p-8 space-y-4">
          <div className="h-6 w-48 bg-muted rounded-lg animate-pulse"></div>
          <div className="grid md:grid-cols-4 gap-6 pt-4">
            {[1, 2, 3, 4].map((i) => (
              <div key={i} className="h-16 bg-muted rounded-xl animate-pulse"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  const isUserPremium = currentSubscription && currentSubscription.status === "ACTIVE" && Boolean(currentSubscription.isActive);
  const daysRemaining = isUserPremium ? calculateDaysRemaining(currentSubscription.endDate) : 0;

  return (
    <div className="min-h-screen bg-muted/30 py-10 px-4 sm:px-6 lg:px-8 font-sans">
      <div className="max-w-6xl mx-auto space-y-8">
        
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-3xl font-extrabold text-foreground tracking-tight flex items-center gap-3">
              <Crown className="h-8 w-8 text-amber-500 fill-amber-500" />
              Membership & Billing
            </h1>
            <p className="text-sm text-muted-foreground mt-1">
              Manage your premium plan, billing history, and membership privileges.
            </p>
          </div>

          <Button
            onClick={() => navigate("/upgrade")}
            className="cursor-pointer bg-rose-600 hover:bg-rose-700 text-white font-bold rounded-xl shadow-md flex items-center gap-2"
          >
            <Sparkles className="h-4 w-4" />
            <span>{isUserPremium ? "Upgrade / Extend Plan" : "Upgrade to Premium"}</span>
            <ArrowRight className="h-4 w-4" />
          </Button>
        </div>

        {error && (
          <div className="rounded-2xl border border-rose-200 bg-rose-50 dark:bg-rose-950/40 p-4 text-rose-700 dark:text-rose-300 flex items-center gap-3 text-sm">
            <AlertCircle className="h-5 w-5 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        {/* Current Active Plan Card */}
        {isUserPremium ? (
          <motion.div
            initial={{ opacity: 0, y: 15 }}
            animate={{ opacity: 1, y: 0 }}
            className="bg-card rounded-3xl border-2 border-amber-500/30 p-8 shadow-xl relative overflow-hidden"
          >
            <div className="absolute top-0 right-0 w-48 h-48 bg-amber-500/5 rounded-full blur-2xl -mr-16 -mt-16 pointer-events-none"></div>

            <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6 pb-6 border-b border-border">
              <div className="flex items-center gap-4">
                <div className="w-16 h-16 rounded-2xl bg-amber-500/10 text-amber-600 flex items-center justify-center shrink-0 shadow-inner">
                  <Crown className="h-9 w-9 fill-amber-500" />
                </div>
                <div>
                  <div className="flex items-center gap-3">
                    <h2 className="text-2xl font-bold text-foreground">{currentSubscription.planName}</h2>
                    <span className="px-3 py-0.5 rounded-full text-xs font-bold bg-emerald-100 dark:bg-emerald-950/60 text-emerald-700 dark:text-emerald-400 border border-emerald-300">
                      ACTIVE
                    </span>
                  </div>
                  <p className="text-sm text-muted-foreground mt-0.5">
                    Premium Matrimonial Privileges Enabled
                  </p>
                </div>
              </div>

              <Button
                onClick={() => navigate("/upgrade")}
                variant="outline"
                className="cursor-pointer border-amber-500/50 hover:bg-amber-50 dark:hover:bg-amber-950/40 font-semibold text-amber-700 dark:text-amber-300"
              >
                Renew / Change Plan
              </Button>
            </div>

            {/* Metrics */}
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-6 pt-6 text-left">
              <div>
                <span className="text-xs font-semibold text-muted-foreground flex items-center gap-1">
                  <Calendar className="h-3.5 w-3.5" /> Start Date
                </span>
                <p className="text-base font-bold text-foreground mt-1">
                  {formatDate(currentSubscription.startDate)}
                </p>
              </div>

              <div>
                <span className="text-xs font-semibold text-muted-foreground flex items-center gap-1">
                  <Calendar className="h-3.5 w-3.5" /> Valid Till
                </span>
                <p className="text-base font-bold text-foreground mt-1">
                  {formatDate(currentSubscription.endDate)}
                </p>
              </div>

              <div>
                <span className="text-xs font-semibold text-muted-foreground flex items-center gap-1">
                  <Clock className="h-3.5 w-3.5" /> Days Remaining
                </span>
                <p className={`text-base font-bold mt-1 ${daysRemaining <= 7 ? "text-rose-600 font-extrabold" : "text-emerald-600"}`}>
                  {daysRemaining} Day{daysRemaining !== 1 ? "s" : ""}
                </p>
              </div>

              <div>
                <span className="text-xs font-semibold text-muted-foreground flex items-center gap-1">
                  <ShieldCheck className="h-3.5 w-3.5" /> Status
                </span>
                <p className="text-base font-bold text-emerald-600 dark:text-emerald-400 mt-1">
                  Active Member
                </p>
              </div>
            </div>
          </motion.div>
        ) : (
          <div className="bg-card rounded-3xl border border-border p-10 text-center shadow-md space-y-4">
            <div className="w-16 h-16 bg-rose-100 dark:bg-rose-950/60 text-rose-600 rounded-2xl flex items-center justify-center mx-auto shadow-inner">
              <Crown className="h-8 w-8" />
            </div>
            <h2 className="text-2xl font-bold text-foreground">No Active Premium Plan</h2>
            <p className="text-sm text-muted-foreground max-w-md mx-auto">
              Upgrade to Premium to contact verified profiles directly, send unlimited messages, and boost your matchmaking success.
            </p>
            <div className="pt-2">
              <Button
                onClick={() => navigate("/upgrade")}
                className="bg-rose-600 hover:bg-rose-700 text-white font-bold px-8 py-6 rounded-2xl cursor-pointer shadow-md"
              >
                <Sparkles className="h-4 w-4 mr-2" />
                View Premium Plans
              </Button>
            </div>
          </div>
        )}

        {/* History Table */}
        <div className="bg-card rounded-3xl border border-border shadow-md overflow-hidden">
          <div className="px-6 py-5 border-b border-border flex items-center justify-between">
            <h2 className="text-lg font-bold text-foreground flex items-center gap-2">
              <History className="h-5 w-5 text-muted-foreground" />
              Subscription & Billing History
            </h2>
            <span className="text-xs text-muted-foreground">
              {subscriptionHistory.length} Record{subscriptionHistory.length !== 1 ? "s" : ""}
            </span>
          </div>

          {subscriptionHistory.length === 0 ? (
            <div className="p-12 text-center text-muted-foreground text-sm">
              No historical subscription records found.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-left text-sm">
                <thead className="bg-muted/50 text-xs font-bold uppercase tracking-wider text-muted-foreground border-b border-border">
                  <tr>
                    <th className="px-6 py-3.5">Plan Name</th>
                    <th className="px-6 py-3.5">Start Date</th>
                    <th className="px-6 py-3.5">End Date</th>
                    <th className="px-6 py-3.5">Status</th>
                    <th className="px-6 py-3.5">Active</th>
                    <th className="px-6 py-3.5 text-right">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-border">
                  {subscriptionHistory.map((sub) => {
                    const isSubActive = Boolean(sub.isActive) && sub.status === "ACTIVE";
                    return (
                      <tr key={sub.id} className="hover:bg-muted/30 transition-colors">
                        <td className="px-6 py-4 font-bold text-foreground">
                          {sub.planName || "Premium Plan"}
                        </td>
                        <td className="px-6 py-4 text-muted-foreground">
                          {formatDate(sub.startDate)}
                        </td>
                        <td className="px-6 py-4 text-muted-foreground">
                          {formatDate(sub.endDate)}
                        </td>
                        <td className="px-6 py-4">
                          <span className={`px-2.5 py-1 rounded-full text-xs font-bold ${getStatusBadge(sub.status)}`}>
                            {sub.status}
                          </span>
                        </td>
                        <td className="px-6 py-4 font-semibold">
                          {isSubActive ? (
                            <span className="text-emerald-600">Yes</span>
                          ) : (
                            <span className="text-muted-foreground">No</span>
                          )}
                        </td>
                        <td className="px-6 py-4 text-right">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate("/upgrade")}
                            className="cursor-pointer text-xs text-rose-600 hover:text-rose-700 hover:bg-rose-50 dark:hover:bg-rose-950/40"
                          >
                            Renew
                          </Button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>

      </div>
    </div>
  );
};

export default SubscriptionHistory;