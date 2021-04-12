package app.controllers;


import data.MySQL;
import data.SQLHelper;
import data.entity.Agent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
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
        txtFirstName.setText(CurrentAgent.getFirstName());
        txtMiddleInitial.setText(CurrentAgent.getMiddleInitial());
        txtLastName.setText(CurrentAgent.getLastName());
        txtBusPhone.setText(CurrentAgent.getBusPhone());
        txtEmail.setText(CurrentAgent.getEmail());
        txtPosition.setText(CurrentAgent.getPosition());
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

/*Unused code from TableView attempt.

import app.controllers.util.TableProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

FXML private TableView<TableProperty> AgentsTable;
FXML private TableColumn<TableProperty, String> PropertyColumn;
FXML private TableColumn<TableProperty, String> ValueColumn;

//Methods for setting up form with TableView instead of TextFields.
PropertyColumn.setCellValueFactory(new PropertyValueFactory<TableProperty, String>("Name"));
ValueColumn.setCellValueFactory(new PropertyValueFactory<TableProperty, String>("Value"));
AgentsTable.setEditable(true);
ValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());


//Method to allowing editing of cell contents.
public void ChangePropertyValue(TableColumn.CellEditEvent editedCell) {
    TableProperty currentProperty = AgentsTable.getSelectionModel().getSelectedItem();
    currentProperty.setValue(editedCell.getNewValue().toString());
}
*/