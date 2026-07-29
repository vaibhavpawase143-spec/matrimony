// Subscriptions.jsx
import { useEffect, useState } from "react";
import { FaSearch } from "react-icons/fa";
import SubscriptionActionMenu from "../components/SubscriptionActionMenu";
import { useNavigate } from "react-router-dom";
import {
  getSubscriptions,
  getSubscriptionStats,
  activateSubscription,
  expireSubscription,
  cancelSubscription,
} from "../services/subscriptionService";
import { toast } from "sonner";
import ExportDropdown from "../components/common/ExportDropdown";
import { exportToCSV } from "../utils/export/csvExport";
import { exportToExcel } from "../utils/export/excelExport";
import { exportToPDF } from "../utils/export/pdfExport";
import ConfirmModal from "../components/common/ConfirmModal";
export default function Subscriptions() {
  const [search, setSearch] = useState("");
const [status, setStatus] = useState("");
  const [subscriptions, setSubscriptions] = useState([]);

  const [loading, setLoading] = useState(true);

  const [selectedSubscription, setSelectedSubscription] = useState(null);

  const [selectedAction, setSelectedAction] = useState("");

  const [isModalOpen, setIsModalOpen] = useState(false);

  // Pagination
  const [currentPage, setCurrentPage] = useState(0);

const [pageSize, setPageSize] = useState(10);

  const [totalPages, setTotalPages] = useState(0);

  const [totalElements, setTotalElements] = useState(0);
const navigate = useNavigate();
const openConfirmModal = (subscription, action) => {
  setSelectedSubscription(subscription);
  setSelectedAction(action);
  setIsModalOpen(true);
};

 const loadSubscriptions = async (
   page = currentPage,
   searchText = search
 ) => {
   try {
     setLoading(true);

     const data = await getSubscriptions(
       page,
       pageSize,
       searchText,
       status
     );

     setSubscriptions(data?.content || []);
     setCurrentPage(data?.number || 0);
     setTotalPages(data?.totalPages || 0);
     setTotalElements(data?.totalElements || 0);

   } catch (e) {
     console.error(e);
     setSubscriptions([]);
     setTotalPages(0);
     setTotalElements(0);
   } finally {
     setLoading(false);
   }
 };
useEffect(() => {
  loadSubscriptions(currentPage, search);
}, [currentPage, search, status, pageSize]);


const handleConfirmAction = async () => {
  if (!selectedSubscription) return;

  try {
    switch (selectedAction) {
      case "activate":
        await activateSubscription(selectedSubscription.id);
        toast.success("Subscription activated successfully.");
        break;

      case "expire":
        await expireSubscription(selectedSubscription.id);
        toast.success("Subscription expired successfully.");
        break;

      case "cancel": {
        const reason = window.prompt(
          "Enter cancellation reason",
          "Cancelled by Admin"
        );

        if (!reason) {
          setIsModalOpen(false);
          return;
        }

        await cancelSubscription(selectedSubscription.id, reason);
        toast.success("Subscription cancelled successfully.");
        break;
      }

      default:
        return;
    }

    await loadSubscriptions();

    setIsModalOpen(false);
    setSelectedSubscription(null);
    setSelectedAction("");

  } catch (err) {
    console.error(err);

    toast.error(
      err?.response?.data?.message ||
      err?.message ||
      "Action failed."
    );
  }
};

const subscriptionColumns = [
  {
    label: "ID",
    value: (sub) => sub.id,
  },
  {
    label: "User",
    value: (sub) => sub.userName,
  },
  {
    label: "Plan",
    value: (sub) => sub.planName,
  },
  {
    label: "Start Date",
    value: (sub) =>
      sub.startDate
        ? new Date(sub.startDate).toLocaleDateString()
        : "-",
  },
  {
    label: "End Date",
    value: (sub) =>
      sub.endDate
        ? new Date(sub.endDate).toLocaleDateString()
        : "-",
  },
  {
    label: "Status",
    value: (sub) => sub.status,
  },
];
const handleExportCSV = () => {
  exportToCSV({
    data: subscriptions,
    columns: subscriptionColumns,
    fileName: "Subscriptions_Report",
  });
};

const handleExportExcel = () => {
  exportToExcel({
    data: subscriptions,
    columns: subscriptionColumns,
    fileName: "Subscriptions_Report",
  });
};

const handleExportPDF = () => {
  exportToPDF({
    data: subscriptions,
    columns: subscriptionColumns,
    title: "Subscription Management Report",
    fileName: "Subscriptions_Report",
  });
};
  return (
    <div className="p-6">

      <div className="flex justify-between items-center mb-6">

        <div>
          <h1 className="text-3xl font-bold">
            Subscriptions
          </h1>

          <p className="text-gray-500">
            Manage all user subscriptions.
          </p>
        </div>

        <ExportDropdown
          onPDF={handleExportPDF}
          onExcel={handleExportExcel}
          onCSV={handleExportCSV}
        />

      </div>



     <div className="bg-white rounded-xl shadow-md p-4 mb-6 border border-gray-200">

       {/* Search */}
       <div className="flex items-center gap-3">
         <FaSearch className="text-gray-400" />

         <input
           type="text"
           placeholder="Search User..."
           className="w-full outline-none"
           value={search}
           onChange={(e) => {
             setSearch(e.target.value);
             setCurrentPage(0);
           }}
         />
       </div>

       {/* Status Filter */}
<div className="mt-4 flex justify-between items-center">
         <select
           value={status}
           onChange={(e) => {
             setStatus(e.target.value);
             setCurrentPage(0);
           }}
           className="border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-violet-500"
         >
           <option value="">All Status</option>
           <option value="ACTIVE">ACTIVE</option>
           <option value="EXPIRED">EXPIRED</option>
           <option value="CANCELLED">CANCELLED</option>
           <option value="REFUNDED">REFUNDED</option>
         </select>
       </div>

     </div>

     <div className="bg-white rounded-2xl shadow-md overflow-visible border border-purple-100">
        <table className="w-full">
          <thead className="bg-gradient-to-r from-violet-700 to-purple-600 text-white">
            <tr>
              <th className="px-6 py-4 text-left">User</th>
              <th className="px-6 py-4 text-left">Plan</th>
              <th className="px-6 py-4 text-left">Start</th>
              <th className="px-6 py-4 text-left">End</th>
              <th className="px-6 py-4 text-left">Status</th>
              <th className="px-6 py-4 text-center">Actions</th>
            </tr>
          </thead>

          <tbody className="divide-y divide-gray-200">
{subscriptions.length ? subscriptions.map((sub) => (
              <tr key={sub.id} className="hover:bg-purple-50">
                <td className="px-6 py-4">{sub.userName}</td>
                <td className="px-6 py-4">{sub.planName}</td>
                <td className="px-6 py-4">{sub.startDate ? new Date(sub.startDate).toLocaleDateString() : "-"}</td>
                <td className="px-6 py-4">{sub.endDate ? new Date(sub.endDate).toLocaleDateString() : "-"}</td>
                <td className="px-6 py-4">{sub.status}</td>
                <td className="px-6 py-4">
                 <div className="flex justify-center">
                   <SubscriptionActionMenu
                     subscription={sub}
                     onView={(subscription) =>
                       navigate(`/subscriptions/${subscription.id}`)
                     }
                    onActivate={(subscription) =>
                      openConfirmModal(subscription, "activate")
                    }
                    onExpire={(subscription) =>
                      openConfirmModal(subscription, "expire")
                    }
                    onCancel={(subscription) =>
                      openConfirmModal(subscription, "cancel")
                    }                   />
                 </div>                </td>
              </tr>
            )) : (
              <tr><td colSpan="6" className="text-center py-8 text-gray-500">No subscriptions found.</td></tr>
            )}
          </tbody>
        </table>
     </div>
<div className="flex items-center gap-2">
  <label className="text-sm font-medium text-gray-600">
    Show
  </label>

  <select
    value={pageSize}
    onChange={(e) => {
      setPageSize(Number(e.target.value));
      setCurrentPage(0);
    }}
    className="border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-violet-500"
  >
    <option value={10}>10</option>
    <option value={20}>20</option>
    <option value={50}>50</option>
    <option value={100}>100</option>
  </select>
</div>
     {/* Pagination */}
     <div className="flex flex-col md:flex-row justify-between items-center mt-6 gap-4">

       <div className="text-sm text-gray-600">
         Showing{" "}
         {totalElements === 0
           ? 0
           : currentPage * pageSize + 1}
         {" - "}
         {Math.min(
           (currentPage + 1) * pageSize,
           totalElements
         )}{" "}
         of {totalElements} subscriptions
       </div>

       <div className="flex items-center gap-2">

         <button
           onClick={() => setCurrentPage((prev) => prev - 1)}
           disabled={currentPage === 0}
           className={`px-4 py-2 rounded-lg border ${
             currentPage === 0
               ? "bg-gray-100 text-gray-400 cursor-not-allowed"
               : "bg-white hover:bg-violet-100"
           }`}
         >
           Previous
         </button>

         {Array.from({ length: totalPages }, (_, index) => (
           <button
             key={index}
             onClick={() => setCurrentPage(index)}
             className={`w-10 h-10 rounded-lg ${
               currentPage === index
                 ? "bg-violet-600 text-white"
                 : "bg-white border hover:bg-violet-100"
             }`}
           >
             {index + 1}
           </button>
         ))}

         <button
           onClick={() => setCurrentPage((prev) => prev + 1)}
           disabled={currentPage === totalPages - 1 || totalPages === 0}
           className={`px-4 py-2 rounded-lg border ${
             currentPage === totalPages - 1 || totalPages === 0
               ? "bg-gray-100 text-gray-400 cursor-not-allowed"
               : "bg-white hover:bg-violet-100"
           }`}
         >
           Next
         </button>

       </div>

     </div>

     <ConfirmModal
         isOpen={isModalOpen}
         title="Confirm Action"
         message={
           selectedSubscription
             ? `Are you sure you want to ${selectedAction} this subscription of "${selectedSubscription.userName}"?`
             : ""
         }
         confirmText="Yes"
         cancelText="Cancel"
         confirmButtonClass={
           selectedAction === "cancel"
             ? "bg-red-600 hover:bg-red-700"
             : selectedAction === "expire"
             ? "bg-yellow-600 hover:bg-yellow-700"
             : "bg-violet-600 hover:bg-violet-700"
         }
         onConfirm={handleConfirmAction}
         onCancel={() => {
           setIsModalOpen(false);
           setSelectedSubscription(null);
           setSelectedAction("");
         }}
       />
     </div>
   );
 }
