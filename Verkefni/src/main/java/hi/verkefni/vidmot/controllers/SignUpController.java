package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.VPos;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

import java.util.Optional;

import java.io.IOException;

public class SignUpController {
    private final int HEIGHTCUTOFF = 50;
    private final int WIDTHCUTFOFF = 300;

    @FXML
    public String signUpDialog() {
        boolean isDone = false;
        while(!isDone) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Sign up");

            TextField username = new TextField();
            PasswordField password = new PasswordField();
            Label usernameLabel = new Label();
            Label passwordLabel = new Label();

            username.setPromptText("Username");
            usernameLabel.setText("test");
            password.setPromptText("Password");
            passwordLabel.setText("test2");

            GridPane rootGrid = new GridPane();

            rootGrid.add(usernameLabel, 0, 0);
            rootGrid.add(username, 1, 0);

            rootGrid.add(passwordLabel, 0, 1);
            rootGrid.add(password, 1, 1);

            username.setPrefWidth(WIDTHCUTFOFF);
            rootGrid.setPrefHeight(HEIGHTCUTOFF);

            rootGrid.setHgap(10);

            dialog.getDialogPane().setContent(rootGrid);

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