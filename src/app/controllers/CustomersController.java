package app.controllers;

import data.DBContext;
import data.SQLHelper;
import data.entity.Customer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/** CustomersController Class
 * created by: Chester Solang
 *
 */
public class CustomersController {
    String url = "jdbc:mysql://localhost:3306/travelexperts";
    String username = "root";
    String password = "";
    //Instantiates database object.
    DBContext database = new DBContext(url, username, password);

    @FXML
    private ListView<Object> CustomersListView;
    @FXML
    private GridPane customerInfo;
    @FXML
    private TextField txtCustomerId;
    @FXML
    private TextField txtCustFirstName;
    @FXML
    private TextField txtCustLastName;
    @FXML
    private TextField txtCustAddress;
    @FXML
    private TextField txtCustCity;
    @FXML
    private TextField txtCustProv;
    @FXML
    private TextField txtCustPostal;
    @FXML
    private TextField txtCustCountry;
    @FXML
    private TextField txtCustHomePhone;
    @FXML
    private TextField txtCustBusPhone;
    @FXML
    private TextField txtCustEmail;
    @FXML
    private TextField txtAgentId;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnDelete;
    @FXML
    private TextArea txtConsole;

    /**
     * Populate ListView of Customers on application load.
     */
    public void initialize() {
        try {
            RefreshCustomersListView();
        } catch (Exception ex) {
            txtConsole.setText("Error initializing: " + ex.getMessage());
        }
    }//initialize

    /**
     * Retrieves customer's list from database ordered by First Name.
     */
    public List<Object> getCustomers() {
        try (Connection connection = database.OpenConnection())
        {
            ResultSet result = database.Select("*").Table("customers").Args("ORDER BY CustFirstName").ExecuteQuery(connection);
            return SQLHelper.CreateList(result, Customer.class);
        }
        catch (Exception ex)
        {
            txtConsole.setText("Error retrieving data: " + ex.getMessage());
            return null;
        }
    }//getAllCustomers

    /**
     * Display Selected Customer Details on the text fields.
     */
    public void displayCustomerInfo()
    {
        Customer selectedCustomer = (Customer)CustomersListView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null)
        {
            return;
        }
        txtCustomerId.setText(Integer.toString(selectedCustomer.getCustomerId()));
        txtCustFirstName.setText(selectedCustomer.getCustFirstName());
        txtCustLastName.setText(selectedCustomer.getCustLastName());
        txtCustAddress.setText(selectedCustomer.getCustAddress());
        txtCustCity.setText(selectedCustomer.getCustCity());
        txtCustProv.setText(selectedCustomer.getCustProv());
        txtCustPostal.setText(selectedCustomer.getCustPostal());
        txtCustCountry.setText(selectedCustomer.getCustCountry());
        txtCustHomePhone.setText(selectedCustomer.getCustHomePhone());
        txtCustBusPhone.setText(selectedCustomer.getCustBusPhone());
        txtCustEmail.setText(selectedCustomer.getCustEmail());
        txtAgentId.setText(Integer.toString(selectedCustomer.getAgentId()));

        customerInfo.setStyle("-fx-background-color: aqua");
    }//displayCustomerInfo

    /**
     * Creates a new Customer object from the text fields and tries to save this data to the database.
     */
    public void saveEdit_btnSave() {
        try {
            Customer customer = CreateTempCustomer();
            //Attempt to create and run a SQL update statement on the database.
            try (Connection conn = database.OpenConnection()) {
                if (database.Table("customers").ExecuteUpdate(conn, customer, customer.getPrimaryKey())) {
                    RefreshCustomersListView();
                    //initialize();
                    txtConsole.setText("Edit successfully saved to database.");
                }
                else {
                    txtConsole.setText("Edit failed.");
                }
            } catch (Exception ex) {
                txtConsole.setText("Database error: " + ex.getMessage());
            }
        } catch (Exception ex) {
            txtConsole.setText("Error while creating Customer object. (Did you leave a text field empty?)\n" + ex.getClass() + ", " + ex.getMessage());
        }
        disableSave();
    }//saveEdit_btnSave

    /**
     * Adds a new Customer to the database.
     */
    public void commitAdd_btnAdd() {
        try {
            Customer customer = CreateNewCustomer();
            //Attempt to create and run a SQL update statement on the database.
            try (Connection conn = database.OpenConnection()) {
                if (database.Table("customers").ExecuteInsert(conn, customer)) {
                    RefreshCustomersListView();
                    //initialize();
                    txtConsole.setText("Customer added to database.");
                } else {
                    txtConsole.setText("Adding Customer failed.");
                }
            } catch (Exception ex) {
                txtConsole.setText("Database error: " + ex.getMessage());
            }

        } catch (Exception ex) {
            txtConsole.setText("Error while creating Customer object. (Did you leave a text field empty?)\n" + ex.getClass() + ", " + ex.getMessage());
        }
        disableAdd();
    }//commitAdd_btnAdd

    /**
     * Delete a selected Customer from the database.
     */
    public void deleteCustomer() {
        Customer customer = (Customer) CustomersListView.getSelectionModel().getSelectedItem();
        if (customer == null) {
            txtConsole.setText("Select a Customer to delete.");
            return;
        }
        try (Connection conn = database.OpenConnection()) {
            if (database.Table("customers").ExecuteDelete(conn, customer)) {
                RefreshCustomersListView();
                txtConsole.setText("Customer deleted from database.");
            } else {
                txtConsole.setText("Delete failed.");
            }
        } catch (Exception ex) {
            txtConsole.setText("Error deleting record: " + ex.getClass() + ", " + ex.getMessage());
        }
    }//deleteCustomer

    //Disables add button, enables 'edit' and 'new' button.
    public void disableAdd() {
        btnAdd.setDisable(true);
        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);
        customerInfo.setStyle("-fx-background-color: transparent");
        disableTextEdit();
    }

    //Modify Customer Information. Disable 'edit' button and enable 'save' button.
    public void enableSave_btnEdit() {
        btnSave.setDisable(false);
        btnEdit.setDisable(true);
        btnNew.setDisable(true);
        btnDelete.setDisable(true);
        customerInfo.setStyle("-fx-background-color: yellow");
        enableTextEdit();
    }

    //Enables 'edit' button and disables 'save' button.
    public void disableSave() {
        btnSave.setDisable(true);
        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);
        customerInfo.setStyle("-fx-background-color: transparent");
        disableTextEdit();
    }

    //Disables 'edit' and 'new' buttons.
    public void enableAdd_btnNew() {
        btnAdd.setDisable(false);
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        btnNew.setDisable(true);
        customerInfo.setStyle("-fx-background-color: yellow");
        enableTextEdit();
        clearFields();
    }

    //Enables all text fields in form to be edited.
    public void enableTextEdit() {
        for (Node node : customerInfo.getChildren()) {
            if (node instanceof TextField && node != txtCustomerId) {
                ((TextField) node).setEditable(true);
            }
        }
    }

    //Disables editing in all text fields in form.
    public void disableTextEdit() {
        for (Node node : customerInfo.getChildren()) {
            if (node instanceof TextField && node != txtCustomerId) {
                ((TextField) node).setEditable(false);
            }
        }
    }

    //Clears all text fields and disables all buttons except 'Edit' and 'New'
    public void cancelAll() {
        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
        btnSave.setDisable(true);
        customerInfo.setStyle("-fx-background-color: transparent");
        clearFields();
        disableTextEdit();
    }

    private void RefreshCustomersListView() {
       CustomersListView.setItems(FXCollections.observableArrayList(getCustomers()));
    }

    private Customer CreateNewCustomer() {
        return new Customer(
                0, //customerID
                txtCustFirstName.getText(),
                txtCustLastName.getText(),
                txtCustAddress.getText(),
                txtCustCity.getText(),
                txtCustProv.getText(),
                txtCustPostal.getText(),
                txtCustCountry.getText(),
                txtCustHomePhone.getText(),
                txtCustBusPhone.getText(),
                txtCustEmail.getText(),
                Integer.parseInt(txtAgentId.getText())
        );
    }

    private Customer CreateTempCustomer() {
        return new Customer(
                Integer.parseInt(txtCustomerId.getText()),
                txtCustFirstName.getText(),
                txtCustLastName.getText(),
                txtCustAddress.getText(),
                txtCustCity.getText(),
                txtCustProv.getText(),
                txtCustPostal.getText(),
                txtCustCountry.getText(),
                txtCustHomePhone.getText(),
                txtCustBusPhone.getText(),
                txtCustEmail.getText(),
                Integer.parseInt(txtAgentId.getText())
        );
    }

    //Clears all text fields on the form.
    private void clearFields() {
        for (Node node : customerInfo.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setText("");
            }
        }
    }

}//class CustomersController
