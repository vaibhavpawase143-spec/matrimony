import { HelpCircle, Heart, Shield, Users, CreditCard, Search, MessageSquare, CheckCircle } from "lucide-react";
import { motion } from "framer-motion";
import { useState } from "react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

const FAQ = () => {
  const [expandedItems, setExpandedItems] = useState([]);

  const toggleItem = (index) => {
    setExpandedItems(prev => 
      prev.includes(index) 
        ? prev.filter(i => i !== index)
        : [...prev, index]
    );
  };

  const faqCategories = [
    {
      icon: <Users className="h-5 w-5" />,
      title: "Getting Started",
      color: "text-blue-600",
      questions: [
        {
          q: "How do I create an account on Gathbandhan Matrimony?",
          a: "Creating an account is simple and free! Click on the 'Register' button, fill in your basic details like name, age, contact information, and preferences. You'll receive a verification email/SMS to activate your account. The entire process takes just 2-3 minutes."
        },
        {
          q: "What information do I need to provide for registration?",
          a: "You'll need to provide: Full name, date of birth, gender, contact details (email and phone), location, basic education and profession details, and partner preferences. All information is kept secure and is used only for matchmaking purposes."
        },
        {
          q: "Is my profile information kept confidential?",
          a: "Yes, absolutely! Your privacy is our top priority. Your profile is visible only to registered members who match your preferences. You can control your visibility settings and choose who can view your profile and photos."
        },
        {
          q: "How long does profile verification take?",
          a: "Basic verification is usually completed within 24-48 hours. Premium members get priority verification. You'll receive an email once your profile is verified and ready to be viewed by other members."
        }
      ]
    },
    {
      icon: <Heart className="h-5 w-5" />,
      title: "Matching & Communication",
      color: "text-pink-600",
      questions: [
        {
          q: "How does the matching system work?",
          a: "Our intelligent matching system uses multiple factors: age, location, education, profession, family background, lifestyle preferences, and astrological compatibility. The system learns from your interactions to provide better matches over time."
        },
        {
          q: "Can I communicate with other members for free?",
          a: "Basic members can express interest in profiles, but direct messaging requires a premium subscription. Premium members can send unlimited messages, view contact details, and use advanced communication features."
        },
        {
          q: "How do I know if a profile is genuine?",
          a: "Look for verification badges (photo verified, phone verified, education verified). Verified profiles have higher authenticity. We also recommend video calls before meeting in person and checking social media profiles when possible."
        },
        {
          q: "What if I'm not getting good matches?",
          a: "Try updating your preferences, adding more details to your profile, and uploading recent photos. Our matching algorithm improves with more information. You can also manually search and filter profiles based on your criteria."
        }
      ]
    },
    {
      icon: <Shield className="h-5 w-5" />,
      title: "Safety & Security",
      color: "text-green-600",
      questions: [
        {
          q: "How do you ensure member safety?",
          a: "We implement multiple safety measures: profile verification, photo verification, phone verification, AI-powered fraud detection, 24/7 monitoring, and easy reporting mechanisms. We also provide safety guidelines for offline meetings."
        },
        {
          q: "What should I do if I encounter a fake profile?",
          a: "Immediately report the profile using the 'Report' button. Block the user to prevent further contact. Our safety team investigates all reports within 24 hours and takes appropriate action, including permanent banning of fraudulent accounts."
        },
        {
          q: "Is it safe to share my contact information?",
          a: "Share contact details only with verified members you trust. Start with platform messaging, then move to phone calls before sharing address. Never share financial information or send money to anyone you meet online."
        },
        {
          q: "How do I arrange safe meetings?",
          a: "Always meet in public places like cafes or restaurants. Inform family or friends about your meeting. Tell someone the location, time, and person you're meeting. Consider video calls before in-person meetings."
        }
      ]
    },
    {
      icon: <CreditCard className="h-5 w-5" />,
      title: "Payments & Membership",
      color: "text-purple-600",
      questions: [
        {
          q: "What are the membership plans available?",
          a: "We offer three plans: Basic (free limited features), Premium (3 months, 6 months, 12 months), and Elite (lifetime access). Premium plans include unlimited messaging, advanced filters, profile highlighting, and priority support."
        },
        {
          q: "What payment methods do you accept?",
          a: "We accept all major credit/debit cards, UPI, net banking, and digital wallets. All payments are processed through secure, PCI DSS compliant payment gateways with encryption."
        },
        {
          q: "Can I get a refund if I'm not satisfied?",
          a: "We offer a 7-day money-back guarantee for new premium members. If you're not satisfied within the first week, contact our support team for a full refund. Please refer to our Refund Policy for detailed terms."
        },
        {
          q: "Does my subscription auto-renew?",
          a: "Yes, subscriptions auto-renew for convenience. You can disable auto-renewal anytime from your account settings. We'll send a reminder 3 days before renewal so you can cancel if needed."
        }
      ]
    },
    {
      icon: <Search className="h-5 w-5" />,
      title: "Profile & Features",
      color: "text-orange-600",
      questions: [
        {
          q: "How many photos can I upload?",
          a: "Basic members can upload up to 5 photos. Premium members can upload up to 20 photos including cover photos and private albums. All photos go through manual approval to ensure appropriateness."
        },
        {
          q: "Can I hide my profile from specific people?",
          a: "Yes, you can block specific members from viewing your profile. You can also use privacy settings to control who sees your profile, photos, and contact information based on membership level and preferences."
        },
        {
          q: "What is Kundli matching?",
          a: "Kundli matching is traditional Vedic astrology compatibility analysis. We provide Ashtakoot Gun Milan scoring with detailed reports on 36 compatibility points. This helps assess astrological compatibility between partners."
        },
        {
          q: "How do I improve my profile visibility?",
          a: "Complete your profile fully, upload recent photos, get verified badges, use premium membership for profile highlighting, and stay active on the platform. Complete profiles get 5x more views and responses."
        }
      ]
    },
    {
      icon: <MessageSquare className="h-5 w-5" />,
      title: "Technical Support",
      color: "text-indigo-600",
      questions: [
        {
          q: "I forgot my password. How do I reset it?",
          a: "Click on 'Forgot Password' on the login page. Enter your registered email or phone number. You'll receive a reset link/OTP to create a new password. The reset link is valid for 24 hours."
        },
        {
          q: "Why isn't my profile showing in search results?",
          a: "This could be due to incomplete profile, photo pending approval, privacy settings, or account suspension. Complete your profile, upload photos, check your privacy settings, and ensure your account is in good standing."
        },
        {
          q: "How do I delete my account permanently?",
          a: "Go to Account Settings > Delete Account. Enter your password to confirm. Account deletion takes 30 days to process, during which you can reactivate it. After 30 days, all data is permanently deleted."
        },
        {
          q: "The app is running slow. What should I do?",
          a: "Try clearing your browser cache, updating the app, checking your internet connection, or using a different browser. If issues persist, contact our technical support with details about your device and browser."
        }
      ]
    }
  ];

  return (
    <div className="min-h-screen bg-background">
      <Navbar />

      <div className="py-12 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <HelpCircle className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Frequently Asked Questions</h1>
        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">Find answers to common questions about Gathbandhan Matrimony</p>
      </div>

      <div className="container mx-auto px-4 py-12 max-w-4xl">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-6">Quick Help</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="flex items-center gap-3 p-4 bg-muted/50 rounded-lg">
              <CheckCircle className="h-5 w-5 text-green-600" />
              <div>
                <p className="font-semibold text-foreground">24/7 Support</p>
                <p className="text-sm text-muted-foreground">Get help anytime, anywhere</p>
              </div>
            </div>
            <div className="flex items-center gap-3 p-4 bg-muted/50 rounded-lg">
              <Shield className="h-5 w-5 text-blue-600" />
              <div>
                <p className="font-semibold text-foreground">Safe Platform</p>
                <p className="text-sm text-muted-foreground">Verified profiles & secure matching</p>
              </div>
            </div>
            <div className="flex items-center gap-3 p-4 bg-muted/50 rounded-lg">
              <Heart className="h-5 w-5 text-pink-600" />
              <div>
                <p className="font-semibold text-foreground">Success Stories</p>
                <p className="text-sm text-muted-foreground">20,000+ happy marriages</p>
              </div>
            </div>
            <div className="flex items-center gap-3 p-4 bg-muted/50 rounded-lg">
              <Users className="h-5 w-5 text-purple-600" />
              <div>
                <p className="font-semibold text-foreground">Large Community</p>
                <p className="text-sm text-muted-foreground">50,000+ verified members</p>
              </div>
            </div>
          </div>
        </motion.div>

        {faqCategories.map((category, categoryIndex) => (
          <motion.div
            key={categoryIndex}
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: categoryIndex * 0.1 }}
            className="mb-8"
          >
            <div className="flex items-center gap-3 mb-6">
              <div className={category.color}>{category.icon}</div>
              <h2 className="text-2xl font-display font-bold text-foreground">{category.title}</h2>
            </div>
            
            <div className="space-y-4">
              {category.questions.map((faq, faqIndex) => {
                const itemIndex = `${categoryIndex}-${faqIndex}`;
                const isExpanded = expandedItems.includes(itemIndex);
                
                return (
                  <div key={faqIndex} className="bg-card rounded-xl border border-border overflow-hidden">
                    <button
                      onClick={() => toggleItem(itemIndex)}
                      className="w-full px-6 py-4 text-left flex items-center justify-between hover:bg-muted/50 transition-colors"
                    >
                      <span className="font-medium text-foreground pr-4">{faq.q}</span>
                      <div className={`transform transition-transform ${isExpanded ? 'rotate-180' : ''}`}>
                        <svg className="h-5 w-5 text-muted-foreground" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                        </svg>
                      </div>
                    </button>
                    
                    {isExpanded && (
                      <motion.div
                        initial={{ height: 0, opacity: 0 }}
                        animate={{ height: 'auto', opacity: 1 }}
                        exit={{ height: 0, opacity: 0 }}
                        transition={{ duration: 0.3 }}
                        className="px-6 pb-4"
                      >
                        <p className="text-muted-foreground leading-relaxed">{faq.a}</p>
                      </motion.div>
                    )}
                  </div>
                );
              })}
            </div>
          </motion.div>
        ))}

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.6 }}
          className="bg-card rounded-xl border border-border p-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-6">Still Have Questions?</h2>
          <p className="text-muted-foreground leading-relaxed mb-6">
            Can't find what you're looking for? Our dedicated support team is here to help you with any queries or concerns.
          </p>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <h3 className="font-semibold text-foreground mb-3">Contact Support</h3>
              <div className="space-y-2 text-muted-foreground">
                <p><strong>Email:</strong> support@gathbandhan.com</p>
                <p><strong>Phone:</strong> +91 8999823244</p>
                <p><strong>WhatsApp:</strong> +91 8999823244</p>
                <p><strong>Timing:</strong> Mon-Sat, 9AM-6PM</p>
              </div>
            </div>
            
            <div>
              <h3 className="font-semibold text-foreground mb-3">Quick Links</h3>
              <div className="space-y-2">
                <a href="/help" className="block text-primary hover:underline">Help Center</a>
                <a href="/contact" className="block text-primary hover:underline">Contact Us</a>
                <a href="/privacy-policy" className="block text-primary hover:underline">Privacy Policy</a>
                <a href="/terms" className="block text-primary hover:underline">Terms & Conditions</a>
              </div>
            </div>
          </div>
        </motion.div>
      </div>

      <Footer />
    </div>
  );
};

export default FAQ;
