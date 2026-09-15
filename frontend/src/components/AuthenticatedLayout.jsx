import { useAuth } from "@/hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import Footer from "./Footer";
import { useLanguage } from "@/context/LanguageContext";
import Navbar from "./Navbar";
import DashboardSidebar from "./DashboardSidebar";
import ErrorBoundary from "@/components/common/ErrorBoundary";

const AuthenticatedLayout = ({ children }) => {
  const { t } = useLanguage();
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [isChecking, setIsChecking] = useState(true);
  const [profileChecked, setProfileChecked] = useState(false);

  // Authentication Check
  useEffect(() => {
    console.log("AuthenticatedLayout: Checking authentication");
    const token = sessionStorage.getItem("token") || localStorage.getItem("token");

    const timer = setTimeout(() => {
      setIsChecking(false);

      if (!isAuthenticated()) {
        navigate("/login", { replace: true });
      }
    }, 100);

    return () => clearTimeout(timer);
  }, [isAuthenticated, navigate]);
console.log("CHECK PROFILE STARTED");
  // Profile Completion Check
  useEffect(() => {
      setProfileChecked(true);
  }, []);

  // Loading Screen
  if (
    isChecking ||
    !profileChecked
  ) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-muted/30">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>

          <p className="text-muted-foreground">
            Loading...
          </p>
        </div>
      </div>
    );
  }

  // Not Authenticated
  if (!isAuthenticated()) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-muted/30">
        <p className="text-muted-foreground">
          {t.auth?.redirecting ||
            "Redirecting..."}
        </p>
      </div>
    );
  }

return (
  <div className="h-screen bg-muted/30 flex flex-col overflow-hidden">
    <Navbar />
    <div className="flex-1 flex overflow-hidden">
      <DashboardSidebar />
      <main className="flex-1 min-w-0 overflow-y-auto overflow-x-hidden">
        <ErrorBoundary>
          {children}
        </ErrorBoundary>
        <Footer />
      </main>
    </div>
  </div>
);
};

export default AuthenticatedLayout;