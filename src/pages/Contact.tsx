import { Mail, Phone, MapPin, Send } from "lucide-react";
import Navbar from "@/components/Navbar";

const Contact = () => {
  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      <div className="py-8 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Contact Us</h1>
        <p className="text-primary-foreground/70 text-sm">We're here to help you find your perfect match</p>
      </div>

      <div className="container mx-auto px-4 py-10">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 max-w-5xl mx-auto">
          {/* Contact Info Cards */}
          <div className="space-y-4">
            {[
              { icon: <Phone className="h-5 w-5" />, title: "Phone", detail: "+91 98765 43210", sub: "Mon–Sat, 9am–6pm" },
              { icon: <Mail className="h-5 w-5" />, title: "Email", detail: "support@matrimonial.com", sub: "We reply within 24hrs" },
              { icon: <MapPin className="h-5 w-5" />, title: "Office", detail: "123 Wedding Lane, Mumbai", sub: "Maharashtra, India" },
            ].map((c) => (
              <div key={c.title} className="bg-card rounded-xl border border-border p-5 flex items-start gap-4">
                <div className="h-10 w-10 rounded-full bg-primary/10 text-primary flex items-center justify-center flex-shrink-0">
                  {c.icon}
                </div>
                <div>
                  <h3 className="text-sm font-semibold text-foreground">{c.title}</h3>
                  <p className="text-sm text-foreground mt-0.5">{c.detail}</p>
                  <p className="text-xs text-muted-foreground">{c.sub}</p>
                </div>
              </div>
            ))}
          </div>

          {/* Contact Form */}
          <div className="lg:col-span-2 bg-card rounded-xl border border-border p-6">
            <h2 className="text-lg font-display font-bold text-foreground mb-5">Send us a Message</h2>
            <div className="space-y-4">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">Full Name</label>
                  <input className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder="Your name" />
                </div>
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">Email</label>
                  <input type="email" className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder="your@email.com" />
                </div>
              </div>
              <div>
                <label className="text-xs font-medium text-foreground mb-1 block">Subject</label>
                <input className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder="How can we help?" />
              </div>
              <div>
                <label className="text-xs font-medium text-foreground mb-1 block">Message</label>
                <textarea rows={5} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary resize-none" placeholder="Write your message..." />
              </div>
              <button className="flex items-center justify-center gap-2 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold px-8 py-2.5 rounded-lg text-sm transition-colors">
                <Send className="h-4 w-4" />
                Send Message
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Contact;
