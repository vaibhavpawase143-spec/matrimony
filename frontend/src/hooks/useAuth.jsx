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

  // Check for existing auth on mount (from tab-isolated sessionStorage)
  useEffect(() => {
    const savedToken = sessionStorage.getItem("token") || localStorage.getItem("token");
    const savedRole = sessionStorage.getItem("role") || localStorage.getItem("role");
    const savedUser = sessionStorage.getItem("user") || localStorage.getItem("user");
    
    if (savedToken) {
      setToken(savedToken);
      sessionStorage.setItem("token", savedToken);
    }
    if (savedRole) {
      setRole(savedRole);
      sessionStorage.setItem("role", savedRole);
    }
    if (savedUser) {
      try {
        const parsed = JSON.parse(savedUser);
        setUser(parsed);
        sessionStorage.setItem("user", savedUser);
      } catch (e) {
        if (import.meta.env.DEV) {
          console.error("Error parsing saved user:", e);
        }
        sessionStorage.removeItem("user");
      }
    }
    // Clean legacy global auth keys from localStorage to prevent cross-tab collision
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("user");
  }, []);

  const login = (userData, userToken, userRole = userData?.role) => {
    if (!userToken) {
      throw new Error('Token is required for login');
    }
    
    setToken(userToken);
    setUser(userData);
    setRole(userRole);
    
    sessionStorage.setItem("token", userToken);
    if (userRole) {
      sessionStorage.setItem("role", userRole);
    }
    if (userData) {
      sessionStorage.setItem("user", JSON.stringify(userData));
    }
    // Remove global keys to prevent tab collision
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("user");
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    setRole(null);
    
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("role");
    sessionStorage.removeItem("user");
    sessionStorage.removeItem("refreshToken");
    sessionStorage.removeItem("isAdmin");

    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("user");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("isAdmin");
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
