package com.app.database;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseService {

    @Autowired
    private DataSource dataSource; // Đảm bảo bạn có cấu hình datasource trong application.properties

    @PostConstruct
    public void init() {
        System.out.println("Checking database connection...");
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}