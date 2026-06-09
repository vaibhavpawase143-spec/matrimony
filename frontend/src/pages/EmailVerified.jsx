import { Link } from "react-router-dom";
import { CheckCircle } from "lucide-react";

const EmailVerified = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-muted/30">

      <div className="bg-white rounded-2xl shadow-xl p-8 text-center max-w-md">

        <CheckCircle
          className="mx-auto text-green-600 mb-4"
          size={70}
        />

        <h1 className="text-3xl font-bold mb-3">
          Email Verified
        </h1>

        <p className="text-gray-600 mb-6">
          Your email has been verified successfully.
          You can now login to Gathbandhan.
        </p>

        <Link
          to="/login"
          className="
          inline-block
          bg-green-600
          hover:bg-green-700
          text-white
          px-6
          py-3
          rounded-lg
          "
        >
          Go To Login
        </Link>

      </div>

    </div>
  );
};

export default EmailVerified;