import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, CheckCircle2, XCircle } from "lucide-react";
import { matchAPI } from "@/services/api";
import { useToast } from "@/components/Toast";

const MatchDetails = () => {
  const { partnerId } = useParams();
  const navigate = useNavigate();
  const { error } = useToast();

  const [loading, setLoading] = useState(true);
  const [match, setMatch] = useState(null);

  useEffect(() => {
    loadDetails();
  }, []);

  const loadDetails = async () => {
    try {
      const currentUser = JSON.parse(localStorage.getItem("user"));
      const userId = currentUser.profile.userId;

      const response = await matchAPI.getMatchDetails(userId, partnerId);

      console.log("MATCH DETAILS =", response);

      setMatch(response);
    } catch (e) {
      console.error(e);
      error("Unable to load match details");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-pink-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <div className="max-w-5xl mx-auto p-6">

        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-2 text-pink-600 mb-6"
        >
          <ArrowLeft size={18} />
          Back to Matches
        </button>

        <div className="bg-white rounded-xl shadow p-6 mb-6">
          <div className="flex justify-between items-center flex-wrap gap-4">
            <div>
              <h1 className="text-3xl font-bold">{match.fullName}</h1>
              <p className="text-gray-500">Match Details</p>
            </div>

            <div className="w-32 h-32 rounded-full bg-pink-100 flex items-center justify-center">
              <div className="text-center">
                <div className="text-4xl font-bold text-pink-600">
                  {match.matchPercentage}%
                </div>
                <div className="text-sm">Matched</div>
              </div>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4 mt-8">
            <div className="bg-green-50 rounded-lg p-4 text-center">
              <div className="text-3xl font-bold text-green-600">
                {match.matchedFields}
              </div>
              <div>Matched Fields</div>
            </div>

            <div className="bg-blue-50 rounded-lg p-4 text-center">
              <div className="text-3xl font-bold text-blue-600">
                {match.totalFields}
              </div>
              <div>Total Compared</div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow">
          <div className="border-b p-5">
            <h2 className="text-xl font-bold">Compatibility Breakdown</h2>
          </div>

          {match.fieldMatches?.map((field, index) => (
            <div
              key={index}
              className="flex justify-between items-center border-b p-5"
            >
              <div>
                <h3 className="font-semibold">{field.fieldName}</h3>
                <p className="text-gray-500 text-sm">
                    Preferred : <b>{field.myValue || "-"}</b>
                </p>
                <p className="text-gray-500 text-sm">
                  Partner : <b>{field.partnerValue || "-"}</b>
                </p>
              </div>

              {field.matched ? (
                <div className="flex items-center gap-2 text-green-600 font-semibold">
                  <CheckCircle2 />
                  Matched
                </div>
              ) : (
                <div className="flex items-center gap-2 text-red-500 font-semibold">
                  <XCircle />
                  Not Matched
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MatchDetails;
