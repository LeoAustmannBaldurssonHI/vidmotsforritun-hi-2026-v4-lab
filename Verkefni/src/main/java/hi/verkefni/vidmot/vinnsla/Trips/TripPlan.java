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
        System.out.println("System override, engan notendi gat veri fundin né skoðað"); // temp
        // ekkert
    }

    public TripPlan(String user) {
        /*
        TODO: Setja inn case statements þar sem að hvert notendi er með sitt eigin ferð
         */
        
        if(user.equals("Leó Austmann Baldursson")) {
            System.out.println("Leó Austmann Baldursson skráður inn");

            /*plannedTrips.add(new Trip("Ferð til Íran", "Íran", "1.3.2026"));
            plannedTrips.add(new Trip("Ferð til Selfoss (er ekki til)", "Ísland", "3.3.2026"));
            plannedTrips.add(new Trip("Ferð til Póllands", "Póllands", "19.6.2028"));*/
        } else if (user.equals("Ebba Þóra Hvannberg")) {
            System.out.println("Ebba Þóra Hvannberg skráður inn");

            //plannedTrips.add(new Trip("Köbenhavn veisla", "Kaupmannahafn", "21.06.2025"));
        } else if(user.equals("Jón Jónsson")) {
            System.out.println("Ebba Þóra Hvannberg skráður inn");

            //plannedTrips.add(new Trip("Köbenhavn veisla", "Kaupmannahafn", "21.06.2025"));
        } else {
            System.out.println("Óþekktur notendi, engan ferðir verður lagt fram");
            return;
        }
    }

    /**
     * Bætir nýr basic ferðalag fyrir notenda.
     * @param date
     * @param title
     * @param destination
     */
    public void addNewTrip(String title, String destination, String start, String end) {
        plannedTrips.add(new Trip(title, destination, start, end));
    }


    public void addNewTrip(String title, String blank ) {

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