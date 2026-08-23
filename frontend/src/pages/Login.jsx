import { Heart, Key, Smartphone, Mail, Sparkles } from "lucide-react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useState, useEffect } from "react";
import { useAuth } from "@/hooks/useAuth";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import { useLanguage } from "@/context/LanguageContext.jsx";
import { authAPI, otpAPI } from "@/services/api";
import { useGoogleReCaptcha } from "react-google-recaptcha-v3";
import { trackEvent } from "@/utils/analytics";
import OTPModal from "@/components/auth/OTPModal";
import { validateSafeRedirect } from "@/utils/urlSecurity";

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const rawRedirect = new URLSearchParams(location.search).get("redirect");
  const redirectTo = validateSafeRedirect(rawRedirect, null);
  const { login } = useAuth();
  const { success, error } = useToast();

  useEffect(() => {
    const message = localStorage.getItem("sessionExpiredMessage");
    if (message) {
      error(message);
      localStorage.removeItem("sessionExpiredMessage");
    }
  }, [error]);

  const { startLoading, stopLoading } = useLoading();
  const { t } = useLanguage();
  const { executeRecaptcha } = useGoogleReCaptcha();

  const [loginMethod, setLoginMethod] = useState("PASSWORD"); // "PASSWORD" or "OTP"
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [otpTarget, setOtpTarget] = useState("");
  const [showOtpModal, setShowOtpModal] = useState(false);
  const [devOtp, setDevOtp] = useState(null);
  const [otpLoading, setOtpLoading] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    setEmail("");
    setPassword("");
    setOtpTarget("");
  }, []);

  const handlePasswordLogin = async (e) => {
    e.preventDefault();
    const errs = {};

    if (!email.trim()) {
      errs.email = t?.login?.errors?.emailRequired || "Email is required";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      errs.email = t?.login?.errors?.emailInvalid || "Enter a valid email";
    }

    if (!password.trim()) {
      errs.password = t?.login?.errors?.passwordRequired || "Password is required";
    } else if (password.length < 6) {
      errs.password = t?.login?.errors?.passwordMin || "Password must be at least 6 characters";
    }

    setErrors(errs);
    if (Object.keys(errs).length > 0) {
      error(t?.login?.messages?.fixErrorsFirst || "Please fix the errors first");
      return;
    }

    startLoading(t?.login?.messages?.loggingIn || "Logging in...");

    try {
      if (!executeRecaptcha) {
        error("reCAPTCHA is not ready.");
        stopLoading();
        return;
      }

      const recaptchaToken = await executeRecaptcha("login");
      const response = await authAPI.login(
        {
          email: email.trim(),
          password,
          recaptchaToken,
        },
        false
      );

      if (!response.success) {
        throw new Error(response.message || "Login failed");
      }

      const userData = response.data;
      const token = response.token;
      const userRole = response.role;

      login(userData, token, userRole);
      trackEvent("user_login", { login_type: "user" });

      if (rememberMe) {
        localStorage.setItem("rememberedEmail", email.trim());
      } else {
        localStorage.removeItem("rememberedEmail");
      }

      success(t?.login?.messages?.loginSuccess || "Login successful");
      stopLoading();

      if (redirectTo) {
        navigate(redirectTo);
      } else if (userData?.needsProfile) {
        navigate("/profile/create");
      } else {
        navigate("/home");
      }
    } catch (err) {
      stopLoading();
      const errorMessage = err.message || t?.login?.messages?.loginFailed || "Login failed";
      error(errorMessage);
    }
  };

  const handleSendLoginOTP = async (e) => {
    e.preventDefault();
    if (!otpTarget.trim()) {
      error("Please enter your Phone number or Email address");
      return;
    }

    setOtpLoading(true);
    startLoading("Sending OTP code...");

    try {
      const response = await otpAPI.sendLoginOTP(otpTarget.trim());
      stopLoading();
      setOtpLoading(false);

      if (response?.data?.devOtp) {
        setDevOtp(response.data.devOtp);
      } else {
        setDevOtp(null);
      }

      setShowOtpModal(true);
      success("OTP code sent successfully!");
    } catch (err) {
      stopLoading();
      setOtpLoading(false);
      error(err.message || "Failed to send OTP code. Please check your phone/email.");
    }
  };

  const handleVerifyLoginOTP = async (otpCode) => {
    setOtpLoading(true);
    try {
      const response = await otpAPI.loginWithOTP(otpTarget.trim(), otpCode);
      setShowOtpModal(false);
      setOtpLoading(false);

      const userData = response.data;
      const token = response.token;
      const userRole = response.role;

      login(userData, token, userRole);
      trackEvent("user_login", { login_type: "otp" });

      success("OTP Login Successful!");

      if (redirectTo) {
        navigate(redirectTo);
      } else if (userData?.needsProfile) {
        navigate("/profile/create");
      } else {
        navigate("/home");
      }
    } catch (err) {
      setOtpLoading(false);
      throw err;
    }
  };

  const handleResendLoginOTP = async () => {
    const response = await otpAPI.sendLoginOTP(otpTarget.trim());
    if (response?.data?.devOtp) {
      setDevOtp(response.data.devOtp);
    }
    success("New OTP code resent!");
  };

  return (
    <div
      className="min-h-screen flex items-center justify-center relative overflow-hidden p-4"
      style={{
        background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))",
      }}
    >
      <Heart className="absolute top-12 left-[10%] h-5 w-5 text-pink-soft fill-pink-soft opacity-40 animate-float-heart" />
      <Heart className="absolute top-24 right-[20%] h-4 w-4 text-pink-soft fill-pink-soft opacity-30 animate-float-heart [animation-delay:1s]" />

      <div className="bg-card rounded-3xl shadow-2xl p-6 sm:p-8 w-full max-w-md relative z-10 border border-white/20">
        <div className="text-center mb-6">
          <div className="flex items-center justify-center gap-2 mb-2">
            <Heart className="h-8 w-8 text-primary fill-primary animate-pulse" />
            <span className="text-2xl font-display font-bold text-foreground">Gathbandhan</span>
          </div>
          <p className="text-muted-foreground text-xs sm:text-sm">
            {t?.login?.subtitle || "Find your life partner with confidence"}
          </p>
        </div>

        {/* Login Method Tab (Password vs Real-Time OTP) */}
        <div className="flex rounded-xl bg-gray-100 dark:bg-gray-800 p-1 mb-6">
          <button
            type="button"
            onClick={() => setLoginMethod("PASSWORD")}
            className={`flex-1 py-2 text-xs font-semibold rounded-lg transition-all flex items-center justify-center gap-1.5 ${
              loginMethod === "PASSWORD"
                ? "bg-white dark:bg-gray-900 text-primary shadow-sm"
                : "text-gray-500 hover:text-gray-700"
            }`}
          >
            <Key className="w-3.5 h-3.5" />
            <span>Password Login</span>
          </button>
          <button
            type="button"
            onClick={() => setLoginMethod("OTP")}
            className={`flex-1 py-2 text-xs font-semibold rounded-lg transition-all flex items-center justify-center gap-1.5 ${
              loginMethod === "OTP"
                ? "bg-white dark:bg-gray-900 text-primary shadow-sm"
                : "text-gray-500 hover:text-gray-700"
            }`}
          >
            <Sparkles className="w-3.5 h-3.5 text-pink-500" />
            <span>Real-Time OTP Login</span>
          </button>
        </div>

        {/* PASSWORD LOGIN FORM */}
        {loginMethod === "PASSWORD" ? (
          <form className="space-y-4" onSubmit={handlePasswordLogin} autoComplete="off">
            <div>
              <label className="text-xs font-medium mb-1 block">{t?.login?.emailLabel || "Email Address"}</label>
              <input
                type="email"
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                  if (errors.email) setErrors((prev) => ({ ...prev, email: "" }));
                }}
                className="w-full border rounded-xl px-4 py-2.5 text-sm focus:ring-2 focus:ring-primary/20 outline-none"
                placeholder="Enter your email address"
              />
              {errors.email && <p className="text-xs text-red-500 mt-1">{errors.email}</p>}
            </div>

            <div>
              <label className="text-xs font-medium mb-1 block">{t?.login?.passwordLabel || "Password"}</label>
              <input
                type="password"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                  if (errors.password) setErrors((prev) => ({ ...prev, password: "" }));
                }}
                className="w-full border rounded-xl px-4 py-2.5 text-sm focus:ring-2 focus:ring-primary/20 outline-none"
                placeholder="Enter your password"
              />
              {errors.password && <p className="text-xs text-red-500 mt-1">{errors.password}</p>}
            </div>

            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                  className="rounded text-primary focus:ring-primary"
                />
                <span className="text-xs text-muted-foreground">{t?.login?.rememberMe || "Remember me"}</span>
              </div>
              <Link to="/forgot-password" className="text-xs text-pink-600 hover:text-pink-700 font-medium">
                Forgot Password?
              </Link>
            </div>

            <button
              type="submit"
              className="w-full bg-primary text-white py-3 rounded-xl hover:opacity-90 transition font-semibold text-sm shadow-md"
            >
              {t?.login?.button || "Sign In"}
            </button>
          </form>
        ) : (
          /* REAL-TIME OTP LOGIN FORM */
          <form className="space-y-4" onSubmit={handleSendLoginOTP}>
            <div>
              <label className="text-xs font-medium mb-1 block">Phone Number or Email</label>
              <div className="relative">
                <input
                  type="text"
                  value={otpTarget}
                  onChange={(e) => setOtpTarget(e.target.value)}
                  className="w-full border rounded-xl px-4 py-2.5 text-sm focus:ring-2 focus:ring-primary/20 outline-none"
                  placeholder="Enter phone (e.g. 9876543210) or email"
                  required
                />
              </div>
              <p className="text-[11px] text-muted-foreground mt-1">
                We will send an instant 6-digit OTP code to verify your identity.
              </p>
            </div>

            <button
              type="submit"
              disabled={otpLoading}
              className="w-full bg-gradient-to-r from-pink-600 to-purple-600 text-white py-3 rounded-xl hover:opacity-95 transition font-semibold text-sm shadow-md flex items-center justify-center gap-2"
            >
              <Sparkles className="w-4 h-4" />
              <span>Get Real-Time OTP Code</span>
            </button>
          </form>
        )}

        <div className="mt-6 pt-4 border-t border-gray-100 text-center text-xs text-muted-foreground">
          {t?.login?.noAccount || "Don't have an account?"}{" "}
          <Link to="/register" className="text-primary font-semibold hover:underline">
            {t?.login?.registerLink || "Create an account"}
          </Link>
        </div>
      </div>

      {/* REAL-TIME OTP VERIFICATION MODAL */}
      <OTPModal
        isOpen={showOtpModal}
        onClose={() => setShowOtpModal(false)}
        target={otpTarget}
        purpose="LOGIN"
        onVerify={handleVerifyLoginOTP}
        onResend={handleResendLoginOTP}
        loading={otpLoading}
        devOtp={devOtp}
      />
    </div>
  );
};

export default Login;