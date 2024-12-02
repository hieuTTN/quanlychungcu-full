package com.app.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String licensePlate;

    // 0-xe đạp, 1-xe máy, 2-ô tô
    private Integer vehicleType;

    private Integer quantity;

    @ManyToOne
    private Apartment apartment;
}
