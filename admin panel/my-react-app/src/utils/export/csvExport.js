/**
 * Generic CSV Export Utility with CSV Formula Injection (CWE-1236) Protection
 */

export const sanitizeSpreadsheetCell = (val) => {
  if (val === null || val === undefined) return "";
  const str = String(val);
  // Escape formula characters if cell starts with =, +, -, @, tab, CR, pipe
  if (/^[=+\-@\t\r|]/.test(str)) {
    return `'${str}`;
  }
  return str;
};

export const exportToCSV = ({
  data = [],
  columns = [],
  fileName = "export",
}) => {
  if (!data.length) {
    alert("No data available to export.");
    return;
  }

  const headers = columns.map((column) => `"${String(column.label).replace(/"/g, '""')}"`);

  const rows = data.map((item) =>
    columns.map((column) => {
      const rawValue = column.value(item);
      const safeValue = sanitizeSpreadsheetCell(rawValue);
      return `"${safeValue.replace(/"/g, '""')}"`;
    })
  );

  const csvContent = [headers, ...rows]
    .map((row) => row.join(","))
    .join("\n");

  const blob = new Blob([csvContent], {
    type: "text/csv;charset=utf-8;",
  });

  const link = document.createElement("a");
  const blobUrl = URL.createObjectURL(blob);
  link.href = blobUrl;
  link.download = `${fileName}.csv`;

  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(blobUrl);
};