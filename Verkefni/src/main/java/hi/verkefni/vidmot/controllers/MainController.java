package hi.verkefni.vidmot.controllers;

// FXML & Scene imports
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;

// Bindings imports
import javafx.beans.binding.*;
import javafx.beans.property.*;

// Vinnsla imports
import hi.verkefni.vidmot.vinnsla.Trips.Trip;
import hi.verkefni.vidmot.vinnsla.Trips.TripPlan;
import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

// Switcher imports
import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;

// Optional imports
import java.util.Optional;

// IO imports
import java.io.IOException;

public class MainController {
    @FXML
    private ListView<Trip> tripListView;
    @FXML
    private Label selectedTrip;
    @FXML
    private Label userHeader;
    @FXML
    private Button accountButton, viewButton_main, editButton_main;
    @FXML
    private AnchorPane mainPane;

    private Account acc;
    private String user;
    private TimeManager tm = new TimeManager();

    private BooleanProperty userLogged = new SimpleBooleanProperty(false);

    /**
     * Checks if the user's name goes over 32 characters,
     * if it does then we initilize an "overflow" method to prevent the header text from being too long
     * @param name
     * @return name of the user
     */
    private String nameOverflow(String name) {
        if(name.length() > 24) {
            name = name.substring(0, 24) + "...";
        }
        return name;
    }

    /**
     * Simplifies the login process in a singular helper method, rather than having 20 copies
     * @return is the account logged in or not at the moment?
     */
    private boolean ensureLog() {
        try {
            if(!Account.activeSession()) {
                LoginController login = new LoginController();
                acc = login.loginDialog();
            } else {
                acc = Account.getCurrentAccount();
            }

            if(acc == null || !Account.activeSession()) {
                userLogged.set(false);
                return false;
            }

            user = Account.getSignedAccountName();

            if(user == null) {
                userLogged.set(false);
                return false;
            } else {
                userHeader.setText("Hello, " + nameOverflow(user) + ". Welcome to your Trip Planner");
                userLogged.set(true);
                TripPlan.getInstance().getSignedAccountTrips(acc);
                return true;
            }
        } catch(IOException e) {
            e.printStackTrace();
            userLogged.set(false);
            return false;
        }
    }

    @FXML
    public void initialize() {
        tripListView.setItems(TripPlan.getInstance().getTrips());
        Platform.runLater(() -> {
            ensureLog();
            tripListView.getFocusModel().focus(-1); // disabled the automatic selection of the top trip card
        });

        tripListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Trip trip, boolean empty) {
                super.updateItem(trip, empty);

                if(empty || trip == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                GridPane grid = new GridPane();

                Label title = new Label(trip.getTitle());
                Label destination = new Label(trip.getDestination());
                Label start = new Label(tm.formatDate(trip.getStartDate()));
                Label end = new Label(tm.formatDate(trip.getEndDate()));
                Label cost = new Label(trip.getTotalCost());

                title.setPrefWidth(280);
                destination.setPrefWidth(260);
                start.setPrefWidth(140);
                end.setPrefWidth(140);
                cost.setPrefWidth(130);

                grid.setPrefWidth(900);

                // align the text to the center
                title.setAlignment(Pos.CENTER);
                destination.setAlignment(Pos.CENTER);
                start.setAlignment(Pos.CENTER);
                end.setAlignment(Pos.CENTER);
                cost.setAlignment(Pos.CENTER);

                grid.getStyleClass().add(
                        "trip-row"
                );

                title.getStyleClass().add(
                        "custom-cell"
                );

                destination.getStyleClass().add(
                        "custom-cell"
                );

                start.getStyleClass().add(
                        "custom-cell"
                );

                end.getStyleClass().add(
                        "custom-cell"
                );

                cost.getStyleClass().add(
                        "custom-cell"
                );

                grid.add(title, 0, 0);
                grid.add(destination, 1, 0);
                grid.add(start, 2, 0);
                grid.add(end, 3, 0);
                grid.add(cost, 4, 0);

                setGraphic(grid);
            }
        });

        // allows the system to deselect the item
        mainPane.setOnMouseClicked(e -> {
            if (!tripListView.isHover()) {
                tripListView.getSelectionModel().clearSelection();
            }
        });

        // checks if we have selected a trip with our mouse
        tripListView.getSelectionModel().selectedItemProperty().addListener((obs, oldTrip, newTrip) -> {
            selectedTrip.setText(newTrip == null ? "No trip selected" : "You have selected: " + newTrip.getTitle());
        });

        mainPane.visibleProperty().bind(userLogged); // changes if the layout should be visible or not

        /* a rule that prevents the fxml document from never opening when the visibleProperty is false.
         in short this just takes in consideration if the layout -
         (set by the fxml document) should be taken in consideration or not. */
        mainPane.managedProperty().bind(userLogged);

        // Disable properties rules - view button
        viewButton_main.disableProperty().bind(
                Bindings.isEmpty(tripListView.getItems())
                        .or(tripListView.getSelectionModel().selectedItemProperty().isNull())
        );

        // Disable properties rules - edit button
        editButton_main.disableProperty().bind(
                Bindings.isEmpty(tripListView.getItems())
                        .or(tripListView.getSelectionModel().selectedItemProperty().isNull())
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

    @FXML
    private void newAccountManage() throws IOException {
        AccountDialog accountManage = new AccountDialog();

        accountManage.accountDialog();

        if(!Account.activeSession()) userLogged.set(false);

        ensureLog();
    }

    /**
     * Switches the current document to view-trip.fxml
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