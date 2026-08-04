import { HelpCircle, Heart, Shield, Users, CreditCard, Search, MessageSquare, CheckCircle } from "lucide-react";
import { motion } from "framer-motion";
import { useState, useEffect } from "react";
import { faqAPI } from "@/services/api";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

const FAQ = () => {
    const [faqs, setFaqs] = useState([]);
  const [expandedItems, setExpandedItems] = useState([]);
  useEffect(() => {
      loadFaqs();
  }, []);

  const loadFaqs = async () => {
      const data = await faqAPI.getPublishedFaqs();
      setFaqs(data);
  };


  const toggleItem = (index) => {
    setExpandedItems(prev => 
      prev.includes(index) 
        ? prev.filter(i => i !== index)
        : [...prev, index]
    );
  };



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

        <div className="space-y-4">

            {faqs.map((faq, index) => {

                const isExpanded =
                    expandedItems.includes(index);

                return (

                    <div
                        key={faq.id}
                        className="bg-card rounded-xl border border-border overflow-hidden"
                    >

                        <button
                            onClick={() => toggleItem(index)}
                            className="w-full px-6 py-4 text-left flex items-center justify-between hover:bg-muted/50 transition-colors"
                        >

                            <span className="font-medium text-foreground pr-4">
                                {faq.question}
                            </span>

                            <div
                                className={`transform transition-transform ${
                                    isExpanded ? "rotate-180" : ""
                                }`}
                            >
                                <svg
                                    className="h-5 w-5 text-muted-foreground"
                                    fill="none"
                                    stroke="currentColor"
                                    viewBox="0 0 24 24"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth={2}
                                        d="M19 9l-7 7-7-7"
                                    />
                                </svg>
                            </div>

                        </button>

                        {isExpanded && (

                            <motion.div
                                initial={{ height: 0, opacity: 0 }}
                                animate={{ height: "auto", opacity: 1 }}
                                transition={{ duration: 0.3 }}
                                className="px-6 pb-4"
                            >

                                <p className="text-muted-foreground leading-relaxed">
                                    {faq.answer}
                                </p>

                            </motion.div>

                        )}

                    </div>

                );

            })}

        </div>

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
