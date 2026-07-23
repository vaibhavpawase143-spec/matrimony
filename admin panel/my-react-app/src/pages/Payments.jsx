import { useEffect, useState } from "react";
import { FaSearch, FaEye, FaCheck, FaTimes } from "react-icons/fa";

import {
  getPayments,
  getPaymentById,
  markPaymentSuccess,
  markPaymentFailed,
} from "../services/paymentService";
import ExportDropdown from "../components/common/ExportDropdown";
import { exportToPDF } from "../utils/export/pdfExport";
import { exportToCSV } from "../utils/export/csvExport";
import { exportToExcel } from "../utils/export/excelExport";
export default function Payments() {
  const [search, setSearch] = useState("");
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedPayment, setSelectedPayment] = useState(null);

  const loadPayments = async () => {
    try {
      setLoading(true);

      const data = await getPayments();

      setPayments(data?.content || []);
    } catch (error) {
      console.error("Failed to load payments:", error);
      setPayments([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPayments();
  }, []);

  const filteredPayments = payments.filter((payment) => {
    const keyword = search.toLowerCase();

    return (
      payment.userName?.toLowerCase().includes(keyword) ||
      payment.transactionId?.toLowerCase().includes(keyword)
    );
  });

  const approvePayment = async (id) => {
    try {
      await markPaymentSuccess(id);
      await loadPayments();
    } catch (error) {
      console.error(error);
    }
  };

  const rejectPayment = async (id) => {
    try {
      await markPaymentFailed(id, "Rejected by Admin");
      await loadPayments();
    } catch (error) {
      console.error(error);
    }
  };

  const viewPayment = async (id) => {
    try {
      const payment = await getPaymentById(id);
      setSelectedPayment(payment);
    } catch (error) {
      console.error(error);
    }
  };

  const closePaymentDetails = () => {
    setSelectedPayment(null);
  };
const paymentColumns = [
  {
    label: "Transaction ID",
    value: (payment) => payment.transactionId ?? "-",
  },
  {
    label: "User",
    value: (payment) => payment.userName ?? "-",
  },
  {
    label: "Plan",
    value: (payment) => payment.planName ?? "-",
  },
  {
    label: "Amount",
    value: (payment) => `₹${payment.amount ?? 0}`,
  },
  {
    label: "Method",
    value: (payment) => payment.paymentMethod ?? "-",
  },
  {
    label: "Status",
    value: (payment) => payment.status ?? "-",
  },
  {
    label: "Date",
    value: (payment) =>
      payment.createdAt
        ? new Date(payment.createdAt).toLocaleDateString()
        : "-",
  },
];
const handleExportPDF = () => {
  exportToPDF({
    data: filteredPayments,
    columns: paymentColumns,
    title: "Payment Report",
    fileName: "Payments_Report",
  });
};

const handleExportCSV = () => {
  exportToCSV({
    data: filteredPayments,
    columns: paymentColumns,
    fileName: "Payments_Report",
  });
};

const handleExportExcel = () => {
  exportToExcel({
    data: filteredPayments,
    columns: paymentColumns,
    fileName: "Payments_Report",
  });
};
  if (loading) {
    return (
      <div className="p-6 text-center text-gray-500">
        Loading payments...
      </div>
    );
  }

  return (
    <div className="p-6">

 <div className="flex justify-between items-center mb-6">

     <div>
         <h1 className="text-3xl font-bold">
             Payments
         </h1>

         <p className="text-gray-500">
             Manage all payment transactions.
         </p>
     </div>

     <ExportDropdown
         onPDF={handleExportPDF}
         onCSV={handleExportCSV}
         onExcel={handleExportExcel}
     />

 </div>

      <div className="bg-white shadow rounded-xl p-4 mb-5 flex items-center gap-3">
        <FaSearch className="text-gray-500" />

        <input
          type="text"
          placeholder="Search Transaction..."
          className="w-full outline-none"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="bg-white rounded-xl shadow overflow-hidden border border-gray-200">
        <div className="overflow-x-auto">

          <table className="min-w-full divide-y divide-gray-200">

            <thead className="bg-purple-600 text-white">
              <tr>
                <th className="px-6 py-4 text-left">Transaction ID</th>
                <th className="px-6 py-4 text-left">User</th>
                <th className="px-6 py-4 text-left">Plan</th>
                <th className="px-6 py-4 text-left">Amount</th>
                <th className="px-6 py-4 text-left">Method</th>
                <th className="px-6 py-4 text-left">Status</th>
                <th className="px-6 py-4 text-left">Date</th>
                <th className="px-6 py-4 text-center">Action</th>
              </tr>
            </thead>



          <tbody className="bg-white divide-y divide-gray-100">
            {filteredPayments.length > 0 ? (
              filteredPayments.map((payment) => (
                <tr
                  key={payment.id}
                  className="hover:bg-purple-50 transition-colors"
                >
                  <td className="px-6 py-4 whitespace-nowrap text-sm">
                    {payment.transactionId}
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-sm">
                    {payment.userName}
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-sm">
                    {payment.planName}
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-sm">
                    ₹{payment.amount}
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-sm">
                    {payment.paymentMethod}
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap">
                    <span
                      className={`inline-flex items-center rounded-full px-3 py-1 text-sm font-medium text-white ${
                        payment.status === "SUCCESS"
                          ? "bg-green-500"
                          : payment.status === "PENDING"
                          ? "bg-yellow-500"
                          : "bg-red-500"
                      }`}
                    >
                      {payment.status}
                    </span>
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-sm">
                    {payment.createdAt
                      ? new Date(payment.createdAt).toLocaleDateString()
                      : "-"}
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex justify-center items-center gap-2">

                      <button
                        onClick={() => viewPayment(payment.id)}
                        className="text-blue-600 p-2 rounded-full hover:bg-purple-100"
                      >
                        <FaEye />
                      </button>

                      <button
                        onClick={() => approvePayment(payment.id)}
                        className="text-green-600 p-2 rounded-full hover:bg-green-100"
                      >
                        <FaCheck />
                      </button>

                      <button
                        onClick={() => rejectPayment(payment.id)}
                        className="text-red-600 p-2 rounded-full hover:bg-red-100"
                      >
                        <FaTimes />
                      </button>

                    </div>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td
                  colSpan="8"
                  className="text-center py-10 text-gray-500"
                >
                  No payment records found.
                </td>
              </tr>
            )}
          </tbody>

                    </table>
                  </div>
                </div>

                {selectedPayment && (
                  <div className="bg-white rounded-xl shadow p-6 mt-6 border border-purple-100">

                    <div className="flex justify-between items-center mb-5">
                      <div>
                        <h2 className="text-2xl font-bold">
                          Payment Details
                        </h2>

                        <p className="text-gray-500">
                          Transaction ID :
                          {" "}
                          {selectedPayment.transactionId}
                        </p>
                      </div>

                      <button
                        onClick={closePaymentDetails}
                        className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700"
                      >
                        Close
                      </button>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">

                      <div className="border rounded-lg p-4">
                        <p className="text-gray-500 text-sm">User</p>
                        <p className="font-semibold mt-2">
                          {selectedPayment.userName}
                        </p>
                      </div>

                      <div className="border rounded-lg p-4">
                        <p className="text-gray-500 text-sm">Email</p>
                        <p className="font-semibold mt-2">
                          {selectedPayment.userEmail}
                        </p>
                      </div>

                      <div className="border rounded-lg p-4">
                        <p className="text-gray-500 text-sm">Plan</p>
                        <p className="font-semibold mt-2">
                          {selectedPayment.planName}
                        </p>
                      </div>

                      <div className="border rounded-lg p-4">
                        <p className="text-gray-500 text-sm">Amount</p>
                        <p className="font-semibold mt-2">
                          ₹{selectedPayment.amount}
                        </p>
                      </div>

                      <div className="border rounded-lg p-4">
                        <p className="text-gray-500 text-sm">
                          Payment Method
                        </p>

                        <p className="font-semibold mt-2">
                          {selectedPayment.paymentMethod}
                        </p>
                      </div>

                      <div className="border rounded-lg p-4">
                        <p className="text-gray-500 text-sm">Status</p>
                        <p className="font-semibold mt-2">
                          {selectedPayment.status}
                        </p>
                      </div>

                      <div className="border rounded-lg p-4">
                        <p className="text-gray-500 text-sm">
                          Created At
                        </p>

                        <p className="font-semibold mt-2">
                          {selectedPayment.createdAt
                            ? new Date(
                                selectedPayment.createdAt
                              ).toLocaleString()
                            : "-"}
                        </p>
                      </div>

                    </div>

                  </div>
                )}

              </div>
            );
          }