/**
 * Generic CSV Export Utility
 */

export const exportToCSV = ({
  data = [],
  columns = [],
  fileName = "export",
}) => {
  if (!data.length) {
    alert("No data available to export.");
    return;
  }

  const headers = columns.map((column) => column.label);

  const rows = data.map((item) =>
    columns.map((column) => {
      const value = column.value(item);

      if (value === null || value === undefined) {
        return "";
      }

      return `"${String(value).replace(/"/g, '""')}"`;
    })
  );

  const csvContent = [headers, ...rows]
    .map((row) => row.join(","))
    .join("\n");

  const blob = new Blob([csvContent], {
    type: "text/csv;charset=utf-8;",
  });

  const link = document.createElement("a");

  link.href = URL.createObjectURL(blob);

  link.download = `${fileName}.csv`;

  document.body.appendChild(link);

  link.click();

  document.body.removeChild(link);
};