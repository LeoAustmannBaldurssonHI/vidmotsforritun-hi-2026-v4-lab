package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

import java.util.Optional;

import java.io.IOException;

public class loginController {
    @FXML
    public String loginDialog() throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Log in");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        VBox box = new VBox(10, usernameField, passwordField);
        dialog.getDialogPane().setContent(box);

        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType signUp = new ButtonType("Sign up", ButtonBar.ButtonData.OTHER);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, signUp, cancel);

        boolean isDone = false;

        while (!isDone) {
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isEmpty() || result.get() == cancel) {
                isDone = true;
                System.exit(0);
            } else if(result.get() == signUp) {
                // set up later...
            } else if(result.get() == confirm) {
                try {
                    Account acc = new Account();
                    String attemptedUser = usernameField.getText();
                    String attemptedPassword = passwordField.getText();
                    boolean signInStatus = acc.signIn(attemptedUser, attemptedPassword);

                    if(!signInStatus) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login failed");
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong username or password.");
                        alert.showAndWait();
                    } else {
                        dialog.close();
                        isDone = true;
                        return acc.getSignedAccount();
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
        return null;
    }
}