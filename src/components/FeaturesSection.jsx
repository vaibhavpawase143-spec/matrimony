import { Shield, Heart, Star, Users, Globe, Lock } from "lucide-react";
import { motion } from "framer-motion";
import { useLanguage } from "@/context/LanguageContext";

const iconSets = [
  <Shield className="h-6 w-6" />,
  <Heart className="h-6 w-6" />,
  <Star className="h-6 w-6" />,
  <Users className="h-6 w-6" />,
  <Globe className="h-6 w-6" />,
  <Lock className="h-6 w-6" />,
];

const FeaturesSection = () => {
  const { t } = useLanguage();
  const features = t.features.items.map((item, index) => ({
    icon: iconSets[index],
    ...item,
  }));

  return (
    <section className="py-16 bg-background">
      <div className="container mx-auto px-4">
        <h2 className="text-3xl md:text-4xl font-display font-bold text-foreground text-center mb-3">{t.features.title}</h2>
        <p className="text-muted-foreground text-center mb-12 max-w-lg mx-auto">{t.features.subtitle}</p>
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
};

export default FeaturesSection;
