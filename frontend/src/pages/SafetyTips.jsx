import { ShieldCheck } from "lucide-react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import CMSContent from "@/components/cms/CMSContent";
import { useEffect, useState } from "react";
import { cmsAPI } from "@/services/api";

const SafetyTips = () => {
  const [page, setPage] = useState(null);

  useEffect(() => {
    loadCMS();
  }, []);

  const loadCMS = async () => {
    try {
      const data = await cmsAPI.getPage("SAFETY_TIPS");
      setPage(data);
    } catch (error) {
      console.error("Failed to load CMS page", error);
    }
  };

  return (
    <div className="min-h-screen bg-background">
      <Navbar />

      {/* Hero */}
      <div
        className="py-12 text-center"
        style={{
          background:
            "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))",
        }}
      >
        <ShieldCheck className="inline-block h-8 w-8 text-pink-soft mb-2" />

        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">
          Safety Tips
        </h1>

        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">
          Stay safe while finding your perfect life partner on Gathbandhan Matrimony.
        </p>
      </div>

      {/* CMS Content */}
      <CMSContent html={page?.content} />

      <Footer />
    </div>
  );
};

export default SafetyTips;