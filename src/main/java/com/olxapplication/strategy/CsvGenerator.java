package com.olxapplication.strategy;

import com.olxapplication.constants.ReportMessages;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsvGenerator implements FileGeneratorStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvGenerator.class);

    @Override
    public String generateFile(Map<YearMonth, Integer> map) {
        File file = new File("Reports/CSV_Report.csv");

        try{
            FileWriter outputFile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputFile);

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"Year-Month", "Number of posted announces"});
            for(Map.Entry<YearMonth, Integer> entry : map.entrySet()){
                data.add(new String[] {entry.getKey().toString(), entry.getValue().toString()});
            }
            writer.writeAll(data);
            writer.close();
            return ReportMessages.REPORT_GENERATED_SUCCESSFULLY;

        } catch (IOException e){
            LOGGER.error(ReportMessages.REPORT_NOT_GENERATED + e.getMessage());
            return ReportMessages.REPORT_NOT_GENERATED + e.getMessage();
        }
    }
}
