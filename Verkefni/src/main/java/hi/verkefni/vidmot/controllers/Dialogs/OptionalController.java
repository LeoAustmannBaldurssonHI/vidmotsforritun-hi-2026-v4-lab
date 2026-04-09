package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

import hi.verkefni.vidmot.vinnsla.Trips.*;

import java.util.Optional;

import java.io.IOException;

public class OptionalController {
    private final int HEIGHTCUTOFF = 50;
    private final int WIDTHCUTFOFF = 300;

    private CheckBox hotelBox, flightBox, carBox, workBox;
    private TextField groupSize, hotelCost, flightCost, carCost;

    /**
     * Prints the status of each checkboxes
     */
    private void debugStatus() {
        System.out.println("Hotel: " + hotelBox.isSelected());
        System.out.println("Flight: " + flightBox.isSelected());
        System.out.println("Car: " + carBox.isSelected());
        System.out.println("Work: " + workBox.isSelected());
    }

    public Trip OptionalController() throws IOException {
        boolean done = false;
        while (!done) {
            Dialog<ButtonType> dialog = new Dialog<>();

            dialog.setTitle("Trip creation - Optionals");

            GridPane grid = new GridPane();

            hotelBox = new CheckBox();
            flightBox = new CheckBox();
            carBox = new CheckBox();
            workBox = new CheckBox();

            hotelCost = new TextField();
            flightCost = new TextField();
            carCost = new TextField();
            groupSize = new TextField();

            Label hotel = new Label();
            Label flight = new Label();
            Label car = new Label();
            Label work = new Label();
            Label cost = new Label();

            grid.add(hotel, 0, 0);
            grid.add(flight, 0, 1);
            grid.add(car, 0, 2);
            grid.add(work, 0, 3);

            grid.add(hotelBox, 1, 0);
            grid.add(flightBox, 1, 1);
            grid.add(carBox, 1, 2);
            grid.add(workBox, 2, 2);

            dialog.getDialogPane().setContent(grid);

            ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL); // Tökum út default takarnar
            dialog.getDialogPane().getButtonTypes().addAll(confirm, cancel); // Notum okkar frekar

            // debuggers
            hotelBox.selectedProperty().addListener((obs, oldVal, newVal) -> debugStatus());
            flightBox.selectedProperty().addListener((obs, oldVal, newVal) -> debugStatus());
            carBox.selectedProperty().addListener((obs, oldVal, newVal) -> debugStatus());
            workBox.selectedProperty().addListener((obs, oldVal, newVal) -> debugStatus());

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.get() == cancel) {
                done = true;
                dialog.close();
                return null;
            } else if(result.get() == confirm) {
                Trip trip = new Trip();

                trip.setHotel(hotelBox.isSelected());
                trip.setCar(carBox.isSelected());
                trip.setWork(workBox.isSelected());
                trip.setFlight(flightBox.isSelected());

                trip.setHotelCost(hotelCost.getText());
                trip.setFlightCost(flightCost.getText());
                trip.setCarCost(carCost.getText());

                trip.setSize(groupSize.getText());

                done = true;
                dialog.close();

                return trip;
            }
        }
        return null;
    }
}