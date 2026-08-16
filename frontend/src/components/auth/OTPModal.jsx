import React, { useState, useEffect } from "react";
import { X, Smartphone, Mail, ShieldCheck, RefreshCw, Clock, AlertCircle, CheckCircle2 } from "lucide-react";
import OTPInput from "./OTPInput";

const OTPModal = ({
  isOpen,
  onClose,
  target,
  purpose = "VERIFICATION",
  onVerify,
  onResend,
  loading = false,
  devOtp = null,
}) => {
  const [otp, setOtp] = useState("");
  const [cooldown, setCooldown] = useState(60);
  const [expiry, setExpiry] = useState(600); // 10 minutes
  const [errorMsg, setErrorMsg] = useState("");
  const isEmail = target?.includes("@");

  useEffect(() => {
    if (isOpen) {
      setOtp("");
      setCooldown(60);
      setExpiry(600);
      setErrorMsg("");
    }
  }, [isOpen]);

  // Cooldown countdown timer (60s)
  useEffect(() => {
    if (!isOpen || cooldown <= 0) return;
    const interval = setInterval(() => {
      setCooldown((prev) => Math.max(0, prev - 1));
    }, 1000);
    return () => clearInterval(interval);
  }, [isOpen, cooldown]);

  // Expiry countdown timer (10m = 600s)
  useEffect(() => {
    if (!isOpen || expiry <= 0) return;
    const interval = setInterval(() => {
      setExpiry((prev) => Math.max(0, prev - 1));
    }, 1000);
    return () => clearInterval(interval);
  }, [isOpen, expiry]);

  // Auto verify when 6 digits completed
  useEffect(() => {
    if (otp.length === 6 && !loading) {
      handleVerify();
    }
  }, [otp]);

  if (!isOpen) return null;

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs < 10 ? "0" : ""}${secs}`;
  };

  const handleVerify = async () => {
    if (otp.length !== 6) {
      setErrorMsg("Please enter complete 6-digit OTP code");
      return;
    }
    setErrorMsg("");
    try {
      await onVerify(otp);
    } catch (err) {
      setErrorMsg(err?.message || "Invalid OTP code. Please try again.");
    }
  };

  const handleResendClick = async () => {
    if (cooldown > 0 || loading) return;
    setErrorMsg("");
    try {
      await onResend();
      setCooldown(60);
      setExpiry(600);
      setOtp("");
    } catch (err) {
      setErrorMsg(err?.message || "Failed to resend OTP");
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
      <div className="bg-white dark:bg-slate-900 rounded-3xl shadow-2xl max-w-md w-full p-6 sm:p-8 relative border border-gray-100 dark:border-slate-800 transform transition-all animate-scale-up">
        {/* Close Button */}
        <button
          onClick={onClose}
          disabled={loading}
          className="absolute top-4 right-4 p-2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 rounded-full hover:bg-gray-100 dark:hover:bg-slate-800 transition"
        >
          <X className="w-5 h-5" />
        </button>

        {/* Header Badge & Title */}
        <div className="text-center mb-6">
          <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-pink-100 dark:bg-pink-950/50 text-pink-600 mb-4 shadow-inner">
            {isEmail ? <Mail className="w-8 h-8" /> : <Smartphone className="w-8 h-8" />}
          </div>
          <h3 className="text-2xl font-bold text-gray-900 dark:text-white mb-1">
            Real-Time OTP Verification
          </h3>
          <p className="text-sm text-gray-500 dark:text-gray-400">
            We sent a 6-digit code to <br />
            <span className="font-semibold text-gray-900 dark:text-gray-100 inline-flex items-center gap-1 mt-1">
              {target}
            </span>
          </p>
        </div>

        {/* Dev Mode Notification Badge */}
        {devOtp && (
          <div className="mb-4 p-3 rounded-xl bg-amber-50 dark:bg-amber-950/40 border border-amber-200 dark:border-amber-800/50 text-amber-800 dark:text-amber-300 text-xs text-center flex items-center justify-center gap-2 font-mono">
            <ShieldCheck className="w-4 h-4 text-amber-600" />
            <span>[Dev Mode Code: <strong>{devOtp}</strong>]</span>
          </div>
        )}

        {/* Expiry Timer & Cooldown Status */}
        <div className="flex items-center justify-between text-xs text-gray-500 mb-6 bg-gray-50 dark:bg-slate-800/50 px-4 py-2.5 rounded-xl border border-gray-100 dark:border-slate-800">
          <span className="flex items-center gap-1 text-gray-600 dark:text-gray-300">
            <Clock className="w-3.5 h-3.5 text-pink-500" />
            OTP Expiry: <strong className="text-gray-900 dark:text-white font-mono">{formatTime(expiry)}</strong>
          </span>
          <span className="text-pink-600 font-medium">3 attempts max</span>
        </div>

        {/* Error Alert */}
        {errorMsg && (
          <div className="mb-4 p-3 rounded-xl bg-red-50 dark:bg-red-950/40 border border-red-200 dark:border-red-800/50 text-red-600 dark:text-red-300 text-xs flex items-center gap-2">
            <AlertCircle className="w-4 h-4 shrink-0" />
            <span>{errorMsg}</span>
          </div>
        )}

        {/* 6-Digit OTP Input */}
        <div className="mb-6">
          <OTPInput length={6} value={otp} onChange={setOtp} disabled={loading || expiry === 0} />
        </div>

        {/* Action Buttons */}
        <div className="space-y-3">
          <button
            onClick={handleVerify}
            disabled={otp.length !== 6 || loading || expiry === 0}
            className="w-full py-3.5 px-4 rounded-xl bg-gradient-to-r from-pink-600 to-purple-600 text-white font-semibold shadow-lg shadow-pink-500/25 hover:opacity-95 active:scale-[0.99] transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
          >
            {loading ? (
              <>
                <RefreshCw className="w-5 h-5 animate-spin" />
                <span>Verifying Code...</span>
              </>
            ) : (
              <>
                <CheckCircle2 className="w-5 h-5" />
                <span>Verify & Proceed</span>
              </>
            )}
          </button>

          <div className="text-center pt-2">
            <button
              onClick={handleResendClick}
              disabled={cooldown > 0 || loading}
              className="text-xs font-semibold text-pink-600 hover:text-pink-700 disabled:text-gray-400 disabled:cursor-not-allowed transition inline-flex items-center gap-1.5"
            >
              <RefreshCw className={`w-3.5 h-3.5 ${loading ? "animate-spin" : ""}`} />
              {cooldown > 0 ? `Resend OTP in ${cooldown}s` : "Resend OTP Code"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OTPModal;
