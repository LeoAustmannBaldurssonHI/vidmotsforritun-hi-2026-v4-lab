package hi.verkefni.vidmot.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.beans.binding.*;

import hi.verkefni.vidmot.vinnsla.account.Account;

import java.util.Optional;

import java.io.IOException;

// do later...

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
                    "cancelButtonDialog"
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
                ButtonType deleteCancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                Button confirmButton = (Button) deleteDialog.getDialogPane().lookupButton(confirm);
                Button deleteCancelButton = (Button) deleteDialog.getDialogPane().lookupButton(deleteCancel);

                deleteDialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK);
                deleteDialog.getDialogPane().getButtonTypes().addAll(confirm, deleteCancel);

                deleteDialog.setTitle("Account Deletion");
                deleteDialog.setContentText("Warning: You're about to do something that is irrversable. If press on " +
                        "confirm, the account will be deleted from our system permanently and cannot be retrieved.");

                Optional<ButtonType> deleteResult = deleteDialog.showAndWait();
                if(deleteResult.get() == confirm) {
                    try {
                        String deleteAccount = acc.getSignedAccountName();
                        acc.deleteAccount(deleteAccount);
                        deleteDialog.close();
                        dialog.close();

                        if(acc.activeSession()) System.out.println("We have an error with the account system.");

                        done = true;
                        return;
                    } catch(IOException e) {
                        e.printStackTrace();
                        done = false;
                        return;
                    }
                } else if(result.isEmpty() || result.get() == cancel) {
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