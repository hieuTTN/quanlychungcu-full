package com.app.controller.apartment;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.entity.Apartment;
import com.app.repository.ApartmentRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class AddApartmentController implements Initializable {

    private FrameController frameController;

    private Long id;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApartmentRepository apartmentRepository;

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    public void setId(Long id) {
        this.id = id;
        if(id != null){
            Apartment apartment = apartmentRepository.findById(id).get();
            txt_name.setText(apartment.getName());
            txt_dientich.setText(apartment.getAcreage().toString());
            txt_so_tang.setText(apartment.getFloor().toString());
            checkDaBan.setSelected(apartment.getIsSold());
            String formattedValue = String.format("%.0f", apartment.getPrice()); // Làm tròn và định dạng số không có dấu thập phân
            txt_giaban.setText(formattedValue);
        }
    }

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_dientich;

    @FXML
    private TextField txt_giaban;

    @FXML
    private TextField txt_so_tang;

    @FXML
    private CheckBox checkDaBan;

    @FXML
    private Button btnadd;

    @FXML
    void addCanHo(ActionEvent event) {
        if(checkInput()){
            Apartment apartment = new Apartment();
            if(id != null){
                apartment = apartmentRepository.findById(id).get();
            }
            apartment.setName(txt_name.getText());
            apartment.setAcreage(Float.valueOf(txt_dientich.getText()));
            apartment.setFloor(Integer.valueOf(txt_so_tang.getText()));
            apartment.setPrice(Double.valueOf(txt_giaban.getText()));
            apartment.setIsSold(checkDaBan.isSelected());
            apartmentRepository.save(apartment);
            Message.getMess("Thêm căn hộ thành công", Alert.AlertType.INFORMATION);
            openCanHo();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Boolean checkInput(){
        if(txt_giaban.getText().equals("") || txt_dientich.getText().equals("") || txt_name.getText().equals("")  || txt_so_tang.getText().equals("")){
            Message.getMess("Dữ liệu không được bỏ trống!", Alert.AlertType.WARNING);
            return false;
        }
        try {
            Integer.parseInt(txt_giaban.getText());
            Float.valueOf(txt_dientich.getText());
            Double.valueOf(txt_so_tang.getText());
        }catch (Exception e){
            Message.getMess("giá bán, diện tích, số tầng phải là kiểu số!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    void openCanHo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/apartment/apartment.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            VBox node = loader.load();
            ApartmentController childController = loader.getController();
            childController.setFrameController(frameController);
            frameController.noidung.getChildren().clear();
            frameController.noidung.getChildren().add(node);
            node.prefWidthProperty().bind(frameController.noidung.widthProperty());
            node.prefHeightProperty().bind(frameController.noidung.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
