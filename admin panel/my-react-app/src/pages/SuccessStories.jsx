import { useEffect, useState, useMemo } from "react";
import {
  FaPlus,
  FaEdit,
  FaTrash,
  FaSearch,
  FaGlobe,
  FaEyeSlash,
  FaHeart,
  FaCheckCircle,
  FaTimesCircle,
  FaMapMarkerAlt,
  FaCalendarAlt,
} from "react-icons/fa";
import { toast } from "react-toastify";
import adminSuccessStoryService from "../services/adminSuccessStoryService";
import SuccessStoryModal from "../components/success-stories/SuccessStoryModal";

export default function SuccessStories() {
  const [stories, setStories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [search, setSearch] = useState("");
  const [publishedFilter, setPublishedFilter] = useState("ALL"); // ALL, PUBLISHED, DRAFT

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [modalOpen, setModalOpen] = useState(false);
  const [selectedStory, setSelectedStory] = useState(null);

  const [actionLoading, setActionLoading] = useState(null);

  useEffect(() => {
    loadStories();
  }, [page, publishedFilter]);

  const loadStories = async () => {
    try {
      setLoading(true);
      const params = {
        page,
        size: 10,
        search: search.trim(),
        sortBy: "displayOrder",
        sortDir: "asc",
      };

      if (publishedFilter === "PUBLISHED") params.published = true;
      if (publishedFilter === "DRAFT") params.published = false;

      const res = await adminSuccessStoryService.getAllStories(params);
      const pageData = res.data ?? res;

      if (pageData && pageData.content) {
        setStories(pageData.content);
        setTotalPages(pageData.totalPages);
        setTotalElements(pageData.totalElements);
      } else if (Array.isArray(pageData)) {
        setStories(pageData);
        setTotalElements(pageData.length);
        setTotalPages(1);
      }
    } catch (error) {
      console.error(error);
      toast.error("Failed to load success stories.");
    } finally {
      setLoading(false);
    }
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    setPage(0);
    loadStories();
  };

  const handleAdd = () => {
    setSelectedStory(null);
    setModalOpen(true);
  };

  const handleEdit = (story) => {
    setSelectedStory(story);
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedStory(null);
    setModalOpen(false);
  };

  const handleSave = async (formData) => {
    try {
      setSaving(true);
      if (selectedStory) {
        await adminSuccessStoryService.updateStory(selectedStory.id, formData);
        toast.success("Success story updated successfully.");
      } else {
        await adminSuccessStoryService.createStory(formData);
        toast.success("Success story created successfully.");
      }
      handleCloseModal();
      await loadStories();
    } catch (error) {
      console.error(error);
      toast.error(error.message || "Unable to save success story.");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id, names) => {
    if (!window.confirm(`Are you sure you want to delete story for ${names}?`)) {
      return;
    }

    try {
      setActionLoading(id);
      await adminSuccessStoryService.deleteStory(id);
      toast.success("Success story deleted successfully.");
      await loadStories();
    } catch (error) {
      console.error(error);
      toast.error(error.message || "Unable to delete success story.");
    } finally {
      setActionLoading(null);
    }
  };

  const handlePublishToggle = async (story) => {
    try {
      setActionLoading(story.id);
      if (story.isPublished) {
        await adminSuccessStoryService.unpublishStory(story.id);
        toast.success("Success story unpublished.");
      } else {
        if (!story.consentGiven) {
          toast.error("Cannot publish story without consent from couple!");
          return;
        }
        await adminSuccessStoryService.publishStory(story.id);
        toast.success("Success Story published. Notifications are being delivered in the background.");
      }
      await loadStories();
    } catch (error) {
      console.error(error);
      toast.error(error.message || "Unable to update story publication status.");
    } finally {
      setActionLoading(null);
    }
  };

  const stats = useMemo(() => {
    const published = stories.filter((s) => s.isPublished).length;
    const consented = stories.filter((s) => s.consentGiven).length;
    return {
      total: totalElements,
      published,
      drafts: totalElements - published,
      consented,
    };
  }, [stories, totalElements]);

  return (
    <div className="p-6">
      {/* Header */}
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
            <FaHeart className="text-pink-500" /> Success Stories
          </h1>
          <p className="text-gray-500">
            Manage couple love stories, consent records, and public showcase settings.
          </p>
        </div>

        <button
          onClick={handleAdd}
          className="flex items-center justify-center gap-2 rounded-xl bg-gradient-to-r from-pink-500 to-rose-600 px-5 py-2.5 font-medium text-white shadow-lg shadow-pink-200 transition hover:from-pink-600 hover:to-rose-700"
        >
          <FaPlus />
          Add Success Story
        </button>
      </div>

      {/* Stats Cards */}
      <div className="mb-6 grid grid-cols-1 gap-4 sm:grid-cols-4">
        <div className="rounded-xl border border-gray-100 bg-white p-4 shadow-sm">
          <p className="text-xs font-semibold text-gray-500">TOTAL STORIES</p>
          <p className="mt-1 text-2xl font-bold text-gray-900">{stats.total}</p>
        </div>
        <div className="rounded-xl border border-emerald-100 bg-emerald-50/50 p-4 shadow-sm">
          <p className="text-xs font-semibold text-emerald-700">PUBLISHED</p>
          <p className="mt-1 text-2xl font-bold text-emerald-800">{stats.published}</p>
        </div>
        <div className="rounded-xl border border-amber-100 bg-amber-50/50 p-4 shadow-sm">
          <p className="text-xs font-semibold text-amber-700">DRAFTS</p>
          <p className="mt-1 text-2xl font-bold text-amber-800">{stats.drafts}</p>
        </div>
        <div className="rounded-xl border border-blue-100 bg-blue-50/50 p-4 shadow-sm">
          <p className="text-xs font-semibold text-blue-700">CONSENT VERIFIED</p>
          <p className="mt-1 text-2xl font-bold text-blue-800">{stats.consented}</p>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="mb-6 flex flex-col gap-4 rounded-xl bg-white p-4 shadow-sm sm:flex-row sm:items-center sm:justify-between">
        <form onSubmit={handleSearchSubmit} className="flex flex-1 items-center gap-3 rounded-lg border bg-gray-50 px-3 py-2">
          <FaSearch className="text-gray-400" />
          <input
            type="text"
            placeholder="Search by partner names, location..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full bg-transparent text-sm outline-none"
          />
          <button type="submit" className="rounded-md bg-gray-200 px-3 py-1 text-xs font-semibold text-gray-700 hover:bg-gray-300">
            Search
          </button>
        </form>

        <div className="flex items-center gap-2">
          <span className="text-xs font-semibold text-gray-500">STATUS:</span>
          {["ALL", "PUBLISHED", "DRAFT"].map((filter) => (
            <button
              key={filter}
              onClick={() => {
                setPublishedFilter(filter);
                setPage(0);
              }}
              className={`rounded-lg px-3 py-1.5 text-xs font-semibold transition ${
                publishedFilter === filter
                  ? "bg-pink-600 text-white"
                  : "bg-gray-100 text-gray-600 hover:bg-gray-200"
              }`}
            >
              {filter}
            </button>
          ))}
        </div>
      </div>

      {/* Content Table / Cards */}
      {loading ? (
        <div className="rounded-xl bg-white p-12 text-center text-gray-500 shadow-sm">
          Loading success stories...
        </div>
      ) : stories.length === 0 ? (
        <div className="rounded-xl bg-white p-12 text-center shadow-sm">
          <FaHeart className="mx-auto text-4xl text-gray-300" />
          <h3 className="mt-3 text-lg font-bold text-gray-700">No Success Stories Found</h3>
          <p className="mt-1 text-sm text-gray-500">
            {search ? "No matches found for your search." : "Get started by adding your first matrimony success story."}
          </p>
        </div>
      ) : (
        <div className="overflow-hidden rounded-xl border border-gray-200 bg-white shadow-sm">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50 text-left text-xs font-bold uppercase tracking-wider text-gray-500">
              <tr>
                <th className="px-6 py-4">Couple</th>
                <th className="px-6 py-4">Wedding / Location</th>
                <th className="px-6 py-4 text-center">Consent</th>
                <th className="px-6 py-4 text-center">Status</th>
                <th className="px-6 py-4 text-center">Order</th>
                <th className="px-6 py-4 text-right">Actions</th>
              </tr>
            </thead>

            <tbody className="divide-y divide-gray-100 bg-white text-sm">
              {stories.map((story) => (
                <tr key={story.id} className="transition hover:bg-pink-50/40">
                  {/* Couple Name & Photo */}
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <div className="h-12 w-12 flex-shrink-0 overflow-hidden rounded-lg bg-pink-100 border">
                        {story.coupleImageUrl || story.partnerOneImageUrl ? (
                          <img
                            src={story.coupleImageUrl || story.partnerOneImageUrl}
                            alt={`${story.partnerOneName} & ${story.partnerTwoName}`}
                            className="h-full w-full object-cover"
                          />
                        ) : (
                          <div className="flex h-full w-full items-center justify-center text-pink-400 font-bold text-xs">
                            ❤️
                          </div>
                        )}
                      </div>
                      <div>
                        <div className="font-bold text-gray-900">
                          {story.partnerOneName} & {story.partnerTwoName}
                        </div>
                        <p className="text-xs text-gray-500 line-clamp-1 max-w-xs">
                          {story.shortStory}
                        </p>
                      </div>
                    </div>
                  </td>

                  {/* Wedding Date & Location */}
                  <td className="px-6 py-4 text-gray-600">
                    {story.weddingDate && (
                      <div className="flex items-center gap-1.5 text-xs">
                        <FaCalendarAlt className="text-pink-500" />
                        <span>{story.weddingDate}</span>
                      </div>
                    )}
                    {story.location && (
                      <div className="flex items-center gap-1.5 text-xs text-gray-500 mt-1">
                        <FaMapMarkerAlt className="text-gray-400" />
                        <span>{story.location}</span>
                      </div>
                    )}
                  </td>

                  {/* Consent Badge */}
                  <td className="px-6 py-4 text-center">
                    <span
                      className={`inline-flex items-center gap-1 rounded-full px-2.5 py-1 text-xs font-semibold ${
                        story.consentGiven
                          ? "bg-green-100 text-green-700"
                          : "bg-red-100 text-red-700"
                      }`}
                    >
                      {story.consentGiven ? (
                        <>
                          <FaCheckCircle className="text-green-600" /> Verified
                        </>
                      ) : (
                        <>
                          <FaTimesCircle className="text-red-600" /> Pending
                        </>
                      )}
                    </span>
                  </td>

                  {/* Publication Status Badge */}
                  <td className="px-6 py-4 text-center">
                    <span
                      className={`inline-flex items-center gap-1 rounded-full px-2.5 py-1 text-xs font-semibold ${
                        story.isPublished
                          ? "bg-emerald-100 text-emerald-800"
                          : "bg-amber-100 text-amber-800"
                      }`}
                    >
                      {story.isPublished ? "Published" : "Draft"}
                    </span>
                  </td>

                  {/* Display Order */}
                  <td className="px-6 py-4 text-center font-mono font-medium text-gray-600">
                    {story.displayOrder ?? 0}
                  </td>

                  {/* Actions */}
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-3">
                      <button
                        onClick={() => handlePublishToggle(story)}
                        disabled={actionLoading === story.id}
                        className={`rounded-lg p-2 transition ${
                          story.isPublished
                            ? "bg-amber-50 text-amber-600 hover:bg-amber-100"
                            : "bg-emerald-50 text-emerald-600 hover:bg-emerald-100"
                        }`}
                        title={story.isPublished ? "Unpublish Story" : "Publish Story"}
                      >
                        {story.isPublished ? <FaEyeSlash /> : <FaGlobe />}
                      </button>

                      <button
                        onClick={() => handleEdit(story)}
                        className="rounded-lg bg-blue-50 p-2 text-blue-600 hover:bg-blue-100"
                        title="Edit Story"
                      >
                        <FaEdit />
                      </button>

                      <button
                        onClick={() =>
                          handleDelete(
                            story.id,
                            `${story.partnerOneName} & ${story.partnerTwoName}`
                          )
                        }
                        disabled={actionLoading === story.id}
                        className="rounded-lg bg-red-50 p-2 text-red-600 hover:bg-red-100"
                        title="Delete Story"
                      >
                        <FaTrash />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Pagination Footer */}
          {totalPages > 1 && (
            <div className="flex items-center justify-between border-t bg-gray-50 px-6 py-3 text-xs text-gray-600">
              <span>
                Showing Page {page + 1} of {totalPages} ({totalElements} stories total)
              </span>

              <div className="flex items-center gap-2">
                <button
                  disabled={page === 0}
                  onClick={() => setPage((prev) => Math.max(0, prev - 1))}
                  className="rounded-md border bg-white px-3 py-1 font-medium disabled:opacity-50 hover:bg-gray-100"
                >
                  Previous
                </button>
                <button
                  disabled={page >= totalPages - 1}
                  onClick={() => setPage((prev) => prev + 1)}
                  className="rounded-md border bg-white px-3 py-1 font-medium disabled:opacity-50 hover:bg-gray-100"
                >
                  Next
                </button>
              </div>
            </div>
          )}
        </div>
      )}

      {/* Modal */}
      <SuccessStoryModal
        open={modalOpen}
        onClose={handleCloseModal}
        onSubmit={handleSave}
        loading={saving}
        initialData={selectedStory}
      />
    </div>
  );
}
