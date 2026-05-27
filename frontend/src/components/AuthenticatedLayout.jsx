import { useAuth } from "@/hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import Footer from "./Footer";
import { useLanguage } from "@/context/LanguageContext";

const AuthenticatedLayout = ({ children }) => {
  const { t } = useLanguage();
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [isChecking, setIsChecking] = useState(true);

  useEffect(() => {
    console.log("AuthenticatedLayout: Checking authentication");
    console.log("AuthenticatedLayout: isAuthenticated():", isAuthenticated());
    console.log("AuthenticatedLayout: token:", localStorage.getItem('token'));
    
    // Add a small delay to ensure auth state is updated
    const timer = setTimeout(() => {
      setIsChecking(false);
      if (!isAuthenticated()) {
        console.log("AuthenticatedLayout: Not authenticated, redirecting to login");
        navigate("/login", { replace: true });
      }
    }, 100);
    
    return () => clearTimeout(timer);
  }, [isAuthenticated, navigate]);

  // Show loading while checking authentication
  if (isChecking) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-muted/30">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-muted-foreground">Loading...</p>
        </div>
      </div>
    );
  }

  if (!isAuthenticated()) {
    console.log("AuthenticatedLayout: Not authenticated, showing redirect message");
    return (
      <div className="min-h-screen flex items-center justify-center bg-muted/30">
        <p className="text-muted-foreground">{t.auth?.redirecting || "Redirecting..."}</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-muted/30 flex flex-col">
      {/* Main content area */}
      <div className="flex-1">
        {children}
      </div>
      
      {/* Footer - only for authenticated users */}
      <Footer />
    </div>
  );
};

export default AuthenticatedLayout;
