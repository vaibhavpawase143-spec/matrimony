import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";

export const exportToPDF = ({
  data = [],
  columns = [],
  title = "Report",
  fileName = "report",
}) => {
  const doc = new jsPDF("landscape");

  doc.setFontSize(18);
  doc.text("Gathbandhan Matrimony", 14, 15);

  doc.setFontSize(13);
  doc.text(title, 14, 24);

  doc.setFontSize(10);
  doc.text(
    `Generated: ${new Date().toLocaleString()}`,
    14,
    31
  );

  autoTable(doc, {
    startY: 38,

    head: [
      columns.map((column) => column.label),
    ],

    body: data.map((row) =>
      columns.map((column) =>
        column.value(row)
      )
    ),

    styles: {
      fontSize: 9,
    },

    headStyles: {
      fillColor: [91, 33, 182],
    },

    alternateRowStyles: {
      fillColor: [245, 245, 245],
    },

    margin: {
      left: 10,
      right: 10,
    },
  });

  doc.save(`${fileName}.pdf`);
};