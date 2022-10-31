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

public class Info_about_sicksController {

    private Database database;

    private boolean isClicked = false;

    private ObservableList newVal;

    private final String TABLE_NAME = "info_about_sicks";

    private final ObservableList<String> SOCIAL_STATUS_CHOICES = FXCollections.observableArrayList("рабочий",
            "пенсионер", "студент", "служащий", "школьник");

    @FXML
    private TextField address;

    @FXML
    private TextField birth_date;

    @FXML
    private Button btnAddLine;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnFilterBySocialStatus;

    @FXML
    private TextField id;

    @FXML
    private TableView<ObservableList> info_about_sicksGrid;

    @FXML
    private TextField name;

    @FXML
    private TextField patronymic;

    @FXML
    private ComboBox<String> social_status;

    @FXML
    private TextField surname;

    @FXML
    private TextField telephone_num;

    @FXML
    void btn_AddLine_Click(MouseEvent event) {
        if (id.getText().isEmpty() || surname.getText().isEmpty() || name.getText().isEmpty()
                || social_status.getSelectionModel().isEmpty() || telephone_num.getText().isEmpty()
                || address.getText().isEmpty() || birth_date.getText().isEmpty()) {
            Exception e = new Exception("Enter all data!");
            database.showAlert(e);
        } else {
            String request = "INSERT INTO ambulance." + TABLE_NAME + "(id, surname, name, patronymic, telephone_num, " +
                    "address, birth_date, social_status) " + "VALUES (" + id.getText() + ", '"
                    + surname.getText() + "', '" + name.getText() + "', '" + patronymic.getText() + "', '"
                    + telephone_num.getText() + "', '" + address.getText() + "', "
                    + birth_date.getText() + ", '"
                    + social_status.getSelectionModel().getSelectedItem() + "');";
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
    void btn_FilterBySpecialty_Click(MouseEvent event) {
        if (isClicked) {
            database.getLinesFromTable(info_about_sicksGrid, newVal,
                    TABLE_NAME, "social_status", 7);
            isClicked = false;
        } else {
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void initialize() {
        database = Main.database;
        setItemsInBox(social_status, SOCIAL_STATUS_CHOICES);
        updateTable(TABLE_NAME);
        info_about_sicksGrid.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newVal = newValue;
                    isClicked = true;
                });
    }

    private void setItemsInBox(ComboBox<String> comboBox, ObservableList<String> choices) {
        comboBox.getItems().clear();
        comboBox.setItems(choices);
    }

    private void updateTable(String table_name) {
        database.doRequest(info_about_sicksGrid, "SELECT * FROM " + table_name);
    }
}

