package com.olxapplication.strategy;

import com.olxapplication.constants.ReportMessages;

import java.time.YearMonth;
import java.util.Map;

public class StrategyFactory {

    public FileGeneratorStrategy getStrategy(String fileType,  Map<YearMonth, Integer> map) {


        if (fileType.equals("csv")) {
            return new CsvGenerator();
        }
        if (fileType.equals("txt")) {
            return new TxtGenerator();
        }
        if (fileType.equals("pdf")) {
            return new PdfGenerator();
        }
        return null;
    }
}
