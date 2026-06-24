import { Heart, Search, Menu, X, User, MessageSquare, Settings, Star, LogOut, Home, Moon, Sun, Crown, Globe } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { useAuth } from "@/hooks/useAuth";
import { useLanguage } from "@/context/LanguageContext.jsx";
import logo from "@/assets/logo.png";
import { Bell } from "lucide-react";
import { useRef } from "react";

import toast from "react-hot-toast";
import {
  connectNotifications,
  disconnectNotifications
} from "@/utils/notificationSocket";
import { notificationAPI } from "@/services/api";
const Navbar = () => {
    const formatTimeAgo = (date) => {

      const seconds =
        Math.floor(
          (new Date() - new Date(date))
          / 1000
        );

      const minutes =
        Math.floor(seconds / 60);

      const hours =
        Math.floor(minutes / 60);

      const days =
        Math.floor(hours / 24);

      if (days > 0)
        return `${days}d ago`;

      if (hours > 0)
        return `${hours}h ago`;

      if (minutes > 0)
        return `${minutes}m ago`;

      return "Just now";
    };
const notificationRef = useRef(null);
const notificationAudio = useRef(
  new Audio("/microsammy-ak-47-firing-8760.mp3")
);
const user =
  JSON.parse(
    localStorage.getItem("user")
  );

const currentUserId =
  user?.profile?.userId;
  const [mobileOpen, setMobileOpen] = useState(false);
  const [darkMode, setDarkMode] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const [notifications, setNotifications] =
  useState([]);

  const [unreadCount, setUnreadCount] =
  useState(0);

  const [showNotifications,
  setShowNotifications] =
  useState(false);
useEffect(() => {

  const loadNotifications =
    async () => {

      try {

        const user =
          JSON.parse(
            localStorage.getItem("user")
          );

        if (!user?.profile?.userId)
          return;

        const list =
          await notificationAPI.getAll(
            user.profile.userId
          );

        const count =
          await notificationAPI.unreadCount(
            user.profile.userId
          );

        setNotifications(list);

        setUnreadCount(count);

      } catch(err) {

        console.log(err);

      }

    };

  loadNotifications();

}, []);
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
    { label: "Shortlists", to: "/shortlists", icon: <Star className="h-4 w-4" /> },
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

  const handleLogout = async () => {

    try {

      await fetch(
        "http://localhost:9090/api/chat/offline",
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`
          }
        }
      );

    } catch (err) {
      console.log(err);
    }

    logout();
    navigate("/");
  };

  const cycleLanguage = () => {
    const languages = ['en', 'hi', 'mr'];
    const currentIndex = languages.indexOf(language);
    const nextIndex = (currentIndex + 1) % languages.length;
    setLanguage(languages[nextIndex]);
  };
useEffect(() => {

  const handleClickOutside = (event) => {

    if (
      notificationRef.current &&
      !notificationRef.current.contains(event.target)
    ) {
      setShowNotifications(false);
    }

  };

  document.addEventListener(
    "mousedown",
    handleClickOutside
  );

  return () => {

    document.removeEventListener(
      "mousedown",
      handleClickOutside
    );

  };

}, []);

useEffect(() => {

  console.log(
    "WEBSOCKET EFFECT RUNNING"
  );

  const user =
    JSON.parse(
      localStorage.getItem("user")
    );

  if (
    !user?.profile?.userId
  ) {
    return;
  }

  console.log(
    "WS USER ID =",
    user.profile.userId
  );

  connectNotifications(
    user.profile.userId,
    async (data) => {

      console.log(
        "LIVE NOTIFICATION:",
        data
      );
  const activeChat =
      localStorage.getItem("activeChatUserId");

  if (
      data.type === "MESSAGE" &&
      String(activeChat) === String(data.senderId)
  ) {
      return;
  }
if (data.receiverId !== user.profile.userId) {
    return;
}
   if (notificationAudio.current) {

     notificationAudio.current.currentTime = 0;

     notificationAudio.current
       .play()
       .then(() => {

         console.log(
           "SOUND PLAYED"
         );

       })
       .catch((err) => {

         console.log(
           "AUDIO BLOCKED",
           err
         );

       });

   }

      if (

        "Notification" in window &&

        Notification.permission === "granted"

      ) {

 new Notification(

     data.type === "MESSAGE"
         ? `💬 ${data.senderName}`
         : "Gathbandhan",

     {

         body: data.message,

         icon: "/logo.png"

     }

 );

      }

     if (data.type === "MESSAGE") {

         toast.success(`💬 ${data.senderName}`, {

             description: data.message,

             duration: 5000,

             position: "top-right"

         });

     } else {

         toast.success(

             data.message || "New Notification",

             {

                 duration: 5000,

                 position: "top-right"

             }

         );

     }
      const list =
        await notificationAPI.getAll(
          user.profile.userId
        );

      const count =
        await notificationAPI.unreadCount(
          user.profile.userId
        );

      setNotifications(list);

      setUnreadCount(count);

      window.dispatchEvent(
        new Event("interestUpdated")
      );

      window.dispatchEvent(
        new Event("shortlist:updated")
      );

      window.dispatchEvent(
        new Event("like:updated")
      );
window.dispatchEvent(
    new Event("dashboardUpdated")
);
      console.log(
        "DASHBOARD REFRESH EVENT FIRED"
      );
    }
  );

  return () => {
    console.log(
      "DISCONNECTING WS"
    );

    disconnectNotifications();
  };

}, []);useEffect(() => {

  if (
    "Notification" in window &&
    Notification.permission !== "granted"
  ) {

    Notification.requestPermission();

  }

}, []);

useEffect(() => {

  if (
    "Notification" in window &&
    Notification.permission === "default"
  ) {

    Notification.requestPermission();

  }

}, []);

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

       <div className="flex items-center gap-5">
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
<div
className="relative"
ref={notificationRef}
>

        <button
     onClick={() => {
       setShowNotifications(prev => !prev);
     }}
         className="
         relative
         p-2
         rounded-lg
         text-foreground
         hover:bg-slate-200
         hover:text-primary
         transition-all
         duration-200
         "

        >

          <Bell className="h-5 w-5" />

          {unreadCount > 0 && (

            <span
              className="
              absolute
              -top-1
              -right-1
              bg-red-500
              text-white
              text-xs
              rounded-full
              w-5
              h-5
              flex
              items-center
              justify-center
              "
            >
             {unreadCount > 99 ? "99+" : unreadCount}
            </span>

          )}

        </button>
          {
          showNotifications && (

          <div


      className="
    fixed
    top-20
    right-2
    left-2
    md:left-auto
    md:right-5
    w-[95vw] max-w-md md:w-96
      max-h-[500px]
      overflow-y-auto
      bg-slate-900
      rounded-xl
      shadow-2xl
      border
      border-slate-700
      z-[99999]
      "
      >


         <div
         className="
         flex
         justify-between
         items-center
         p-4
         border-b
         border-slate-700
         bg-gradient-to-r
         from-slate-800
         to-slate-900
         "
         >

           <h3
             className="
             font-bold
             text-white
             "
           >
             Notifications
           </h3>

           <div className="flex items-center gap-3">

             <button
               onClick={async () => {

                 if (!currentUserId) return;

                 try {

                   await notificationAPI.markAllRead(
                     currentUserId
                   );

                   setNotifications(
                     prev =>
                       prev.map(
                         n => ({
                           ...n,
                           read: true
                         })
                       )
                   );

                   setUnreadCount(0);

                   toast.success(
                     "All notifications marked as read"
                   );

                 } catch {

                   toast.error(
                     "Failed"
                   );

                 }

               }}
               className="
               text-xs
               text-pink-400
               hover:text-pink-300
               "
             >
               Mark All Read
             </button>

             <button
               onClick={() =>
                 setShowNotifications(false)
               }
               className="
               text-white
               hover:text-red-400
               text-lg
               "
             >
               ✕
             </button>

           </div>

        </div>

        {notifications.length === 0 ? (

          <div
            className="
            p-6
            text-center
            text-slate-400
            "
          >
            No notifications yet
          </div>

        ) : (

          notifications.map(
(item) => (

<div
key={item.id}
onClick={async () => {


  try {

    await notificationAPI.markRead(
      item.id
    );

    setNotifications(prev =>
      prev.map(n =>
        n.id === item.id
          ? { ...n, read: true }
          : n
      )
    );

 const user =
 JSON.parse(
   localStorage.getItem("user")
 );

 const count =
 await notificationAPI.unreadCount(
   user.profile.userId
 );

setUnreadCount(count);

if(item.type === "REQUEST"){

  navigate("/received-interests");

}

else if(item.type === "ACCEPT"){

  navigate("/sent-interests");

}

else if(item.type === "SHORTLIST"){

  navigate("/shortlists");

}

else if(item.type === "VIEW"){

  navigate("/profile-visitors");

}
else if(item.type === "LIKE"){

  navigate("/likes");

}

else if(item.type === "REJECT"){

  navigate("/sent-interests");

}
else if(item.type === "MESSAGE"){

  navigate("/messages");

}

else if(item.type === "MATCH"){

  navigate("/matches");

}

} catch(err) {

    console.log(err);

  }

}}

className={`
mx-2
my-2
p-4
rounded-lg
cursor-pointer
transition-all
duration-200
hover:scale-[1.01]

${item.read
? "bg-slate-950 opacity-60"
: "bg-slate-800 border-l-4 border-pink-500"}

hover:bg-slate-700
`}
>
<div className="flex justify-between items-start gap-3">

  <div className="flex gap-3 flex-1">

    <div className="text-xl">
      {item.type === "MATCH" && "🎉"}
      {item.type === "REQUEST" && "💌"}
      {item.type === "ACCEPT" && "✅"}
      {item.type === "REJECT" && "❌"}
      {item.type === "SHORTLIST" && "⭐"}
      {item.type === "LIKE" && "❤️"}
      {item.type === "VIEW" && "👀"}
      {item.type === "MESSAGE" && "💬"}
    </div>

    <div>

      <p
        className={`
        text-sm
        font-medium
        leading-relaxed
        ${
          item.read
            ? "text-slate-400"
            : "text-white"
        }
        `}
      >
        {item.message}
      </p>

      <p
        className="
        text-xs
        text-slate-500
        mt-1
        "
      >
        {formatTimeAgo(item.createdAt)}
      </p>

    </div>

  </div>

  <button

    onClick={async (e) => {

      e.stopPropagation();

      try {

        await notificationAPI.delete(
          item.id
        );

        setNotifications(
          prev =>
            prev.filter(
              n => n.id !== item.id
            )
        );

        if (!item.read) {

          setUnreadCount(
            prev =>
              Math.max(
                0,
                prev - 1
              )
          );

        }

        toast.success(
          "Notification deleted"
        );

      } catch (err) {

        console.log(err);

        toast.error(
          "Delete failed"
        );

      }

    }}

    className="
    text-slate-400
    hover:text-red-500
    transition
    px-2
    "

    title="Delete Notification"

  >

    ✕

  </button>

</div>
          </div>
          )

        )

      )}

      </div>

          )

          }

          </div>
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
