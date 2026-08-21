import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Heart, Calendar, MapPin, Sparkles, ArrowRight, Loader2 } from "lucide-react";
import { successStoryAPI } from "@/services/api";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

export default function SuccessStories() {
  const [stories, setStories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    fetchStories();
  }, [page]);

  const fetchStories = async () => {
    try {
      setLoading(true);
      const res = await successStoryAPI.getPublishedStories(page, 9);
      const data = res.data ?? res;
      if (data && data.content) {
        setStories(data.content);
        setTotalPages(data.totalPages);
      } else if (Array.isArray(data)) {
        setStories(data);
        setTotalPages(1);
      }
    } catch (err) {
      console.error("Failed to load success stories", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 text-slate-100 flex flex-col font-sans selection:bg-rose-500 selection:text-white">
      <Navbar />

      {/* Hero Banner Header */}
      <div className="relative overflow-hidden bg-gradient-to-b from-rose-950/40 via-slate-900 to-slate-900 py-20 px-4 sm:px-6 lg:px-8">
        <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-rose-900/20 via-transparent to-transparent pointer-events-none" />
        <div className="relative mx-auto max-w-5xl text-center">
          <div className="inline-flex items-center gap-2 rounded-full border border-rose-500/30 bg-rose-500/10 px-4 py-1.5 text-xs font-semibold text-rose-300 shadow-sm backdrop-blur-md">
            <Sparkles className="h-3.5 w-3.5 text-rose-400 animate-pulse" />
            <span>Matched on Gathbandhan</span>
          </div>

          <h1 className="mt-6 text-4xl font-extrabold tracking-tight sm:text-5xl lg:text-6xl bg-gradient-to-r from-white via-rose-100 to-rose-300 bg-clip-text text-transparent">
            Real Couples, Eternal Love
          </h1>

          <p className="mt-4 text-base text-slate-300 sm:text-lg max-w-2xl mx-auto leading-relaxed">
            Discover how thousands of couples found their soulmates through Gathbandhan. Read their heartwarming journeys to forever.
          </p>
        </div>
      </div>

      {/* Main Grid Section */}
      <main className="flex-1 mx-auto w-full max-w-7xl px-4 sm:px-6 lg:px-8 pb-24">
        {loading ? (
          <div className="flex h-64 items-center justify-center">
            <Loader2 className="h-8 w-8 animate-spin text-rose-500" />
          </div>
        ) : stories.length === 0 ? (
          <div className="rounded-2xl border border-slate-800 bg-slate-800/40 p-12 text-center backdrop-blur-md max-w-lg mx-auto">
            <Heart className="mx-auto h-12 w-12 text-rose-500/40" />
            <h3 className="mt-4 text-xl font-bold text-slate-200">No Stories Published Yet</h3>
            <p className="mt-2 text-sm text-slate-400">
              Check back soon for new heartwarming wedding stories from our members!
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {stories.map((story) => (
              <div
                key={story.id}
                className="group relative flex flex-col overflow-hidden rounded-2xl border border-slate-800 bg-slate-800/40 backdrop-blur-md transition-all duration-300 hover:-translate-y-1 hover:border-rose-500/40 hover:shadow-2xl hover:shadow-rose-950/30"
              >
                {/* Image Showcase */}
                <div className="relative h-64 w-full overflow-hidden bg-slate-900">
                  {story.coupleImageUrl || story.partnerOneImageUrl ? (
                    <img
                      src={story.coupleImageUrl || story.partnerOneImageUrl}
                      alt={`${story.partnerOneName} & ${story.partnerTwoName}`}
                      className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-105"
                    />
                  ) : (
                    <div className="flex h-full w-full items-center justify-center bg-gradient-to-br from-rose-950/60 to-slate-900 text-rose-400">
                      <Heart className="h-16 w-16 opacity-40 animate-pulse" />
                    </div>
                  )}

                  <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-slate-950/20 to-transparent" />

                  {/* Wedding Date Badge */}
                  {story.weddingDate && (
                    <div className="absolute top-4 left-4 inline-flex items-center gap-1.5 rounded-full border border-white/10 bg-black/60 px-3 py-1 text-xs font-medium text-rose-200 backdrop-blur-md">
                      <Calendar className="h-3 w-3 text-rose-400" />
                      <span>{new Date(story.weddingDate).toLocaleDateString("en-US", { month: "short", year: "numeric" })}</span>
                    </div>
                  )}
                </div>

                {/* Content */}
                <div className="flex flex-1 flex-col justify-between p-6">
                  <div>
                    <h2 className="text-xl font-bold text-white group-hover:text-rose-300 transition-colors">
                      {story.partnerOneName} & {story.partnerTwoName}
                    </h2>

                    {story.location && (
                      <div className="mt-1.5 flex items-center gap-1 text-xs text-slate-400">
                        <MapPin className="h-3.5 w-3.5 text-rose-400" />
                        <span>{story.location}</span>
                      </div>
                    )}

                    <p className="mt-3 text-sm text-slate-300 line-clamp-3 leading-relaxed">
                      "{story.shortStory}"
                    </p>
                  </div>

                  <div className="mt-6 pt-4 border-t border-slate-700/50">
                    <Link
                      to={`/success-stories/${story.id}`}
                      className="inline-flex items-center gap-2 text-sm font-semibold text-rose-400 hover:text-rose-300 transition-colors"
                    >
                      <span>Read Full Story</span>
                      <ArrowRight className="h-4 w-4 transition-transform group-hover:translate-x-1" />
                    </Link>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="mt-12 flex items-center justify-center gap-3">
            <button
              disabled={page === 0}
              onClick={() => setPage((p) => Math.max(0, p - 1))}
              className="rounded-xl border border-slate-800 bg-slate-800/60 px-4 py-2 text-sm font-medium text-slate-300 hover:bg-slate-700 hover:text-white disabled:opacity-50 transition"
            >
              Previous
            </button>

            <span className="text-xs text-slate-400 font-medium">
              Page {page + 1} of {totalPages}
            </span>

            <button
              disabled={page >= totalPages - 1}
              onClick={() => setPage((p) => p + 1)}
              className="rounded-xl border border-slate-800 bg-slate-800/60 px-4 py-2 text-sm font-medium text-slate-300 hover:bg-slate-700 hover:text-white disabled:opacity-50 transition"
            >
              Next
            </button>
          </div>
        )}
      </main>

      <Footer />
    </div>
  );
}
