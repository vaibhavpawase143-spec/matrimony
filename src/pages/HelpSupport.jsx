import { Headphones, Mail, Phone, MessageCircle, Clock, MapPin, Users, CheckCircle, AlertCircle } from "lucide-react";
import { motion } from "framer-motion";
import { useState } from "react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

const HelpSupport = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    subject: "",
    message: "",
    category: "general"
  });

  const handleInputChange = (e) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission
    console.log('Support request submitted:', formData);
    alert('Your support request has been submitted. We will respond within 24 hours.');
    setFormData({
      name: "",
      email: "",
      phone: "",
      subject: "",
      message: "",
      category: "general"
    });
  };

  const supportOptions = [
    {
      icon: <Phone className="h-6 w-6" />,
      title: "Phone Support",
      description: "Call us for immediate assistance",
      contact: "+91 8999823244",
      timing: "Mon-Sat, 9AM-6PM",
      color: "bg-blue-100 text-blue-600"
    },
    {
      icon: <Mail className="h-6 w-6" />,
      title: "Email Support",
      description: "Send us detailed queries",
      contact: "support@gathbandhan.com",
      timing: "Response within 24 hours",
      color: "bg-green-100 text-green-600"
    },
    {
      icon: <MessageCircle className="h-6 w-6" />,
      title: "WhatsApp Support",
      description: "Quick chat support",
      contact: "+91 8999823244",
      timing: "Mon-Sat, 9AM-6PM",
      color: "bg-purple-100 text-purple-600"
    },
    {
      icon: <Headphones className="h-6 w-6" />,
      title: "Live Chat",
      description: "Real-time assistance",
      contact: "Available on website",
      timing: "Mon-Sat, 10AM-7PM",
      color: "bg-orange-100 text-orange-600"
    }
  ];

  const commonIssues = [
    {
      icon: <Users className="h-5 w-5" />,
      title: "Profile Issues",
      issues: [
        "Profile not showing in search",
        "Photo upload problems",
        "Verification pending",
        "Edit profile information"
      ]
    },
    {
      icon: <AlertCircle className="h-5 w-5" />,
      title: "Account Problems",
      issues: [
        "Login/Password issues",
        "Account suspension",
        "Delete account request",
        "Subscription problems"
      ]
    },
    {
      icon: <CheckCircle className="h-5 w-5" />,
      title: "Matching & Communication",
      issues: [
        "Not getting matches",
        "Messaging issues",
        "Report fake profile",
        "Contact information access"
      ]
    },
    {
      icon: <Clock className="h-5 w-5" />,
      title: "Payment & Billing",
      issues: [
        "Payment failed",
        "Refund request",
        "Subscription cancellation",
        "Invoice issues"
      ]
    }
  ];

  return (
    <div className="min-h-screen bg-background">
      <Navbar />

      <div className="py-12 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <Headphones className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Help & Support</h1>
        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">We're here to help you find your perfect life partner</p>
      </div>

      <div className="container mx-auto px-4 py-12 max-w-6xl">
        {/* Emergency Support */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-red-50 border border-red-200 rounded-xl p-6 mb-8"
        >
          <div className="flex items-center gap-3 mb-3">
            <AlertCircle className="h-6 w-6 text-red-600" />
            <h2 className="text-xl font-bold text-red-800">Emergency Support</h2>
          </div>
          <p className="text-red-700 mb-4">
            For urgent safety concerns, fake profiles, or harassment issues, please contact us immediately:
          </p>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="flex items-center gap-2">
              <Phone className="h-4 w-4 text-red-600" />
              <span className="text-red-700 font-medium">Emergency: +91 8999823244</span>
            </div>
            <div className="flex items-center gap-2">
              <Mail className="h-4 w-4 text-red-600" />
              <span className="text-red-700 font-medium">urgent@gathbandhan.com</span>
            </div>
          </div>
        </motion.div>

        {/* Contact Options */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.1 }}
          className="mb-12"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-8 text-center">How Can We Help You?</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {supportOptions.map((option, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ delay: index * 0.1 }}
                className="bg-card rounded-xl border border-border p-6 hover:shadow-lg transition-shadow"
              >
                <div className={`w-12 h-12 rounded-lg flex items-center justify-center mb-4 ${option.color}`}>
                  {option.icon}
                </div>
                <h3 className="font-semibold text-foreground mb-2">{option.title}</h3>
                <p className="text-sm text-muted-foreground mb-4">{option.description}</p>
                <div className="space-y-2">
                  <p className="text-sm font-medium text-foreground">{option.contact}</p>
                  <div className="flex items-center gap-1 text-xs text-muted-foreground">
                    <Clock className="h-3 w-3" />
                    <span>{option.timing}</span>
                  </div>
                </div>
              </motion.div>
            ))}
          </div>
        </motion.div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Common Issues */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: 0.2 }}
            className="lg:col-span-1"
          >
            <h2 className="text-xl font-display font-bold text-foreground mb-6">Common Issues</h2>
            <div className="space-y-4">
              {commonIssues.map((category, index) => (
                <div key={index} className="bg-card rounded-xl border border-border p-4">
                  <div className="flex items-center gap-2 mb-3">
                    <div className="w-8 h-8 rounded-lg bg-primary/10 text-primary flex items-center justify-center">
                      {category.icon}
                    </div>
                    <h3 className="font-semibold text-foreground">{category.title}</h3>
                  </div>
                  <ul className="space-y-2">
                    {category.issues.map((issue, issueIndex) => (
                      <li key={issueIndex} className="text-sm text-muted-foreground flex items-center gap-2">
                        <div className="w-1.5 h-1.5 rounded-full bg-primary"></div>
                        {issue}
                      </li>
                    ))}
                  </ul>
                </div>
              ))}
            </div>
          </motion.div>

          {/* Support Form */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: 0.3 }}
            className="lg:col-span-2"
          >
            <h2 className="text-xl font-display font-bold text-foreground mb-6">Send Us a Message</h2>
            <form onSubmit={handleSubmit} className="bg-card rounded-xl border border-border p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Full Name *
                  </label>
                  <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    required
                    className="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="Enter your name"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Email Address *
                  </label>
                  <input
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    required
                    className="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="your@email.com"
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Phone Number
                  </label>
                  <input
                    type="tel"
                    name="phone"
                    value={formData.phone}
                    onChange={handleInputChange}
                    className="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="+91 98765 43210"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Category *
                  </label>
                  <select
                    name="category"
                    value={formData.category}
                    onChange={handleInputChange}
                    required
                    className="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                  >
                    <option value="general">General Query</option>
                    <option value="profile">Profile Issues</option>
                    <option value="payment">Payment Issues</option>
                    <option value="technical">Technical Support</option>
                    <option value="safety">Safety Concerns</option>
                    <option value="feedback">Feedback</option>
                  </select>
                </div>
              </div>

              <div className="mb-4">
                <label className="block text-sm font-medium text-foreground mb-2">
                  Subject *
                </label>
                <input
                  type="text"
                  name="subject"
                  value={formData.subject}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                  placeholder="Brief description of your issue"
                />
              </div>

              <div className="mb-6">
                <label className="block text-sm font-medium text-foreground mb-2">
                  Message *
                </label>
                <textarea
                  name="message"
                  value={formData.message}
                  onChange={handleInputChange}
                  required
                  rows={5}
                  className="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent resize-none"
                  placeholder="Please describe your issue in detail..."
                ></textarea>
              </div>

              <button
                type="submit"
                className="w-full bg-primary hover:bg-primary/90 text-primary-foreground font-semibold py-3 px-6 rounded-lg transition-colors duration-200"
              >
                Send Message
              </button>
            </form>
          </motion.div>
        </div>

        {/* Office Information */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.4 }}
          className="mt-12 bg-card rounded-xl border border-border p-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-6">Visit Our Office</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div>
              <div className="flex items-center gap-3 mb-4">
                <MapPin className="h-5 w-5 text-primary" />
                <h3 className="font-semibold text-foreground">Address</h3>
              </div>
              <p className="text-muted-foreground leading-relaxed">
                Gathbandhan Matrimony Office<br />
                Sangamner College, Sangamner<br />
                Ahmednagar, Maharashtra<br />
                India - 423601
              </p>
              
              <div className="mt-6 space-y-3">
                <div className="flex items-center gap-3">
                  <Clock className="h-5 w-5 text-primary" />
                  <div>
                    <p className="font-semibold text-foreground">Office Hours</p>
                    <p className="text-sm text-muted-foreground">Monday - Saturday: 9:00 AM - 6:00 PM</p>
                    <p className="text-sm text-muted-foreground">Sunday: Closed</p>
                  </div>
                </div>
              </div>
            </div>
            
            <div>
              <div className="flex items-center gap-3 mb-4">
                <CheckCircle className="h-5 w-5 text-primary" />
                <h3 className="font-semibold text-foreground">What to Bring</h3>
              </div>
              <ul className="space-y-2 text-muted-foreground">
                <li className="flex items-center gap-2">
                  <div className="w-1.5 h-1.5 rounded-full bg-primary"></div>
                  Government ID proof for verification
                </li>
                <li className="flex items-center gap-2">
                  <div className="w-1.5 h-1.5 rounded-full bg-primary"></div>
                  Recent photographs for profile
                </li>
                <li className="flex items-center gap-2">
                  <div className="w-1.5 h-1.5 rounded-full bg-primary"></div>
                  Educational and professional documents
                </li>
                <li className="flex items-center gap-2">
                  <div className="w-1.5 h-1.5 rounded-full bg-primary"></div>
                  Any specific questions or concerns
                </li>
              </ul>
            </div>
          </div>
        </motion.div>

        {/* Response Time Information */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.5 }}
          className="mt-8 bg-muted/50 rounded-xl p-6"
        >
          <h3 className="font-semibold text-foreground mb-4">Average Response Times</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
            <div className="text-center">
              <p className="font-medium text-foreground">Phone/WhatsApp</p>
              <p className="text-muted-foreground">Immediate (during business hours)</p>
            </div>
            <div className="text-center">
              <p className="font-medium text-foreground">Email</p>
              <p className="text-muted-foreground">Within 24 hours</p>
            </div>
            <div className="text-center">
              <p className="font-medium text-foreground">Live Chat</p>
              <p className="text-muted-foreground">Within 5 minutes</p>
            </div>
          </div>
        </motion.div>
      </div>

      <Footer />
    </div>
  );
};

export default HelpSupport;
