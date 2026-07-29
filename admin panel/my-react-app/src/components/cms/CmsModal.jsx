import { useEffect, useState } from "react";

const initialForm = {
  pageKey: "",
  title: "",
  content: "",
  published: false,
  isActive: true,
};

export default function CmsModal({
  open,
  onClose,
  onSubmit,
  loading = false,
  initialData = null,
}) {
  const [formData, setFormData] = useState(initialForm);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (!open) return;

    if (initialData) {
      setFormData({
        pageKey: initialData.pageKey || "",
        title: initialData.title || "",
        content: initialData.content || "",
        published: initialData.published ?? false,
        isActive: initialData.isActive ?? true,
      });
    } else {
      setFormData(initialForm);
    }

    setErrors({});
  }, [open, initialData]);

  if (!open) return null;

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));

    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: "",
      }));
    }
  };

  const validate = () => {
    const validationErrors = {};

    if (!formData.pageKey.trim()) {
      validationErrors.pageKey = "Page Key is required.";
    }

    if (!formData.title.trim()) {
      validationErrors.title = "Title is required.";
    }

    if (!formData.content.trim()) {
      validationErrors.content = "Content is required.";
    }

    setErrors(validationErrors);

    return Object.keys(validationErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!validate()) return;

    onSubmit(formData);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
      <div className="w-full max-w-3xl rounded-xl bg-white shadow-2xl">
        <div className="flex items-center justify-between border-b px-6 py-4">
          <h2 className="text-2xl font-bold">
            {initialData ? "Edit CMS Page" : "Create CMS Page"}
          </h2>

          <button
            type="button"
            onClick={onClose}
            className="text-2xl text-gray-500 hover:text-black"
          >
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="space-y-5 p-6">
            <div>
              <label className="mb-2 block font-medium">
                Page Key
              </label>

              <input
                type="text"
                name="pageKey"
                value={formData.pageKey}
                onChange={handleChange}
                placeholder="privacy-policy"
                className="w-full rounded-lg border px-4 py-3 focus:border-purple-600 focus:outline-none"
              />

              {errors.pageKey && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.pageKey}
                </p>
              )}
            </div>

            <div>
              <label className="mb-2 block font-medium">
                Title
              </label>

              <input
                type="text"
                name="title"
                value={formData.title}
                onChange={handleChange}
                placeholder="Privacy Policy"
                className="w-full rounded-lg border px-4 py-3 focus:border-purple-600 focus:outline-none"
              />

              {errors.title && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.title}
                </p>
              )}
            </div>

            <div>
              <label className="mb-2 block font-medium">
                Content
              </label>

              <textarea
                rows={10}
                name="content"
                value={formData.content}
                onChange={handleChange}
                placeholder="Enter page content..."
                className="w-full rounded-lg border px-4 py-3 focus:border-purple-600 focus:outline-none"
              />

              {errors.content && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.content}
                </p>
              )}
            </div>

            <div className="flex items-center gap-3">
              <input
                id="published"
                type="checkbox"
                name="published"
                checked={formData.published}
                onChange={handleChange}
              />

              <label htmlFor="published">
                Publish immediately
              </label>
            </div>
          </div>

          <div className="flex justify-end gap-3 border-t px-6 py-4">
            <button
              type="button"
              onClick={onClose}
              disabled={loading}
              className="rounded-lg border px-5 py-2 hover:bg-gray-100"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={loading}
              className="rounded-lg bg-purple-600 px-5 py-2 text-white hover:bg-purple-700 disabled:cursor-not-allowed disabled:opacity-60"
            >
              {loading
                ? "Saving..."
                : initialData
                ? "Update Page"
                : "Create Page"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}