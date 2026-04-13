import {
  User,
  Mail,
  Phone,
  Shield,
  Bell,
  Crown,
  BadgeCheck,
  Lock,
  Eye,
  EyeOff,
  Settings,
  LogOut,
  Trash2,
  Heart,
  Ban,
  Clock,
} from "lucide-react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import { useEffect } from "react";
import Navbar from "@/components/Navbar";
import { useLanguage } from "@/context/LanguageContext";
import { useAuth } from "@/hooks/useAuth";
import { useProfileData } from "@/hooks/useProfileData";
import profile1 from "@/assets/profile1.jpg";

const Account = () => {
  const { t } = useLanguage();
  const navigate = useNavigate();
  const { userName, logout } = useAuth();
  const { profileData, loading } = useProfileData();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-muted/30">
        <p className="text-muted-foreground">{t.account.loading}</p>
      </div>
    );
  }

  const userDetails = {
    name:
      profileData?.fullName ||
      (userName
        ? `${userName.charAt(0).toUpperCase()}${userName.slice(1)}`
        : "User"),
    email: profileData?.email || `${userName || "user"}@email.com`,
    phone: profileData?.phone || "+91 98765 43210",
    memberId: profileData?.memberId || "GB10234",
    profilePhoto: profileData?.profilePhoto || profile1,
    accountType: profileData?.premium ? "Premium Member" : "Free Member",
    phoneVerified: true,
    emailVerified: true,
    profileVerified: false,
  };

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const accountCards = [
    {
      title: "Membership Plan",
      icon: <Crown className="h-5 w-5 text-primary" />,
      content: (
        <div className="space-y-4">
          <div className="flex items-center justify-between flex-wrap gap-3">
            <div>
              <p className="text-sm text-muted-foreground">Current Plan</p>
              <p className="text-lg font-semibold text-foreground">
                {userDetails.accountType}
              </p>
            </div>
            <button
              onClick={() => navigate("/upgrade")}
              className="px-4 py-2 rounded-lg bg-primary text-primary-foreground font-medium hover:bg-primary/90 transition-colors"
            >
              Upgrade Plan
            </button>
          </div>

          <div className="grid sm:grid-cols-3 gap-3">
            <div className="rounded-xl border border-border p-4 bg-muted/30">
              <p className="font-medium">Unlimited Interests</p>
              <p className="text-sm text-muted-foreground">
                Connect with more profiles
              </p>
            </div>
            <div className="rounded-xl border border-border p-4 bg-muted/30">
              <p className="font-medium">Priority Visibility</p>
              <p className="text-sm text-muted-foreground">
                Get better profile reach
              </p>
            </div>
            <div className="rounded-xl border border-border p-4 bg-muted/30">
              <p className="font-medium">Direct Contact Access</p>
              <p className="text-sm text-muted-foreground">
                View contact details faster
              </p>
            </div>
          </div>
        </div>
      ),
    },
    {
      title: "Privacy Settings",
      icon: <Eye className="h-5 w-5 text-primary" />,
      content: (
        <div className="grid sm:grid-cols-2 gap-4">
          <div className="rounded-xl border border-border p-4">
            <div className="flex items-center gap-3 mb-2">
              <Phone className="h-4 w-4 text-primary" />
              <p className="font-medium">Phone Visibility</p>
            </div>
            <p className="text-sm text-muted-foreground">Hidden from public profiles</p>
          </div>

          <div className="rounded-xl border border-border p-4">
            <div className="flex items-center gap-3 mb-2">
              <Mail className="h-4 w-4 text-primary" />
              <p className="font-medium">Email Visibility</p>
            </div>
            <p className="text-sm text-muted-foreground">Visible only after connection</p>
          </div>

          <div className="rounded-xl border border-border p-4">
            <div className="flex items-center gap-3 mb-2">
              <EyeOff className="h-4 w-4 text-primary" />
              <p className="font-medium">Profile Visibility</p>
            </div>
            <p className="text-sm text-muted-foreground">Shown to matching profiles only</p>
          </div>

          <div className="rounded-xl border border-border p-4">
            <div className="flex items-center gap-3 mb-2">
              <User className="h-4 w-4 text-primary" />
              <p className="font-medium">Photo Privacy</p>
            </div>
            <p className="text-sm text-muted-foreground">Protected for selected members</p>
          </div>
        </div>
      ),
    },
    {
      title: "Security Settings",
      icon: <Shield className="h-5 w-5 text-primary" />,
      content: (
        <div className="grid sm:grid-cols-2 gap-4">
          <button className="rounded-xl border border-border p-4 text-left hover:bg-muted/40 transition-colors">
            <div className="flex items-center gap-3 mb-2">
              <Lock className="h-4 w-4 text-primary" />
              <p className="font-medium">Change Password</p>
            </div>
            <p className="text-sm text-muted-foreground">
              Update your password for better account security
            </p>
          </button>

          <button className="rounded-xl border border-border p-4 text-left hover:bg-muted/40 transition-colors">
            <div className="flex items-center gap-3 mb-2">
              <Mail className="h-4 w-4 text-primary" />
              <p className="font-medium">Change Email</p>
            </div>
            <p className="text-sm text-muted-foreground">
              Update your login email address
            </p>
          </button>

          <button className="rounded-xl border border-border p-4 text-left hover:bg-muted/40 transition-colors">
            <div className="flex items-center gap-3 mb-2">
              <Phone className="h-4 w-4 text-primary" />
              <p className="font-medium">Change Mobile Number</p>
            </div>
            <p className="text-sm text-muted-foreground">
              Update your registered phone number
            </p>
          </button>

          <button className="rounded-xl border border-border p-4 text-left hover:bg-muted/40 transition-colors">
            <div className="flex items-center gap-3 mb-2">
              <Clock className="h-4 w-4 text-primary" />
              <p className="font-medium">Login Activity</p>
            </div>
            <p className="text-sm text-muted-foreground">
              Check recent login and device activity
            </p>
          </button>
        </div>
      ),
    },
    {
      title: "Verification Status",
      icon: <BadgeCheck className="h-5 w-5 text-primary" />,
      content: (
        <div className="grid sm:grid-cols-3 gap-4">
          <div className="rounded-xl border border-border p-4">
            <p className="font-medium mb-2">Phone Verification</p>
            <span className="inline-flex px-3 py-1 rounded-full text-xs font-semibold bg-green-100 text-green-700">
              {userDetails.phoneVerified ? "Verified" : "Pending"}
            </span>
          </div>

          <div className="rounded-xl border border-border p-4">
            <p className="font-medium mb-2">Email Verification</p>
            <span className="inline-flex px-3 py-1 rounded-full text-xs font-semibold bg-green-100 text-green-700">
              {userDetails.emailVerified ? "Verified" : "Pending"}
            </span>
          </div>

          <div className="rounded-xl border border-border p-4">
            <p className="font-medium mb-2">Profile Verification</p>
            <span
              className={`inline-flex px-3 py-1 rounded-full text-xs font-semibold ${
                userDetails.profileVerified
                  ? "bg-green-100 text-green-700"
                  : "bg-yellow-100 text-yellow-700"
              }`}
            >
              {userDetails.profileVerified ? "Verified" : "Pending Review"}
            </span>
          </div>
        </div>
      ),
    },
    {
      title: "Notification Preferences",
      icon: <Bell className="h-5 w-5 text-primary" />,
      content: (
        <div className="grid sm:grid-cols-2 gap-4">
          <div className="rounded-xl border border-border p-4">
            <p className="font-medium">Match Alerts</p>
            <p className="text-sm text-muted-foreground">Receive new match notifications</p>
          </div>

          <div className="rounded-xl border border-border p-4">
            <p className="font-medium">Message Alerts</p>
            <p className="text-sm text-muted-foreground">Get notified for new messages</p>
          </div>

          <div className="rounded-xl border border-border p-4">
            <p className="font-medium">Interest Alerts</p>
            <p className="text-sm text-muted-foreground">Know when someone sends interest</p>
          </div>

          <div className="rounded-xl border border-border p-4">
            <p className="font-medium">Email / SMS Alerts</p>
            <p className="text-sm text-muted-foreground">Control communication preferences</p>
          </div>
        </div>
      ),
    },
    {
      title: "Manage Account",
      icon: <Settings className="h-5 w-5 text-primary" />,
      content: (
        <div className="grid sm:grid-cols-2 gap-4">
          <button className="rounded-xl border border-border p-4 text-left hover:bg-muted/40 transition-colors">
            <div className="flex items-center gap-3 mb-2">
              <Heart className="h-4 w-4 text-primary" />
              <p className="font-medium">Saved Profiles</p>
            </div>
            <p className="text-sm text-muted-foreground">View profiles you saved</p>
          </button>

          <button className="rounded-xl border border-border p-4 text-left hover:bg-muted/40 transition-colors">
            <div className="flex items-center gap-3 mb-2">
              <Clock className="h-4 w-4 text-primary" />
              <p className="font-medium">Recently Viewed</p>
            </div>
            <p className="text-sm text-muted-foreground">See recently visited profiles</p>
          </button>

          <button className="rounded-xl border border-border p-4 text-left hover:bg-muted/40 transition-colors">
            <div className="flex items-center gap-3 mb-2">
              <Ban className="h-4 w-4 text-primary" />
              <p className="font-medium">Blocked Profiles</p>
            </div>
            <p className="text-sm text-muted-foreground">Manage blocked members</p>
          </button>

          <button className="rounded-xl border border-border p-4 text-left hover:bg-red-50 transition-colors">
            <div className="flex items-center gap-3 mb-2">
              <Trash2 className="h-4 w-4 text-destructive" />
              <p className="font-medium text-destructive">Deactivate Account</p>
            </div>
            <p className="text-sm text-muted-foreground">Temporarily disable your account</p>
          </button>
        </div>
      ),
    },
  ];

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="py-8 text-center"
        style={{
          background:
            "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))",
        }}
      >
        <h1 className="text-3xl md:text-4xl font-bold text-primary-foreground mb-2">
          Account Settings
        </h1>
        <p className="text-primary-foreground/70 text-sm">
          Manage your account, privacy, and subscription
        </p>
      </motion.div>

      <div className="container mx-auto px-4 py-8 max-w-6xl pb-24 md:pb-8">
        <div className="grid gap-6">
          {/* Account Header Card */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="bg-card rounded-2xl border border-border p-6 md:p-8"
          >
            <div className="flex flex-col md:flex-row items-start md:items-center gap-6">
              <img
                src={userDetails.profilePhoto}
                alt="Profile"
                className="h-24 w-24 rounded-full object-cover border-4 border-background shadow-sm"
              />

              <div className="flex-1">
                <h2 className="text-2xl font-bold text-foreground mb-1">
                  {userDetails.name}
                </h2>
                <div className="grid sm:grid-cols-2 gap-2 text-sm text-muted-foreground">
                  <p className="flex items-center gap-2">
                    <Mail className="h-4 w-4 text-primary" />
                    {userDetails.email}
                  </p>
                  <p className="flex items-center gap-2">
                    <Phone className="h-4 w-4 text-primary" />
                    {userDetails.phone}
                  </p>
                  <p className="flex items-center gap-2">
                    <User className="h-4 w-4 text-primary" />
                    Member ID: {userDetails.memberId}
                  </p>
                  <p className="flex items-center gap-2">
                    <Shield className="h-4 w-4 text-primary" />
                    Account Active
                  </p>
                </div>

                <div className="flex flex-wrap items-center gap-2 mt-4">
                  <span className="inline-flex px-3 py-1 rounded-full text-xs font-semibold bg-gold/20 text-gold">
                    {userDetails.accountType}
                  </span>

                  {userDetails.emailVerified && (
                    <span className="inline-flex px-3 py-1 rounded-full text-xs font-semibold bg-green-100 text-green-700">
                      Email Verified
                    </span>
                  )}

                  {userDetails.phoneVerified && (
                    <span className="inline-flex px-3 py-1 rounded-full text-xs font-semibold bg-green-100 text-green-700">
                      Phone Verified
                    </span>
                  )}
                </div>
              </div>

              <button
                onClick={() => navigate("/settings")}
                className="px-5 py-2.5 bg-primary hover:bg-primary/90 text-primary-foreground rounded-lg font-medium transition-colors"
              >
                Edit Account
              </button>
            </div>
          </motion.div>

          {/* Cards */}
          {accountCards.map((card, index) => (
            <motion.div
              key={card.title}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.1 * (index + 1) }}
              className="bg-card rounded-2xl border border-border p-6 md:p-8"
            >
              <div className="flex items-center gap-3 mb-6">
                {card.icon}
                <h3 className="text-lg font-bold text-foreground">{card.title}</h3>
              </div>
              {card.content}
            </motion.div>
          ))}

          {/* Logout */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.8 }}
            className="bg-card rounded-2xl border border-border p-6 md:p-8"
          >
            <div className="flex flex-col sm:flex-row gap-3 justify-between items-start sm:items-center">
              <div>
                <h3 className="text-lg font-bold text-foreground">Session</h3>
                <p className="text-sm text-muted-foreground">
                  Logout securely from your account
                </p>
              </div>

              <button
                onClick={handleLogout}
                className="flex items-center justify-center gap-2 px-6 py-2.5 bg-destructive/10 hover:bg-destructive/20 text-destructive font-semibold rounded-lg text-sm transition-colors"
              >
                <LogOut className="h-4 w-4" />
                Logout
              </button>
            </div>
          </motion.div>
        </div>
      </div>
    </div>
  );
};

export default Account;