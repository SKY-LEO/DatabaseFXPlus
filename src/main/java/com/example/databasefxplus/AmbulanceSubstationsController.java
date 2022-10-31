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

public class AmbulanceSubstationsController {

    private Database database;

    private boolean isClicked = false;

    private ObservableList newVal;

    private final String TABLE_NAME = "ambulance_substations";

    private final ObservableList<String> AVAILABLE_CHOICES = FXCollections.observableArrayList("Центральный", "Советский",
            "Первомайский", "Партизанский", "Заводской", "Ленинский", "Октябрьский", "Московский", "Фрунзенский");

    @FXML
    private TextField address;

    @FXML
    private TableView<ObservableList> ambulance_substationsGrid;

    @FXML
    private Button btnAddLine;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnFilterByDistrict;

    @FXML
    private TextField num_of_employees;

    @FXML
    private ComboBox<String> select_district;

    @FXML
    private TextField substation_num;

    @FXML
    private TextField telephone_num;

    @FXML
    void btn_AddLine_Click(MouseEvent event) {
        if (address.getText().isEmpty() || num_of_employees.getText().isEmpty() || substation_num.getText().isEmpty()
                || telephone_num.getText().isEmpty() || select_district.getSelectionModel().isEmpty()) {
            Exception e = new Exception("Enter all data!");
            database.showAlert(e);
        } else {
            String request = "INSERT INTO ambulance." + TABLE_NAME + "(substation_num, address, " +
                    "telephone_num, district_of_city, num_of_employees) " + "VALUES (" + substation_num.getText() + ", '"
                    + address.getText() + "', '" + telephone_num.getText() + "', '" +
                    select_district.getSelectionModel().getSelectedItem() + "', " + num_of_employees.getText() + ");";
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
    void btn_FilterByDistrict_Click(MouseEvent event) {
        if (isClicked) {
            database.getLinesFromTable(ambulance_substationsGrid, newVal,
                    TABLE_NAME, "district_of_city", 3);
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
        ambulance_substationsGrid.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newVal = newValue;
                    isClicked = true;
                });
    }

    private void setItemsInBox() {
        select_district.getItems().clear();
        select_district.setItems(AVAILABLE_CHOICES);
    }

    private void updateTable(String table_name) {
        database.doRequest(ambulance_substationsGrid, "SELECT * FROM " + table_name);
    }
}


