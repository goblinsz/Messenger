module com.romawertq.messenger {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens com.romawertq.messenger to javafx.fxml;
    exports com.romawertq.messenger;
}