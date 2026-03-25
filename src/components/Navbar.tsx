import { Heart, Search, Menu, X, User, MessageSquare, Settings, Star, LogOut, Home } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "@/hooks/useAuth";

const publicLinks = [
  { label: "Home", to: "/" },
  { label: "About", to: "/about" },
  { label: "Contact", to: "/contact" },
  { label: "Login", to: "/login" },
  { label: "Register", to: "/register" },
];

const privateLinks = [
  { label: "Home", to: "/home", icon: <Home className="h-4 w-4" /> },
  { label: "Search", to: "/search", icon: <Search className="h-4 w-4" /> },
  { label: "Matches", to: "/matches", icon: <Heart className="h-4 w-4" /> },
  { label: "Kundli", to: "/kundli", icon: <Star className="h-4 w-4" /> },
  { label: "Messages", to: "/messages", icon: <MessageSquare className="h-4 w-4" /> },
  { label: "Settings", to: "/settings", icon: <Settings className="h-4 w-4" /> },
];

const Navbar = () => {
  const [mobileOpen, setMobileOpen] = useState(false);
  const { isLoggedIn, userName, logout } = useAuth();
  const navigate = useNavigate();
  const links = isLoggedIn ? privateLinks : publicLinks;

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav className="sticky top-0 z-50 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 border-b border-border">
      <div className="container mx-auto px-4 flex items-center justify-between h-16">
        <Link to={isLoggedIn ? "/home" : "/"} className="flex items-center gap-2">
          <Heart className="h-7 w-7 text-primary fill-primary" />
          <span className="text-xl font-display font-bold text-foreground">Gathbandhan</span>
        </Link>

        <div className="hidden md:flex items-center gap-5">
          {links.map((l) => (
            <Link key={l.label} to={l.to} className="text-sm font-medium text-foreground hover:text-primary transition-colors flex items-center gap-1.5">
              {"icon" in l && (l as { icon: React.ReactNode }).icon}
              {l.label}
            </Link>
          ))}
          {isLoggedIn && (
            <>
              <button onClick={handleLogout} className="text-sm font-medium text-muted-foreground hover:text-destructive transition-colors flex items-center gap-1.5">
                <LogOut className="h-4 w-4" />
                Logout
              </button>
              <div className="h-8 w-8 rounded-full bg-primary/20 flex items-center justify-center text-primary font-bold text-sm">
                {userName.charAt(0).toUpperCase()}
              </div>
            </>
          )}
        </div>

        <button className="md:hidden text-foreground" onClick={() => setMobileOpen(!mobileOpen)}>
          {mobileOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
        </button>
      </div>

      {mobileOpen && (
        <div className="md:hidden bg-background border-t border-border px-4 py-3 space-y-2">
          {links.map((l) => (
            <Link key={l.label} to={l.to} onClick={() => setMobileOpen(false)} className="block text-sm font-medium text-foreground hover:text-primary py-2">
              {l.label}
            </Link>
          ))}
          {isLoggedIn && (
            <button onClick={() => { handleLogout(); setMobileOpen(false); }} className="block text-sm font-medium text-destructive py-2">
              Logout
            </button>
          )}
        </div>
      )}
    </nav>
  );
};

export default Navbar;
