package com.app.controller.fee;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.controller.maintenance.AddMaintenanceController;
import com.app.entity.*;
import com.app.repository.*;
import com.app.service.MailService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class FeeController implements Initializable {

    private FrameController frameController;

    @Autowired
    private ApplicationContext applicationContext;

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ServiceFeeRepository serviceFeeRepository;

    @Autowired
    private VehicleServiceFeeRepository vehicleServiceFeeRepository;

    @Autowired
    private UtilityBillRepository utilityBillRepository;

    @Autowired
    private VehicleFeeRepository vehicleFeeRepository;

    private MailService mailService = new MailService();

    @FXML
    private Label lbquanlyhodan;

    @FXML
    private ComboBox<String> cb_thang;

    @FXML
    private ComboBox<Integer> cb_nam;

    @FXML
    private TableView<Fee> table;

    @FXML
    private TableColumn<Fee, String> col_canho;

    @FXML
    private TableColumn<Fee, String> col_sodien;

    @FXML
    private TableColumn<Fee, String> col_phicanho;

    @FXML
    private TableColumn<Fee, String> col_phiguixe;

    @FXML
    private TableColumn<Fee, String> col_phidiennuoc;

    @FXML
    private TableColumn<Fee, String> col_conlai;

    @FXML
    private TableColumn<Fee, HBox> col_chucnang;

    private ObservableList<String> listThang = FXCollections.observableArrayList();

    private ObservableList<Integer> listNam = FXCollections.observableArrayList();

    private ObservableList<Fee> fees = FXCollections.observableArrayList();

    @FXML
    void locDuLieu(ActionEvent event) {
        initTable();
    }

    @FXML
    void taoDongPhi(ActionEvent event) {
        Integer month = Integer.valueOf(cb_thang.getValue().split(" ")[1]);
        Integer year = cb_nam.getValue();
        if(Message.confirm("Xác nhận tạo yêu cầu đóng phí tháng "+month+" năm "+year+" ?") == false){
            return;
        }
        List<Apartment> apartments = apartmentRepository.canHoDaBan();
        for (Apartment a : apartments){
            ServiceFee serviceFeeEx = serviceFeeRepository.findByThangNamAndCanHo(month, year, a.getId());
            UtilityBill utilityBillEx = utilityBillRepository.findByThangNamAndCanHo(month, year, a.getId());
            VehicleFee vehicleFeeEx = vehicleFeeRepository.findByThangNamAndCanHo(month, year, a.getId());
            if(serviceFeeEx == null) createServiceFee(a, month, year);
            if(vehicleFeeEx == null) createVehicleFee(a, month, year);
            if(utilityBillEx == null) createDNFee(a, month, year);
            if(serviceFeeEx != null && serviceFeeEx.getPaidStatus() == false){
                sendMailFee(a, serviceFeeEx.getFee(), month, year, 1);
            }
            if(vehicleFeeEx != null && vehicleFeeEx.getPaidStatus() == false){
                sendMailFee(a, vehicleFeeEx.getFee(), month, year, 2);
            }
            if(utilityBillEx != null && utilityBillEx.getPaidStatus() == false && utilityBillEx.getFee() != null){
                sendMailFee(a, utilityBillEx.getFee(), month, year, 3);
            }
        }
        Message.getMess("Tạo yêu cầu thành công", Alert.AlertType.CONFIRMATION);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        khoiTao();
        setDoRongCot();
        initTable();
    }

    public void initTable() {
        fees.clear();
        Integer month = Integer.valueOf(cb_thang.getValue().split(" ")[1]);
        Integer year = cb_nam.getValue();
        List<Apartment> apartments = apartmentRepository.canHoDaBan();
        for (Apartment a : apartments){
            ServiceFee serviceFeeEx = serviceFeeRepository.findByThangNamAndCanHo(month, year, a.getId());
            UtilityBill utilityBillEx = utilityBillRepository.findByThangNamAndCanHo(month, year, a.getId());
            VehicleFee vehicleFeeEx = vehicleFeeRepository.findByThangNamAndCanHo(month, year, a.getId());
            if(serviceFeeEx != null && utilityBillEx != null && vehicleFeeEx != null){
                Fee fee = new Fee();
                fee.setServiceFee(serviceFeeEx);
                fee.setApartment(a);
                fee.setVehicleFee(vehicleFeeEx);
                fee.setUtilityBill(utilityBillEx);
                fees.add(fee);
            }
        }
        table.setItems(fees);
        col_canho.setCellValueFactory(cellData -> {
            Fee fee = cellData.getValue();
            return new SimpleStringProperty(fee.getApartment().getName());
        });
        col_phidiennuoc.setCellValueFactory(cellData -> {
            Fee fee = cellData.getValue();
            if(fee.getUtilityBill().getFee() == null){
                return new SimpleStringProperty("Chưa nhập số điện nước");
            }
            return new SimpleStringProperty(formatToVND(fee.getUtilityBill().getFee()));
        });
        col_sodien.setCellValueFactory(cellData -> {
            Fee fee = cellData.getValue();
            if(fee.getUtilityBill().getFee() == null){
                return new SimpleStringProperty("0");
            }
            return new SimpleStringProperty("Số điện: "+ fee.getUtilityBill().getNumElectricity()+", Số nước: "+fee.getUtilityBill().getNumWater());
        });
        col_phicanho.setCellValueFactory(cellData -> {
            Fee fee = cellData.getValue();
            if(fee.getServiceFee().getFee() == null){
                return new SimpleStringProperty("Chưa có phí dịch vụ");
            }
            return new SimpleStringProperty(formatToVND(fee.getServiceFee().getFee()));
        });
        col_phiguixe.setCellValueFactory(cellData -> {
            Fee fee = cellData.getValue();
            if(fee.getVehicleFee().getFee() == null){
                return new SimpleStringProperty("Chưa có phí gửi xe");
            }
            return new SimpleStringProperty(formatToVND(fee.getVehicleFee().getFee()));
        });
        col_conlai.setCellValueFactory(cellData -> {
            Fee fee = cellData.getValue();
            String conLai = "";
            if(fee.getServiceFee().getPaidStatus() == false ){
                conLai += "Căn hộ";
            }
            if(fee.getVehicleFee().getPaidStatus() == false && fee.getVehicleFee().getFee() > 0){
                if (conLai.length() > 0 ) conLai += " - ";
                conLai += "Gửi xe";
            }
            if(fee.getUtilityBill().getPaidStatus() == false && fee.getUtilityBill().getFee() != null &&  fee.getUtilityBill().getFee() > 0){
                if (conLai.length() > 0 ) conLai += " - ";
                conLai += " Điện nước";
            }
            return new SimpleStringProperty(conLai);
        });

        col_chucnang.setCellFactory(new Callback<TableColumn<Fee, HBox>, TableCell<Fee, HBox>>() {
            @Override
            public TableCell<Fee, HBox> call(TableColumn<Fee, HBox> param) {
                return new TableCell<Fee, HBox>() {
                    @Override
                    protected void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null); // Nếu dòng rỗng, không hiển thị gì
                        } else {
                            Fee fee = getTableView().getItems().get(getIndex()); // Lấy item hiện tại
                            HBox hbox = createButtonBox(fee); // Tạo HBox với button
                            setGraphic(hbox); // Đặt HBox vào cột
                        }
                    }
                };
            }
        });
    }

    private HBox createButtonBox(Fee fee) {
        // Tạo các button
        Button editButton = new Button("Sửa");
        Button deleteButton = new Button("Xóa");
        editButton.getStyleClass().add("editbtn");
        deleteButton.getStyleClass().add("deletebtn");
        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
        iconView.setSize("12px");
        iconView.setFill(Color.WHITE);
        editButton.setGraphic(iconView);
        FontAwesomeIconView iconViewtrash = new FontAwesomeIconView(FontAwesomeIcon.REMOVE);
        iconView.setSize("12px");
        iconViewtrash.setFill(Color.WHITE);
        deleteButton.setGraphic(iconViewtrash);

        // Xử lý sự kiện cho button Edit
        editButton.setOnAction(event -> {
            openEdit(fee);
        });

        // Xử lý sự kiện cho button Delete
        deleteButton.setOnAction(event -> {
            delete(fee);
        });

        // Đặt các button vào HBox
        HBox hbox = new HBox(10, editButton, deleteButton); // 10 là khoảng cách giữa các button
        return hbox;
    }


    public void khoiTao(){
        listThang.clear();
        listNam.clear();
        for(int i=1; i< 13; i++){
            listThang.add("Tháng "+i);
        }
        cb_thang.setItems(listThang);
        int month = LocalDate.now().getMonthValue();
        cb_thang.setValue("Tháng "+month);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=year; i > year-10; i--){
            listNam.add(i);
        }
        cb_nam.setItems(listNam);
        cb_nam.setValue(year);
    }

    public void setDoRongCot(){
        col_canho.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_sodien.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_phicanho.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_phiguixe.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_phidiennuoc.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_chucnang.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        col_conlai.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
    }

    public void createServiceFee(Apartment a, Integer month, Integer year){
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setFee(a.getAcreage() * 13000D);
        serviceFee.setApartment(a);
        serviceFee.setCreatedDate(LocalDateTime.now());
        serviceFee.setMonth(month);
        serviceFee.setYear(year);
        serviceFee.setPaidStatus(false);
        serviceFeeRepository.save(serviceFee);
    }

    private void delete(Fee fee) {
        if(Message.confirm("Xóa yêu cầu đóng phí này?") == false){
            return;
        }
        serviceFeeRepository.deleteById(fee.getServiceFee().getId());
        vehicleFeeRepository.deleteById(fee.getVehicleFee().getId());
        utilityBillRepository.deleteById(fee.getUtilityBill().getId());
        fees.removeIf(f -> f.getApartment().getId().equals(fee.getApartment().getId()));
        table.setItems(fees);
    }

    public void createVehicleFee(Apartment a, Integer month, Integer year){
        VehicleFee vehicleFee = new VehicleFee();
        Double fee = 0D;
        VehicleServiceFee phiOTo = vehicleServiceFeeRepository.findById(1L).get();
        VehicleServiceFee phiXeMay = vehicleServiceFeeRepository.findById(2L).get();
        VehicleServiceFee phiXeDap = vehicleServiceFeeRepository.findById(3L).get();
        for(Vehicle v : a.getVehicles()){
            if(v.getVehicleType() == 2){
                fee += phiOTo.getFee();
            }
            if(v.getVehicleType() == 1){
                fee += phiXeMay.getFee();
            }
            if(v.getVehicleType() == 0){
                fee += phiXeDap.getFee();
            }
        }
        vehicleFee.setFee(fee);
        vehicleFee.setApartment(a);
        vehicleFee.setCreatedDate(LocalDateTime.now());
        vehicleFee.setMonth(month);
        vehicleFee.setYear(year);
        vehicleFee.setPaidStatus(false);
        vehicleFeeRepository.save(vehicleFee);
    }

    public void createDNFee(Apartment a, Integer month, Integer year){
        Integer preMonth = month - 1;
        Integer preYear = year;
        if(preMonth == 0){
            preMonth = 12;
            preYear = year - 1;
        }
        UtilityBill ex = utilityBillRepository.findByThangNamAndCanHo(preMonth, preYear, a.getId());
        UtilityBill utilityBill = new UtilityBill();
        utilityBill.setApartment(a);
        utilityBill.setCreatedDate(LocalDateTime.now());
        utilityBill.setMonth(month);
        utilityBill.setYear(year);
        utilityBill.setPaidStatus(false);
        if(ex != null){
            utilityBill.setElectricityIndexPreMonth(ex.getElectricityIndex());
            utilityBill.setWaterIndexPreMonth(ex.getWaterIndex());
        }
        utilityBillRepository.save(utilityBill);
    }

    public void sendMailFee(Apartment a, Double fee, Integer month, Integer year, Integer loaiPhi){
        if(loaiPhi == 1){
            for (Resident r : a.getResidents()){
                mailService.sendAsyncEmail(r.getEmail(), "Thông báo đóng phí căn hộ "+a.getName(),
                        "Phí căn hộ tháng "+month+" năm "+year+" của bạn là "+ formatToVND(fee));
            }
        }
        if(loaiPhi == 2){
            for (Resident r : a.getResidents()){
                mailService.sendAsyncEmail(r.getEmail(), "Thông báo đóng phí gửi xe ",
                        "Phí gửi xe của căn hộ "+ a.getName()+" tháng "+month+" năm "+year+" của bạn là "+ formatToVND(fee));
            }
        }
        if(loaiPhi == 3){
            for (Resident r : a.getResidents()){
                mailService.sendAsyncEmail(r.getEmail(), "Thông báo đóng phí điện nước ",
                        "Phí điện nước của căn hộ "+ a.getName()+" tháng "+month+" năm "+year+" của bạn là "+ formatToVND(fee));
            }
        }
    }

    public static String formatToVND(Double amount) {
        Locale vietnamLocale = new Locale("vi", "VN"); // Định dạng theo Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnamLocale);
        return currencyFormatter.format(amount);
    }

    public void openEdit(Fee fee){
        try {
            // Load child node khác
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fee/updatefee.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            Node node = loader.load();
            UpdateFeeController childController = loader.getController();
            childController.setFrameController(frameController);
            childController.setFee(fee);
            frameController.noidung.getChildren().clear();
            frameController.noidung.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
