package com.app.controller.apartment;

import com.app.controller.FrameController;
import com.app.entity.*;
import com.app.repository.ApartmentRepository;
import com.app.repository.ServiceFeeRepository;
import com.app.repository.UtilityBillRepository;
import com.app.repository.VehicleFeeRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class DetailController implements Initializable {

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
    private UtilityBillRepository utilityBillRepository;

    @Autowired
    private VehicleFeeRepository vehicleFeeRepository;

    private Apartment apartment;

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
        initTableCuDan(apartment.getResidents());
        initTablePt(apartment.getVehicles());
        initTableDv(apartment);
    }

    private ObservableList<Resident> residents = FXCollections.observableArrayList();
    private ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
    private ObservableList<PhiChuaDong> fees = FXCollections.observableArrayList();


    @FXML
    private TableView<Resident> table_cudan;

    @FXML
    private TableColumn<Resident, ImageView> col_anh;

    @FXML
    private TableColumn<Resident, String> col_hoten;

    @FXML
    private TableColumn<Resident, String> col_email;

    @FXML
    private TableColumn<Resident, Date> col_ngaysinh;

    @FXML
    private TableColumn<Resident, String> col_sodienthoai;

    @FXML
    private TableColumn<Resident, String> col_cccd;

    @FXML
    private TableView<Vehicle> table_phuongtien;

    @FXML
    private TableColumn<Vehicle, String> col_bienso;

    @FXML
    private TableColumn<Vehicle, String> col_loaixe;

    @FXML
    private TableView<PhiChuaDong> table_dv;

    @FXML
    private TableColumn<PhiChuaDong, String> col_monthcd;

    @FXML
    private TableColumn<PhiChuaDong, String> col_tenphi;

    @FXML
    private TableColumn<PhiChuaDong, String> col_sotien;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initTableCuDan(List<Resident> all) {
        setDoRongCotCuDan();
        residents.clear();
        all.forEach(p->{
            p.setImageView();
            residents.add(p);
        });
        table_cudan.setItems(residents);
        col_hoten.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        col_ngaysinh.setCellValueFactory(new PropertyValueFactory<>("bod"));
        col_cccd.setCellValueFactory(new PropertyValueFactory<>("cic"));
        col_anh.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        col_email.setCellValueFactory(cellData -> {
            Resident resident = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(resident.getUsername()+"/n"+resident.getEmail());
        });
        col_sodienthoai.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    public void setDoRongCotCuDan(){
        col_anh.prefWidthProperty().bind(table_cudan.widthProperty().multiply(0.2));
        col_hoten.prefWidthProperty().bind(table_cudan.widthProperty().multiply(0.2));
        col_email.prefWidthProperty().bind(table_cudan.widthProperty().multiply(0.2));
        col_ngaysinh.prefWidthProperty().bind(table_cudan.widthProperty().multiply(0.1));
        col_sodienthoai.prefWidthProperty().bind(table_cudan.widthProperty().multiply(0.1));
        col_cccd.prefWidthProperty().bind(table_cudan.widthProperty().multiply(0.2));
    }

    public void initTablePt(List<Vehicle> all) {
        setDoRongCotPt();
        vehicles.clear();
        all.forEach(p->{
            vehicles.add(p);
        });
        table_phuongtien.setItems(vehicles);
        col_bienso.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        col_loaixe.setCellValueFactory(cellData -> {
            Vehicle vehicle = cellData.getValue();
            String loaiXe = "";
            if(vehicle.getVehicleType() == 0){
                loaiXe = "Xe đạp";
            }
            if(vehicle.getVehicleType() == 1){
                loaiXe = "Xe máy";
            }
            if(vehicle.getVehicleType() == 2){
                loaiXe = "Xe ô tô";
            }
            return new SimpleStringProperty(loaiXe);
        });
    }

    public void setDoRongCotPt(){
        col_bienso.prefWidthProperty().bind(table_phuongtien.widthProperty().multiply(0.5));
        col_loaixe.prefWidthProperty().bind(table_phuongtien.widthProperty().multiply(0.5));
    }


    public void initTableDv(Apartment apartment) {
        setDoRongCotDv();
        fees.clear();
        List<ServiceFee> serviceFeeEx = serviceFeeRepository.dichVuChuaDong(apartment.getId());
        List<VehicleFee> vehicleFees = vehicleFeeRepository.dichVuChuaDong(apartment.getId());
        List<UtilityBill> utilityBills = utilityBillRepository.dichVuChuaDong(apartment.getId());
        for(ServiceFee s : serviceFeeEx){
            PhiChuaDong p = new PhiChuaDong();
            p.setFee(formatToVND(s.getFee()));
            p.setMonth(s.getMonth().toString() + " - "+s.getYear().toString());
            p.setName("Phí dịch vụ căn hộ");
            fees.add(p);
        }
        for(VehicleFee s : vehicleFees){
            PhiChuaDong p = new PhiChuaDong();
            p.setFee(formatToVND(s.getFee()));
            p.setMonth(s.getMonth().toString() + " - "+s.getYear().toString());
            p.setName("Phí gửi xe");
            fees.add(p);
        }
        for(UtilityBill s : utilityBills){
            PhiChuaDong p = new PhiChuaDong();
            p.setFee(formatToVND(s.getFee()));
            p.setMonth(s.getMonth().toString() + " - "+s.getYear().toString());
            p.setName("Phí điện - nước");
            fees.add(p);
        }
        table_dv.setItems(fees);
        col_monthcd.setCellValueFactory(new PropertyValueFactory<>("month"));
        col_tenphi.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_sotien.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }

    public void setDoRongCotDv(){
        col_monthcd.prefWidthProperty().bind(table_dv.widthProperty().multiply(0.3));
        col_tenphi.prefWidthProperty().bind(table_dv.widthProperty().multiply(0.4));
        col_sotien.prefWidthProperty().bind(table_dv.widthProperty().multiply(0.3));
    }

    public String formatToVND(Double amount) {
        Locale vietnamLocale = new Locale("vi", "VN"); // Định dạng theo Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnamLocale);
        return currencyFormatter.format(amount);
    }
}
