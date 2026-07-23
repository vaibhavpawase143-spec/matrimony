import { useEffect, useState } from "react";
import {
  FaSearch,
  FaEye,
  FaCheckCircle,
  FaTimesCircle,
  FaClipboardCheck,
  FaDownload,
} from "react-icons/fa";
import ExportDropdown from "../components/common/ExportDropdown";
import { exportToCSV } from "../utils/export/csvExport";
import { exportToExcel } from "../utils/export/excelExport";
import { exportToPDF } from "../utils/export/pdfExport";
import { toast } from "sonner";

import {
  getReports,
  getReportById,
  reviewReport,
  approveReport,
  rejectReport,
  getReportStatistics,
} from "../services/reportService";

export default function ReportedProfiles() {
  // ==========================================================
  // STATE
  // ==========================================================

  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);

  const [search, setSearch] = useState("");

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize] = useState(10);

  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [selectedReport, setSelectedReport] = useState(null);
  const [isDetailsOpen, setIsDetailsOpen] = useState(false);

  const [statistics, setStatistics] = useState({
    totalReports: 0,
    pending: 0,
    underReview: 0,
    approved: 0,
    rejected: 0,
  });

  // ==========================================================
  // LOAD REPORTS
  // ==========================================================

  const loadReports = async (
    page = currentPage,
    searchText = search
  ) => {
    try {
      setLoading(true);

      const data = await getReports(
        page,
        pageSize,
        searchText
      );

      setReports(data?.content || []);
      setCurrentPage(data?.number ?? 0);
      setTotalPages(data?.totalPages ?? 0);
      setTotalElements(data?.totalElements ?? 0);
    } catch (error) {
      console.error(error);

      setReports([]);
      setTotalPages(0);
      setTotalElements(0);

      toast.error(
        error?.message || "Failed to load reports."
      );
    } finally {
      setLoading(false);
    }
  };

  // ==========================================================
  // LOAD STATISTICS
  // ==========================================================

  const loadStatistics = async () => {
    try {
      const stats = await getReportStatistics();

      setStatistics({
        totalReports: stats?.totalReports ?? 0,
        pending: stats?.pendingReports ?? 0,
        underReview: stats?.underReviewReports ?? 0,
        approved: stats?.approvedReports ?? 0,
        rejected: stats?.rejectedReports ?? 0,
      });
    } catch (error) {
      console.error(error);
    }
  };

  // ==========================================================
  // INITIAL LOAD
  // ==========================================================

  useEffect(() => {
    loadReports(currentPage, search);
  }, [currentPage]);

  useEffect(() => {
    loadStatistics();
  }, []);

  // ==========================================================
  // SEARCH
  // ==========================================================

  const handleSearchChange = (e) => {
    const value = e.target.value;

    setSearch(value);
    setCurrentPage(0);

    loadReports(0, value);
  };

  // ==========================================================
  // VIEW REPORT
  // ==========================================================

  const handleView = async (report) => {
    try {
      const data = await getReportById(report.id);

      setSelectedReport(data);
      setIsDetailsOpen(true);
    } catch (error) {
      toast.error(
        error?.message ||
          "Unable to load report details."
      );
    }
  };

  // ==========================================================
  // REVIEW
  // ==========================================================

  const handleReview = async (report) => {
    try {
      await reviewReport(report.id);

      toast.success("Report marked Under Review.");

      loadReports();
      loadStatistics();
    } catch (error) {
      toast.error(
        error?.message ||
          "Unable to review report."
      );
    }
  };

  // ==========================================================
  // APPROVE
  // ==========================================================

  const handleApprove = async (report) => {
    try {
      await approveReport(report.id);

      toast.success("Report approved.");

      loadReports();
      loadStatistics();
    } catch (error) {
      toast.error(
        error?.message ||
          "Unable to approve report."
      );
    }
  };

  // ==========================================================
  // REJECT
  // ==========================================================

  const handleReject = async (report) => {
    try {
      await rejectReport(report.id);

      toast.success("Report rejected.");

      loadReports();
      loadStatistics();
    } catch (error) {
      toast.error(
        error?.message ||
          "Unable to reject report."
      );
    }
  };
  // ==========================================================
  // EXPORT REPORTS
  // ==========================================================

const reportColumns = [
  {
    label: "Reporter",
    value: (report) => report.reporterName ?? "-",
  },
  {
    label: "Reported User",
    value: (report) => report.reportedUserName ?? "-",
  },
  {
    label: "Reason",
    value: (report) => report.reason ?? "-",
  },
  {
    label: "Status",
    value: (report) => report.status ?? "-",
  },
  {
    label: "Reported Date",
    value: (report) =>
      report.createdAt
        ? new Date(report.createdAt).toLocaleDateString()
        : "-",
  },
];
const handleExportCSV = () => {
  exportToCSV({
    data: reports,
    columns: reportColumns,
    fileName: "Reported_Profiles_Report",
  });
};

const handleExportExcel = () => {
  exportToExcel({
    data: reports,
    columns: reportColumns,
    fileName: "Reported_Profiles_Report",
  });
};

const handleExportPDF = () => {
  exportToPDF({
    data: reports,
    columns: reportColumns,
    title: "Reported Profiles Report",
    fileName: "Reported_Profiles_Report",
  });
};


  // ==========================================================
  // LOADING
  // ==========================================================

  if (loading && reports.length === 0) {
    return (
      <div className="flex items-center justify-center h-96 text-lg text-gray-500">
        Loading reported profiles...
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6">

      {/* ===================================================== */}
      {/* HEADER */}
      {/* ===================================================== */}

      <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">

        <div>

          <h1 className="text-3xl font-bold text-gray-800">
            Reported Profiles
          </h1>

          <p className="text-gray-500 mt-1">
            Review, investigate and take action on reported profiles.
          </p>

        </div>

    <ExportDropdown
        onPDF={handleExportPDF}
        onCSV={handleExportCSV}
        onExcel={handleExportExcel}
    />

      </div>

      {/* ===================================================== */}
      {/* STATISTICS */}
      {/* ===================================================== */}

      <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-5 gap-5">

        <div className="bg-white rounded-xl shadow border p-5">
          <p className="text-gray-500 text-sm">
            Total Reports
          </p>

          <h2 className="text-3xl font-bold text-violet-700 mt-2">
            {statistics.totalReports}
          </h2>
        </div>

        <div className="bg-white rounded-xl shadow border p-5">
          <p className="text-gray-500 text-sm">
            Pending
          </p>

          <h2 className="text-3xl font-bold text-yellow-600 mt-2">
            {statistics.pending}
          </h2>
        </div>

        <div className="bg-white rounded-xl shadow border p-5">
          <p className="text-gray-500 text-sm">
            Under Review
          </p>

          <h2 className="text-3xl font-bold text-blue-600 mt-2">
            {statistics.underReview}
          </h2>
        </div>

        <div className="bg-white rounded-xl shadow border p-5">
          <p className="text-gray-500 text-sm">
            Approved
          </p>

          <h2 className="text-3xl font-bold text-green-600 mt-2">
            {statistics.approved}
          </h2>
        </div>

        <div className="bg-white rounded-xl shadow border p-5">
          <p className="text-gray-500 text-sm">
            Rejected
          </p>

          <h2 className="text-3xl font-bold text-red-600 mt-2">
            {statistics.rejected}
          </h2>
        </div>

      </div>

      {/* ===================================================== */}
      {/* SEARCH */}
      {/* ===================================================== */}

      <div className="bg-white rounded-xl shadow border p-4">

        <div className="relative">

          <FaSearch className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />

          <input
            type="text"
            placeholder="Search reports..."
            value={search}
            onChange={handleSearchChange}
            className="w-full pl-11 pr-4 py-3 border rounded-lg outline-none focus:ring-2 focus:ring-violet-500"
          />

        </div>

      </div>

      {/* ===================================================== */}
      {/* TABLE */}
      {/* ===================================================== */}

      <div className="bg-white rounded-xl shadow border overflow-hidden">

        <div className="overflow-x-auto">

          <table className="min-w-full">

            <thead className="bg-violet-700 text-white">

              <tr>

                <th className="px-6 py-4 text-left">
                  Reporter
                </th>

                <th className="px-6 py-4 text-left">
                  Reported User
                </th>

                <th className="px-6 py-4 text-left">
                  Reason
                </th>

                <th className="px-6 py-4 text-left">
                  Status
                </th>

                <th className="px-6 py-4 text-left">
                  Date
                </th>

                <th className="px-6 py-4 text-center">
                  Actions
                </th>

              </tr>

            </thead>

            <tbody>

              {reports.length === 0 ? (

                <tr>

                  <td
                    colSpan={6}
                    className="py-12 text-center text-gray-500"
                  >
                    No reported profiles found.
                  </td>

                </tr>

              ) : (

                reports.map((report, index) => (

                  <tr
                    key={report.id}
                    className={`border-b hover:bg-violet-50 ${
                      index % 2 === 0
                        ? "bg-white"
                        : "bg-gray-50"
                    }`}
                  >
                                    <td className="px-6 py-4">
                                      <div className="font-medium text-gray-900">
                                        {report.reporterName || "-"}
                                      </div>
                                    </td>

                                    <td className="px-6 py-4">
                                      <div className="font-medium text-gray-900">
                                        {report.reportedUserName || "-"}
                                      </div>
                                    </td>

                                    <td className="px-6 py-4 max-w-xs">
                                      <span className="line-clamp-2">
                                        {report.reason || "-"}
                                      </span>
                                    </td>

                                    <td className="px-6 py-4">
                                      <span
                                        className={`inline-flex items-center rounded-full px-3 py-1 text-xs font-semibold
                                        ${
                                          report.status === "PENDING"
                                            ? "bg-yellow-100 text-yellow-700"
                                            : report.status === "UNDER_REVIEW"
                                            ? "bg-blue-100 text-blue-700"
                                            : report.status === "APPROVED"
                                            ? "bg-green-100 text-green-700"
                                            : "bg-red-100 text-red-700"
                                        }`}
                                      >
                                        {report.status}
                                      </span>
                                    </td>

                                    <td className="px-6 py-4">
                                      {report.createdAt
                                        ? new Date(
                                            report.createdAt
                                          ).toLocaleDateString()
                                        : "-"}
                                    </td>

                                    <td className="px-6 py-4">

                                      <div className="flex justify-center gap-2">

                                        <button
                                          onClick={() => handleView(report)}
                                          title="View"
                                          className="p-2 rounded-lg bg-violet-100 text-violet-700 hover:bg-violet-200"
                                        >
                                          <FaEye />
                                        </button>

                                        {report.status === "PENDING" && (
                                          <button
                                            onClick={() =>
                                              handleReview(report)
                                            }
                                            title="Under Review"
                                            className="p-2 rounded-lg bg-blue-100 text-blue-700 hover:bg-blue-200"
                                          >
                                            <FaClipboardCheck />
                                          </button>
                                        )}

                                        {report.status !== "APPROVED" && (
                                          <button
                                            onClick={() =>
                                              handleApprove(report)
                                            }
                                            title="Approve"
                                            className="p-2 rounded-lg bg-green-100 text-green-700 hover:bg-green-200"
                                          >
                                            <FaCheckCircle />
                                          </button>
                                        )}

                                        {report.status !== "REJECTED" && (
                                          <button
                                            onClick={() =>
                                              handleReject(report)
                                            }
                                            title="Reject"
                                            className="p-2 rounded-lg bg-red-100 text-red-700 hover:bg-red-200"
                                          >
                                            <FaTimesCircle />
                                          </button>
                                        )}

                                      </div>

                                    </td>

                                  </tr>

                                ))

                              )}

                            </tbody>

                          </table>

                        </div>

                        {/* ===================================================== */}
                        {/* PAGINATION */}
                        {/* ===================================================== */}

                        <div className="flex flex-col md:flex-row justify-between items-center gap-4 border-t px-6 py-4">

                          <p className="text-sm text-gray-600">
                            Showing{" "}
                            <strong>
                              {reports.length}
                            </strong>{" "}
                            of{" "}
                            <strong>
                              {totalElements}
                            </strong>{" "}
                            reports
                          </p>

                          <div className="flex gap-2">

                            <button
                              disabled={currentPage === 0}
                              onClick={() =>
                                setCurrentPage((prev) =>
                                  Math.max(prev - 1, 0)
                                )
                              }
                              className="px-4 py-2 rounded-lg border disabled:opacity-50 hover:bg-gray-100"
                            >
                              Previous
                            </button>

                            <span className="px-4 py-2 font-medium">
                              Page {currentPage + 1} of{" "}
                              {Math.max(totalPages, 1)}
                            </span>

                            <button
                              disabled={currentPage >= totalPages - 1}
                              onClick={() =>
                                setCurrentPage((prev) =>
                                  prev + 1
                                )
                              }
                              className="px-4 py-2 rounded-lg border disabled:opacity-50 hover:bg-gray-100"
                            >
                              Next
                            </button>

                          </div>

                        </div>

                      </div>

                      {/* ===================================================== */}
                      {/* REPORT DETAILS MODAL */}
                      {/* ===================================================== */}

                      {isDetailsOpen && selectedReport && (
                                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">

                                  <div className="w-full max-w-2xl rounded-xl bg-white shadow-2xl">

                                    {/* ================= HEADER ================= */}

                                    <div className="flex items-center justify-between border-b px-6 py-4">

                                      <h2 className="text-xl font-bold text-gray-800">
                                        Report Details
                                      </h2>

                                      <button
                                        onClick={() => {
                                          setIsDetailsOpen(false);
                                          setSelectedReport(null);
                                        }}
                                        className="text-gray-500 hover:text-red-600 text-xl"
                                      >
                                        ✕
                                      </button>

                                    </div>

                                    {/* ================= BODY ================= */}

                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-5 p-6">

                                      <div>
                                        <p className="text-sm text-gray-500">
                                          Reporter
                                        </p>

                                        <p className="font-semibold">
                                          {selectedReport.reporterName || "-"}
                                        </p>
                                      </div>

                                      <div>
                                        <p className="text-sm text-gray-500">
                                          Reported User
                                        </p>

                                        <p className="font-semibold">
                                          {selectedReport.reportedUserName || "-"}
                                        </p>
                                      </div>

                                      <div>
                                        <p className="text-sm text-gray-500">
                                          Status
                                        </p>

                                        <span
                                          className={`inline-flex rounded-full px-3 py-1 text-sm font-semibold
                                          ${
                                            selectedReport.status === "PENDING"
                                              ? "bg-yellow-100 text-yellow-700"
                                              : selectedReport.status === "UNDER_REVIEW"
                                              ? "bg-blue-100 text-blue-700"
                                              : selectedReport.status === "APPROVED"
                                              ? "bg-green-100 text-green-700"
                                              : "bg-red-100 text-red-700"
                                          }`}
                                        >
                                          {selectedReport.status}
                                        </span>
                                      </div>

                                      <div>
                                        <p className="text-sm text-gray-500">
                                          Report Date
                                        </p>

                                        <p className="font-semibold">
                                          {selectedReport.createdAt
                                            ? new Date(
                                                selectedReport.createdAt
                                              ).toLocaleString()
                                            : "-"}
                                        </p>
                                      </div>

                                      <div className="md:col-span-2">
                                        <p className="text-sm text-gray-500 mb-2">
                                          Reason
                                        </p>

                                        <div className="rounded-lg border bg-gray-50 p-4">
                                          {selectedReport.reason || "-"}
                                        </div>
                                      </div>

                                      {selectedReport.description && (
                                        <div className="md:col-span-2">
                                          <p className="text-sm text-gray-500 mb-2">
                                            Description
                                          </p>

                                          <div className="rounded-lg border bg-gray-50 p-4 whitespace-pre-wrap">
                                            {selectedReport.description}
                                          </div>
                                        </div>
                                      )}

                                    </div>

                                    {/* ================= FOOTER ================= */}

                                    <div className="flex justify-end gap-3 border-t px-6 py-4">

                                      <button
                                        onClick={() => {
                                          setIsDetailsOpen(false);
                                          setSelectedReport(null);
                                        }}
                                        className="rounded-lg border px-5 py-2 hover:bg-gray-100"
                                      >
                                        Close
                                      </button>

                                    </div>

                                  </div>

                                </div>

                              )}

                            </div>
                          );
                        }