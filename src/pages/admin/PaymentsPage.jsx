import { useState } from "react";
import AdminLayout from "@/components/AdminLayout";

const PaymentsPage = () => {
  const [tab, setTab] = useState("all");
  const [search, setSearch] = useState("");
  const [date, setDate] = useState("");

  const payments = [
    {
      id: 1,
      name: "Rahul",
      plan: "Gold",
      amount: 999,
      status: "Success",
      date: "2026-04-12",
    },
    {
      id: 2,
      name: "Sneha",
      plan: "Premium",
      amount: 1999,
      status: "Failed",
      date: "2026-04-10",
    },
    {
      id: 3,
      name: "Amit",
      plan: "Gold",
      amount: 999,
      status: "Success",
      date: "2026-04-08",
    },
    {
      id: 4,
      name: "Neha",
      plan: "Gold",
      amount: 999,
      status: "Refund",
      date: "2026-04-06",
    },
  ];

  // 🔍 FILTER LOGIC
  const filteredPayments = payments.filter((p) => {
    const matchTab =
      tab === "all" ||
      (tab === "success" && p.status === "Success") ||
      (tab === "failed" && p.status === "Failed") ||
      (tab === "refund" && p.status === "Refund");

    const matchSearch = p.name
      .toLowerCase()
      .includes(search.toLowerCase());

    const matchDate = date === "" || p.date === date;

    return matchTab && matchSearch && matchDate;
  });

  // 💰 CALCULATIONS
  const totalRevenue = payments
    .filter((p) => p.status === "Success")
    .reduce((sum, p) => sum + p.amount, 0);

  const totalTransactions = payments.length;

  const successCount = payments.filter(
    (p) => p.status === "Success"
  ).length;

  const failedCount = payments.filter(
    (p) => p.status === "Failed"
  ).length;

  const getStatusColor = (status) => {
    if (status === "Success") return "bg-green-100 text-green-600";
    if (status === "Failed") return "bg-red-100 text-red-600";
    if (status === "Refund") return "bg-yellow-100 text-yellow-600";
  };

  return (
    <AdminLayout>
      <div className="bg-gray-50 min-h-screen p-4 md:p-6">

        {/* HEADER */}
        <h1 className="text-2xl font-bold mb-4">
          Payment Management
        </h1>

        {/* 💰 REVENUE CARDS */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">

          <div className="bg-white p-4 rounded-xl shadow">
            <p className="text-sm text-gray-500">Total Revenue</p>
            <h2 className="text-xl font-bold text-green-600">
              ₹{totalRevenue}
            </h2>
          </div>

          <div className="bg-white p-4 rounded-xl shadow">
            <p className="text-sm text-gray-500">Transactions</p>
            <h2 className="text-xl font-bold">
              {totalTransactions}
            </h2>
          </div>

          <div className="bg-white p-4 rounded-xl shadow">
            <p className="text-sm text-gray-500">Success</p>
            <h2 className="text-xl font-bold text-green-600">
              {successCount}
            </h2>
          </div>

          <div className="bg-white p-4 rounded-xl shadow">
            <p className="text-sm text-gray-500">Failed</p>
            <h2 className="text-xl font-bold text-red-600">
              {failedCount}
            </h2>
          </div>

        </div>

        {/* 🔍 SEARCH + DATE */}
        <div className="flex flex-col md:flex-row gap-3 mb-6">

          <input
            type="text"
            placeholder="Search user..."
            className="border px-4 py-2 rounded-lg w-full md:w-1/3"
            onChange={(e) => setSearch(e.target.value)}
          />

          <input
            type="date"
            className="border px-3 py-2 rounded-lg"
            onChange={(e) => setDate(e.target.value)}
          />

        </div>

        {/* 🔥 TABS */}
        <div className="flex gap-3 mb-6 flex-wrap">
          {["all", "success", "failed", "refund"].map((t) => (
            <button
              key={t}
              onClick={() => setTab(t)}
              className={`px-4 py-2 rounded-lg text-sm ${
                tab === t
                  ? "bg-purple-500 text-white"
                  : "bg-white border"
              }`}
            >
              {t.toUpperCase()}
            </button>
          ))}
        </div>

        {/* 💳 TABLE */}
        <div className="bg-white rounded-xl shadow overflow-x-auto">

          <table className="w-full text-sm">

            <thead className="bg-gray-100 text-gray-600">
              <tr>
                <th className="p-3 text-left">User</th>
                <th>Plan</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Date</th>
              </tr>
            </thead>

            <tbody>
              {filteredPayments.map((p) => (
                <tr key={p.id} className="border-t hover:bg-gray-50">

                  <td className="p-3">{p.name}</td>
                  <td>{p.plan}</td>
                  <td>₹{p.amount}</td>

                  <td>
                    <span className={`px-2 py-1 rounded-full text-xs ${getStatusColor(p.status)}`}>
                      {p.status}
                    </span>
                  </td>

                  <td>{p.date}</td>

                </tr>
              ))}
            </tbody>

          </table>

        </div>

      </div>
    </AdminLayout>
  );
};

export default PaymentsPage;