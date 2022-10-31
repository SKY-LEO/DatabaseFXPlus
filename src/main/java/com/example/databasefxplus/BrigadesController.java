package com.example.databasefxplus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class BrigadesController {

    private Database database;

    private boolean isClicked = false;

    private ObservableList newVal;

    private final String TABLE_NAME = "brigades";

    private final ObservableList<String> AVAILABLE_CHOICES = FXCollections.observableArrayList("общепрофильная",
            "реанимационная", "педиатрическая", "психиатрическая", "интенсивная терапия");

    @FXML
    private TextField brigade_id;

    @FXML
    private ComboBox<String> brigade_profile;

    @FXML
    private TableView<ObservableList> brigadesGrid;

    @FXML
    private Button btnAddLine;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnFilterByDistrict;

    @FXML
    void btn_AddLine_Click(MouseEvent event) {
        if (brigade_id.getText().isEmpty() || brigade_profile.getSelectionModel().isEmpty()) {
            Exception e = new Exception("Enter all data!");
            database.showAlert(e);
        } else {
            String request = "INSERT INTO ambulance." + TABLE_NAME + "(id, brigade_profile) " + "VALUES (" + brigade_id.getText()
                    + ", '" + brigade_profile.getSelectionModel().getSelectedItem() + "');";
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
    void btn_FilterByBrigadeProfile_Click(MouseEvent event) {
        if (isClicked) {
            database.getLinesFromTable(brigadesGrid, newVal,
                    TABLE_NAME, "brigade_profile", 1);
            isClicked = false;
        } else {
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void initialize() {
        database = Main.database;
        setItemsInBox();
        updateTable(TABLE_NAME);
        brigadesGrid.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newVal = newValue;
                    isClicked = true;
                });
    }

    private void setItemsInBox() {
        brigade_profile.getItems().clear();
        brigade_profile.setItems(AVAILABLE_CHOICES);
    }

    private void updateTable(String table_name) {
        database.doRequest(brigadesGrid, "SELECT * FROM " + table_name);
    }
}
