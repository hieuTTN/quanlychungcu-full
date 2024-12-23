package com.app.controller.maintenance;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.controller.apartment.ApartmentController;
import com.app.entity.Apartment;
import com.app.entity.Maintenance;
import com.app.entity.Resident;
import com.app.repository.MaintenanceRepository;
import com.app.repository.ResidentRepository;
import com.app.service.MailService;
import com.app.service.UserUtils;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class AddMaintenanceController implements Initializable {


    private FrameController frameController;

    private Long id;

    @Autowired
    private ApplicationContext applicationContext;

    private MailService mailService = new MailService();

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    public void setId(Long id) {
        this.id = id;
        if(id != null){
            Maintenance maintenance = maintenanceRepository.findById(id).get();
            txt_title.setText(maintenance.getTitle());
            txt_ngayhoanthanh.setValue(maintenance.getExpectedCompletionDate());
            txt_ngaysua.setValue(maintenance.getMaintenanceDate());
            txt_noidung.setHtmlText(maintenance.getContent());
            txt_giosua.setText(maintenance.getMaintenanceTime());
            txt_giohoanthanh.setText(maintenance.getExpectedCompletionTime());
        }
    }

    @FXML
    private TextField txt_title;

    @FXML
    private DatePicker txt_ngaysua;

    @FXML
    private TextField txt_giosua;

    @FXML
    private DatePicker txt_ngayhoanthanh;

    @FXML
    private TextField txt_giohoanthanh;

    @FXML
    private CheckBox checkDaHoanThanh;

    @FXML
    private Button btnadd;

    @FXML
    private HTMLEditor txt_noidung;

    @FXML
    void addThongBao(ActionEvent event) {
        if(checkInput()){
            Maintenance maintenance = new Maintenance();
            if(id != null){
                maintenance = maintenanceRepository.findById(id).get();
            }
            maintenance.setTitle(txt_title.getText());
            maintenance.setCompleted(checkDaHoanThanh.isSelected());
            maintenance.setMaintenanceDate(txt_ngaysua.getValue());
            maintenance.setMaintenanceTime(txt_giosua.getText());
            maintenance.setContent(txt_noidung.getHtmlText());
            maintenance.setCreatedDate(LocalDateTime.now());
            maintenance.setExpectedCompletionDate(txt_ngayhoanthanh.getValue());
            maintenance.setCreatedBy(UserUtils.user);
            maintenance.setExpectedCompletionTime(txt_giohoanthanh.getText());
            maintenanceRepository.save(maintenance);
            Message.getMess("Thêm thông báo thành công", Alert.AlertType.INFORMATION);
            List<Resident> residents = residentRepository.findAll();
            if(maintenance.getCompleted() == false){
                residents.forEach(p->{
                    mailService.sendAsyncEmail(p.getEmail(), txt_title.getText(), txt_noidung.getHtmlText());
                });
            }
            else{
                residents.forEach(p->{
                    mailService.sendAsyncEmail(p.getEmail(), txt_title.getText() + " - Đã hoàn thành",
                            "Xin lỗi về sự bất tiện này, chúng tôi đã hoàn thành bảo trì hệ thống");
                });
            }
            openList();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        txt_giosua.setText(LocalTime.now().format(formatter));
        txt_giohoanthanh.setText(LocalTime.now().format(formatter));
    }

    public Boolean checkInput(){
        if(txt_title.getText().equals("") || txt_noidung.getHtmlText().trim().isEmpty() || txt_ngaysua.getValue() == null  || txt_ngayhoanthanh.getValue() == null){
            Message.getMess("Dữ liệu không được bỏ trống!", Alert.AlertType.WARNING);
            return false;
        }
        if(isValidTimeFormat(txt_giosua.getText()) == false || isValidTimeFormat(txt_giohoanthanh.getText()) == false ){
            Message.getMess("Giờ phải là định dạng giờ:phút!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    void openList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/maintenance/maintenance.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            VBox node = loader.load();
            MaintenanceController childController = loader.getController();
            childController.setFrameController(frameController);
            frameController.noidung.getChildren().clear();
            frameController.noidung.getChildren().add(node);
            node.prefWidthProperty().bind(frameController.noidung.widthProperty());
            node.prefHeightProperty().bind(frameController.noidung.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidTimeFormat(String time) {
        // Biểu thức chính quy cho định dạng hh:mm
        String regex = "^([01]?[0-9]|2[0-3]):([0-5][0-9])$";
        return time.matches(regex);
    }


}
