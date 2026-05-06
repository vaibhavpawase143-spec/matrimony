import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext({
  token: null,
  user: null,
  role: null,
  login: () => {},
  logout: () => {},
  isAuthenticated: () => false,
});

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);
  const [role, setRole] = useState(null);

  // Check for existing auth on mount
  useEffect(() => {
    const savedToken = localStorage.getItem("token");
    const savedRole = localStorage.getItem("role");
    const savedUser = localStorage.getItem("user");
    
    if (savedToken) {
      setToken(savedToken);
    }
    if (savedRole) {
      setRole(savedRole);
    }
    if (savedUser) {
      try {
        setUser(JSON.parse(savedUser));
      } catch (e) {
        console.error("Error parsing saved user:", e);
        localStorage.removeItem("user");
      }
    }
  }, []);

  const login = (userData, userToken = "demo-token", userRole = userData?.role) => {
    setToken(userToken);
    setUser(userData);
    setRole(userRole);
    
    if (userToken) {
      localStorage.setItem("token", userToken);
    }
    if (userRole) {
      localStorage.setItem("role", userRole);
    }
    if (userData) {
      localStorage.setItem("user", JSON.stringify(userData));
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    setRole(null);
    
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("user");
  };

  const isAuthenticated = () => {
    return !!token;
  };

  return (
    <AuthContext.Provider value={{ token, user, role, login, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
