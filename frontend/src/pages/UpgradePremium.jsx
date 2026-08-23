import React, { useState, useEffect } from "react";
import { 
  Crown, 
  Check, 
  X, 
  Star, 
  Heart, 
  MessageCircle, 
  Phone, 
  Filter, 
  Eye, 
  Shield, 
  Headphones, 
  Users, 
  Sparkles, 
  ArrowRight, 
  ChevronDown, 
  RefreshCw,
  Clock,
  Calendar,
  AlertCircle,
  Image
} from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import { subscriptionAPI } from "@/services/api";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import heroCoupleImg from "@/assets/hero-couple-new.png";

const loadRazorpayScript = () => {
  return new Promise((resolve) => {
    if (window.Razorpay) {
      resolve(true);
      return;
    }
    const script = document.createElement("script");
    script.src = "https://checkout.razorpay.com/v1/checkout.js";
    script.async = true;
    script.onload = () => resolve(true);
    script.onerror = () => resolve(false);
    document.body.appendChild(script);
  });
};

const UpgradePremium = () => {
  const navigate = useNavigate();
  const [plans, setPlans] = useState([]);
  const [currentSubscription, setCurrentSubscription] = useState(null);
  const [loading, setLoading] = useState(true);
  const [processingPlanId, setProcessingPlanId] = useState(null);
  const [expandedFAQ, setExpandedFAQ] = useState(null);
  const [showSandboxModal, setShowSandboxModal] = useState(null);

  const loadData = async () => {
    try {
      setLoading(true);
      const [plansData, currentSub] = await Promise.all([
        subscriptionAPI.getPlans(),
        subscriptionAPI.getMySubscription()
      ]);

      const activePlans = Array.isArray(plansData) ? plansData : [];
      setPlans(activePlans);
      setCurrentSubscription(currentSub || null);
    } catch (error) {
      console.error("Failed to load subscription data:", error);
      toast.error("Unable to load subscription plans. Please refresh.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const calculateDaysRemaining = (endDate) => {
    if (!endDate) return 0;
    const diff = new Date(endDate) - new Date();
    const days = Math.ceil(diff / (1000 * 60 * 60 * 24));
    return days > 0 ? days : 0;
  };

  const formatDate = (date) => {
    if (!date) return "-";
    return new Date(date).toLocaleDateString("en-IN", {
      day: "2-digit",
      month: "short",
      year: "numeric"
    });
  };

  const handleSubscribe = async (plan) => {
    try {
      setProcessingPlanId(plan.id);

      // 1. Request backend to create Razorpay payment order
      const orderData = await subscriptionAPI.createOrder(plan.id);

      if (!orderData || !orderData.orderId) {
        throw new Error("Invalid order response from payment server");
      }

      // Check if sandbox test order or real Razorpay
      const isTestOrder = String(orderData.orderId).startsWith("order_test_");
      const isRazorpayAvailable = await loadRazorpayScript();

      if (isTestOrder || !isRazorpayAvailable) {
        // Show Sandbox Test Payment Confirmation modal
        setShowSandboxModal({
          plan,
          orderData
        });
        return;
      }

      // Real Razorpay Checkout flow
      const options = {
        key: orderData.key,
        amount: orderData.amount,
        currency: orderData.currency || "INR",
        name: "Gathbandhan Matrimony",
        description: `${plan.name} (${plan.duration} Days Membership)`,
        order_id: orderData.orderId,
        handler: async function (response) {
          try {
            toast.loading("Verifying payment with bank...", { id: "payment-verify" });
            const verifyRes = await subscriptionAPI.verifyPayment({
              orderId: response.razorpay_order_id,
              paymentId: response.razorpay_payment_id,
              signature: response.razorpay_signature
            });

            if (verifyRes && verifyRes.success !== false) {
              toast.success("🎉 Payment verified! Your Premium membership is active.", { id: "payment-verify" });
              await loadData();
              navigate("/subscription-history");
            } else {
              toast.error("Payment verification failed. Please contact support.", { id: "payment-verify" });
            }
          } catch (verifyErr) {
            toast.error(verifyErr.message || "Payment verification failed", { id: "payment-verify" });
          }
        },
        theme: {
          color: "#be123c"
        }
      };

      const rzp = new window.Razorpay(options);
      rzp.on("payment.failed", function (response) {
        toast.error(`Payment failed: ${response.error?.description || "Transaction declined"}`);
      });
      rzp.open();

    } catch (error) {
      console.error("Payment checkout error:", error);
      toast.error(error.message || "Failed to initiate payment. Please try again.");
    } finally {
      setProcessingPlanId(null);
    }
  };

  const handleCompleteSandboxPayment = async () => {
    if (!showSandboxModal) return;
    const { orderData } = showSandboxModal;

    try {
      setProcessingPlanId(showSandboxModal.plan.id);
      toast.loading("Verifying sandbox transaction...", { id: "sandbox-verify" });

      const verifyRes = await subscriptionAPI.verifyPayment({
        orderId: orderData.orderId,
        paymentId: `pay_sandbox_${Date.now()}`,
        signature: "sandbox_test_signature"
      });

      if (verifyRes && verifyRes.success !== false) {
        toast.success("🎉 Payment verified! Your Premium membership is active.", { id: "sandbox-verify" });
        setShowSandboxModal(null);
        await loadData();
        navigate("/subscription-history");
      } else {
        toast.error("Payment verification failed.", { id: "sandbox-verify" });
      }
    } catch (error) {
      toast.error(error.message || "Verification failed", { id: "sandbox-verify" });
    } finally {
      setProcessingPlanId(null);
    }
  };

  const featuresComparison = [
    { feature: "View Verified Contact Numbers", free: false, premium: true, icon: <Phone className="h-4 w-4" /> },
    { feature: "Direct Personalized Messaging", free: false, premium: true, icon: <MessageCircle className="h-4 w-4" /> },
    { feature: "Priority Profile Visibility in Search", free: "Standard", premium: "Top 1%", icon: <Star className="h-4 w-4" /> },
    { feature: "See Who Visited Your Profile", free: false, premium: true, icon: <Eye className="h-4 w-4" /> },
    { feature: "Unlimited Interest & Express Connect", free: "5 per day", premium: "Unlimited", icon: <Heart className="h-4 w-4" /> },
    { feature: "Full Photo Gallery & Album Access", free: "1 Photo", premium: "Unlimited HD Photos", icon: <Image className="h-4 w-4" /> },
    { feature: "Dedicated Relationship Advisor Support", free: "Email", premium: "Priority Phone & Chat", icon: <Headphones className="h-4 w-4" /> },
    { feature: "Verified Premium Badge", free: false, premium: true, icon: <Shield className="h-4 w-4" /> }
  ];

  const faqs = [
    {
      question: "How does Premium Membership help me find matches faster?",
      answer: "Premium members get direct access to verified contact numbers, unlimited direct messaging, and priority ranking in search results, increasing profile views by up to 10x."
    },
    {
      question: "Are payments 100% safe and secure?",
      answer: "Yes, all transactions are processed through Razorpay using industry-standard 256-bit SSL encryption. We never store your card or bank account credentials."
    },
    {
      question: "Can I renew or extend my membership before it expires?",
      answer: "Yes! When you renew or upgrade, your new plan duration is seamlessly added to your account without losing any remaining days on your current plan."
    },
    {
      question: "What happens when my Premium plan expires?",
      answer: "Your account simply reverts to the standard free tier. All your shortlisted profiles and conversation history remain saved safely in your account."
    }
  ];

  if (loading) {
    return (
      <div className="min-h-[70vh] flex flex-col items-center justify-center gap-4">
        <RefreshCw className="h-10 w-10 text-primary animate-spin" />
        <p className="text-muted-foreground font-medium">Loading membership plans...</p>
      </div>
    );
  }

  const isUserPremium = currentSubscription && currentSubscription.status === "ACTIVE" && Boolean(currentSubscription.isActive);
  const daysLeft = isUserPremium ? calculateDaysRemaining(currentSubscription.endDate) : 0;

  return (
    <div className="min-h-screen bg-muted/30 pb-20 font-sans">
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-br from-rose-900 via-rose-800 to-amber-900 text-white pt-14 pb-20 shadow-xl">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
          <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
            
            {/* Left Copy */}
            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5 }}
              className="lg:col-span-7 text-center lg:text-left space-y-6"
            >
              <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-white/10 backdrop-blur-md border border-white/20 text-rose-200 text-xs font-semibold tracking-wide uppercase shadow-xs">
                <Crown className="h-4 w-4 text-amber-300 fill-amber-300" />
                Premium Matrimonial Membership
              </div>

              <h1 className="text-3xl sm:text-4xl lg:text-5xl font-extrabold tracking-tight text-white leading-tight">
                Find Your Perfect Match Faster
              </h1>

              <p className="text-base sm:text-lg text-rose-100/90 max-w-2xl leading-relaxed">
                Connect directly with serious, verified profiles. Unlock direct phone numbers, unlimited personalized messages, and priority matchmaking visibility.
              </p>

              <div className="flex flex-wrap items-center justify-center lg:justify-start gap-4 pt-2 text-xs sm:text-sm text-rose-200">
                <div className="flex items-center gap-1.5">
                  <Check className="h-4 w-4 text-amber-400 font-bold" />
                  <span>100% Verified Profiles</span>
                </div>
                <div className="flex items-center gap-1.5">
                  <Check className="h-4 w-4 text-amber-400 font-bold" />
                  <span>Direct Contact Access</span>
                </div>
                <div className="flex items-center gap-1.5">
                  <Check className="h-4 w-4 text-amber-400 font-bold" />
                  <span>Bank-Grade Payment Security</span>
                </div>
              </div>
            </motion.div>

            {/* Right Hero Image */}
            <motion.div 
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.6, delay: 0.1 }}
              className="lg:col-span-5 flex justify-center"
            >
              <div className="relative w-full max-w-sm">
                <div className="absolute -inset-1.5 bg-gradient-to-r from-amber-400 to-rose-500 rounded-3xl blur-lg opacity-40 animate-pulse"></div>
                <div className="relative rounded-2xl overflow-hidden border-2 border-white/30 shadow-2xl bg-rose-950/40 backdrop-blur-xs">
                  <img
                    src={heroCoupleImg}
                    alt="Happy Matrimonial Couple"
                    className="w-full h-auto object-cover object-center transform hover:scale-105 transition-transform duration-500"
                    loading="lazy"
                  />
                  <div className="absolute bottom-0 inset-x-0 bg-gradient-to-t from-black/80 via-black/40 to-transparent p-4 text-center">
                    <p className="text-xs font-semibold text-amber-300">Gathbandhan Matrimony</p>
                    <p className="text-[11px] text-white/90">Over 50,000+ Verified Success Stories</p>
                  </div>
                </div>
              </div>
            </motion.div>

          </div>
        </div>
      </section>

      {/* Active Subscription Banner (If User Has Active Plan) */}
      {isUserPremium && (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 -mt-8 relative z-20">
          <motion.div 
            initial={{ opacity: 0, y: 15 }}
            animate={{ opacity: 1, y: 0 }}
            className="bg-card rounded-2xl shadow-lg border border-amber-500/30 p-6 flex flex-col md:flex-row items-center justify-between gap-6"
          >
            <div className="flex items-center gap-4 text-left">
              <div className="w-14 h-14 rounded-2xl bg-amber-500/10 text-amber-600 flex items-center justify-center shrink-0 shadow-inner">
                <Crown className="h-8 w-8 fill-amber-500" />
              </div>
              <div>
                <div className="flex items-center gap-2">
                  <h2 className="text-xl font-bold text-foreground">{currentSubscription.planName}</h2>
                  <span className="px-2.5 py-0.5 rounded-full text-xs font-bold bg-emerald-100 dark:bg-emerald-950/50 text-emerald-700 dark:text-emerald-400 border border-emerald-300">
                    ACTIVE
                  </span>
                </div>
                <div className="flex flex-wrap items-center gap-x-4 gap-y-1 text-sm text-muted-foreground mt-1">
                  <span className="flex items-center gap-1">
                    <Calendar className="h-4 w-4 text-muted-foreground" />
                    Valid till: {formatDate(currentSubscription.endDate)}
                  </span>
                  <span className="flex items-center gap-1 font-semibold text-amber-600 dark:text-amber-400">
                    <Clock className="h-4 w-4" />
                    {daysLeft} Day{daysLeft !== 1 ? "s" : ""} Remaining
                  </span>
                </div>
              </div>
            </div>

            <Button
              onClick={() => navigate("/subscription-history")}
              variant="outline"
              className="cursor-pointer border-amber-500/40 hover:bg-amber-50 dark:hover:bg-amber-950/30"
            >
              View Membership History
            </Button>
          </motion.div>
        </div>
      )}

      {/* Plan Cards Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-14">
        <div className="text-center max-w-3xl mx-auto mb-12">
          <h2 className="text-3xl font-extrabold text-foreground tracking-tight">
            Choose Your Membership Plan
          </h2>
          <p className="text-muted-foreground text-sm sm:text-base mt-2">
            Select the duration that best suits your matchmaking timeline. All plans include full premium access.
          </p>
        </div>

        {/* Plans Grid */}
        {plans.length === 0 ? (
          <div className="text-center py-12 bg-card rounded-2xl border border-border p-8">
            <AlertCircle className="h-10 w-10 text-muted-foreground mx-auto mb-3" />
            <p className="text-foreground font-semibold">No active subscription plans available at the moment.</p>
            <p className="text-sm text-muted-foreground mt-1">Please check back soon or contact support.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 items-stretch">
            {plans
              .filter(p => p.price > 0) // Filter to paid tiers
              .map((plan, index) => {
                const durationMonths = Math.max(1, Math.round(plan.duration / 30));
                const monthlyPrice = Math.round(plan.price / durationMonths);
                const isPopular = plan.duration === 180 || (plan.name && plan.name.includes("6"));
                const isBestValue = plan.duration === 365 || (plan.name && plan.name.includes("12"));

                return (
                  <motion.div
                    key={plan.id}
                    initial={{ opacity: 0, y: 20 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true }}
                    transition={{ delay: index * 0.1 }}
                    className={`relative bg-card rounded-3xl border-2 flex flex-col justify-between p-8 transition-all duration-300 hover:shadow-2xl ${
                      isPopular 
                        ? "border-rose-600 shadow-xl scale-100 md:-translate-y-2 ring-4 ring-rose-500/10" 
                        : "border-border hover:border-rose-400/60 shadow-md"
                    }`}
                  >
                    {/* Badge */}
                    {isPopular && (
                      <div className="absolute -top-3.5 left-1/2 -translate-x-1/2 bg-gradient-to-r from-rose-600 to-amber-600 text-white text-xs font-extrabold px-4 py-1 rounded-full uppercase tracking-wider shadow-md">
                        Most Popular
                      </div>
                    )}
                    {isBestValue && !isPopular && (
                      <div className="absolute -top-3.5 left-1/2 -translate-x-1/2 bg-gradient-to-r from-amber-600 to-amber-700 text-white text-xs font-extrabold px-4 py-1 rounded-full uppercase tracking-wider shadow-md">
                        Best Value
                      </div>
                    )}

                    <div>
                      {/* Title & Duration */}
                      <div className="text-center pb-6 border-b border-border/80">
                        <h3 className="text-2xl font-bold text-foreground">{plan.name}</h3>
                        <p className="text-sm text-muted-foreground mt-1">
                          Full Access for {plan.duration} Days ({durationMonths} Month{durationMonths > 1 ? "s" : ""})
                        </p>

                        {/* Price */}
                        <div className="mt-5 flex items-baseline justify-center gap-1">
                          <span className="text-4xl sm:text-5xl font-extrabold text-foreground tracking-tight">
                            ₹{plan.price.toLocaleString("en-IN")}
                          </span>
                        </div>
                        <p className="text-xs text-rose-600 dark:text-rose-400 font-semibold mt-1">
                          Just ₹{monthlyPrice.toLocaleString("en-IN")}/month
                        </p>
                      </div>

                      {/* Benefits Checklist */}
                      <div className="py-6 space-y-3.5">
                        <p className="text-xs font-bold uppercase tracking-wider text-muted-foreground">
                          Included Privileges:
                        </p>
                        
                        <div className="flex items-start gap-3">
                          <div className="w-5 h-5 rounded-full bg-rose-100 dark:bg-rose-950 text-rose-600 flex items-center justify-center shrink-0 mt-0.5">
                            <Check className="h-3.5 w-3.5 font-bold" />
                          </div>
                          <span className="text-sm text-foreground">Direct Access to Verified Phone Numbers</span>
                        </div>

                        <div className="flex items-start gap-3">
                          <div className="w-5 h-5 rounded-full bg-rose-100 dark:bg-rose-950 text-rose-600 flex items-center justify-center shrink-0 mt-0.5">
                            <Check className="h-3.5 w-3.5 font-bold" />
                          </div>
                          <span className="text-sm text-foreground">Unlimited Direct Chat & Messaging</span>
                        </div>

                        <div className="flex items-start gap-3">
                          <div className="w-5 h-5 rounded-full bg-rose-100 dark:bg-rose-950 text-rose-600 flex items-center justify-center shrink-0 mt-0.5">
                            <Check className="h-3.5 w-3.5 font-bold" />
                          </div>
                          <span className="text-sm text-foreground">Priority Profile Placement in Searches</span>
                        </div>

                        <div className="flex items-start gap-3">
                          <div className="w-5 h-5 rounded-full bg-rose-100 dark:bg-rose-950 text-rose-600 flex items-center justify-center shrink-0 mt-0.5">
                            <Check className="h-3.5 w-3.5 font-bold" />
                          </div>
                          <span className="text-sm text-foreground">View Contact Details of Profile Visitors</span>
                        </div>

                        <div className="flex items-start gap-3">
                          <div className="w-5 h-5 rounded-full bg-rose-100 dark:bg-rose-950 text-rose-600 flex items-center justify-center shrink-0 mt-0.5">
                            <Check className="h-3.5 w-3.5 font-bold" />
                          </div>
                          <span className="text-sm text-foreground">Dedicated Relationship Advisor Support</span>
                        </div>
                      </div>
                    </div>

                    {/* CTA Button */}
                    <div className="pt-6 border-t border-border/80">
                      <Button
                        onClick={() => handleSubscribe(plan)}
                        disabled={processingPlanId === plan.id}
                        className={`w-full py-6 text-base font-bold rounded-2xl transition-all duration-200 cursor-pointer shadow-md flex items-center justify-center gap-2 ${
                          isPopular
                            ? "bg-rose-600 hover:bg-rose-700 text-white hover:shadow-rose-600/30"
                            : "bg-primary hover:bg-primary/90 text-primary-foreground"
                        }`}
                      >
                        {processingPlanId === plan.id ? (
                          <>
                            <RefreshCw className="h-5 w-5 animate-spin" />
                            <span>Processing...</span>
                          </>
                        ) : isUserPremium ? (
                          <>
                            <span>Extend / Renew Plan</span>
                            <ArrowRight className="h-4 w-4" />
                          </>
                        ) : (
                          <>
                            <span>Choose {plan.name}</span>
                            <ArrowRight className="h-4 w-4" />
                          </>
                        )}
                      </Button>
                      <p className="text-[11px] text-center text-muted-foreground mt-2">
                        Instant Activation • 100% Encrypted Payment
                      </p>
                    </div>

                  </motion.div>
                );
              })}
          </div>
        )}
      </section>

      {/* Free vs Premium Features Table */}
      <section className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 mt-20">
        <div className="text-center mb-10">
          <h2 className="text-2xl sm:text-3xl font-bold text-foreground">
            Free vs. Premium Comparison
          </h2>
          <p className="text-sm text-muted-foreground mt-1">
            See how Premium features accelerate your partner search.
          </p>
        </div>

        <div className="bg-card rounded-2xl border border-border shadow-md overflow-hidden">
          <div className="grid grid-cols-12 bg-muted/60 p-4 font-bold text-sm text-foreground border-b border-border">
            <div className="col-span-7 sm:col-span-6">Features & Privileges</div>
            <div className="col-span-2 sm:col-span-3 text-center text-muted-foreground">Free Tier</div>
            <div className="col-span-3 text-center text-rose-600 dark:text-rose-400">Premium</div>
          </div>

          <div className="divide-y divide-border">
            {featuresComparison.map((item, idx) => (
              <div key={idx} className="grid grid-cols-12 p-4 items-center text-xs sm:text-sm hover:bg-muted/20 transition-colors">
                <div className="col-span-7 sm:col-span-6 flex items-center gap-3 text-foreground font-medium">
                  <span className="text-muted-foreground shrink-0">{item.icon}</span>
                  <span>{item.feature}</span>
                </div>
                <div className="col-span-2 sm:col-span-3 text-center">
                  {typeof item.free === "boolean" ? (
                    item.free ? (
                      <Check className="h-4 w-4 text-emerald-600 mx-auto" />
                    ) : (
                      <X className="h-4 w-4 text-muted-foreground/60 mx-auto" />
                    )
                  ) : (
                    <span className="text-muted-foreground">{item.free}</span>
                  )}
                </div>
                <div className="col-span-3 text-center font-semibold text-rose-600 dark:text-rose-400">
                  {typeof item.premium === "boolean" ? (
                    item.premium ? (
                      <Check className="h-4 w-4 text-rose-600 dark:text-rose-400 font-bold mx-auto" />
                    ) : (
                      <X className="h-4 w-4 text-muted-foreground mx-auto" />
                    )
                  ) : (
                    <span>{item.premium}</span>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* FAQs */}
      <section className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 mt-20">
        <div className="text-center mb-8">
          <h2 className="text-2xl font-bold text-foreground">Frequently Asked Questions</h2>
          <p className="text-sm text-muted-foreground mt-1">Clear answers to common questions about Gathbandhan Premium.</p>
        </div>

        <div className="space-y-3">
          {faqs.map((faq, idx) => {
            const isOpen = expandedFAQ === idx;
            return (
              <div key={idx} className="bg-card rounded-2xl border border-border shadow-xs overflow-hidden">
                <button
                  type="button"
                  onClick={() => setExpandedFAQ(isOpen ? null : idx)}
                  className="w-full p-5 text-left flex items-center justify-between gap-4 font-semibold text-foreground hover:text-rose-600 transition-colors cursor-pointer"
                >
                  <span>{faq.question}</span>
                  <ChevronDown className={`h-5 w-5 text-muted-foreground shrink-0 transition-transform duration-200 ${isOpen ? "rotate-180 text-rose-600" : ""}`} />
                </button>
                <AnimatePresence>
                  {isOpen && (
                    <motion.div
                      initial={{ height: 0, opacity: 0 }}
                      animate={{ height: "auto", opacity: 1 }}
                      exit={{ height: 0, opacity: 0 }}
                      className="px-5 pb-5 text-sm text-muted-foreground leading-relaxed border-t border-border/40 pt-3"
                    >
                      {faq.answer}
                    </motion.div>
                  )}
                </AnimatePresence>
              </div>
            );
          })}
        </div>
      </section>

      {/* Sandbox / Test Payment Modal */}
      <AnimatePresence>
        {showSandboxModal && (
          <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-xs flex items-center justify-center p-4">
            <motion.div
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0, scale: 0.95 }}
              className="bg-card w-full max-w-md rounded-3xl border border-border p-6 shadow-2xl space-y-5"
            >
              <div className="flex items-center justify-between border-b border-border pb-4">
                <div className="flex items-center gap-2 text-amber-600">
                  <Shield className="h-6 w-6" />
                  <h3 className="font-bold text-lg text-foreground">Secure Payment Gateway</h3>
                </div>
                <button 
                  onClick={() => setShowSandboxModal(null)}
                  className="text-muted-foreground hover:text-foreground p-1 rounded-lg cursor-pointer"
                >
                  <X className="h-5 w-5" />
                </button>
              </div>

              <div className="bg-muted/50 rounded-2xl p-4 space-y-2 border border-border">
                <div className="flex justify-between text-sm">
                  <span className="text-muted-foreground">Selected Plan:</span>
                  <span className="font-semibold text-foreground">{showSandboxModal.plan.name}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-muted-foreground">Duration:</span>
                  <span className="font-semibold text-foreground">{showSandboxModal.plan.duration} Days</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-muted-foreground">Amount Payable:</span>
                  <span className="font-extrabold text-rose-600 text-base">₹{showSandboxModal.plan.price.toLocaleString("en-IN")}</span>
                </div>
                <div className="flex justify-between text-xs pt-1 border-t border-border/80">
                  <span className="text-muted-foreground">Order ID:</span>
                  <span className="font-mono text-muted-foreground">{showSandboxModal.orderData.orderId}</span>
                </div>
              </div>

              <div className="p-3 bg-amber-50 dark:bg-amber-950/40 rounded-xl border border-amber-300 text-xs text-amber-800 dark:text-amber-300">
                ⚡ <strong>Sandbox Mode:</strong> Real Razorpay credentials are in test mode. Clicking below will verify and activate your subscription securely.
              </div>

              <div className="flex gap-3 pt-2">
                <Button
                  onClick={() => setShowSandboxModal(null)}
                  variant="outline"
                  className="w-1/2 py-5 rounded-xl cursor-pointer"
                >
                  Cancel
                </Button>
                <Button
                  onClick={handleCompleteSandboxPayment}
                  disabled={processingPlanId === showSandboxModal.plan.id}
                  className="w-1/2 py-5 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-xl cursor-pointer shadow-md"
                >
                  {processingPlanId === showSandboxModal.plan.id ? (
                    <RefreshCw className="h-4 w-4 animate-spin" />
                  ) : (
                    "Complete Payment"
                  )}
                </Button>
              </div>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default UpgradePremium;
