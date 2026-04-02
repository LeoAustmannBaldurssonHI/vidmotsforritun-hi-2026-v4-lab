package hi.verkefni.vidmot.vinnsla.Trips;

import hi.verkefni.vidmot.vinnsla.account.Account;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.IOException;


public class TripPlan {
    private Account account;
    private ObservableList<Trip> plannedTrips = FXCollections.observableArrayList();

    private static TripPlan instance;

    public static TripPlan getInstance() {
        if (instance == null) {
            try {
                instance = new TripPlan();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    /**
     * Aðferðin sem raðiðr "ferðalög" í lista.
     */
    public TripPlan() throws IOException {
        this.account = new Account();
    }

    public void getSignedAccountTrips(Account account) {
        plannedTrips.clear(); // system override
        plannedTrips.addAll(account.getAccountTrips());
    }

    /**
     * add details little later
     * @param trip
     */
    public void newTrip(Trip trip) {
        plannedTrips.add(trip);
    }

    /**
     * Eyðir út ferðalag sem notendi byður um
     * @param trip
     */
    public void removeTrip(Trip trip) {
        plannedTrips.remove(trip);
    }

    /**
     * List of trips that the user has.
     * @return Ferðalög lista (plannedTrips)
     */
    public ObservableList<Trip> getTrips() {
        return plannedTrips;
    }
}