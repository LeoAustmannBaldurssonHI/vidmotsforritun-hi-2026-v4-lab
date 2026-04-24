package hi.verkefni.vidmot.controllers;

// FXMl & Scene imports
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

// Binding imports
import javafx.beans.binding.*;

// Vinnsla & Switcher imports
import hi.verkefni.vidmot.vinnsla.account.Account;
import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;

// Optional imports
import java.util.Optional;

// IO Imports
import java.io.IOException;

public class AccountDialog {
    private final String CSS = "/hi/verkefni/vidmot/CSS/style.css";
    private final int HEIGHTCUTOFF = 50;
    private final int WIDTHCUTFOFF = 300;
    private Account acc;

    public void accountDialog() throws IOException {
        acc = new Account();
        if (acc == null) {
            System.err.println("Critical error, no user authenticated for us to be able to log out or delete the account.");
            return;
        }
        boolean done = false;
        while(!done) {
            Dialog<ButtonType> dialog = new Dialog<>();

            dialog.getDialogPane().getStylesheets().add(
                    getClass().getResource(CSS).toExternalForm()
            );

            GridPane grid = new GridPane(); // declared early

            Label warning = new Label("Choose between the two actions");
            Label option1 = new Label("Log out: Signs out of the current account");
            Label option2 = new Label("Delete account: Delete's the current account");

            warning.getStyleClass().add(
                    "accountHeader"
            );

            option1.getStyleClass().add(
                    "dialogLabel"
            );

            option2.getStyleClass().add(
                    "dialogLabel"
            );

            grid.add(warning, 0, 0);
            grid.add(option1, 0, 1);
            grid.add(option2, 0, 2);

            dialog.getDialogPane().setContent(grid);

            dialog.setTitle("Account Management");

            ButtonType delete = new ButtonType("Delete Account", ButtonBar.ButtonData.OTHER);
            ButtonType logOut = new ButtonType("Log out", ButtonBar.ButtonData.OTHER);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().getButtonTypes().addAll(logOut, delete, cancel);

            Button deleteButton = (Button) dialog.getDialogPane().lookupButton(delete);
            Button logOutButton = (Button) dialog.getDialogPane().lookupButton(logOut);
            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancel);

            deleteButton.getStyleClass().add(
                    "deleteButtonDialog"
            );

            cancelButton.getStyleClass().add(
                    "cancelDialogButton"
            );

            logOutButton.getStyleClass().add(
                    "logOutButtonDialog"
            );

            Optional<ButtonType> result = dialog.showAndWait();

            if(!result.isPresent()) return;

            if(result.get() == delete) {
                String currentAccount = acc.getSignedAccountName();

                Dialog<ButtonType> deleteDialog = new Dialog<>();

                deleteDialog.getDialogPane().getStylesheets().add(
                        getClass().getResource(CSS).toExternalForm()
                );

                ButtonType confirm = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
                ButtonType deleteCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                deleteDialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK);
                deleteDialog.getDialogPane().getButtonTypes().addAll(confirm, deleteCancel);

                Button confirmDialogButton = (Button) deleteDialog.getDialogPane().lookupButton(confirm);
                Button deleteCancelButton = (Button) deleteDialog.getDialogPane().lookupButton(deleteCancel);

                confirmDialogButton.getStyleClass().add(
                        "specialConfirmDialogButton"
                );

                deleteCancelButton.getStyleClass().add(
                        "specialCancelDialogButton"
                );

                deleteDialog.setWidth(600);

                deleteDialog.setTitle("Account Deletion");
                deleteDialog.setContentText("Warning: You're about to do something that is irrversable. \nIf press on " +
                        "confirm, the account will be deleted from our system permanently and cannot be retrieved.");

                Optional<ButtonType> deleteResult = deleteDialog.showAndWait();
                if(deleteResult.get() == confirm) {
                    try {
                        String deleteAccount = acc.getSignedAccountName();
                        acc.deleteAccount(deleteAccount);
                        deleteDialog.close();
                        dialog.close();

                        // Security check - Do we still have an active session?
                        if(acc.activeSession()) {
                            System.out.println("We have an error with the account system.");
                            System.exit(1);
                        }

                        done = true;
                        return;
                    } catch(IOException e) {
                        e.printStackTrace();
                        done = false;
                        return;
                    }
                } else if(deleteResult.isEmpty() || deleteResult.get() == cancel) {
                    deleteDialog.close();
                    done = true;
                }
            } else if(result.get() == logOut) {
                acc.logOut();
                if(acc.activeSession()) System.out.println("We have an error with the account system.");
                done = true;
                return;
            } else if(result.isEmpty() || result.get() == cancel) {
                dialog.close();
                done = true;
                return;
            }
        }
        return;
    }
}