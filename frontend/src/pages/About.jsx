import {
  Heart,
  Target,
  Eye,
  ShieldCheck,
  Users,
  Globe,
  Award,
  Scale,
  FileText,
  Lock,
  AlertTriangle,
  Mail,
  MapPin,
  CheckCircle2,
  ArrowRight,
  Building2,
  Gavel
} from "lucide-react";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import { useLanguage } from "@/context/LanguageContext";

const About = () => {
  const { t } = useLanguage();

  const values = t?.about?.values || [
    { title: "Trust & Safety", desc: "Every profile undergoes manual and ID-based verification. Your safety is our primary commitment." },
    { title: "Family First", desc: "We honor traditional family involvement while providing modern matchmaking tools." },
    { title: "Pan-India Reach", desc: "Connecting verified singles across 500+ cities and all major communities." },
    { title: "Privacy Protection", desc: "Advanced data encryption and photo privacy controls keep your personal information secure." }
  ];

  const legalPolicies = [
    {
      title: "Terms & Conditions",
      desc: "Detailed user agreement, eligibility rules, and platform usage guidelines.",
      path: "/terms",
      icon: <FileText className="h-6 w-6 text-pink-600" />
    },
    {
      title: "Privacy Policy",
      desc: "Comprehensive breakdown of data collection, encryption, and usage rights.",
      path: "/privacy",
      icon: <Lock className="h-6 w-6 text-purple-600" />
    },
    {
      title: "Refund Policy",
      desc: "Clear policies regarding subscription purchases, upgrades, and cancellations.",
      path: "/refund-policy",
      icon: <Scale className="h-6 w-6 text-indigo-600" />
    },
    {
      title: "Safety & Security",
      desc: "Essential guidelines and safety advice for online & offline interactions.",
      path: "/safety-tips",
      icon: <ShieldCheck className="h-6 w-6 text-emerald-600" />
    },
    {
      title: "Cookie Policy",
      desc: "Information about cookies, analytics, and session management practices.",
      path: "/cookie-policy",
      icon: <Globe className="h-6 w-6 text-blue-600" />
    }
  ];

  return (
    <div className="min-h-screen bg-background text-foreground flex flex-col">
      <Navbar />

      {/* Hero Section */}
      <div
        className="relative py-16 md:py-20 text-center overflow-hidden"
        style={{
          background:
            "linear-gradient(135deg, hsl(270 60% 30%), hsl(290 55% 40%), hsl(270 50% 50%))",
        }}
      >
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_right,rgba(255,255,255,0.15),transparent_50%)]" />
        <div className="relative container mx-auto px-4 z-10">
          <motion.div
            initial={{ scale: 0.8, opacity: 0 }}
            animate={{ scale: 1, opacity: 1 }}
            transition={{ duration: 0.5 }}
          >
            <Heart className="inline-block h-12 w-12 text-pink-300 fill-pink-300 mb-3 animate-pulse" />
          </motion.div>

          <h1 className="text-3xl md:text-5xl font-display font-extrabold text-white mb-4 tracking-tight">
            About Gathbandhan Matrimony & Legal Information
          </h1>

          <p className="text-white/85 text-base md:text-lg max-w-3xl mx-auto px-4 font-normal leading-relaxed">
            Connecting eligible singles and families across India with trust, transparency, and strict legal compliance.
          </p>
        </div>
      </div>

      <div className="container mx-auto px-4 py-12 max-w-6xl flex-1 space-y-12">
        {/* Who We Are & Overview */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-card rounded-2xl border border-border p-8 shadow-sm hover:shadow-md transition-shadow"
        >
          <div className="flex items-center gap-3 mb-4">
            <div className="h-10 w-10 rounded-xl bg-pink-100 dark:bg-pink-950/50 text-pink-600 flex items-center justify-center">
              <Building2 className="h-6 w-6" />
            </div>
            <h2 className="text-2xl md:text-3xl font-display font-bold">
              {t?.about?.whoWeAreTitle || "Who We Are"}
            </h2>
          </div>

          <p className="text-muted-foreground leading-relaxed text-base">
            {t?.about?.whoWeAreDesc ||
              "Gathbandhan Matrimony is a premier, technology-driven online matrimony platform committed to helping individuals find compatible life partners. Combining traditional cultural values with state-of-the-art matching algorithms, Gathbandhan ensures a secure, respectful, and joyful matchmaking experience for singles and their families."}
          </p>

          <div className="mt-6 grid sm:grid-cols-3 gap-4 pt-6 border-t border-border">
            <div className="flex items-center gap-3">
              <CheckCircle2 className="h-5 w-5 text-emerald-600 shrink-0" />
              <span className="text-sm font-medium">100% ID Verified Profiles</span>
            </div>
            <div className="flex items-center gap-3">
              <CheckCircle2 className="h-5 w-5 text-emerald-600 shrink-0" />
              <span className="text-sm font-medium">End-to-End Data Privacy</span>
            </div>
            <div className="flex items-center gap-3">
              <CheckCircle2 className="h-5 w-5 text-emerald-600 shrink-0" />
              <span className="text-sm font-medium">Government Compliant Intermediary</span>
            </div>
          </div>
        </motion.div>

        {/* Mission & Vision */}
        <div className="grid md:grid-cols-2 gap-6">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            className="bg-card rounded-2xl border border-border p-8 shadow-sm flex flex-col justify-between"
          >
            <div>
              <div className="flex items-center gap-3 mb-4">
                <div className="h-10 w-10 rounded-xl bg-purple-100 dark:bg-purple-950/50 text-purple-600 flex items-center justify-center">
                  <Target className="h-5 w-5" />
                </div>
                <h3 className="text-xl font-bold">
                  {t?.about?.missionTitle || "Our Mission"}
                </h3>
              </div>
              <p className="text-muted-foreground leading-relaxed">
                {t?.about?.missionDesc ||
                  "To facilitate authentic lifelong unions through a safe, reliable, and transparent platform that honors family values while implementing modern privacy standards."}
              </p>
            </div>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, x: 20 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            className="bg-card rounded-2xl border border-border p-8 shadow-sm flex flex-col justify-between"
          >
            <div>
              <div className="flex items-center gap-3 mb-4">
                <div className="h-10 w-10 rounded-xl bg-indigo-100 dark:bg-indigo-950/50 text-indigo-600 flex items-center justify-center">
                  <Eye className="h-5 w-5" />
                </div>
                <h3 className="text-xl font-bold">
                  {t?.about?.visionTitle || "Our Vision"}
                </h3>
              </div>
              <p className="text-muted-foreground leading-relaxed">
                {t?.about?.visionDesc ||
                  "To be India's most trusted matrimony service, celebrated for technological excellence, ethical operations, and the highest rate of happy, verified marriages."}
              </p>
            </div>
          </motion.div>
        </div>

        {/* Why Choose Us */}
        <div>
          <h2 className="text-2xl md:text-3xl font-display font-bold text-center mb-8">
            {t?.about?.whyChooseTitle || "Why Choose Gathbandhan?"}
          </h2>

          <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {values.map((val, index) => (
              <motion.div
                key={val.title || index}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ delay: index * 0.08 }}
                className="bg-card rounded-2xl border border-border p-6 shadow-sm hover:shadow-md transition-all flex flex-col justify-between"
              >
                <div>
                  <div className="h-11 w-11 rounded-xl bg-pink-500/10 text-pink-600 flex items-center justify-center mb-4">
                    <ShieldCheck className="h-6 w-6" />
                  </div>
                  <h3 className="font-bold text-lg mb-2">{val.title}</h3>
                  <p className="text-sm text-muted-foreground leading-relaxed">
                    {val.desc}
                  </p>
                </div>
              </motion.div>
            ))}
          </div>
        </div>

        {/* ================= LEGAL & COMPLIANCE FRAMEWORK ================= */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-card rounded-2xl border border-border p-8 shadow-sm space-y-6"
        >
          <div className="flex items-center gap-3 border-b border-border pb-4">
            <div className="h-10 w-10 rounded-xl bg-amber-100 dark:bg-amber-950/50 text-amber-600 flex items-center justify-center">
              <Gavel className="h-6 w-6" />
            </div>
            <div>
              <h2 className="text-2xl font-display font-bold">Legal & Intermediary Compliance Notice</h2>
              <p className="text-xs text-muted-foreground">Information Technology Act, 2000 & Intermediary Guidelines Compliance</p>
            </div>
          </div>

          <div className="grid md:grid-cols-2 gap-6 text-sm leading-relaxed text-muted-foreground">
            <div className="space-y-3">
              <h4 className="font-semibold text-foreground flex items-center gap-2">
                <Scale className="h-4 w-4 text-pink-600" />
                Intermediary Status & Liability Disclaimer
              </h4>
              <p>
                Gathbandhan Matrimony functions as an online matchmaking platform and intermediary under <strong>Section 79 of the Information Technology Act, 2000</strong>. We provide a digital infrastructure for registered users to view and interact with profiles. Gathbandhan does not guarantee marriage outcomes or initiate independent background checks beyond user-submitted verification documents.
              </p>
            </div>

            <div className="space-y-3">
              <h4 className="font-semibold text-foreground flex items-center gap-2">
                <AlertTriangle className="h-4 w-4 text-amber-600" />
                User Advisory & Due Diligence
              </h4>
              <p>
                Members and their families are strictly advised to perform independent background checks, family verifications, and financial inquiries prior to entering into matrimonial commitments or monetary transactions. Gathbandhan is not responsible for misrepresentations made by members.
              </p>
            </div>

            <div className="space-y-3">
              <h4 className="font-semibold text-foreground flex items-center gap-2">
                <Lock className="h-4 w-4 text-emerald-600" />
                Data Protection & Privacy Standards
              </h4>
              <p>
                We comply with the <strong>Digital Personal Data Protection (DPDP) Act 2023</strong> and Section 43A of the IT Act. User information is encrypted in transit and at rest. Personal contact details are only disclosed based on explicit user consent and privacy settings.
              </p>
            </div>

            <div className="space-y-3">
              <h4 className="font-semibold text-foreground flex items-center gap-2">
                <ShieldCheck className="h-4 w-4 text-indigo-600" />
                Prohibited Conduct & Anti-Fraud Policy
              </h4>
              <p>
                Financial solicitations, dowry demands, harassment, pornography, commercial advertising, and fake profile creation are strictly prohibited. Any violation results in immediate permanent ban and reporting to appropriate cybercrime enforcement authorities.
              </p>
            </div>
          </div>
        </motion.div>

        {/* Grievance Redressal Mechanism */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-primary/5 border border-primary/20 rounded-2xl p-8"
        >
          <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
            <div className="space-y-2">
              <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 text-primary text-xs font-semibold">
                IT Rules 2021 Requirement
              </div>
              <h3 className="text-xl font-bold">Grievance Redressal & Compliance Officer</h3>
              <p className="text-sm text-muted-foreground max-w-2xl leading-relaxed">
                If you have concerns regarding profile content, privacy violations, or regulatory compliance, please contact our designated Grievance Officer. Complaints will be acknowledged within 24 hours and resolved within 15 days.
              </p>
            </div>

            <div className="bg-card border border-border rounded-xl p-5 shrink-0 w-full md:w-auto text-sm space-y-2 shadow-sm">
              <div className="flex items-center gap-2 font-medium">
                <Mail className="h-4 w-4 text-primary" />
                <span>grievance@gathbandhan.com</span>
              </div>
              <div className="flex items-center gap-2 font-medium">
                <Mail className="h-4 w-4 text-primary" />
                <span>legal@gathbandhan.com</span>
              </div>
              <div className="flex items-center gap-2 text-muted-foreground text-xs pt-1 border-t border-border">
                <MapPin className="h-3.5 w-3.5" />
                <span>Legal Dept, Maharashtra, India</span>
              </div>
            </div>
          </div>
        </motion.div>

        {/* Directory of Legal Policies */}
        <div>
          <h2 className="text-2xl font-display font-bold mb-6">Legal Documentation Directory</h2>
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-5">
            {legalPolicies.map((policy) => (
              <Link
                key={policy.title}
                to={policy.path}
                className="bg-card border border-border rounded-xl p-5 hover:border-primary/40 hover:shadow-md transition-all flex flex-col justify-between group"
              >
                <div>
                  <div className="mb-3">{policy.icon}</div>
                  <h3 className="font-bold text-base mb-1 group-hover:text-primary transition-colors flex items-center justify-between">
                    {policy.title}
                    <ArrowRight className="h-4 w-4 opacity-0 group-hover:opacity-100 transition-opacity" />
                  </h3>
                  <p className="text-xs text-muted-foreground leading-relaxed">
                    {policy.desc}
                  </p>
                </div>
              </Link>
            ))}
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default About;