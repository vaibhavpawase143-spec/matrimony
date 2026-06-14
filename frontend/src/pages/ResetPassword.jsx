import { useState } from "react";
import {
  useSearchParams,
  useNavigate
} from "react-router-dom";
import toast from "react-hot-toast";
import { authAPI } from "@/services/api";

const ResetPassword = () => {

  const [searchParams] =
    useSearchParams();

  const navigate =
    useNavigate();

  const token =
    searchParams.get("token");

  const [password, setPassword] =
    useState("");

  const [confirmPassword,
    setConfirmPassword] =
    useState("");

  const [loading, setLoading] =
    useState(false);

  const handleSubmit =
    async (e) => {

      e.preventDefault();

      if (!token) {

        toast.error(
          "Invalid reset link"
        );

        return;

      }

      if (
        password !==
        confirmPassword
      ) {

        toast.error(
          "Passwords do not match"
        );

        return;

      }

      try {

        setLoading(true);

        await authAPI.resetPassword(
          token,
          password
        );

        toast.success(
          "Password reset successful"
        );

        navigate("/login");

      } catch (err) {

        toast.error(
          err?.message ||
          "Failed to reset password"
        );

      } finally {

        setLoading(false);

      }

    };

  return (

    <div className="min-h-screen flex items-center justify-center bg-gray-100">

      <div className="bg-white p-8 rounded-xl shadow-md w-full max-w-md">

        <h1 className="text-2xl font-bold mb-6 text-center">
          Reset Password
        </h1>

        <form onSubmit={handleSubmit}>

          <input
            type="password"
            placeholder="New Password"
            value={password}
            onChange={(e) =>
              setPassword(
                e.target.value
              )
            }
            className="w-full border p-3 rounded-lg mb-4"
            required
          />

          <input
            type="password"
            placeholder="Confirm Password"
            value={confirmPassword}
            onChange={(e) =>
              setConfirmPassword(
                e.target.value
              )
            }
            className="w-full border p-3 rounded-lg mb-4"
            required
          />

          <button
            type="submit"
            disabled={loading}
            className="
            w-full
            bg-pink-600
            text-white
            py-3
            rounded-lg
            "
          >
            {
              loading
                ? "Resetting..."
                : "Reset Password"
            }
          </button>

        </form>

      </div>

    </div>

  );

};

export default ResetPassword;