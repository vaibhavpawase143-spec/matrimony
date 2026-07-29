import clsx from "clsx";

export default function StatusBadge({
  label,
  type = "default",
}) {
  const styles = {
    success:
      "bg-green-100 text-green-700 border border-green-200",

    danger:
      "bg-red-100 text-red-700 border border-red-200",

    warning:
      "bg-yellow-100 text-yellow-700 border border-yellow-200",

    info:
      "bg-blue-100 text-blue-700 border border-blue-200",

    premium:
      "bg-purple-100 text-purple-700 border border-purple-200",

    default:
      "bg-gray-100 text-gray-700 border border-gray-200",
  };

  return (
    <span
      className={clsx(
        "inline-flex items-center rounded-full px-3 py-1 text-xs font-semibold",
        styles[type] || styles.default
      )}
    >
      {label}
    </span>
  );
}