import { useEffect, useRef, useState } from "react";
import { ChevronDown, FileText, FileSpreadsheet, File } from "lucide-react";

export default function ExportDropdown({
  onPDF,
  onExcel,
  onCSV,
  disabled = false,
}) {
  const [open, setOpen] = useState(false);
  const dropdownRef = useRef(null);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target)
      ) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const handleAction = (callback) => {
    setOpen(false);

    if (callback) {
      callback();
    }
  };

  return (
    <div className="relative inline-block" ref={dropdownRef}>
      <button
        disabled={disabled}
        onClick={() => setOpen((prev) => !prev)}
        className={`flex items-center gap-2 px-4 py-2 rounded-lg text-white font-medium shadow transition
          ${
            disabled
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-violet-600 hover:bg-violet-700"
          }`}
      >
        Export
        <ChevronDown size={18} />
      </button>

      {open && (
        <div className="absolute right-0 mt-2 w-52 bg-white rounded-xl shadow-xl border border-gray-200 z-50 overflow-hidden">
          <button
            onClick={() => handleAction(onPDF)}
            className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-gray-100 transition"
          >
            <FileText size={18} className="text-red-600" />
            Export PDF
          </button>

          <button
            onClick={() => handleAction(onExcel)}
            className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-gray-100 transition"
          >
            <FileSpreadsheet size={18} className="text-green-600" />
            Export Excel
          </button>

          <button
            onClick={() => handleAction(onCSV)}
            className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-gray-100 transition"
          >
            <File size={18} className="text-blue-600" />
            Export CSV
          </button>
        </div>
      )}
    </div>
  );
}