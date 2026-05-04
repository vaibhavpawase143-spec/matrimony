import { useParams, Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { Heart, MapPin, GraduationCap, Briefcase, Calendar, ArrowLeft, Star, MessageSquare } from "lucide-react";
import Navbar from "@/components/Navbar";
import { useLanguage } from "@/context/LanguageContext";

const InfoRow = ({ label, value }) => (
  <div className="flex justify-between py-2.5 border-b border-border last:border-0">
    <span className="text-xs text-muted-foreground">{label}</span>
    <span className="text-xs font-medium text-foreground">{value}</span>
  </div>
);

const ProfileDetails = () => {
  const { t } = useLanguage();
  const { id } = useParams();
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    // TODO: fetch profile by ID from backend
  }, [id]);

  if (!profile) {
    return (
      <div className="min-h-screen bg-muted/30 flex items-center justify-center">
        <div className="text-center">
          <p className="text-muted-foreground">Loading profile...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-muted/30">
      <Navbar />

      <div className="container mx-auto px-4 py-8">
        <Link to="/search" className="inline-flex items-center gap-1.5 text-sm text-muted-foreground hover:text-foreground transition-colors mb-6">
          <ArrowLeft className="h-4 w-4" /> {t.profileDetails.backToSearch}
        </Link>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left - Photo & quick actions */}
          <div className="lg:col-span-1 space-y-4">
            <div className="bg-card rounded-xl overflow-hidden border border-border">
              <div className="aspect-[3/4] overflow-hidden">
                <img src={profile.image} alt={profile.name} className="w-full h-full object-cover" />
              </div>
              <div className="p-4 space-y-2">
                <button className="w-full flex items-center justify-center gap-2 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold py-2.5 rounded-lg text-sm transition-colors">
                  <Heart className="h-4 w-4" /> {t.profileDetails.sendInterest}
                </button>
                <button className="w-full flex items-center justify-center gap-2 bg-accent/10 text-accent font-semibold py-2.5 rounded-lg text-sm hover:bg-accent/20 transition-colors">
                  <MessageSquare className="h-4 w-4" /> {t.profileDetails.message}
                </button>
                <button className="w-full flex items-center justify-center gap-2 bg-gold/10 text-gold font-semibold py-2.5 rounded-lg text-sm hover:bg-gold/20 transition-colors">
                  <Star className="h-4 w-4" /> {t.profileDetails.shortlist}
                </button>
              </div>
            </div>
          </div>

          {/* Right - Details */}
          <div className="lg:col-span-2 space-y-5">
            <div className="bg-card rounded-xl border border-border p-6">
              <h1 className="text-2xl font-display font-bold text-foreground">{profile.name}</h1>
              <div className="flex flex-wrap gap-4 mt-2 text-xs text-muted-foreground">
                <span className="flex items-center gap-1"><Calendar className="h-3.5 w-3.5" /> {profile.age} yrs</span>
                <span className="flex items-center gap-1"><MapPin className="h-3.5 w-3.5" /> {profile.city}</span>
                <span className="flex items-center gap-1"><GraduationCap className="h-3.5 w-3.5" /> {profile.education}</span>
                <span className="flex items-center gap-1"><Briefcase className="h-3.5 w-3.5" /> {profile.profession}</span>
              </div>
              <p className="mt-4 text-sm text-muted-foreground leading-relaxed">{profile.about}</p>
            </div>

            <div className="bg-card rounded-xl border border-border p-6">
              <h2 className="text-sm font-semibold text-foreground mb-3">{t.profileDetails.personalDetails}</h2>
              <InfoRow label={t.profileDetails.height} value={profile.height} />
              <InfoRow label={t.profileDetails.religion} value={profile.religion} />
              <InfoRow label={t.profileDetails.caste} value={profile.caste} />
              <InfoRow label={t.profileDetails.motherTongue} value={profile.motherTongue} />
              <InfoRow label={t.profileDetails.education} value={profile.education} />
              <InfoRow label={t.profileDetails.profession} value={profile.profession} />
              <InfoRow label={t.profileDetails.location} value={profile.city} />
            </div>

            <div className="bg-card rounded-xl border border-border p-6">
              <h2 className="text-sm font-semibold text-foreground mb-3">{t.profileDetails.familyDetails}</h2>
              <InfoRow label={t.profileDetails.familyType} value={profile.familyType} />
              <InfoRow label={t.profileDetails.fatherOccupation} value={profile.fatherOccupation} />
              <InfoRow label={t.profileDetails.siblings} value={profile.siblings} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfileDetails;
