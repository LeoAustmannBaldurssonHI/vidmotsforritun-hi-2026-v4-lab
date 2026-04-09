package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.beans.binding.*;

import java.util.Optional;

import java.io.IOException;

import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;
import hi.verkefni.vidmot.dataInterface.DataInterface;

import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;
import hi.verkefni.vidmot.vinnsla.Trips.Trip;
import hi.verkefni.vidmot.vinnsla.Trips.TripPlan;

public class ViewController implements DataInterface {
    private Trip selectedTrip;
    private String currentAccount, currentTripTitle;
    private Account acc;

    @FXML
    private Label userHeader, tripCountdown;

    /**
     * combines two strings into a singular long string used for the header
     * @param title of the trip
     * @param account which is currently logged in
     * @return header text
     */
    private String stringCombo(String title, String account) {
        return account + ", you're currently viewing: " + title;
    }

    @FXML
    public void initialize() {
        try {
            acc = new Account();
        } catch (IOException e) {
            e.printStackTrace();
            acc = null;
        }

        /*tripCountdown.textProperty().bind(
                Bindings.when()
                        .then()
                        .otherwise()
        );*/
    }

    @Override
    public void setGogn(Object data) {
        if(data instanceof Trip trip) {
            this.selectedTrip = trip;

            // missing info
            if (acc != null) {
                currentAccount = acc.getSignedAccountName();
            } else {
                currentAccount = "Unknown user";
            }
            currentTripTitle = trip.getTitle();

            userHeader.setText(stringCombo(currentTripTitle, currentAccount));
        }
    }

    public void deleteTrip() {
        if(acc != null) {
            // idk?
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
