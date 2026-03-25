import { Shield, Heart, Star, Users, Globe, Lock } from "lucide-react";
import { motion } from "framer-motion";

const features = [
  { icon: <Shield className="h-6 w-6" />, title: "Verified Profiles", desc: "100% verified profiles with ID proof and background checks" },
  { icon: <Heart className="h-6 w-6" />, title: "Smart Matching", desc: "AI-powered matching based on preferences, horoscope & lifestyle" },
  { icon: <Star className="h-6 w-6" />, title: "Kundli Matching", desc: "Traditional Ashtakoot gun milan for astrological compatibility" },
  { icon: <Users className="h-6 w-6" />, title: "50,000+ Members", desc: "Large community of genuine profiles from across India" },
  { icon: <Globe className="h-6 w-6" />, title: "Pan India", desc: "Profiles from 500+ cities across all states and regions" },
  { icon: <Lock className="h-6 w-6" />, title: "100% Privacy", desc: "Your data is encrypted and shared only with your consent" },
];

const FeaturesSection = () => (
  <section className="py-16 bg-background">
    <div className="container mx-auto px-4">
      <h2 className="text-3xl md:text-4xl font-display font-bold text-foreground text-center mb-3">Why Choose Gathbandhan?</h2>
      <p className="text-muted-foreground text-center mb-12 max-w-lg mx-auto">We bring families together with trust, tradition, and technology</p>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {features.map((f, i) => (
          <motion.div
            key={f.title}
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: i * 0.1 }}
            className="bg-card rounded-xl border border-border p-6 hover:shadow-lg transition-shadow group"
          >
            <div className="h-12 w-12 rounded-xl bg-primary/10 text-primary flex items-center justify-center mb-4 group-hover:bg-primary group-hover:text-primary-foreground transition-colors">
              {f.icon}
            </div>
            <h3 className="text-base font-semibold text-foreground mb-2">{f.title}</h3>
            <p className="text-sm text-muted-foreground">{f.desc}</p>
          </motion.div>
        ))}
      </div>
    </div>
  </section>
);

export default FeaturesSection;
