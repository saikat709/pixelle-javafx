module com.saikat.pixelle {
    // requires javafxadd origin .controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires jdk.compiler;

    opens com.saikat.pixelle to javafx.fxml;
    opens com.saikat.pixelle.screens to javafx.fxml;
    opens com.saikat.pixelle.controllers to javafx.fxml;
    opens com.saikat.pixelle.components;
    exports com.saikat.pixelle;
    exports com.saikat.pixelle.constants;
    exports com.saikat.pixelle.screens to javafx.fxml;
    exports com.saikat.pixelle.components;
    exports com.saikat.pixelle.listeners;
    exports com.saikat.pixelle.savable;
}