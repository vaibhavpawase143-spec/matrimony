import { useRef } from "react";
import { ChevronLeft, ChevronRight, Heart } from "lucide-react";
import { motion } from "framer-motion";
import { useLanguage } from "@/context/LanguageContext";
import success1 from "@/assets/success-couple1.jpg";
import success2 from "@/assets/success-couple2.jpg";
import success3 from "@/assets/success-couple3.jpg";

const stories = [
  { image: success1, names: "Rahul & Priya", city: "Mumbai", quote: "We found each other on Gathbandhan and it was love at first sight. Our families connected instantly!" },
  { image: success2, names: "Vikram & Ananya", city: "Delhi", quote: "After searching for 2 years, Gathbandhan matched us perfectly. We got married within 6 months!" },
  { image: success3, names: "Arjun & Meera", city: "Pune", quote: "Gathbandhan made our dream wedding possible. The Kundli matching feature sealed the deal for our parents." },
  { image: success1, names: "Saurabh & Kavya", city: "Jaipur", quote: "Thank you Gathbandhan for bringing two souls together. We are grateful for this beautiful journey." },
  { image: success2, names: "Rohit & Sneha", city: "Bangalore", quote: "Best matrimony platform! Verified profiles and excellent customer support made our search easy." },
];

const SuccessStories = () => {
  const { t } = useLanguage();
  const scrollRef = useRef(null);

  const scroll = (dir) => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: dir === "left" ? -320 : 320, behavior: "smooth" });
    }
  };

  return (
    <section className="py-16 bg-muted/50">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-3xl md:text-4xl font-display font-bold text-foreground">{t.successStories.title}</h2>
            <p className="text-muted-foreground text-sm mt-1">{t.successStories.subtitle}</p>
          </div>
          <div className="flex gap-2">
            <button onClick={() => scroll("left")} className="h-10 w-10 rounded-full bg-card border border-border flex items-center justify-center hover:bg-primary hover:text-primary-foreground transition-colors">
              <ChevronLeft className="h-5 w-5" />
            </button>
            <button onClick={() => scroll("right")} className="h-10 w-10 rounded-full bg-card border border-border flex items-center justify-center hover:bg-primary hover:text-primary-foreground transition-colors">
              <ChevronRight className="h-5 w-5" />
            </button>
          </div>
        </div>

        <div ref={scrollRef} className="flex gap-5 overflow-x-auto scrollbar-hide pb-4 snap-x snap-mandatory" style={{ scrollbarWidth: "none" }}>
          {stories.map((s, i) => (
            <motion.div
              key={i}
              initial={{ opacity: 0, scale: 0.95 }}
              whileInView={{ opacity: 1, scale: 1 }}
              viewport={{ once: true }}
              className="min-w-[300px] max-w-[300px] bg-card rounded-xl border border-border overflow-hidden shadow-sm hover:shadow-md transition-shadow snap-start flex-shrink-0"
            >
              <div className="h-48 overflow-hidden">
                <img src={s.image} alt={s.names} className="w-full h-full object-cover" />
              </div>
              <div className="p-5">
                <div className="flex items-center gap-2 mb-2">
                  <Heart className="h-4 w-4 text-primary fill-primary" />
                  <span className="text-sm font-semibold text-foreground">{s.names}</span>
                </div>
                <p className="text-xs text-muted-foreground mb-2">{s.city}</p>
                <p className="text-sm text-muted-foreground italic">"{s.quote}"</p>
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default SuccessStories;
