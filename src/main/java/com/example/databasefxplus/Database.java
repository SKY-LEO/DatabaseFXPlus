package com.example.databasefxplus;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

public class Database {
    Connection connection = null;
    Statement statement = null;

    void showTableDetails(TableView<ObservableList> datGridSQLResult, ObservableList list) {
        Optional optional = list.stream().findFirst();
        if (optional.isPresent()) {
            System.out.println(optional.get());
            String request = "SELECT * FROM " + optional.get();
            doRequest(datGridSQLResult, request);
        }
    }

    void getLinesFromTable(TableView<ObservableList> table, ObservableList list, String table_name, String column_name,
                           int index) {
        String string = list.get(index).toString();
        if (!string.isEmpty()) {
            System.out.println(string);
            String request = "SELECT * FROM " + table_name + " WHERE " + column_name + " = '" + string + "';";
            doRequest(table, request);
        }
    }

    void showTables(TableView<ObservableList> DatGridDBTables) throws SQLException {
        DatGridDBTables.getColumns().clear();
        DatGridDBTables.getItems().clear();
        ObservableList<ObservableList> data_line = FXCollections.observableArrayList();
        String tableName;
        String columnNames;
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String[] types = {"TABLE"};
        ResultSet resultSet = databaseMetaData.getTables(null, null, "%", types);
        TableColumn column = new TableColumn("Table Name");
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        DatGridDBTables.getColumns().addAll(column);
        TableColumn column1 = new TableColumn("Field Names in Tables");
        column1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(1).toString());
            }
        });
        DatGridDBTables.getColumns().addAll(column1);
        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            tableName = resultSet.getString("TABLE_NAME");
            row.add(tableName);
            columnNames = getTableFields(tableName);
            row.add(columnNames);
            data_line.add(row);
        }
        DatGridDBTables.setItems(data_line);
    }

    void showAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Request Error");
        alert.setHeaderText("Request Error");
        alert.setContentText(e.getMessage());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    void doRequest(TableView<ObservableList> datGridSQLResult, String request) {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        try {
            datGridSQLResult.getItems().clear();
            datGridSQLResult.getColumns().clear();
            resultSet = SQLRequest(request);
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn column = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        Object temp = param.getValue().get(j);
                        if (temp == null) {
                            return new SimpleStringProperty("");
                        }
                        return new SimpleStringProperty(temp.toString());
                    }
                });
                datGridSQLResult.getColumns().addAll(column);
            }
            while (resultSet.next()) {//Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {//Iterate Column
                    row.add(resultSet.getString(i));
                }
                data.add(row);
            }
            datGridSQLResult.setItems(data);//FINALLY ADDED TO TableView
        } catch (Exception e) {
            showAlert(e);
        }
    }

    void disconnect() throws SQLException {
        try {
            if (connection != null) {
                if (!connection.isClosed() || !statement.isClosed()) {
                    connection.close();
                    statement.close();
                    System.out.println("Disconnected successfully");
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    String getTableFields(String TableName) throws SQLException {
        if (!connection.isClosed()) {
            ResultSet resultSet = null;
            String sql_command = "SELECT * FROM " + TableName;
            try {
                resultSet = statement.executeQuery(sql_command);
            } catch (SQLException e) {
                showAlert(e);
            }
            StringBuilder ResStr = new StringBuilder();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int ColCount = resultSetMetaData.getColumnCount();
            for (int i = 1; i < ColCount + 1; i++) {
                String StrCon = ", ";
                if (i == ColCount) StrCon = ";";
                ResStr.append(resultSetMetaData.getColumnName(i)).append("[")
                        .append(resultSetMetaData.getColumnTypeName(i)).append("]")
                        .append(StrCon);
            }
            return ResStr.toString();
        } else {
            return null;
        }
    }

    ResultSet SQLRequest(String RequestStr) throws SQLException {
        if (!connection.isClosed()) {
            ResultSet resultSet;
            try {
                resultSet = statement.executeQuery(RequestStr);
            } catch (Exception e) {
                throw new SQLException(e);
            }
            return resultSet;
        } else {
            return null;
        }
    }

    void connectTo(String datasource, String initialCatalog) throws SQLException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        String url = "jdbc:mysql://" + datasource + "/" + initialCatalog
                + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT&useSSL=false";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            System.out.println("Database connected!");
        } catch (SQLException e) {
            showAlert(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    ArrayList<String> getNamesOfTables() {
        ArrayList<String> names = new ArrayList<>();
        DatabaseMetaData databaseMetaData = null;
        try {
            databaseMetaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet resultSet = databaseMetaData.getTables(null, null, "%", types);
            while (resultSet.next()) {
                names.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return names;
    }

    void addToTableRequest(String request) {
        try {
            statement.executeUpdate(request);
        } catch (SQLException e) {
            showAlert(e);
        }
    }
}
