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

public class EmployeesController {

    private Database database;

    private boolean isClicked = false;

    private ObservableList newVal;

    private final String TABLE_NAME = "employees";

    private final ObservableList<String> JOB_TITLE_CHOICES = FXCollections.observableArrayList("врач", "фельдшер");

    private final ObservableList<String> SPECIALTY_CHOICES = FXCollections.observableArrayList("врач-педиатр",
            "врач-анестезиолог", "врач-кардиолог", "врач-психиатр");

    @FXML
    private TextField bounty;

    @FXML
    private Button btnAddLine;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnFilterByInBrigadeId;

    @FXML
    private Button btnFilterByJobTitle;

    @FXML
    private Button btnFilterBySpecialty;

    @FXML
    private TableView<ObservableList> employeeGrid;

    @FXML
    private TextField id;

    @FXML
    private TextField in_brigade_id;

    @FXML
    private ComboBox<String> job_title;

    @FXML
    private TextField name;

    @FXML
    private TextField patronymic;

    @FXML
    private ComboBox<String> specialty;

    @FXML
    private TextField surname;

    @FXML
    private TextField telephone_num;

    @FXML
    void btn_AddLine_Click(MouseEvent event) {
        if (id.getText().isEmpty() || surname.getText().isEmpty() || name.getText().isEmpty()
                || job_title.getSelectionModel().isEmpty() || in_brigade_id.getText().isEmpty()
                || telephone_num.getText().isEmpty()) {
            Exception e = new Exception("Enter all data!");
            database.showAlert(e);
        } else {
            String request = "INSERT INTO ambulance." + TABLE_NAME + "(id, surname, name, patronymic, job_title, specialty," +
                    " in_brigade_id, telephone_num, bounty) " + "VALUES (" + id.getText() + ", '"
                    + surname.getText() + "', '" + name.getText() + "', '" + patronymic.getText() + "', '"
                    + job_title.getSelectionModel().getSelectedItem() + "', '"
                    + specialty.getSelectionModel().getSelectedItem() + "', " + in_brigade_id.getText() + ", '"
                    + telephone_num.getText() + "', '" + bounty.getText() + "');";
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
            database.getLinesFromTable(employeeGrid, newVal,
                    TABLE_NAME, "in_brigade_id", 6);
            isClicked = false;
        } else {
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void btn_FilterByJobTitle_Click(MouseEvent event) {
        if (isClicked) {
            database.getLinesFromTable(employeeGrid, newVal,
                    TABLE_NAME, "job_title", 4);
            isClicked = false;
        } else {
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void btn_FilterBySpecialty_Click(MouseEvent event) {
        if (isClicked) {
            database.getLinesFromTable(employeeGrid, newVal,
                    TABLE_NAME, "specialty", 5);
            isClicked = false;
        } else {
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void initialize() {
        database = Main.database;
        setItemsInBox(job_title, JOB_TITLE_CHOICES);
        setItemsInBox(specialty, SPECIALTY_CHOICES);
        updateTable(TABLE_NAME);
        employeeGrid.getSelectionModel().selectedItemProperty().addListener(
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
        database.doRequest(employeeGrid, "SELECT * FROM " + table_name);
    }
}

