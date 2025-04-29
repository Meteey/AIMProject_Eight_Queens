module com.example.aimproject_eight_queens {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.example.aimproject_eight_queens to javafx.fxml;
    exports com.example.aimproject_eight_queens;
}