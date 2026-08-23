import * as XLSX from "xlsx";
import { sanitizeSpreadsheetCell } from "./csvExport";

/**
 * Generic Excel Export Utility with Formula Injection Protection
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
      const rawVal = column.value(item);
      row[column.label] =
        typeof rawVal === "number" || typeof rawVal === "boolean"
          ? rawVal
          : sanitizeSpreadsheetCell(rawVal);
    });

    return row;
  });

  const worksheet = XLSX.utils.json_to_sheet(excelData);
  const workbook = XLSX.utils.book_new();

  XLSX.utils.book_append_sheet(workbook, worksheet, "Report");

  XLSX.writeFile(workbook, `${fileName}.xlsx`);
};