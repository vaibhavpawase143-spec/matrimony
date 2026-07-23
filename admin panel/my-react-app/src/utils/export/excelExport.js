import * as XLSX from "xlsx";

/**
 * Generic Excel Export Utility
 */

export const exportToExcel = ({
  data = [],
  columns = [],
  fileName = "export",
}) => {
  if (!data.length) {
    alert("No data available to export.");
    return;
  }

  const excelData = data.map((item) => {
    const row = {};

    columns.forEach((column) => {
      row[column.label] = column.value(item);
    });

    return row;
  });

  const worksheet = XLSX.utils.json_to_sheet(excelData);

  const workbook = XLSX.utils.book_new();

  XLSX.utils.book_append_sheet(workbook, worksheet, "Report");

  XLSX.writeFile(workbook, `${fileName}.xlsx`);
};