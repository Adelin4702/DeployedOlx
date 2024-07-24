package com.olxapplication.service;

import com.olxapplication.constants.ReportMessages;
import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.User;
import com.olxapplication.repository.AnnouncementRepository;
import com.olxapplication.repository.UserRepository;
import com.olxapplication.strategy.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;

/**
 * Service class for generating reports in the OLX application.
 */
@Service
@AllArgsConstructor
public class ReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all announcements from the repository and sorts them by date.
     * @return A list of time-sorted announcements.
     */
    public List<Announcement> getTimeSortedAnnounces(){
        List<Announcement> announcements = announcementRepository.findAll();
        announcements.sort(Comparator.comparing(Announcement::getDate));

        return announcements;
    }

    /**
     * Retrieves the admin user from the repository.
     * @return The admin user.
     */
    public User getAdmin(){
        User admin = userRepository.findByRole("admin");

        return admin;
    }

    /**
     * Collects data for reporting based on the list of announcements.
     * The data is collected by year and month with the count of announcements.
     * @param announcements The list of announcements to process.
     * @return A map with YearMonth as keys and the count of announcements as values.
     */
    public Map<YearMonth, Integer> getData(List<Announcement> announcements){
        Map<YearMonth, Integer> map = new HashMap<>();
        for (Announcement announcement : announcements) {
            YearMonth yearMonth = YearMonth.of(announcement.getDate().getYear(), announcement.getDate().getMonth());
            if(map.containsKey(yearMonth)){
                map.put(yearMonth, map.get(yearMonth) + 1);
            } else {
                map.put(yearMonth, 1);
            }
        }
        return map;
    }

    /**
     * Generates a report in the specified file format.
     * @param fileType The type of file to generate ("csv", "txt", or "pdf").
     * @return A success message if the report is generated, otherwise an error message.
     */
    public String generateReport(String fileType) {
        Map<YearMonth, Integer> map = getData(getTimeSortedAnnounces());

        StrategyFactory strategyFactory = new StrategyFactory();
        FileGeneratorStrategy fileGeneratorStrategy = strategyFactory.getStrategy(fileType, map);
        fileGeneratorStrategy.generateFile(map);
        return ReportMessages.REPORT_GENERATED_SUCCESSFULLY;
    }
}


