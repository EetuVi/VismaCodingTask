package com.visma;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

public class HolidayPlanner {
    // Luodaan lista suomen vapaapäivistä ja alustetaan lista LocalDate-tyypin objekteille sekä
    // formatter päivämäärien tyypin muuttamiseksi
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

    // Muutetaan String-tyypin päivämäärät LocalDate-muotoon heti constructorissa ja lisätään ne listaan
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

        // Muutetaan annetut päivämäärät LocalDate-objekteiksi, jos tapahtuu virheitä tulostetaan virhesanoma konsoliin
        // ja palautetaan null
        try {
            fromDate = LocalDate.parse(fromDateString, dateFormat);
            toDate = LocalDate.parse(toDateString, dateFormat);
        } catch (Exception e){
            System.out.println("Date format is invalid. Should be d.M.YYYY");
            return null;
        }

        // Validoidaan päivämäärät ja niiden aikaväli tehtävänannon mukaisin ehdoin.
        // Jos virheitä havaitaan, tulostetaan virhesanoma konsoliin ja palautetaan null.
        error = validateDates(fromDate, toDate);
        if(error != null){
            System.out.println(error);
            return null;
        }

        // Alustetaan tarvittavat lomapäivät nollaan ja lasketaan tarvittavien lomapäivien määrä aikavälille
        // pois lukien sunnuntait
        int days = 0;
        for(LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)){
            if(date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                days += 1;
            }
        }

        // Käydään läpi kansalliset vapaapäivät ja vähennetään tarvittavia lomapäiviä, jos vapaapäivä osuu halutulle
        // loman aikavälille.
        for(LocalDate date : finnishHolidays){
            if((date.isAfter(fromDate) || date.isEqual(fromDate)) && (date.isBefore(toDate) || date.isEqual(toDate))){
                days -= 1;
            }
        }

        return days;
    }

    private String validateDates(LocalDate fromDate, LocalDate toDate){
        // Alustetaan lomavuoden validit alku- ja loppupäivämäärät
        int year = fromDate.getYear();
        String validFromDateString = "1.4." + year;
        String validToDateString = "31.3." + (year + 1);
        LocalDate validFromDate = LocalDate.parse(validFromDateString, dateFormat);
        LocalDate validToDate = LocalDate.parse(validToDateString, dateFormat);

        // Tarkistetaan, että loman alkupäivämäärä on ennen loppupäivämäärää
        if(fromDate.isAfter(toDate)){
            return "First date must be earlier than the second one.";
        }

        // Tarkistetaan, että loma-ajan pituus ei ylita 50 päivää.
        if(ChronoUnit.DAYS.between(fromDate, toDate) > 50){
            return "Time period between the dates is too long, should be maximum of 50 days.";
        }

        // Tarkistetaan, onko validi lomavuosi oikea. Loman alkupäivän sijoittuessa vuoden alkuun, säädetään lomavuotta
        // vuodella taaksepäin.
        if(fromDate.isBefore(validFromDate)){
            validFromDate = validFromDate.minusYears(1);
            validToDate = validToDate.minusYears(1);
        }
        // Tarkistetaan, että halutun loman alku- ja loppupäivämäärät osuvat saman lomavuoden sisälle.
        if(!(fromDate.isAfter(validFromDate) && fromDate.isBefore(validToDate))
           || !(toDate.isAfter(validFromDate) && toDate.isBefore(validToDate))){
            return "Time period between dates must take place within one year between 1.4. - 31.3.";
        }

        // Palautetaan null, jos virheitä ei havaita.
        return null;
    }
}
