package app.controllers;

import data.MySQL;
import data.SQLHelper;
import data.entity.Agent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 * Code written by: Tony (Zongzheng) Li
 * Last modified on:
 */
public class AgentsController {

    String url = "jdbc:mysql://localhost:3306/travelexperts";
    String username = "root";
    String password = "";

    //Instantiates database object.
    MySQL database = new MySQL(url, username, password);

    @FXML ListView<Object> AgentsListView;
    @FXML TextField txtAgentId;
    @FXML TextField txtFirstName;
    @FXML TextField txtMiddleInitial;
    @FXML TextField txtLastName;
    @FXML TextField txtBusPhone;
    @FXML TextField txtEmail;
    @FXML TextField txtPosition;
    @FXML TextField txtAgencyId;
    @FXML Button btnSave;
    @FXML GridPane agentInfo;

    //Populate ListView with Agents on application start.
    public void initialize() {
        AgentsListView.setItems(FXCollections.observableArrayList(fetchAgents()));
    }

    /**
     * Retrieves all agents from the database.
     */
    public List<Object> fetchAgents() {
        try (Connection connection = database.OpenConnection()) {
            ResultSet result = database.Select("*").Table("agents").Args("ORDER BY AgtFirstName").ExecuteQuery(connection);
            return SQLHelper.CreateList(result, Agent.class);
        } catch (Exception err) {
            System.out.println("Error retrieving data: " + err.getMessage());
            return null;
        }
    }

    /**
     * Populates the Agent text fields with values after an agent is selected on the ListView.
     */
    public void ShowAgentInfo() {
        Agent CurrentAgent = (Agent) AgentsListView.getSelectionModel().getSelectedItem();

        txtAgentId.setText(Integer.toString(CurrentAgent.getAgentId()));
        txtFirstName.setText(CurrentAgent.getAgtFirstName());
        txtMiddleInitial.setText(CurrentAgent.getAgtMiddleInitial());
        txtLastName.setText(CurrentAgent.getAgtLastName());
        txtBusPhone.setText(CurrentAgent.getAgtBusPhone());
        txtEmail.setText(CurrentAgent.getAgtEmail());
        txtPosition.setText(CurrentAgent.getAgtPosition());
        txtAgencyId.setText(Integer.toString(CurrentAgent.getAgencyId()));
    }

    /**
     * Creates a new Agent object from the text fields and tries to save this data to the database.
     */
    public void saveEdit() {
        try {
            Agent currentAgent = new Agent(
                    Integer.parseInt(txtAgentId.getText()),
                    txtFirstName.getText(),
                    txtMiddleInitial.getText(),
                    txtLastName.getText(),
                    txtBusPhone.getText(),
                    txtEmail.getText(),
                    txtPosition.getText(),
                    Integer.parseInt(txtAgencyId.getText())
            );

            //Attempt to create and run a SQL update statement on the database.
            try (Connection connection = database.OpenConnection()) {
                if (database.Table("agents").ExecuteUpdate(connection, currentAgent, currentAgent.getAgentId())) {
                    AgentsListView.setItems(FXCollections.observableArrayList(fetchAgents()));
                }
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }

        } catch (Exception err) {
            System.out.println("Error while creating agent. (Did you leave a text field empty?)");
        }
    }
}