package com.app.controller.vehicle;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.entity.Resident;
import com.app.entity.Vehicle;
import com.app.repository.ResidentRepository;
import com.app.repository.VehicleRepository;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class VehicleController implements Initializable {

    private FrameController frameController;

    @Autowired
    private ApplicationContext applicationContext;

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    @Autowired
    private VehicleRepository vehicleRepository;

    @FXML
    private Label lbquanlyhodan;

    @FXML
    private TableView<Vehicle> table;

    @FXML
    private TableColumn<Vehicle, Long> col_id;

    @FXML
    private TableColumn<Vehicle, String> col_bienso;

    @FXML
    private TableColumn<Vehicle, String> col_loaixe;

    @FXML
    private TableColumn<Vehicle, String> col_canho;

    @FXML
    private TableColumn<Vehicle, HBox> col_chucnang;

    private ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDoRongCot();
        initTable();
    }

    public void initTable() {
        vehicles.clear();
        List<Vehicle> all = vehicleRepository.findAll();
        all.forEach(p->{
            vehicles.add(p);
        });
        table.setItems(vehicles);
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_bienso.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        col_canho.setCellValueFactory(cellData -> {
            Vehicle vehicle = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(vehicle.getApartment().getName()+", Tầng"+vehicle.getApartment().getFloor());
        });
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
        col_chucnang.setCellFactory(new Callback<TableColumn<Vehicle, HBox>, TableCell<Vehicle, HBox>>() {
            @Override
            public TableCell<Vehicle, HBox> call(TableColumn<Vehicle, HBox> param) {
                return new TableCell<Vehicle, HBox>() {
                    @Override
                    protected void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null); // Nếu dòng rỗng, không hiển thị gì
                        } else {
                            Vehicle vehicle = getTableView().getItems().get(getIndex()); // Lấy item hiện tại
                            HBox hbox = createButtonBox(vehicle); // Tạo HBox với button
                            setGraphic(hbox); // Đặt HBox vào cột
                        }
                    }
                };
            }
        });
    }

    private HBox createButtonBox(Vehicle vehicle) {
        // Tạo các button
        Button deleteButton = new Button("Xóa");
        deleteButton.getStyleClass().add("deletebtn");
        FontAwesomeIconView iconViewtrash = new FontAwesomeIconView(FontAwesomeIcon.REMOVE);
        iconViewtrash.setSize("12px");
        iconViewtrash.setFill(Color.WHITE);
        deleteButton.setGraphic(iconViewtrash);


        // Xử lý sự kiện cho button Delete
        deleteButton.setOnAction(event -> {
            delete(vehicle.getId()); // Gọi hàm xóa, truyền ID vào
        });

        // Đặt các button vào HBox
        HBox hbox = new HBox(10, deleteButton); // 10 là khoảng cách giữa các button
        return hbox;
    }


    private void delete(Long id) {
        if(Message.confirm("Xóa phương tiện này?") == false){
            return;
        }
        System.out.println("Delete: " + id);
        vehicleRepository.deleteById(id);
        vehicles.removeIf(apartment -> apartment.getId().equals(id));
        table.setItems(vehicles);
    }

    public void setDoRongCot(){
        col_id.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_bienso.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_canho.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_loaixe.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_chucnang.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
    }
}
