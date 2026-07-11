import React from "react";

const reasons = [
  "Fake Profile",
  "Inappropriate Profile",
  "Harassment / Abuse",
  "Spam",
  "Scammer / Fraud",
  "Underage Profile",
  "Other"
];

const ReportModal = ({
  open,
  onClose,
  selectedReason,
  setSelectedReason,
  customReason,
  setCustomReason,
  onSubmit
}) => {

  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">

      <div className="bg-white rounded-2xl shadow-xl w-[420px] max-w-[95%] p-6">

        <h2 className="text-xl font-bold mb-2">
          Report Profile
        </h2>

        <p className="text-gray-500 text-sm mb-5">
          Why are you reporting this profile?
        </p>

        <div className="space-y-3">

          {reasons.map((reason) => (
            <label
              key={reason}
              className="flex items-center gap-3 cursor-pointer"
            >
              <input
                type="radio"
                value={reason}
                checked={selectedReason === reason}
                onChange={(e) =>
                  setSelectedReason(e.target.value)
                }
              />

              <span>{reason}</span>
            </label>
          ))}

        </div>

        {selectedReason === "Other" && (
          <textarea
            className="w-full mt-4 border rounded-lg p-3"
            rows={3}
            placeholder="Enter reason..."
            value={customReason}
            onChange={(e) => setCustomReason(e.target.value)}
          />
        )}

        <div className="flex justify-end gap-3 mt-6">

          <button
            onClick={onClose}
            className="px-4 py-2 rounded-lg border"
          >
            Cancel
          </button>

          <button
            onClick={onSubmit}
            className="bg-red-500 text-white px-5 py-2 rounded-lg hover:bg-red-600"
          >
            Report
          </button>

        </div>

      </div>

    </div>
  );
};

export default ReportModal;