package com.app.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "maintenance")
@Getter
@Setter
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private LocalDateTime createdDate;

    private String title;

    private String content;

    private LocalDate maintenanceDate;

    private String maintenanceTime;

    private LocalDate expectedCompletionDate;

    private String expectedCompletionTime;

    private Boolean completed = false;

    @ManyToOne
    private User createdBy;
}