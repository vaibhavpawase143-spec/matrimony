import { Shield, Eye, Lock, Database, UserCheck, Globe } from "lucide-react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

const PrivacyPolicy = () => {
  return (
    <div className="min-h-screen bg-background">
      <Navbar />

      <div className="py-12 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <Shield className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Privacy Policy</h1>
        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">Your privacy is our top priority at Gathbandhan Matrimony</p>
      </div>

      <div className="container mx-auto px-4 py-12 max-w-4xl">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-4">Introduction</h2>
          <p className="text-muted-foreground leading-relaxed mb-6">
            At Gathbandhan Matrimony, we are committed to protecting your personal information and ensuring your privacy. 
            This Privacy Policy outlines how we collect, use, and safeguard your data when you use our matrimony services.
          </p>
          <p className="text-muted-foreground leading-relaxed">
            By using Gathbandhan Matrimony, you agree to the collection and use of information in accordance with this policy.
          </p>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.1 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <Database className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Information We Collect</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Personal Information</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Name, age, date of birth, and gender</li>
                <li>Contact information (phone number, email address)</li>
                <li>Location details (city, state, country)</li>
                <li>Professional and educational background</li>
                <li>Family details and preferences</li>
                <li>Photographs and videos</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Usage Information</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Profile views and interactions</li>
                <li>Search preferences and filters</li>
                <li>Communication with other members</li>
                <li>Login frequency and session duration</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Technical Information</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>IP address and device information</li>
                <li>Browser type and operating system</li>
                <li>Cookies and tracking data</li>
                <li>Access times and referring websites</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.2 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <Eye className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">How We Use Your Information</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Service Provision</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Create and manage your matrimonial profile</li>
                <li>Match you with compatible partners</li>
                <li>Facilitate communication between members</li>
                <li>Provide personalized recommendations</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Safety and Security</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Verify member identities and profiles</li>
                <li>Prevent fraudulent activities</li>
                <li>Monitor for inappropriate behavior</li>
                <li>Ensure platform safety</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Service Improvement</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Analyze usage patterns to improve features</li>
                <li>Conduct research and development</li>
                <li>Send relevant notifications and updates</li>
                <li>Provide customer support</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.3 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <Lock className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Data Protection</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Security Measures</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>SSL encryption for all data transmissions</li>
                <li>Secure password storage with hashing</li>
                <li>Regular security audits and updates</li>
                <li>Limited employee access to personal data</li>
                <li>Secure data centers with 24/7 monitoring</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Data Retention</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Personal data retained only as long as necessary</li>
                <li>Account deletion upon user request</li>
                <li>Automatic deletion of inactive accounts after 2 years</li>
                <li>Secure disposal of physical and digital records</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.4 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <UserCheck className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Your Rights</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Control Over Your Data</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>View and update your profile information</li>
                <li>Delete your account and associated data</li>
                <li>Download your personal data</li>
                <li>Control visibility of your profile</li>
                <li>Opt-out of marketing communications</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Privacy Settings</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Control who can view your profile</li>
                <li>Manage photo visibility settings</li>
                <li>Set communication preferences</li>
                <li>Block or report inappropriate users</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.5 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <Globe className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Third-Party Sharing</h2>
          </div>
          
          <p className="text-muted-foreground leading-relaxed mb-4">
            We do not sell, rent, or trade your personal information with third parties for marketing purposes. 
            We may share information only in the following circumstances:
          </p>
          
          <ul className="list-disc list-inside text-muted-foreground space-y-2">
            <li>With your explicit consent</li>
            <li>With verified matches for communication purposes</li>
            <li>With service providers who assist in platform operations</li>
            <li>When required by law or legal process</li>
            <li>To protect our rights, property, or safety</li>
          </ul>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.6 }}
          className="bg-card rounded-xl border border-border p-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-4">Contact Us</h2>
          <p className="text-muted-foreground leading-relaxed mb-4">
            If you have any questions about this Privacy Policy or how we handle your data, please contact us:
          </p>
          
          <div className="space-y-2 text-muted-foreground">
            <p><strong>Email:</strong> privacy@gathbandhan.com</p>
            <p><strong>Phone:</strong> +91 8999823244</p>
            <p><strong>Address:</strong> Sangamner College, Sangamner, Maharashtra, India</p>
          </div>
          
          <p className="text-muted-foreground leading-relaxed mt-4">
            This Privacy Policy was last updated on {new Date().toLocaleDateString()} and may be updated periodically 
            to reflect changes in our practices or legal requirements.
          </p>
        </motion.div>
      </div>

      <Footer />
    </div>
  );
};

export default PrivacyPolicy;
