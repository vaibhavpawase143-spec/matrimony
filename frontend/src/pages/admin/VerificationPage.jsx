import { useState } from "react";
import AdminLayout from "@/components/AdminLayout";

const VerificationPage = () => {
  const [tab, setTab] = useState("id");
  const [selected, setSelected] = useState(null);

  const [data, setData] = useState([
    {
      id: 1,
      name: "Rahul",
      type: "id",
      status: "Pending",
      img: "https://images.unsplash.com/photo-1589571894960-20bbe2828d0a",
    },
    {
      id: 2,
      name: "Sneha",
      type: "photo",
      status: "Pending",
      img: "https://images.unsplash.com/photo-1615109398623-88346a601842",
    },
    {
      id: 3,
      name: "Amit",
      type: "verified",
      status: "Approved",
      img: "https://images.unsplash.com/photo-1607746882042-944635dfe10e",
    },
  ]);

  // 🔥 FILTER
  const filtered = data.filter((d) => {
    if (tab === "id") return d.type === "id";
    if (tab === "photo") return d.type === "photo";
    if (tab === "verified") return d.type === "verified";
  });

  // ✅ APPROVE
  const approve = (id) => {
    setData(data.map((d) =>
      d.id === id ? { ...d, status: "Approved", type: "verified" } : d
    ));
  };

  // ❌ REJECT
  const reject = (id) => {
    setData(data.map((d) =>
      d.id === id ? { ...d, status: "Rejected" } : d
    ));
  };

  return (
    <AdminLayout>
      <div className="bg-gray-50 min-h-screen p-4 md:p-6">

        {/* HEADER */}
        <h1 className="text-2xl font-bold mb-4">
          Verification Management
        </h1>

        {/* 🔥 TABS */}
        <div className="flex gap-3 mb-6">
          {[
            { key: "id", label: "ID Verification" },
            { key: "photo", label: "Photo Verification" },
            { key: "verified", label: "Verified" },
          ].map((t) => (
            <button
              key={t.key}
              onClick={() => setTab(t.key)}
              className={`px-4 py-2 rounded ${
                tab === t.key
                  ? "bg-purple-500 text-white"
                  : "bg-white border"
              }`}
            >
              {t.label}
            </button>
          ))}
        </div>

        {/* 💘 CARDS */}
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">

          {filtered.map((p) => (
            <div key={p.id} className="bg-white rounded-xl shadow p-3">

              <img
                src={p.img}
                className="w-full h-40 object-cover rounded-lg"
              />

              <div className="mt-2 text-center">
                <h2 className="font-semibold">{p.name}</h2>
              </div>

              {/* STATUS */}
              <div className="text-center mt-2">
                <span className={`px-2 py-1 text-xs rounded-full ${
                  p.status === "Approved"
                    ? "bg-green-100 text-green-600"
                    : p.status === "Rejected"
                    ? "bg-red-100 text-red-600"
                    : "bg-yellow-100 text-yellow-600"
                }`}>
                  {p.status}
                </span>
              </div>

              {/* ACTIONS */}
              <div className="flex gap-2 mt-3">

                <button
                  onClick={() => setSelected(p)}
                  className="bg-blue-500 text-white w-full py-1 rounded text-xs"
                >
                  View
                </button>

                {p.status === "Pending" && (
                  <>
                    <button
                      onClick={() => approve(p.id)}
                      className="bg-green-500 text-white w-full py-1 rounded text-xs"
                    >
                      Approve
                    </button>

                    <button
                      onClick={() => reject(p.id)}
                      className="bg-red-500 text-white w-full py-1 rounded text-xs"
                    >
                      Reject
                    </button>
                  </>
                )}

              </div>

            </div>
          ))}

        </div>

        {/* 👁️ MODAL */}
        {selected && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center">
            <div className="bg-white p-6 rounded-xl w-80">

              <h2 className="font-bold mb-2">Verification Details</h2>

              <img
                src={selected.img}
                className="w-full h-40 rounded mb-2"
              />

              <p>Name: {selected.name}</p>
              <p>Status: {selected.status}</p>

              <button
                onClick={() => setSelected(null)}
                className="mt-3 bg-gray-500 text-white px-3 py-1 rounded w-full"
              >
                Close
              </button>

            </div>
          </div>
        )}

      </div>
    </AdminLayout>
  );
};

export default VerificationPage;