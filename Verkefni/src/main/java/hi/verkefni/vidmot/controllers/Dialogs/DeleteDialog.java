package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;
import hi.verkefni.vidmot.vinnsla.Trips.*;
import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;

import java.util.Optional;

import java.io.IOException;

public class DeleteDialog {
    private final String CSS = "/hi/verkefni/vidmot/CSS/style.css";
    private final int HEIGHTCUTOFF = 50;
    private final int WIDTHCUTFOFF = 300;
    private Trip selectedTrip;

    public DeleteDialog(Trip selectedTrip) throws IOException {
        boolean done = false;

        if (selectedTrip == null) {
            throw new IllegalArgumentException("selectedTrip cannot be null");
        }
        this.selectedTrip = selectedTrip;

        while (!done) {
            Dialog<ButtonType> dialog = new Dialog<>();

            dialog.setTitle("Delete trip");
            dialog.setHeaderText(selectedTrip.getTitle());
            dialog.setContentText("Are you sure you wanna do this?");

            ButtonType confirm = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().getButtonTypes().addAll(confirm, cancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == confirm) {
                TripPlan.getInstance().removeTrip(selectedTrip);
                Account.getCurrentAccount().removeTripFromAccount(selectedTrip);
                System.out.println("Trip has been deleted");
                done = true;
                dialog.close();
                Switcher.switchTo(View.MAIN, false, null);
            } else if((result.isPresent() && result.get() == cancel) || (result.isEmpty())) {
                dialog.close();
                done = true;
                return;
            }
        }
    }
}