package hi.verkefni.vidmot.controllers;

// FXML & Scene imports
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;

// Binding imports
import javafx.beans.binding.*;
import java.util.Optional;

// Vinnsla imports
import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

// Switcher import
import hi.verkefni.vidmot.switcher.*;

// IO Imports
import java.io.IOException;

public class SignUpController {
    private final String CSS = "/hi/verkefni/vidmot/CSS/style.css";
    private final int HEIGHTCUTOFF = 50;
    private final int WIDTHCUTFOFF = 300;

    private String savedUsername;
    private String savedPassword;

    @FXML
    public String signUpDialog() {
        boolean isDone = false;
        while(!isDone) {
            Dialog<ButtonType> dialog = new Dialog<>();

            dialog.getDialogPane().getStylesheets().add(
                    getClass().getResource(CSS).toExternalForm()
            );

            dialog.setTitle("Sign up");

            TextField username = new TextField();
            PasswordField password = new PasswordField();
            Label usernameLabel = new Label();
            Label passwordLabel = new Label();

            username.setPromptText("Username");
            usernameLabel.setText("Insert username");
            password.setPromptText("Password");
            passwordLabel.setText("Insert password");

            Label instructions = new Label(
                    "Username cannot be blank or contain 'admin'.\n" + "Password must contain:\n" +
                            "- an uppercase letter\n" +
                            "- a lowercase letter\n" +
                            "- a number\n" +
                            "- a special character"
            );

            Label instructionsHeader = new Label("Account Sign up procedure:");
            instructionsHeader.setAlignment(Pos.CENTER);

            if(savedPassword != null) password.setText(savedPassword);
            if(savedUsername != null) username.setText(savedUsername);

            GridPane rootGrid = new GridPane();

            rootGrid.add(instructions, 0, 1, 2, 1);
            rootGrid.add(instructionsHeader, 0, 0, 2, 1);

            rootGrid.add(usernameLabel, 0, 3);
            rootGrid.add(username, 1, 3);

            rootGrid.add(passwordLabel, 0, 4);
            rootGrid.add(password, 1, 4);

            username.setPrefWidth(WIDTHCUTFOFF);
            rootGrid.setPrefHeight(300);

            rootGrid.setHgap(10);

            dialog.getDialogPane().setContent(rootGrid);

            ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(confirm, cancel);

            Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirm);
            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancel);

            confirmButton.getStyleClass().add(
                    "confirmDialogButton"
            );

            cancelButton.getStyleClass().add(
                    "cancelDialogButton"
            );

            instructions.getStyleClass().add(
                    "instructions"
            );

            instructionsHeader.getStyleClass().add(
                    "instructions-header"
            );

            Optional<ButtonType> result = dialog.showAndWait();

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
                    savedUsername = addUser;
                    savedPassword = addPassword;

                    if(acc.accountExists(addUser)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Signup failed");
                        alert.setHeaderText(null);
                        alert.setContentText("Account with this name already exists");
                        continue;
                    }

                    boolean adder = acc.newAccount(addUser, addPassword);

                    if(adder) {
                        isDone = true;
                        dialog.close();
                        return acc.getSignedAccountName();
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