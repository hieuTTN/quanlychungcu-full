package com.app.controller.repair;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.controller.resident.AddResidentController;
import com.app.entity.RepairRequest;
import com.app.entity.Resident;
import com.app.entity.Vehicle;
import com.app.repository.RepairRequestRepository;
import com.app.repository.VehicleRepository;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class RepairController implements Initializable {

    private FrameController frameController;

    @Autowired
    private ApplicationContext applicationContext;

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @FXML
    private Label lbquanlyhodan;

    @FXML
    private TableView<RepairRequest> table;

    @FXML
    private TableColumn<RepairRequest, Long> col_id;

    @FXML
    private TableColumn<RepairRequest, String> col_ngayyeucau;

    @FXML
    private TableColumn<RepairRequest, String> col_trangthaisua;

    @FXML
    private TableColumn<RepairRequest, String> col_chiphisua;

    @FXML
    private TableColumn<RepairRequest, String> col_thanhtoan;

    @FXML
    private TableColumn<RepairRequest, String> col_mota;

    @FXML
    private TableColumn<RepairRequest, String> col_canho;

    @FXML
    private TableColumn<RepairRequest, HBox> col_chucnang;

    private ObservableList<RepairRequest> repairRequests = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDoRongCot();
        initTable();
    }

    public void initTable() {
        repairRequests.clear();
        List<RepairRequest> all = repairRequestRepository.findAll();
        all.forEach(p->{
            repairRequests.add(p);
        });
        table.setItems(repairRequests);
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_canho.setCellValueFactory(cellData -> {
            RepairRequest repairRequest = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(repairRequest.getApartment().getName()+", Tầng"+repairRequest.getApartment().getFloor());
        });
        col_chiphisua.setCellValueFactory(cellData -> {
            RepairRequest repairRequest = cellData.getValue();
            DecimalFormat decimalFormat = new DecimalFormat("#,###.###");
            String formattedAmount = decimalFormat.format(repairRequest.getFee()) + "đ";
            return new SimpleStringProperty(formattedAmount);
        });
        col_ngayyeucau.setCellValueFactory(cellData -> {
            RepairRequest repairRequest = cellData.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
            String formattedDateTime = repairRequest.getRequestDate().format(formatter);
            return new SimpleStringProperty(formattedDateTime);
        });
        col_trangthaisua.setCellValueFactory(cellData -> {
            RepairRequest repairRequest = cellData.getValue();
            return new SimpleStringProperty(repairRequest.getFixed() == true?"Đã sửa":"Chưa sửa");
        });
        col_thanhtoan.setCellValueFactory(cellData -> {
            RepairRequest repairRequest = cellData.getValue();
            return new SimpleStringProperty(repairRequest.getPaid() == true?"Đã thanh toán":"Chưa thanh toán");
        });

        col_mota.setCellValueFactory(new PropertyValueFactory<>("description"));

        col_mota.setCellFactory(column -> new TableCell<RepairRequest, String>() {
            private final WebView webView = new WebView();

            @Override
            protected void updateItem(String html, boolean empty) {
                super.updateItem(html, empty);
                if (empty || html == null) {
                    setGraphic(null);
                } else {
                    webView.setPrefHeight(100);
                    webView.getEngine().loadContent(html);
                    setGraphic(webView);
                }
            }
        });
        col_chucnang.setCellFactory(new Callback<TableColumn<RepairRequest, HBox>, TableCell<RepairRequest, HBox>>() {
            @Override
            public TableCell<RepairRequest, HBox> call(TableColumn<RepairRequest, HBox> param) {
                return new TableCell<RepairRequest, HBox>() {
                    @Override
                    protected void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null); // Nếu dòng rỗng, không hiển thị gì
                        } else {
                            RepairRequest repairRequest = getTableView().getItems().get(getIndex()); // Lấy item hiện tại
                            HBox hbox = createButtonBox(repairRequest); // Tạo HBox với button
                            setGraphic(hbox); // Đặt HBox vào cột
                        }
                    }
                };
            }
        });
    }

    private HBox createButtonBox(RepairRequest repairRequest) {
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
            edit(repairRequest); // Gọi hàm chỉnh sửa, truyền apartment vào
        });

        // Xử lý sự kiện cho button Delete
        deleteButton.setOnAction(event -> {
            delete(repairRequest.getId()); // Gọi hàm xóa, truyền ID vào
        });

        // Đặt các button vào HBox
        HBox hbox = new HBox(10, editButton, deleteButton); // 10 là khoảng cách giữa các button
        return hbox;
    }

    private void edit(RepairRequest repairRequest) {
        openAddCh(repairRequest.getId());
    }

    private void delete(Long id) {
        if(Message.confirm("Xóa yêu cầu này?") == false){
            return;
        }
        System.out.println("Delete: " + id);
        repairRequestRepository.deleteById(id);
        repairRequests.removeIf(apartment -> apartment.getId().equals(id));
        table.setItems(repairRequests);
    }

    public void setDoRongCot(){
        col_id.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_mota.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_thanhtoan.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_trangthaisua.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_ngayyeucau.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_canho.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_chiphisua.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_chucnang.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
    }

    public void openAddCh(Long id){
        try {
            // Load child node khác
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/repair/updaterepair.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            Node node = loader.load();
            UpdateRepairController childController = loader.getController();
            childController.setFrameController(frameController);
            childController.setId(id);
            frameController.noidung.getChildren().clear();
            frameController.noidung.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
