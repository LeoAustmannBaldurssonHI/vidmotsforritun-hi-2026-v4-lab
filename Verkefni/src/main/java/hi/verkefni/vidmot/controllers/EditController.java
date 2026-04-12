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

// Alert import
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class EditController implements DataInterface {
    private Trip selectedTrip;
    private Account acc;
    private boolean current;
    private String currentAccount, currentTripTitle;
    private TimeManager tm = new TimeManager();
    private boolean legalSafe = false; // legalSafe is only to be used to prevent certain changes to be implemented that bypasses the creation

    @FXML
    private Label updateWork, updateFlight, updateCar, updateHotel, totalCostLabel, userHeader;

    @FXML
    private TextField title, destination, groupSize, hotelCost, flightCost, carCost;

    @FXML
    private DatePicker start, end;

    /**
     * combines two strings into a singular long string used for the header
     * @param title of the trip
     * @param account which is currently logged in
     * @return header text
     */
    private String stringCombo(String title, String account) {
        return account + ", you're currently viewing: " + title;
    }

    /**
     * A helper method that sets the text of each label in one go. This is a work around with the account system since we need to log in first before we can retrieve the data about the selected trip.
     */
    private void updateLables() {
        if(selectedTrip == null) return;

        title.setText(selectedTrip.getTitle());
        destination.setText(selectedTrip.getDestination());
        start.setValue(selectedTrip.getStartDate());
        end.setValue(selectedTrip.getEndDate());
        totalCostLabel.setText(selectedTrip.getTotalCost());
        updateWork.setText((selectedTrip.getWork() ? "Yes" : "No"));
        updateCar.setText((selectedTrip.getCar() ? "Yes" : "No"));
        updateHotel.setText((selectedTrip.getHotel() ? "Yes" : "No"));
        updateFlight.setText((selectedTrip.getFlight() ? "Yes" : "No"));
        groupSize.setText(selectedTrip.getGroupSize());
        flightCost.setText(selectedTrip.getFlightCost());
        carCost.setText(selectedTrip.getCarCost());
        hotelCost.setText(selectedTrip.getHotelCost());
    }

    @FXML
    public void initialize() {
        try {
            acc = new Account();
        } catch (IOException e) {
            e.printStackTrace();
            acc = null;
        }

        updateWork.setOnMouseClicked(e -> {
            current = selectedTrip.getWork();
            selectedTrip.setWork(!current);

            updateWork.setText(!current ? "Yes" : "No");
        });

        updateCar.setOnMouseClicked(e -> {
           current = selectedTrip.getCar();
           selectedTrip.setCar(!current);

           updateCar.setText(!current ? "Yes" : "No");
        });

        updateFlight.setOnMouseClicked(e -> {
            current = selectedTrip.getFlight();
            selectedTrip.setFlight(!current);

            updateFlight.setText(!current ? "Yes" : "No");
        });

        updateHotel.setOnMouseClicked(e -> {
            current = selectedTrip.getHotel();
            selectedTrip.setHotel(!current);

            updateHotel.setText(!current ? "Yes" : "No");
        });

        title.textProperty().addListener((obs, oldVal, newVal) -> {
            selectedTrip.setTitle(newVal);
            if(newVal.length() > 0) {
                // debugger
                System.out.println("New title: " + newVal);
            }
        });

        destination.textProperty().addListener((obs, oldVal, newVal) -> {
            selectedTrip.setDestination(newVal);
            if(newVal.length() > 0) {
                // debugger
                System.out.println("New destination: " + newVal);
            }
        });

        start.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedTrip.setStartDate(newVal);
                System.out.println(newVal);
            }

            if(newVal.isAfter(selectedTrip.getEndDate())) {
                legalSafe = false;
            } else {
                legalSafe = true;
            }
            System.out.println("Start date change is allowed: " + legalSafe);
        });

        end.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedTrip.setEndDate(newVal);
                System.out.println(newVal);
            }

            if(newVal.isBefore(selectedTrip.getStartDate())) {
                legalSafe = false;
            } else {
                legalSafe = true;
            }
            System.out.println("End date change is allowed: " + legalSafe);
        });

        groupSize.textProperty().addListener((obs, oldVal, newVal) -> {
            selectedTrip.setSize(newVal);
            if(newVal.length() > 0) {
                // debugger
                System.out.println("New group size: " + newVal);
            }
        });


        // disabledProperty()

        
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

            updateLables();
        }
    }

    @FXML
    public void directView() {
        if(!legalSafe) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Edit error");
            alert.setHeaderText("Illegal edits made");
            alert.setContentText("The changes you made are illegal, please fix them!");

            alert.showAndWait();
        } else {
            Switcher.switchTo(View.VIEWTRIP, false, selectedTrip);
        }
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