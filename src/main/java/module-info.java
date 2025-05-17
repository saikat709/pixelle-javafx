module com.saikat.pixelle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.saikat.pixelle to javafx.fxml;
    opens com.saikat.pixelle.screens to javafx.fxml;
    opens com.saikat.pixelle.controllers to javafx.fxml;
    exports com.saikat.pixelle;
    exports com.saikat.pixelle.screens to javafx.fxml;
}