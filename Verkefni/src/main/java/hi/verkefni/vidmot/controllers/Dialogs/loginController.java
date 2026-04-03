package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

import java.util.Optional;

import java.io.IOException;

public class loginController {
    private final int HEIGHTCUTOFF = 50;
    private final int WIDTHCUTFOFF = 300;

    @FXML
    public Account loginDialog() throws IOException {
        boolean isDone = false;
        while (!isDone) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Log in");

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

            rootGrid.setHgap(10);

            username.setPrefWidth(WIDTHCUTFOFF);
            rootGrid.setPrefHeight(HEIGHTCUTOFF);

            dialog.getDialogPane().setContent(rootGrid);

            ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            ButtonType signUp = new ButtonType("Sign up", ButtonBar.ButtonData.OTHER);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(confirm, signUp, cancel);

            Optional<ButtonType> result = dialog.showAndWait();
            System.out.println(result.get());
            if(result.isEmpty() || result.get() == cancel) {
                isDone = true;
                System.exit(0);
            } else if(result.get() == signUp) {
                SignUpController signUpControl = new SignUpController();
                String user = signUpControl.signUpDialog();
                if(user != null) {
                    Account acc = new Account();
                    isDone = true;
                    return acc;
                } else {
                    isDone = false;
                }
                continue;
            } else if(result.get() == confirm) {
                try {
                    Account acc = new Account();
                    String attemptedUser = username.getText();
                    String attemptedPassword = password.getText();
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
                        return acc;
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Something went wrong during login.");
                    e.printStackTrace();
                    alert.showAndWait();
                    System.exit(1);
                }
            }
        }
        return null;
    }
}