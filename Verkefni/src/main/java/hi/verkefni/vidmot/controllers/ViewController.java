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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ViewController implements DataInterface {
    private Trip selectedTrip;
    private String currentAccount, currentTripTitle;
    private Account acc;
    private TimeManager tm = new TimeManager();

    // other labels
    @FXML
    private Label userHeader, tripCountdown;

    // trip labels
    @FXML
    private Label viewTitleLabel, viewDestinationLabel, viewStartLabel, viewEndLabel, viewTotalCostLabel, viewWorkLabel, viewHotelLabel, viewFlightsLabel, viewCarLabel, viewSizeLabel, viewFlightCostLabel, viewHotelCostLabel, viewCarCostLabel;

    /**
     * combines two strings into a singular long string used for the header
     * @param title of the trip
     * @param account which is currently logged in
     * @return header text
     */
    private String stringCombo(String title, String account) {
        return account + ", you're currently viewing: " + title;
    }

    private void updateLables() {
        if(selectedTrip == null) return;

        viewTitleLabel.setText(selectedTrip.getTitle());
        viewDestinationLabel.setText(selectedTrip.getDestination());
        viewStartLabel.setText(tm.formatDate(selectedTrip.getStartDate()));
        viewEndLabel.setText(tm.formatDate(selectedTrip.getEndDate()));
        viewTotalCostLabel.setText(selectedTrip.getTotalCost());
        viewWorkLabel.setText((selectedTrip.getWork() ? "Yes" : "No"));
        viewCarLabel.setText((selectedTrip.getCar() ? "Yes" : "No"));
        viewHotelLabel.setText((selectedTrip.getHotel() ? "Yes" : "No"));
        viewFlightsLabel.setText((selectedTrip.getFlight() ? "Yes" : "No"));
        viewSizeLabel.setText(selectedTrip.getGroupSize());
        viewFlightCostLabel.setText(selectedTrip.getFlightCost());
        viewCarCostLabel.setText(selectedTrip.getCarCost());
        viewHotelCostLabel.setText(selectedTrip.getHotelCost());
    }

    private void updateTripCountdown() {
        if (selectedTrip == null || selectedTrip.getStartDate() == null) {
            tripCountdown.setText("No trip selected");
            return;
        }

        LocalDate today = tm.getCurrentDate();
        LocalDate tripDate = selectedTrip.getStartDate();

        long days = ChronoUnit.DAYS.between(today, tripDate);

        if (days > 0) {
            tripCountdown.setText(days + " days until this trip");
        } else if (days == 0) {
            tripCountdown.setText("This trip starts today");
        } else {
            tripCountdown.setText("This trip has already started");
        }
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

            updateTripCountdown();
            updateLables();
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
