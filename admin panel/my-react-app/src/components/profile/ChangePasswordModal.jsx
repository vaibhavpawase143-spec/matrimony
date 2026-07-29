import { useState } from "react";
import {
  FaEye,
  FaEyeSlash,
  FaLock,
  FaTimes,
} from "react-icons/fa";
import { Loader2 } from "lucide-react";
import { toast } from "sonner";
import { changePassword } from "../../services/adminProfileService";

export default function ChangePasswordModal({
  open,
  onClose,
}) {
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  const [showPassword, setShowPassword] = useState({
    current: false,
    new: false,
    confirm: false,
  });

  if (!open) return null;

  const updateField = (field, value) => {
    setForm((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const togglePassword = (field) => {
    setShowPassword((prev) => ({
      ...prev,
      [field]: !prev[field],
    }));
  };

  const resetForm = () => {
    setForm({
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
    });

    setShowPassword({
      current: false,
      new: false,
      confirm: false,
    });
  };

  const passwordStrength = () => {
    const password = form.newPassword;

    let score = 0;

    if (password.length >= 8) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[a-z]/.test(password)) score++;
    if (/\d/.test(password)) score++;
    if (/[@$!%*?&#.^()_+=-]/.test(password)) score++;

    return score;
  };

  const strength = passwordStrength();

  const strengthColor = () => {
    if (strength <= 2) return "bg-red-500";
    if (strength === 3) return "bg-yellow-500";
    if (strength === 4) return "bg-blue-500";
    return "bg-green-600";
  };

  const strengthText = () => {
    if (strength <= 2) return "Weak";
    if (strength === 3) return "Medium";
    if (strength === 4) return "Strong";
    return "Very Strong";
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (
      !form.currentPassword ||
      !form.newPassword ||
      !form.confirmPassword
    ) {
      toast.error("Please fill all fields.");
      return;
    }

    if (form.newPassword !== form.confirmPassword) {
      toast.error("Passwords do not match.");
      return;
    }

    if (form.currentPassword === form.newPassword) {
      toast.error(
        "New password must be different from current password."
      );
      return;
    }

    try {
      setLoading(true);

      await changePassword(form);

      toast.success("Password changed successfully.");

      resetForm();

      onClose();
    } catch (error) {
      toast.error(
        error.message || "Unable to change password."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center bg-black/50 backdrop-blur-sm">

      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-lg overflow-hidden">

        {/* Header */}

        <div className="flex items-center justify-between px-6 py-4 border-b bg-violet-700 text-white">

          <div className="flex items-center gap-3">

            <FaLock className="text-xl" />

            <h2 className="text-lg font-semibold">
              Change Password
            </h2>

          </div>

          <button
            onClick={() => {
              resetForm();
              onClose();
            }}
            className="hover:text-red-200 transition"
          >
            <FaTimes />
          </button>

        </div>

        <form
          onSubmit={handleSubmit}
          className="p-6 space-y-5"
        >
                  {/* Current Password */}

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Current Password
                    </label>

                    <div className="relative">

                      <input
                        type={showPassword.current ? "text" : "password"}
                        value={form.currentPassword}
                        onChange={(e) =>
                          updateField("currentPassword", e.target.value)
                        }
                        placeholder="Enter current password"
                        className="w-full border rounded-xl px-4 py-3 pr-12 focus:ring-2 focus:ring-violet-500 focus:outline-none"
                      />

                      <button
                        type="button"
                        onClick={() => togglePassword("current")}
                        className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500"
                      >
                        {showPassword.current ? <FaEyeSlash /> : <FaEye />}
                      </button>

                    </div>
                  </div>

                  {/* New Password */}

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      New Password
                    </label>

                    <div className="relative">

                      <input
                        type={showPassword.new ? "text" : "password"}
                        value={form.newPassword}
                        onChange={(e) =>
                          updateField("newPassword", e.target.value)
                        }
                        placeholder="Enter new password"
                        className="w-full border rounded-xl px-4 py-3 pr-12 focus:ring-2 focus:ring-violet-500 focus:outline-none"
                      />

                      <button
                        type="button"
                        onClick={() => togglePassword("new")}
                        className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500"
                      >
                        {showPassword.new ? <FaEyeSlash /> : <FaEye />}
                      </button>

                    </div>

                    {/* Password Strength */}

                    {form.newPassword && (
                      <div className="mt-3">

                        <div className="flex justify-between text-xs mb-1">

                          <span>Password Strength</span>

                          <span className="font-semibold">
                            {strengthText()}
                          </span>

                        </div>

                        <div className="h-2 rounded-full bg-gray-200 overflow-hidden">

                          <div
                            className={`h-full transition-all ${strengthColor()}`}
                            style={{
                              width: `${(strength / 5) * 100}%`,
                            }}
                          />

                        </div>

                      </div>
                    )}

                  </div>

                  {/* Confirm Password */}

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Confirm Password
                    </label>

                    <div className="relative">

                      <input
                        type={showPassword.confirm ? "text" : "password"}
                        value={form.confirmPassword}
                        onChange={(e) =>
                          updateField("confirmPassword", e.target.value)
                        }
                        placeholder="Confirm new password"
                        className="w-full border rounded-xl px-4 py-3 pr-12 focus:ring-2 focus:ring-violet-500 focus:outline-none"
                      />

                      <button
                        type="button"
                        onClick={() => togglePassword("confirm")}
                        className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500"
                      >
                        {showPassword.confirm ? (
                          <FaEyeSlash />
                        ) : (
                          <FaEye />
                        )}
                      </button>

                    </div>
                  </div>

                  {/* Buttons */}

                  <div className="flex justify-end gap-3 pt-4 border-t">

                    <button
                      type="button"
                      onClick={() => {
                        resetForm();
                        onClose();
                      }}
                      className="px-5 py-2 rounded-xl border border-gray-300 hover:bg-gray-100 transition"
                    >
                      Cancel
                    </button>

                    <button
                      type="submit"
                      disabled={loading}
                      className="px-6 py-2 rounded-xl bg-violet-700 text-white hover:bg-violet-800 transition disabled:opacity-60 flex items-center gap-2"
                    >
                      {loading && (
                        <Loader2 className="w-4 h-4 animate-spin" />
                      )}

                      {loading
                        ? "Changing..."
                        : "Change Password"}

                    </button>

                  </div>

                </form>

              </div>

            </div>
          );
        }