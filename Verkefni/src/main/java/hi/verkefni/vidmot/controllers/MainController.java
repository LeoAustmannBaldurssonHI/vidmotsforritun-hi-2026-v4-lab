package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.Trips.Trip;
import hi.verkefni.vidmot.vinnsla.Trips.TripPlan;

import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;

import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

import java.util.Optional;

import java.io.IOException;

public class MainController {
    @FXML
    private ListView<Trip> tripListView;
    @FXML
    private Label selectedTrip;
    @FXML
    private Label userHeader;
    @FXML
    private Button accountButton;

    private Account acc;
    private String user;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                loginController login = new loginController();
                acc = login.loginDialog();

                if (acc != null) {
                    user = acc.getSignedAccount();
                    String name = user;

                    if (name.length() > 32) {
                        name = name.substring(0, 32) + "...";
                    }

                    userHeader.setText("Hello, " + name + ". Welcome to your Trip Planner");
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        });

        /* tripListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        System.out.println("Selected: " + newValue);
                    }
                }
        ); // Þessi er bara hér til að fylgjast með user changes á ferð */
    }

    /**
     * Fer til viðmót scene sem býr til nýja ferðalög
     */
    @FXML
    private void openNewTrip(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog();

        dialog.setTitle("Adding a new trip");
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
     * add later
     */
    @FXML
    private void accountChange() throws IOException {
        if (acc == null) {
            System.err.println("Critical error, no user authenticated for us to be able to log out");
            return;
        }

        acc.logOut();

        loginController login = new loginController();
        acc = login.loginDialog();

        if (acc != null) {
            user = acc.getSignedAccount();
            String name = user;

            if (name.length() > 32) {
                name = name.substring(0, 32) + "...";
            }

            userHeader.setText("Hello, " + name + ". Welcome to your Trip Planner");
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
