package app.controllers;

import data.DBContext;
import data.SQLHelper;
import data.entity.Agent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
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
    DBContext database = new DBContext(url, username, password);

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
    @FXML Button btnEdit;
    @FXML Button btnNew;
    @FXML Button btnAdd;
    @FXML Button btnDelete;
    @FXML Button btnCancel;
    @FXML GridPane agentInfo;
    @FXML TextArea txtConsole;

    /**
     * Populate ListView with Agents on application start.
     */
    public void initialize() {
        try {
            RefreshAgentListView();
        } catch (Exception err) {
            txtConsole.setText("Error on initialization: " + err.getMessage());
        }
    }

    /**
     * Retrieves all agents from the database.
     */
    public List<Object> fetchAgents() {
        try (Connection connection = database.OpenConnection()) {
            ResultSet result = database.Select("*").Table("agents").Args("ORDER BY AgtFirstName").ExecuteQuery(connection);
            return SQLHelper.CreateList(result, Agent.class);
        } catch (Exception err) {
            txtConsole.setText("Error retrieving data: " + err.getMessage());
            return null;
        }
    }

    /**
     * Populates the Agent text fields with values after an agent is selected on the ListView.
     */
    public void ShowAgentInfo() {
        Agent CurrentAgent = (Agent) AgentsListView.getSelectionModel().getSelectedItem();

        if (CurrentAgent == null) {
            return;
        }

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
            Agent currentAgent = CreateTempAgent();

            //Attempt to create and run a SQL update statement on the database.
            try (Connection connection = database.OpenConnection()) {
                if (database.Table("agents").ExecuteUpdate(connection, currentAgent, currentAgent.getPrimaryKey())) {
                    RefreshAgentListView();
                    txtConsole.setText("Edit saved to database.");
                } else {
                    txtConsole.setText("Saving edit failed.");
                }
            } catch (Exception err) {
                txtConsole.setText("Database error: " + err.getMessage());
            }

        } catch (Exception err) {
            txtConsole.setText("Error while creating Agent object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage());
        }

        disableSave();
    }

    /**
     * Adds a new agent to the database.
     */
    public void commitAdd() {
        try {
            Agent currentAgent = CreateNewAgent();

            //Attempt to create and run a SQL update statement on the database.
            try (Connection connection = database.OpenConnection()) {
                if (database.Table("agents").ExecuteInsert(connection, currentAgent)) {
                    RefreshAgentListView();
                    txtConsole.setText("New agent added to database.");
                } else {
                    txtConsole.setText("Adding new agent failed.");
                }
            } catch (Exception err) {
                txtConsole.setText("Database error: " + err.getMessage());
            }

        } catch (Exception err) {
            txtConsole.setText("Error while creating Agent object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage());
        }

        disableAdd();
    }

    /**
     * Operation to delete a selected agent from the database.
     */
    public void deleteAgent() {
        Agent CurrentAgent = (Agent) AgentsListView.getSelectionModel().getSelectedItem();

        if (CurrentAgent == null) {
            System.out.println("No agent specified.");
            return;
        }

        try (Connection connection = database.OpenConnection()) {
            if (database.Table("agents").ExecuteDelete(connection, CurrentAgent)) {
                RefreshAgentListView();
                txtConsole.setText("Agent deleted from database.");
            } else {
                txtConsole.setText("Delete failed.");
            }
        } catch (Exception err) {
            txtConsole.setText("Error deleting record: " + err.getClass() + ", " + err.getMessage());
        }
    }

    /**
     * Refreshes the AgentListView.
     */
    private void RefreshAgentListView() {
        AgentsListView.setItems(FXCollections.observableArrayList(fetchAgents()));
    }

    private Agent CreateTempAgent() {
        return new Agent(
                Integer.parseInt(txtAgentId.getText()),
                txtFirstName.getText(),
                txtMiddleInitial.getText(),
                txtLastName.getText(),
                txtBusPhone.getText(),
                txtEmail.getText(),
                txtPosition.getText(),
                Integer.parseInt(txtAgencyId.getText())
        );
    }

    private Agent CreateNewAgent() {
        return new Agent(
                0,
                txtFirstName.getText(),
                txtMiddleInitial.getText(),
                txtLastName.getText(),
                txtBusPhone.getText(),
                txtEmail.getText(),
                txtPosition.getText(),
                Integer.parseInt(txtAgencyId.getText())
        );
    }

    /**
     * Enables adding of a new agent to the form. Disables 'edit' and 'new' buttons.
     */
    public void enableAdd() {
        btnAdd.setDisable(false);

        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        btnNew.setDisable(true);

        agentInfo.setStyle("-fx-background-color: #FFFACD");

        enableTextEdit();
        clearFields();
    }

    /**
     * Enables editing of an agent's information on the form. Disables edit button and enables save button.
     */
    public void enableSave() {
        btnSave.setDisable(false);

        btnEdit.setDisable(true);
        btnNew.setDisable(true);
        btnDelete.setDisable(true);

        agentInfo.setStyle("-fx-background-color: #FFFACD");

        enableTextEdit();
    }

    /**
     * Disables editing of an agent's information on the form. Enables edit button and disables save button.
     */
    public void disableSave() {
        btnSave.setDisable(true);

        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);

        agentInfo.setStyle("-fx-background-color: transparent");

        disableTextEdit();
    }

    /**
     * Disables committing the adding of a new agent to the database. Disables add button, enables edit and new button.
     */
    public void disableAdd() {
        btnAdd.setDisable(true);

        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);

        agentInfo.setStyle("-fx-background-color: transparent");

        disableTextEdit();
    }

    /**
     * Clears all text fields and disables all buttons except 'Edit' and 'New'
     */
    public void cancelAll() {
        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);

        btnAdd.setDisable(true);
        btnSave.setDisable(true);

        agentInfo.setStyle("-fx-background-color: transparent");

        clearFields();
        disableTextEdit();
    }

    /**
     * Enables all text fields in form to be edited.
     */
    public void enableTextEdit() {
        for (Node node : agentInfo.getChildren()) {
            if (node instanceof TextField && node != txtAgentId) {
                ((TextField) node).setEditable(true);
            }
        }
    }

    /**
     * Disables editing in all text fields in form.
     */
    public void disableTextEdit() {
        for (Node node : agentInfo.getChildren()) {
            if (node instanceof TextField && node != txtAgentId) {
                ((TextField) node).setEditable(false);
            }
        }
    }

    /**
     * Clears all text fields on the form.
     */
    private void clearFields() {
        for (Node node : agentInfo.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setText("");
            }
        }
    }
}