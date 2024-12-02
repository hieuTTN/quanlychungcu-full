package com.app.controller.repair;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.controller.maintenance.MaintenanceController;
import com.app.entity.Maintenance;
import com.app.entity.RepairRequest;
import com.app.repository.RepairRequestRepository;
import com.app.repository.ResidentRepository;
import com.app.service.MailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class UpdateRepairController implements Initializable {

    private FrameController frameController;

    private Long id;

    @Autowired
    private ApplicationContext applicationContext;

    private MailService mailService = new MailService();

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    public void setId(Long id) {
        this.id = id;
        if(id != null){
            RepairRequest repairRequest = repairRequestRepository.findById(id).get();
            txt_chiphi.setText(repairRequest.getFee().toString());
            checkDaSua.setSelected(repairRequest.getFixed());
            checkDaThanhToan.setSelected(repairRequest.getPaid());
            lbthongtin.setText("Cập nhật thông tin sửa chữa căn hộ "+repairRequest.getApartment().getName());
        }
    }

    @FXML
    private Label lbthongtin;

    @FXML
    private TextField txt_chiphi;

    @FXML
    private CheckBox checkDaSua;

    @FXML
    private CheckBox checkDaThanhToan;

    @FXML
    private Button btnadd;

    @FXML
    void capNhat(ActionEvent event) {
        RepairRequest repairRequest = repairRequestRepository.findById(id).get();
        if(!txt_chiphi.getText().equals("")){
            try {
                Double gia = Double.valueOf(txt_chiphi.getText());
                repairRequest.setFee(gia);
                repairRequest.setFixed(checkDaSua.isSelected());
                repairRequest.setPaid(checkDaThanhToan.isSelected());
                repairRequestRepository.save(repairRequest);
                openList();
            }catch (Exception e){
                Message.getMess("Chi phí phải là kiểu số", Alert.AlertType.WARNING);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    void openList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/repair/repair.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            VBox node = loader.load();
            RepairController childController = loader.getController();
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
