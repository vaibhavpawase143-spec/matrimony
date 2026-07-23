import { useEffect, useState } from "react";

const defaultForm = {
  question: "",
  answer: "",
  displayOrder: 1,
  published: false,
  isActive: true,
};

export default function FaqModal({
  open,
  onClose,
  onSubmit,
  loading = false,
  initialData = null,
}) {
  const [formData, setFormData] = useState(defaultForm);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (!open) return;

    if (initialData) {
      setFormData({
        question: initialData.question || "",
        answer: initialData.answer || "",
        displayOrder: initialData.displayOrder || 1,
        published: initialData.published ?? false,
        isActive: initialData.isActive ?? true,
      });
    } else {
      setFormData(defaultForm);
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

    setErrors((prev) => ({
      ...prev,
      [name]: "",
    }));
  };

  const validate = () => {
    const validationErrors = {};

    if (!formData.question.trim()) {
      validationErrors.question = "Question is required.";
    }

    if (!formData.answer.trim()) {
      validationErrors.answer = "Answer is required.";
    }

    if (
      formData.displayOrder === "" ||
      Number(formData.displayOrder) <= 0
    ) {
      validationErrors.displayOrder =
        "Display order must be greater than 0.";
    }

    setErrors(validationErrors);

    return Object.keys(validationErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!validate()) return;

    onSubmit({
      ...formData,
      displayOrder: Number(formData.displayOrder),
    });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
      <div className="w-full max-w-2xl rounded-xl bg-white shadow-xl">
        <div className="flex items-center justify-between border-b px-6 py-4">
          <h2 className="text-xl font-semibold">
            {initialData ? "Edit FAQ" : "Add FAQ"}
          </h2>

          <button
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
                Question
              </label>

              <input
                type="text"
                name="question"
                value={formData.question}
                onChange={handleChange}
                className="w-full rounded-lg border px-4 py-2 focus:border-purple-500 focus:outline-none"
              />

              {errors.question && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.question}
                </p>
              )}
            </div>

            <div>
              <label className="mb-2 block font-medium">
                Answer
              </label>

              <textarea
                rows={5}
                name="answer"
                value={formData.answer}
                onChange={handleChange}
                className="w-full rounded-lg border px-4 py-2 focus:border-purple-500 focus:outline-none"
              />

              {errors.answer && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.answer}
                </p>
              )}
            </div>

            <div className="grid gap-5 md:grid-cols-2">
              <div>
                <label className="mb-2 block font-medium">
                  Display Order
                </label>

                <input
                  type="number"
                  min="1"
                  name="displayOrder"
                  value={formData.displayOrder}
                  onChange={handleChange}
                  className="w-full rounded-lg border px-4 py-2 focus:border-purple-500 focus:outline-none"
                />

                {errors.displayOrder && (
                  <p className="mt-1 text-sm text-red-500">
                    {errors.displayOrder}
                  </p>
                )}
              </div>

              <div className="flex items-center pt-8">
                <label className="flex cursor-pointer items-center gap-3">
                  <input
                    type="checkbox"
                    name="published"
                    checked={formData.published}
                    onChange={handleChange}
                  />

                  <span>Published</span>
                </label>
              </div>
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
                ? "Update FAQ"
                : "Create FAQ"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}