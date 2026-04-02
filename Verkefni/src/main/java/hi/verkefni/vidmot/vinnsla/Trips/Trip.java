package hi.verkefni.vidmot.vinnsla.Trips;

import javafx.beans.property.*; // frekar að import allt en hafa 5 sitthvort imports.
import java.time.LocalDate;
import javafx.scene.media.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Trip {
    private StringProperty title, destination, groupSize, cost;
    private LocalDate startDate, endDate;
    private BooleanProperty car, flight, hotel, work;
    private Media backgroundImg;

    public Trip() {
        this.title = new SimpleStringProperty();
        this.destination = new SimpleStringProperty();
        this.groupSize = new SimpleStringProperty();
        this.cost = new SimpleStringProperty();

        this.car = new SimpleBooleanProperty(false);
        this.flight = new SimpleBooleanProperty(false);
        this.hotel = new SimpleBooleanProperty(false);
        this.work = new SimpleBooleanProperty(false);
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

    public BooleanProperty flightProperty() {
        return flight;
    }

    public BooleanProperty hotelProperty() {return hotel; }

    public BooleanProperty workProperty() {return work; }

    public StringProperty groupSizeProperty() {return groupSize; }

    public StringProperty costProperty() { return cost; }

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

    public boolean getHotel() { return hotel.get(); }

    public boolean getWork() { return work.get(); }

    public String getGroupSize() {return groupSize.get(); }

    public String getCost() { return cost.get(); }

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

    public void setHotel(boolean hotel) {this.hotel.set(hotel); }

    public void setWork(boolean work) {
        this.work.set(work);
    }

    public void setSize(String groupSize) {
        this.groupSize.set(groupSize);
    }

    public void setCost(String cost) {
        this.cost.set(cost);
    }

    @Override
    public String toString() {
        return getTitle() + "\t".repeat(2)+ getDestination() + "\t".repeat(2) + getStartDate() + "\t".repeat(2) + getEndDate() + "\t".repeat(2) + getCost();
    }
}