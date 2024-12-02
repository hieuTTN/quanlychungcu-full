package com.app.entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "resident")
@Getter
@Setter
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String email;

    private String username;

    private String password;

    private String fullName;

    private LocalDate bod;

    private String phone;

    private String image;

    // căn cước công dân
    private String cic;

    private Boolean isHouseholdHead = false;

    @ManyToOne
    private Apartment apartment;

    @Transient
    private ImageView imageView;

    public void setImageView() {
        Image image = new Image(getImage());
        imageView = new ImageView(image);
    }
    public ImageView getImageView() {
        imageView.setPreserveRatio(false);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }
}
