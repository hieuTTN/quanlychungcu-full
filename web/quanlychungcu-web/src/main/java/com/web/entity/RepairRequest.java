package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "repair_request")
@Getter
@Setter
public class RepairRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Boolean fixed = false;

    private Double fee;

    private Boolean paid = false;

    private LocalDateTime requestDate;

    private Boolean canceled;

    private LocalDateTime cancelDate;

    @ManyToOne
    private Apartment apartment;

}
