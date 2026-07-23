import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  FaUsers,
  FaUserCheck,
  FaUserTimes,
  FaCrown,
  FaMoneyBillWave,
  FaClipboardList,
  FaExclamationTriangle,
  FaExchangeAlt,
  FaSyncAlt,
  FaArrowUp,
  FaChartLine,
  FaMapMarkerAlt,
  FaPrayingHands,
  FaCreditCard,
  FaShieldAlt,
} from "react-icons/fa";

import Loader from "../components/Loader";
import StatsCard from "../components/StatsCard";
import MiniChart from "../components/MiniChart";
import SubscriptionChart from "../components/SubscriptionChart";

import { getDashboardStats } from "../services/dashboardService";

export default function Dashboard() {
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [dashboard, setDashboard] = useState({});
  const [error, setError] = useState("");

  // ===============================
  // LOAD DASHBOARD
  // ===============================

  const loadDashboard = async () => {
    try {
      setError("");

      const response = await getDashboardStats();

      setDashboard(response || {});
    } catch (err) {
      console.error(err);
      setDashboard({});
      setError("Unable to load dashboard.");
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    loadDashboard();
  }, []);

  const refreshDashboard = () => {
    if (refreshing) return;

    setRefreshing(true);
    loadDashboard();
  };

  // ===============================
  // MONTH LABELS
  // ===============================

  const months = [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec",
  ];

  // ===============================
  // CHART DATA
  // ===============================

  const userTrend = useMemo(
    () => Object.values(dashboard.userRegistrationTrend || {}),
    [dashboard.userRegistrationTrend]
  );

  const revenueTrend = useMemo(
    () => Object.values(dashboard.revenueTrend || {}),
    [dashboard.revenueTrend]
  );

  const reportTrend = useMemo(
    () => Object.values(dashboard.reportsTrend || {}),
    [dashboard.reportsTrend]
  );

  const paymentDistribution = useMemo(
    () =>
      Object.entries(dashboard.paymentMethodDistribution || {}).map(
        ([name, value]) => ({
          name,
          value,
        })
      ),
    [dashboard.paymentMethodDistribution]
  );

  const reportDistribution = useMemo(
    () =>
      Object.entries(dashboard.reportTypeDistribution || {}).map(
        ([name, value]) => ({
          name,
          value,
        })
      ),
    [dashboard.reportTypeDistribution]
  );

  if (loading) {
    return <Loader />;
  }

  return (
    <div className="space-y-6">

      {/* ========================================= */}
      {/* HEADER */}
      {/* ========================================= */}

      <div className="bg-white rounded-2xl shadow border border-gray-100 p-6">

        <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-5">

          <div>

            <h1 className="text-3xl font-bold text-gray-800">
              Admin Dashboard
            </h1>

            <p className="text-gray-500 mt-2">
              Welcome to the Gathbandhan Matrimony Admin Panel.
            </p>

            <p className="text-sm text-gray-400 mt-1">
              Manage users, subscriptions, payments, reports and analytics
              from one centralized dashboard.
            </p>

          </div>

          <button
            onClick={refreshDashboard}
            disabled={refreshing}
            className="flex items-center justify-center gap-2 bg-violet-600 hover:bg-violet-700 text-white px-5 py-3 rounded-xl transition disabled:opacity-60"
          >
            <FaSyncAlt
              className={refreshing ? "animate-spin" : ""}
            />

            {refreshing
              ? "Refreshing..."
              : "Refresh Dashboard"}
          </button>

        </div>

        {error && (

          <div className="mt-5 rounded-xl border border-red-200 bg-red-50 text-red-600 px-4 py-3">

            {error}

          </div>

        )}

      </div>

      {/* ========================================= */}
      {/* OVERVIEW CARDS */}
      {/* ========================================= */}

      <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-5">

        <StatsCard
          title="Total Users"
          value={dashboard.totalUsers ?? 0}
          icon={<FaUsers />}
          type="users"
        />

        <StatsCard
          title="Active Users"
          value={dashboard.activeUsers ?? 0}
          icon={<FaUserCheck />}
          type="active"
        />

        <StatsCard
          title="Inactive Users"
          value={dashboard.inactiveUsers ?? 0}
          icon={<FaUserTimes />}
          type="inactive"
        />

        <StatsCard
          title="Active Subscriptions"
          value={dashboard.activeSubscriptions ?? 0}
          icon={<FaCrown />}
          type="premium"
        />

        <StatsCard
          title="Total Revenue"
          value={`₹${dashboard.totalRevenue ?? 0}`}
          icon={<FaMoneyBillWave />}
          type="revenue"
        />

        <StatsCard
          title="Transactions"
          value={dashboard.totalTransactions ?? 0}
          icon={<FaExchangeAlt />}
          type="transaction"
        />

        <StatsCard
          title="Total Reports"
          value={dashboard.totalReports ?? 0}
          icon={<FaClipboardList />}
          type="reports"
        />

        <StatsCard
          title="Pending Reports"
          value={dashboard.pendingReports ?? 0}
          icon={<FaExclamationTriangle />}
          type="pending"
        />

      </div>

      {/* ========================================= */}
      {/* GROWTH CARDS */}
      {/* ========================================= */}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
        <StatsCard
          title="User Growth"
          value={`${(dashboard.userGrowthPercentage ?? 0).toFixed(1)}%`}
          icon={<FaArrowUp />}
          type="growth"
        />

        <StatsCard
          title="Revenue Growth"
          value={`${(dashboard.revenueGrowthPercentage ?? 0).toFixed(1)}%`}
          icon={<FaChartLine />}
          type="growth"
        />

        <StatsCard
          title="Subscription Growth"
          value={`${(dashboard.subscriptionGrowthPercentage ?? 0).toFixed(1)}%`}
          icon={<FaCrown />}
          type="growth"
        />

      </div>

      {/* ========================================= */}
      {/* USER & REVENUE CHARTS */}
      {/* ========================================= */}

      <div className="grid grid-cols-1 xl:grid-cols-2 gap-6">

        {/* User Registration Trend */}

        <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">

          <div className="flex items-center justify-between mb-6">

            <div>

              <h2 className="text-xl font-semibold text-gray-800">
                User Registration Trend
              </h2>

              <p className="text-sm text-gray-500 mt-1">
                Monthly user registrations.
              </p>

            </div>

            <div className="w-11 h-11 rounded-xl bg-violet-100 flex items-center justify-center">

              <FaUsers className="text-violet-700 text-lg" />

            </div>

          </div>

          <MiniChart
            data={userTrend}
            labels={months}
            color="#7C3AED"
            width={700}
            height={260}
          />

        </div>

        {/* Revenue Trend */}

        <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">

          <div className="flex items-center justify-between mb-6">

            <div>

              <h2 className="text-xl font-semibold text-gray-800">
                Revenue Trend
              </h2>

              <p className="text-sm text-gray-500 mt-1">
                Monthly revenue generated from subscriptions.
              </p>

            </div>

            <div className="w-11 h-11 rounded-xl bg-green-100 flex items-center justify-center">

              <FaMoneyBillWave className="text-green-600 text-lg" />

            </div>

          </div>

          <MiniChart
            data={revenueTrend}
            labels={months}
            color="#10B981"
            width={700}
            height={260}
          />

        </div>

      </div>

      {/* ========================================= */}
      {/* REPORTS & PAYMENT DISTRIBUTION */}
      {/* ========================================= */}

      <div className="grid grid-cols-1 xl:grid-cols-2 gap-6">
                  {/* ========================================= */}
                  {/* REPORTS TREND */}
                  {/* ========================================= */}

                  <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">

                    <div className="flex items-center justify-between mb-6">

                      <div>

                        <h2 className="text-xl font-semibold text-gray-800">
                          Reports Trend
                        </h2>

                        <p className="text-sm text-gray-500 mt-1">
                          Monthly reported profiles.
                        </p>

                      </div>

                      <div className="w-11 h-11 rounded-xl bg-red-100 flex items-center justify-center">

                        <FaShieldAlt className="text-red-600 text-lg" />

                      </div>

                    </div>

                    <MiniChart
                      data={reportTrend}
                      labels={months}
                      color="#EF4444"
                      width={700}
                      height={260}
                    />

                  </div>

                  {/* ========================================= */}
                  {/* PAYMENT METHOD DISTRIBUTION */}
                  {/* ========================================= */}

                  <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">

                    <div className="flex items-center justify-between mb-6">

                      <div>

                        <h2 className="text-xl font-semibold text-gray-800">
                          Payment Method Distribution
                        </h2>

                        <p className="text-sm text-gray-500 mt-1">
                          Successful transactions by payment method.
                        </p>

                      </div>

                      <div className="w-11 h-11 rounded-xl bg-blue-100 flex items-center justify-center">

                        <FaCreditCard className="text-blue-600 text-lg" />

                      </div>

                    </div>

                    {paymentDistribution.length === 0 ? (

                      <div className="flex items-center justify-center h-56 text-gray-400">
                        No payment data available.
                      </div>

                    ) : (

                      <div className="space-y-4">

                        {paymentDistribution.map((item) => (

                          <div
                            key={item.name}
                            className="flex items-center justify-between border rounded-xl px-4 py-3"
                          >

                            <div className="font-medium text-gray-700">
                              {item.name}
                            </div>

                            <span className="font-bold text-violet-700">
                              {item.value}
                            </span>

                          </div>

                        ))}

                      </div>

                    )}

                  </div>

                </div>

                {/* ========================================= */}
                {/* REPORT STATUS + TOP PAYMENT PLANS */}
                {/* ========================================= */}

                <div className="grid grid-cols-1 xl:grid-cols-2 gap-6">

                  {/* Report Status Distribution */}

                  <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">

                    <h2 className="text-xl font-semibold text-gray-800 mb-6">
                      Report Status Distribution
                    </h2>

                    {reportDistribution.length === 0 ? (

                      <div className="flex items-center justify-center h-56 text-gray-400">
                        No report data available.
                      </div>

                    ) : (

                      <div className="space-y-4">

                        {reportDistribution.map((item) => (

                          <div
                            key={item.name}
                            className="flex items-center justify-between border rounded-xl px-4 py-3"
                          >

                            <span className="font-medium text-gray-700">
                              {item.name}
                            </span>

                            <span className="font-bold text-red-600">
                              {item.value}
                            </span>

                          </div>

                        ))}

                      </div>

                    )}

                  </div>

                  {/* Top Subscription Plans */}

                  <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">

                    <div className="flex items-center justify-between mb-6">

                      <div>

                        <h2 className="text-xl font-semibold text-gray-800">
                          Top Subscription Plans
                        </h2>

                        <p className="text-sm text-gray-500 mt-1">
                          Most purchased premium plans.
                        </p>

                      </div>

                      <FaCrown className="text-yellow-500 text-2xl" />

                    </div>

                    <SubscriptionChart
                      plans={dashboard.topPaymentPlans || []}
                    />

                  </div>

                </div>

                {/* ========================================= */}
                {/* TOP CITIES & TOP RELIGIONS */}
                {/* ========================================= */}

                <div className="grid grid-cols-1 xl:grid-cols-2 gap-6">
                       {/* ========================================= */}
                       {/* TOP CITIES */}
                       {/* ========================================= */}

                       <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">

                         <div className="flex items-center justify-between mb-6">

                           <div>

                             <h2 className="text-xl font-semibold text-gray-800">
                               Top Cities
                             </h2>

                             <p className="text-sm text-gray-500 mt-1">
                               Cities with the highest number of registered users.
                             </p>

                           </div>

                           <div className="w-11 h-11 rounded-xl bg-violet-100 flex items-center justify-center">
                             <FaMapMarkerAlt className="text-violet-700 text-lg" />
                           </div>

                         </div>

                         {(dashboard.topCities || []).length === 0 ? (

                           <div className="flex items-center justify-center h-56 text-gray-400">
                             No city data available.
                           </div>

                         ) : (

                           <div className="space-y-4">

                             {(dashboard.topCities || []).map((city, index) => (

                               <div
                                 key={city.cityId}
                                 className="flex items-center justify-between border rounded-xl px-4 py-3 hover:bg-violet-50 transition"
                               >

                                 <div className="flex items-center gap-4">

                                   <div className="w-10 h-10 rounded-full bg-violet-600 text-white flex items-center justify-center font-bold">
                                     #{index + 1}
                                   </div>

                                   <div>

                                     <h4 className="font-semibold text-gray-800">
                                       {city.cityName}
                                     </h4>

                                     <p className="text-xs text-gray-500">
                                       Registered Members
                                     </p>

                                   </div>

                                 </div>

                                 <span className="text-lg font-bold text-violet-700">
                                   {city.userCount}
                                 </span>

                               </div>

                             ))}

                           </div>

                         )}

                       </div>

                       {/* ========================================= */}
                       {/* TOP RELIGIONS */}
                       {/* ========================================= */}

                       <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">

                         <div className="flex items-center justify-between mb-6">

                           <div>

                             <h2 className="text-xl font-semibold text-gray-800">
                               Top Religions
                             </h2>

                             <p className="text-sm text-gray-500 mt-1">
                               Religion-wise registered users.
                             </p>

                           </div>

                           <div className="w-11 h-11 rounded-xl bg-orange-100 flex items-center justify-center">
                             <FaPrayingHands className="text-orange-600 text-lg" />
                           </div>

                         </div>

                         {(dashboard.topReligions || []).length === 0 ? (

                           <div className="flex items-center justify-center h-56 text-gray-400">
                             No religion data available.
                           </div>

                         ) : (

                           <div className="space-y-4">

                             {(dashboard.topReligions || []).map((religion, index) => (

                               <div
                                 key={religion.religionId}
                                 className="flex items-center justify-between border rounded-xl px-4 py-3 hover:bg-orange-50 transition"
                               >

                                 <div className="flex items-center gap-4">

                                   <div className="w-10 h-10 rounded-full bg-orange-500 text-white flex items-center justify-center font-bold">
                                     #{index + 1}
                                   </div>

                                   <div>

                                     <h4 className="font-semibold text-gray-800">
                                       {religion.religionName}
                                     </h4>

                                     <p className="text-xs text-gray-500">
                                       Registered Members
                                     </p>

                                   </div>

                                 </div>

                                 <span className="text-lg font-bold text-orange-600">
                                   {religion.userCount}
                                 </span>

                               </div>

                             ))}

                           </div>

                         )}

                       </div>

                     </div>

                     {/* ========================================= */}
                     {/* QUICK ACTIONS */}
                     {/* ========================================= */}

                     <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">

                       <div className="flex items-center justify-between mb-6">

                         <div>

                           <h2 className="text-xl font-semibold text-gray-800">
                             Quick Actions
                           </h2>

                           <p className="text-sm text-gray-500 mt-1">
                             Navigate quickly to important admin modules.
                           </p>

                         </div>

                       </div>

                       <div className="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-5">

                         <button
                           onClick={() => navigate("/users")}
                           className="rounded-xl bg-violet-50 hover:bg-violet-100 transition p-5 text-left"
                         >
                           <FaUsers className="text-3xl text-violet-700 mb-3" />
                           <h4 className="font-semibold text-gray-800">
                             Users
                           </h4>
                           <p className="text-xs text-gray-500 mt-1">
                             Manage registered users
                           </p>
                         </button>

                         <button
                           onClick={() => navigate("/subscriptions")}
                           className="rounded-xl bg-yellow-50 hover:bg-yellow-100 transition p-5 text-left"
                         >
                           <FaCrown className="text-3xl text-yellow-600 mb-3" />
                           <h4 className="font-semibold text-gray-800">
                             Subscriptions
                           </h4>
                           <p className="text-xs text-gray-500 mt-1">
                             Premium plans & users
                           </p>
                         </button>

                         <button
                           onClick={() => navigate("/payments")}
                           className="rounded-xl bg-green-50 hover:bg-green-100 transition p-5 text-left"
                         >
                           <FaMoneyBillWave className="text-3xl text-green-600 mb-3" />
                           <h4 className="font-semibold text-gray-800">
                             Payments
                           </h4>
                           <p className="text-xs text-gray-500 mt-1">
                             Payment history
                           </p>
                         </button>

                         <button
                           onClick={() => navigate("/reported-profiles")}
                           className="rounded-xl bg-red-50 hover:bg-red-100 transition p-5 text-left"
                         >
                           <FaShieldAlt className="text-3xl text-red-600 mb-3" />
                           <h4 className="font-semibold text-gray-800">
                             Reports
                           </h4>
                           <p className="text-xs text-gray-500 mt-1">
                             View reported profiles
                           </p>
                         </button>

                       </div>

                     </div>
                           {/* ========================================= */}
                           {/* DASHBOARD SUMMARY */}
                           {/* ========================================= */}

                           <div className="grid grid-cols-1 lg:grid-cols-4 gap-5">

                             <div className="bg-gradient-to-r from-violet-600 to-purple-700 rounded-2xl text-white p-6 shadow-lg">

                               <h3 className="text-sm uppercase tracking-wider opacity-80">
                                 Verified Users
                               </h3>

                               <h2 className="text-3xl font-bold mt-2">
                                 {dashboard.verifiedUsers ?? 0}
                               </h2>

                               <p className="text-sm opacity-80 mt-2">
                                 Email & Phone Verified
                               </p>

                             </div>

                             <div className="bg-gradient-to-r from-green-500 to-emerald-600 rounded-2xl text-white p-6 shadow-lg">

                               <h3 className="text-sm uppercase tracking-wider opacity-80">
                                 Successful Payments
                               </h3>

                               <h2 className="text-3xl font-bold mt-2">
                                 {dashboard.successfulTransactions ?? 0}
                               </h2>

                               <p className="text-sm opacity-80 mt-2">
                                 Completed Transactions
                               </p>

                             </div>

                             <div className="bg-gradient-to-r from-red-500 to-rose-600 rounded-2xl text-white p-6 shadow-lg">

                               <h3 className="text-sm uppercase tracking-wider opacity-80">
                                 Failed Payments
                               </h3>

                               <h2 className="text-3xl font-bold mt-2">
                                 {dashboard.failedTransactions ?? 0}
                               </h2>

                               <p className="text-sm opacity-80 mt-2">
                                 Failed Transactions
                               </p>

                             </div>

                             <div className="bg-gradient-to-r from-amber-500 to-orange-500 rounded-2xl text-white p-6 shadow-lg">

                               <h3 className="text-sm uppercase tracking-wider opacity-80">
                                 Pending Payments
                               </h3>

                               <h2 className="text-3xl font-bold mt-2">
                                 {dashboard.pendingTransactions ?? 0}
                               </h2>

                               <p className="text-sm opacity-80 mt-2">
                                 Awaiting Completion
                               </p>

                             </div>

                           </div>

                           {/* ========================================= */}
                           {/* FOOTER */}
                           {/* ========================================= */}

                           <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-5">

                             <div className="flex flex-col lg:flex-row items-center justify-between gap-3">

                               <div>

                                 <h3 className="font-semibold text-gray-800">
                                   Gathbandhan Matrimony Admin Dashboard
                                 </h3>

                                 <p className="text-sm text-gray-500 mt-1">
                                   Monitor users, subscriptions, reports, payments and platform
                                   performance from one centralized dashboard.
                                 </p>

                               </div>

                               <button
                                 onClick={refreshDashboard}
                                 disabled={refreshing}
                                 className="px-5 py-3 rounded-xl bg-violet-600 hover:bg-violet-700 text-white transition disabled:opacity-60"
                               >
                                 {refreshing ? "Refreshing..." : "Refresh Dashboard"}
                               </button>

                             </div>

                           </div>

                         </div>
                       );
                     }
