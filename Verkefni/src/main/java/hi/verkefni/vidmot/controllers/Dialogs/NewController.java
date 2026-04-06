package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.VPos;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

import hi.verkefni.vidmot.vinnsla.Trips.Trip;

import java.util.Optional;

import java.io.IOException;

public class NewController {
    TimeManager tm = new TimeManager();
    @FXML
    public Trip createTrip() throws IOException {
        boolean done = false;
        while(!done) {
            Dialog<ButtonType> dialog = new Dialog<>();

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

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.get() == optional) {
                OptionalController addons = new OptionalController();

            } else if(result.get() == cancel) {
                done = true;
                return null;
            } else if(result.get() == confirm) {
                try {

                    return null;
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
}