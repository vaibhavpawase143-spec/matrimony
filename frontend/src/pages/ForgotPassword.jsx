import { useState } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import { authAPI } from "@/services/api";

const ForgotPassword = () => {

  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {

    e.preventDefault();

    try {

      setLoading(true);

      await authAPI.forgotPassword(email);

      toast.success(
        "Password reset link sent to your email"
      );

    } catch (err) {

      toast.error(
        err?.message ||
        "Failed to send reset link"
      );

    } finally {

      setLoading(false);

    }

  };

  return (

    <div className="min-h-screen flex items-center justify-center bg-gray-100">

      <div className="bg-white p-8 rounded-xl shadow-md w-full max-w-md">

        <h1 className="text-2xl font-bold mb-6 text-center">
          Forgot Password
        </h1>

        <p className="text-center text-gray-500 mb-4">
          Enter your registered email address
        </p>

        <form onSubmit={handleSubmit}>

          <input
            type="email"
            placeholder="Enter your email"
            value={email}
            onChange={(e) =>
              setEmail(e.target.value)
            }
            className="w-full border p-3 rounded-lg mb-4"
            required
          />

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-pink-600 text-white py-3 rounded-lg"
          >
            {
              loading
                ? "Sending..."
                : "Send Reset Link"
            }
          </button>

        </form>

        <Link
          to="/login"
          className="block mt-4 text-center text-pink-600"
        >
          Back To Login
        </Link>

      </div>

    </div>

  );

};

export default ForgotPassword;