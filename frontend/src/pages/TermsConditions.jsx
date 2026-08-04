import { FileText } from "lucide-react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import CMSContent from "@/components/cms/CMSContent";
import { useEffect, useState } from "react";
import { cmsAPI } from "@/services/api";

const TermsConditions = () => {

  const [page, setPage] = useState(null);

  useEffect(() => {
    loadCMS();
  }, []);

  const loadCMS = async () => {
    try {
      const data = await cmsAPI.getPage("TERMS_AND_CONDITIONS");
      setPage(data);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="min-h-screen bg-background">

      <Navbar />

      <div
        className="py-12 text-center"
        style={{
          background:
            "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))",
        }}
      >
        <FileText className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />

        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">
          Terms & Conditions
        </h1>

        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">
          Please read these terms carefully before using Gathbandhan Matrimony.
        </p>
      </div>

      <CMSContent html={page?.content} />

      <Footer />

    </div>
  );
};

export default TermsConditions;