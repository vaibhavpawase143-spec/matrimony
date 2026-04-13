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
  Gift,
  Zap
} from "lucide-react";
import { useState } from "react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import { Button } from "@/components/ui/button";

const UpgradePremium = () => {
  const [billingToggle, setBillingToggle] = useState("monthly");
  const [expandedFAQ, setExpandedFAQ] = useState(null);

  // Premium plans data
  const premiumPlans = [
    {
      id: 1,
      name: "1 Month Plan",
      duration: "1 Month",
      monthlyPrice: 999,
      totalPrice: 999,
      discount: null,
      badge: null,
      features: [
        "Unlimited profile views",
        "Send unlimited messages",
        "View contact details",
        "Advanced partner filters",
        "Basic profile visibility",
        "Email support"
      ],
      highlighted: false,
      buttonText: "Choose Plan"
    },
    {
      id: 2,
      name: "3 Months Plan",
      duration: "3 Months",
      monthlyPrice: 799,
      totalPrice: 2397,
      discount: "20% OFF",
      badge: "Most Popular",
      features: [
        "Unlimited profile views",
        "Send unlimited messages",
        "View contact details",
        "Advanced partner filters",
        "Enhanced profile visibility",
        "See who viewed your profile",
        "Priority email support",
        "Personalized match recommendations"
      ],
      highlighted: true,
      buttonText: "Choose Plan"
    },
    {
      id: 3,
      name: "6 Months Plan",
      duration: "6 Months",
      monthlyPrice: 599,
      totalPrice: 3594,
      discount: "40% OFF",
      badge: "Best Value",
      features: [
        "Everything in 3 Months",
        "Premium profile visibility",
        "Direct chat support",
        "Verified profile badge",
        "Early access to new features"
      ],
      highlighted: false,
      buttonText: "Choose Plan"
    },
    {
      id: 4,
      name: "12 Months Plan",
      duration: "12 Months",
      monthlyPrice: 499,
      totalPrice: 5988,
      discount: "50% OFF",
      badge: null,
      features: [
        "Everything in 6 Months",
        "Maximum profile visibility",
        "Dedicated relationship manager",
        "Exclusive premium events access",
        "Money-back guarantee"
      ],
      highlighted: false,
      buttonText: "Choose Plan"
    }
  ];

  // Features comparison data
  const featuresComparison = [
    {
      feature: "Unlimited Profile Views",
      free: false,
      premium: true,
      icon: <Eye className="h-4 w-4" />
    },
    {
      feature: "Unlimited Messages",
      free: false,
      premium: true,
      icon: <MessageCircle className="h-4 w-4" />
    },
    {
      feature: "View Contact Details",
      free: false,
      premium: true,
      icon: <Phone className="h-4 w-4" />
    },
    {
      feature: "Advanced Filters",
      free: false,
      premium: true,
      icon: <Filter className="h-4 w-4" />
    },
    {
      feature: "Profile Visibility",
      free: "Basic",
      premium: "Priority",
      icon: <Star className="h-4 w-4" />
    },
    {
      feature: "Customer Support",
      free: "Email",
      premium: "Priority",
      icon: <Headphones className="h-4 w-4" />
    },
    {
      feature: "See Profile Viewers",
      free: false,
      premium: true,
      icon: <Users className="h-4 w-4" />
    },
    {
      feature: "Verified Badge",
      free: false,
      premium: true,
      icon: <Shield className="h-4 w-4" />
    }
  ];

  // Premium benefits
  const premiumBenefits = [
    {
      icon: <Heart className="h-8 w-8 text-primary" />,
      title: "Find Your Perfect Match Faster",
      description: "Get access to advanced filters and unlimited messaging to connect with compatible partners"
    },
    {
      icon: <Star className="h-8 w-8 text-primary" />,
      title: "Stand Out from the Crowd",
      description: "Premium visibility and verified badge help you get noticed by serious candidates"
    },
    {
      icon: <Shield className="h-8 w-8 text-primary" />,
      title: "Safe & Secure Dating",
      description: "Enhanced privacy controls and priority support for a worry-free experience"
    },
    {
      icon: <Zap className="h-8 w-8 text-primary" />,
      title: "Exclusive Features",
      description: "Early access to new features and personalized match recommendations"
    }
  ];

  // Testimonials data
  const testimonials = [
    {
      name: "Priya & Rahul",
      location: "Mumbai",
      story: "We found each other within 2 weeks of upgrading to Premium. The advanced filters helped us find exactly what we were looking for!",
      avatar: "PR",
      rating: 5
    },
    {
      name: "Anjali & Vikram",
      location: "Delhi", 
      story: "Premium membership was worth every penny. The unlimited messaging and contact details made communication so much easier.",
      avatar: "AV",
      rating: 5
    },
    {
      name: "Kavita & Amit",
      location: "Bangalore",
      story: "The verified badge and priority visibility really helped us connect with genuine profiles. Thank you Gathbandhan!",
      avatar: "KA",
      rating: 5
    }
  ];

  // FAQ data
  const faqs = [
    {
      question: "What payment methods do you accept?",
      answer: "We accept all major credit/debit cards, UPI, net banking, and popular digital wallets. All payments are secure and encrypted."
    },
    {
      question: "Can I cancel my premium membership anytime?",
      answer: "Yes, you can cancel your premium membership anytime. Your benefits will continue until the end of your current billing period."
    },
    {
      question: "Is there a money-back guarantee?",
      answer: "We offer a 7-day money-back guarantee for our 6-month and 12-month plans. If you're not satisfied, we'll refund your payment."
    },
    {
      question: "What happens after my premium membership expires?",
      answer: "You'll revert to our free membership with access to basic features. Your premium features will be disabled until you renew."
    },
    {
      question: "Can I change my plan later?",
      answer: "Yes, you can upgrade or downgrade your plan at any time. The new pricing will apply from your next billing cycle."
    },
    {
      question: "Is my personal information secure?",
      answer: "Absolutely! We use bank-level encryption to protect your data. Your privacy and security are our top priorities."
    }
  ];

  const toggleFAQ = (index) => {
    setExpandedFAQ(expandedFAQ === index ? null : index);
  };

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />
      
      {/* Hero Section */}
      <section className="relative overflow-hidden" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        {/* Floating hearts */}
        <div className="absolute inset-0 overflow-hidden">
          <Heart className="absolute top-12 left-[10%] h-5 w-5 text-pink-soft fill-pink-soft opacity-40 animate-float-heart" />
          <Heart className="absolute top-24 right-[20%] h-4 w-4 text-pink-soft fill-pink-soft opacity-30 animate-float-heart [animation-delay:1s]" />
          <Heart className="absolute bottom-20 left-[30%] h-6 w-6 text-pink-soft fill-pink-soft opacity-30 animate-float-heart [animation-delay:0.5s]" />
          <Heart className="absolute top-32 right-[40%] h-3 w-3 text-pink-soft fill-pink-soft opacity-20 animate-float-heart [animation-delay:1.5s]" />
        </div>

        <div className="container mx-auto px-4 py-16 md:py-20 text-center relative z-10">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
          >
            <div className="flex items-center justify-center gap-2 mb-4">
              <Crown className="h-8 w-8 text-primary-foreground" />
              <span className="bg-primary/20 text-primary-foreground px-3 py-1 rounded-full text-sm font-semibold">
                PREMIUM MEMBERSHIP
              </span>
            </div>
            
            <h1 className="text-4xl md:text-5xl lg:text-6xl font-display font-bold text-primary-foreground leading-tight mb-6">
              Upgrade to Premium
            </h1>
            
            <p className="text-lg md:text-xl text-primary-foreground/80 max-w-2xl mx-auto mb-8">
              Unlock exclusive features and find your perfect life partner faster with premium benefits designed for serious matrimony seekers
            </p>

            <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
              <Button size="lg" className="bg-primary hover:bg-primary/90 text-primary-foreground px-8 py-3 text-lg font-semibold">
                <Sparkles className="h-5 w-5 mr-2" />
                Start Your Premium Journey
              </Button>
              <div className="flex items-center gap-2 text-primary-foreground/80">
                <Shield className="h-5 w-5" />
                <span className="text-sm">100% Secure Payment</span>
              </div>
            </div>
          </motion.div>
        </div>
      </section>

      {/* Premium Plans Section */}
      <section className="py-16 md:py-20">
        <div className="container mx-auto px-4">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-center mb-12"
          >
            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
              Choose Your Perfect Plan
            </h2>
            <p className="text-muted-foreground text-lg max-w-2xl mx-auto">
              Select the plan that works best for your journey to find love
            </p>
          </motion.div>

          {/* Billing Toggle */}
          <div className="flex justify-center mb-12">
            <div className="bg-muted rounded-lg p-1 inline-flex">
              <button
                onClick={() => setBillingToggle("monthly")}
                className={`px-6 py-2 rounded-md text-sm font-medium transition-all ${
                  billingToggle === "monthly"
                    ? "bg-background text-foreground shadow-sm"
                    : "text-muted-foreground hover:text-foreground"
                }`}
              >
                Monthly Billing
              </button>
              <button
                onClick={() => setBillingToggle("annual")}
                className={`px-6 py-2 rounded-md text-sm font-medium transition-all flex items-center gap-2 ${
                  billingToggle === "annual"
                    ? "bg-background text-foreground shadow-sm"
                    : "text-muted-foreground hover:text-foreground"
                }`}
              >
                Annual Billing
                <Gift className="h-4 w-4" />
                <span className="bg-primary/20 text-primary px-2 py-0.5 rounded text-xs">Save 50%</span>
              </button>
            </div>
          </div>

          {/* Plans Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 max-w-7xl mx-auto">
            {premiumPlans.map((plan, index) => (
              <motion.div
                key={plan.id}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ delay: index * 0.1 }}
                className={`relative rounded-2xl border-2 p-6 transition-all hover:shadow-xl ${
                  plan.highlighted
                    ? "border-primary bg-primary/5 shadow-lg scale-105"
                    : "border-border bg-card hover:border-primary/50"
                }`}
              >
                {/* Badge */}
                {plan.badge && (
                  <div className="absolute -top-3 left-1/2 transform -translate-x-1/2">
                    <span className="bg-primary text-primary-foreground px-3 py-1 rounded-full text-xs font-semibold">
                      {plan.badge}
                    </span>
                  </div>
                )}

                {/* Discount Badge */}
                {plan.discount && (
                  <div className="absolute top-4 right-4">
                    <span className="bg-destructive/10 text-destructive px-2 py-1 rounded text-xs font-semibold">
                      {plan.discount}
                    </span>
                  </div>
                )}

                <div className="text-center mb-6">
                  <h3 className="text-xl font-bold text-foreground mb-2">{plan.name}</h3>
                  <div className="mb-4">
                    <div className="text-3xl font-bold text-foreground">
                      ₹{plan.monthlyPrice}
                      <span className="text-sm text-muted-foreground font-normal">/month</span>
                    </div>
                    <div className="text-sm text-muted-foreground">
                      Total: ₹{plan.totalPrice}
                    </div>
                  </div>
                </div>

                <div className="space-y-3 mb-6">
                  {plan.features.map((feature, idx) => (
                    <div key={idx} className="flex items-start gap-2">
                      <Check className="h-4 w-4 text-primary flex-shrink-0 mt-0.5" />
                      <span className="text-sm text-foreground">{feature}</span>
                    </div>
                  ))}
                </div>

                <Button 
                  className={`w-full ${
                    plan.highlighted 
                      ? "bg-primary hover:bg-primary/90 text-primary-foreground" 
                      : "bg-muted hover:bg-muted/80 text-foreground"
                  }`}
                >
                  {plan.buttonText}
                  <ArrowRight className="h-4 w-4 ml-2" />
                </Button>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* Features Comparison Section */}
      <section className="py-16 md:py-20 bg-background">
        <div className="container mx-auto px-4">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-center mb-12"
          >
            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
              Free vs Premium Comparison
            </h2>
            <p className="text-muted-foreground text-lg max-w-2xl mx-auto">
              See how Premium membership can transform your matrimony experience
            </p>
          </motion.div>

          <div className="max-w-4xl mx-auto">
            <div className="bg-card rounded-2xl border border-border overflow-hidden">
              <div className="grid grid-cols-3 border-b border-border">
                <div className="p-4 font-semibold text-foreground">Features</div>
                <div className="p-4 font-semibold text-center text-muted-foreground">Free</div>
                <div className="p-4 font-semibold text-center text-primary">Premium</div>
              </div>
              
              {featuresComparison.map((item, index) => (
                <div key={index} className="grid grid-cols-3 border-b border-border last:border-b-0">
                  <div className="p-4 flex items-center gap-3">
                    <div className="text-muted-foreground">{item.icon}</div>
                    <span className="text-sm text-foreground">{item.feature}</span>
                  </div>
                  <div className="p-4 text-center">
                    {typeof item.free === "boolean" ? (
                      item.free ? (
                        <Check className="h-5 w-5 text-primary mx-auto" />
                      ) : (
                        <X className="h-5 w-5 text-muted-foreground mx-auto" />
                      )
                    ) : (
                      <span className="text-sm text-muted-foreground">{item.free}</span>
                    )}
                  </div>
                  <div className="p-4 text-center">
                    {typeof item.premium === "boolean" ? (
                      item.premium ? (
                        <Check className="h-5 w-5 text-primary mx-auto" />
                      ) : (
                        <X className="h-5 w-5 text-muted-foreground mx-auto" />
                      )
                    ) : (
                      <span className="text-sm text-primary font-medium">{item.premium}</span>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* Why Choose Premium Section */}
      <section className="py-16 md:py-20">
        <div className="container mx-auto px-4">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-center mb-12"
          >
            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
              Why Choose Premium?
            </h2>
            <p className="text-muted-foreground text-lg max-w-2xl mx-auto">
              Discover the benefits that make Premium membership worth every penny
            </p>
          </motion.div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {premiumBenefits.map((benefit, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ delay: index * 0.1 }}
                className="text-center"
              >
                <div className="bg-primary/10 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                  {benefit.icon}
                </div>
                <h3 className="text-lg font-semibold text-foreground mb-3">{benefit.title}</h3>
                <p className="text-muted-foreground text-sm">{benefit.description}</p>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* Testimonials Section */}
      <section className="py-16 md:py-20 bg-background">
        <div className="container mx-auto px-4">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-center mb-12"
          >
            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
              Success Stories
            </h2>
            <p className="text-muted-foreground text-lg max-w-2xl mx-auto">
              Real couples who found love through Gathbandhan Premium
            </p>
          </motion.div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-6xl mx-auto">
            {testimonials.map((testimonial, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ delay: index * 0.1 }}
                className="bg-card rounded-2xl border border-border p-6"
              >
                <div className="flex items-center gap-1 mb-4">
                  {[...Array(testimonial.rating)].map((_, i) => (
                    <Star key={i} className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                  ))}
                </div>
                
                <p className="text-muted-foreground mb-4 text-sm italic">
                  "{testimonial.story}"
                </p>
                
                <div className="flex items-center gap-3">
                  <div className="w-12 h-12 bg-primary/20 rounded-full flex items-center justify-center text-primary font-semibold">
                    {testimonial.avatar}
                  </div>
                  <div>
                    <div className="font-semibold text-foreground">{testimonial.name}</div>
                    <div className="text-sm text-muted-foreground">{testimonial.location}</div>
                  </div>
                </div>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* FAQ Section */}
      <section className="py-16 md:py-20">
        <div className="container mx-auto px-4">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-center mb-12"
          >
            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
              Frequently Asked Questions
            </h2>
            <p className="text-muted-foreground text-lg max-w-2xl mx-auto">
              Everything you need to know about Premium membership
            </p>
          </motion.div>

          <div className="max-w-3xl mx-auto">
            {faqs.map((faq, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ delay: index * 0.1 }}
                className="mb-4"
              >
                <button
                  onClick={() => toggleFAQ(index)}
                  className="w-full bg-card rounded-xl border border-border p-6 text-left hover:border-primary/50 transition-colors"
                >
                  <div className="flex items-center justify-between">
                    <h3 className="font-semibold text-foreground">{faq.question}</h3>
                    <ChevronDown 
                      className={`h-5 w-5 text-muted-foreground transition-transform ${
                        expandedFAQ === index ? "rotate-180" : ""
                      }`}
                    />
                  </div>
                </button>
                
                {expandedFAQ === index && (
                  <motion.div
                    initial={{ opacity: 0, height: 0 }}
                    animate={{ opacity: 1, height: "auto" }}
                    className="bg-card rounded-xl border border-border border-t-0 p-6 pt-0"
                  >
                    <p className="text-muted-foreground">{faq.answer}</p>
                  </motion.div>
                )}
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 md:py-20 bg-background" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <div className="container mx-auto px-4 text-center">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
          >
            <Crown className="h-16 w-16 text-primary-foreground mx-auto mb-6" />
            <h2 className="text-3xl md:text-4xl font-bold text-primary-foreground mb-4">
              Ready to Find Your Perfect Match?
            </h2>
            <p className="text-lg text-primary-foreground/80 max-w-2xl mx-auto mb-8">
              Join thousands of happy couples who found love through Gathbandhan Premium
            </p>
            
            <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
              <Button size="lg" className="bg-primary hover:bg-primary/90 text-primary-foreground px-8 py-3 text-lg font-semibold">
                <Sparkles className="h-5 w-5 mr-2" />
                Get Premium Now
              </Button>
              <div className="flex items-center gap-4 text-primary-foreground/80 text-sm">
                <div className="flex items-center gap-1">
                  <Shield className="h-4 w-4" />
                  <span>Secure Payment</span>
                </div>
                <div className="flex items-center gap-1">
                  <Gift className="h-4 w-4" />
                  <span>7-Day Guarantee</span>
                </div>
              </div>
            </div>
          </motion.div>
        </div>
      </section>
    </div>
  );
};

export default UpgradePremium;
