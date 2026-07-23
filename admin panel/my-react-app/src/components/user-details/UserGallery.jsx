import { useEffect, useState } from "react";
import {
  FiZoomIn,
  FiChevronLeft,
  FiChevronRight,
} from "react-icons/fi";

export default function UserGallery({ user }) {
  const photos =
    user?.photos?.length > 0
      ? user.photos
      : [
          {
            id: 1,
            photoUrl:
              user?.imageUrl ||
              "https://ui-avatars.com/api/?name=User&size=500",
            photoType: "Profile",
          },
        ];

  const [selectedImage, setSelectedImage] = useState(
    photos[0]?.photoUrl
  );
  const [previewOpen, setPreviewOpen] = useState(false);

  useEffect(() => {
    const handleKeyDown = (e) => {
      if (!previewOpen) return;

      if (e.key === "Escape") {
        setPreviewOpen(false);
      }

      if (e.key === "ArrowLeft") {
        handlePrevious();
      }

      if (e.key === "ArrowRight") {
        handleNext();
      }
    };

    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [previewOpen, selectedImage]);

  const currentIndex = photos.findIndex(
    (photo) => photo.photoUrl === selectedImage
  );

  const handlePrevious = () => {
    const prevIndex =
      currentIndex === 0 ? photos.length - 1 : currentIndex - 1;

    setSelectedImage(photos[prevIndex].photoUrl);
  };

  const handleNext = () => {
    const nextIndex =
      currentIndex === photos.length - 1 ? 0 : currentIndex + 1;

    setSelectedImage(photos[nextIndex].photoUrl);
  };

  return (
    <>
      <div className="bg-white rounded-xl shadow-sm border border-gray-200">
        <div className="border-b border-gray-100 px-6 py-4">
          <h2 className="text-lg font-semibold text-gray-800">
            Gallery
          </h2>
        </div>

        <div className="p-6">
          {/* Main Image */}
          <div
            onClick={() => setPreviewOpen(true)}
            className="group relative cursor-zoom-in overflow-hidden rounded-lg border"
          >
            <img
              src={selectedImage}
              alt="Profile"
              className="w-full h-80 object-cover transition duration-300 group-hover:scale-105"
            />

            <div className="absolute inset-0 flex flex-col items-center justify-center bg-black/0 opacity-0 transition-all duration-300 group-hover:bg-black/40 group-hover:opacity-100">
              <FiZoomIn className="text-white text-5xl mb-2" />
              <span className="text-white font-medium text-lg">
                Click to Preview
              </span>
            </div>
          </div>

          {/* Thumbnails */}
          {photos.length > 1 && (
            <div className="grid grid-cols-4 gap-3 mt-4">
              {photos.map((photo, index) => (
                <img
                  key={photo.id || index}
                  src={photo.photoUrl}
                  alt={photo.photoType || `Photo ${index + 1}`}
                  onClick={() => setSelectedImage(photo.photoUrl)}
                  className={`h-24 w-full rounded-lg object-cover cursor-pointer border-2 transition-all duration-300 hover:scale-105 hover:shadow-lg ${
                    selectedImage === photo.photoUrl
                      ? "border-purple-600"
                      : "border-gray-200 hover:border-purple-400"
                  }`}
                />
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Preview Modal */}
      {previewOpen && (
        <div
          onClick={() => setPreviewOpen(false)}
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/90 p-6"
        >
          <div
            onClick={(e) => e.stopPropagation()}
            className="relative flex items-center"
          >
            {/* Previous */}
            {photos.length > 1 && (
              <button
                onClick={handlePrevious}
                className="absolute left-[-70px] w-12 h-12 rounded-full bg-white/90 hover:bg-white shadow-lg flex items-center justify-center transition"
              >
                <FiChevronLeft size={30} />
              </button>
            )}

            {/* Close */}
            <button
              onClick={() => setPreviewOpen(false)}
              className="absolute -top-5 -right-5 w-11 h-11 rounded-full bg-white shadow-lg text-2xl text-gray-700 hover:bg-gray-100"
            >
              ×
            </button>

            {/* Image */}
            <img
              src={selectedImage}
              alt="Preview"
              className="max-h-[90vh] max-w-[90vw] rounded-xl shadow-2xl"
            />

            {/* Next */}
            {photos.length > 1 && (
              <button
                onClick={handleNext}
                className="absolute right-[-70px] w-12 h-12 rounded-full bg-white/90 hover:bg-white shadow-lg flex items-center justify-center transition"
              >
                <FiChevronRight size={30} />
              </button>
            )}
          </div>
        </div>
      )}
    </>
  );
}