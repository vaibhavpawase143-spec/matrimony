import { useState, useEffect } from "react";
import { useSearchParams, useNavigate, Link } from "react-router-dom";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import { authAPI } from "@/services/api";

const ResetPassword = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { success, error } = useToast();
  const { startLoading, stopLoading } = useLoading();

  const [token, setToken] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [isReset, setIsReset] = useState(false);

  useEffect(() => {
    const resetToken = searchParams.get("token");
    if (!resetToken) {
      error("Invalid reset link");
      navigate("/forgot-password");
    } else {
      setToken(resetToken);
    }
  }, [searchParams, navigate, error]);

  const handleResetPassword = (e) => {
    e.preventDefault();
    
    if (!newPassword.trim()) {
      error("Please enter a new password");
      return;
    }

    if (newPassword.length < 6) {
      error("Password must be at least 6 characters");
      return;
    }

    if (!confirmPassword.trim()) {
      error("Please confirm your new password");
      return;
    }

    if (newPassword !== confirmPassword) {
      error("Passwords do not match");
      return;
    }

    startLoading("Resetting password...");

    authAPI.resetPassword(token, newPassword)
      .then((data) => {
        setIsReset(true);
        success(data.message || "Password reset successfully!");
        stopLoading();
        
        // Redirect to login after 2 seconds
        setTimeout(() => {
          navigate("/login");
        }, 2000);
      })
      .catch((err) => {
        // Use backend error message directly
        const errorMessage = err.message || "Failed to reset password";
        
        // Handle specific error cases using error codes and status from backend
        if (err.errorCode === 'ERR_TOKEN_EXPIRED' || err.errorCode === 'ERR_INVALID_TOKEN' || err.status === 400) {
          error(errorMessage);
          setTimeout(() => {
            navigate("/forgot-password");
          }, 2000);
        } else if (err.status === 404) {
          error(errorMessage);
          setTimeout(() => {
            navigate("/forgot-password");
          }, 2000);
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
          <h2 className="text-2xl font-bold mb-2">Reset Password</h2>
          <p className="text-muted-foreground">
            {isReset 
              ? "Password reset successful"
              : "Enter your new password"
            }
          </p>
        </div>

        {!isReset ? (
          <form className="space-y-4" onSubmit={handleResetPassword}>
            <div>
              <label className="text-sm font-medium mb-1 block">New Password</label>
              <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                className="w-full border rounded-lg px-4 py-2.5 text-sm"
                placeholder="Enter new password"
                required
              />
            </div>

            <div>
              <label className="text-sm font-medium mb-1 block">Confirm Password</label>
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="w-full border rounded-lg px-4 py-2.5 text-sm"
                placeholder="Confirm new password"
                required
              />
            </div>

            <button
              type="submit"
              className="w-full bg-primary text-white py-2.5 rounded-lg hover:opacity-90 transition"
            >
              Reset Password
            </button>
          </form>
        ) : (
          <div className="space-y-4">
            <div className="bg-green-50 border border-green-200 rounded-lg p-4">
              <p className="text-green-800 text-sm">
                ✅ Password has been reset successfully
              </p>
              <p className="text-green-700 text-xs mt-2">
                Redirecting to login page...
              </p>
            </div>
          </div>
        )}

        <div className="mt-6 text-center space-y-2">
          {!isReset && (
            <p className="text-xs">
              <Link to="/login" className="text-primary hover:underline">
                Back to Login
              </Link>
            </p>
          )}
        </div>
      </div>
    </div>
  );
};

export default ResetPassword;
