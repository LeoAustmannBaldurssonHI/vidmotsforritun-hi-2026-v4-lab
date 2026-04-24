package hi.verkefni.vidmot;

import hi.verkefni.vidmot.switcher.Switcher;
import hi.verkefni.vidmot.switcher.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class MultipleApps extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try {
            System.out.println("Executing program...");

            var url = getClass().getResource(
                    "/hi/verkefni/vidmot/main-view.fxml"
            );

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            hi.verkefni.vidmot.switcher.Switcher.setScene(scene);

            // change the icon of our application
            stage.getIcons().add(
              new Image(getClass().getResourceAsStream("/hi/verkefni/vidmot/CSS/Images/icon.png"))
            );

            stage.setResizable(false); // disable resizing
            stage.setScene(scene);
            stage.setTitle("Java Trip Assistance");
            stage.show();

            System.out.println("System initialized.");
        } catch (Exception e) {
            System.out.println("CRASH in start(): " + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
