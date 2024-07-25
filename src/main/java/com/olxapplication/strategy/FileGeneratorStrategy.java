package com.olxapplication.strategy;

import java.io.File;
import java.time.YearMonth;
import java.util.Map;

public interface FileGeneratorStrategy {
    File generateFile(Map<YearMonth, Integer> map);
}
