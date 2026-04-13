import AdminLayout from "@/components/AdminLayout";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid,
  Legend,
} from "recharts";

const AdminDashboard = () => {
  const stats = [
    {
      title: "Total Users",
      value: "1,890",
      sub: "+10% This Month",
      color: "from-purple-500 to-indigo-500",
    },
    {
      title: "Matches",
      value: "560",
      sub: "+8% This Month",
      color: "from-pink-500 to-rose-500",
    },
    {
      title: "Premium Users",
      value: "320",
      sub: "+5% This Month",
      color: "from-teal-400 to-cyan-500",
    },
    {
      title: "New Signups",
      value: "410",
      sub: "Last 7 Days",
      color: "from-orange-400 to-red-500",
    },
  ];

  const chartData = [
    { name: "Jan", thisMonth: 2000, lastMonth: 1800 },
    { name: "Feb", thisMonth: 4000, lastMonth: 3000 },
    { name: "Mar", thisMonth: 7000, lastMonth: 6000 },
    { name: "Apr", thisMonth: 6000, lastMonth: 5500 },
    { name: "May", thisMonth: 10000, lastMonth: 7000 },
    { name: "Jun", thisMonth: 8000, lastMonth: 6500 },
    { name: "Jul", thisMonth: 12000, lastMonth: 9000 },
    { name: "Aug", thisMonth: 15000, lastMonth: 11000 },
    { name: "Sep", thisMonth: 18000, lastMonth: 13000 },
    { name: "Oct", thisMonth: 20000, lastMonth: 16000 },
    { name: "Nov", thisMonth: 22000, lastMonth: 18000 },
    { name: "Dec", thisMonth: 25000, lastMonth: 20000 },
    
  ];

  return (
    <AdminLayout>
      <div className="bg-gray-50 min-h-screen p-6">

        {/* HEADER */}
        <h1 className="text-2xl font-bold mb-6">
          Matrimony Admin Dashboard
        </h1>

        {/* STATS */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
          {stats.map((item, i) => (
            <div
              key={i}
              className={`bg-gradient-to-r ${item.color} text-white p-5 rounded-xl shadow-lg`}
            >
              <p className="text-sm">{item.title}</p>
              <h2 className="text-2xl font-bold">{item.value}</h2>
              <p className="text-xs opacity-80">{item.sub}</p>
            </div>
          ))}
        </div>

        {/* GRAPH + ACTIVITY */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">

          {/* GRAPH */}
          <div className="bg-white p-5 rounded-xl shadow">
            <h2 className="font-semibold mb-4">User Growth</h2>

            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />

                <Line
                  type="monotone"
                  dataKey="thisMonth"
                  stroke="#7c3aed"
                  strokeWidth={3}
                />

                <Line
                  type="monotone"
                  dataKey="lastMonth"
                  stroke="#94a3b8"
                  strokeWidth={3}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>

          {/* ACTIVITY */}
          <div className="bg-white p-5 rounded-xl shadow">
            <h2 className="font-semibold mb-3">Recent Activity</h2>
            <ul className="text-sm space-y-2 text-gray-600">
              <li>• New user registered</li>
              <li>• Rahul & Priya matched</li>
              <li>• Premium plan purchased ₹1200</li>
              <li>• Swati updated profile</li>
              <li>• Payment failed ₹500</li>
            </ul>
          </div>
        </div>

        {/* USERS TABLE */}
        <div className="mt-6 bg-white p-5 rounded-xl shadow">
          <h2 className="font-semibold mb-3">Recent Users</h2>

          <table className="w-full text-sm">
            <thead>
              <tr className="text-gray-500 border-b">
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
              </tr>
            </thead>

            <tbody>
              <tr className="text-center border-b">
                <td>1</td>
                <td>Rajesh</td>
                <td>rajesh@gmail.com</td>
              </tr>
              <tr className="text-center border-b">
                <td>2</td>
                <td>Rina</td>
                <td>rina@gmail.com</td>
              </tr>
              <tr className="text-center">
                <td>3</td>
                <td>Rohit</td>
                <td>rohit@gmail.com</td>
              </tr>
            </tbody>
          </table>
        </div>

        {/* MATCHES */}
        <div className="mt-6 bg-white p-5 rounded-xl shadow">
          <h2 className="font-semibold mb-4">Recent Matches</h2>

          <div className="grid grid-cols-2 md:grid-cols-3 gap-4">

            <div className="bg-gray-100 p-2 rounded-xl">
              <div className="flex gap-2">
                <img
                  src="https://images.unsplash.com/photo-1589571894960-20bbe2828d0a"
                  className="w-1/2 h-32 object-cover rounded-lg"
                />
                <img
                  src="https://images.unsplash.com/photo-1615109398623-88346a601842"
                  className="w-1/2 h-32 object-cover rounded-lg"
                />
              </div>
              <p className="mt-2 text-center font-medium">
                Rahul & Priya
              </p>
            </div>

            <div className="bg-gray-100 p-2 rounded-xl">
              <div className="flex gap-2">
                <img
                  src="https://images.unsplash.com/photo-1607746882042-944635dfe10e"
                  className="w-1/2 h-32 object-cover rounded-lg"
                />
                <img
                  src="https://images.unsplash.com/photo-1590080875515-8a3a8dc5735e"
                  className="w-1/2 h-32 object-cover rounded-lg"
                />
              </div>
              <p className="mt-2 text-center font-medium">
                Amit & Nisha
              </p>
            </div>

          </div>
        </div>

      </div>
    </AdminLayout>
  );
};

export default AdminDashboard;