import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { Heart, Calendar, MapPin, ArrowLeft, Loader2, Quote, Sparkles } from "lucide-react";
import { successStoryAPI } from "@/services/api";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

export default function SuccessStoryDetails() {
  const { id } = useParams();
  const [story, setStory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchStory();
  }, [id]);

  const fetchStory = async () => {
    try {
      setLoading(true);
      const res = await successStoryAPI.getStoryById(id);
      const data = res.data ?? res;
      setStory(data);
    } catch (err) {
      console.error(err);
      setError(err.message || "Failed to load story details");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-900 text-slate-100 flex flex-col">
        <Navbar />
        <div className="flex flex-1 items-center justify-center">
          <Loader2 className="h-8 w-8 animate-spin text-rose-500" />
        </div>
        <Footer />
      </div>
    );
  }

  if (error || !story) {
    return (
      <div className="min-h-screen bg-slate-900 text-slate-100 flex flex-col">
        <Navbar />
        <div className="flex flex-1 flex-col items-center justify-center p-6 text-center">
          <Heart className="h-16 w-16 text-rose-500/40" />
          <h2 className="mt-4 text-2xl font-bold">Story Not Available</h2>
          <p className="mt-2 text-sm text-slate-400 max-w-md">
            This success story might have been removed or is currently unavailable.
          </p>
          <Link
            to="/success-stories"
            className="mt-6 inline-flex items-center gap-2 rounded-xl bg-rose-600 px-6 py-2.5 text-sm font-semibold text-white hover:bg-rose-700 transition"
          >
            <ArrowLeft className="h-4 w-4" /> Back to Success Stories
          </Link>
        </div>
        <Footer />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-900 text-slate-100 flex flex-col font-sans selection:bg-rose-500 selection:text-white">
      <Navbar />

      <main className="flex-1 py-12 px-4 sm:px-6 lg:px-8">
        <div className="mx-auto max-w-4xl">
          {/* Back button */}
          <Link
            to="/success-stories"
            className="inline-flex items-center gap-2 text-sm font-medium text-slate-400 hover:text-rose-400 transition-colors mb-8"
          >
            <ArrowLeft className="h-4 w-4" /> Back to all success stories
          </Link>

          {/* Main Card */}
          <article className="overflow-hidden rounded-3xl border border-slate-800 bg-slate-800/40 backdrop-blur-xl shadow-2xl shadow-rose-950/20">
            {/* Main Header / Image Banner */}
            <div className="relative h-80 sm:h-96 w-full overflow-hidden bg-slate-950">
              {story.coupleImageUrl || story.partnerOneImageUrl ? (
                <img
                  src={story.coupleImageUrl || story.partnerOneImageUrl}
                  alt={`${story.partnerOneName} & ${story.partnerTwoName}`}
                  className="h-full w-full object-cover"
                />
              ) : (
                <div className="flex h-full w-full items-center justify-center bg-gradient-to-br from-rose-950/80 to-slate-950 text-rose-400">
                  <Heart className="h-24 w-24 opacity-30 animate-pulse" />
                </div>
              )}
              <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-slate-950/40 to-transparent" />

              <div className="absolute bottom-6 left-6 right-6">
                <div className="inline-flex items-center gap-2 rounded-full border border-rose-500/30 bg-rose-500/20 px-3 py-1 text-xs font-semibold text-rose-300 backdrop-blur-md mb-3">
                  <Sparkles className="h-3.5 w-3.5 text-rose-400" />
                  <span>Matched on Gathbandhan</span>
                </div>

                <h1 className="text-3xl font-extrabold sm:text-4xl text-white">
                  {story.partnerOneName} & {story.partnerTwoName}
                </h1>

                <div className="mt-2 flex flex-wrap items-center gap-4 text-sm text-slate-300">
                  {story.weddingDate && (
                    <div className="flex items-center gap-1.5">
                      <Calendar className="h-4 w-4 text-rose-400" />
                      <span>Married on {new Date(story.weddingDate).toLocaleDateString("en-US", { month: "long", day: "numeric", year: "numeric" })}</span>
                    </div>
                  )}
                  {story.location && (
                    <div className="flex items-center gap-1.5">
                      <MapPin className="h-4 w-4 text-rose-400" />
                      <span>{story.location}</span>
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* Individual partner photos if available */}
            {(story.partnerOneImageUrl || story.partnerTwoImageUrl) && (
              <div className="flex items-center justify-center gap-8 py-6 border-b border-slate-700/50 bg-slate-900/50">
                {story.partnerOneImageUrl && (
                  <div className="text-center">
                    <img
                      src={story.partnerOneImageUrl}
                      alt={story.partnerOneName}
                      className="h-20 w-20 rounded-full object-cover border-2 border-rose-500/40 mx-auto shadow-md"
                    />
                    <span className="mt-2 block text-xs font-semibold text-slate-300">{story.partnerOneName}</span>
                  </div>
                )}

                <div className="text-rose-500">
                  <Heart className="h-6 w-6 fill-current text-rose-500" />
                </div>

                {story.partnerTwoImageUrl && (
                  <div className="text-center">
                    <img
                      src={story.partnerTwoImageUrl}
                      alt={story.partnerTwoName}
                      className="h-20 w-20 rounded-full object-cover border-2 border-rose-500/40 mx-auto shadow-md"
                    />
                    <span className="mt-2 block text-xs font-semibold text-slate-300">{story.partnerTwoName}</span>
                  </div>
                )}
              </div>
            )}

            {/* Story Content */}
            <div className="p-8 sm:p-12 space-y-8">
              {/* Short Story Teaser */}
              <div className="relative rounded-2xl border border-rose-500/20 bg-gradient-to-r from-rose-950/20 to-slate-900/60 p-6 backdrop-blur-md">
                <Quote className="absolute top-4 right-4 h-8 w-8 text-rose-500/20" />
                <p className="text-base sm:text-lg font-medium italic text-rose-100 leading-relaxed">
                  "{story.shortStory}"
                </p>
              </div>

              {/* Full Story */}
              {story.fullStory ? (
                <div className="space-y-4 text-slate-300 leading-relaxed text-base">
                  <h2 className="text-xl font-bold text-white border-b border-slate-700/50 pb-2">
                    Their Matrimonial Journey
                  </h2>
                  <div className="whitespace-pre-line text-slate-300 leading-relaxed">
                    {story.fullStory}
                  </div>
                </div>
              ) : null}

              {/* Footer Call to Action */}
              <div className="mt-12 rounded-2xl border border-slate-700 bg-slate-900/80 p-8 text-center">
                <h3 className="text-2xl font-bold text-white">Begin Your Own Love Story</h3>
                <p className="mt-2 text-sm text-slate-400 max-w-md mx-auto">
                  Thousands of verified profiles are waiting for you on Gathbandhan. Start your journey today!
                </p>
                <Link
                  to="/register"
                  className="mt-6 inline-flex items-center gap-2 rounded-xl bg-gradient-to-r from-rose-600 to-pink-600 px-8 py-3 font-bold text-white shadow-lg shadow-rose-950/50 hover:from-rose-500 hover:to-pink-500 transition"
                >
                  Join Gathbandhan Today
                </Link>
              </div>
            </div>
          </article>
        </div>
      </main>

      <Footer />
    </div>
  );
}
