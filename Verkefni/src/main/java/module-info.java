module hi.verkefni.vidmot.verkefni3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens hi.verkefni.vidmot.verkefni3.controllers to javafx.fxml;
    opens hi.verkefni.vidmot.verkefni3.switcher to javafx.fxml;
    opens hi.verkefni.vidmot.verkefni3.dataInterface to javafx.fxml;

    exports hi.verkefni.vidmot.verkefni3;
}