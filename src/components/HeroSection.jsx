import { Heart, Search } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import heroCoupleNew from "@/assets/hero-couple-new.png";

const FloatingHeart = ({ className }) => (
  <Heart className={`absolute text-pink-soft fill-pink-soft opacity-50 ${className}`} />
);

const HeroSection = () => {
  const navigate = useNavigate();

  return (
    <section className="relative overflow-hidden min-h-[80vh] flex items-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
      {/* Floating hearts */}
      <FloatingHeart className="h-4 w-4 top-12 left-[10%] animate-float-heart" />
      <FloatingHeart className="h-6 w-6 top-24 left-[25%] animate-float-heart [animation-delay:0.5s]" />
      <FloatingHeart className="h-3 w-3 top-16 right-[30%] animate-float-heart [animation-delay:1s]" />
      <FloatingHeart className="h-5 w-5 top-32 right-[15%] animate-float-heart [animation-delay:1.5s]" />
      <FloatingHeart className="h-4 w-4 bottom-24 left-[40%] animate-float-heart [animation-delay:2s]" />
      <FloatingHeart className="h-3 w-3 top-8 left-[60%] animate-float-heart [animation-delay:0.8s]" />

      {/* Glowing search overlay */}
      <div className="absolute top-1/2 right-[15%] -translate-y-1/2 hidden lg:block">
        <motion.div
          className="w-64 h-12 rounded-full border-2 border-primary-foreground/30 flex items-center gap-3 px-5 backdrop-blur-sm bg-primary-foreground/10"
          animate={{ boxShadow: ["0 0 20px hsl(330 70% 85% / 0.3)", "0 0 40px hsl(330 70% 85% / 0.6)", "0 0 20px hsl(330 70% 85% / 0.3)"] }}
          transition={{ duration: 2, repeat: Infinity }}
        >
          <Search className="h-5 w-5 text-primary-foreground/70" />
          <motion.span
            className="text-sm text-primary-foreground/60"
            animate={{ opacity: [0.4, 1, 0.4] }}
            transition={{ duration: 2, repeat: Infinity }}
          >
            Finding your match...
          </motion.span>
        </motion.div>
      </div>

      <div className="container mx-auto px-4 py-16 md:py-20 flex flex-col md:flex-row items-center gap-8 relative z-10">
        <motion.div
          className="flex-1 text-center md:text-left"
          initial={{ opacity: 0, x: -40 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.7 }}
        >
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-display font-bold text-primary-foreground leading-tight mb-4">
            Find Your Perfect<br />Life Partner
          </h1>
          <p className="text-primary-foreground/80 text-lg md:text-xl mb-8 max-w-md">
            Join India's Most Trusted Matrimony Platform — Gathbandhan Matrimony
          </p>
          <div className="flex gap-4 justify-center md:justify-start">
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              onClick={() => navigate("/register")}
              className="bg-orange-cta hover:bg-orange-cta/90 text-primary-foreground font-semibold px-8 py-3 rounded-lg text-base transition-all duration-150 shadow-lg hover:shadow-xl"
            >
              Register Now
            </motion.button>
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              onClick={() => navigate("/login")}
              className="bg-primary-foreground/20 hover:bg-primary-foreground/30 text-primary-foreground font-semibold px-8 py-3 rounded-lg text-base border border-primary-foreground/30 backdrop-blur transition-all duration-150"
            >
              Login
            </motion.button>
          </div>
        </motion.div>

        <motion.div
          className="flex-1 flex justify-center md:justify-end"
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.7, delay: 0.2 }}
        >
          <img
            src={heroCoupleNew}
            alt="Happy couple"
            className="w-72 md:w-96 lg:w-[28rem] object-contain drop-shadow-2xl"
          />
        </motion.div>
      </div>
    </section>
  );
};

export default HeroSection;
