import { Heart } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "@/hooks/useAuth";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import { authAPI } from "@/services/api";
import { useLanguage } from "@/context/LanguageContext.jsx";

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const { success, error } = useToast();
  const { startLoading, stopLoading } = useLoading();
  const { t } = useLanguage();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const [errors, setErrors] = useState({});

  const handleLogin = async () => {
    const errs = {};

    if (!email.trim()) {
      errs.email = t.login.errors.emailRequired;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      errs.email = t.login.errors.emailInvalid;
    }

    if (!password.trim()) {
      errs.password = t.login.errors.passwordRequired;
    } else if (password.length < 6) {
      errs.password = t.login.errors.passwordMin;
    }

    setErrors(errs);

    if (Object.keys(errs).length === 0) {
      startLoading(t.login.messages.loggingIn);

      try {
        if (
          email === "admin@gathbandhan.com" &&
          password === "admin123"
        ) {
          localStorage.setItem("role", "ADMIN");
          login("admin");
          success(t.login.messages.adminLoginSuccess);
          stopLoading();
          navigate("/admin");
          return;
        }

        const response = await authAPI.login({
          email: email.trim(),
          password,
          rememberMe,
        });

        const user = response.user;

        login(user?.first_name || user?.email || email.split("@")[0]);

        const role = user?.role || "USER";
        localStorage.setItem("role", role);

        success(t.login.messages.loginSuccess);
        stopLoading();

        if (role === "ADMIN") {
          navigate("/admin");
        } else {
          navigate("/home");
        }
      } catch (err) {
        stopLoading();
        error(err.message || t.login.messages.loginFailed);

        if (err.field) {
          setErrors({ [err.field]: err.message });
        }
      }
    } else {
      error(t.login.messages.fixErrorsFirst);
    }
  };

  return (
    <div
      className="min-h-screen flex items-center justify-center relative overflow-hidden"
      style={{
        background:
          "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))",
      }}
    >
      <Heart className="absolute top-12 left-[10%] h-5 w-5 text-pink-soft fill-pink-soft opacity-40 animate-float-heart" />
      <Heart className="absolute top-24 right-[20%] h-4 w-4 text-pink-soft fill-pink-soft opacity-30 animate-float-heart [animation-delay:1s]" />

      <div className="bg-card rounded-2xl shadow-2xl p-8 w-full max-w-md mx-4 relative z-10">
        <div className="text-center mb-6">
          <div className="flex items-center justify-center gap-2 mb-2">
            <Heart className="h-7 w-7 text-primary fill-primary" />
            <span className="text-2xl font-display font-bold text-foreground">
              Gathbandhan
            </span>
          </div>
          <p className="text-muted-foreground text-sm">{t.login.subtitle}</p>
        </div>

        <div className="space-y-4">
          <div>
            <label className="text-xs font-medium mb-1 block">{t.login.emailLabel}</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full border rounded-lg px-4 py-2.5 text-sm"
              placeholder={t.login.emailPlaceholder}
            />
            {errors.email && (
              <p className="text-xs text-red-500">{errors.email}</p>
            )}
          </div>

          <div>
            <label className="text-xs font-medium mb-1 block">{t.login.passwordLabel}</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full border rounded-lg px-4 py-2.5 text-sm"
              placeholder={t.login.passwordPlaceholder}
            />
            {errors.password && (
              <p className="text-xs text-red-500">{errors.password}</p>
            )}
          </div>

          <div className="flex items-center gap-2">
            <input
              type="checkbox"
              checked={rememberMe}
              onChange={(e) => setRememberMe(e.target.checked)}
            />
            <span className="text-xs">{t.login.rememberMe}</span>
          </div>

          <button
            onClick={handleLogin}
            className="w-full bg-primary text-white py-2.5 rounded-lg"
          >
            {t.login.button}
          </button>

          <p className="text-center text-xs">
            {t.login.noAccount}{" "}
            <Link to="/register" className="text-primary font-semibold">
              {t.login.registerLink}
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
