import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FiEye, FiEyeOff } from "react-icons/fi";
import Input from "../components/Input";
import Button from "../components/Button";
import Loader from "../components/Loader";
import { loginAdmin } from "../services/authService";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");

    if (!email) {
      setError("Email is required.");
      return;
    }

    if (!password) {
      setError("Password is required.");
      return;
    }

    setLoading(true);
    try {
      const { token, user } = await loginAdmin({ email, password });
      localStorage.setItem("authToken", token);
      localStorage.setItem("user", JSON.stringify(user));
      navigate("/dashboard");
    } catch (err) {
      setError(
        err?.response?.data?.message || "Login failed. Please check your credentials and try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-purple-50 flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-md rounded-3xl border border-purple-200 bg-white p-8 shadow-xl shadow-purple-200/50">
        <div className="mb-8 text-center">
          <p className="text-sm uppercase tracking-[0.3em] text-purple-600">Admin login</p>
          <h1 className="mt-3 text-3xl font-semibold text-gray-800">Gathbandhan Admin Portal</h1>
        </div>

        {error && (
          <div className="mb-4 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <Input
            label="Email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="admin@example.com"
          />

          <div>
            <div className="flex items-center justify-between">
              <label className="block text-sm font-medium text-gray-700">Password</label>
              <button
                type="button"
                className="text-sm font-medium text-violet-700 hover:text-violet-800"
                onClick={() => setShowPassword((current) => !current)}
              >
                {showPassword ? "Hide" : "Show"}
              </button>
            </div>
            <div className="mt-1 relative">
              <input
                type={showPassword ? "text" : "password"}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter your password"
                className="block w-full rounded-xl border border-purple-300 bg-white px-3 py-2 text-sm shadow-sm outline-none transition focus:border-violet-500 focus:ring-2 focus:ring-violet-100"
              />
              <span className="pointer-events-none absolute inset-y-0 right-3 flex items-center text-gray-500">
                {showPassword ? <FiEyeOff /> : <FiEye />}
              </span>
            </div>
          </div>

          <div className="flex items-center justify-between gap-4 text-sm text-gray-600">
            <label className="inline-flex items-center gap-2">
              <input
                type="checkbox"
                checked={rememberMe}
                onChange={(e) => setRememberMe(e.target.checked)}
                className="h-4 w-4 rounded border-purple-300 text-violet-700 focus:ring-violet-500"
              />
              Remember me
            </label>
            <button type="button" className="text-violet-700 hover:text-violet-800">
              Forgot password?
            </button>
          </div>

          <Button type="submit" className="w-full py-3" disabled={loading}>
            {loading ? <Loader /> : "Login"}
          </Button>
        </form>
      </div>
    </div>
  );
}

export default Login;
