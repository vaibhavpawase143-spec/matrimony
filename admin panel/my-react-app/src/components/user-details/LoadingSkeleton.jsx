export default function LoadingSkeleton() {
  return (
    <div className="space-y-6 animate-pulse">
      {/* Header */}
      <div className="bg-white rounded-xl border p-6">
        <div className="flex items-center gap-5">
          <div className="w-24 h-24 rounded-full bg-gray-200"></div>

          <div className="flex-1 space-y-3">
            <div className="h-7 w-56 bg-gray-200 rounded"></div>
            <div className="h-4 w-40 bg-gray-200 rounded"></div>
            <div className="h-4 w-64 bg-gray-200 rounded"></div>
          </div>
        </div>
      </div>

      {/* Cards */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {[1, 2, 3, 4].map((item) => (
          <div
            key={item}
            className="bg-white rounded-xl border p-6"
          >
            <div className="h-6 w-40 bg-gray-200 rounded mb-6"></div>

            <div className="space-y-4">
              {[1, 2, 3, 4].map((row) => (
                <div
                  key={row}
                  className="h-4 bg-gray-200 rounded"
                />
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}