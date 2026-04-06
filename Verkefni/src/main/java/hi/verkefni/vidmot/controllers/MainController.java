package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.beans.binding.*;
import javafx.beans.property.*;

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
    private Button accountButton, viewButton_main;
    @FXML
    private AnchorPane mainPane;

    private Account acc;
    private String user;

    private BooleanProperty userLogged = new SimpleBooleanProperty(false);

    /**
     * Checks if the user's name goes over 32 characters,
     * if it does then we initilize an "overflow" method to prevent the header text from being too long
     * @param name
     * @return name of the user
     */
    private String nameOverflow(String name) {
        if(name.length() > 32) {
            name = name.substring(0, 32) + "...";
        }
        return name;
    }

    @FXML
    public void initialize() {
        tripListView.setItems(TripPlan.getInstance().getTrips());
        Platform.runLater(() -> {
            try {
                if(!Account.activeSession()) {
                    loginController login = new loginController();
                    acc = login.loginDialog();
                }

                if (Account.activeSession()) {
                    user = acc.getSignedAccountName();
                    acc = acc.getCurrentAccount();
                    System.out.println(acc);

                    // security check - do we currently have an active account session?
                    if(user == null) {
                        System.out.println("No account found");
                        System.exit(1);
                    }

                    String name = user;
                    userHeader.setText("Hello, " + nameOverflow(name) + ". Welcome to your Trip Planner");
                    userLogged.set(true);
                    TripPlan.getInstance().getSignedAccountTrips(acc);
                }
            } catch(IOException e) {
                e.printStackTrace();
                userLogged.set(false);
            }
        });

        tripListView.getSelectionModel().selectedItemProperty().addListener((obs, oldTrip, newTrip) -> {
            selectedTrip.setText(newTrip == null ? "You have selected: NULL" : "You have selected: " + newTrip.getTitle());
        });

        mainPane.visibleProperty().bind(userLogged); // changes if the layout should be visible or not

        /* a rule that prevents the fxml document from never opening when the visibleProperty is false.
         in short this just takes in consideration if the layout -
         (set by the fxml document) should be taken in consideration or not. */
        mainPane.managedProperty().bind(userLogged);

        // Disable properties rules
        viewButton_main.disableProperty().bind(
                Bindings.isEmpty(tripListView.getItems())
        );

        viewButton_main.disableProperty().bind(
                tripListView.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    /**
     * Fer til viðmót scene sem býr til nýja ferðalög
     */
    @FXML
    private void openNewTrip(ActionEvent event) throws IOException {
        System.out.println("new trip creation button clicked");

        if(acc == null) return; // security check

        NewController newTrip = new NewController();

        newTrip.createTrip(); // opens the dialog menu
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
    private void accountManage() throws IOException {
        if (acc == null) {
            System.err.println("Critical error, no user authenticated for us to be able to log out");
            return;
        }
        boolean done = false;
        while(!done) {
            Dialog<ButtonType> dialog = new Dialog<>();

            GridPane grid = new GridPane(); // declared early

            dialog.setTitle("Account Management");

            ButtonType delete = new ButtonType("Delete Account", ButtonBar.ButtonData.OTHER);
            ButtonType logOut = new ButtonType("Log out", ButtonBar.ButtonData.OTHER);

            dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().addAll(logOut, delete);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.get() == delete) {
                String currentAccount = acc.getSignedAccountName();

                Dialog<ButtonType> deleteDialog = new Dialog<>();

                dialog.setTitle("Account Deletion");

            } else if(result.get() == logOut) {
                acc.logOut();

                userLogged.set(false);

                loginController login = new loginController();
                acc = login.loginDialog();

                if (acc != null) {
                    user = acc.getSignedAccountName();
                    String name = user;

                    userHeader.setText("Hello, " + nameOverflow(name) + ". Welcome to your Trip Planner");

                    userLogged.set(true);
                    TripPlan.getInstance().getSignedAccountTrips(acc);
                }
            }
        }
    }

    /**
     *
     * @param event ónotað í þessari tilfelli
     */
    @FXML
    private void viewTrip(ActionEvent event) {
        Trip selected = tripListView.getSelectionModel().getSelectedItem();

        if(selected == null) {
            System.out.println("Select a trip to view!");
            return;
        }

        Switcher.switchTo(View.VIEWTRIP, false, selected);
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

        Switcher.switchTo(View.EDIT, false, selected);
    }
}