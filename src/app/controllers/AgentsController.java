package app.controllers;

import data.MySQL;
import data.ResultData;
import data.entity.Agent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgentsController {
    String url = "jdbc:mysql://localhost:3306/travelexperts";
    String username = "root";
    String password = "";

    //Instantiates database object.
    MySQL database = new MySQL(url, username, password);

    @FXML
    ListView<Object> AgentsListView;

    public void initialize() {
        List<Object> agents = new ArrayList<>();
        fetchAgents(agents);
        AgentsListView.getItems().addAll(FXCollections.observableArrayList(agents));
    }

    public void fetchAgents(List<Object> input) {
        try (Connection connection = database.OpenConnection()) {
            ResultSet result = database.Select("*").Table("agents").ExecuteQuery(connection);
            ResultData.CreateList(result, input, Agent.class);
        } catch (Exception err) {
            System.out.println("Error retrieving data: " + err.getMessage());
        }
    }
}
