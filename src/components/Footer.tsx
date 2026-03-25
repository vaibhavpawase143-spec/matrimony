import { Heart } from "lucide-react";
import { Link } from "react-router-dom";

const Footer = () => (
  <footer className="bg-foreground text-background py-12">
    <div className="container mx-auto px-4">
      <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
        <div>
          <div className="flex items-center gap-2 mb-3">
            <Heart className="h-6 w-6 text-primary fill-primary" />
            <span className="text-lg font-display font-bold">Gathbandhan</span>
          </div>
          <p className="text-sm text-background/60">India's most trusted matrimony service. Connecting hearts since 2020.</p>
        </div>
        <div>
          <h4 className="font-semibold text-sm mb-3">Quick Links</h4>
          <div className="space-y-2 text-sm text-background/60">
            <Link to="/about" className="block hover:text-background transition-colors">About Us</Link>
            <Link to="/search" className="block hover:text-background transition-colors">Search</Link>
            <Link to="/contact" className="block hover:text-background transition-colors">Contact</Link>
          </div>
        </div>
        <div>
          <h4 className="font-semibold text-sm mb-3">Features</h4>
          <div className="space-y-2 text-sm text-background/60">
            <Link to="/kundli" className="block hover:text-background transition-colors">Kundli Matching</Link>
            <Link to="/register" className="block hover:text-background transition-colors">Register Free</Link>
            <span className="block">Premium Plans</span>
          </div>
        </div>
        <div>
          <h4 className="font-semibold text-sm mb-3">Contact</h4>
          <div className="space-y-2 text-sm text-background/60">
            <p>support@gathbandhan.com</p>
            <p>+91 98765 43210</p>
            <p>Mumbai, India</p>
          </div>
        </div>
      </div>
      <div className="border-t border-background/10 pt-6 text-center text-xs text-background/40">
        © 2026 Gathbandhan Matrimony. All rights reserved.
      </div>
    </div>
  </footer>
);

export default Footer;
