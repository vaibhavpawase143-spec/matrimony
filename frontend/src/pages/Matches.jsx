import { Heart, MessageSquare, X } from "lucide-react";
import { motion } from "framer-motion";
import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect, useRef, useCallback } from "react";

import { useLanguage } from "@/context/LanguageContext";
import { useToast } from "@/components/Toast";
import { matchAPI } from "@/services/api";
import profile1 from "@/assets/profile1.jpg";
import { resolveImageUrl } from "@/utils/urlSecurity";

const parseScore = (m) => {
  const raw = m?.matchScore ?? m?.matchPercentage ?? 0;
  if (typeof raw === "string") {
    const parsed = parseFloat(raw.replace(/[^0-9.]/g, ""));
    return isNaN(parsed) ? 0 : parsed;
  }
  return Number(raw) || 0;
};

const Matches = () => {
  const { t } = useLanguage();
  const { success, error } = useToast();
  const navigate = useNavigate();

  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadingMore, setLoadingMore] = useState(false);

  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  const observer = useRef();

  // =========================================================
  // LOAD MATCHES
  // =========================================================
  const loadMatches = useCallback(
    async (pageNumber = 0, append = false) => {
      try {
        if (pageNumber === 0) {
          setLoading(true);
        } else {
          setLoadingMore(true);
        }

        const currentUser = JSON.parse(
          sessionStorage.getItem("user") ||
          localStorage.getItem("user") || "{}"
        );

        const userId = Number(
          currentUser?.userId ||
          currentUser?.id ||
          currentUser?.profile?.userId
        );

        if (!userId) {
          throw new Error("User ID not found");
        }

        console.log(
          "LOADING MATCH PAGE:",
          pageNumber
        );

        // Backend call for top matches
        const response = await matchAPI.getTopMatches(
          userId,
          pageNumber,
          20
        );

        // Safe response handling
        const matchList = Array.isArray(response)
          ? response
          : Array.isArray(response?.data)
            ? response.data
            : Array.isArray(response?.content)
              ? response.content
              : [];

        console.log(
          "RAW MATCHES:",
          matchList.length
        );

        // Top matches from backend (sorted by highest compatibility)
        const validMatches = matchList.filter(
          (match) => match && (match.userId || match.profileId || match.id)
        );

        console.log(
          "MATCHES LOADED:",
          validMatches.length
        );

        if (append) {
          setMatches((previousMatches) => {
            // Prevent duplicate profiles
            const existingIds = new Set(
              previousMatches.map(
                (match) =>
                  match.userId ||
                  match.profileId ||
                  match.id
              )
            );

            const newMatches =
              validMatches.filter(
                (match) =>
                  !existingIds.has(
                    match.userId ||
                    match.profileId ||
                    match.id
                  )
              );

            return [
              ...previousMatches,
              ...newMatches
            ];
          });
        } else {
          setMatches(validMatches);
        }

        /*
          IMPORTANT:

          Backend page मध्ये 20 पेक्षा कमी records आले
          म्हणजे पुढचे candidates नाहीत.

          पण 20 candidates आले आणि त्यातून
          0 profiles 75%+ असले तरी
          hasMore TRUE राहील.
        */
        if (matchList.length < 20) {
          setHasMore(false);
        } else {
          setHasMore(true);
        }

      } catch (err) {
        console.error(
          "Failed to load matches:",
          err
        );

        if (pageNumber === 0) {
          error(
            "Failed to load matches. Please try again."
          );
          setMatches([]);
        }

      } finally {
        setLoading(false);
        setLoadingMore(false);
      }
    },
    [error]
  );


  // =========================================================
  // INITIAL LOAD
  // =========================================================
  useEffect(() => {
    loadMatches(0, false);
  }, [loadMatches]);


  // =========================================================
  // INFINITE SCROLL OBSERVER
  // =========================================================
  const lastMatchRef = useCallback(
    (node) => {

      if (loading || loadingMore) {
        return;
      }

      if (observer.current) {
        observer.current.disconnect();
      }

      observer.current =
        new IntersectionObserver(
          (entries) => {

            if (
              entries[0].isIntersecting &&
              hasMore
            ) {

              const nextPage = page + 1;

              console.log(
                "LOADING NEXT PAGE:",
                nextPage
              );

              setPage(nextPage);

              loadMatches(
                nextPage,
                true
              );
            }
          },
          {
            rootMargin: "300px"
          }
        );

      if (node) {
        observer.current.observe(node);
      }

    },
    [
      loading,
      loadingMore,
      hasMore,
      page,
      loadMatches
    ]
  );


  return (
    <div className="min-h-screen bg-muted/30">

      {/* HEADER */}

      <div
        className="py-8 text-center"
        style={{
          background:
            "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))"
        }}
      >

        <Heart className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />

        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">
          {t.matches.title}
        </h1>

        <p className="text-primary-foreground/70 text-sm">
          {t.matches.subtitle}
        </p>

      </div>


      <div className="container mx-auto px-4 py-8">

        {/* INITIAL LOADING */}

        {loading ? (

          <div className="flex items-center justify-center py-12">

            <div className="text-center">

              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>

              <p className="text-sm text-muted-foreground">
                Loading matches...
              </p>

            </div>

          </div>

        ) : matches.length === 0 && !hasMore ? (

          /* NO MATCHES */

          <div className="text-center py-12">

            <div className="text-muted-foreground mb-4">

              <Heart className="h-12 w-12 mx-auto mb-4 opacity-50" />

              <h3 className="text-lg font-semibold text-foreground mb-2">
                No matches found
              </h3>

              <p className="text-sm">
                We couldn't find any matches for you yet.
                Complete your profile to get better matches!
              </p>

            </div>

            <button
              onClick={() =>
                window.location.href = "/settings"
              }
              className="mt-4 bg-primary text-primary-foreground px-6 py-2 rounded-lg hover:opacity-90 transition"
            >
              Complete Profile
            </button>

          </div>

        ) : (

          <>

            {/* MATCH GRID */}

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-2">

              {matches.map((m, i) => {

                const isLastMatch =
                  i === matches.length - 1;

                return (

                  <motion.div
                    ref={
                      isLastMatch
                        ? lastMatchRef
                        : null
                    }

                    key={
                      m.userId ||
                      m.profileId ||
                      m.id
                    }

                    initial={{
                      opacity: 0,
                      y: 20
                    }}

                    animate={{
                      opacity: 1,
                      y: 0
                    }}

                    transition={{
                      delay: Math.min(
                        i * 0.05,
                        0.5
                      )
                    }}

                    className="bg-card rounded-xl border border-border overflow-hidden hover:shadow-lg transition-shadow group"
                  >

                    <div className="h-40 overflow-hidden relative">

                      <img
                        src={resolveImageUrl(m.profilePhotoUrl || m.imageUrl, profile1)}
                        alt={m.fullName || m.name || "Match Profile"}
                        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                        onError={(e) => {
                          e.target.onerror = null;
                          e.target.src = profile1;
                        }}
                      />

                      <div className="absolute top-3 right-3 bg-emerald-badge text-primary-foreground text-xs font-bold px-2.5 py-1 rounded-full shadow">
                        ❤️ {Math.round(parseScore(m))}% Match
                      </div>

                    </div>


                    <div className="p-3">

                      <h3 className="text-base font-semibold text-foreground">

                        {m.fullName || m.name},

                        <span className="text-primary">
                          {" "}
                          {m.age || "?"}
                        </span>

                      </h3>


                      <p className="text-xs text-muted-foreground mt-0.5">

                        {m.profession ||
                          m.occupation ||
                          "Profession not specified"}

                        {" · "}

                        {m.city ||
                          "Location not specified"}

                      </p>


                      <div className="flex gap-2 mt-4">

                        <Link
                          to={`/profile/${
                            m.profileId ||
                            m.id ||
                            m.userId
                          }`}
                          className="flex-1 flex items-center justify-center gap-1.5 bg-primary hover:bg-primary/90 text-primary-foreground text-xs font-semibold py-2 rounded-lg transition-colors text-center"
                        >
                          👤 View Profile
                        </Link>


                        <button
                          onClick={() =>
                            navigate(
                              `/match-details/${m.userId || m.profileId || m.id}`
                            )
                          }
                          className="flex-1 flex items-center justify-center gap-1.5 bg-pink-50 border border-pink-200 text-pink-700 text-xs font-semibold py-2 rounded-lg hover:bg-pink-100 transition-colors text-center"
                        >
                          ⚡ Match Details
                        </button>

                      </div>

                    </div>

                  </motion.div>
                );
              })}

            </div>


            {/* LOAD MORE SPINNER */}

            {loadingMore && (

              <div className="flex justify-center py-8">

                <div className="text-center">

                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-3"></div>

                  <p className="text-sm text-muted-foreground">
                    Loading more matches...
                  </p>

                </div>

              </div>

            )}


            {/* END MESSAGE */}

            {!hasMore &&
              matches.length > 0 && (

                <div className="text-center py-8">

                  <p className="text-sm text-muted-foreground">
                    You've seen all available matches.
                  </p>

                </div>

              )}

          </>

        )}

      </div>

    </div>
  );
};

export default Matches;