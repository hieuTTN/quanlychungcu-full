package com.app.controller.apartment;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.entity.Apartment;
import com.app.repository.ApartmentRepository;
import com.app.repository.UserRepository;
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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class ApartmentController implements Initializable {

    private FrameController frameController;

    @Autowired
    private ApplicationContext applicationContext;

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }


    @Autowired
    private ApartmentRepository apartmentRepository;

    @FXML
    private TableView<Apartment> table;

    @FXML
    private TableColumn<Apartment, Long> col_id;

    @FXML
    private TableColumn<Apartment, String> col_name;

    @FXML
    private TableColumn<Apartment, String> col_dt;

    @FXML
    private TableColumn<Apartment, Integer> col_tang;

    @FXML
    private TableColumn<Apartment, String> col_giaban;

    @FXML
    private TableColumn<Apartment, String> col_daban;

    @FXML
    private TableColumn<Apartment, String> col_cudan;

    @FXML
    private TableColumn<Apartment, String> col_soxe;

    @FXML
    private TableColumn<Apartment, HBox> col_chucnang;

    private ObservableList<Apartment> apartments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDoRongCot();
        initTable();
    }

    @FXML
    void openAddCanHo(ActionEvent event) {
        openAddCh(null);
    }

    public void initTable() {
        apartments.clear();
        List<Apartment> all = apartmentRepository.findAll();
        all.forEach(p->{
            apartments.add(p);
        });
        table.setItems(apartments);
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_dt.setCellValueFactory(cellData -> {
            Apartment apartment = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(apartment.getAcreage() + " m2");
        });
        col_tang.setCellValueFactory(new PropertyValueFactory<>("floor"));
        col_giaban.setCellValueFactory(cellData -> {
            Apartment apartment = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(formatToVND(apartment.getPrice()));
        });
        col_daban.setCellValueFactory(cellData -> {
            Apartment apartment = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(apartment.getIsSold() == true?"Đã bán":"Chưa bán");
        });
        col_cudan.setCellValueFactory(cellData -> {
            Apartment apartment = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(String.valueOf(apartment.getResidents().size()));
        });
        col_soxe.setCellValueFactory(cellData -> {
            Apartment apartment = cellData.getValue(); // Lấy đối tượng hiện tại
            return new SimpleStringProperty(String.valueOf(apartment.getVehicles().size()));
        });

        col_chucnang.setCellFactory(new Callback<TableColumn<Apartment, HBox>, TableCell<Apartment, HBox>>() {
            @Override
            public TableCell<Apartment, HBox> call(TableColumn<Apartment, HBox> param) {
                return new TableCell<Apartment, HBox>() {
                    @Override
                    protected void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null); // Nếu dòng rỗng, không hiển thị gì
                        } else {
                            Apartment apartment = getTableView().getItems().get(getIndex()); // Lấy item hiện tại
                            HBox hbox = createButtonBox(apartment); // Tạo HBox với button
                            setGraphic(hbox); // Đặt HBox vào cột
                        }
                    }
                };
            }
        });
    }

    private HBox createButtonBox(Apartment apartment) {
        // Tạo các button
        Button editButton = new Button("Sửa");
        Button deleteButton = new Button("Xóa");
        Button thanhVienButton = new Button("");
        editButton.getStyleClass().add("editbtn");
        thanhVienButton.getStyleClass().add("editbtn");
        deleteButton.getStyleClass().add("deletebtn");
        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
        iconView.setSize("12px");
        iconView.setFill(Color.WHITE);
        editButton.setGraphic(iconView);
        FontAwesomeIconView iconViewtrash = new FontAwesomeIconView(FontAwesomeIcon.REMOVE);
        iconView.setSize("12px");
        iconViewtrash.setFill(Color.WHITE);
        deleteButton.setGraphic(iconViewtrash);
        FontAwesomeIconView iconViewUser = new FontAwesomeIconView(FontAwesomeIcon.EYE);
        iconViewUser.setSize("12px");
        iconViewUser.setFill(Color.WHITE);
        thanhVienButton.setGraphic(iconViewUser);

        // Xử lý sự kiện cho button Edit
        editButton.setOnAction(event -> {
            editApartment(apartment); // Gọi hàm chỉnh sửa, truyền apartment vào
        });

        // Xử lý sự kiện cho button Delete
        deleteButton.setOnAction(event -> {
            deleteApartment(apartment.getId()); // Gọi hàm xóa, truyền ID vào
        });

        thanhVienButton.setOnAction(event -> {
        });

        // Đặt các button vào HBox
        HBox hbox = new HBox(10, editButton, deleteButton, thanhVienButton); // 10 là khoảng cách giữa các button
        return hbox;
    }

    private void editApartment(Apartment apartment) {
        openAddCh(apartment.getId());
    }

    private void deleteApartment(Long id) {
        if(Message.confirm("Xóa căn hộ này?") == false){
            return;
        }
        System.out.println("Delete: " + id);
        apartmentRepository.deleteById(id);
        apartments.removeIf(apartment -> apartment.getId().equals(id));
        table.setItems(apartments);
    }

    public void setDoRongCot(){
        col_id.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_name.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_dt.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_tang.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_giaban.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_daban.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_chucnang.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col_cudan.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col_soxe.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
    }

    public static String formatToVND(Double amount) {
        Locale vietnamLocale = new Locale("vi", "VN"); // Định dạng theo Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnamLocale);
        return currencyFormatter.format(amount);
    }

    public void openAddCh(Long id){
        try {
            // Load child node khác
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/apartment/addapartment.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            Node node = loader.load();
            AddApartmentController childController = loader.getController();
            childController.setFrameController(frameController);
            childController.setId(id);
            frameController.noidung.getChildren().clear();
            frameController.noidung.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
