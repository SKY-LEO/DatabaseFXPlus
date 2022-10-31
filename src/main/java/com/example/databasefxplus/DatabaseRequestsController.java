package com.example.databasefxplus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class DatabaseRequestsController {
    Database database;

    @FXML
    private TableView<ObservableList> DatGridDBTables;


    @FXML
    private Button btnConnect;

    @FXML
    private Button btnRequest;

    @FXML
    private Button btnEdit;

    @FXML
    private ComboBox<String> editBox;

    @FXML
    private TableView<ObservableList> datGridSQLResult;

    @FXML
    private Label labDatSource;

    @FXML
    private Label labInitCat;

    @FXML
    private Label labSQLReq;

    @FXML
    private TextField tbDatSource;

    @FXML
    private TextField tbInitCat;

    @FXML
    private TextField tbRequest;

    @FXML
    void btn_Connect_Click(MouseEvent event) throws Exception {
        boolean is_connected = false;
        try {
            database.connectTo(tbDatSource.getText(), tbInitCat.getText());
            is_connected = true;
        } catch (Exception e) {
            database.showAlert(e);
        }
        if (is_connected) {
            tbRequest.setDisable(false);
            btnRequest.setDisable(false);
            btnEdit.setDisable(false);
            datGridSQLResult.setDisable(false);
            DatGridDBTables.setDisable(false);
            setItemsInBox();
            editBox.setDisable(false);
            database.showTables(DatGridDBTables);
        } else {
            tbRequest.setDisable(true);
            btnRequest.setDisable(true);
            btnEdit.setDisable(true);
            datGridSQLResult.setDisable(true);
            DatGridDBTables.setDisable(true);
            editBox.setDisable(true);
        }
    }

    @FXML
    void btn_Request_Click(MouseEvent event) {
        database.doRequest(datGridSQLResult, tbRequest.getText());
    }

    @FXML
    void btn_Edit_Click(MouseEvent event) {
        String selected_item = editBox.getSelectionModel().getSelectedItem();
        System.out.println(selected_item);
        if (selected_item != null) {
            System.out.println("change window");
            Main m = new Main();
            try {
                m.changeScene(selected_item + ".fxml", "Edit " + selected_item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Exception e = new Exception("Please, select table!");
            database.showAlert(e);
        }
    }

    @FXML
    void initialize() {
        database = Main.database;
        if(database.statement != null && database.connection != null){
            tbRequest.setDisable(false);
            btnRequest.setDisable(false);
            btnEdit.setDisable(false);
            datGridSQLResult.setDisable(false);
            DatGridDBTables.setDisable(false);
            setItemsInBox();
            editBox.setDisable(false);
            try {
                database.showTables(DatGridDBTables);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        DatGridDBTables.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> database.showTableDetails(datGridSQLResult, newValue));

    }

    private void setItemsInBox() {
        editBox.getItems().clear();
        ArrayList<String> names_of_tables = database.getNamesOfTables();
        ObservableList<String> availableChoices = FXCollections.observableArrayList(names_of_tables);
        editBox.setItems(availableChoices);
    }

}
