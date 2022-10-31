package com.example.databasefxplus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class Main extends Application {

    private static Stage stage;
    static Database database = new Database();

    @Override
    public void start(Stage primary_stage) throws IOException {
        stage = primary_stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("database-requests.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 495);
        primary_stage.setTitle("Data Base Requests");
        primary_stage.setScene(scene);
        primary_stage.show();
    }

    @Override
    public void stop() throws SQLException {
        database.disconnect();
    }

    public static void main(String[] args) {
        launch();
    }

    public void changeScene(String fxml, String stageTitle) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), 550, 495);
        stage.setScene(scene);
        stage.setTitle(stageTitle);
    }
}