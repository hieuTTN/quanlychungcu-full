module com.app.JavaFXSpringApplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires spring.core;
    requires spring.context;
    requires spring.boot;
    requires spring.data.jpa;
    requires spring.jdbc;
    requires spring.boot.autoconfigure;
    requires lombok;
//    requires static lombok;
    requires java.persistence;
    requires java.annotation;
    requires spring.beans;
    requires org.hibernate.orm.core;
    requires de.jensd.fx.glyphs.fontawesome;
    requires cloudinary.core;
    requires cloudinary.http44;
    requires com.jfoenix;
    requires javafx.web;
    requires javafx.graphics ;
    requires jakarta.mail;
    requires spring.boot.starter.mail;

    opens com.app;
    opens com.app.controller;
    opens com.app.entity;
    opens com.app.database;
    opens com.app.repository;
    exports com.app;
    exports com.app.controller;
    exports com.app.service;
    exports com.app.controller.apartment;
    exports com.app.controller.resident;
    exports com.app.controller.account;
    exports com.app.controller.maintenance;
    opens com.app.controller.apartment;
    opens com.app.controller.account;
    opens com.app.controller.resident;
    opens com.app.controller.maintenance;
    opens com.app.service;
}
