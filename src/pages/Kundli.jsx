import { useState } from "react";
import { Star, Heart, CheckCircle2 } from "lucide-react";
import Navbar from "@/components/Navbar";
import { useLanguage } from "@/context/LanguageContext";

const InputField = ({ label, placeholder, type = "text" }) => (
  <div>
    <label className="text-xs font-medium text-foreground mb-1 block">{label}</label>
    <input type={type} placeholder={placeholder} className="w-full bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" />
  </div>
);

const matchAspects = [
  { name: "Varna", points: 1, maxPoints: 1, matched: true },
  { name: "Vashya", points: 2, maxPoints: 2, matched: true },
  { name: "Tara", points: 3, maxPoints: 3, matched: true },
  { name: "Yoni", points: 4, maxPoints: 4, matched: true },
  { name: "Graha Maitri", points: 4, maxPoints: 5, matched: false },
  { name: "Gana", points: 5, maxPoints: 6, matched: false },
  { name: "Bhakoot", points: 7, maxPoints: 7, matched: true },
  { name: "Nadi", points: 0, maxPoints: 8, matched: false },
];

const totalPoints = matchAspects.reduce((s, a) => s + a.points, 0);
const maxTotal = matchAspects.reduce((s, a) => s + a.maxPoints, 0);
const percentage = Math.round((totalPoints / maxTotal) * 100);

const Kundli = () => {
  const { t } = useLanguage();
  const [showResult, setShowResult] = useState(false);

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      <div className="py-8 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <Star className="inline-block h-8 w-8 text-gold mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">{t.kundli.title}</h1>
        <p className="text-primary-foreground/70 text-sm">{t.kundli.subtitle}</p>
      </div>

      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-4xl mx-auto">
          {/* Boy Details */}
          <div className="bg-card rounded-xl border border-border p-6">
            <h2 className="text-lg font-display font-bold text-foreground mb-4 flex items-center gap-2">
              <span className="h-8 w-8 rounded-full bg-accent/20 text-accent flex items-center justify-center text-sm font-bold">♂</span>
              {t.kundli.boysDetails}
            </h2>
            <div className="space-y-3">
              <InputField label={t.kundli.fullName} placeholder={`${t.kundli.enterName} (${t.kundli.boysDetails})`} />
              <InputField label={t.kundli.dateOfBirth} placeholder="" type="date" />
              <InputField label={t.kundli.timeOfBirth} placeholder="" type="time" />
              <InputField label={t.kundli.placeOfBirth} placeholder={t.kundli.enterCity} />
            </div>
          </div>

          {/* Girl Details */}
          <div className="bg-card rounded-xl border border-border p-6">
            <h2 className="text-lg font-display font-bold text-foreground mb-4 flex items-center gap-2">
              <span className="h-8 w-8 rounded-full bg-primary/20 text-primary flex items-center justify-center text-sm font-bold">♀</span>
              {t.kundli.girlsDetails}
            </h2>
            <div className="space-y-3">
              <InputField label={t.kundli.fullName} placeholder={`${t.kundli.enterName} (${t.kundli.girlsDetails})`} />
              <InputField label={t.kundli.dateOfBirth} placeholder="" type="date" />
              <InputField label={t.kundli.timeOfBirth} placeholder="" type="time" />
              <InputField label={t.kundli.placeOfBirth} placeholder={t.kundli.enterCity} />
            </div>
          </div>
        </div>

        <div className="text-center mt-6">
          <button
            onClick={() => setShowResult(true)}
            className="bg-primary hover:bg-primary/90 text-primary-foreground font-semibold px-10 py-3 rounded-lg text-sm transition-colors shadow-lg"
          >
            {t.kundli.matchButton}
          </button>
        </div>

        {/* Result */}
        {showResult && (
          <div className="max-w-3xl mx-auto mt-8 space-y-6">
            {/* Score circle */}
            <div className="bg-card rounded-xl border border-border p-8 text-center">
              <h2 className="text-lg font-display font-bold text-foreground mb-4">{t.kundli.matchResult}</h2>
              <div className="relative w-32 h-32 mx-auto mb-4">
                <svg className="w-full h-full -rotate-90" viewBox="0 0 120 120">
                  <circle cx="60" cy="60" r="52" fill="none" stroke="hsl(var(--border))" strokeWidth="10" />
                  <circle cx="60" cy="60" r="52" fill="none" stroke="hsl(var(--primary))" strokeWidth="10" strokeDasharray={`${(percentage / 100) * 327} 327`} strokeLinecap="round" />
                </svg>
                <div className="absolute inset-0 flex flex-col items-center justify-center">
                  <span className="text-2xl font-bold text-foreground">{totalPoints}/{maxTotal}</span>
                  <span className="text-xs text-muted-foreground">{percentage}%</span>
                </div>
              </div>
              <div className="flex items-center justify-center gap-2">
                <Heart className="h-5 w-5 text-primary fill-primary" />
                <span className="text-sm font-semibold text-foreground">
                  {percentage >= 70 ? t.kundli.excellentMatch : percentage >= 50 ? t.kundli.goodMatch : t.kundli.averageMatch}
                </span>
              </div>
            </div>

            {/* Detailed breakdown */}
            <div className="bg-card rounded-xl border border-border p-6">
              <h3 className="text-sm font-semibold text-foreground mb-4">{t.kundli.ashtakootDetails}</h3>
              <div className="space-y-0">
                {matchAspects.map((a) => (
                  <div key={a.name} className="flex items-center justify-between py-3 border-b border-border last:border-0">
                    <div className="flex items-center gap-2">
                      <CheckCircle2 className={`h-4 w-4 ${a.matched ? "text-emerald-badge" : "text-muted-foreground/40"}`} />
                      <span className="text-sm text-foreground">{a.name}</span>
                    </div>
                    <span className={`text-sm font-semibold ${a.matched ? "text-emerald-badge" : "text-destructive"}`}>{a.points}/{a.maxPoints}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Kundli;
