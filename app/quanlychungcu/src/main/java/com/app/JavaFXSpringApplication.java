package com.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableJpaRepositories("com.app.repository")
public class JavaFXSpringApplication extends Application {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        // Khởi động Spring Boot trước
        applicationContext = SpringApplication.run(JavaFXSpringApplication.class, args);
        launch(args);  // Sau đó mới khởi chạy JavaFX
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/frames.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/account/login.fxml"));
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        String css = getClass().getResource("/view/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("JavaFX with Spring Boot");
        stage.setScene(scene);
        stage.show();
    }
}
