package hi.verkefni.vidmot.vinnsla;

// Project imports

// Java lang + util import
import java.lang.Comparable;

// Java time import
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class TimeManager {
    private long currentTime; // called upon at each function.
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");

    /**
     * Constructs a public constructor that provides the current time (GMT+0)
     */
    public TimeManager() {
        currentTime = System.currentTimeMillis();
    }

    /**
     * Checks how long is still the time.
     * @return
     */
    public LocalDate isTimeSoon() {
        return currentTime.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        String formattedDate = currentTime.format(formatter);
        return "Current time: " + formattedDate;
    }
}