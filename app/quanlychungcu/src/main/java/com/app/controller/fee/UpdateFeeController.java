package com.app.controller.fee;

import com.app.controller.FrameController;
import com.app.entity.Fee;
import com.app.repository.MaintenanceRepository;
import com.app.repository.ResidentRepository;
import com.app.service.MailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class UpdateFeeController implements Initializable {

    private FrameController frameController;

    private Fee fee;

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

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    @FXML
    private Label lb_infor;

    @FXML
    private TextField txt_chisodien;

    @FXML
    private TextField txt_sodien;

    @FXML
    private TextField txt_chisonuoc;

    @FXML
    private TextField txt_sonuoc;

    @FXML
    private Label lb_chisodienthangtruoc;

    @FXML
    private Label lb_chisonuocthangtruoc;

    @FXML
    private CheckBox checkTienDien;

    @FXML
    private CheckBox checkPhiCanHo;

    @FXML
    private CheckBox checkPhiGuiXe;

    @FXML
    private Button btnadd;

    @FXML
    private ImageView anh;

    @FXML
    void capNhatThongTin(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
