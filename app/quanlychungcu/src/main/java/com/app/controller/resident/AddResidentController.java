package com.app.controller.resident;

import com.app.controller.FrameController;
import com.app.controller.Message;
import com.app.controller.apartment.ApartmentController;
import com.app.entity.Apartment;
import com.app.entity.Resident;
import com.app.repository.ApartmentRepository;
import com.app.repository.ResidentRepository;
import com.app.service.CloudinaryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@Component
public class AddResidentController implements Initializable {

    private FrameController frameController;

    private Long id;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    private ObservableList<Apartment> apartments = FXCollections.observableArrayList();

    public void setFrameController(FrameController frameController) {
        this.frameController = frameController;
    }

    public void setId(Long id) {
        this.id = id;
        if(id != null){
            Resident resident = residentRepository.findById(id).get();
            txt_username.setText(resident.getUsername());
            txt_ngaysinh.setValue(resident.getBod());
            txt_hoten.setText(resident.getFullName());
            txt_matkhau.setText(resident.getPassword());
            txt_sdt.setText(resident.getPhone());
            txt_cccd.setText(resident.getCic());
            cbChonCanHo.setValue(resident.getApartment());
            anh.setImage(new Image(resident.getImage()));
            linkAnh = resident.getImage();
            txt_email.setText(resident.getEmail());
            checkChuHo.setSelected(resident.getIsHouseholdHead());
        }
    }

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_username;

    @FXML
    private PasswordField txt_matkhau;

    @FXML
    private TextField txt_hoten;

    @FXML
    private DatePicker txt_ngaysinh;

    @FXML
    private TextField txt_sdt;

    @FXML
    private TextField txt_cccd;

    @FXML
    private CheckBox checkChuHo;

    @FXML
    private ComboBox<Apartment> cbChonCanHo;

    @FXML
    private Button btnadd;

    @FXML
    private ImageView anh;

    private String linkAnh = null;

    @FXML
    void addCuDan(ActionEvent event) {
        if(checkInput()){
            Resident resident = new Resident();
            if(id != null){
                resident = residentRepository.findById(id).get();
                if(residentRepository.findByUserNameAndId(txt_username.getText(), id) != null){
                    Message.getMess("Tên đăng nhập đã được dùng cho tài khoản khác!", Alert.AlertType.WARNING);
                    return;
                }
            }
            else{
                if(residentRepository.findByUserName(txt_username.getText()) != null){
                    Message.getMess("Tên đăng nhập đã được dùng cho tài khoản khác!", Alert.AlertType.WARNING);
                    return;
                }
            }
            resident.setImage(linkAnh);
            resident.setBod(txt_ngaysinh.getValue());
            resident.setEmail(txt_email.getText());
            resident.setCic(txt_cccd.getText());
            resident.setUsername(txt_username.getText());
            if(txt_username.getText().equals("")){
                resident.setUsername(txt_email.getText());
            }
            resident.setFullName(txt_hoten.getText());
            resident.setIsHouseholdHead(checkChuHo.isSelected());
            resident.setPassword(txt_matkhau.getText());
            resident.setApartment(cbChonCanHo.getValue());
            resident.setPhone(txt_sdt.getText());
            residentRepository.save(resident);
            Message.getMess("Thêm cư dân thành công", Alert.AlertType.INFORMATION);
            openCuDan();
        }
    }

    @FXML
    void openChonAnh(MouseEvent event) throws MalformedURLException {
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if (f != null) {
            String s = f.getName();
            Path urlFile = f.toPath();
            String link = urlFile.toString();
            File file = new File(link);
            String path = file.toURI().toURL().toString();
            Image image = new Image(path);
            this.anh.setImage(image);
            if(file.exists()){
                linkAnh = cloudinaryService.uploadFile(file);
                System.out.println(linkAnh);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Apartment> all = apartmentRepository.findAll();
        all.forEach(p->{
            apartments.add(p);
        });
        cbChonCanHo.setItems(apartments);
    }

    public Boolean checkInput(){
        if(txt_hoten.getText().equals("") || txt_ngaysinh.getValue() == null){
            Message.getMess("Họ tên và ngày sinh không được bỏ trống!", Alert.AlertType.WARNING);
            return false;
        }
        if(cbChonCanHo.getValue() == null){
            Message.getMess("Hãy chọn căn hộ!", Alert.AlertType.WARNING);
            return false;
        }
        if(linkAnh == null){
            Message.getMess("Hãy chọn ảnh", Alert.AlertType.WARNING);
            return false;
        }
        if(checkChuHo.isSelected() == true){
            if(txt_email.getText().equals("") || txt_username.getText().equals("") || txt_matkhau.getText().equals("")){
                Message.getMess("Chủ hộ phải có email và tên đăng nhập, mật khẩu!", Alert.AlertType.WARNING);
                return false;
            }
        }
        if(!txt_cccd.getText().equals("")){
            if(isValidSo(txt_cccd.getText()) == false){
                Message.getMess("Căn cước công dân chỉ chứa ký tự từ 0-9!", Alert.AlertType.WARNING);
                return false;
            }
            if(txt_cccd.getText().length() < 9 || txt_cccd.getText().length() > 12){
                Message.getMess("Căn cước công dân chỉ có độ dài từ 9-12 ký tự", Alert.AlertType.WARNING);
                return false;
            }
        }
        if(!txt_sdt.getText().equals("")){
            if(isValidSo(txt_sdt.getText()) == false){
                Message.getMess("Số điện thoại chỉ chứa ký tự từ 0-9!", Alert.AlertType.WARNING);
                return false;
            }
            if(txt_sdt.getText().length() < 10 || txt_sdt.getText().length() > 11){
                Message.getMess("Số điện thoại chỉ có độ dài từ 10-11 ký tự", Alert.AlertType.WARNING);
                return false;
            }
        }
        return true;
    }
    public boolean isValidSo(String str) {
        String regex = "^\\d+$";
        return Pattern.matches(regex, str);
    }
    void openCuDan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/resident/resident.fxml"));
            loader.setControllerFactory(applicationContext::getBean);  // Dùng Spring để tạo controller
            VBox node = loader.load();
            ResidentController childController = loader.getController();
            childController.setFrameController(frameController);
            frameController.noidung.getChildren().clear();
            frameController.noidung.getChildren().add(node);
            node.prefWidthProperty().bind(frameController.noidung.widthProperty());
            node.prefHeightProperty().bind(frameController.noidung.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
