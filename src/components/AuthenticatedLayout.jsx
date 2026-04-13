import { useAuth } from "@/hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import Footer from "./Footer";
import { useLanguage } from "@/context/LanguageContext";

const AuthenticatedLayout = ({ children }) => {
  const { t } = useLanguage();
  const { isLoggedIn } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoggedIn) {
      navigate("/login", { replace: true });
    }
  }, [isLoggedIn, navigate]);

  if (!isLoggedIn) {
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
