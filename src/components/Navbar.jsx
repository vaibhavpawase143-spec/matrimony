import { Heart, Search, Menu, X, User, MessageSquare, Settings, Star, LogOut, Home, Moon, Sun, Crown, Globe } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { useAuth } from "@/hooks/useAuth";
import { useLanguage } from "@/context/LanguageContext.jsx";
import logo from "@/assets/logo.png";

const Navbar = () => {
  const [mobileOpen, setMobileOpen] = useState(false);
  const [darkMode, setDarkMode] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const { isLoggedIn, userName, logout } = useAuth();
  const navigate = useNavigate();
  const { language, setLanguage, t } = useLanguage();

  const publicLinks = [
    { label: t.navbar.home, to: "/" },
    { label: t.navbar.about, to: "/about" },
    { label: t.navbar.contact, to: "/contact" },
    { label: t.navbar.login, to: "/login" },
    { label: t.navbar.register, to: "/register" },
  ];

  const privateLinks = [
    { label: t.navbar.home, to: "/home", icon: <Home className="h-4 w-4" /> },
    { label: t.navbar.search, to: "/search", icon: <Search className="h-4 w-4" /> },
    { label: t.navbar.matches, to: "/matches", icon: <Heart className="h-4 w-4" /> },
    { label: t.navbar.kundli, to: "/kundli", icon: <Star className="h-4 w-4" /> },
    { label: t.navbar.messages, to: "/messages", icon: <MessageSquare className="h-4 w-4" /> },
    { label: t.navbar.settings, to: "/settings", icon: <Settings className="h-4 w-4" /> },
  ];

  const links = isLoggedIn ? privateLinks : publicLinks;

  useEffect(() => {
    // Load theme from localStorage
    const savedTheme = localStorage.getItem('darkMode');
    if (savedTheme === 'true') {
      setDarkMode(true);
      document.documentElement.classList.add('dark');
    }

    // Handle scroll effect
    const handleScroll = () => {
      setScrolled(window.scrollY > 10);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const toggleDarkMode = () => {
    const newDarkMode = !darkMode;
    setDarkMode(newDarkMode);
    
    if (newDarkMode) {
      document.documentElement.classList.add('dark');
      localStorage.setItem('darkMode', 'true');
    } else {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('darkMode', 'false');
    }
  };

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const cycleLanguage = () => {
    const languages = ['en', 'hi', 'mr'];
    const currentIndex = languages.indexOf(language);
    const nextIndex = (currentIndex + 1) % languages.length;
    setLanguage(languages[nextIndex]);
  };

  return (
    <motion.nav 
      initial={{ opacity: 0, y: -20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5, ease: "easeOut" }}
      className={`sticky top-0 z-50 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 border-border transition-all duration-300 ${scrolled ? 'shadow-md' : ''}`} 
      style={{ background: "linear-gradient(135deg, #d4bfe9, #e5bdee, rgb(217, 193, 241))" }}
    >
      {/* Floating hearts */}
      <div className="container mx-auto px-4 flex items-center justify-between h-16">
        <Link to={isLoggedIn ? "/home" : "/"} className="flex items-center gap-2">
          <img 
            src={logo} 
            alt="Gathbandhan Logo" 
           className="h-22 w-60 object-contain"
          />
        </Link>

        <div className="hidden md:flex items-center gap-5">
          {links.map((l) => (
            <motion.div key={l.label} whileHover={{ scale: 1.05 }} transition={{ duration: 0.2 }}>
              <Link 
                to={l.to} 
                className={`text-sm font-medium text-foreground hover:text-primary transition-all duration-300 ease-in-out flex items-center gap-1.5 relative group ${
                  window.location.pathname === l.to ? 'text-primary' : ''
                }`}
              >
                {"icon" in l && l.icon}
                {l.label}
                <span className={`absolute bottom-0 left-0 w-full h-0.5 bg-primary transform scale-x-0 transition-transform duration-300 ease-in-out ${
                  window.location.pathname === l.to ? 'scale-x-100' : 'group-hover:scale-x-100'
                }`} />
              </Link>
            </motion.div>
          ))}
          <button 
            onClick={cycleLanguage}
            className="text-foreground hover:text-primary transition-all duration-300 ease-in-out p-2 rounded-lg hover:bg-muted/50 hover:scale-105 hover:shadow-md"
            title={t.navbar?.languageLabel || "Language"}
          >
            <Globe className="h-4 w-4" />
          </button>
          <button 
            onClick={toggleDarkMode}
            className="text-foreground hover:text-primary transition-all duration-300 ease-in-out p-2 rounded-lg hover:bg-muted/50 hover:scale-105 hover:shadow-md"
          >
            {darkMode ? <Sun className="h-4 w-4" /> : <Moon className="h-4 w-4" />}
          </button>
          {isLoggedIn && (
            <>
              <motion.button 
                onClick={() => navigate("/upgrade")}
                className="text-sm font-medium bg-primary hover:bg-primary/90 text-primary-foreground px-4 py-2 rounded-lg transition-all duration-300 ease-in-out flex items-center gap-2 hover:scale-105"
                whileHover={{ scale: 1.05 }}
                transition={{ duration: 0.2 }}
              >
                <Crown className="h-4 w-4" />
                {t.navbar.upgrade}
              </motion.button>
              <motion.button 
                onClick={handleLogout} 
                className="text-sm font-medium text-muted-foreground hover:text-destructive transition-all duration-300 ease-in-out flex items-center gap-1.5 hover:scale-105 hover:shadow-md"
                whileHover={{ scale: 1.05 }}
                transition={{ duration: 0.2 }}
              >
                <LogOut className="h-4 w-4" />
                {t.navbar.logout}
              </motion.button>
              <motion.button 
                onClick={() => navigate("/account")} 
                className="h-8 w-8 rounded-full bg-primary/20 hover:bg-primary/30 flex items-center justify-center text-primary font-bold text-sm cursor-pointer transition-all duration-300"
                whileHover={{ scale: 1.1 }}
                transition={{ duration: 0.2 }}
                title={t.navbar.account}
              >
                {userName.charAt(0).toUpperCase()}
              </motion.button>
            </>
          )}
        </div>

        <motion.button 
          className="md:hidden text-foreground transition-all duration-300 ease-in-out hover:scale-110" 
          onClick={() => setMobileOpen(!mobileOpen)}
          whileHover={{ scale: 1.1 }}
          transition={{ duration: 0.2 }}
        >
          {mobileOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
        </motion.button>
      </div>

      {mobileOpen && (
        <motion.div 
          initial={{ opacity: 0, height: 0 }}
          animate={{ opacity: 1, height: "auto" }}
          exit={{ opacity: 0, height: 0 }}
          transition={{ duration: 0.3, ease: "easeInOut" }}
          className="md:hidden bg-background border-t border-border px-4 py-3 space-y-2 overflow-hidden"
        >
          {links.map((l) => (
            <motion.div key={l.label} whileHover={{ x: 5 }} transition={{ duration: 0.2 }}>
              <Link 
                key={l.label} 
                to={l.to} 
                onClick={() => setMobileOpen(false)} 
                className="block text-sm font-medium text-foreground hover:text-primary py-2 transition-all duration-300 ease-in-out"
              >
                {l.label}
              </Link>
            </motion.div>
          ))}
          {isLoggedIn && (
            <>
              <motion.button 
                onClick={() => { navigate("/upgrade"); setMobileOpen(false); }}
                className="block text-sm font-medium bg-primary hover:bg-primary/90 text-primary-foreground px-4 py-2 rounded-lg transition-all duration-300 ease-in-out hover:scale-105"
                whileHover={{ x: 5 }}
                transition={{ duration: 0.2 }}
              >
                <Crown className="h-4 w-4 inline mr-2" />
                {t.navbar.upgrade}
              </motion.button>
              <motion.button 
                onClick={() => { handleLogout(); setMobileOpen(false); }} 
                className="block text-sm font-medium text-destructive py-2 transition-all duration-300 ease-in-out hover:scale-105"
                whileHover={{ x: 5 }}
                transition={{ duration: 0.2 }}
              >
                {t.navbar.logout}
              </motion.button>
            </>
          )}
          <div className="border-t border-border pt-3 flex justify-center">
            <button 
              onClick={cycleLanguage}
              className="text-foreground hover:text-primary transition-all duration-300 ease-in-out p-2 rounded-lg hover:bg-muted/50 hover:scale-105 hover:shadow-md"
              title={t.navbar?.languageLabel || "Language"}
            >
              <Globe className="h-4 w-4" />
            </button>
          </div>
        </motion.div>
      )}
    </motion.nav>
  );
};

export default Navbar;
