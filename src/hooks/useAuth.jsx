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
    
    console.log('🔐 AuthProvider mount - Checking localStorage:');
    console.log('  - Token:', savedToken && typeof savedToken === 'string' ? savedToken.substring(0, Math.min(50, savedToken.length)) + '...' : 'null');
    console.log('  - Role:', savedRole);
    console.log('  - User:', savedUser);
    
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

  const login = (userData, userToken, userRole = userData?.role) => {
    console.log('🔐 useAuth login - Called with:');
    console.log('  - userData:', userData);
    console.log('  - userToken:', userToken && typeof userToken === 'string' ? userToken.substring(0, Math.min(50, userToken.length)) + '...' : 'null');
    console.log('  - userRole:', userRole);
    
    if (!userToken) {
      throw new Error('Token is required for login');
    }
    
    setToken(userToken);
    setUser(userData);
    setRole(userRole);
    
    localStorage.setItem("token", userToken);
    console.log('🔐 useAuth login - Token set in localStorage');
    
    if (userRole) {
      localStorage.setItem("role", userRole);
      console.log('🔐 useAuth login - Role set in localStorage:', userRole);
    }
    if (userData) {
      localStorage.setItem("user", JSON.stringify(userData));
      console.log('🔐 useAuth login - User data set in localStorage');
    }
  };

  const logout = () => {
    console.log('🔐 useAuth logout - Clearing auth data');
    setToken(null);
    setUser(null);
    setRole(null);
    
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("user");
    localStorage.removeItem("isAdmin");
    console.log('🔐 useAuth logout - All auth data cleared from localStorage');
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
