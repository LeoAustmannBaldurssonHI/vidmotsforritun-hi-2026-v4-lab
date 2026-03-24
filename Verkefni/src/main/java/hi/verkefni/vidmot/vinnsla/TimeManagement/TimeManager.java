package hi.verkefni.vidmot.vinnsla.TimeManagement;

// Java lang + util import
import java.util.ArrayList;
import java.util.List;

// Java time import
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeManager {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"); // formatter rule

    public TimeManager() {
        // empty constructor
    }

    /**
     * Checks if the given input is a valid date
     * @param input of the user date
     * @return true / false
     */
    public boolean isFormatValid(String input) {
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
     * @return
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