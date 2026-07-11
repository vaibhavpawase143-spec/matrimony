import { Heart, MessageSquare, X } from "lucide-react";
import { motion } from "framer-motion";
import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

import { useLanguage } from "@/context/LanguageContext";
import { useToast } from "@/components/Toast";
import { matchAPI } from "@/services/api";
import profile1 from "@/assets/profile1.jpg";

const Matches = () => {
  const { t } = useLanguage();
  const { success, error } = useToast();
  const navigate = useNavigate();
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);

  // Load matches on component mount
  useEffect(() => {
    loadMatches();
  }, []);

  const loadMatches = async () => {
    try {
      setLoading(true);
      // Get profiles that match current user's preferences
      const currentUser = JSON.parse(
        localStorage.getItem("user")
      );

      const userId = Number(
        currentUser.profile.userId
      );

      const response = await matchAPI.getTopMatches(userId, 50);

      const filteredMatches = response.filter(
        (match) => match.matchScore >= 75
      );
    console.log("MATCHES =", filteredMatches);

      setMatches(filteredMatches);
    } catch (err) {
      console.warn('Failed to load matches:', err.message);
      error('Failed to load matches. Please try again.');
      setMatches([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-muted/30">


      <div className="py-8 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <Heart className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">{t.matches.title}</h1>
        <p className="text-primary-foreground/70 text-sm">{t.matches.subtitle}</p>
      </div>

    <div className="container mx-auto px-4 py-8">
      {loading ? (
        <div className="flex items-center justify-center py-12">
          <div className="text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>
            <p className="text-sm text-muted-foreground">Loading matches...</p>
          </div>
        </div>
      ) : matches.length === 0 ? (
        <div className="text-center py-12">
          <div className="text-muted-foreground mb-4">
            <Heart className="h-12 w-12 mx-auto mb-4 opacity-50" />
            <h3 className="text-lg font-semibold text-foreground mb-2">No matches found</h3>
            <p className="text-sm">We couldn't find any matches for you yet. Complete your profile to get better matches!</p>
          </div>
          <button 
            onClick={() => window.location.href = '/settings'}
            className="mt-4 bg-primary text-primary-foreground px-6 py-2 rounded-lg hover:opacity-90 transition"
          >
            Complete Profile
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-2">
          {matches.map((m, i) => (
            <motion.div
              key={m.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: i * 0.1 }}
              className="bg-card rounded-xl border border-border overflow-hidden hover:shadow-lg transition-shadow group"
            >
              <div className="h-40 overflow-hidden relative">
                <img 
                  src={m.profilePhotoUrl || m.imageUrl || profile1} 
                  alt={m.fullName || m.name} 
                  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                  onError={(e) => {
                    e.target.src = profile1; // Fallback to default image
                  }}
                />
                <div className="absolute top-3 right-3 bg-emerald-badge text-primary-foreground text-xs font-bold px-2.5 py-1 rounded-full">
                 {m.matchPercentage}
                </div>
              </div>
              <div className="p-3">
                <h3 className="text-base font-semibold text-foreground">
                  {m.fullName || m.name}, <span className="text-primary">{m.age || '?'}</span>
                </h3>
                <p className="text-xs text-muted-foreground mt-0.5">
                  {m.profession || m.occupation || 'Profession not specified'} · {m.city || 'Location not specified'}
                </p>

                <div className="flex gap-2 mt-4">
                  <Link
                    to={`/profile/${m.profileId || m.id}`}
                    className="flex-1 flex items-center justify-center gap-1.5 bg-primary hover:bg-primary/90 text-primary-foreground text-xs font-semibold py-1.5 rounded-lg transition-colors text-center"
                  >
                     View Profile
                  </Link>
                  <button
                      onClick={() =>
                          navigate(`/match-details/${m.userId}`)
                      }
                      className="flex-1 flex items-center justify-center gap-1.5 bg-accent/10 text-accent text-xs font-semibold py-1.5 rounded-lg hover:bg-accent/20 transition-colors"
                  >

                      View Match Details
                  </button>

                </div>
              </div>
            </motion.div>
          ))}
        </div>
      )}
    </div>
  </div>
  );
};

export default Matches;
