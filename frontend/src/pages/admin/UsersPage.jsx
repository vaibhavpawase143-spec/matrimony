import { useState } from "react";
import AdminLayout from "@/components/AdminLayout";

const UsersPage = () => {
  const [tab, setTab] = useState("all");
  const [selectedUser, setSelectedUser] = useState(null);
  const [editUser, setEditUser] = useState(null);

  const [users, setUsers] = useState([
    { id: 1, name: "Rahul", email: "rahul@test.com", status: "Active" },
    { id: 2, name: "Sneha", email: "sneha@test.com", status: "Blocked" },
    { id: 3, name: "Amit", email: "amit@test.com", status: "Active" },
  ]);

  // 🔥 FILTER
  const filteredUsers = users.filter((u) =>
    tab === "all" ? true : u.status === "Blocked"
  );

  // 🚫 BLOCK / UNBLOCK
  const toggleStatus = (id) => {
    setUsers(users.map((u) =>
      u.id === id
        ? { ...u, status: u.status === "Active" ? "Blocked" : "Active" }
        : u
    ));
  };

  // 💾 SAVE EDIT
  const saveEdit = () => {
    setUsers(users.map((u) =>
      u.id === editUser.id ? editUser : u
    ));
    setEditUser(null);
  };

  return (
    <AdminLayout>
      <div className="bg-gray-50 min-h-screen p-4 md:p-6">

        {/* HEADER */}
        <h1 className="text-2xl font-bold text-gray-800 mb-6">
          User Management
        </h1>

        {/* TABS */}
        <div className="flex gap-3 mb-6">
          {["all", "blocked"].map((t) => (
            <button
              key={t}
              onClick={() => setTab(t)}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition ${
                tab === t
                  ? "bg-purple-500 text-white shadow"
                  : "bg-white border hover:bg-gray-100"
              }`}
            >
              {t.toUpperCase()}
            </button>
          ))}
        </div>

        {/* TABLE */}
        <div className="bg-white rounded-2xl shadow overflow-hidden">

          <div className="overflow-x-auto">
            <table className="w-full text-sm">

              <thead className="bg-gray-100 text-gray-600">
                <tr>
                  <th className="p-4 text-left">Name</th>
                  <th>Email</th>
                  <th className="text-center">Status</th>
                  <th className="text-center">Actions</th>
                </tr>
              </thead>

              <tbody>
                {filteredUsers.map((u) => (
                  <tr key={u.id} className="border-t hover:bg-gray-50 transition">

                    <td className="p-4 font-medium">{u.name}</td>
                    <td className="text-gray-600">{u.email}</td>

                    {/* ✅ FIXED STATUS */}
                    <td className="text-center">
                      <div className="flex justify-center">
                        <span
                          className={`px-3 py-1 rounded-full text-xs font-semibold flex items-center gap-1 ${
                            u.status === "Active"
                              ? "bg-green-100 text-green-700"
                              : "bg-red-100 text-red-700"
                          }`}
                        >
                          <span
                            className={`w-2 h-2 rounded-full ${
                              u.status === "Active"
                                ? "bg-green-500"
                                : "bg-red-500"
                            }`}
                          ></span>
                          {u.status}
                        </span>
                      </div>
                    </td>

                    {/* ACTIONS */}
                    <td>
                      <div className="flex justify-center gap-2">

                        <button
                          onClick={() => setSelectedUser(u)}
                          className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded text-xs"
                        >
                          View
                        </button>

                        <button
                          onClick={() => setEditUser(u)}
                          className="bg-yellow-500 hover:bg-yellow-600 text-white px-3 py-1 rounded text-xs"
                        >
                          Edit
                        </button>

                        <button
                          onClick={() => toggleStatus(u.id)}
                          className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded text-xs"
                        >
                          {u.status === "Active" ? "Block" : "Unblock"}
                        </button>

                      </div>
                    </td>

                  </tr>
                ))}
              </tbody>

            </table>
          </div>

        </div>

        {/* 👁️ VIEW MODAL */}
        {selectedUser && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-2xl shadow-lg w-80">

              <h2 className="text-lg font-bold mb-3">User Details</h2>

              <p><b>Name:</b> {selectedUser.name}</p>
              <p><b>Email:</b> {selectedUser.email}</p>
              <p><b>Status:</b> {selectedUser.status}</p>

              <button
                onClick={() => setSelectedUser(null)}
                className="mt-4 w-full bg-gray-500 text-white py-2 rounded"
              >
                Close
              </button>

            </div>
          </div>
        )}

        {/* ✏️ EDIT MODAL */}
        {editUser && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-2xl shadow-lg w-80">

              <h2 className="text-lg font-bold mb-3">Edit User</h2>

              <input
                value={editUser.name}
                onChange={(e) =>
                  setEditUser({ ...editUser, name: e.target.value })
                }
                className="border p-2 w-full mb-3 rounded"
              />

              <input
                value={editUser.email}
                onChange={(e) =>
                  setEditUser({ ...editUser, email: e.target.value })
                }
                className="border p-2 w-full mb-3 rounded"
              />

              <div className="flex gap-2">
                <button
                  onClick={saveEdit}
                  className="bg-green-500 text-white w-full py-2 rounded"
                >
                  Save
                </button>

                <button
                  onClick={() => setEditUser(null)}
                  className="bg-gray-500 text-white w-full py-2 rounded"
                >
                  Cancel
                </button>
              </div>

            </div>
          </div>
        )}

      </div>
    </AdminLayout>
  );
};

export default UsersPage;