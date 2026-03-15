package hi.verkefni.vidmot.vinnsla;

import javafx.beans.property.*; // frekar að import allt en hafa 5 sitthvort imports.

public class Trip {
    private StringProperty date;
    private StringProperty title;
    private StringProperty destination;

    public Trip() {
        // should never be initialised
    }

    public String getDate() {
        return date.get();
    }

    public String getDestination() {
        return destination.get();
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

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setDestination(String destination) {
        this.destination.set(destination);
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public StringProperty dateProperty() {
        return date;
    }

    /**
     * Býr til nýja ferðalag áætlun.
     * @param date
     * @param title
     * @param destination
     */
    public Trip(String title, String destination, String date) {
        this.date = new SimpleStringProperty(date);
        this.title = new SimpleStringProperty(title);
        this.destination = new SimpleStringProperty(destination);
    }

    @Override
    public String toString() {
        return getTitle() + " - " + getDestination() + " (" + getDate() + ")";
    }

}