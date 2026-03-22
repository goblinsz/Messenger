package com.romawertq.messenger;

import com.romawertq.messenger.ReceiptAndDispatch.Dispatch;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Label lbl = new Label();
        TextField textField = new TextField();
        textField.setPrefColumnCount(11);
        Button btn = new Button("Click");
        btn.setOnAction(event -> lbl.setText("Сообщение отправлено"));
        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10, textField, btn, lbl);
        Scene scene = new Scene(root, 250, 200);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}