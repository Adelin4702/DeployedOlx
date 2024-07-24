package com.olxapplication.strategy;

import java.time.YearMonth;
import java.util.Map;

public interface FileGeneratorStrategy {
    String generateFile(Map<YearMonth, Integer> map);
}
