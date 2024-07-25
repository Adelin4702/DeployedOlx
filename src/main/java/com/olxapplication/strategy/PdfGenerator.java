package com.olxapplication.strategy;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.olxapplication.constants.ReportMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.YearMonth;
import java.util.Map;

public class PdfGenerator implements FileGeneratorStrategy{

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGenerator.class);

    public File generateFile(Map<YearMonth, Integer> map) {
        Document document = new Document();
        try {
            File outputFile = new File("Reports/PDF_Report.pdf");
            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();

            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Monthly Announces Report", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);
            table.addCell("Year-Month");
            table.addCell("Number of posted announces");

            for (Map.Entry<YearMonth, Integer> entry : map.entrySet()) {
                table.addCell(entry.getKey().toString());
                table.addCell(entry.getValue().toString());
            }

            document.add(table);
            document.close();
            File f = new File("Reports/PDF_Report.pdf");
            return f;
        } catch (DocumentException | IOException e) {
            LOGGER.error(ReportMessages.REPORT_NOT_GENERATED + e.getMessage());
            return null;
        }
    }
}
