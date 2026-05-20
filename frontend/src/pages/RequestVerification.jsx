import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import { useLanguage } from "@/context/LanguageContext.jsx";
import { apiClient } from "@/services/api";

const RequestVerification = () => {
  const navigate = useNavigate();
  const { success, error } = useToast();
  const { startLoading, stopLoading } = useLoading();
  const { t } = useLanguage();

  const [email, setEmail] = useState("");
  const [isSent, setIsSent] = useState(false);

  const handleSendVerification = (e) => {
    e.preventDefault();
    
    if (!email.trim()) {
      error("Please enter your email address");
      return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      error("Please enter a valid email address");
      return;
    }

    startLoading("Sending verification email...");

    // Call backend to send verification email using api.js client
    apiClient(`/auth/send-verification?email=${encodeURIComponent(email.trim())}`, {
      method: "POST"
    })
      .then((data) => {
        setIsSent(true);
        success("Verification email sent successfully!");
        stopLoading();
      })
      .catch((err) => {
        // Use backend error message directly
        const errorMessage = err.message || "Failed to send verification email";
        
        // Handle specific error cases using error codes and status from backend
        if (err.status === 404) {
          error(errorMessage);
        } else if (err.errorCode === 'ERR_EMAIL_ALREADY_VERIFIED') {
          error(errorMessage);
          setTimeout(() => {
            navigate("/login");
          }, 2000);
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

  const handleBypassVerification = () => {
    if (!email.trim()) {
      error("Please enter your email address");
      return;
    }

    startLoading("Bypassing verification...");

    // Development bypass using api.js client
    apiClient(`/auth/bypass-verification?email=${encodeURIComponent(email.trim())}`, {
      method: "POST"
    })
      .then((data) => {
        success("Email verification bypassed! You can now login.");
        stopLoading();
        
        // Redirect to login after 1 second
        setTimeout(() => {
          navigate("/login");
        }, 1000);
      })
      .catch((err) => {
        // Use backend error message directly
        const errorMessage = err.message || "Failed to bypass verification";
        
        // Handle specific error cases using error codes and status from backend
        if (err.status === 404) {
          error(errorMessage);
        } else if (err.errorCode === 'ERR_EMAIL_ALREADY_VERIFIED') {
          error(errorMessage);
          setTimeout(() => {
            navigate("/login");
          }, 2000);
        } else if (err.errorCode === 'ERR_BYPASS_NOT_ALLOWED' || err.status === 403) {
          error(errorMessage);
        } else if (err.status === 400) {
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
          <h2 className="text-2xl font-bold mb-2">Email Verification</h2>
          <p className="text-muted-foreground">
            {isSent 
              ? "Check your email for the verification link"
              : "Enter your email to receive a verification link"
            }
          </p>
        </div>

        {!isSent ? (
          <form className="space-y-4" onSubmit={handleSendVerification}>
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
              Send Verification Email
            </button>
          </form>
        ) : (
          <div className="space-y-4">
            <div className="bg-green-50 border border-green-200 rounded-lg p-4">
              <p className="text-green-800 text-sm">
                ✅ Verification email has been sent to <strong>{email}</strong>
              </p>
              <p className="text-green-700 text-xs mt-2">
                Please check your inbox and click the verification link.
              </p>
            </div>
            
            <button
              onClick={() => setIsSent(false)}
              className="w-full bg-secondary text-secondary-foreground py-2.5 rounded-lg hover:opacity-90 transition"
            >
              Send Another Email
            </button>
          </div>
        )}

        
        <p className="text-center text-xs mt-4">
          <button
            onClick={() => navigate("/login")}
            className="text-primary hover:underline"
          >
            Back to Login
          </button>
        </p>
      </div>
    </div>
  );
};

export default RequestVerification;
