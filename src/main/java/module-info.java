module com.romawertq.messenger {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.rabbitmq.client;
    requires org.json;
    requires com.fasterxml.jackson.databind;

    opens com.romawertq.messenger to javafx.fxml;
    exports com.romawertq.messenger;
}