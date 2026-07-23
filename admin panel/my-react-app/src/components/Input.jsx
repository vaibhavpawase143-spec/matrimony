import React from "react";

export default function Input({ label, error, className = "", ...props }) {
  return (
    <label className="block text-sm font-medium text-gray-700">
      {label && <span className="mb-2 inline-block">{label}</span>}
      <input
        className={`mt-1 block w-full rounded-xl border border-purple-300 bg-white px-3 py-2 text-sm shadow-sm outline-none transition focus:border-violet-500 focus:ring-2 focus:ring-violet-100 ${
          error ? "border-red-500" : ""
        } ${className}`}
        {...props}
      />
      {error && <p className="mt-1 text-xs text-red-600">{error}</p>}
    </label>
  );
}
