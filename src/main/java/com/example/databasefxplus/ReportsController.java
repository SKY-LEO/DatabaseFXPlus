package com.example.databasefxplus;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ReportsController {

    private Database database;

    private boolean isClicked = false;

    private ObservableList newVal;

    private final String TABLE_NAME = "reports";

    @FXML
    private Button btnAddLine;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnFilterByInBrigadeId;

    @FXML
    private TextField id;

    @FXML
    private TextField in_brigade_id;

    @FXML
    private TableView<ObservableList> reportsGrid;

    @FXML
    void btn_AddLine_Click(MouseEvent event) {
        if (id.getText().isEmpty() || in_brigade_id.getText().isEmpty()) {
            Exception e = new Exception("Enter all data!");
            database.showAlert(e);
        } else {
            String request = "INSERT INTO ambulance." + TABLE_NAME + "(id, brigade_id) " + "VALUES ("
                    + id.getText() + ", " + in_brigade_id.getText() + ");";
            database.addToTableRequest(request);
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void btn_Back_Click(MouseEvent event) {
        Main m = new Main();
        try {
            m.changeScene("database-requests.fxml", "Data Base Requests");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btn_FilterByInBrigadeId_Click(MouseEvent event) {
        if (isClicked) {
            database.getLinesFromTable(reportsGrid, newVal,
                    TABLE_NAME, "brigade_id", 1);
            isClicked = false;
        } else {
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void initialize() {
        database = Main.database;
        updateTable(TABLE_NAME);
        reportsGrid.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newVal = newValue;
                    isClicked = true;
                });
    }

    private void updateTable(String table_name) {
        database.doRequest(reportsGrid, "SELECT * FROM " + table_name);
    }
}
