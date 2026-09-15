import { Heart, User, Search, Settings, LogOut, MessageSquare } from "lucide-react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";
import { useProfileData } from "@/hooks/useProfileData";
import { useState } from "react";
import { subscriptionAPI } from "@/services/api";
import PremiumUpgradeModal from "@/components/PremiumUpgradeModal";

const DashboardSidebar = ({ isSidebarOpen = true }) => {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const { profileData } = useProfileData();
  const [showProfilePopup, setShowProfilePopup] = useState(false);
  const [showUpgradePopup, setShowUpgradePopup] = useState(false);
  const [premiumFeature, setPremiumFeature] = useState("");

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const navItems = [
    { icon: <User className="h-4 w-4" />, label: "Dashboard", to: "/home" },
    { icon: <Heart className="h-4 w-4" />, label: "Matches", to: "/matches" },
    { icon: <Search className="h-4 w-4" />, label: "Search", to: "/search" },
    { icon: <MessageSquare className="h-4 w-4" />, label: "Messages", to: "/messages" },
    { icon: <Settings className="h-4 w-4" />, label: "Settings", to: "/settings" },
  ];

  const handleNavClick = async (e, item) => {
    const isCompleted = Boolean(
      profileData?.profileCompleted || (profileData?.profileCompletionPercentage >= 80)
    );

    if (!isCompleted && item.label !== "Dashboard" && item.label !== "Settings") {
      e.preventDefault();
      setShowProfilePopup(true);
      return;
    }

    if (item.label === "Messages" || item.label === "Matches") {
      e.preventDefault();
      try {
        const subscription = await subscriptionAPI.getMySubscription();
        if (subscription?.isActive) {
          navigate(item.to);
        } else {
          setPremiumFeature(item.label);
          setShowUpgradePopup(true);
        }
      } catch (error) {
        setPremiumFeature(item.label);
        setShowUpgradePopup(true);
      }
      return;
    }
  };

  return (
    <>
      <aside
        className={`hidden md:flex flex-col bg-card border-r border-border h-[calc(100vh-4rem)] sticky top-16 shrink-0 transition-all duration-300 ${isSidebarOpen ? "w-64" : "w-20"}`}
      >
        <nav className="flex-1 p-4 space-y-1 overflow-y-auto">
          {navItems.map((item) => {
            const isActive = location.pathname.startsWith(item.to);
            return (
              <Link
                key={item.label}
                to={item.to}
                onClick={(e) => handleNavClick(e, item)}
                className={`w-full flex items-center px-4 py-2.5 rounded-lg text-sm font-medium transition-colors ${
                  isSidebarOpen ? 'gap-3' : 'justify-center'
                } ${
                  isActive
                    ? "bg-primary text-primary-foreground"
                    : "text-muted-foreground hover:bg-muted hover:text-foreground"
                }`}
              >
                {item.icon}
                {isSidebarOpen && item.label}
              </Link>
            );
          })}
        </nav>
        <div className="p-4 border-t border-border mt-auto">
          <button onClick={handleLogout} className={`w-full flex items-center px-4 py-2.5 rounded-lg text-sm font-medium text-muted-foreground hover:bg-muted hover:text-foreground transition-colors ${
            isSidebarOpen ? 'gap-3' : 'justify-center'
          }`}>
            <LogOut className="h-4 w-4" />
            {isSidebarOpen && 'Logout'}
          </button>
        </div>
      </aside>

      {showProfilePopup && (
        <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-[9999]">
          <div className="bg-white rounded-2xl p-8 max-w-md w-full mx-4 shadow-2xl text-center">
            <div className="text-6xl mb-4">⚠️</div>
            <h2 className="text-2xl font-bold mb-3">Complete Your Profile</h2>
            <p className="text-gray-600 mb-6">
              Please complete your profile to continue. You are not allowed to access this feature until your profile is completed.
            </p>
            <div className="flex gap-3 justify-center">
              <button className="bg-pink-600 text-white px-6 py-3 rounded-xl" onClick={() => { setShowProfilePopup(false); navigate("/settings"); }}>
                Complete Profile
              </button>
              <button className="border px-6 py-3 rounded-xl" onClick={() => setShowProfilePopup(false)}>
                Later
              </button>
            </div>
          </div>
        </div>
      )}

      <PremiumUpgradeModal
        open={showUpgradePopup}
        onClose={() => setShowUpgradePopup(false)}
        feature={premiumFeature}
      />
    </>
  );
};

export default DashboardSidebar;
