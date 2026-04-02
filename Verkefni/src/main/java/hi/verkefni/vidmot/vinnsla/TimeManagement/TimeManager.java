package hi.verkefni.vidmot.vinnsla.TimeManagement;

// Java time import
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.List;

public class TimeManager {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"); // formatter rule

    public TimeManager() {
        // empty constructor
    }

    /**
     *
     * @param date
     * @return
     */
    public String formatDate(LocalDate date) {
        return date.format(formatter);
    }

    /**
     *
     * @param input
     * @return
     */
    public LocalDate parseDate(String input) {
        return LocalDate.parse(input, formatter);
    }

    /**
     * Checks if the given input is a valid date
     * @param input of the user date
     * @return true / false
     */
    public boolean formatValidator(String input) {
        try {
            LocalDate userDate = LocalDate.parse(input, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns a sorted list of dates. If the dates are out of the current time, they wlll not be accepted
     * @param dates to sort
     * @return sorted list of dates
     */
    public List<LocalDate> getSortedDates(List<LocalDate> dates) {
        return dates.stream().filter(date -> !date.isBefore(LocalDate.now())).sorted().toList();
    }

    /**
     * Gets the current date when the program is called upon
     * @return  date
     */
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}