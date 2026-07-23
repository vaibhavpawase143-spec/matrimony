export default function InfoRow({
  label,
  value,
  border = true,
}) {
  return (
    <div
      className={`flex items-center justify-between py-3 ${
        border ? "border-b border-gray-100" : ""
      }`}
    >
      <span className="text-sm font-medium text-gray-500">
        {label}
      </span>

      <span className="text-sm font-semibold text-gray-800 text-right">
        {value}
      </span>
    </div>
  );
}