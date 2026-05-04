import { useAuth } from "@/hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import Footer from "./Footer";
import { useLanguage } from "@/context/LanguageContext";

const AuthenticatedLayout = ({ children }) => {
  const { t } = useLanguage();
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    console.log("AuthenticatedLayout: Checking authentication");
    console.log("AuthenticatedLayout: isAuthenticated():", isAuthenticated());
    if (!isAuthenticated()) {
      console.log("AuthenticatedLayout: Not authenticated, redirecting to login");
      navigate("/login", { replace: true });
    }
  }, [isAuthenticated, navigate]);

  if (!isAuthenticated()) {
    console.log("AuthenticatedLayout: Not authenticated, showing redirect message");
    return (
      <div className="min-h-screen flex items-center justify-center bg-muted/30">
        <p className="text-muted-foreground">{t.auth.redirecting}</p>
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
