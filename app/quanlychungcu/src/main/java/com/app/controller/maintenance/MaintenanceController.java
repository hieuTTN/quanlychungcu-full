package com.app.controller.maintenance;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.controller.apartment.AddApartmentController;
import com.app.entity.Apartment;
import com.app.entity.Maintenance;
import com.app.entity.Resident;
import com.app.repository.ApartmentRepository;
import com.app.repository.MaintenanceRepository;
import com.app.repository.ResidentRepository;
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
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class MaintenanceController implements Initializable {

    private FrameController frameController;

    @Autowired
    private ApplicationContext applicationContext;

    private MailService mailService = new MailService();

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }


    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @FXML
    private TableView<Maintenance> table;

    @FXML
    private TableColumn<Maintenance, Long> col_id;

    @FXML
    private TableColumn<Maintenance, String> col_tieude;

    @FXML
    private TableColumn<Maintenance, String> col_ngaysua;

    @FXML
    private TableColumn<Maintenance, String> col_ngaydukienht;

    @FXML
    private TableColumn<Maintenance, String> col_nguoitao;

    @FXML
    private TableColumn<Maintenance, String> col_tinhtrang;

    @FXML
    private TableColumn<Maintenance, HBox> col_chucnang;

    private ObservableList<Maintenance> maintenances = FXCollections.observableArrayList();

    @FXML
    void openAddBaoTri(ActionEvent event) {
        openAddBt(null);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDoRongCot();
        initTable();
    }

    public void initTable() {
        maintenances.clear();
        List<Maintenance> all = maintenanceRepository.findAll();
        all.forEach(p->{
            maintenances.add(p);
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


        table.setItems(maintenances);
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_tieude.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_tinhtrang.setCellValueFactory(cellData -> {
            Maintenance maintenance = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(maintenance.getCompleted() == true?"Đã hoàn tất" : " Đang sửa chữa");
        });
        col_ngaysua.setCellValueFactory(cellData -> {
            Maintenance maintenance = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(maintenance.getMaintenanceTime()+", "+maintenance.getMaintenanceDate().format(formatter));
        });
        col_ngaydukienht.setCellValueFactory(cellData -> {
            Maintenance maintenance = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(maintenance.getExpectedCompletionTime()+", "+maintenance.getExpectedCompletionDate().format(formatter));
        });
        col_nguoitao.setCellValueFactory(cellData -> {
            Maintenance maintenance = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(maintenance.getCreatedBy() == null?"":maintenance.getCreatedBy().getFullName());
        });

        col_chucnang.setCellFactory(new Callback<TableColumn<Maintenance, HBox>, TableCell<Maintenance, HBox>>() {
            @Override
            public TableCell<Maintenance, HBox> call(TableColumn<Maintenance, HBox> param) {
                return new TableCell<Maintenance, HBox>() {
                    @Override
                    protected void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null); // Nếu dòng rỗng, không hiển thị gì
                        } else {
                            Maintenance maintenance = getTableView().getItems().get(getIndex()); // Lấy item hiện tại
                            HBox hbox = createButtonBox(maintenance); // Tạo HBox với button
                            setGraphic(hbox); // Đặt HBox vào cột
                        }
                    }
                };
            }
        });
    }

    private HBox createButtonBox(Maintenance maintenance) {
        // Tạo các button
        Button editButton = new Button("Sửa");
        Button deleteButton = new Button("Xóa");
        Button mailButton = new Button("Gửi");

        editButton.getStyleClass().add("editbtn");
        deleteButton.getStyleClass().add("deletebtn");
        mailButton.getStyleClass().add("editbtn");

        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
        iconView.setSize("12px");
        iconView.setFill(Color.WHITE);
        editButton.setGraphic(iconView);
        FontAwesomeIconView iconViewtrash = new FontAwesomeIconView(FontAwesomeIcon.REMOVE);
        iconView.setSize("12px");
        iconViewtrash.setFill(Color.WHITE);
        deleteButton.setGraphic(iconViewtrash);
        FontAwesomeIconView iconViewMail = new FontAwesomeIconView(FontAwesomeIcon.MAIL_REPLY);
        iconViewMail.setSize("12px");
        iconViewMail.setFill(Color.WHITE);
        mailButton.setGraphic(iconViewMail);

        // Xử lý sự kiện cho button Edit
        editButton.setOnAction(event -> {
            editApartment(maintenance); // Gọi hàm chỉnh sửa, truyền apartment vào
        });

        // Xử lý sự kiện cho button Delete
        deleteButton.setOnAction(event -> {
            delete(maintenance.getId()); // Gọi hàm xóa, truyền ID vào
        });

        mailButton.setOnAction(event -> {
            sendMail(maintenance); // Gọi hàm xóa, truyền ID vào
        });
        // Đặt các button vào HBox
        HBox hbox = new HBox(10, editButton, deleteButton, mailButton); // 10 là khoảng cách giữa các button
        return hbox;
    }

    private void delete(Long id) {
        if(Message.confirm("Xóa bảo trì này?") == false){
            return;
        }
        System.out.println("Delete: " + id);
        maintenanceRepository.deleteById(id);
        maintenances.removeIf(apartment -> apartment.getId().equals(id));
        table.setItems(maintenances);
    }

    public void sendMail(Maintenance maintenance){
        if(Message.confirm("Xác nhận gửi lại mail thông báo?") == false){
            return;
        }
        List<Resident> residents = residentRepository.findAll();
        residents.forEach(p->{
            mailService.sendAsyncEmail(p.getEmail(), maintenance.getTitle(), maintenance.getContent());
        });
        Message.getMess("Gửi mail thành công", Alert.AlertType.INFORMATION);
    }

    private void editApartment(Maintenance maintenance) {
        openAddBt(maintenance.getId());
    }

    public void setDoRongCot(){
        col_id.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_ngaydukienht.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_ngaysua.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_nguoitao.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_tieude.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_chucnang.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_tinhtrang.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
    }

    public void openAddBt(Long id){
        try {
            // Load child node khác
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/maintenance/addmaintenance.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            Node node = loader.load();
            AddMaintenanceController childController = loader.getController();
            childController.setFrameController(frameController);
            childController.setId(id);
            frameController.noidung.getChildren().clear();
            frameController.noidung.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
