import { Heart } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "@/hooks/useAuth";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
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

  const handleLogin = (e) => {
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
      errs.password =
        t?.login?.errors?.passwordMin || "Password must be at least 6 characters";
    }

    setErrors(errs);

    if (Object.keys(errs).length > 0) {
      error(t?.login?.messages?.fixErrorsFirst || "Please fix the errors first");
      return;
    }

    startLoading(t?.login?.messages?.loggingIn || "Logging in...");

    try {
      // Simple local login
      const adminEmail = "admin@gmail.com";
      const adminPassword = "admin123";

      const userEmail = "user@gmail.com";
      const userPassword = "user123";

      let user = null;

      if (email.trim() === adminEmail && password === adminPassword) {
        user = {
          first_name: "Admin",
          email: adminEmail,
          role: "ADMIN",
        };
      } else if (email.trim() === userEmail && password === userPassword) {
        user = {
          first_name: "User",
          email: userEmail,
          role: "USER",
        };
      } else {
        throw new Error("Invalid email or password");
      }

      console.log("Login: Attempting login with user:", user);
      console.log("Login: User role:", user.role);

      // Store user data in localStorage first
      localStorage.setItem("user", JSON.stringify(user));
      localStorage.setItem("role", user.role);

      // Call login with full user object
      login(user);

      console.log("Login: Auth state updated, navigating to:", user.role === "ADMIN" ? "/admin" : "/home");

      if (rememberMe) {
        localStorage.setItem("rememberedEmail", email.trim());
      } else {
        localStorage.removeItem("rememberedEmail");
      }

      success(t?.login?.messages?.loginSuccess || "Login successful");
      stopLoading();

      // Add small delay to ensure auth state is updated before navigation
      setTimeout(() => {
        console.log("Login: Navigating to:", user.role === "ADMIN" ? "/admin" : "/home");
        if (user.role === "ADMIN") {
          navigate("/admin");
        } else {
          navigate("/home");
        }
      }, 100);
    } catch (err) {
      stopLoading();
      error(err.message || t?.login?.messages?.loginFailed || "Login failed");
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
          <p className="text-muted-foreground text-sm">
            {t?.login?.subtitle || "Welcome back"}
          </p>
        </div>

        <form className="space-y-4" onSubmit={handleLogin}>
          <div>
            <label className="text-xs font-medium mb-1 block">
              {t?.login?.emailLabel || "Email"}
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => {
                setEmail(e.target.value);
                if (errors.email) {
                  setErrors((prev) => ({ ...prev, email: "" }));
                }
              }}
              className="w-full border rounded-lg px-4 py-2.5 text-sm"
              placeholder={t?.login?.emailPlaceholder || "Enter your email"}
            />
            {errors.email && (
              <p className="text-xs text-red-500 mt-1">{errors.email}</p>
            )}
          </div>

          <div>
            <label className="text-xs font-medium mb-1 block">
              {t?.login?.passwordLabel || "Password"}
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
                if (errors.password) {
                  setErrors((prev) => ({ ...prev, password: "" }));
                }
              }}
              className="w-full border rounded-lg px-4 py-2.5 text-sm"
              placeholder={t?.login?.passwordPlaceholder || "Enter your password"}
            />
            {errors.password && (
              <p className="text-xs text-red-500 mt-1">{errors.password}</p>
            )}
          </div>

          <div className="flex items-center gap-2">
            <input
              type="checkbox"
              checked={rememberMe}
              onChange={(e) => setRememberMe(e.target.checked)}
            />
            <span className="text-xs">
              {t?.login?.rememberMe || "Remember me"}
            </span>
          </div>

          <button
            type="submit"
            className="w-full bg-primary text-white py-2.5 rounded-lg hover:opacity-90 transition"
          >
            {t?.login?.button || "Login"}
          </button>

          <p className="text-center text-xs">
            {t?.login?.noAccount || "Don't have an account?"}{" "}
            <Link to="/register" className="text-primary font-semibold">
              {t?.login?.registerLink || "Register"}
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
};

export default Login;