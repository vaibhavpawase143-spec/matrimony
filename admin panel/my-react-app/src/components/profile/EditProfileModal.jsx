import { useEffect, useState } from "react";
import { toast } from "sonner";

import FormModal from "../common/FormModal";
import { updateAdminProfile } from "../../services/adminProfileService";
import { uploadImage } from "../../services/imageUploadService";

export default function EditProfileModal({
  isOpen,
  onClose,
  profile,
  onSuccess,
}) {
  const [formData, setFormData] = useState({
    name: "",
    phone: "",
    profilePhoto: "",
  });

  const [loading, setLoading] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const [preview, setPreview] = useState("");

  useEffect(() => {
    if (profile) {
      setFormData({
        name: profile.name || "",
        phone: profile.phone || "",
        profilePhoto: profile.profilePhoto || "",
      });

      setPreview(profile.profilePhoto || "");
      setSelectedFile(null);
    }
  }, [profile]);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];

    if (!file) return;

    setSelectedFile(file);
    setPreview(URL.createObjectURL(file));
  };

  const handleSubmit = async () => {
    if (!formData.name.trim()) {
      toast.error("Name is required.");
      return;
    }

    if (
      formData.phone &&
      !/^[0-9]{10}$/.test(formData.phone)
    ) {
      toast.error("Phone number must be 10 digits.");
      return;
    }

    try {
      setLoading(true);

      let imageUrl = formData.profilePhoto;

      if (selectedFile) {
        imageUrl = await uploadImage(selectedFile);
      }

      await updateAdminProfile({
        ...formData,
        profilePhoto: imageUrl,
      });

      toast.success("Profile updated successfully.");

      onSuccess();
      onClose();
    } catch (err) {
      toast.error(err.message || "Failed to update profile.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <FormModal
      isOpen={isOpen}
      onClose={onClose}
      title="Edit Profile"
      footer={
        <div className="flex justify-end gap-3">
          <button
            onClick={onClose}
            className="px-5 py-2 rounded-lg border border-gray-300 hover:bg-gray-100"
          >
            Cancel
          </button>

          <button
            onClick={handleSubmit}
            disabled={loading}
            className="px-5 py-2 rounded-lg bg-violet-600 hover:bg-violet-700 text-white disabled:opacity-50"
          >
            {loading ? "Saving..." : "Save Changes"}
          </button>
        </div>
      }
    >
      <div className="space-y-5">
                  <div>
                    <label className="block text-sm font-medium mb-2">
                      Name
                    </label>

                    <input
                      type="text"
                      name="name"
                      value={formData.name}
                      onChange={handleChange}
                      className="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-violet-500 outline-none"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium mb-2">
                      Phone
                    </label>

                    <input
                      type="text"
                      name="phone"
                      value={formData.phone}
                      onChange={handleChange}
                      className="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-violet-500 outline-none"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium mb-3">
                      Profile Photo
                    </label>

                    <div className="flex items-center gap-5">
                      <img
                        src={
                          preview ||
                          `https://ui-avatars.com/api/?name=${encodeURIComponent(
                            formData.name || "Admin"
                          )}`
                        }
                        alt="Profile Preview"
                        className="w-24 h-24 rounded-full border object-cover"
                      />

                      <input
                        type="file"
                        accept="image/png,image/jpeg"
                        onChange={handleImageChange}
                        className="block w-full text-sm text-gray-700
                          file:mr-4
                          file:py-2
                          file:px-4
                          file:rounded-lg
                          file:border-0
                          file:bg-violet-600
                          file:text-white
                          hover:file:bg-violet-700"
                      />
                    </div>

                    <p className="text-xs text-gray-500 mt-2">
                      JPG or PNG only. Maximum size: 5 MB.
                    </p>
                  </div>
                </div>
              </FormModal>
            );
          }