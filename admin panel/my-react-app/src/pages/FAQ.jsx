import { useEffect, useMemo, useState } from "react";
import {
  FaPlus,
  FaEdit,
  FaTrash,
  FaSearch,
  FaEye,
  FaEyeSlash,
} from "react-icons/fa";
import { toast } from "react-toastify";

import adminFaqService from "../services/adminFaqService";
import FaqModal from "../components/faq/FaqModal";

export default function FAQs() {
  const [faqs, setFaqs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [search, setSearch] = useState("");

  const [modalOpen, setModalOpen] = useState(false);
  const [selectedFaq, setSelectedFaq] = useState(null);

  const [deleteLoading, setDeleteLoading] = useState(null);
  const [publishLoading, setPublishLoading] = useState(null);

  const filteredFaqs = useMemo(() => {
    const keyword = search.trim().toLowerCase();

    if (!keyword) return faqs;

    return faqs.filter(
      (faq) =>
        faq.question?.toLowerCase().includes(keyword) ||
        faq.answer?.toLowerCase().includes(keyword)
    );
  }, [faqs, search]);

  useEffect(() => {
    loadFaqs();
  }, []);
  const loadFaqs = async () => {
    try {
      setLoading(true);

      const response = await adminFaqService.getAllFaqs();

      setFaqs(response.data ?? []);
    } catch (error) {
      console.error("Failed to load FAQs", error);
      toast.error("Failed to load FAQs");
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setSelectedFaq(null);
    setModalOpen(true);
  };

  const handleEdit = (faq) => {
    setSelectedFaq(faq);
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedFaq(null);
    setModalOpen(false);
  };

  const handleSave = async (formData) => {
    try {
      setSaving(true);

      if (selectedFaq) {
        await adminFaqService.updateFaq(selectedFaq.id, formData);

        toast.success("FAQ updated successfully");
      } else {
        await adminFaqService.createFaq(formData);

        toast.success("FAQ created successfully");
      }

      handleCloseModal();

      await loadFaqs();
    } catch (error) {
      console.error(error);

      toast.error(
        error?.response?.data?.message ||
          "Unable to save FAQ."
      );
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this FAQ?"
    );

    if (!confirmed) return;

    try {
      setDeleteLoading(id);

      await adminFaqService.deleteFaq(id);

      toast.success("FAQ deleted successfully");

      await loadFaqs();
    } catch (error) {
      console.error(error);

      toast.error(
        error?.response?.data?.message ||
          "Failed to delete FAQ."
      );
    } finally {
      setDeleteLoading(null);
    }
  };

  const handlePublishToggle = async (faq) => {
    try {
      setPublishLoading(faq.id);

      if (faq.published) {
        await adminFaqService.unpublishFaq(faq.id);

        toast.success("FAQ unpublished");
      } else {
        await adminFaqService.publishFaq(faq.id);

        toast.success("FAQ published");
      }

      await loadFaqs();
    } catch (error) {
      console.error(error);

      toast.error(
        error?.response?.data?.message ||
          "Failed to update FAQ status."
      );
    } finally {
      setPublishLoading(null);
    }
  };
  return (
    <div className="p-6">
      {/* Header */}
      <div className="mb-6 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            FAQ Management
          </h1>
          <p className="mt-1 text-gray-500">
            Manage Frequently Asked Questions
          </p>
        </div>

        <button
          onClick={handleAdd}
          className="flex items-center gap-2 rounded-lg bg-purple-600 px-5 py-2.5 font-medium text-white transition hover:bg-purple-700"
        >
          <FaPlus />
          Add FAQ
        </button>
      </div>

      {/* Search */}
      <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">
        <div className="flex items-center gap-3">
          <FaSearch className="text-gray-400" />

          <input
            type="text"
            placeholder="Search FAQs..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full border-none outline-none"
          />
        </div>
      </div>

      {/* Loading */}
      {loading ? (
        <div className="rounded-xl bg-white p-10 text-center shadow">
          <p className="text-gray-500">Loading FAQs...</p>
        </div>
      ) : filteredFaqs.length === 0 ? (
        /* Empty State */
        <div className="rounded-xl bg-white p-10 text-center shadow">
          <h3 className="text-lg font-semibold text-gray-700">
            No FAQs Found
          </h3>

          <p className="mt-2 text-gray-500">
            Try changing your search or add a new FAQ.
          </p>
        </div>
      ) : (
        <div className="overflow-x-auto rounded-xl border bg-white shadow">
          <table className="min-w-full">
            <thead className="bg-purple-600 text-white">
              <tr>
                <th className="px-5 py-3 text-left">Question</th>
                <th className="px-5 py-3 text-left">Answer</th>
                <th className="px-5 py-3 text-center">
                  Display Order
                </th>
                <th className="px-5 py-3 text-center">
                  Status
                </th>
                <th className="px-5 py-3 text-center">
                  Actions
                </th>
              </tr>
            </thead>

            <tbody>
              {filteredFaqs.map((faq) => (
                <tr
                  key={faq.id}
                  className="border-t hover:bg-gray-50"
                >
                  <td className="px-5 py-4 font-medium">
                    {faq.question}
                  </td>

                  <td className="max-w-md px-5 py-4">
                    <p className="line-clamp-2">
                      {faq.answer}
                    </p>
                  </td>

                  <td className="px-5 py-4 text-center">
                    {faq.displayOrder}
                  </td>

                  <td className="px-5 py-4 text-center">
                    <span
                      className={`rounded-full px-3 py-1 text-xs font-semibold text-white ${
                        faq.published
                          ? "bg-green-600"
                          : "bg-yellow-500"
                      }`}
                    >
                      {faq.published
                        ? "Published"
                        : "Draft"}
                    </span>
                  </td>

                  <td className="px-5 py-4">
                    <div className="flex items-center justify-center gap-3">
                      <button
                        onClick={() => handleEdit(faq)}
                        className="text-blue-600 transition hover:text-blue-800"
                        title="Edit FAQ"
                      >
                        <FaEdit />
                      </button>

                      <button
                        onClick={() =>
                          handlePublishToggle(faq)
                        }
                        disabled={
                          publishLoading === faq.id
                        }
                        className="text-green-600 transition hover:text-green-800 disabled:opacity-50"
                        title={
                          faq.published
                            ? "Unpublish"
                            : "Publish"
                        }
                      >
                        {faq.published ? (
                          <FaEyeSlash />
                        ) : (
                          <FaEye />
                        )}
                      </button>

                      <button
                        onClick={() =>
                          handleDelete(faq.id)
                        }
                        disabled={
                          deleteLoading === faq.id
                        }
                        className="text-red-600 transition hover:text-red-800 disabled:opacity-50"
                        title="Delete FAQ"
                      >
                        <FaTrash />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="border-t bg-gray-50 px-5 py-3 text-sm text-gray-500">
            Total FAQs : {filteredFaqs.length}
          </div>
        </div>
      )}
        <FaqModal
          open={modalOpen}
          onClose={handleCloseModal}
          onSubmit={handleSave}
          loading={saving}
          initialData={selectedFaq}
        />
      </div>
    );
  }