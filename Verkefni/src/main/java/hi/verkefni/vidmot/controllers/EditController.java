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
    private boolean legalSafe = true; // legalSafe is only to be used to prevent certain changes to be implemented that bypasses the creation

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
        return account + ", you're currently editing: " + title;
    }

    private String formatCost(String cost) {
        if(cost == null || cost.isBlank()) {
            return "0kr";
        }

        cost = cost.trim().replace("kr", "").trim();

        if (!cost.matches("\\d+")) {
            return "0kr";
        }

        return cost + "kr";
    }

    private void saveInfo() {
        Account activeAccount = Account.getCurrentAccount();
        if (selectedTrip == null || activeAccount == null) return;

        try {
            Trip oldTrip = new Trip();
            oldTrip.setTitle(selectedTrip.getTitle());
            oldTrip.setDestination(selectedTrip.getDestination());
            oldTrip.setStartDate(selectedTrip.getStartDate());
            oldTrip.setEndDate(selectedTrip.getEndDate());
            oldTrip.setHotel(selectedTrip.getHotel());
            oldTrip.setFlight(selectedTrip.getFlight());
            oldTrip.setCar(selectedTrip.getCar());
            oldTrip.setWork(selectedTrip.getWork());
            oldTrip.setSize(selectedTrip.getGroupSize());
            oldTrip.setHotelCost(selectedTrip.getHotelCost());
            oldTrip.setFlightCost(selectedTrip.getFlightCost());
            oldTrip.setCarCost(selectedTrip.getCarCost());

            // update selectedTrip with new UI values
            selectedTrip.setTitle(title.getText());
            selectedTrip.setDestination(destination.getText());
            selectedTrip.setStartDate(start.getValue());
            selectedTrip.setEndDate(end.getValue());
            selectedTrip.setHotel(updateHotel.getText().equals("Yes"));
            selectedTrip.setFlight(updateFlight.getText().equals("Yes"));
            selectedTrip.setCar(updateCar.getText().equals("Yes"));
            selectedTrip.setWork(updateWork.getText().equals("Yes"));
            selectedTrip.setSize(groupSize.getText().isBlank() ? "0" : groupSize.getText());
            selectedTrip.setHotelCost(hotelCost.getText().endsWith("kr") ? hotelCost.getText() : formatCost(hotelCost.getText()));
            selectedTrip.setFlightCost(flightCost.getText().endsWith("kr") ? flightCost.getText() : formatCost(flightCost.getText()));
            selectedTrip.setCarCost(carCost.getText().endsWith("kr") ? carCost.getText() : formatCost(carCost.getText()));

            // persist change to file
            activeAccount.removeTripFromAccount(oldTrip);
            activeAccount.addTripToAccount(selectedTrip);

            System.out.println("All info saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            acc = Account.getCurrentAccount();
            if (acc == null) {
                acc = new Account();
            }
        } catch (IOException e) {
            e.printStackTrace();
            acc = null;
        }

        updateWork.setOnMouseClicked(e -> {
            boolean isYes = updateWork.getText().equals("Yes");
            updateWork.setText(isYes ? "No" : "Yes");
        });

        updateCar.setOnMouseClicked(e -> {
            boolean isYes = updateCar.getText().equals("Yes");
            updateCar.setText(isYes ? "No" : "Yes");

            if (isYes) {
                carCost.setText("0kr");
            }
        });

        updateFlight.setOnMouseClicked(e -> {
            boolean isYes = updateFlight.getText().equals("Yes");
            updateFlight.setText(isYes ? "No" : "Yes");

            if (isYes) {
                flightCost.setText("0kr");
            }
        });

        updateHotel.setOnMouseClicked(e -> {
            boolean isYes = updateHotel.getText().equals("Yes");
            updateHotel.setText(isYes ? "No" : "Yes");

            if (isYes) {
                hotelCost.setText("0kr");
            }
        });

        title.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) {
                System.out.println("New title: " + newVal);
            }
        });

        destination.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) {
                System.out.println("New destination: " + newVal);
            }
        });

        groupSize.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) {
                System.out.println("New group size: " + newVal);
            }
        });

        start.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || end.getValue() == null) {
                legalSafe = true;
                return;
            }

            legalSafe = !newVal.isAfter(end.getValue());
            System.out.println("Start date change is allowed: " + legalSafe);
        });

        end.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || start.getValue() == null) {
                legalSafe = true;
                return;
            }

            legalSafe = !newVal.isBefore(start.getValue());
            System.out.println("End date change is allowed: " + legalSafe);
        });

        hotelCost.disableProperty().bind(
                updateHotel.textProperty().isEqualTo("No")
        );

        carCost.disableProperty().bind(
                updateCar.textProperty().isEqualTo("No")
        );

        flightCost.disableProperty().bind(
                updateFlight.textProperty().isEqualTo("No")
        );
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

            totalCostLabel.textProperty().bind(
                    Bindings.createStringBinding(
                            () -> selectedTrip.getTotalCost(),
                            hotelCost.textProperty(),
                            flightCost.textProperty(),
                            carCost.textProperty()
                    )
            );
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
            saveInfo();
            Switcher.switchTo(View.VIEWTRIP, false, selectedTrip);
        }
    }

    @FXML
    public void directNew() throws IOException {
        System.out.println("user wants to create a trip");
        if(!legalSafe) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Edit error");
            alert.setHeaderText("Illegal edits made");
            alert.setContentText("The changes you made are illegal, please fix them!");

            alert.showAndWait();
        } else {
            if(acc == null) {

            } else {
                saveInfo();
                NewController create = new NewController();
                create.createTrip();
            }
        }
    }

    @FXML
    public void directDelete() throws IOException {
        System.out.println("user wants to delete this trip");

        DeleteDialog delete = new DeleteDialog(selectedTrip);
    }

    @FXML
    public void directHome() {
        if(acc == null) {
            System.err.println("CRITICAL SYSTEM ERROR: No account authenticated, system killing");
            System.exit(1);
        }
        if(!legalSafe) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Edit error");
            alert.setHeaderText("Illegal edits made");
            alert.setContentText("The changes you made are illegal, please fix them!");

            alert.showAndWait();
        } else {
            saveInfo();
            Switcher.switchTo(View.MAIN, false, null);
        }
    }
}