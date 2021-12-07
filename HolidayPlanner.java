package com.visma;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

public class HolidayPlanner {
    ArrayList<String> finnishHolidaysAsString = new ArrayList<>(Arrays.asList(
                                "1.1.2021",
                                "6.1.2021",
                                "2.4.2021",
                                "5.4.2021",
                                "13.5.2021",
                                "25.6.2021",
                                "6.12.2021",
                                "24.12.2021",
                                "1.1.2022",
                                "6.1.2022",
                                "15.4.2022",
                                "18.4.2022",
                                "1.5.2022",
                                "26.5.2022",
                                "5.6.2022",
                                "24.6.2022",
                                "25.6.2022",
                                "6.12.2022",
                                "24.12.2022",
                                "25.12.2022",
                                "26.12.2022"
                                ));
    ArrayList<LocalDate> finnishHolidays = new ArrayList<>();

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d.M.yyyy");

    public HolidayPlanner() {
        finnishHolidaysAsString.forEach((date) -> {
            try {
                finnishHolidays.add(LocalDate.parse(date, dateFormat));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Integer countHolidayDays(String fromDateString, String toDateString){
        LocalDate fromDate = null;
        LocalDate toDate = null;
        String error = null;

        try {
            fromDate = LocalDate.parse(fromDateString, dateFormat);
            toDate = LocalDate.parse(toDateString, dateFormat);
        } catch (Exception e){
            System.out.println("Date format is invalid. Should be d.M.YYYY");
            return null;
        }

        error = validateDates(fromDate, toDate);
        if(error != null){
            System.out.println(error);
            return null;
        }

        //Long days = ChronoUnit.DAYS.between(fromDate, toDate) + 1;
        int days = 0;
        for(LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)){
            if(date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                days += 1;
            }
        }

        for(LocalDate date : finnishHolidays){
            if((date.isAfter(fromDate) || date.isEqual(fromDate)) && (date.isBefore(toDate) || date.isEqual(toDate))){
                days -= 1;
            }
        }

        return days;
    }

    private String validateDates(LocalDate fromDate, LocalDate toDate){
        int year = fromDate.getYear();
        String validFromDateString = "1.4." + year;
        String validToDateString = "31.3." + (year + 1);
        LocalDate validFromDate = LocalDate.parse(validFromDateString, dateFormat);
        LocalDate validToDate = LocalDate.parse(validToDateString, dateFormat);

        if(fromDate.isAfter(toDate)){
            return "First date must be earlier than the second one.";
        }

        if(ChronoUnit.DAYS.between(fromDate, toDate) > 50){
            return "Time period between the dates is too long, should be maximum of 50 days.";
        }

        if(fromDate.isBefore(validFromDate)){
            validFromDate = validFromDate.minusYears(1);
            validToDate = validToDate.minusYears(1);
        }

        if(!(fromDate.isAfter(validFromDate) && fromDate.isBefore(validToDate)) || !(toDate.isAfter(validFromDate) && toDate.isBefore(validToDate))){
            return "Time period between dates must take place within one year between 1.4. - 31.3.";
        }
        return null;
    }
}
