package app.controllers;

import app.controllers.util.TableProperty;
import com.sun.javafx.reflect.FieldUtil;
import data.MySQL;
import data.ResultData;
import data.entity.Agent;

import javafx.beans.binding.ObjectExpression;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class AgentsController {


    String url = "jdbc:mysql://localhost:3306/travelexperts";
    String username = "root";
    String password = "";

    //Instantiates database object.
    MySQL database = new MySQL(url, username, password);

    @FXML private ListView<Object> AgentsListView;
    @FXML private TableView<TableProperty> AgentsTable;
    @FXML private TableColumn<TableProperty, String> PropertyColumn;
    @FXML private TableColumn<TableProperty, Object> ValueColumn;

    public void initialize() {
        List<Object> agents = new ArrayList<>();
        fetchAgents(agents);
        AgentsListView.getItems().addAll(FXCollections.observableArrayList(agents));

        PropertyColumn.setCellValueFactory(new PropertyValueFactory<TableProperty, String>("Name"));
        ValueColumn.setCellValueFactory(new PropertyValueFactory<TableProperty, Object>("Value"));
    }

    public void fetchAgents(List<Object> input) {
        try (Connection connection = database.OpenConnection()) {
            ResultSet result = database.Select("*").Table("agents").ExecuteQuery(connection);
            ResultData.CreateList(result, input, Agent.class);
        } catch (Exception err) {
            System.out.println("Error retrieving data: " + err.getMessage());
        }
    }

    public void ShowAgentInfo() {
        Agent CurrentAgent = (Agent) AgentsListView.getSelectionModel().getSelectedItem();
        try {
            AgentsTable.setItems(FXCollections.observableArrayList(CurrentAgent.GetProps()));
        } catch (IllegalAccessException err) {
            System.out.println("Error on retrieving object data: " + err.getMessage());
        }
    }
}
