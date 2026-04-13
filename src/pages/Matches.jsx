import { Heart, MessageSquare, X } from "lucide-react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import { useLanguage } from "@/context/LanguageContext";
import profile1 from "@/assets/profile1.jpg";
import profile2 from "@/assets/profile2.jpg";
import profile3 from "@/assets/profile3.jpg";
import profile4 from "@/assets/profile4.jpg";

const matches = [
  { id: 1, name: "Priya Sharma", age: 25, city: "Pune", profession: "Software Engineer", compatibility: 92, image: profile1 },
  { id: 2, name: "Sneha Patel", age: 24, city: "Mumbai", profession: "Designer", compatibility: 87, image: profile2 },
  { id: 3, name: "Aarushi Gupta", age: 26, city: "Delhi", profession: "Doctor", compatibility: 85, image: profile3 },
  { id: 4, name: "Neha Verma", age: 23, city: "Bangalore", profession: "Teacher", compatibility: 80, image: profile4 },
  { id: 5, name: "Riya Singh", age: 27, city: "Jaipur", profession: "CA", compatibility: 78, image: profile1 },
  { id: 6, name: "Kavya Joshi", age: 25, city: "Ahmedabad", profession: "Manager", compatibility: 75, image: profile2 },
];

const Matches = () => {
  const { t } = useLanguage();

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      <div className="py-8 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <Heart className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">{t.matches.title}</h1>
        <p className="text-primary-foreground/70 text-sm">{t.matches.subtitle}</p>
      </div>

    <div className="container mx-auto px-4 py-8">
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {matches.map((m, i) => (
          <motion.div
            key={m.id}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.1 }}
            className="bg-card rounded-xl border border-border overflow-hidden hover:shadow-lg transition-shadow group"
          >
            <div className="aspect-[4/3] overflow-hidden relative">
              <img src={m.image} alt={m.name} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" />
              <div className="absolute top-3 right-3 bg-emerald-badge text-primary-foreground text-xs font-bold px-2.5 py-1 rounded-full">
                {m.compatibility}% {t.matches.compatibilityLabel}
              </div>
            </div>
            <div className="p-4">
              <h3 className="text-base font-semibold text-foreground">{m.name}, <span className="text-primary">{m.age}</span></h3>
              <p className="text-xs text-muted-foreground mt-0.5">{m.profession} · {m.city}</p>

              <div className="flex gap-2 mt-4">
                <button className="flex-1 flex items-center justify-center gap-1.5 bg-primary hover:bg-primary/90 text-primary-foreground text-xs font-semibold py-2 rounded-lg transition-colors">
                  <Heart className="h-3.5 w-3.5" /> {t.matches.connect}
                </button>
                <button className="flex-1 flex items-center justify-center gap-1.5 bg-accent/10 text-accent text-xs font-semibold py-2 rounded-lg hover:bg-accent/20 transition-colors">
                  <MessageSquare className="h-3.5 w-3.5" /> {t.matches.message}
                </button>
                <button className="h-9 w-9 flex items-center justify-center rounded-lg border border-border text-muted-foreground hover:text-destructive hover:border-destructive transition-colors">
                  <X className="h-4 w-4" />
                </button>
              </div>
            </div>
          </motion.div>
        ))}
      </div>
    </div>
  </div>
  );
};

export default Matches;
