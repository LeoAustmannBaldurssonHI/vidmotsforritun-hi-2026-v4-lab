package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;
import hi.verkefni.vidmot.dataInterface.DataInterface;

import hi.verkefni.vidmot.vinnsla.*;

public class deleteController implements DataInterface {
    private Trip selectedTrip;
    @FXML
    private Label tripDeleteName;

    @Override
    public void setGogn(Object data) {
        if(data instanceof Trip trip) {
            this.selectedTrip = trip;

            tripDeleteName.textProperty().unbind();

            tripDeleteName.textProperty().bind(trip.titleProperty());
        }
    }

    @FXML
    private void deleteTrip() {
        if(selectedTrip != null) {
            TripPlan.getInstance().removeTrip(selectedTrip);

            // Debug
            System.out.println("Trip has been deleted, returning to main menu");

            // Switch
            Switcher.switchTo(View.MAIN, false, null);
        } else {
            // Error message
            System.err.println("Critical Error: I got no fucking idea how you got here without an item to delete :sob:");

            // Switch
            Switcher.switchTo(View.MAIN, false, null);
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