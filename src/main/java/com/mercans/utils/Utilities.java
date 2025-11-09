package com.mercans.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Utilities {
    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);
    private static final Random random = new Random();

    public static String getRandomWeekdayDateForCurrentMonth() {
        LocalDate today = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        int daysInMonth = yearMonth.lengthOfMonth();
        
        LocalDate randomDate;
        int attempts = 0;
        int maxAttempts = 31;
        
        do {
            int randomDay = random.nextInt(daysInMonth) + 1;
            randomDate = LocalDate.of(today.getYear(), today.getMonth(), randomDay);
            attempts++;
            
            if (attempts >= maxAttempts) {
                if (isWeekday(today)) {
                    randomDate = today;
                    break;
                } else {
                    randomDate = today;
                    while (!isWeekday(randomDate) && attempts < maxAttempts * 2) {
                        randomDate = randomDate.plusDays(1);
                        attempts++;
                    }
                    break;
                }
            }
        } while (!isWeekday(randomDate));
        
        String dateString = randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.info("Generated random weekday date: {}", dateString);
        return dateString;
    }

    public static String getRandomWeekdayDateAfterStart(String startDateId) {
        LocalDate startDate = LocalDate.parse(startDateId, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate today = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        int daysInMonth = yearMonth.lengthOfMonth();
        
        LocalDate randomDate;
        int attempts = 0;
        int maxAttempts = 31;
        
        do {
            int minDay = startDate.getDayOfMonth() + 1;
            int maxDay = daysInMonth;
            
            if (minDay > maxDay) {
                randomDate = LocalDate.of(today.getYear(), today.getMonth(), maxDay);
                if (!isWeekday(randomDate)) {
                    randomDate = LocalDate.of(today.getYear(), today.getMonth(), maxDay);
                    while (!isWeekday(randomDate) && randomDate.isAfter(startDate)) {
                        randomDate = randomDate.minusDays(1);
                    }
                }
                break;
            }
            
            int randomDay = minDay + random.nextInt(maxDay - minDay + 1);
            randomDate = LocalDate.of(today.getYear(), today.getMonth(), randomDay);
            attempts++;
            
            if (attempts >= maxAttempts) {
                randomDate = startDate.plusDays(1);
                while (!isWeekday(randomDate) && randomDate.getMonth() == today.getMonth() && attempts < maxAttempts * 2) {
                    randomDate = randomDate.plusDays(1);
                    attempts++;
                }
                if (randomDate.getMonth() != today.getMonth()) {
                    randomDate = LocalDate.of(today.getYear(), today.getMonth(), maxDay);
                    while (!isWeekday(randomDate)) {
                        randomDate = randomDate.minusDays(1);
                    }
                }
                break;
            }
        } while (!isWeekday(randomDate) || !randomDate.isAfter(startDate));
        
        String dateString = randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.info("Generated random weekday date after {}: {}", startDateId, dateString);
        return dateString;
    }

    private static boolean isWeekday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
}

