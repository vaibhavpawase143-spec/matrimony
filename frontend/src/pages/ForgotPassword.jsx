import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import { authAPI } from "@/services/api";

const ForgotPassword = () => {
  const navigate = useNavigate();
  const { success, error } = useToast();
  const { startLoading, stopLoading } = useLoading();

  const [email, setEmail] = useState("");
  const [isSent, setIsSent] = useState(false);

  const handleForgotPassword = (e) => {
    e.preventDefault();
    
    if (!email.trim()) {
      error("Please enter your email address");
      return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      error("Please enter a valid email address");
      return;
    }

    startLoading("Sending password reset link...");

    authAPI.forgotPassword(email.trim())
      .then((data) => {
        setIsSent(true);
        success(data.message || "Password reset link sent to your email");
        stopLoading();
      })
      .catch((err) => {
        // Use backend error message directly
        const errorMessage = err.message || "Failed to send password reset link";
        
        // Handle specific error cases using error codes and status from backend
        if (err.status === 404) {
          error(errorMessage);
        } else if (err.errorCode === 'ERR_USER_NOT_FOUND') {
          error(errorMessage);
        } else if (err.status === 400) {
          error(errorMessage);
        } else if (err.status === 429) {
          error(errorMessage);
        } else if (err.status === 500) {
          error("Server is experiencing issues. Please try again later.");
        } else {
          error(errorMessage);
        }
        stopLoading();
      });
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-muted/30">
      <div className="bg-card rounded-2xl shadow-2xl p-8 w-full max-w-md mx-4">
        <div className="text-center mb-6">
          <h2 className="text-2xl font-bold mb-2">Forgot Password</h2>
          <p className="text-muted-foreground">
            {isSent 
              ? "Check your email for the reset link"
              : "Enter your email to receive a password reset link"
            }
          </p>
        </div>

        {!isSent ? (
          <form className="space-y-4" onSubmit={handleForgotPassword}>
            <div>
              <label className="text-sm font-medium mb-1 block">Email Address</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full border rounded-lg px-4 py-2.5 text-sm"
                placeholder="Enter your email address"
                required
              />
            </div>

            <button
              type="submit"
              className="w-full bg-primary text-white py-2.5 rounded-lg hover:opacity-90 transition"
            >
              Send Reset Link
            </button>
          </form>
        ) : (
          <div className="space-y-4">
            <div className="bg-green-50 border border-green-200 rounded-lg p-4">
              <p className="text-green-800 text-sm">
                ✅ Password reset link has been sent to <strong>{email}</strong>
              </p>
              <p className="text-green-700 text-xs mt-2">
                Please check your inbox and click the reset link.
              </p>
            </div>
            
            <button
              onClick={() => setIsSent(false)}
              className="w-full bg-secondary text-secondary-foreground py-2.5 rounded-lg hover:opacity-90 transition"
            >
              Send Another Link
            </button>
          </div>
        )}

        <div className="mt-6 text-center space-y-2">
          <p className="text-xs">
            <Link to="/login" className="text-primary hover:underline">
              Back to Login
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;
