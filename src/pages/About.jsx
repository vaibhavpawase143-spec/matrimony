import { Heart, Target, Eye, ShieldCheck, Users, Globe, Award } from "lucide-react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import { useLanguage } from "@/context/LanguageContext";

const About = () => {
  const { t } = useLanguage();
  const values = t.about.values;

  return (
    <div className="min-h-screen bg-background">
      <Navbar />

      <div className="py-12 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <Heart className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">{t.about.title}</h1>
        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">{t.about.subtitle}</p>
      </div>

      <div className="container mx-auto px-4 py-12 max-w-4xl">
        <motion.div initial={{ opacity: 0, y: 20 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true }} className="bg-card rounded-xl border border-border p-8 mb-8">
        <h2 className="text-2xl font-display font-bold text-foreground mb-4">{t.about.whoWeAreTitle}</h2>
        <p className="text-muted-foreground leading-relaxed">
          {t.about.whoWeAreDesc}
        </p>
      </motion.div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <motion.div initial={{ opacity: 0, x: -20 }} whileInView={{ opacity: 1, x: 0 }} viewport={{ once: true }} className="bg-card rounded-xl border border-border p-6">
          <div className="flex items-center gap-3 mb-3">
            <div className="h-10 w-10 rounded-full bg-primary/10 text-primary flex items-center justify-center"><Target className="h-5 w-5" /></div>
            <h3 className="text-lg font-display font-bold text-foreground">{t.about.missionTitle}</h3>
          </div>
          <p className="text-sm text-muted-foreground leading-relaxed">{t.about.missionDesc}</p>
        </motion.div>

        <motion.div initial={{ opacity: 0, x: 20 }} whileInView={{ opacity: 1, x: 0 }} viewport={{ once: true }} className="bg-card rounded-xl border border-border p-6">
          <div className="flex items-center gap-3 mb-3">
            <div className="h-10 w-10 rounded-full bg-accent/10 text-accent flex items-center justify-center"><Eye className="h-5 w-5" /></div>
            <h3 className="text-lg font-display font-bold text-foreground">{t.about.visionTitle}</h3>
          </div>
          <p className="text-sm text-muted-foreground leading-relaxed">{t.about.visionDesc}</p>
        </motion.div>
      </div>

      <h2 className="text-2xl font-display font-bold text-foreground text-center mb-8">{t.about.whyChooseTitle}</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
        {values.map((v, i) => (
          <motion.div key={v.title} initial={{ opacity: 0, y: 20 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true }} transition={{ delay: i * 0.1 }}
            className="bg-card rounded-xl border border-border p-5 flex items-start gap-4 hover:shadow-md transition-shadow"
          >
            <div className="h-11 w-11 rounded-xl bg-primary/10 text-primary flex items-center justify-center flex-shrink-0">{v.icon}</div>
            <div>
              <h4 className="text-sm font-semibold text-foreground mb-1">{v.title}</h4>
              <p className="text-xs text-muted-foreground">{v.desc}</p>
            </div>
          </motion.div>
        ))}
      </div>
    </div>

    <Footer />
  </div>
  );
};

export default About;
