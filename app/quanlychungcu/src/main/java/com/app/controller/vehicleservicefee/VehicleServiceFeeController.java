package com.app.controller.vehicleservicefee;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.entity.Resident;
import com.app.entity.Vehicle;
import com.app.entity.VehicleServiceFee;
import com.app.repository.ResidentRepository;
import com.app.repository.VehicleFeeRepository;
import com.app.repository.VehicleServiceFeeRepository;
import com.app.service.MailService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class VehicleServiceFeeController implements Initializable {

    private FrameController frameController;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private VehicleServiceFeeRepository vehicleServiceFeeRepository;

    @Autowired
    private ResidentRepository residentRepository;

    private MailService mailService = new MailService();

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    @FXML
    private Label lbquanlyhodan;

    @FXML
    private TableView<VehicleServiceFee> table;

    @FXML
    private TableColumn<VehicleServiceFee, Long> col_id;

    @FXML
    private TableColumn<VehicleServiceFee, String> col_tenxe;

    @FXML
    private TableColumn<VehicleServiceFee, String> col_phigui;

    @FXML
    private TextField txt_tenxe;

    @FXML
    private TextField txt_phigui;

    private VehicleServiceFee vehicleServiceFee;

    private ObservableList<VehicleServiceFee> vehicleServiceFees = FXCollections.observableArrayList();

    @FXML
    void capNhat(ActionEvent event) {
        if(vehicleServiceFee == null){
            Message.getMess("Hãy chọn 1 phí gửi xe", Alert.AlertType.WARNING); return;
        }
        if(checkInput()){
            String giaCu = formatToVND(vehicleServiceFee.getFee());
            if(vehicleServiceFee.getFee() == Double.valueOf(txt_phigui.getText())){
                Message.getMess("Giá tiền không thay đổi", Alert.AlertType.WARNING);
                return;
            }
            vehicleServiceFee.setFee(Double.valueOf(txt_phigui.getText()));
            vehicleServiceFeeRepository.save(vehicleServiceFee);
            initTable();
            Message.getMess("Cập nhật thành công", Alert.AlertType.CONFIRMATION);
            for(Resident r : residentRepository.findAll()){
                mailService.sendAsyncEmail(r.getEmail(), "Thay đổi phí gửi xe", "Thông báo, phí gửi xe "+vehicleServiceFee.getName()+
                        " sẽ thay đổi từ "+giaCu+" thành "+formatToVND(vehicleServiceFee.getFee())+" từ tháng sau. Xin chân thành cảm ơn quý cư dân");
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDoRongCot();
        initTable();
        clickMainTable();
    }

    public void initTable() {
        vehicleServiceFees.clear();
        List<VehicleServiceFee> all = vehicleServiceFeeRepository.findAll();
        all.forEach(p->{
            vehicleServiceFees.add(p);
        });
        table.setItems(vehicleServiceFees);
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_tenxe.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_phigui.setCellValueFactory(cellData -> {
            VehicleServiceFee vehicle = cellData.getValue();
            return new SimpleStringProperty(formatToVND(vehicle.getFee()));
        });
    }

    public void clickMainTable() {
        table.setRowFactory(tv -> {
            TableRow<VehicleServiceFee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    vehicleServiceFee = row.getItem();
                    txt_phigui.setText(vehicleServiceFee.getFee().toString());
                    txt_tenxe.setText(vehicleServiceFee.getName().toString());
                }
            });
            return row;
        });
    }


    public void setDoRongCot(){
        col_id.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_tenxe.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
        col_phigui.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
    }

    public static String formatToVND(Double amount) {
        Locale vietnamLocale = new Locale("vi", "VN"); // Định dạng theo Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnamLocale);
        return currencyFormatter.format(amount);
    }

    public Boolean checkInput(){
        if(txt_phigui.equals("")){
            Message.getMess("Phí không được để trống", Alert.AlertType.WARNING);return false;
        }
        try {
            Double.valueOf(txt_phigui.getText());
        }
        catch (Exception e){
            Message.getMess("Phí phải là kiểu số", Alert.AlertType.WARNING);return false;
        }
        return true;
    }
}
