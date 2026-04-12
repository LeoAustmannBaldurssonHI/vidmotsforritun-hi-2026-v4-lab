package hi.verkefni.vidmot.controllers;

// FXMl & Scene imports
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

// Binding imports
import javafx.beans.binding.*;

// Vinnsla imports
import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;
import hi.verkefni.vidmot.vinnsla.Trips.*;

import hi.verkefni.vidmot.switcher.*;

// Date imports
import java.time.LocalDate;

// Alert & optional imports
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// IO Imports
import java.io.IOException;

public class NewController {
    private final String CSS = "/hi/verkefni/vidmot/CSS/style.css";
    // alert cut off
    private final int HEIGHTCUTOFF = 175;
    private final int WIDTHCUTFOFF = 300;

    private TimeManager tm = new TimeManager();
    private Trip options = null;

    private String savedTitle, savedDestination, savedCity, savedCountry;
    private LocalDate savedStart, savedEnd;

    public Trip createTrip() throws IOException {
        boolean done = false;

        savedTitle = "";
        savedCity = "";
        savedCountry = "";
        savedStart = null;
        savedEnd = null;

        while(!done) {
            Dialog<ButtonType> dialog = new Dialog<>();

            dialog.getDialogPane().getStylesheets().add(
                    getClass().getResource(CSS).toExternalForm()
            );

            dialog.setTitle("Trip creation");
            dialog.setHeaderText("Insert the following information.");
            dialog.setContentText("All of these fields are mandatory to fill out");

            TextField title = new TextField();
            TextField city = new TextField();
            TextField country = new TextField();
            DatePicker start = new DatePicker();
            DatePicker end = new DatePicker();

            start.setPromptText(tm.formatDate(tm.getCurrentDate().plusDays(1)));

            end.setPromptText(tm.formatDate(tm.getCurrentDate().plusDays(7)));

            Label titleLabel = new Label();
            Label cityLabel = new Label();
            Label countryLabel = new Label();
            Label startLabel = new Label();
            Label endLabel = new Label();

            titleLabel.setText("Title: ");
            cityLabel.setText("City: ");
            countryLabel.setText("Country: ");
            startLabel.setText("Start of Trip: ");
            endLabel.setText("End of Trip: ");

            title.setText(savedTitle);
            city.setText(savedCity);
            country.setText(savedCountry);
            start.setValue(savedStart);
            end.setValue(savedEnd);

            GridPane grid = new GridPane();

            grid.add(titleLabel, 0, 0);
            grid.add(cityLabel, 0, 1);
            grid.add(countryLabel, 0, 2);
            grid.add(startLabel, 0, 3);
            grid.add(endLabel, 0, 4);

            grid.add(title, 1, 0);
            grid.add(city, 1, 1);
            grid.add(country, 1, 2);
            grid.add(start, 1, 3);
            grid.add(end, 1, 4);

            dialog.getDialogPane().setContent(grid);

            ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType optional = new ButtonType("Optional", ButtonBar.ButtonData.OTHER);

            dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL); // Tökum út default takarnar
            dialog.getDialogPane().getButtonTypes().addAll(confirm, optional, cancel); // Notum okkar frekar

            Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirm);
            Button optionalButton = (Button) dialog.getDialogPane().lookupButton(optional);
            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancel);

            confirmButton.getStyleClass().add(
                    "confirmDialogButton"
            );

            optionalButton.getStyleClass().add(
                    "optionalDialogButton"
            );

            cancelButton.getStyleClass().add(
                    "cancelDialogButton"
            );

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get() == optional) {
                savedTitle = title.getText();
                savedCity = city.getText();
                savedCountry = country.getText();
                savedStart = start.getValue();
                savedEnd = end.getValue();

                OptionalController addons = new OptionalController();

                Trip temp = addons.OptionalController();

                if (temp != null) {
                    options = temp;
                }

                done = false;
                continue;
            } else if(result.isEmpty() || result.get() == cancel) {
                done = true;
                return null;
            } else if(result.get() == confirm) {
                try {
                    TimeManager tm = new TimeManager();
                    savedTitle = title.getText();
                    savedCity = city.getText();
                    savedCountry = country.getText();
                    savedStart = start.getValue();
                    savedEnd = end.getValue();

                    Trip trip = new Trip();

                    if (title.getText().isBlank()
                            || city.getText().isBlank()
                            || country.getText().isBlank()
                            || start.getValue() == null
                            || end.getValue() == null) {
                        System.out.println("All required fields must be filled in.");

                        Alert alert = new Alert(AlertType.WARNING);

                        alert.setTitle("Creation error");
                        alert.setHeaderText("Missing information");
                        alert.setContentText("You need to fill in all of the fields in this menu before being able to create the trip");

                        alert.getDialogPane().setPrefWidth(WIDTHCUTFOFF);
                        alert.getDialogPane().setPrefHeight(HEIGHTCUTOFF);

                        alert.showAndWait();

                        continue;
                    }

                    if(savedStart.isBefore(tm.getCurrentDate())) {
                        Alert alert = new Alert(AlertType.ERROR);

                        alert.setTitle("Date error");
                        alert.setHeaderText("Start date error");
                        alert.setContentText("The start of date you gave has already expired");

                        alert.getDialogPane().setPrefWidth(WIDTHCUTFOFF);
                        alert.getDialogPane().setPrefHeight(HEIGHTCUTOFF);

                        alert.showAndWait();
                        done = false;
                        continue;
                    } else if(savedEnd.isBefore(savedStart)) {
                        Alert alert = new Alert(AlertType.ERROR);

                        alert.setTitle("Date error");
                        alert.setHeaderText("End date error");
                        alert.setContentText("The end of date you gave ends before the start date");

                        alert.getDialogPane().setPrefWidth(WIDTHCUTFOFF);
                        alert.getDialogPane().setPrefHeight(HEIGHTCUTOFF);

                        alert.showAndWait();
                        done = false;
                        continue;
                    } else {
                        trip.setTitle(savedTitle);
                        trip.setDestination(savedCity + ", " + savedCountry); // putting them together as one
                        trip.setStartDate(savedStart);
                        trip.setEndDate(savedEnd);
                        trip.setSize("0");
                        trip.setCarCost("0kr");
                        trip.setHotelCost("0kr");
                        trip.setFlightCost("0kr");
                    }

                    if(options != null) {
                        trip.setCar(options.getCar());
                        trip.setHotel(options.getHotel());
                        trip.setWork(options.getWork());
                        trip.setFlight(options.getFlight());
                        trip.setSize(options.getGroupSize());

                        trip.setHotelCost(options.getHotelCost());
                        trip.setFlightCost(options.getFlightCost());
                        trip.setCarCost(options.getCarCost());
                    }

                    TripPlan.getInstance().getTrips().add(trip);
                    Account.getCurrentAccount().addTripToAccount(trip);
                    Switcher.switchTo(View.MAIN, false, null);
                    return trip;
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in creating trip");
                    System.exit(1);
                    return null;
                }
            }
        }
        return null;
    }
}