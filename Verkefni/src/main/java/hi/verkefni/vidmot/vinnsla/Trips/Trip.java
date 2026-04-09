package hi.verkefni.vidmot.vinnsla.Trips;

import javafx.beans.property.*; // frekar að import allt en hafa 5 sitthvort imports.
import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;

public class Trip {
    private StringProperty title, destination, groupSize, hotelCost, flightCost, carCost;
    private LocalDate startDate, endDate;
    private BooleanProperty car, flight, hotel, work;
    private JsonNode tripNodes;

    public Trip() {
        this.title = new SimpleStringProperty();
        this.destination = new SimpleStringProperty();
        this.groupSize = new SimpleStringProperty();
        this.hotelCost = new SimpleStringProperty();
        this.flightCost = new SimpleStringProperty();
        this.carCost = new SimpleStringProperty();

        this.car = new SimpleBooleanProperty(false);
        this.flight = new SimpleBooleanProperty(false);
        this.hotel = new SimpleBooleanProperty(false);
        this.work = new SimpleBooleanProperty(false);
    }

    /**
     * Constructor that contains all of the mandatory fields
     * @param title
     * @param destination
     * @param startDate
     * @param endDate
     */
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

    public BooleanProperty hotelProperty() { return hotel; }

    public BooleanProperty workProperty() { return work; }

    public StringProperty groupSizeProperty() { return groupSize; }

    public StringProperty hotelCostProperty() { return hotelCost; }

    public StringProperty flightCostProperty() { return flightCost; }

    public StringProperty carCostProperty() { return carCost;}

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

    public String getGroupSize() { return groupSize.get(); }

    public String getFlightCost() {
        return flightCost.get();
    }

    public String getCarCost() {
        return carCost.get();
    }

    public String getHotelCost() {
        return hotelCost.get();
    }

    public String getTotalCost() {
        int totalSum = parseCost(getFlightCost())
                + parseCost(getCarCost())
                + parseCost(getHotelCost());
        return totalSum + "kr";
    }

    private int parseCost(String cost) {
        if (cost == null || cost.isBlank()) {
            return 0;
        }

        cost = cost.replace("kr", "").trim();

        try {
            return Integer.parseInt(cost);
        } catch (NumberFormatException e) {
            return 0;
        }
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

    public void setHotel(boolean hotel) {this.hotel.set(hotel); }

    public void setWork(boolean work) {
        this.work.set(work);
    }

    public void setSize(String groupSize) {
        this.groupSize.set(groupSize);
    }

    public void setCarCost(String carCost) {
        this.carCost.set(carCost);
    }

    public void setHotelCost(String hotelCost) {
        this.hotelCost.set(hotelCost);
    }

    public void setFlightCost(String flightCost) {
        this.flightCost.set(flightCost);
    }

    /**
     * The string method for the trip when displaying the content.
     * @return a card for out trip in the listview.
     */
    @Override
    public String toString() {
        return getTitle() + "\t".repeat(2)+ getDestination() + "\t".repeat(2) + getStartDate() + "\t".repeat(2) + getEndDate() + "\t".repeat(2) + getTotalCost();
    }
}