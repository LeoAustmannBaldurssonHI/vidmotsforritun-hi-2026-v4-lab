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

public class MainController {
    @FXML
    private ListView<Trip> tripListView;

    @FXML
    private Label selectedTrip;

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

        // Í stað fyrir texta input, við ætlum að nota buttons
        Dialog<ButtonType>  dialog = new Dialog<>();

        ButtonType titleButton = new ButtonType("Title", ButtonBar.ButtonData.OK_DONE); // Þessi verður alltaf default, engan hugmynd hvernig á að slökkva á því :sob:
        ButtonType dateButton = new ButtonType("Date", ButtonBar.ButtonData.OK_DONE);
        ButtonType destinationButton = new ButtonType("Destination", ButtonBar.ButtonData.OK_DONE);
        ButtonType is_cancelButton = new ButtonType("Hætta", ButtonBar.ButtonData.CANCEL_CLOSE); // Cancel en á íslensku

        dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL); // Tökum út default takarnar
        dialog.getDialogPane().getButtonTypes().addAll(titleButton, destinationButton, dateButton, is_cancelButton); // Notum okkar frekar

        // Útlit af dialog
        dialog.setHeaderText("Þú ert að fara breyta:\n" + selected.toString());
        dialog.setTitle("Trip editor");
        dialog.setContentText("Vinsamlegast veldu einn af hnöpparnar fyrir neðan til að byrja");

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent()) {
            if(result.get() == titleButton) {
                TextInputDialog title = new TextInputDialog(selected.getTitle());
                title.setTitle("Editing: Title");
                title.setHeaderText("Sláðu inn texta til að uppfæra titilinn af þessari ferðalag.");
                Optional<String> titleResult = title.showAndWait();
                titleResult.ifPresent(value -> selected.setTitle(value));
            } else if(result.get() ==  destinationButton) {
                TextInputDialog destination = new TextInputDialog(selected.getDestination());
                destination.setTitle("Editing: Destination");
                destination.setContentText("Sláðu inn áfangastað sem þú áætlar til");
                Optional<String> destinationResult = destination.showAndWait();
                destinationResult.ifPresent(value -> selected.setDestination(value));
            } else if(result.get() ==  dateButton) {
                TextInputDialog datePicker = new TextInputDialog(selected.getDate());
                datePicker.setTitle("Editing: Date");
                datePicker.setContentText("Sláðu inn dagsetningu (DD.MM.YYYY)");
                Optional<String> dateResult = datePicker.showAndWait();
                dateResult.ifPresent(value -> selected.setDate(value));
            } else {
                System.out.println("User selected to go back, returning to main-view.fxml");
            }
            tripListView.refresh(); // endurræsir kerfið eftir að breyta, án þess þarf að ýta eitthvert til að fá nýja gögnin að byrtast.
        }
    }
}
