import { useEffect, useState } from "react";
import { FaUpload, FaSpinner, FaHeart } from "react-icons/fa";
import { toast } from "react-toastify";
import { uploadImage } from "../../services/imageUploadService";

export default function SuccessStoryModal({
  open,
  onClose,
  onSubmit,
  loading,
  initialData,
}) {
  const [formData, setFormData] = useState({
    partnerOneName: "",
    partnerTwoName: "",
    partnerOneImageUrl: "",
    partnerTwoImageUrl: "",
    coupleImageUrl: "",
    shortStory: "",
    fullStory: "",
    weddingDate: "",
    location: "",
    consentGiven: false,
    displayOrder: 0,
  });

  const [uploadingCouple, setUploadingCouple] = useState(false);
  const [uploadingPartner1, setUploadingPartner1] = useState(false);
  const [uploadingPartner2, setUploadingPartner2] = useState(false);

  useEffect(() => {
    if (initialData) {
      setFormData({
        partnerOneName: initialData.partnerOneName || "",
        partnerTwoName: initialData.partnerTwoName || "",
        partnerOneImageUrl: initialData.partnerOneImageUrl || "",
        partnerTwoImageUrl: initialData.partnerTwoImageUrl || "",
        coupleImageUrl: initialData.coupleImageUrl || "",
        shortStory: initialData.shortStory || "",
        fullStory: initialData.fullStory || "",
        weddingDate: initialData.weddingDate || "",
        location: initialData.location || "",
        consentGiven: Boolean(initialData.consentGiven),
        displayOrder: initialData.displayOrder ?? 0,
      });
    } else {
      setFormData({
        partnerOneName: "",
        partnerTwoName: "",
        partnerOneImageUrl: "",
        partnerTwoImageUrl: "",
        coupleImageUrl: "",
        shortStory: "",
        fullStory: "",
        weddingDate: "",
        location: "",
        consentGiven: false,
        displayOrder: 0,
      });
    }
  }, [initialData, open]);

  if (!open) return null;

  const handleImageUpload = async (e, field, setUploadingState) => {
    const file = e.target.files?.[0];
    if (!file) return;

    if (file.size > 5 * 1024 * 1024) {
      toast.error("File size must not exceed 5MB");
      return;
    }

    try {
      setUploadingState(true);
      const url = await uploadImage(file);
      setFormData((prev) => ({ ...prev, [field]: url }));
      toast.success("Image uploaded successfully");
    } catch (err) {
      console.error(err);
      toast.error(err.message || "Failed to upload image");
    } finally {
      setUploadingState(false);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!formData.partnerOneName.trim()) {
      toast.error("Partner 1 name is required.");
      return;
    }
    if (!formData.partnerTwoName.trim()) {
      toast.error("Partner 2 name is required.");
      return;
    }
    if (!formData.shortStory.trim()) {
      toast.error("Short story is required.");
      return;
    }

    onSubmit(formData);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4 backdrop-blur-sm">
      <div className="max-h-[90vh] w-full max-w-3xl overflow-y-auto rounded-2xl bg-white p-6 shadow-2xl">
        <div className="mb-6 flex items-center justify-between border-b pb-4">
          <div className="flex items-center gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-full bg-pink-100 text-pink-600">
              <FaHeart className="text-xl" />
            </div>
            <div>
              <h2 className="text-xl font-bold text-gray-800">
                {initialData ? "Edit Success Story" : "Create Success Story"}
              </h2>
              <p className="text-sm text-gray-500">
                Celebrate member journeys and inspiring love stories
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="rounded-full p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-600"
          >
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Couple Names */}
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
            <div>
              <label className="mb-1 block text-sm font-semibold text-gray-700">
                Partner 1 Name <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                required
                value={formData.partnerOneName}
                onChange={(e) =>
                  setFormData({ ...formData, partnerOneName: e.target.value })
                }
                placeholder="e.g. Rahul Sharma"
                className="w-full rounded-lg border border-gray-300 p-2.5 outline-none focus:border-pink-500 focus:ring-1 focus:ring-pink-500"
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-semibold text-gray-700">
                Partner 2 Name <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                required
                value={formData.partnerTwoName}
                onChange={(e) =>
                  setFormData({ ...formData, partnerTwoName: e.target.value })
                }
                placeholder="e.g. Ananya Patel"
                className="w-full rounded-lg border border-gray-300 p-2.5 outline-none focus:border-pink-500 focus:ring-1 focus:ring-pink-500"
              />
            </div>
          </div>

          {/* Wedding Date & Location & Display Order */}
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
            <div>
              <label className="mb-1 block text-sm font-semibold text-gray-700">
                Wedding Date
              </label>
              <input
                type="date"
                value={formData.weddingDate}
                onChange={(e) =>
                  setFormData({ ...formData, weddingDate: e.target.value })
                }
                className="w-full rounded-lg border border-gray-300 p-2.5 outline-none focus:border-pink-500 focus:ring-1 focus:ring-pink-500"
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-semibold text-gray-700">
                Location / City
              </label>
              <input
                type="text"
                value={formData.location}
                onChange={(e) =>
                  setFormData({ ...formData, location: e.target.value })
                }
                placeholder="e.g. Mumbai, India"
                className="w-full rounded-lg border border-gray-300 p-2.5 outline-none focus:border-pink-500 focus:ring-1 focus:ring-pink-500"
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-semibold text-gray-700">
                Display Order
              </label>
              <input
                type="number"
                min="0"
                value={formData.displayOrder}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    displayOrder: parseInt(e.target.value, 10) || 0,
                  })
                }
                className="w-full rounded-lg border border-gray-300 p-2.5 outline-none focus:border-pink-500 focus:ring-1 focus:ring-pink-500"
              />
            </div>
          </div>

          {/* Image Uploads */}
          <div className="rounded-xl border border-gray-200 bg-gray-50 p-4 space-y-4">
            <h3 className="text-sm font-bold text-gray-800">
              Photos & Media
            </h3>

            {/* Couple Main Photo */}
            <div>
              <label className="mb-1 block text-xs font-semibold text-gray-600">
                Couple Main Image (Recommended)
              </label>
              <div className="flex items-center gap-4">
                {formData.coupleImageUrl && (
                  <img
                    src={formData.coupleImageUrl}
                    alt="Couple"
                    className="h-16 w-16 rounded-lg object-cover border"
                  />
                )}
                <label className="flex cursor-pointer items-center gap-2 rounded-lg border bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-100">
                  {uploadingCouple ? (
                    <FaSpinner className="animate-spin text-pink-600" />
                  ) : (
                    <FaUpload className="text-gray-500" />
                  )}
                  Upload Couple Photo
                  <input
                    type="file"
                    accept="image/*"
                    className="hidden"
                    onChange={(e) =>
                      handleImageUpload(e, "coupleImageUrl", setUploadingCouple)
                    }
                  />
                </label>
                {formData.coupleImageUrl && (
                  <button
                    type="button"
                    onClick={() =>
                      setFormData({ ...formData, coupleImageUrl: "" })
                    }
                    className="text-xs text-red-600 hover:underline"
                  >
                    Remove
                  </button>
                )}
              </div>
            </div>

            {/* Partner 1 & 2 Photos */}
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <div>
                <label className="mb-1 block text-xs font-semibold text-gray-600">
                  Partner 1 Individual Image
                </label>
                <div className="flex items-center gap-3">
                  {formData.partnerOneImageUrl && (
                    <img
                      src={formData.partnerOneImageUrl}
                      alt="Partner 1"
                      className="h-12 w-12 rounded-full object-cover border"
                    />
                  )}
                  <label className="flex cursor-pointer items-center gap-2 rounded-lg border bg-white px-3 py-1.5 text-xs font-medium text-gray-700 hover:bg-gray-100">
                    {uploadingPartner1 ? (
                      <FaSpinner className="animate-spin text-pink-600" />
                    ) : (
                      <FaUpload className="text-gray-500" />
                    )}
                    Upload
                    <input
                      type="file"
                      accept="image/*"
                      className="hidden"
                      onChange={(e) =>
                        handleImageUpload(
                          e,
                          "partnerOneImageUrl",
                          setUploadingPartner1
                        )
                      }
                    />
                  </label>
                </div>
              </div>

              <div>
                <label className="mb-1 block text-xs font-semibold text-gray-600">
                  Partner 2 Individual Image
                </label>
                <div className="flex items-center gap-3">
                  {formData.partnerTwoImageUrl && (
                    <img
                      src={formData.partnerTwoImageUrl}
                      alt="Partner 2"
                      className="h-12 w-12 rounded-full object-cover border"
                    />
                  )}
                  <label className="flex cursor-pointer items-center gap-2 rounded-lg border bg-white px-3 py-1.5 text-xs font-medium text-gray-700 hover:bg-gray-100">
                    {uploadingPartner2 ? (
                      <FaSpinner className="animate-spin text-pink-600" />
                    ) : (
                      <FaUpload className="text-gray-500" />
                    )}
                    Upload
                    <input
                      type="file"
                      accept="image/*"
                      className="hidden"
                      onChange={(e) =>
                        handleImageUpload(
                          e,
                          "partnerTwoImageUrl",
                          setUploadingPartner2
                        )
                      }
                    />
                  </label>
                </div>
              </div>
            </div>
          </div>

          {/* Short Story */}
          <div>
            <label className="mb-1 block text-sm font-semibold text-gray-700">
              Short Summary / Teaser <span className="text-red-500">*</span>
            </label>
            <textarea
              required
              rows={3}
              maxLength={1000}
              value={formData.shortStory}
              onChange={(e) =>
                setFormData({ ...formData, shortStory: e.target.value })
              }
              placeholder="A brief snippet displayed on cards and search listings (max 1000 characters)..."
              className="w-full rounded-lg border border-gray-300 p-2.5 outline-none focus:border-pink-500 focus:ring-1 focus:ring-pink-500"
            />
            <div className="mt-1 text-right text-xs text-gray-400">
              {formData.shortStory.length} / 1000
            </div>
          </div>

          {/* Full Story */}
          <div>
            <label className="mb-1 block text-sm font-semibold text-gray-700">
              Full Story Detail
            </label>
            <textarea
              rows={5}
              value={formData.fullStory}
              onChange={(e) =>
                setFormData({ ...formData, fullStory: e.target.value })
              }
              placeholder="Detailed story of how they met, their journey on Gathbandhan, and their wedding celebration..."
              className="w-full rounded-lg border border-gray-300 p-2.5 outline-none focus:border-pink-500 focus:ring-1 focus:ring-pink-500"
            />
          </div>

          {/* Consent Enforcement Checkbox */}
          <div className="rounded-xl border border-amber-200 bg-amber-50 p-4">
            <label className="flex items-start gap-3 cursor-pointer">
              <input
                type="checkbox"
                checked={formData.consentGiven}
                onChange={(e) =>
                  setFormData({ ...formData, consentGiven: e.target.checked })
                }
                className="mt-1 h-5 w-5 rounded border-gray-300 text-pink-600 focus:ring-pink-500"
              />
              <div>
                <span className="text-sm font-bold text-amber-900">
                  Couple Consent Verification (Mandatory for Publishing)
                </span>
                <p className="text-xs text-amber-700 mt-0.5">
                  I confirm that explicit consent has been received from both partners to feature their names, images, and story on the Gathbandhan platform. Unchecking consent will automatically unpublish this story.
                </p>
              </div>
            </label>
          </div>

          {/* Action Buttons */}
          <div className="flex items-center justify-end gap-3 border-t pt-4">
            <button
              type="button"
              onClick={onClose}
              className="rounded-lg border border-gray-300 px-5 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex items-center gap-2 rounded-lg bg-pink-600 px-6 py-2 text-sm font-medium text-white hover:bg-pink-700 disabled:opacity-50"
            >
              {loading && <FaSpinner className="animate-spin" />}
              {initialData ? "Update Story" : "Create Story"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
