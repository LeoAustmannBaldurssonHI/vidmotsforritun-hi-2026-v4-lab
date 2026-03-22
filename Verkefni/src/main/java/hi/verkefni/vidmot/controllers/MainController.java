package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.Trip;
import hi.verkefni.vidmot.vinnsla.TripPlan;

import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;

import java.util.Optional;

import java.io.IOException;

public class MainController {
    @FXML
    private ListView<Trip> tripListView;

    @FXML
    private Label selectedTrip;

    @FXML
    private Button mainDeleteButton, mainViewButton, mainUpdateButton;

    /*

    Disabled temp

    @FXML
    public void initialize() {
        tripListView.setItems(TripPlan.getInstance().getTrips());

        /* tripListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        System.out.println("Selected: " + newValue);
                    }
                }
        ); // Þessi er bara hér til að fylgjast með user changes á ferð

        selectedTrip.textProperty().bind(
                Bindings
                        .when(tripListView.getSelectionModel().selectedItemProperty().isNull())
                        .then("")
                        .otherwise(tripListView.getSelectionModel().selectedItemProperty().asString())
        );

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

        // disable the edit button when the user hasn't selected item to edit
        mainUpdateButton.disableProperty().bind(
           tripListView.getSelectionModel().selectedItemProperty().isNull()
        );
    }*/

    @FXML
    public void loginDialog() throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Log in");

        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType signUp = new ButtonType("Sign up", ButtonBar.ButtonData.OTHER);
    }

    @FXML
    public void signUpDialog() throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Sign up");

        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
    }

    /**
     * Fer til viðmót scene sem býr til nýja ferðalög
     */
    @FXML
    private void openNewTrip() {
        /*
        * TODO: Uppfæra þessari method til að fá hann að opna dialog í staðinn
        *  og að ná í gögn
        * */
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

    /**
     * Þessi (hluti af bónus verk) virkar þanning að notendi getur breytt eitthvað um þeirra ferðalag.
     * Ef notendi til dæmis sláði inn vitlausa dagsetningu getur hann einfaldlega ýtt á hnapp tengd við
     * þessari aðferð og velji "date" option við að breyta.
     * @param event ónotað í þessari tilfelli
     */
    @FXML
    private void editTrip(ActionEvent event) {
        Trip selected = tripListView.getSelectionModel().getSelectedItem();
        if(selected == null) {
            System.out.println("Select a trip to edit!");
            return;
        }

        /* Debugger

        String selectedTitle = selected.getTitle();
        String selectedDestination = " " + selected.getDestination();
        String selectedDate = " " + selected.getDate();

        System.out.println("Title: " + selectedTitle);
        System.out.println("Destination: " + selectedDestination);
        System.out.println("Date: " + selectedDate);

         */

    }
}
