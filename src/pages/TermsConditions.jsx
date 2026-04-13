import { FileText, Users, Heart, Shield, AlertCircle, CheckCircle } from "lucide-react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

const TermsConditions = () => {
  return (
    <div className="min-h-screen bg-background">
      <Navbar />

      <div className="py-12 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <FileText className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Terms & Conditions</h1>
        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">Please read these terms carefully before using Gathbandhan Matrimony</p>
      </div>

      <div className="container mx-auto px-4 py-12 max-w-4xl">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-4">Acceptance of Terms</h2>
          <p className="text-muted-foreground leading-relaxed mb-6">
            By accessing and using Gathbandhan Matrimony, you accept and agree to be bound by these Terms and Conditions. 
            If you do not agree to these terms, please do not use our services.
          </p>
          <p className="text-muted-foreground leading-relaxed">
            These terms constitute a legally binding agreement between you and Gathbandhan Matrimony.
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
            <Users className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Eligibility & Registration</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Age Requirements</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>You must be at least 18 years old to create an account</li>
                <li>Minimum marriageable age as per Indian laws applies</li>
                <li>Users must be legally eligible to marry in their jurisdiction</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Registration Requirements</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Provide accurate and complete information</li>
                <li>Use genuine contact details</li>
                <li>Create only one account per person</li>
                <li>Agree to profile verification process</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Prohibited Users</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Individuals already married</li>
                <li>Those seeking casual relationships</li>
                <li>Users with fraudulent intentions</li>
                <li>Previously banned accounts</li>
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
            <Heart className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">User Responsibilities</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Profile Information</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Provide truthful and accurate information</li>
                <li>Keep profile details up to date</li>
                <li>Upload recent and authentic photographs</li>
                <li>Disclose relevant information honestly</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Communication Guidelines</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Communicate respectfully with all members</li>
                <li>Do not share inappropriate content</li>
                <li>Avoid harassment or offensive language</li>
                <li>Report suspicious behavior to support</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Meeting Safety</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Meet in public places initially</li>
                <li>Inform family about meetings</li>
                <li>Verify identity before meeting</li>
                <li>Trust your instincts</li>
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
            <Shield className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Prohibited Activities</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Strictly Forbidden</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Creating fake profiles or impersonating others</li>
                <li>Requesting or sharing money</li>
                <li>Engaging in fraudulent activities</li>
                <li>Sharing explicit or inappropriate content</li>
                <li>Harassment, threats, or abusive behavior</li>
                <li>Violating privacy of other members</li>
                <li>Using automated tools or bots</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Consequences</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Immediate account suspension</li>
                <li>Permanent ban for serious violations</li>
                <li>Legal action for criminal activities</li>
                <li>Reporting to law enforcement</li>
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
            <AlertCircle className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Disclaimer & Limitation</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Service Disclaimer</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>We do not guarantee successful matches</li>
                <li>Profile verification is not background verification</li>
                <li>Users are responsible for their decisions</li>
                <li>We are not marriage counselors or advisors</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Limitation of Liability</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Not liable for user interactions or outcomes</li>
                <li>Not responsible for offline meetings</li>
                <li>Platform provided "as is" without warranties</li>
                <li>Maximum liability limited to subscription fees</li>
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
            <CheckCircle className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Membership & Payments</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Subscription Terms</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Subscription fees are non-refundable except as specified</li>
                <li>Auto-renewal unless cancelled before renewal date</li>
                <li>Prices subject to change with notice</li>
                <li>No refunds for unused subscription periods</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Payment Security</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Secure payment gateway processing</li>
                <li>PCI DSS compliant payment systems</li>
                <li>Encrypted financial transactions</li>
                <li>Multiple payment options available</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.6 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-4">Account Termination</h2>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">User Termination</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Can delete account anytime through settings</li>
                <li>Data deletion takes 30 days to complete</li>
                <li>Cannot recover deleted accounts</li>
                <li>Subscription benefits end immediately</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Platform Termination</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Can suspend accounts for violations</li>
                <li>Can terminate accounts for serious breaches</li>
                <li>May provide notice for account closure</li>
                <li>Can modify or discontinue services</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.7 }}
          className="bg-card rounded-xl border border-border p-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-4">Legal Information</h2>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Governing Law</h3>
              <p className="text-muted-foreground leading-relaxed">
                These terms are governed by the laws of India. Any disputes shall be subject to the exclusive jurisdiction 
                of courts in Maharashtra, India.
              </p>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Modifications</h3>
              <p className="text-muted-foreground leading-relaxed">
                We reserve the right to modify these terms at any time. Changes will be effective immediately upon posting. 
                Continued use of the service constitutes acceptance of modified terms.
              </p>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Contact Information</h3>
              <div className="space-y-2 text-muted-foreground">
                <p><strong>Email:</strong> legal@gathbandhan.com</p>
                <p><strong>Phone:</strong> +91 8999823244</p>
                <p><strong>Address:</strong> Sangamner College, Sangamner, Maharashtra, India</p>
              </div>
            </div>
          </div>
          
          <p className="text-muted-foreground leading-relaxed mt-6">
            These Terms & Conditions were last updated on {new Date().toLocaleDateString()}. 
            By using Gathbandhan Matrimony, you acknowledge that you have read, understood, and agree to these terms.
          </p>
        </motion.div>
      </div>

      <Footer />
    </div>
  );
};

export default TermsConditions;
