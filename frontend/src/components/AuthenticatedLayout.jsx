import { useAuth } from "@/hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import Footer from "./Footer";
import { useLanguage } from "@/context/LanguageContext";
import { profileAPI } from "@/services/api";
import { useLocation } from "react-router-dom";
import Navbar from "./Navbar";
const AuthenticatedLayout = ({ children }) => {
  const { t } = useLanguage();
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
const location = useLocation();

  const [isChecking, setIsChecking] = useState(true);
  const [profileChecked, setProfileChecked] = useState(false);

  // Authentication Check
  useEffect(() => {
    console.log("AuthenticatedLayout: Checking authentication");
    console.log("AuthenticatedLayout: isAuthenticated():", isAuthenticated());
    console.log(
      "AuthenticatedLayout: token:",
      localStorage.getItem("token")
    );

    const timer = setTimeout(() => {
      setIsChecking(false);

      if (!isAuthenticated()) {
        console.log(
          "AuthenticatedLayout: Not authenticated, redirecting to login"
        );

        navigate("/login", { replace: true });
      }
    }, 100);

    return () => clearTimeout(timer);
  }, [isAuthenticated, navigate]);
console.log("CHECK PROFILE STARTED");
  // Profile Completion Check
  useEffect(() => {
    const checkProfile = async () => {
      if (!isAuthenticated()) {
        setProfileChecked(true);
        return;
      }

      try {
        const profile =
          await profileAPI.getProfile();
console.log(
  "CURRENT PROFILE =",
  profile
);
      console.log(
        "PROFILE COMPLETED =",
        profile.profileCompleted
      );
  if (
    profile &&
    profile.profileCompleted === false
  ) {

    const allowedRoutes = [
      "/home",
      "/settings"
    ];

    if (
      !allowedRoutes.includes(
        location.pathname
      )
    ) {

      navigate(
        "/home",
        { replace: true }
      );

      return;
    }

  }
//         if (
//           profile &&
//           profile.profileCompleted === false
//         ) {
//           navigate("/profile/create", {
//             replace: true,
//           });
//
//           return;
//         }

        setProfileChecked(true);
      } catch (err) {
        console.error(
          "PROFILE CHECK ERROR",
          err
        );

        setProfileChecked(true);
      }
    };

    checkProfile();
}, [
  isAuthenticated,
  navigate,
  location.pathname
]);

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
  <div className="min-h-screen bg-muted/30 flex flex-col">

    <Navbar />

    <div className="flex-1">
      {children}
    </div>

    <Footer />

  </div>
);
};

export default AuthenticatedLayout;