import {
  Heart,
  Target,
  Eye,
  ShieldCheck,
  Users,
  Globe,
  Award,
} from "lucide-react";
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

      {/* Hero Section */}
      <div
        className="py-14 text-center"
        style={{
          background:
            "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))",
        }}
      >
        <Heart className="inline-block h-10 w-10 text-pink-soft fill-pink-soft mb-3" />

        <h1 className="text-3xl md:text-5xl font-display font-bold text-primary-foreground mb-4">
          {t.about.title}
        </h1>

        <p className="text-primary-foreground/80 max-w-3xl mx-auto px-4">
          {t.about.subtitle}
        </p>
      </div>

      <div className="container mx-auto px-4 py-12 max-w-6xl">
        {/* Who We Are */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <h2 className="text-2xl font-display font-bold mb-4">
            {t.about.whoWeAreTitle}
          </h2>

          <p className="text-muted-foreground leading-8">
            {t.about.whoWeAreDesc}
          </p>
        </motion.div>

        {/* Mission & Vision */}
        <div className="grid md:grid-cols-2 gap-6 mb-10">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            className="bg-card rounded-xl border border-border p-6"
          >
            <div className="flex items-center gap-3 mb-4">
              <div className="h-10 w-10 rounded-full bg-primary/10 text-primary flex items-center justify-center">
                <Target className="h-5 w-5" />
              </div>

              <h3 className="text-xl font-bold">
                {t.about.missionTitle}
              </h3>
            </div>

            <p className="text-muted-foreground leading-7">
              {t.about.missionDesc}
            </p>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, x: 20 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            className="bg-card rounded-xl border border-border p-6"
          >
            <div className="flex items-center gap-3 mb-4">
              <div className="h-10 w-10 rounded-full bg-accent/10 text-accent flex items-center justify-center">
                <Eye className="h-5 w-5" />
              </div>

              <h3 className="text-xl font-bold">
                {t.about.visionTitle}
              </h3>
            </div>

            <p className="text-muted-foreground leading-7">
              {t.about.visionDesc}
            </p>
          </motion.div>
        </div>

        {/* Why Choose */}
        <h2 className="text-3xl font-display font-bold text-center mb-8">
          {t.about.whyChooseTitle}
        </h2>

        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-10">
          {values.map((value, index) => (
            <motion.div
              key={value.title}
              initial={{ opacity: 0, y: 25 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: index * 0.08 }}
              className="bg-card rounded-xl border border-border p-6 hover:shadow-lg transition-all"
            >
              <div className="h-12 w-12 rounded-xl bg-primary/10 text-primary flex items-center justify-center mb-4">
                {value.icon}
              </div>

              <h3 className="font-semibold text-lg mb-2">
                {value.title}
              </h3>

              <p className="text-sm text-muted-foreground leading-6">
                {value.desc}
              </p>
            </motion.div>
          ))}
        </div>

        {/* Commitment */}
        <motion.div
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          viewport={{ once: true }}
          className="bg-primary/5 border border-primary/10 rounded-xl p-8 text-center"
        >
          <ShieldCheck className="h-12 w-12 mx-auto text-primary mb-4" />

          <h2 className="text-2xl font-bold mb-4">
            {t.about.commitmentTitle}
          </h2>

          <p className="text-muted-foreground leading-8 max-w-4xl mx-auto">
            {t.about.commitmentDesc}
          </p>
        </motion.div>
      </div>

      <Footer />
    </div>
  );
};

export default About;