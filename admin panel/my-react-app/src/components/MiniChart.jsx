import React, { useId, useMemo, useRef, useState } from "react";

export default function MiniChart({
  data = [],
  width = 600,
  height = 220,
  color = "#7C3AED",
  labels = [],
}) {
  const wrapperRef = useRef(null);
  const gradientId = useId();
  const [hoverIdx, setHoverIdx] = useState(null);

  if (!data || data.length === 0) {
    return (
      <div
        className="flex items-center justify-center text-gray-500"
        style={{ height }}
      >
        No data available
      </div>
    );
  }

  const max = Math.max(...data);
  const min = Math.min(...data);

  const stepX =
    data.length > 1
      ? width / (data.length - 1)
      : width / 2;

  const points = useMemo(() => {
    return data.map((value, index) => {
      const x =
        data.length === 1
          ? width / 2
          : index * stepX;

      const y =
        max === min
          ? height / 2
          : height -
            ((value - min) / (max - min)) *
              (height - 30) -
            15;

      return {
        x,
        y,
        value,
        label: labels[index],
      };
    });
  }, [data, labels, height, max, min, stepX, width]);

  const line = points
    .map((p) => `${p.x},${p.y}`)
    .join(" ");

  const area = `M0,${height}
      L${points
        .map((p) => `${p.x},${p.y}`)
        .join(" L")}
      L${width},${height}
      Z`;

  function handleMove(e) {
    const rect =
      wrapperRef.current.getBoundingClientRect();

    const clientX = e.clientX - rect.left;

    const svgX =
      (clientX / rect.width) * width;

    const index = Math.max(
      0,
      Math.min(
        data.length - 1,
        Math.round(svgX / stepX)
      )
    );

    setHoverIdx(index);
  }

  return (
    <div
      ref={wrapperRef}
      className="relative w-full"
      style={{ height }}
    >
      <svg
        width="100%"
        height={height}
        viewBox={`0 0 ${width} ${height}`}
        preserveAspectRatio="none"
        onMouseMove={handleMove}
        onMouseLeave={() => setHoverIdx(null)}
      >
        <defs>
          <linearGradient
            id={gradientId}
            x1="0"
            y1="0"
            x2="0"
            y2="1"
          >
            <stop
              offset="0%"
              stopColor={color}
              stopOpacity="0.35"
            />
            <stop
              offset="100%"
              stopColor={color}
              stopOpacity="0.05"
            />
          </linearGradient>
        </defs>

        <path
          d={area}
          fill={`url(#${gradientId})`}
        />

        <polyline
          fill="none"
          stroke={color}
          strokeWidth="4"
          strokeLinejoin="round"
          strokeLinecap="round"
          points={line}
        />

        {points.map((point, index) => (
          <circle
            key={index}
            cx={point.x}
            cy={point.y}
            r={
              hoverIdx === index
                ? 7
                : 5
            }
            fill={color}
            stroke="white"
            strokeWidth="2"
            style={{
              transition:
                "all .2s ease",
            }}
          />
        ))}

        {hoverIdx !== null && (
          <line
            x1={points[hoverIdx].x}
            x2={points[hoverIdx].x}
            y1={0}
            y2={height}
            stroke="#CBD5E1"
            strokeDasharray="5 5"
          />
        )}
      </svg>

      {hoverIdx !== null && (
        <div
          className="absolute bg-white rounded-lg shadow-lg border px-3 py-2 text-sm pointer-events-none"
          style={{
            left: `${
              (points[hoverIdx].x /
                width) *
              100
            }%`,
            top: Math.max(
              8,
              points[hoverIdx].y - 60
            ),
            transform:
              "translateX(-50%)",
            zIndex: 50,
          }}
        >
          <div className="font-semibold text-gray-800">
            {points[hoverIdx].label ??
              `Item ${
                hoverIdx + 1
              }`}
          </div>

          <div className="text-gray-600">
            {points[hoverIdx].value}
          </div>
        </div>
      )}
    </div>
  );
}