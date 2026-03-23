package hi.verkefni.vidmot;

import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MultipleApps extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try {
            System.out.println("Starting app...");

            var url = getClass().getResource(
                    "/hi/verkefni/vidmot/main-view.fxml"
            );

            System.out.println("FXML URL = " + url);

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            hi.verkefni.vidmot.switcher.Switcher.setScene(scene);

            stage.setScene(scene);
            stage.setTitle("Java Trip Assistance");
            stage.show();

            System.out.println("Stage shown.");
        } catch (Exception e) {
            System.out.println("CRASH in start(): " + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
