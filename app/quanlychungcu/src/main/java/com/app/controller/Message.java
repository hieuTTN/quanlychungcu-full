package com.app.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Message {

    public static void getMess(String str, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(str);
        alert.show();
    }

    public static Boolean confirm(String str) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận hành động");
        alert.setHeaderText(str);
        alert.setContentText("Lựa chọn OK để thực hiện");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }


}
