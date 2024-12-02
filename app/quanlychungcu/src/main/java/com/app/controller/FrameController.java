package com.app.controller;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.app.controller.apartment.ApartmentController;
import com.app.controller.maintenance.MaintenanceController;
import com.app.controller.resident.ResidentController;
import com.app.repository.UserRepository;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FrameController implements Initializable {

    @FXML
    private Button btnqlcanho;

    @FXML
    private Button btnquanlyhodan;

    @FXML
    private Button btnqlphidichvu;

    @FXML
    private Button btntinhtrangbaotri;

    @FXML
    private Button btndangkybaoduong;

    @FXML
    private Button btndongphidichvu;

    @FXML
    private Button btnphuongtien;

    @FXML
    private Label time;

    @FXML
    private ImageView logo;

    @FXML
    private ImageView avatar;

    @FXML
    public VBox noidung;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private List<Button> buttons;

    @FXML
    void openQlCanHo(MouseEvent event) throws IOException {
        setNullId();
        Button buttonClicked = (Button) event.getSource();
        buttonClicked.setId("activemenu");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/apartment/apartment.fxml"));
        loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
        VBox node = loader.load();
        noidung.getChildren().clear();
        noidung.getChildren().add(node);
        ApartmentController childController = loader.getController();
        childController.setFrameController(this);
        node.prefWidthProperty().bind(noidung.widthProperty());
        node.prefHeightProperty().bind(noidung.heightProperty());
    }

    @FXML
    void openQuanLyHoDan(MouseEvent event) throws IOException{
        setNullId();
        Button buttonClicked = (Button) event.getSource();
        buttonClicked.setId("activemenu");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/resident/resident.fxml"));
        loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
        VBox node = loader.load();
        noidung.getChildren().clear();
        noidung.getChildren().add(node);
        ResidentController childController = loader.getController();
        childController.setFrameController(this);
        node.prefWidthProperty().bind(noidung.widthProperty());
        node.prefHeightProperty().bind(noidung.heightProperty());
    }

    @FXML
    void openBaoTri(MouseEvent event) throws IOException {
        setNullId();
        Button buttonClicked = (Button) event.getSource();
        buttonClicked.setId("activemenu");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/maintenance/maintenance.fxml"));
        loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
        VBox node = loader.load();
        noidung.getChildren().clear();
        noidung.getChildren().add(node);
        MaintenanceController childController = loader.getController();
        childController.setFrameController(this);
        node.prefWidthProperty().bind(noidung.widthProperty());
        node.prefHeightProperty().bind(noidung.heightProperty());
    }

    @FXML
    void openQlDichVu(MouseEvent event) {

    }


    @FXML
    void openQuanLyCanHo(MouseEvent event) {

    }


    @FXML
    void openBaoDuong(MouseEvent event) {

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        buttons = List.of(btndangkybaoduong, btndongphidichvu, btnphuongtien, btnqlcanho, btnqlphidichvu, btnquanlyhodan, btntinhtrangbaotri);
		setAvatar();
		setLogo();
		AnimationTimer timer = new AnimationTimer() {
		    @Override
		    public void handle(long now) {
		        time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		    }
		};
		timer.start();
//        List<User> users = userRepository.findAll();
//        System.out.println("size bash: "+users.size());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/apartment/apartment.fxml"));
        loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
        VBox node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        noidung.getChildren().clear();
        noidung.getChildren().add(node);
        ApartmentController childController = loader.getController();
        childController.setFrameController(this);
        node.prefWidthProperty().bind(noidung.widthProperty());
        node.prefHeightProperty().bind(noidung.heightProperty());
	}

    public void setNullId(){
        for (Button button : buttons) {
            button.setId(null); // Hoặc đặt ID khác nếu cần
        }
    }

	public void setAvatar() {
		InputStream ip = getClass().getResourceAsStream("/image/avatar.jpg");
		Image image = new Image(ip);
		avatar.setImage(image);
	}

	public void setLogo() {
		InputStream ip = getClass().getResourceAsStream("/image/avatar.jpg");
		Image image = new Image(ip);
		logo.setImage(image);
	}




}