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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class OptionalController {
    private final String CSS = "/hi/verkefni/vidmot/CSS/style.css";
    private final int HEIGHTCUTOFF = 50;
    private final int WIDTHCUTFOFF = 300;

    private CheckBox hotelBox, flightBox, carBox, workBox;
    private TextField groupSize, hotelCost, flightCost, carCost;

    private boolean savedHotel, savedFlight, savedCar, savedWork;
    private String savedHotelCost, savedFlightCost, savedCarCost, savedGroupSize;

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

        savedHotel = false;
        savedFlight = false;
        savedCar = false;
        savedWork = false;

        savedGroupSize = "";
        savedHotelCost = "";
        savedFlightCost = "";
        savedCarCost = "";

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
            Label hotelCostLabel = new Label();
            Label flightCostLabel = new Label();
            Label carCostLabel = new Label();
            Label groupLabel = new Label();

            grid.add(hotel, 0, 0);
            grid.add(flight, 1, 0);
            grid.add(car, 2, 0);
            grid.add(work, 0, 3);

            grid.add(hotelBox, 0, 1);
            grid.add(flightBox, 1, 1);
            grid.add(carBox, 2, 1);
            grid.add(workBox, 0, 4);

            grid.add(groupLabel, 0, 5);
            grid.add(groupSize, 1, 5);
            GridPane.setColumnSpan(groupSize, 2);

            grid.add(hotelCostLabel, 0, 6);
            grid.add(flightCostLabel, 1, 6);
            grid.add(carCostLabel, 2, 6);
            grid.add(hotelCost, 0, 7);
            grid.add(flightCost, 1, 7);
            grid.add(carCost, 2, 7);

            hotel.setText("Hotel booked?");
            flight.setText("Flights booked?");
            car.setText("Rental car booked?");
            work.setText("Work trip?");
            groupLabel.setText("How many people will be in your trip?");

            groupSize.setPrefWidth(50);

            hotelCost.disableProperty().bind(hotelBox.selectedProperty().not());
            flightCost.disableProperty().bind(flightBox.selectedProperty().not());
            carCost.disableProperty().bind(carBox.selectedProperty().not());

            dialog.getDialogPane().setContent(grid);

            // setters

            hotelBox.setSelected(savedHotel);
            flightBox.setSelected(savedFlight);
            carBox.setSelected(savedCar);
            workBox.setSelected(savedWork);

            carCost.setText(savedCarCost);
            hotelCost.setText(savedHotelCost);
            groupSize.setText(savedGroupSize);
            carCost.setText(savedCarCost);

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
                String verify = groupSize.getText();

                if(verify.matches(".*[a-zA-Z].*")) {
                    System.out.println("No characters in the group size field!");

                    savedWork = workBox.isSelected();
                    savedCar = carBox.isSelected();
                    savedFlight = flightBox.isSelected();
                    savedHotel = hotelBox.isSelected();

                    savedCarCost = carCost.getText();
                    savedFlightCost = flightCost.getText();
                    savedHotelCost = hotelCost.getText();

                    Alert alert = new Alert(AlertType.ERROR);

                    alert.setTitle("Group size error");
                    alert.setHeaderText("Character detected");
                    alert.setContentText("You're not supposed to put a character in the groupsize box, fix it please!");

                    alert.getDialogPane().setPrefWidth(WIDTHCUTFOFF);
                    alert.getDialogPane().setPrefHeight(HEIGHTCUTOFF);

                    alert.showAndWait();

                    continue;
                }

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