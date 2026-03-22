module hi.verkefni.vidmot {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;

    opens hi.verkefni.vidmot.controllers to javafx.fxml;
    opens hi.verkefni.vidmot.switcher to javafx.fxml;
    opens hi.verkefni.vidmot.dataInterface to javafx.fxml;

    exports hi.verkefni.vidmot;
}