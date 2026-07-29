import { useEffect, useMemo, useState } from "react";
import {
  FaPlus,
  FaEdit,
  FaTrash,
  FaSearch,
  FaGlobe,
  FaGlobeAmericas,
} from "react-icons/fa";
import { toast } from "react-toastify";

import adminCmsService from "../services/adminCmsService";
import CmsModal from "../components/cms/CmsModal";

export default function CMSPages() {
  const [pages, setPages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [search, setSearch] = useState("");

  const [modalOpen, setModalOpen] = useState(false);
  const [selectedPage, setSelectedPage] = useState(null);

  const [deleteLoading, setDeleteLoading] = useState(null);
  const [publishLoading, setPublishLoading] = useState(null);

  const filteredPages = useMemo(() => {
    const keyword = search.trim().toLowerCase();

    if (!keyword) return pages;

    return pages.filter(
      (page) =>
        page.title?.toLowerCase().includes(keyword) ||
        page.pageKey?.toLowerCase().includes(keyword)
    );
  }, [pages, search]);

  useEffect(() => {
    loadPages();
  }, []);

  const loadPages = async () => {
    try {
      setLoading(true);

      const response = await adminCmsService.getAllPages();

      setPages(response.data ?? response ?? []);
    } catch (error) {
      console.error(error);
      toast.error("Failed to load CMS pages.");
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setSelectedPage(null);
    setModalOpen(true);
  };

  const handleEdit = (page) => {
    setSelectedPage(page);
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedPage(null);
    setModalOpen(false);
  };

  const handleSave = async (formData) => {
    try {
      setSaving(true);

      if (selectedPage) {
        await adminCmsService.updatePage(selectedPage.id, formData);

        toast.success("CMS page updated successfully.");
      } else {
        await adminCmsService.createPage(formData);

        toast.success("CMS page created successfully.");
      }

      handleCloseModal();

      await loadPages();
    } catch (error) {
      console.error(error);

      toast.error(
        error.message || "Unable to save CMS page."
      );
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this CMS page?")) return;

    try {
      setDeleteLoading(id);

      await adminCmsService.deletePage(id);

      toast.success("CMS page deleted successfully.");

      await loadPages();
    } catch (error) {
      console.error(error);

      toast.error(
        error.message || "Unable to delete CMS page."
      );
    } finally {
      setDeleteLoading(null);
    }
  };

  const handlePublishToggle = async (page) => {
    try {
      setPublishLoading(page.id);

      if (page.published) {
        await adminCmsService.unpublishPage(page.id);

        toast.success("CMS page unpublished.");
      } else {
        await adminCmsService.publishPage(page.id);

        toast.success("CMS page published.");
      }

      await loadPages();
    } catch (error) {
      console.error(error);

      toast.error(
        error.message || "Unable to update page."
      );
    } finally {
      setPublishLoading(null);
    }
  };

  return (
    <div className="p-6">

      {/* Header */}

      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">
            CMS Pages
          </h1>

          <p className="text-gray-500">
            Manage website content pages.
          </p>
        </div>

        <button
          onClick={handleAdd}
          className="flex items-center gap-2 rounded-lg bg-purple-600 px-5 py-2 text-white hover:bg-purple-700"
        >
          <FaPlus />
          Create Page
        </button>
      </div>

      {/* Search */}

      <div className="mb-6 rounded-xl bg-white p-4 shadow">
        <div className="flex items-center gap-3">
          <FaSearch className="text-gray-400" />

          <input
            type="text"
            placeholder="Search CMS pages..."
            value={search}
            onChange={(e) =>
              setSearch(e.target.value)
            }
            className="w-full outline-none"
          />
        </div>
      </div>

      {loading ? (
        <div className="rounded-xl bg-white p-10 text-center shadow">
          Loading CMS pages...
        </div>
      ) : filteredPages.length === 0 ? (
        <div className="rounded-xl bg-white p-10 text-center shadow">
          <h3 className="text-xl font-semibold">
            No CMS Pages Found
          </h3>

          <p className="mt-2 text-gray-500">
            Create your first CMS page.
          </p>
        </div>
      ) : (
                <div className="overflow-hidden rounded-xl border border-gray-200 bg-white shadow">
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-purple-600 text-white">
                      <tr>
                        <th className="px-6 py-4 text-left text-sm font-semibold">
                          Title
                        </th>

                        <th className="px-6 py-4 text-left text-sm font-semibold">
                          Page Key
                        </th>

                        <th className="px-6 py-4 text-center text-sm font-semibold">
                          Published
                        </th>

                        <th className="px-6 py-4 text-center text-sm font-semibold">
                          Status
                        </th>

                        <th className="px-6 py-4 text-center text-sm font-semibold">
                          Actions
                        </th>
                      </tr>
                    </thead>

                    <tbody className="divide-y divide-gray-100 bg-white">
                      {filteredPages.map((page) => (
                        <tr
                          key={page.id}
                          className="transition hover:bg-purple-50"
                        >
                          <td className="px-6 py-4">
                            <div className="font-medium text-gray-900">
                              {page.title}
                            </div>
                          </td>

                          <td className="px-6 py-4 text-gray-600">
                            {page.pageKey}
                          </td>

                          <td className="px-6 py-4 text-center">
                            <span
                              className={`rounded-full px-3 py-1 text-xs font-semibold text-white ${
                                page.published
                                  ? "bg-green-500"
                                  : "bg-yellow-500"
                              }`}
                            >
                              {page.published ? "Published" : "Draft"}
                            </span>
                          </td>

                          <td className="px-6 py-4 text-center">
                            <span
                              className={`rounded-full px-3 py-1 text-xs font-semibold text-white ${
                                page.isActive
                                  ? "bg-blue-500"
                                  : "bg-red-500"
                              }`}
                            >
                              {page.isActive ? "Active" : "Inactive"}
                            </span>
                          </td>

                          <td className="px-6 py-4">
                            <div className="flex items-center justify-center gap-4">
                              <button
                                onClick={() => handleEdit(page)}
                                className="text-blue-600 transition hover:text-blue-800"
                                title="Edit"
                              >
                                <FaEdit />
                              </button>

                              <button
                                onClick={() => handlePublishToggle(page)}
                                disabled={publishLoading === page.id}
                                className={`transition ${
                                  page.published
                                    ? "text-yellow-600 hover:text-yellow-800"
                                    : "text-green-600 hover:text-green-800"
                                }`}
                                title={
                                  page.published
                                    ? "Unpublish"
                                    : "Publish"
                                }
                              >
                                {page.published ? (
                                  <FaGlobe />
                                ) : (
                                  <FaGlobeAmericas />
                                )}
                              </button>

                              <button
                                onClick={() => handleDelete(page.id)}
                                disabled={deleteLoading === page.id}
                                className="text-red-600 transition hover:text-red-800"
                                title="Delete"
                              >
                                <FaTrash />
                              </button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>

                  <div className="border-t bg-gray-50 px-6 py-3 text-sm text-gray-600">
                    Total CMS Pages : {filteredPages.length}
                  </div>
                </div>
              )}

              <CmsModal
                open={modalOpen}
                onClose={handleCloseModal}
                onSubmit={handleSave}
                loading={saving}
                initialData={selectedPage}
              />
            </div>
          );
        }