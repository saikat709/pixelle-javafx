module com.saikat.pixelle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.saikat.pixelle to javafx.fxml;
    exports com.saikat.pixelle;
}