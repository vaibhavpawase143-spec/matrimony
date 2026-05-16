import { useState, useEffect } from "react";
import AdminLayout from "@/components/AdminLayout";
import { CreditCard, TrendingUp, Search, Filter, CheckCircle, XCircle, AlertCircle } from "lucide-react";

const AdminPayments = () => {
  const [payments, setPayments] = useState([]);
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [filter, setFilter] = useState("all");

  useEffect(() => {
    fetchPayments();
    fetchStats();
  }, []);

  const fetchPayments = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");
      const response = await fetch("/api/admin/payments", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = await response.json();
      if (data.success) {
        setPayments(data.data.content || []);
      }
    } catch (error) {
      console.error("Error fetching payments:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchStats = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch("/api/admin/operations/stats/payments", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = await response.json();
      if (data.success) {
        setStats(data.data);
      }
    } catch (error) {
      console.error("Error fetching payment stats:", error);
    }
  };

  const handleVerifyPayment = async (paymentId) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`/api/admin/payments/${paymentId}/verify`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
        fetchPayments();
        fetchStats();
      }
    } catch (error) {
      console.error("Error verifying payment:", error);
    }
  };

  const handleRefundPayment = async (paymentId) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`/api/admin/payments/${paymentId}/refund`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
        fetchPayments();
        fetchStats();
      }
    } catch (error) {
      console.error("Error refunding payment:", error);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "SUCCESS":
        return "bg-green-100 text-green-800";
      case "FAILED":
        return "bg-red-100 text-red-800";
      case "PENDING":
        return "bg-yellow-100 text-yellow-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case "SUCCESS":
        return <CheckCircle size={16} className="text-green-500" />;
      case "FAILED":
        return <XCircle size={16} className="text-red-500" />;
      case "PENDING":
        return <AlertCircle size={16} className="text-yellow-500" />;
      default:
        return null;
    }
  };

  const filteredPayments = payments.filter(payment =>
    payment.transactionId?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    payment.userEmail?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return (
      <AdminLayout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600"></div>
        </div>
      </AdminLayout>
    );
  }

  return (
    <AdminLayout>
      <div className="bg-gray-50 min-h-screen p-6">
        {/* Header */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <CreditCard size={28} />
            Payment Management
          </h1>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
          <div className="bg-gradient-to-r from-green-500 to-emerald-500 text-white p-5 rounded-xl shadow-lg">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm">Total Revenue</p>
                <h2 className="text-2xl font-bold">₹{stats.totalRevenue || 0}</h2>
              </div>
              <TrendingUp size={24} />
            </div>
          </div>
          <div className="bg-gradient-to-r from-blue-500 to-cyan-500 text-white p-5 rounded-xl shadow-lg">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm">Successful</p>
                <h2 className="text-2xl font-bold">{stats.successfulTransactions || 0}</h2>
              </div>
              <CheckCircle size={24} />
            </div>
          </div>
          <div className="bg-gradient-to-r from-red-500 to-pink-500 text-white p-5 rounded-xl shadow-lg">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm">Failed</p>
                <h2 className="text-2xl font-bold">{stats.failedTransactions || 0}</h2>
              </div>
              <XCircle size={24} />
            </div>
          </div>
          <div className="bg-gradient-to-r from-yellow-500 to-orange-500 text-white p-5 rounded-xl shadow-lg">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm">Pending</p>
                <h2 className="text-2xl font-bold">{stats.pendingTransactions || 0}</h2>
              </div>
              <AlertCircle size={24} />
            </div>
          </div>
        </div>

        {/* Search and Filter */}
        <div className="bg-white p-4 rounded-xl shadow mb-6">
          <div className="flex gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-3 text-gray-400" size={20} />
              <input
                type="text"
                placeholder="Search by transaction ID or email..."
                className="pl-10 pr-4 py-2 border rounded-lg w-full"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
            <select
              className="px-4 py-2 border rounded-lg"
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
            >
              <option value="all">All Payments</option>
              <option value="SUCCESS">Successful</option>
              <option value="FAILED">Failed</option>
              <option value="PENDING">Pending</option>
            </select>
          </div>
        </div>

        {/* Payments Table */}
        <div className="bg-white rounded-xl shadow overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50 border-b">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Transaction ID
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    User
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Amount
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Method
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Date
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredPayments.map((payment) => (
                  <tr key={payment.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {payment.transactionId}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">{payment.userEmail}</div>
                      <div className="text-sm text-gray-500">ID: {payment.userId}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        ₹{payment.amount}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">{payment.method}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center gap-2">
                        {getStatusIcon(payment.status)}
                        <span
                          className={`px-2 py-1 text-xs rounded-full ${getStatusColor(payment.status)}`}
                        >
                          {payment.status}
                        </span>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">
                        {new Date(payment.createdAt).toLocaleDateString()}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex gap-2">
                        {payment.status === "PENDING" && (
                          <button
                            onClick={() => handleVerifyPayment(payment.id)}
                            className="text-green-600 hover:text-green-900"
                            title="Verify Payment"
                          >
                            <CheckCircle size={16} />
                          </button>
                        )}
                        {payment.status === "SUCCESS" && (
                          <button
                            onClick={() => handleRefundPayment(payment.id)}
                            className="text-red-600 hover:text-red-900"
                            title="Refund Payment"
                          >
                            <XCircle size={16} />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </AdminLayout>
  );
};

export default AdminPayments;
