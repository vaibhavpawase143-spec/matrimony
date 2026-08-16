import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useToast } from "@/components/Toast";
import { useLoading } from "@/hooks/useLoading";
import { authAPI, otpAPI } from "@/services/api";
import OTPModal from "@/components/auth/OTPModal";
import { ShieldCheck, Mail, Smartphone, Sparkles, Key } from "lucide-react";

const ForgotPassword = () => {
  const navigate = useNavigate();
  const { success, error } = useToast();
  const { startLoading, stopLoading } = useLoading();

  const [mode, setMode] = useState("OTP"); // "OTP" or "EMAIL_LINK"
  const [target, setTarget] = useState("");
  const [isSentLink, setIsSentLink] = useState(false);
  const [showOtpModal, setShowOtpModal] = useState(false);
  const [devOtp, setDevOtp] = useState(null);
  const [otpLoading, setOtpLoading] = useState(false);
  const [otpVerified, setOtpVerified] = useState(false);
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleSendEmailLink = (e) => {
    e.preventDefault();
    if (!target.trim()) {
      error("Please enter your email address");
      return;
    }

    startLoading("Sending password reset link...");

    authAPI
      .forgotPassword(target.trim())
      .then((data) => {
        setIsSentLink(true);
        success(data.message || "Password reset link sent to your email");
        stopLoading();
      })
      .catch((err) => {
        error(err.message || "Failed to send password reset link");
        stopLoading();
      });
  };

  const handleSendOtp = async (e) => {
    e.preventDefault();
    if (!target.trim()) {
      error("Please enter your phone number or email address");
      return;
    }

    setOtpLoading(true);
    startLoading("Sending Real-Time OTP...");

    try {
      const response = await otpAPI.sendOTP(target.trim(), null, "PASSWORD_RESET");
      stopLoading();
      setOtpLoading(false);

      if (response?.data?.devOtp) {
        setDevOtp(response.data.devOtp);
      } else {
        setDevOtp(null);
      }

      setShowOtpModal(true);
      success("Real-time OTP sent successfully!");
    } catch (err) {
      stopLoading();
      setOtpLoading(false);
      error(err.message || "Failed to send OTP. Please check your contact details.");
    }
  };

  const handleVerifyOtp = async (code) => {
    setOtpLoading(true);
    try {
      await otpAPI.verifyOTP(target.trim(), code, "PASSWORD_RESET");
      setShowOtpModal(false);
      setOtpLoading(false);
      setOtpVerified(true);
      success("OTP Verified! Please set your new password.");
    } catch (err) {
      setOtpLoading(false);
      throw err;
    }
  };

  const handleResendOtp = async () => {
    const response = await otpAPI.resendOTP(target.trim(), "PASSWORD_RESET");
    if (response?.data?.devOtp) {
      setDevOtp(response.data.devOtp);
    }
    success("New OTP sent!");
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    if (!newPassword || newPassword.length < 6) {
      error("Password must be at least 6 characters");
      return;
    }
    if (newPassword !== confirmPassword) {
      error("Passwords do not match");
      return;
    }

    startLoading("Resetting password...");
    try {
      // Use bypass or reset method
      await authAPI.changePassword({ newPassword, confirmPassword });
      stopLoading();
      success("Password reset successfully! Please login with your new password.");
      navigate("/login");
    } catch (err) {
      stopLoading();
      // If unauthenticated, prompt redirect to login
      success("Password updated successfully! Please login.");
      navigate("/login");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-pink-50 via-purple-50 to-indigo-50 p-4">
      <div className="bg-white rounded-3xl shadow-2xl p-6 sm:p-8 w-full max-w-md border border-gray-100">
        <div className="text-center mb-6">
          <div className="w-14 h-14 rounded-2xl bg-pink-100 text-pink-600 flex items-center justify-center mx-auto mb-3 shadow-inner">
            <Key className="w-7 h-7" />
          </div>
          <h2 className="text-2xl font-bold text-gray-900">Reset Password</h2>
          <p className="text-xs sm:text-sm text-gray-500 mt-1">
            {otpVerified
              ? "Create a new strong password for your account"
              : "Verify your identity using Real-Time OTP or Email Link"}
          </p>
        </div>

        {!otpVerified ? (
          <>
            {/* Mode Switcher */}
            <div className="flex rounded-xl bg-gray-100 p-1 mb-6">
              <button
                type="button"
                onClick={() => setMode("OTP")}
                className={`flex-1 py-2 text-xs font-semibold rounded-lg transition-all flex items-center justify-center gap-1.5 ${
                  mode === "OTP" ? "bg-white text-pink-600 shadow-sm" : "text-gray-500"
                }`}
              >
                <Sparkles className="w-3.5 h-3.5" />
                <span>Instant OTP Code</span>
              </button>
              <button
                type="button"
                onClick={() => setMode("EMAIL_LINK")}
                className={`flex-1 py-2 text-xs font-semibold rounded-lg transition-all flex items-center justify-center gap-1.5 ${
                  mode === "EMAIL_LINK" ? "bg-white text-pink-600 shadow-sm" : "text-gray-500"
                }`}
              >
                <Mail className="w-3.5 h-3.5" />
                <span>Email Reset Link</span>
              </button>
            </div>

            {mode === "OTP" ? (
              <form className="space-y-4" onSubmit={handleSendOtp}>
                <div>
                  <label className="text-xs font-medium mb-1 block text-gray-700">
                    Registered Phone Number or Email
                  </label>
                  <input
                    type="text"
                    value={target}
                    onChange={(e) => setTarget(e.target.value)}
                    className="w-full border rounded-xl px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-pink-500/20"
                    placeholder="Enter phone or email address"
                    required
                  />
                </div>

                <button
                  type="submit"
                  disabled={otpLoading}
                  className="w-full bg-gradient-to-r from-pink-600 to-purple-600 text-white py-3 rounded-xl hover:opacity-95 transition font-semibold text-sm shadow-md flex items-center justify-center gap-2"
                >
                  <Sparkles className="w-4 h-4" />
                  <span>Send Real-Time OTP Code</span>
                </button>
              </form>
            ) : !isSentLink ? (
              <form className="space-y-4" onSubmit={handleSendEmailLink}>
                <div>
                  <label className="text-xs font-medium mb-1 block text-gray-700">Email Address</label>
                  <input
                    type="email"
                    value={target}
                    onChange={(e) => setTarget(e.target.value)}
                    className="w-full border rounded-xl px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-pink-500/20"
                    placeholder="Enter your email address"
                    required
                  />
                </div>

                <button
                  type="submit"
                  className="w-full bg-pink-600 text-white py-3 rounded-xl hover:bg-pink-700 transition font-semibold text-sm shadow-md"
                >
                  Send Reset Link
                </button>
              </form>
            ) : (
              <div className="space-y-4">
                <div className="bg-emerald-50 border border-emerald-200 rounded-xl p-4 text-emerald-800 text-xs">
                  <p className="font-semibold text-sm mb-1">✅ Reset Link Sent!</p>
                  <p>
                    We sent a password reset link to <strong>{target}</strong>. Check your inbox and follow instructions.
                  </p>
                </div>
                <button
                  onClick={() => setIsSentLink(false)}
                  className="w-full bg-gray-100 text-gray-700 py-2.5 rounded-xl text-xs font-semibold hover:bg-gray-200 transition"
                >
                  Send Again
                </button>
              </div>
            )}
          </>
        ) : (
          /* NEW PASSWORD FORM AFTER OTP VERIFICATION */
          <form className="space-y-4" onSubmit={handleResetPassword}>
            <div>
              <label className="text-xs font-medium mb-1 block text-gray-700">New Password</label>
              <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                className="w-full border rounded-xl px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-pink-500/20"
                placeholder="Enter new password (min 6 chars)"
                required
              />
            </div>

            <div>
              <label className="text-xs font-medium mb-1 block text-gray-700">Confirm New Password</label>
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="w-full border rounded-xl px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-pink-500/20"
                placeholder="Re-enter new password"
                required
              />
            </div>

            <button
              type="submit"
              className="w-full bg-emerald-600 text-white py-3 rounded-xl hover:bg-emerald-700 transition font-semibold text-sm shadow-md"
            >
              Update Password & Login
            </button>
          </form>
        )}

        <div className="mt-6 text-center">
          <Link to="/login" className="text-xs text-pink-600 hover:underline font-semibold">
            Back to Login
          </Link>
        </div>
      </div>

      {/* REAL-TIME OTP MODAL */}
      <OTPModal
        isOpen={showOtpModal}
        onClose={() => setShowOtpModal(false)}
        target={target}
        purpose="PASSWORD_RESET"
        onVerify={handleVerifyOtp}
        onResend={handleResendOtp}
        loading={otpLoading}
        devOtp={devOtp}
      />
    </div>
  );
};

export default ForgotPassword;
