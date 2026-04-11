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

public class EditController implements DataInterface {
    @FXML
    private Label userHeader;

    private Trip selectedTrip;
    private Account acc;
    private String currentAccount, currentTripTitle;

    /**
     * combines two strings into a singular long string used for the header
     * @param title of the trip
     * @param account which is currently logged in
     * @return header text
     */
    private String stringCombo(String title, String account) {
        return account + ", you're currently editing: " + title;
    }

    @FXML
    public void initialize() {
        try {
            acc = new Account();
        } catch (IOException e) {
            e.printStackTrace();
            acc = null;
        }
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

    @FXML
    public void directView() {
        Switcher.switchTo(View.VIEWTRIP, false, selectedTrip);
    }

    @FXML
    public void directNew() {
        System.out.println("user wants to create a trip");
    }

    @FXML
    public void directDelete() throws IOException {
        System.out.println("user wants to delete this trip");

        DeleteDialog delete = new DeleteDialog(selectedTrip);
    }

    @FXML
    public void directHome() {
        Switcher.switchTo(View.MAIN, false, null);
    }
}