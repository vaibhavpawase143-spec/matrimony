import { Heart, User, Search, Settings, LogOut, ChevronDown, Bell, MessageSquare, Star } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import { useAuth } from "@/hooks/useAuth";
import profile1 from "@/assets/profile1.jpg";
import profile2 from "@/assets/profile2.jpg";
import profile3 from "@/assets/profile3.jpg";
import profile4 from "@/assets/profile4.jpg";
import success1 from "@/assets/success-couple1.jpg";
import success2 from "@/assets/success-couple2.jpg";
import success3 from "@/assets/success-couple3.jpg";

const profiles = [
  { name: "Priya", age: 25, city: "Pune", image: profile1, action: "View Profile" },
  { name: "Sneha", age: 24, city: "Mumbai", image: profile2, action: "View Profile" },
  { name: "Aarushi", age: 26, city: "Delhi", image: profile3, action: "Send Interest" },
  { name: "Neha", age: 23, city: "Bangalore", image: profile4, action: "Send Interest" },
];

const successStories = [
  { image: success1, names: "Rahul & Priya", city: "Mumbai", date: "Dec 2025" },
  { image: success2, names: "Vikram & Ananya", city: "Delhi", date: "Nov 2025" },
  { image: success3, names: "Arjun & Meera", city: "Pune", date: "Oct 2025" },
];

const Home = () => {
  const navigate = useNavigate();
  const { userName, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <div className="min-h-screen bg-muted/30 flex">
      {/* Sidebar */}
      <aside className="hidden md:flex flex-col w-64 bg-card border-r border-border min-h-screen sticky top-0">
        <div className="p-5 border-b border-border">
          <Link to="/home" className="flex items-center gap-2">
            <Heart className="h-6 w-6 text-primary fill-primary" />
            <span className="text-xl font-display font-bold text-foreground">Gathbandhan</span>
          </Link>
        </div>
        <nav className="flex-1 p-4 space-y-1">
          {[
            { icon: <User className="h-4 w-4" />, label: "Dashboard", active: true, to: "/home" },
            { icon: <Heart className="h-4 w-4" />, label: "Matches", to: "/matches" },
            { icon: <Search className="h-4 w-4" />, label: "Search", to: "/search" },
            { icon: <MessageSquare className="h-4 w-4" />, label: "Messages", to: "/messages" },
            { icon: <Star className="h-4 w-4" />, label: "Kundli", to: "/kundli" },
            { icon: <Settings className="h-4 w-4" />, label: "Settings", to: "/settings" },
          ].map((item) => (
            <Link
              key={item.label}
              to={item.to}
              className={`w-full flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm font-medium transition-colors ${
                item.active
                  ? "bg-primary text-primary-foreground"
                  : "text-muted-foreground hover:bg-muted hover:text-foreground"
              }`}
            >
              {item.icon}
              {item.label}
            </Link>
          ))}
        </nav>
        <div className="p-4 border-t border-border">
          <button onClick={handleLogout} className="w-full flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm font-medium text-muted-foreground hover:bg-muted hover:text-foreground transition-colors">
            <LogOut className="h-4 w-4" /> Logout
          </button>
        </div>
      </aside>

      {/* Main content */}
      <div className="flex-1 flex flex-col min-h-screen">
        <header className="sticky top-0 z-40 bg-card/95 backdrop-blur border-b border-border px-6 py-3 flex items-center justify-between">
          <div>
            <p className="text-muted-foreground text-sm">Welcome,</p>
            <h1 className="text-xl font-display font-bold text-foreground capitalize">{userName || "User"}!</h1>
          </div>
          <div className="flex items-center gap-4">
            <button className="relative text-muted-foreground hover:text-foreground transition-colors">
              <Bell className="h-5 w-5" />
              <span className="absolute -top-1 -right-1 h-4 w-4 bg-primary text-primary-foreground text-[10px] font-bold rounded-full flex items-center justify-center">3</span>
            </button>
            <div className="h-9 w-9 rounded-full bg-accent/20 flex items-center justify-center text-accent font-bold text-sm">
              {(userName || "U").charAt(0).toUpperCase()}
            </div>
          </div>
        </header>

        {/* Hero banner */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mx-6 mt-6 rounded-2xl p-8 text-primary-foreground relative overflow-hidden"
          style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}
        >
          <Heart className="absolute top-4 right-8 h-8 w-8 text-pink-soft/40 fill-pink-soft/30 animate-float-heart" />
          <h2 className="text-3xl md:text-4xl font-display font-bold mb-2">Find Your Perfect Match.</h2>
          <p className="text-primary-foreground/70 text-sm max-w-md">Discover compatible profiles tailored just for you</p>
        </motion.div>

        <div className="px-6 py-6 grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left column */}
          <div className="lg:col-span-2 space-y-6">
            {/* Stats */}
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div className="bg-card rounded-xl border border-border p-5">
                <h3 className="text-sm font-semibold text-foreground mb-3">Profile Completion</h3>
                <div className="relative h-2 bg-muted rounded-full overflow-hidden mb-2">
                  <div className="absolute inset-y-0 left-0 bg-accent rounded-full" style={{ width: "70%" }} />
                </div>
                <p className="text-xs text-muted-foreground">70% Complete</p>
              </div>
              <div className="bg-card rounded-xl border border-border p-5">
                <h3 className="text-sm font-semibold text-foreground mb-3">Membership</h3>
                <span className="inline-block bg-gold/20 text-gold px-3 py-1 rounded-full text-xs font-semibold">Free Member</span>
              </div>
              <div className="bg-card rounded-xl border border-border p-5">
                <h3 className="text-sm font-semibold text-foreground mb-3">Activity</h3>
                <div className="space-y-1.5 text-xs text-muted-foreground">
                  <p>Received: <span className="text-emerald-badge font-semibold">2 Accepted</span></p>
                  <p>Sent: <span className="text-accent font-semibold">5 Responded</span></p>
                </div>
              </div>
            </div>

            {/* Success Stories - vertical */}
            <div>
              <h3 className="text-lg font-display font-bold text-foreground mb-4">Success Stories</h3>
              <div className="space-y-3">
                {successStories.map((s, i) => (
                  <motion.div
                    key={i}
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: i * 0.1 }}
                    className="bg-card rounded-xl border border-border p-4 flex items-center gap-4 hover:shadow-md transition-shadow"
                  >
                    <img src={s.image} alt={s.names} className="h-16 w-16 rounded-lg object-cover flex-shrink-0" />
                    <div>
                      <p className="text-sm font-semibold text-foreground">{s.names}</p>
                      <p className="text-xs text-muted-foreground">{s.city} · {s.date}</p>
                    </div>
                    <Heart className="h-4 w-4 text-primary fill-primary ml-auto flex-shrink-0" />
                  </motion.div>
                ))}
              </div>
            </div>

            {/* Recommended Matches */}
            <div>
              <h3 className="text-lg font-display font-bold text-foreground mb-4">Recommended Matches</h3>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                {profiles.map((p, i) => (
                  <motion.div
                    key={i}
                    initial={{ opacity: 0, scale: 0.95 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ delay: i * 0.1 }}
                    className="bg-card rounded-xl overflow-hidden border border-border shadow-sm hover:shadow-md transition-shadow group"
                  >
                    <div className="aspect-[3/4] overflow-hidden">
                      <img src={p.image} alt={p.name} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" />
                    </div>
                    <div className="p-3">
                      <p className="text-sm font-semibold text-foreground">
                        <span className="text-primary">{p.name}</span>, {p.age}, {p.city}
                      </p>
                      <button className={`mt-2 w-full text-xs font-semibold py-1.5 rounded-lg transition-colors ${
                        p.action === "View Profile"
                          ? "bg-accent/10 text-accent hover:bg-accent/20"
                          : "bg-orange-cta/10 text-orange-cta hover:bg-orange-cta/20"
                      }`}>
                        {p.action}
                      </button>
                    </div>
                  </motion.div>
                ))}
              </div>
            </div>
          </div>

          {/* Right column - Search Partner */}
          <div className="space-y-6">
            <div className="bg-card rounded-xl border border-border p-5">
              <h3 className="text-sm font-semibold text-foreground mb-4">Search Your Partner</h3>
              <div className="space-y-3">
                {["Age:", "Religion:", "City:", "Education:", "Profession:"].map((label) => (
                  <div key={label} className="relative">
                    <select className="w-full appearance-none bg-background border border-border rounded-lg px-4 py-2.5 pr-10 text-xs text-muted-foreground font-body focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors">
                      <option>{label}</option>
                    </select>
                    <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-muted-foreground pointer-events-none" />
                  </div>
                ))}
                <button className="w-full flex items-center justify-center gap-2 bg-orange-cta hover:bg-orange-cta/90 text-primary-foreground font-semibold py-2.5 rounded-lg text-xs transition-colors">
                  <Search className="h-3.5 w-3.5" /> Search
                </button>
              </div>
            </div>

            {/* Quick Links */}
            <div className="bg-card rounded-xl border border-border p-5">
              <h3 className="text-sm font-semibold text-foreground mb-3">Quick Actions</h3>
              <div className="flex flex-wrap gap-2">
                <Link to="/settings" className="bg-emerald-badge/10 text-emerald-badge text-xs font-semibold px-4 py-2 rounded-lg hover:bg-emerald-badge/20 transition-colors">Complete Profile</Link>
                <Link to="/settings" className="bg-accent/10 text-accent text-xs font-semibold px-4 py-2 rounded-lg hover:bg-accent/20 transition-colors">Update Photo</Link>
                <button className="bg-orange-cta text-primary-foreground text-xs font-semibold px-4 py-2 rounded-lg hover:bg-orange-cta/90 transition-colors">Upgrade Premium</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
