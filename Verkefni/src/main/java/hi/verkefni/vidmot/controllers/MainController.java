package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.Trip;
import hi.verkefni.vidmot.vinnsla.TripPlan;

import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;

import hi.verkefni.vidmot.vinnsla.account.Account;

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

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        VBox box = new Vbox(10, usernameField, passwordField);
        dialog.getDialogPane().setContent(box);

        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType signUp = new ButtonType("Sign up", ButtonBar.ButtonData.OTHER);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL);

        dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, signUp, cancel);

        boolean isDone = false;

        while (!isDone) {
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.get().isEmpty() || result.get() == cancel) {
                done = true;
            } else if(result.get() == signUp) {
                signUpDialog(); // transfer to the sign up dialog
            } else if(result.get() == confirm) {
                try {
                    Account acc = new Account();
                    String attemptedUser = userNameField.getText();
                    String attemptedPassword = passwordField.getText();
                    boolean signInStatus = acc.signIn(attemptedUser, attemptedPassword);

                    if(!signInStatus) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login failed");
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong username or password.");
                        alert.showAndWait();
                    } else {
                        userHeader.setText("Hello, " + acc.getSignedAcccount());
                        done = true;
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Something went wrong during login.");
                    e.printStackTrace();
                    alert.showAndWait();
                }
            }
        }
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
    private void openNewTrip(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog();

        currentSignedUser = null;

        dialog.setTitle("Adding a new trip")
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
