import { Navigate } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";

const AdminRoute = ({ children }) => {
  const { user, token } = useAuth();
  
  // Check if user has admin token (simpler check)
  const hasAdminToken = token && token.length > 0;
  
  console.log("AdminRoute: Checking admin access");
  console.log("AdminRoute: user:", user);
  console.log("AdminRoute: has token:", hasAdminToken);
  
  return hasAdminToken ? children : <Navigate to="/login" />;
};

export default AdminRoute;