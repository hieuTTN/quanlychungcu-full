package com.app.controller.account;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class LoginController {
	
	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	void login(ActionEvent event) {

	}
}
