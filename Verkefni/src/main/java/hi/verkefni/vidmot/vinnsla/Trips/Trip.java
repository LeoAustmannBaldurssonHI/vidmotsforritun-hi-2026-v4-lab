package hi.verkefni.vidmot.vinnsla.Trips;

import javafx.beans.property.*; // frekar að import allt en hafa 5 sitthvort imports.
import java.time.LocalDate;
import javafx.scene.media.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Trip {
    private StringProperty title, destination;
    private LocalDate startDate, endDate;
    private BooleanProperty car, flight;
    private Media backgroundImg;

    public Trip() {
        // should never be initialised
    }

    public Trip(String title, String destination, LocalDate startDate, LocalDate endDate) {
        this.title = new SimpleStringProperty(title);
        this.destination = new SimpleStringProperty(destination);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Properties
    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public BooleanProperty carProperty() {
        return car;
    }

    public BooleanProperty flightPropery() {
        return flight;
    }

    // Getters
    public String getStartDate() {
        return startDate.toString();
    }

    public String getEndDate() {
        return endDate.toString();
    }

    public String getTitle() {
        return title.get();
    }

    public String getDestination() {
        return destination.get();
    }

    public boolean getCar() {
        return car.get();
    }

    public boolean getFlight() {
        return flight.get();
    }

    // Setters

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setDestination(String destination) {
        this.destination.set(destination);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setCar(boolean car) {
        this.car.set(car);
    }

    public void setFlight(boolean flight) {
        this.flight.set(flight);
    }
}