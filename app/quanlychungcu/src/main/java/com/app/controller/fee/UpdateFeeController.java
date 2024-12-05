package com.app.controller.fee;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.entity.Fee;
import com.app.entity.UtilityBill;
import com.app.repository.*;
import com.app.service.MailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    private ServiceFeeRepository serviceFeeRepository;

    @Autowired
    private UtilityBillRepository utilityBillRepository;

    @Autowired
    private VehicleFeeRepository vehicleFeeRepository;

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
        if(fee.getUtilityBill().getElectricityIndexPreMonth() != null){
            lb_chisodienthangtruoc.setText(fee.getUtilityBill().getElectricityIndexPreMonth().toString());
        }
        if(fee.getUtilityBill().getWaterIndexPreMonth() != null){
            lb_chisonuocthangtruoc.setText(fee.getUtilityBill().getWaterIndexPreMonth().toString());
        }
        if(fee.getUtilityBill().getNumWater() != null) txt_sonuoc.setText(fee.getUtilityBill().getNumWater().toString());
        if(fee.getUtilityBill().getNumElectricity() != null) txt_sodien.setText(fee.getUtilityBill().getNumElectricity().toString());
        if(fee.getUtilityBill().getWaterIndex() != null) txt_chisonuoc.setText(fee.getUtilityBill().getWaterIndex().toString());
        if(fee.getUtilityBill().getElectricityIndex() != null) txt_chisodien.setText(fee.getUtilityBill().getElectricityIndex().toString());
        checkPhiCanHo.setSelected(fee.getServiceFee().getPaidStatus());
        checkPhiGuiXe.setSelected(fee.getVehicleFee().getPaidStatus());
        checkTienDien.setSelected(fee.getUtilityBill().getPaidStatus());
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
        if(Message.confirm("Xác nhận cập nhật thông tin ?") == false){
            return;
        }
        fee.getVehicleFee().setPaidStatus(checkPhiGuiXe.isSelected());
        vehicleFeeRepository.save(fee.getVehicleFee());

        fee.getServiceFee().setPaidStatus(checkPhiCanHo.isSelected());
        serviceFeeRepository.save(fee.getServiceFee());

        if(!txt_chisodien.equals("") && !txt_sodien.equals("")){
            try {
                Float chiSoDien = Float.valueOf(txt_chisodien.getText());
                Float soDien = Float.valueOf(txt_sodien.getText());
                fee.getUtilityBill().setElectricityIndex(chiSoDien);
                fee.getUtilityBill().setNumElectricity(soDien);
            }catch (Exception e) {}
        }
        if(!txt_chisonuoc.equals("") && !txt_sonuoc.equals("")){
            try {
                Float chiSoNuoc = Float.valueOf(txt_chisonuoc.getText());
                Float soNuoc = Float.valueOf(txt_sonuoc.getText());
                fee.getUtilityBill().setWaterIndex(chiSoNuoc);
                fee.getUtilityBill().setNumWater(soNuoc);
            }catch (Exception e) {}
        }
        saveHDDienNuoc(fee.getUtilityBill());
        Message.getMess("Cập nhật thông tin thành công", Alert.AlertType.CONFIRMATION);
        openList();
    }

    @FXML
    void nhapChiSoDien(KeyEvent event) {
        String soDien = txt_chisodien.getText();
        try {
            Float sd = Float.valueOf(soDien);
            if(fee.getUtilityBill().getElectricityIndexPreMonth() != null){
                Float soDienDung = sd - fee.getUtilityBill().getElectricityIndexPreMonth();
                txt_sodien.setText(soDienDung.toString());
            }
            else{
                txt_sodien.setText(sd.toString());
            }
        }catch (Exception e) {
            txt_sodien.setText("0");
        }
    }

    @FXML
    void nhapChiSoNuoc(KeyEvent event) {
        String soNuoc = txt_chisonuoc.getText();
        try {
            Float sd = Float.valueOf(soNuoc);
            if(fee.getUtilityBill().getWaterIndexPreMonth() != null){
                Float soNuocDung = sd - fee.getUtilityBill().getWaterIndexPreMonth();
                txt_sonuoc.setText(soNuocDung.toString());
            }
            else{
                txt_sonuoc.setText(sd.toString());
            }
        }catch (Exception e) {
            txt_sonuoc.setText("0");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void saveHDDienNuoc(UtilityBill utilityBill){
        Double fee = 0D;
        if(utilityBill.getNumWater() != null){
            fee += calculateWaterBill(utilityBill.getNumWater().intValue());
        }
        if(utilityBill.getNumElectricity() != null){
            fee += calculateElectricityBill(utilityBill.getNumElectricity().intValue());
        }
        if(fee != 0D){
            utilityBill.setFee(fee);
        }
        utilityBillRepository.save(utilityBill);
    }



    public double calculateWaterBill(int numWater) {
        double totalCost = 0;

        // Giá nước theo từng bậc
        int tier1Limit = 10;
        int tier2Limit = 20;
        int tier3Limit = 30;

        double tier1Rate = 8500;
        double tier2Rate = 9900;
        double tier3Rate = 16000;
        double tier4Rate = 27000;

        if (numWater > tier3Limit) {
            totalCost += (numWater - tier3Limit) * tier4Rate;
            numWater = tier3Limit;
        }
        if (numWater > tier2Limit) {
            totalCost += (numWater - tier2Limit) * tier3Rate;
            numWater = tier2Limit;
        }
        if (numWater > tier1Limit) {
            totalCost += (numWater - tier1Limit) * tier2Rate;
            numWater = tier1Limit;
        }
        if (numWater > 0) {
            totalCost += numWater * tier1Rate;
        }

        return totalCost;
    }

    public static double calculateElectricityBill(int numKWh) {
        double totalCost = 0;

        // Giá điện theo từng bậc
        int tier1Limit = 50;
        int tier2Limit = 100;
        int tier3Limit = 200;
        int tier4Limit = 300;
        int tier5Limit = 400;

        double tier1Rate = 1806;
        double tier2Rate = 1866;
        double tier3Rate = 2167;
        double tier4Rate = 2729;
        double tier5Rate = 3050;
        double tier6Rate = 3151;

        if (numKWh > tier5Limit) {
            totalCost += (numKWh - tier5Limit) * tier6Rate;
            numKWh = tier5Limit;
        }
        if (numKWh > tier4Limit) {
            totalCost += (numKWh - tier4Limit) * tier5Rate;
            numKWh = tier4Limit;
        }
        if (numKWh > tier3Limit) {
            totalCost += (numKWh - tier3Limit) * tier4Rate;
            numKWh = tier3Limit;
        }
        if (numKWh > tier2Limit) {
            totalCost += (numKWh - tier2Limit) * tier3Rate;
            numKWh = tier2Limit;
        }
        if (numKWh > tier1Limit) {
            totalCost += (numKWh - tier1Limit) * tier2Rate;
            numKWh = tier1Limit;
        }
        if (numKWh > 0) {
            totalCost += numKWh * tier1Rate;
        }

        return totalCost;
    }

    public void openList(){
        try {
            // Load child node khác
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fee/fee.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            Node node = loader.load();
            FeeController childController = loader.getController();
            childController.setFrameController(frameController);
            frameController.noidung.getChildren().clear();
            frameController.noidung.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
