import React from "react";
import {
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
} from "recharts";

const COLORS = [
  "#7C3AED",
  "#EC4899",
  "#3B82F6",
  "#10B981",
  "#F59E0B",
  "#EF4444",
  "#6366F1",
  "#14B8A6",
];

export default function SubscriptionChart({
  plans = [],
  height = 320,
}) {

  const chartData = (plans || []).map((plan, index) => ({
    name:
      plan.planName ||
      plan.name ||
      `Plan ${index + 1}`,

    value:
      plan.purchaseCount ||
      plan.count ||
      plan.total ||
      0,

    color: COLORS[index % COLORS.length],
  }));

  const total = chartData.reduce(
    (sum, item) => sum + item.value,
    0
  );

  const innerRadius = Math.max(
    40,
    Math.floor(height * 0.23)
  );

  const outerRadius = Math.max(
    70,
    Math.floor(height * 0.36)
  );

  if (chartData.length === 0) {
    return (
      <div
        className="flex items-center justify-center text-gray-400"
        style={{ height }}
      >
        No subscription data available.
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 items-center">

      {/* Donut Chart */}

      <div
        className="relative"
        style={{ height }}
      >

        <ResponsiveContainer
          width="100%"
          height="100%"
        >
          <PieChart>

            <Pie
              data={chartData}
              dataKey="value"
              innerRadius={innerRadius}
              outerRadius={outerRadius}
              paddingAngle={4}
            >

              {chartData.map((item, index) => (

                <Cell
                  key={index}
                  fill={item.color}
                />

              ))}

            </Pie>

          </PieChart>

        </ResponsiveContainer>

        <div className="absolute inset-0 flex flex-col items-center justify-center">

          <p className="text-gray-500 text-sm">
            Total
          </p>

          <h2 className="text-4xl font-bold text-gray-800">
            {total}
          </h2>

          <p className="text-violet-600 font-medium">
            Purchases
          </p>

        </div>

      </div>

      {/* Legend */}

      <div className="space-y-6">

        {chartData.map((item) => {

          const percent =
            total === 0
              ? 0
              : (
                  (item.value / total) *
                  100
                ).toFixed(0);

          return (

            <div key={item.name}>

              <div className="flex justify-between mb-2">

                <div className="flex items-center gap-3">

                  <span
                    className="w-4 h-4 rounded-full"
                    style={{
                      backgroundColor:
                        item.color,
                    }}
                  />

                  <span className="font-semibold">
                    {item.name}
                  </span>

                </div>

                <span className="font-bold">
                  {item.value}
                </span>

              </div>

              <div className="w-full h-3 bg-gray-200 rounded-full">

                <div
                  className="h-3 rounded-full"
                  style={{
                    width: `${percent}%`,
                    backgroundColor:
                      item.color,
                  }}
                />

              </div>

              <p className="text-sm text-gray-500 mt-1">
                {percent}% of Total
              </p>

            </div>

          );

        })}

      </div>

    </div>
  );
}