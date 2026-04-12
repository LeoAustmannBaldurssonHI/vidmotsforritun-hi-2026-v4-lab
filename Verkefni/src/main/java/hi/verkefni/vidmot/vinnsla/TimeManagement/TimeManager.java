package hi.verkefni.vidmot.vinnsla.TimeManagement;

// Java time import
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Java list import
import java.util.List;

public class TimeManager {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"); // formatter rule

    public TimeManager() {
        // empty constructor
    }

    /**
     * Turns a local date into a string
     * @param date
     * @return date in a string
     */
    public String formatDate(LocalDate date) {
        return date.format(formatter);
    }

    /**
     * Ensures that the date being inputted is following the formatter.
     * @param input
     * @return date
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
     * Gets the current date when the program is called upon
     * @return  date
     */
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}