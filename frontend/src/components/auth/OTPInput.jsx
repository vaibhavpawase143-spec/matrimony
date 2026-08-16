import React, { useRef, useEffect } from "react";

const OTPInput = ({ length = 6, value = "", onChange, disabled = false }) => {
  const inputRefs = useRef([]);

  useEffect(() => {
    if (inputRefs.current[0] && !disabled) {
      inputRefs.current[0].focus();
    }
  }, [disabled]);

  const handleChange = (index, e) => {
    const val = e.target.value.replace(/[^0-9]/g, "");
    if (!val) {
      // Cleared input
      const newOtp = value.split("");
      newOtp[index] = "";
      onChange(newOtp.join(""));
      return;
    }

    // Single digit input
    const digit = val.slice(-1);
    const newOtp = value.split("");

    // Ensure array is padded up to length
    while (newOtp.length < length) {
      newOtp.push("");
    }
    newOtp[index] = digit;
    const updatedValue = newOtp.join("");
    onChange(updatedValue);

    // Auto-advance to next box
    if (index < length - 1 && digit) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  const handleKeyDown = (index, e) => {
    if (e.key === "Backspace") {
      if (!value[index] && index > 0) {
        inputRefs.current[index - 1]?.focus();
      }
    } else if (e.key === "ArrowLeft" && index > 0) {
      inputRefs.current[index - 1]?.focus();
    } else if (e.key === "ArrowRight" && index < length - 1) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  const handlePaste = (e) => {
    e.preventDefault();
    if (disabled) return;
    const pastedData = e.clipboardData.getData("text").replace(/[^0-9]/g, "").slice(0, length);
    if (!pastedData) return;

    onChange(pastedData);
    const targetIndex = Math.min(pastedData.length, length - 1);
    inputRefs.current[targetIndex]?.focus();
  };

  const digits = Array.from({ length }, (_, i) => value[i] || "");

  return (
    <div className="flex items-center justify-center gap-2 sm:gap-3" onPaste={handlePaste}>
      {digits.map((digit, index) => (
        <input
          key={index}
          ref={(el) => (inputRefs.current[index] = el)}
          type="text"
          inputMode="numeric"
          pattern="[0-9]*"
          maxLength={1}
          value={digit}
          disabled={disabled}
          onChange={(e) => handleChange(index, e)}
          onKeyDown={(e) => handleKeyDown(index, e)}
          className={`w-11 h-13 sm:w-12 sm:h-14 text-center text-xl font-bold rounded-xl border-2 transition-all duration-200 outline-none
            ${disabled ? "bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed" : ""}
            ${
              digit
                ? "border-pink-600 bg-pink-50/50 text-pink-700 shadow-sm ring-2 ring-pink-500/20"
                : "border-gray-300 bg-white hover:border-gray-400 focus:border-pink-500 focus:ring-4 focus:ring-pink-500/20"
            }`}
        />
      ))}
    </div>
  );
};

export default OTPInput;
