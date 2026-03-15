package hi.verkefni.vidmot.verkefni3.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;

import hi.verkefni.vidmot.verkefni3.switcher.Switcher;
import hi.verkefni.vidmot.verkefni3.switcher.View;

import hi.verkefni.vidmot.vinnsla.*;

public class newController {
    @FXML
    private TextField tripTitleInput, destinationInput;
    @FXML
    private DatePicker dateSelect;

    /**
     * Fer til baka til main-view.fxml (eða annarsvegar ef breytt)
     */
    @FXML
    private void goHome() {
        Switcher.switchTo(View.MAIN, false, null);
    }

    @FXML
    private void initialize() {
        /**
         * Setjum reglur fyrir okkar notendi
         */
        destinationInput.disableProperty().bind(
                tripTitleInput.textProperty().isEmpty()
        );

        dateSelect.disableProperty().bind(
          destinationInput.textProperty().isEmpty()
        );
    }

    /**
     * Býr til nýjan ferð eftir notendi ýtir á hnappan
     */
    @FXML
    private void registerTrip() {
        String title = tripTitleInput.getText();
        String destination = destinationInput.getText();
        String date = dateSelect.getValue().toString();

        TripPlan.getInstance().addNewTrip(title, destination, date);
        System.out.println("Trip registered to the system, transferring user");

        Switcher.switchTo(View.MAIN, false, null); // fer til baka eftir að skrá ferð
    }
}