import { Crown } from "lucide-react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import { useEffect, useState } from "react";
import { cmsAPI } from "@/services/api";

const MembershipPlans = () => {
  const [page, setPage] = useState(null);

  useEffect(() => {
    loadCMS();
  }, []);

  const loadCMS = async () => {
    try {
      const data = await cmsAPI.getPage("MEMBERSHIP_PLANS");
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
        <Crown className="inline-block h-8 w-8 text-yellow-300 mb-2" />

        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">
          Membership Plans
        </h1>

        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">
          Choose the perfect membership plan and unlock premium features.
        </p>
      </div>

      {/* CMS Content */}
      <div className="container mx-auto px-4 py-12">
        <div
          className="cms-content max-w-5xl mx-auto bg-card border rounded-xl shadow-sm p-8"
          dangerouslySetInnerHTML={{
            __html: page?.content || "",
          }}
        />
      </div>

      <Footer />
    </div>
  );
};

export default MembershipPlans;