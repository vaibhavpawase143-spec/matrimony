import { useState, useEffect } from "react";
import AdminLayout from "@/components/AdminLayout";
import { Users, Search, Filter, Eye, Ban, CheckCircle, XCircle } from "lucide-react";
const adminRole =
    localStorage.getItem("adminRole");

const isSuperAdmin =
    adminRole === "ROLE_SUPER_ADMIN";
const AdminUsers = () => {
  const [users, setUsers] = useState([
    { id: 1, firstName: "Rahul", lastName: "Sharma", email: "rahul@test.com", phone: "9876543210", isActive: true, isBlocked: false, emailVerified: true, phoneVerified: true },
    { id: 2, firstName: "Sneha", lastName: "Patel", email: "sneha@test.com", phone: "9876543211", isActive: false, isBlocked: true, emailVerified: true, phoneVerified: false },
    { id: 3, firstName: "Amit", lastName: "Kumar", email: "amit@test.com", phone: "9876543212", isActive: true, isBlocked: false, emailVerified: false, phoneVerified: true },
    { id: 4, firstName: "Priya", lastName: "Singh", email: "priya@test.com", phone: "9876543213", isActive: false, isBlocked: false, emailVerified: false, phoneVerified: false }
  ]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [filter, setFilter] = useState("all");
  const [sortBy, setSortBy] = useState("a-z"); // "a-z", "z-a", "oldest", "newest"
  const [pagination, setPagination] = useState({ page: 0, size: 10, total: 4 });

  useEffect(() => {
    fetchUsers();
  }, [filter, pagination.page]);

  const fetchUsers = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) return;
      setLoading(true);
      const response = await fetch(
        `/api/admin/users?page=${pagination.page}&size=${pagination.size}&filter=${filter}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      const data = await response.json();
      if (data?.success && Array.isArray(data?.data?.content) && data.data.content.length > 0) {
        setUsers(data.data.content);
        setPagination(prev => ({ ...prev, total: data.data.totalElements || data.data.content.length }));
      }
    } catch (error) {
      console.error("Error fetching users, using fallback:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleBlockUser = async (userId) => {
    setUsers((prev) =>
      prev.map((u) => (u.id === userId ? { ...u, isBlocked: true } : u))
    );
    try {
      const token = localStorage.getItem("token");
      if (token) {
        await fetch(`/api/admin/users/${userId}/block`, {
          method: "PUT",
          headers: { Authorization: `Bearer ${token}` },
        });
      }
    } catch (error) {
      console.error("Error blocking user:", error);
    }
  };

  const handleUnblockUser = async (userId) => {
    setUsers((prev) =>
      prev.map((u) => (u.id === userId ? { ...u, isBlocked: false } : u))
    );
    try {
      const token = localStorage.getItem("token");
      if (token) {
        await fetch(`/api/admin/users/${userId}/unblock`, {
          method: "PUT",
          headers: { Authorization: `Bearer ${token}` },
        });
      }
    } catch (error) {
      console.error("Error unblocking user:", error);
    }
  };

  const handleToggleActive = async (userId) => {
    setUsers((prev) =>
      prev.map((u) => (u.id === userId ? { ...u, isActive: !u.isActive } : u))
    );
    const target = users.find((u) => u.id === userId);
    const endpoint = target?.isActive ? "deactivate" : "activate";
    try {
      const token = localStorage.getItem("token");
      if (token) {
        await fetch(`/api/admin/users/${userId}/${endpoint}`, {
          method: "PUT",
          headers: { Authorization: `Bearer ${token}` },
        });
      }
    } catch (error) {
      console.error("Error toggling status:", error);
    }
  };

  const handleVerifyEmail = async (userId) => {
    setUsers((prev) =>
      prev.map((u) => (u.id === userId ? { ...u, emailVerified: true } : u))
    );
    try {
      const token = localStorage.getItem("token");
      if (token) {
        await fetch(`/api/admin/verification/verify-email/${userId}`, {
          method: "POST",
          headers: { Authorization: `Bearer ${token}` },
        });
      }
    } catch (error) {
      console.error("Error verifying email:", error);
    }
  };

  const handleVerifyPhone = async (userId) => {
    setUsers((prev) =>
      prev.map((u) => (u.id === userId ? { ...u, phoneVerified: true } : u))
    );
    try {
      const token = localStorage.getItem("token");
      if (token) {
        await fetch(`/api/admin/verification/verify-phone/${userId}`, {
          method: "POST",
          headers: { Authorization: `Bearer ${token}` },
        });
      }
    } catch (error) {
      console.error("Error verifying phone:", error);
    }
  };

  const filteredUsers = users.filter(user => {
    // 1. Search filter
    const searchLower = searchTerm.toLowerCase();
    const fullName = `${user.firstName || ''} ${user.lastName || ''}`.toLowerCase();
    const emailStr = (user.email || '').toLowerCase();
    const phoneStr = (user.phone || '').toLowerCase();
    
    const matchesSearch =
      fullName.includes(searchLower) ||
      emailStr.includes(searchLower) ||
      phoneStr.includes(searchLower);

    if (!matchesSearch) return false;

    // 2. Status filter
    if (filter === "active") return user.isActive === true && !user.isBlocked;
    if (filter === "inactive") return user.isActive === false && !user.isBlocked;
    if (filter === "blocked") return user.isBlocked === true;
    if (filter === "unverified") return !user.emailVerified || !user.phoneVerified;

    return true;
  });

  const sortedUsers = [...filteredUsers].sort((a, b) => {
    const nameA = `${a.firstName || ''} ${a.lastName || ''}`.trim().toLowerCase();
    const nameB = `${b.firstName || ''} ${b.lastName || ''}`.trim().toLowerCase();
    const idA = Number(a.id) || 0;
    const idB = Number(b.id) || 0;

    if (sortBy === "a-z") {
      return nameA.localeCompare(nameB);
    } else if (sortBy === "z-a") {
      return nameB.localeCompare(nameA);
    } else if (sortBy === "oldest") {
      return idA - idB;
    } else if (sortBy === "newest") {
      return idB - idA;
    }
    return 0;
  });

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
            <Users size={28} />
            User Management
          </h1>
        </div>

        {/* Search and Filter */}
        <div className="bg-white p-4 rounded-xl shadow mb-6">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-3 text-gray-400" size={20} />
              <input
                type="text"
                placeholder="Search users..."
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
              <option value="all">All Status</option>
              <option value="active">Active</option>
              <option value="inactive">Inactive</option>
              <option value="blocked">Blocked</option>
              <option value="unverified">Unverified</option>
            </select>
            <select
              className="px-4 py-2 border rounded-lg font-medium bg-white"
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
            >
              <option value="a-z">Sort: A to Z</option>
              <option value="z-a">Sort: Z to A</option>
              <option value="oldest">Sort: Oldest to Newest</option>
              <option value="newest">Sort: Newest to Oldest</option>
            </select>
          </div>
        </div>

        {/* Users Table */}
        <div className="bg-white rounded-xl shadow overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50 border-b">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    User
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Contact
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Verification
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {sortedUsers.map((user) => (
                  <tr key={user.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm font-medium text-gray-900">
                          {user.firstName} {user.lastName}
                        </div>
                        <div className="text-sm text-gray-500">ID: {user.id}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">{user.email}</div>
                      <div className="text-sm text-gray-500">{user.phone}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex gap-2">
                        <span
                          className={`px-2 py-1 text-xs rounded-full ${
                            user.isActive
                              ? "bg-green-100 text-green-800"
                              : "bg-red-100 text-red-800"
                          }`}
                        >
                          {user.isActive ? "Active" : "Inactive"}
                        </span>
                        {user.isBlocked && (
                          <span className="px-2 py-1 text-xs rounded-full bg-red-100 text-red-800">
                            Blocked
                          </span>
                        )}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex gap-2">
                        {user.emailVerified ? (
                          <CheckCircle size={16} className="text-green-500" />
                        ) : (
                          <XCircle size={16} className="text-red-500" />
                        )}
                        {user.phoneVerified ? (
                          <CheckCircle size={16} className="text-green-500" />
                        ) : (
                          <XCircle size={16} className="text-red-500" />
                        )}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex gap-2 items-center">
                        <button
                          onClick={() => handleToggleActive(user.id)}
                          className={`px-2 py-1 text-xs rounded border transition ${
                            user.isActive
                              ? "bg-amber-50 text-amber-700 border-amber-200 hover:bg-amber-100"
                              : "bg-green-50 text-green-700 border-green-200 hover:bg-green-100"
                          }`}
                          title={user.isActive ? "Deactivate User" : "Activate User"}
                        >
                          {user.isActive ? "Deactivate" : "Activate"}
                        </button>
                        {!user.emailVerified && (
                          <button
                            onClick={() => handleVerifyEmail(user.id)}
                            className="text-blue-600 hover:text-blue-900"
                            title="Verify Email"
                          >
                            <CheckCircle size={16} />
                          </button>
                        )}
                        {!user.phoneVerified && (
                          <button
                            onClick={() => handleVerifyPhone(user.id)}
                            className="text-blue-600 hover:text-blue-900"
                            title="Verify Phone"
                          >
                            <CheckCircle size={16} />
                          </button>
                        )}
                        {user.isBlocked ? (
                          <button
                            onClick={() => handleUnblockUser(user.id)}
                            className="text-green-600 hover:text-green-900"
                            title="Unblock User"
                          >
                            <Ban size={16} />
                          </button>
                        ) : (
                          <button
                            onClick={() => handleBlockUser(user.id)}
                            className="text-red-600 hover:text-red-900"
                            title="Block User"
                          >
                            <Ban size={16} />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Pagination */}
          <div className="bg-gray-50 px-4 py-3 flex items-center justify-between border-t border-gray-200">
            <div className="flex-1 flex justify-between sm:hidden">
              <button
                onClick={() => setPagination(prev => ({ ...prev, page: Math.max(0, prev.page - 1) }))}
                disabled={pagination.page === 0}
                className="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
              >
                Previous
              </button>
              <button
                onClick={() => setPagination(prev => ({ ...prev, page: prev.page + 1 }))}
                disabled={pagination.page * pagination.size >= pagination.total}
                className="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
              >
                Next
              </button>
            </div>
          </div>
        </div>
      </div>
    </AdminLayout>
  );
};

export default AdminUsers;
