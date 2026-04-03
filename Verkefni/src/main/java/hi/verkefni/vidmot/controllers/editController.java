package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.beans.binding.*;

import java.util.Optional;

import java.io.IOException;

import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;
import hi.verkefni.vidmot.dataInterface.DataInterface;

import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;
import hi.verkefni.vidmot.vinnsla.Trips.Trip;
import hi.verkefni.vidmot.vinnsla.Trips.TripPlan;

public class editController implements DataInterface {
    // some labels here...

    private Trip selectedTrip;

    @Override
    public void setGogn(Object data) {
        if(data instanceof Trip trip) {
            this.selectedTrip = trip;

            // Fyrst ætlum við að hreinsa út texta (örrygiskref)
            //trip_viewTitle.textProperty().unbind();

            // Bæta út öll texta
            //trip_viewTitle.textProperty().bind(trip.titleProperty());
        }
    }

    @FXML
    public void goEdit() {
        System.out.println("user wants to edit, but i am too dumb to let him"); // temp
    }

    @FXML
    public void directHome() {
        Switcher.switchTo(View.MAIN, false, null);
    }
}