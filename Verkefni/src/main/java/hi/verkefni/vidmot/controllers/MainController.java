package hi.verkefni.vidmot.verkefni3.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.Trip;
import hi.verkefni.vidmot.vinnsla.TripPlan;

import hi.verkefni.vidmot.verkefni3.switcher.Switcher;
import hi.verkefni.vidmot.verkefni3.switcher.View;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

import java.util.Optional;


public class MainController {
    @FXML
    private ListView<Trip> tripListView;

    @FXML
    private Button mainDeleteButton, mainViewButton, mainUpdateButton;

    @FXML
    public void initialize() {
        tripListView.setItems(TripPlan.getInstance().getTrips());

        /* tripListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        System.out.println("Selected: " + newValue);
                    }
                }
        ); // Þessi er bara hér til að fylgjast með user changes á ferð */

        // disable mainDeleteButton when there is 0 items in our stack
        mainDeleteButton.disableProperty().bind(
                Bindings.isEmpty(tripListView.getItems())
        );

        // disable the mainDeleteButton when the user hasn't selected an item to overview
        mainDeleteButton.disableProperty().bind(
                tripListView.getSelectionModel().selectedItemProperty().isNull()
        );

        // disable mainViewButton when there is 0 items in our stack
        mainViewButton.disableProperty().bind(
                Bindings.isEmpty(tripListView.getItems())
        );

        // disable mainViewButton when the user hasn't selected an item to overview
        mainViewButton.disableProperty().bind(
                tripListView.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    /**
     * Fer til viðmót scene sem býr til nýja ferðalög
     */
    @FXML
    private void openNewTrip() {
        Switcher.switchTo(View.NEWTRIP, false, null);
    }

    /**
     * Fer til viðmót scene sem eyðir út <strong><u>SELECTED</u></strong> ferðina.
     * @param event
     */
    @FXML
    private void openDeleteTrip(ActionEvent event) {
        Trip selected = tripListView.getSelectionModel().getSelectedItem();

        if(selected != null) {
            Switcher.switchTo(View.DELETE, false, selected); // fer til delete scene og færir yfir upplýsing um ferðin sem notendi valdi.
            return;
        } else {
            System.err.println("Veldu ferð fyrst!"); // Örrygis check
            return;
        }
    }

    /**
     * Fer til viðmót scene sem skoðir <strong><u>SELECTED</u></strong> ferðina.
     */
    @FXML
    private void reviewTrip() {
        Trip selected = tripListView.getSelectionModel().getSelectedItem();

        if(selected != null) {
            Switcher.switchTo(View.VIEWTRIP, false, selected);
            return;
        } else {
            System.err.println("Veldu ferð fyrst!"); // error handler
            return;
        }
    }

    @FXML
    private void editTrip(ActionEvent event) {
        Trip selected = tripListView.getSelectionModel().getSelectedItem();
        if(selected == null) {
            System.out.println("Select a trip to edit!");
            return;
        }

        String selectedTitle = selected.getTitle();
        String selectedDestination = selected.getDestination();
        String selectedDate = selected.getDate();

        // Debugger (check if we get the selectedData)
        System.out.println("Selected title: " + selectedTitle);
        System.out.println("Selected destination: " + selectedDestination);
        System.out.println("Selected date: " + selectedDate);

        TextInputDialog dialog = new TextInputDialog();

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            Trip trip = new Trip();
            if(result.get().equals("title")) {
                // building a new dialog
                TextInputDialog title =  new TextInputDialog();
                title.setTitle("Edit - Title");
                Optional<String> titleResult = title.showAndWait();
                if(titleResult.isEmpty()) return;

                // setter (bug)
                trip.setTitle(titleResult.get());
            } else if(result.get().equals("destination")) {
                TextInputDialog destination = new TextInputDialog();
                destination.setTitle("Edit - Destination");
               Optional<String> destinationResult = destination.showAndWait();
               if(destinationResult.isEmpty()) return;

               // setter (bug)
                trip.setDestination(destinationResult.get());
            } else if (result.get().equals("date")) {
                TextInputDialog datePicker = new TextInputDialog();
                datePicker.setTitle("Edit - Date");
                Optional<String> dateResult = datePicker.showAndWait();
                if(dateResult.isEmpty()) return;

                // setter (bug)
                trip.setDate(dateResult.get());
            }
        }
    }
}
