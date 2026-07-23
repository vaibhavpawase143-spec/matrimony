import { useState } from "react";
import {
  FaPlus,
  FaEdit,
  FaTrash,
  FaSearch,
} from "react-icons/fa";

export default function MasterData() {
  const tabs = [
    "Religion",
    "Caste",
    "Sub Caste",
    "Country",
    "State",
    "City",
    "Occupation",
    "Qualification",
    "Income",
    "Education",
    "Height",
    "Weight",
    "Diet",
    "Smoking",
    "Drinking",
    "Body Type",
    "Complexion",
    "Marital Status",
    "Mother Tongue",
    "Employment",
    "Family Type",
    "Subscription Plans",
  ];

  const [activeTab, setActiveTab] = useState("Religion");
  const [search, setSearch] = useState("");

  const sectionData = {
    Religion: [
      { id: 1, name: "Hindu", status: "Active" },
      { id: 2, name: "Muslim", status: "Active" },
      { id: 3, name: "Christian", status: "Inactive" },
      { id: 4, name: "Jain", status: "Active" },
    ],
    Caste: [
      { id: 1, name: "Brahmin", status: "Active" },
      { id: 2, name: "Kshatriya", status: "Active" },
      { id: 3, name: "Vaishya", status: "Inactive" },
      { id: 4, name: "Shudra", status: "Active" },
    ],
    "Sub Caste": [
      { id: 1, name: "Iyer", status: "Active" },
      { id: 2, name: "Nair", status: "Active" },
      { id: 3, name: "Maratha", status: "Inactive" },
      { id: 4, name: "Patel", status: "Active" },
    ],
  };

  const currentData = sectionData[activeTab] || [];

  const filtered = currentData.filter((item) =>
    item.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="p-6">

      <div className="flex justify-between items-center mb-6">

        <div>
          <h1 className="text-3xl font-bold">Master Data</h1>
          <p className="text-gray-500">
            Manage all master data from one place.
          </p>
        </div>

        <button className="bg-purple-600 hover:bg-purple-700 text-white px-5 py-2 rounded-lg flex items-center gap-2">
          <FaPlus />
          Add
        </button>

      </div>

      {/* Tabs */}

      <div className="flex gap-3 overflow-x-auto mb-6 pb-2">

        {tabs.map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-4 py-2 rounded-lg whitespace-nowrap ${
              activeTab === tab
                ? "bg-purple-600 text-white"
                : "bg-gray-200"
            }`}
          >
            {tab}
          </button>
        ))}

      </div>

      {/* Search */}

      <div className="bg-white rounded-xl shadow p-4 flex items-center gap-3 mb-5">

        <FaSearch className="text-gray-500" />

        <input
          className="w-full outline-none"
          placeholder={`Search ${activeTab}`}
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

      </div>

      {/* Table */}

      <div className="bg-white rounded-xl shadow overflow-hidden">

        <table className="w-full">

          <thead className="bg-purple-600 text-white">

            <tr>
              <th className="p-3 text-left">ID</th>
              <th className="text-left">{activeTab}</th>
              <th className="text-left">Status</th>
              <th className="text-left">Actions</th>
            </tr>

          </thead>

          <tbody>

            {filtered.map((item) => (

              <tr
                key={`${activeTab}-${item.id}`}
                className="border-b hover:bg-purple-50"
              >

                <td className="p-3">{item.id}</td>

                <td>{item.name}</td>

                <td>

                  <span
                    className={`px-3 py-1 rounded-full text-white ${
                      item.status === "Active"
                        ? "bg-green-500"
                        : "bg-red-500"
                    }`}
                  >
                    {item.status}
                  </span>

                </td>

                <td>

                  <div className="flex justify-center gap-4">

                    <button className="text-blue-600">
                      <FaEdit />
                    </button>

                    <button className="text-red-600">
                      <FaTrash />
                    </button>

                  </div>

                </td>

              </tr>

            ))}

          </tbody>

        </table>

      </div>

      {/* Pagination */}

      <div className="flex justify-between items-center mt-6">

        <p>Total Records : {filtered.length}</p>

        <div className="flex gap-2">

          <button className="px-4 py-2 bg-gray-200 rounded">
            Previous
          </button>

          <button className="px-4 py-2 bg-purple-600 text-white rounded">
            1
          </button>

          <button className="px-4 py-2 bg-gray-200 rounded">
            Next
          </button>

        </div>

      </div>

    </div>
  );
}