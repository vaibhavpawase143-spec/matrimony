import { Navigate } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";

const AdminRoute = ({ children }) => {
  const { token, role, user } = useAuth();

  console.log("========== ADMIN ROUTE ==========");
  console.log("Token :", token);
  console.log("Role :", role);
  console.log("User :", user);
  console.log("================================");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default AdminRoute;