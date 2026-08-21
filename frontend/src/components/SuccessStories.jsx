import { useRef, useState, useEffect } from "react";
import { ChevronLeft, ChevronRight, Heart, ArrowRight } from "lucide-react";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import { useLanguage } from "@/context/LanguageContext";
import { successStoryAPI } from "@/services/api";
import success1 from "@/assets/success-couple1.jpg";
import success2 from "@/assets/success-couple2.jpg";
import success3 from "@/assets/success-couple3.jpg";

const fallbackStories = [
  { id: "f1", coupleImageUrl: success1, partnerOneName: "Rahul", partnerTwoName: "Priya", location: "Mumbai", shortStory: "We found each other on Gathbandhan and it was love at first sight. Our families connected instantly!" },
  { id: "f2", coupleImageUrl: success2, partnerOneName: "Vikram", partnerTwoName: "Ananya", location: "Delhi", shortStory: "After searching for 2 years, Gathbandhan matched us perfectly. We got married within 6 months!" },
  { id: "f3", coupleImageUrl: success3, partnerOneName: "Arjun", partnerTwoName: "Meera", location: "Pune", shortStory: "Gathbandhan made our dream wedding possible. The Kundli matching feature sealed the deal for our parents." },
];

const SuccessStories = () => {
  const { t } = useLanguage();
  const scrollRef = useRef(null);
  const [stories, setStories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStories();
  }, []);

  const loadStories = async () => {
    try {
      setLoading(true);
      const res = await successStoryAPI.getPublishedStories(0, 6);
      const data = res.data ?? res;

      if (data && data.content && data.content.length > 0) {
        setStories(data.content);
      } else if (Array.isArray(data) && data.length > 0) {
        setStories(data);
      } else {
        setStories(fallbackStories);
      }
    } catch (err) {
      console.error("Error loading success stories section", err);
      setStories(fallbackStories);
    } finally {
      setLoading(false);
    }
  };

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
            <h2 className="text-3xl md:text-4xl font-display font-bold text-foreground">
              {t?.successStories?.title || "Real Success Stories"}
            </h2>
            <p className="text-muted-foreground text-sm mt-1">
              {t?.successStories?.subtitle || "Read how couples met and found forever love on Gathbandhan"}
            </p>
          </div>
          <div className="flex items-center gap-3">
            <Link
              to="/success-stories"
              className="hidden sm:inline-flex items-center gap-1.5 text-sm font-semibold text-primary hover:underline"
            >
              <span>View All</span>
              <ArrowRight className="h-4 w-4" />
            </Link>
            <div className="flex gap-2">
              <button
                onClick={() => scroll("left")}
                className="h-10 w-10 rounded-full bg-card border border-border flex items-center justify-center hover:bg-primary hover:text-primary-foreground transition-colors"
              >
                <ChevronLeft className="h-5 w-5" />
              </button>
              <button
                onClick={() => scroll("right")}
                className="h-10 w-10 rounded-full bg-card border border-border flex items-center justify-center hover:bg-primary hover:text-primary-foreground transition-colors"
              >
                <ChevronRight className="h-5 w-5" />
              </button>
            </div>
          </div>
        </div>

        <div
          ref={scrollRef}
          className="flex gap-5 overflow-x-auto scrollbar-hide pb-4 snap-x snap-mandatory"
          style={{ scrollbarWidth: "none" }}
        >
          {stories.map((s, i) => (
            <motion.div
              key={s.id || i}
              initial={{ opacity: 0, scale: 0.95 }}
              whileInView={{ opacity: 1, scale: 1 }}
              viewport={{ once: true }}
              className="min-w-[300px] max-w-[300px] bg-card rounded-xl border border-border overflow-hidden shadow-sm hover:shadow-md transition-shadow snap-start flex-shrink-0 flex flex-col justify-between"
            >
              <div>
                <div className="h-48 overflow-hidden bg-slate-900">
                  <img
                    src={s.coupleImageUrl || s.partnerOneImageUrl || success1}
                    alt={`${s.partnerOneName} & ${s.partnerTwoName}`}
                    className="w-full h-full object-cover"
                  />
                </div>
                <div className="p-5">
                  <div className="flex items-center gap-2 mb-1">
                    <Heart className="h-4 w-4 text-primary fill-primary flex-shrink-0" />
                    <span className="text-sm font-semibold text-foreground truncate">
                      {s.partnerOneName} & {s.partnerTwoName}
                    </span>
                  </div>
                  {s.location && <p className="text-xs text-muted-foreground mb-2">{s.location}</p>}
                  <p className="text-sm text-muted-foreground italic line-clamp-3">"{s.shortStory}"</p>
                </div>
              </div>

              {s.id && typeof s.id === "number" && (
                <div className="px-5 pb-5 pt-0">
                  <Link
                    to={`/success-stories/${s.id}`}
                    className="text-xs font-semibold text-primary hover:underline inline-flex items-center gap-1"
                  >
                    Read Story <ArrowRight className="h-3 w-3" />
                  </Link>
                </div>
              )}
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default SuccessStories;
