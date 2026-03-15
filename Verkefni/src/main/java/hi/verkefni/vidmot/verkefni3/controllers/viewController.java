package hi.verkefni.vidmot.verkefni3.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import hi.verkefni.vidmot.verkefni3.switcher.Switcher;
import hi.verkefni.vidmot.verkefni3.switcher.View;
import hi.verkefni.vidmot.verkefni3.dataInterface.DataInterface;

import hi.verkefni.vidmot.vinnsla.Trip;
import hi.verkefni.vidmot.vinnsla.TripPlan;

public class viewController implements DataInterface {
    private Trip selectedTrip;

    // Labels
    @FXML
    private Label trip_viewTitle, trip_viewDestination, trip_viewDate;

    @Override
    public void setGogn(Object data) {
        if(data instanceof Trip trip) {
            this.selectedTrip = trip;

            // Fyrst ætlum við að hreinsa út texta (örrygiskref)
            trip_viewTitle.textProperty().unbind();
            trip_viewDestination.textProperty().unbind();
            trip_viewDate.textProperty().unbind();

            // Bæta út öll texta
            trip_viewTitle.textProperty().bind(trip.titleProperty());
            trip_viewDestination.textProperty().bind(trip.destinationProperty());
            trip_viewDate.textProperty().bind(trip.dateProperty());
        }
    }

    /**
     * Fer til baka til main-view.fxml (eða annarsvegar ef breytt)
     */
    @FXML
    private void goHome() {
        Switcher.switchTo(View.MAIN, false, null);
    }
}
