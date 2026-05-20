import { useAuth } from "@/hooks/useAuth";
import Footer from "./Footer";
import { useLanguage } from "@/context/LanguageContext";

const AuthenticatedLayout = ({ children }) => {
  const { t } = useLanguage();
  const { token, loading } = useAuth();

  console.log('🔐 AuthenticatedLayout Render');
  console.log('Token:', token);
  console.log('Loading:', loading);

  // Wait until auth is fully loaded
  if (loading) {
    console.log('⏳ Waiting for authentication...');
    return (
      <div className="min-h-screen flex items-center justify-center bg-muted/30">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-muted-foreground">Loading...</p>
        </div>
      </div>
    );
  }

  // Only redirect if token is truly missing
  if (!token) {
    console.log('❌ Token missing, redirecting to login');
    return (
      <div className="min-h-screen flex items-center justify-center bg-muted/30">
        <p className="text-muted-foreground">Redirecting to login...</p>
      </div>
    );
  }

  console.log('✅ User authenticated');

  return (
    <div className="min-h-screen bg-muted/30 flex flex-col">
      {/* Main Content */}
      <div className="flex-1">
        {children}
      </div>

      {/* Footer */}
      <Footer />
    </div>
  );
};

export default AuthenticatedLayout;