import { Navigate } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";

const AdminRoute = ({ children }) => {
  const { user, role } = useAuth();
  
  // Check if user is admin
  const isAdmin = role === "ADMIN" || user?.role === "ADMIN";
  
  console.log("AdminRoute: Checking admin access");
  console.log("AdminRoute: user:", user);
  console.log("AdminRoute: role:", role);
  console.log("AdminRoute: isAdmin:", isAdmin);
  
  return isAdmin ? children : <Navigate to="/login" />;
};

export default AdminRoute;