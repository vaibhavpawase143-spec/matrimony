import React from "react";

export default function StatsCard({
  title,
  value,
  icon,
  type = "default",
  subtitle,
  growth,
}) {
  const colorClasses = {
    users: "from-violet-500 to-purple-600",
    active: "from-green-500 to-emerald-600",
    inactive: "from-gray-500 to-gray-700",
    blocked: "from-red-500 to-rose-600",
    verified: "from-blue-500 to-cyan-600",
    premium: "from-pink-500 to-rose-600",
    revenue: "from-green-500 to-lime-500",
    reports: "from-orange-500 to-red-500",
    subscriptions: "from-indigo-500 to-violet-600",
    default: "from-slate-500 to-slate-700",
  };

  const bgGradient = colorClasses[type] || colorClasses.default;

  return (
    <div className="bg-white rounded-2xl shadow-md hover:shadow-xl transition-all duration-300 border border-gray-100 p-6">

      <div className="flex items-start justify-between">

        <div className="flex-1">

          <p className="text-sm font-medium text-gray-500">
            {title}
          </p>

          <h2 className="mt-3 text-3xl font-bold text-gray-900">
            {value ?? 0}
          </h2>

          {subtitle && (
            <p className="mt-2 text-sm text-gray-500">
              {subtitle}
            </p>
          )}

          {growth !== undefined && growth !== null && (
            <div
              className={`mt-3 inline-flex items-center rounded-full px-3 py-1 text-xs font-semibold ${
                growth >= 0
                  ? "bg-green-100 text-green-700"
                  : "bg-red-100 text-red-700"
              }`}
            >
              {growth >= 0 ? "▲" : "▼"} {Math.abs(growth)}%
            </div>
          )}

        </div>

        <div
          className={`w-16 h-16 rounded-2xl bg-gradient-to-r ${bgGradient} flex items-center justify-center text-white shadow-lg`}
        >
          {icon}
        </div>

      </div>

    </div>
  );
}