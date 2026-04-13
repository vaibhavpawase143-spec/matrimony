import { useEffect, useRef } from "react";
import { Play } from "lucide-react";
import { useLanguage } from "@/context/LanguageContext";
import success1 from "@/assets/success-couple1.jpg";
import success2 from "@/assets/success-couple2.jpg";
import success3 from "@/assets/success-couple3.jpg";

const videos = [
  { thumbnail: success1, title: "Rahul & Priya's Wedding Story" },
  { thumbnail: success2, title: "Vikram & Ananya's Journey" },
  { thumbnail: success3, title: "Arjun & Meera's Love Story" },
  { thumbnail: success1, title: "Finding Love on Gathbandhan" },
  { thumbnail: success2, title: "Our Wedding Day Highlights" },
  { thumbnail: success3, title: "From Match to Marriage" },
];

const VideoCarousel = () => {
  const { t } = useLanguage();
  const scrollRef = useRef(null);

  useEffect(() => {
    const el = scrollRef.current;
    if (!el) return;
    const interval = setInterval(() => {
      if (el.scrollLeft >= el.scrollWidth - el.clientWidth - 10) {
        el.scrollTo({ left: 0, behavior: "smooth" });
      } else {
        el.scrollBy({ left: 260, behavior: "smooth" });
      }
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  return (
    <section className="py-16 bg-background">
      <div className="container mx-auto px-4">
        <h2 className="text-3xl md:text-4xl font-display font-bold text-foreground text-center mb-3">{t.videoCarousel.title}</h2>
        <p className="text-muted-foreground text-center mb-10">{t.videoCarousel.subtitle}</p>

        <div ref={scrollRef} className="flex gap-5 overflow-x-auto pb-4" style={{ scrollbarWidth: "none" }}>
          {videos.map((v, i) => (
            <div key={i} className="min-w-[240px] max-w-[240px] flex-shrink-0 group cursor-pointer">
              <div className="relative aspect-video rounded-xl overflow-hidden mb-2">
                <img src={v.thumbnail} alt={v.title} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" />
                <div className="absolute inset-0 bg-foreground/30 flex items-center justify-center group-hover:bg-foreground/40 transition-colors">
                  <div className="h-12 w-12 rounded-full bg-primary-foreground/90 flex items-center justify-center shadow-lg">
                    <Play className="h-5 w-5 text-primary ml-0.5" />
                  </div>
                </div>
              </div>
              <p className="text-sm font-medium text-foreground">{v.title}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default VideoCarousel;
