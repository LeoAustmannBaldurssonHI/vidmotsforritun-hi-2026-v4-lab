package hi.verkefni.vidmot.vinnsla;

import javafx.beans.property.*; // frekar að import allt en hafa 5 sitthvort imports.
import java.time.LocalDate;

public class Trip {
    private StringProperty title, destination;
    private LocalDate startDate, endDate;
    private BooleanProperty car, flight;

    public Trip() {
        // should never be initialised
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;

    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setStartDate(String date) {
        this.startDate = LocalDate.parse(date);
    }

    public void setEndDate(String date) {
        this.endDate = LocalDate.parse(date);
    }

    public void setDestination(String destination) {
        this.destination.set(destination);
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public BooleanProperty carProperty() { return car; }

    /**
     * Býr til nýja ferðalag áætlun.
     * @param date
     * @param title
     * @param destination
     */
    public Trip(String title, String destination, String start, String end) {
        this.title = new SimpleStringProperty(title);
        this.destination = new SimpleStringProperty(destination);
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
    }

    //@Override
   // public String toString() {
        // return getTitle() + " - " + getDestination() + " (" + getDate() + ")";
    //}
}