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

public class SignUpController {
    @FXML
    public String signUpDialog() {
        boolean isDone = false;
        while(!isDone) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Sign up");

            TextField username = new TextField();
            PasswordField password = new PasswordField();

            VBox box = new VBox(10, username, password);
            dialog.getDialogPane().setContent(box);

            ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(confirm, cancel);

            Optional<ButtonType> result = dialog.showAndWait();
            System.out.println(result.get());

            if(result.isEmpty() || result.get() == cancel) {
                isDone = true;
                dialog.close();
                return null;
            }

            if(result.get() == confirm) {
                try {
                    Account acc = new Account();
                    String addUser = username.getText();
                    String addPassword = password.getText();
                    boolean adder = acc.newAccount(addUser, addPassword);

                    if(adder) {
                        dialog.close();
                        isDone = true;
                        return acc.getSignedAccount();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Signup failed");
                        alert.setHeaderText(null);
                        if(acc.accountExists(addUser)) {
                            alert.setContentText("Account with this name already exists");
                        } else {
                            alert.setContentText("Password is not valid by our standards.");
                        }
                        alert.showAndWait();
                    }
                } catch(Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Something went wrong during the signup.");
                    e.printStackTrace();
                    alert.showAndWait();
                }
            } else if(result.get() == cancel) {
                isDone = true; // needed or the dialog can't close
                dialog.close();
            }
        }
        return null;
    }
}