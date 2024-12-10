package com.app.controller.account;

import com.app.controller.Message;
import com.app.controller.vehicle.VehicleController;
import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.service.UserUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable {

	@Autowired
	private UserRepository userRepository;

	@FXML
	private GridPane mainpaine;
	
	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@Autowired
	private ApplicationContext applicationContext;

	@FXML
	void login(ActionEvent event) {
		User user = userRepository.login(username.getText(), password.getText());
		if(user == null){
			Message.getMess("Đăng nhập thất bại", Alert.AlertType.WARNING);
			return;
		}
		UserUtils.user = user;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/frames.fxml"));
			loader.setControllerFactory(applicationContext::getBean);
			Parent root = loader.load();
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		setBackground();
	}

	public void setBackground() {
		InputStream ip = getClass().getResourceAsStream("/image/background.jpg");
		if (ip != null) {
			Image image = new Image(ip);
			// Tạo BackgroundImage
			BackgroundImage backgroundImage = new BackgroundImage(
					image,
					BackgroundRepeat.NO_REPEAT, // Không lặp lại hình ảnh
					BackgroundRepeat.NO_REPEAT, // Không lặp lại theo chiều dọc
					BackgroundPosition.CENTER,  // Đặt vị trí trung tâm
					new BackgroundSize(100, 100, true, true, true, true)// Điều chỉnh kích thước
			);
			// Áp dụng Background cho GridPane
			mainpaine.setBackground(new Background(backgroundImage));
		} else {
			System.out.println("Hình ảnh không được tìm thấy!");
		}
	}
}
