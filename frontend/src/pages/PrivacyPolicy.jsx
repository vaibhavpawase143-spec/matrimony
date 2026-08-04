import { Shield, Eye, Lock, Database, UserCheck, Globe } from "lucide-react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import { useEffect, useState } from "react";
import { cmsAPI } from "@/services/api";
import CMSContent from "@/components/cms/CMSContent";
import Footer from "@/components/Footer";

const PrivacyPolicy = () => {
    const [page, setPage] = useState(null);
    useEffect(() => {
        loadCMS();
    }, []);
const loadCMS = async () => {
    try {
        const data = await cmsAPI.getPage("PRIVACY_POLICY");
        setPage(data);
    } catch (error) {
        console.error("Failed to load CMS page", error);
    }
};
  return (
    <div className="min-h-screen bg-background">
      <Navbar />

      <div className="py-12 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <Shield className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Privacy Policy</h1>
        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">Your privacy is our top priority at Gathbandhan Matrimony</p>
      </div>

      <div className="container mx-auto px-4 py-12 max-w-4xl">
 <CMSContent html={page?.content} />

      </div>

      <Footer />
    </div>
  );
};

export default PrivacyPolicy;
