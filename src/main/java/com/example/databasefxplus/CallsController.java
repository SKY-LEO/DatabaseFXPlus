package com.example.databasefxplus;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class CallsController {

    private Database database;

    private boolean isClicked = false;

    private ObservableList newVal;

    private final String TABLE_NAME = "calls";

    @FXML
    private TextField ambulance_substation_id;

    @FXML
    private Button btnAddLine;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnFilterByAmbulanceId;

    @FXML
    private Button btnFilterByDate;

    @FXML
    private TableView<ObservableList> callsGrid;

    @FXML
    private TextField cause;

    @FXML
    private TextField date;

    @FXML
    private TextField id;

    @FXML
    private TextField report_id;

    @FXML
    private TextField sick_info_id;

    @FXML
    private TextField time;

    @FXML
    void btn_AddLine_Click(MouseEvent event) {
        if (id.getText().isEmpty() || ambulance_substation_id.getText().isEmpty() || time.getText().isEmpty()
                || date.getText().isEmpty() || cause.getText().isEmpty() || report_id.getText().isEmpty()
                || sick_info_id.getText().isEmpty()) {
            Exception e = new Exception("Enter all data!");
            database.showAlert(e);
        } else {
            String request = "INSERT INTO ambulance." + TABLE_NAME + "(id, ambulance_substation_id, time, date, cause, "
                    + "report_id, sick_info_id) " + "VALUES (" + id.getText() + ", "
                    + ambulance_substation_id.getText() + ", " + time.getText() + ", " +
                    date.getText() + ", '" + cause.getText() + "', " + report_id.getText() +
                    ", " + sick_info_id.getText() + ");";
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
    void btn_FilterByAmbulanceId_Click(MouseEvent event) {
        if (isClicked) {
            database.getLinesFromTable(callsGrid, newVal,
                    TABLE_NAME, "ambulance_substation_id", 1);
            isClicked = false;
        } else {
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void btn_FilterByDate_Click(MouseEvent event) {
        if (isClicked) {
            database.getLinesFromTable(callsGrid, newVal,
                    TABLE_NAME, "date", 3);
            isClicked = false;
        } else {
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void initialize() {
        database = Main.database;
        updateTable(TABLE_NAME);
        callsGrid.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newVal = newValue;
                    isClicked = true;
                });
    }

    private void updateTable(String table_name) {
        database.doRequest(callsGrid, "SELECT * FROM " + table_name);
    }
}

