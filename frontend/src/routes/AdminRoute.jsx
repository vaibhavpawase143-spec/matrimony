import {
  Navigate
} from "react-router-dom";

import {
  useEffect,
  useState
} from "react";

import {
  useAuth
} from "@/hooks/useAuth";

const AdminRoute = ({
  children
}) => {

  const {
    user,
    token,
    loading
  } = useAuth();

  const [
    hasAdminToken,
    setHasAdminToken
  ] = useState(false);

  const [
    checkingAuth,
    setCheckingAuth
  ] = useState(true);

  useEffect(() => {

    console.log(
      "🔐 AdminRoute: Checking authentication..."
    );

    const savedToken =
      localStorage.getItem("token");

    console.log(
      "🔐 AdminRoute: token from localStorage:",
      savedToken
    );

    if (
      savedToken &&
      savedToken.length > 0
    ) {

      setHasAdminToken(true);

    } else {

      setHasAdminToken(false);

    }

    setCheckingAuth(false);

  }, [token]);

  console.log(
    "🔐 AdminRoute Render:"
  );

  console.log(
    "  - user:",
    user
  );

  console.log(
    "  - token:",
    token
  );

  console.log(
    "  - hasAdminToken:",
    hasAdminToken
  );

  console.log(
    "  - loading:",
    loading
  );

  console.log(
    "  - checkingAuth:",
    checkingAuth
  );

  // Wait until auth loads
  if (
    loading ||
    checkingAuth
  ) {

    console.log(
      "⏳ AdminRoute: Waiting for auth..."
    );

    return null;

  }

  // Redirect only after checks complete
  if (!hasAdminToken) {

    console.log(
      "❌ AdminRoute: No token found, redirecting to login"
    );

    return (
      <Navigate
        to="/login"
        replace
      />
    );

  }

  console.log(
    "✅ AdminRoute: Access granted"
  );

  return children;

};

export default AdminRoute;