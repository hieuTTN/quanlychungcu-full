package com.app.controller.resident;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.controller.apartment.AddApartmentController;
import com.app.entity.Apartment;
import com.app.entity.Resident;
import com.app.repository.ApartmentRepository;
import com.app.repository.ResidentRepository;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ResidentController implements Initializable {

    private FrameController frameController;

    @Autowired
    private ApplicationContext applicationContext;

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    @Autowired
    private ResidentRepository residentRepository;

    @FXML
    private Label lbquanlyhodan;

    @FXML
    private TableView<Resident> table;

    @FXML
    private TableColumn<Resident, Long> col_id;

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
    private TableColumn<Resident, String> col_canho;

    @FXML
    private TableColumn<Resident, HBox> col_chucnang;

    private ObservableList<Resident> residents = FXCollections.observableArrayList();

    @FXML
    void openAddHoDan(ActionEvent event) {
        openAddCh(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDoRongCot();
        initTable();
    }

    public void initTable() {
        residents.clear();
        List<Resident> all = residentRepository.findAll();
        all.forEach(p->{
            p.setImageView();
            residents.add(p);
        });
        table.setItems(residents);
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_hoten.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        col_ngaysinh.setCellValueFactory(new PropertyValueFactory<>("bod"));
        col_cccd.setCellValueFactory(new PropertyValueFactory<>("cic"));
        col_anh.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        col_email.setCellValueFactory(cellData -> {
            Resident resident = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(resident.getUsername()+"/n"+resident.getEmail());
        });
        col_sodienthoai.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_canho.setCellValueFactory(cellData -> {
            Resident resident = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(resident.getApartment().getName()+"/nTầng: "+resident.getApartment().getFloor());
        });
        col_chucnang.setCellFactory(new Callback<TableColumn<Resident, HBox>, TableCell<Resident, HBox>>() {
            @Override
            public TableCell<Resident, HBox> call(TableColumn<Resident, HBox> param) {
                return new TableCell<Resident, HBox>() {
                    @Override
                    protected void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null); // Nếu dòng rỗng, không hiển thị gì
                        } else {
                            Resident resident = getTableView().getItems().get(getIndex()); // Lấy item hiện tại
                            HBox hbox = createButtonBox(resident); // Tạo HBox với button
                            setGraphic(hbox); // Đặt HBox vào cột
                        }
                    }
                };
            }
        });
    }

    private HBox createButtonBox(Resident resident) {
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
            editApartment(resident); // Gọi hàm chỉnh sửa, truyền apartment vào
        });

        // Xử lý sự kiện cho button Delete
        deleteButton.setOnAction(event -> {
            deleteApartment(resident.getId()); // Gọi hàm xóa, truyền ID vào
        });

        // Đặt các button vào HBox
        HBox hbox = new HBox(10, editButton, deleteButton); // 10 là khoảng cách giữa các button
        return hbox;
    }

    private void editApartment(Resident resident) {
        openAddCh(resident.getId());
    }

    private void deleteApartment(Long id) {
        if(Message.confirm("Xóa căn hộ này?") == false){
            return;
        }
        System.out.println("Delete: " + id);
        residentRepository.deleteById(id);
        residents.removeIf(apartment -> apartment.getId().equals(id));
        table.setItems(residents);
    }

    public void setDoRongCot(){
        col_id.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_anh.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_hoten.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_email.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_ngaysinh.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_sodienthoai.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_cccd.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_canho.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_chucnang.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
    }

    public void openAddCh(Long id){
        try {
            // Load child node khác
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/resident/addresident.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            Node node = loader.load();
            AddResidentController childController = loader.getController();
            childController.setFrameController(frameController);
            childController.setId(id);
            frameController.noidung.getChildren().clear();
            frameController.noidung.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
