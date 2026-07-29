import { useEffect, useState } from "react";
import {
    Search,
    Eye,
    RefreshCw
} from "lucide-react";
import ExportDropdown from "../components/common/ExportDropdown";
import { exportToPDF } from "../utils/export/pdfExport";
import { exportToCSV } from "../utils/export/csvExport";
import { exportToExcel } from "../utils/export/excelExport";

import { toast } from "react-toastify";
import adminAuditLogService from "../services/adminAuditLogService";
import AuditLogDetailsModal from "../components/audit/AuditLogDetailsModal";

const AuditLog = () => {

    const [logs, setLogs] = useState([]);
    const [loading, setLoading] = useState(false);

    const [selectedLog, setSelectedLog] = useState(null);
    const [modalOpen, setModalOpen] = useState(false);

    const [page, setPage] = useState(0);
    const [size,setSize] = useState(10);
const handlePageSizeChange = (e) => {
    setSize(Number(e.target.value));
    setPage(0);
};
    const [totalPages, setTotalPages] = useState(0);

    const [filters, setFilters] = useState({
        search: "",
        module: "",
        action: "",
        fromDate: "",
        toDate: "",
        sortBy: "createdAt",
        direction: "DESC"
    });

    useEffect(() => {
        loadLogs();
    }, [page,size]);

    const loadLogs = async () => {

        try {

            setLoading(true);

const response = await adminAuditLogService.getAuditLogs({
    page,
    size,
    ...filters
});

console.log("Audit API Response:", response);

setLogs(response.content ?? []);
setTotalPages(response.totalPages ?? 0);

        } catch (error) {

            toast.error(
                error.message || "Failed to load audit logs."
            );

        } finally {

            setLoading(false);

        }

    };

    const handleSearch = () => {

        setPage(0);
        loadLogs();

    };

    const handleReset = () => {

        setFilters({
            search: "",
            module: "",
            action: "",
            fromDate: "",
            toDate: "",
            sortBy: "createdAt",
            direction: "DESC"
        });

        setPage(0);

        setTimeout(loadLogs, 100);

    };

    const openDetails = (log) => {

        setSelectedLog(log);
        setModalOpen(true);

    };
const auditColumns = [
  {
    label: "Date",
    value: (log) =>
      log.createdAt
        ? new Date(log.createdAt).toLocaleString()
        : "-",
  },
  {
    label: "Admin",
    value: (log) => log.adminName ?? "-",
  },
  {
    label: "Module",
    value: (log) => log.module ?? "-",
  },
  {
    label: "Action",
    value: (log) => log.action ?? "-",
  },
  {
    label: "Description",
    value: (log) => log.description ?? "-",
  },
];
const handleExportPDF = () => {
  exportToPDF({
    data: logs,
    columns: auditColumns,
    title: "Audit Log Report",
    fileName: "Audit_Logs_Report",
  });
};

const handleExportCSV = () => {
  exportToCSV({
    data: logs,
    columns: auditColumns,
    fileName: "Audit_Logs_Report",
  });
};

const handleExportExcel = () => {
  exportToExcel({
    data: logs,
    columns: auditColumns,
    fileName: "Audit_Logs_Report",
  });
};
    return (

        <div className="p-6">

            <div className="flex justify-between items-center mb-6">

                <div>

                    <h1 className="text-2xl font-bold">
                        Audit Logs
                    </h1>

                    <p className="text-gray-500 mt-1">
                        View all administrator activities.
                    </p>

                </div>

                <ExportDropdown
                    onPDF={handleExportPDF}
                    onExcel={handleExportExcel}
                    onCSV={handleExportCSV}
                />

            </div>

            <div className="bg-white rounded-xl shadow p-5 mb-6">

                <div className="grid grid-cols-1 md:grid-cols-6 gap-4">

                    <input
                        type="text"
                        placeholder="Search..."
                        value={filters.search}
                        onChange={(e) =>
                            setFilters({
                                ...filters,
                                search: e.target.value
                            })
                        }
                        className="border rounded-lg px-3 py-2"
                    />

                    <input
                        type="text"
                        placeholder="Module"
                        value={filters.module}
                        onChange={(e) =>
                            setFilters({
                                ...filters,
                                module: e.target.value
                            })
                        }
                        className="border rounded-lg px-3 py-2"
                    />

                    <input
                        type="text"
                        placeholder="Action"
                        value={filters.action}
                        onChange={(e) =>
                            setFilters({
                                ...filters,
                                action: e.target.value
                            })
                        }
                        className="border rounded-lg px-3 py-2"
                    />

                    <input
                        type="date"
                        value={filters.fromDate}
                        onChange={(e) =>
                            setFilters({
                                ...filters,
                                fromDate: e.target.value
                            })
                        }
                        className="border rounded-lg px-3 py-2"
                    />

                    <input
                        type="date"
                        value={filters.toDate}
                        onChange={(e) =>
                            setFilters({
                                ...filters,
                                toDate: e.target.value
                            })
                        }
                        className="border rounded-lg px-3 py-2"
                    />

                    <div className="flex gap-2">

                        <button
                            onClick={handleSearch}
                            className="bg-blue-600 text-white px-4 rounded-lg flex items-center gap-2"
                        >
                            <Search size={18} />
                            Search
                        </button>

                        <button
                            onClick={handleReset}
                            className="bg-gray-200 px-4 rounded-lg flex items-center gap-2"
                        >
                            <RefreshCw size={18} />
                            Reset
                        </button>

                    </div>

                </div>

            </div>
                        <div className="bg-white rounded-xl shadow overflow-hidden">

                            {loading ? (

                                <div className="p-8 text-center">
                                    Loading audit logs...
                                </div>

                            ) : logs.length === 0 ? (

                                <div className="p-8 text-center text-gray-500">
                                    No audit logs found.
                                </div>

                         ) : (

                         <>

                         <div className="flex justify-end mb-3">
                             <div className="flex items-center gap-2">
                                 <span className="text-sm font-medium">
                                     Rows per page
                                 </span>

                                 <select
                                     value={size}
                                     onChange={handlePageSizeChange}
                                     className="border rounded-lg px-3 py-2"
                                 >
                                     <option value={2}>2</option>
                                     <option value={10}>10</option>
                                     <option value={20}>20</option>
                                     <option value={50}>50</option>
                                 </select>
                             </div>
                         </div>

                         <table className="min-w-full">
                                    <thead className="bg-gray-100">

                                        <tr>

                                            <th className="px-4 py-3 text-left">Date</th>
                                            <th className="px-4 py-3 text-left">Admin</th>
                                            <th className="px-4 py-3 text-left">Module</th>
                                            <th className="px-4 py-3 text-left">Action</th>
                                            <th className="px-4 py-3 text-left">Description</th>
                                            <th className="px-4 py-3 text-center">Action</th>

                                        </tr>

                                    </thead>

                                    <tbody>

                                        {logs.map((log) => (

                                            <tr
                                                key={log.id}
                                                className="border-t hover:bg-gray-50"
                                            >

                                                <td className="px-4 py-3">
                                                    {new Date(log.createdAt).toLocaleString()}
                                                </td>

                                                <td className="px-4 py-3">
                                                    {log.adminName}
                                                </td>

                                                <td className="px-4 py-3">
                                                    {log.module}
                                                </td>

                                                <td className="px-4 py-3">

                                                    <span className="px-2 py-1 rounded bg-blue-100 text-blue-700 text-xs">
                                                        {log.action}
                                                    </span>

                                                </td>

                                                <td className="px-4 py-3 max-w-xs truncate">
                                                    {log.description}
                                                </td>

                                                <td className="px-4 py-3 text-center">

                                                    <button
                                                        onClick={() => openDetails(log)}
                                                        className="inline-flex items-center gap-2 bg-gray-100 hover:bg-gray-200 px-3 py-2 rounded-lg"
                                                    >
                                                        <Eye size={16} />
                                                        View
                                                    </button>

                                                </td>

                                            </tr>

                                        ))}

                                    </tbody>

                                </table>
</>
                            )}

                        </div>

                        {totalPages > 1 && (

                            <div className="flex justify-center items-center gap-3 mt-6">

                                <button
                                    disabled={page === 0}
                                    onClick={() => setPage((prev) => prev - 1)}
                                    className="px-4 py-2 border rounded disabled:opacity-50"
                                >
                                    Previous
                                </button>

                                <span>
                                    Page {page + 1} of {totalPages}
                                </span>

                                <button
                                    disabled={page + 1 >= totalPages}
                                    onClick={() => setPage((prev) => prev + 1)}
                                    className="px-4 py-2 border rounded disabled:opacity-50"
                                >
                                    Next
                                </button>

                            </div>

                        )}

                        <AuditLogDetailsModal
                            open={modalOpen}
                            onClose={() => {
                                setModalOpen(false);
                                setSelectedLog(null);
                            }}
                            auditLog={selectedLog}
                        />

                    </div>

                );
            };

            export default AuditLog;