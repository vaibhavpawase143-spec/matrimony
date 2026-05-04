import { Mail, Phone, MapPin, Send } from "lucide-react";
import Navbar from "@/components/Navbar";
import { useLanguage } from "@/context/LanguageContext";

const Contact = () => {
  const { t } = useLanguage();

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      <div className="py-8 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">{t.contact.title}</h1>
        <p className="text-primary-foreground/70 text-sm">{t.contact.subtitle}</p>
      </div>

      <div className="container mx-auto px-4 py-10">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 max-w-5xl mx-auto">
          {/* Contact Info Cards */}
          <div className="space-y-4">
            {[
              { icon: <Phone className="h-5 w-5" />, title: t.contact.phoneTitle, detail: t.contact.phoneDetail, sub: t.contact.phoneSub },
              { icon: <Mail className="h-5 w-5" />, title: t.contact.emailTitle, detail: t.contact.emailDetail, sub: t.contact.emailSub },
              { icon: <MapPin className="h-5 w-5" />, title: t.contact.officeTitle, detail: t.contact.officeDetail, sub: t.contact.officeSub },
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
            <h2 className="text-lg font-display font-bold text-foreground mb-5">{t.contact.sendMessageTitle}</h2>
            <div className="space-y-4">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">{t.contact.fullName}</label>
                  <input className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder={t.contact.fullNamePlaceholder} />
                </div>
                <div>
                  <label className="text-xs font-medium text-foreground mb-1 block">{t.contact.email}</label>
                  <input type="email" className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder={t.contact.emailPlaceholder} />
                </div>
              </div>
              <div>
                <label className="text-xs font-medium text-foreground mb-1 block">{t.contact.subject}</label>
                <input className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" placeholder={t.contact.subjectPlaceholder} />
              </div>
              <div>
                <label className="text-xs font-medium text-foreground mb-1 block">{t.contact.message}</label>
                <textarea rows={5} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary resize-none" placeholder={t.contact.messagePlaceholder} />
              </div>
              <button className="flex items-center justify-center gap-2 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold px-8 py-2.5 rounded-lg text-sm transition-colors">
                <Send className="h-4 w-4" />
                {t.contact.sendButton}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Contact;
