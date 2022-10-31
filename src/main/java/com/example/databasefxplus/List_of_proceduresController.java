package com.example.databasefxplus;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class List_of_proceduresController {

    private Database database;

    private boolean isClicked = false;

    private ObservableList newVal;

    private final String TABLE_NAME = "list_of_procedures";

    @FXML
    private TextField id;

    @FXML
    private Button btnAddLine;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnFilterByProcedure;

    @FXML
    private TextField in_report_id;

    @FXML
    private TableView<ObservableList> list_of_proceduresGrid;

    @FXML
    private TextField monetary_equivalent;

    @FXML
    private TextField procedure;

    @FXML
    void btn_AddLine_Click(MouseEvent event) {
        if (id.getText().isEmpty() || procedure.getText().isEmpty() || monetary_equivalent.getText().isEmpty()
                || in_report_id.getText().isEmpty()) {
            Exception e = new Exception("Enter all data!");
            database.showAlert(e);
        } else {
            String request = "INSERT INTO ambulance." + TABLE_NAME + "(id, `procedure`, monetary_equivalent, " +
                    "in_report_id) " + "VALUES (" + id.getText() + ", '"
                    + procedure.getText() + "', " + monetary_equivalent.getText() + ", "
                    + in_report_id.getText() + ");";
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
    void btn_FilterByProcedure_Click(MouseEvent event) {
        if (isClicked) {
            database.getLinesFromTable(list_of_proceduresGrid, newVal,
                    TABLE_NAME, "`procedure`", 1);
            isClicked = false;
        } else {
            updateTable(TABLE_NAME);
        }
    }

    @FXML
    void initialize() {
        database = Main.database;
        updateTable(TABLE_NAME);
        list_of_proceduresGrid.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newVal = newValue;
                    isClicked = true;
                });
    }

    private void updateTable(String table_name) {
        database.doRequest(list_of_proceduresGrid, "SELECT * FROM " + table_name);
    }
}
