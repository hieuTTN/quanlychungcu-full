package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.swing.text.html.ImageView;
import java.awt.*;
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

}
