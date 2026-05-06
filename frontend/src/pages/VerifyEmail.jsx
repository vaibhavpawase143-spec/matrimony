import { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import { useLanguage } from "@/context/LanguageContext.jsx";
import { authAPI } from "@/services/api";

const VerifyEmail = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { success, error } = useToast();
  const { startLoading, stopLoading } = useLoading();
  const { t } = useLanguage();

  const [status, setStatus] = useState("loading");

  useEffect(() => {
    const token = searchParams.get("token");
    
    if (!token) {
      setStatus("error");
      error("Invalid verification link");
      return;
    }

    startLoading("Verifying email...");
    
    // Call backend to verify email
    fetch(`/api/auth/verify?token=${token}`)
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
        throw new Error("Verification failed");
      })
      .then((data) => {
        setStatus("success");
        success("Email verified successfully! You can now login.");
        stopLoading();
        
        // Redirect to login after 2 seconds
        setTimeout(() => {
          navigate("/login");
        }, 2000);
      })
      .catch((err) => {
        setStatus("error");
        error(err.message || "Email verification failed");
        stopLoading();
      });
  }, [searchParams, navigate, success, error, startLoading, stopLoading]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-muted/30">
      <div className="bg-card rounded-2xl shadow-2xl p-8 w-full max-w-md mx-4 text-center">
        {status === "loading" && (
          <div>
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
            <h2 className="text-xl font-semibold mb-2">Verifying your email...</h2>
            <p className="text-muted-foreground">Please wait while we verify your email address.</p>
          </div>
        )}

        {status === "success" && (
          <div>
            <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg className="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
              </svg>
            </div>
            <h2 className="text-xl font-semibold mb-2 text-green-600">Email Verified!</h2>
            <p className="text-muted-foreground mb-4">Your email has been successfully verified.</p>
            <p className="text-sm text-muted-foreground">Redirecting to login page...</p>
          </div>
        )}

        {status === "error" && (
          <div>
            <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg className="w-8 h-8 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </div>
            <h2 className="text-xl font-semibold mb-2 text-red-600">Verification Failed</h2>
            <p className="text-muted-foreground mb-4">Invalid or expired verification link.</p>
            <button
              onClick={() => navigate("/login")}
              className="bg-primary text-white px-6 py-2 rounded-lg hover:opacity-90 transition"
            >
              Back to Login
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default VerifyEmail;
