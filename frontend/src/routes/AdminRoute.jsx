import { Navigate } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";
import { toast } from "sonner";
import { useEffect } from "react";

// ✅ Role constants for type safety
const ADMIN_ROLE = "ADMIN";

const AdminRoute = ({ children }) => {
  const { token, role, user } = useAuth();

  // ✅ STEP 1: Check authentication (token exists)
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // ✅ STEP 2: Check authorization (role must be exactly ADMIN)
  // ❌ BEFORE: Did not check role at all
  // ✅ AFTER: Verify user is ADMIN role
  if (role !== ADMIN_ROLE) {
    // Log security violation
    console.error(
      `[SECURITY] Unauthorized admin access attempt by user ${user?.id} with role ${role}`
    );

    // Show error message
    toast.error("Access denied. Admin privileges required.");

    // Redirect to home
    return <Navigate to="/home" replace />;
  }

  // ✅ STEP 3: Double-check account type (defense in depth)
  if (user?.accountType !== "ADMIN") {
    console.error(
      `[SECURITY] Invalid account type ${user?.accountType} for admin route`
    );
    toast.error("Access denied.");
    return <Navigate to="/home" replace />;
  }

  // ✅ All security checks passed - render admin component
  return children;
};

export default AdminRoute;