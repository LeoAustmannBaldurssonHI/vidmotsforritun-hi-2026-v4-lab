package hi.verkefni.vidmot.vinnsla;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import javafx.beans.property.*;

public class TripPlan {
    private ObservableList<Trip> plannedTrips = FXCollections.observableArrayList();

    private static final TripPlan instance = new TripPlan();

    public static TripPlan getInstance() {
        return instance;
    }

    /**
     * Aðferðin sem raðiðr "ferðalög" í lista.
     */
    public TripPlan() {
        plannedTrips.add(new Trip("Ferð til Íran", "Íran", "1.3.2026"));
        plannedTrips.add(new Trip("Ferð til Selfoss (er ekki til)", "Ísland", "3.3.2026"));
        plannedTrips.add(new Trip("Ferð til Póllands", "Póllands", "19.6.2028"));
    }

    /**
     * Bætir nýr ferðalag fyrir notenda.
     * @param date
     * @param title
     * @param destination
     */
    public void addNewTrip(String date, String title, String destination) {
        plannedTrips.add(new Trip(date, title, destination));
    }

    /**
     * Eyðir út ferðalag sem notendi byður um
     * @param trip
     */
    public void removeTrip(Trip trip) {
        plannedTrips.remove(trip);
    }

    /**
     * Lista af ferðalögum sem notendi er með
     * @return Ferðalög lista (plannedTrips)
     */
    public ObservableList<Trip> getTrips() {
        return plannedTrips;
    }
}