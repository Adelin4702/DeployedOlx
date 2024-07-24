package com.olxapplication.strategy;

import com.olxapplication.constants.ReportMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.YearMonth;
import java.util.Map;

public class TxtGenerator implements FileGeneratorStrategy{
    private static final Logger LOGGER = LoggerFactory.getLogger(TxtGenerator.class);

    public String generateFile(Map<YearMonth, Integer> map) {

        File file = new File("Reports/TXT_Report.txt");

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file));

            writer.println("Year-Month\tNumber of posted announces");

            for (Map.Entry<YearMonth, Integer> entry : map.entrySet()) {
                writer.println(entry.getKey().toString() + "\t\t" + entry.getValue().toString());
            }

            writer.close();
            return ReportMessages.REPORT_GENERATED_SUCCESSFULLY;

        } catch (IOException e) {
            LOGGER.error(ReportMessages.REPORT_NOT_GENERATED + e.getMessage());
            return ReportMessages.REPORT_NOT_GENERATED + e.getMessage();
        }
    }
}
