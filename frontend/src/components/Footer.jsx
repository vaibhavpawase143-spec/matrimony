import { Heart, Mail, Phone, MapPin, Facebook, Twitter, Instagram, Linkedin } from "lucide-react";
import { Link } from "react-router-dom";
import { useLanguage } from "@/context/LanguageContext.jsx";

const Footer = () => {
  const currentYear = new Date().getFullYear();
  const { t } = useLanguage();
  
  return (
    <footer className="bg-card border-t border-border mt-auto">
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-8">
          {/* Brand Section */}
          <div>
            <div className="flex items-center gap-2 mb-4">
              <Heart className="h-6 w-6 text-primary fill-primary" />
              <span className="text-xl font-display font-bold text-foreground">Gathbandhan</span>
            </div>
            <p className="text-sm text-muted-foreground mb-4">
              {t.footer.brandDescription}
            </p>
            <div className="flex space-x-3">
              <a href="#" className="text-muted-foreground hover:text-primary transition-colors">
                <Facebook className="h-5 w-5" />
              </a>
              <a href="#" className="text-muted-foreground hover:text-primary transition-colors">
                <Twitter className="h-5 w-5" />
              </a>
              <a href="#" className="text-muted-foreground hover:text-primary transition-colors">
                <Instagram className="h-5 w-5" />
              </a>
              <a href="#" className="text-muted-foreground hover:text-primary transition-colors">
                <Linkedin className="h-5 w-5" />
              </a>
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="font-semibold text-foreground mb-4">{t.footer.quickLinks}</h4>
            <div className="space-y-2">
              <Link to="/home" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.navbar.home}
              </Link>
              <Link to="/search" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.footer.searchProfiles}
              </Link>
              <Link to="/matches" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.navbar.matches}
              </Link>
              <Link to="/upgrade" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.footer.premiumPlans}
              </Link>
            </div>
          </div>

          {/* Support */}
          <div>
            <h4 className="font-semibold text-foreground mb-4">{t.footer.support}</h4>
            <div className="space-y-2">
              <Link to="/about" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.footer.aboutUs}
              </Link>
              <Link to="/contact" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.footer.contactUs}
              </Link>
              <Link to="/help" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.footer.helpSupport}
              </Link>
              <Link to="/faq" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.footer.faq}
              </Link>
            </div>
          </div>

          {/* Legal & Contact */}
          <div>
            <h4 className="font-semibold text-foreground mb-4">{t.footer.legal}</h4>
            <div className="space-y-2 mb-4">
              <Link to="/privacy-policy" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.footer.privacyPolicy}
              </Link>
              <Link to="/terms" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.footer.terms}
              </Link>
              <Link to="/refund-policy" className="block text-sm text-muted-foreground hover:text-primary transition-colors">
                {t.footer.refundPolicy}
              </Link>
            </div>
            <div className="space-y-2 text-sm text-muted-foreground">
              <div className="flex items-center gap-2">
                <Mail className="h-4 w-4" />
                <span>{t.footer.email}</span>
              </div>
              <div className="flex items-center gap-2">
                <Phone className="h-4 w-4" />
                <span>{t.footer.phone}</span>
              </div>
              <div className="flex items-center gap-2">
                <MapPin className="h-4 w-4" />
                <span>{t.footer.address}</span>
              </div>
            </div>
          </div>
        </div>

        {/* Copyright */}
        <div className="border-t border-border pt-6">
          <div className="flex flex-col md:flex-row justify-between items-center text-sm text-muted-foreground">
            <p>{t.footer.copyright.replace("{year}", currentYear)}</p>
            <div className="flex items-center gap-4 mt-4 md:mt-0">
              <span>{t.footer.madeWithLove}</span>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
