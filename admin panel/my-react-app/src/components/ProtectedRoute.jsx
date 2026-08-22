import { Navigate } from "react-router-dom";
import { ADMIN_PERMISSIONS } from "../config/adminPermissions";
import { getAdmin } from "../services/authService";

const ProtectedRoute = ({ module, children }) => {
  const admin = getAdmin();

  if (!admin) {
    return <Navigate to="/" replace />;
  }

  const role = admin.role;

  const allowed =
    ADMIN_PERMISSIONS[role] &&
    ADMIN_PERMISSIONS[role][module];

  if (!allowed) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
};

export default ProtectedRoute;