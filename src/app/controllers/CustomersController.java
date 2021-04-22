package app.controllers;

//import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import com.mysql.cj.log.Log;
import data.DBContext;
import data.SQLHelper;
import data.entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import static javafx.collections.FXCollections.observableArrayList;

/** CustomersController Class
 * created by: Chester Solang
 */


public class CustomersController {

    String url = "jdbc:mysql://localhost:3306/travelexperts";
    String username = "root";
    String password = "";

    //Instantiates database object.
    DBContext database = new DBContext(url, username, password);

    //@FXML // ResourceBundle that was given to the FXMLLoader
    //private ResourceBundle resources;
    //@FXML // URL location of the FXML file that was given to the FXMLLoader
    //private URL location;
    @FXML // fx:id="CustomersListView"
    private ListView<Object> CustomersListView; // Value injected by FXMLLoader
    @FXML // fx:id="customerInfo"
    private GridPane customerInfo; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustomerId"
    private TextField txtCustomerId; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustFirstName"
    private TextField txtCustFirstName; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustLastName"
    private TextField txtCustLastName; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustAddress"
    private TextField txtCustAddress; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustCity"
    private TextField txtCustCity; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustProv"
    private TextField txtCustProv; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustPostal"
    private TextField txtCustPostal; // Value injected by FXMLLoader
    @FXML // fx:id="txtAgentId"
    private TextField txtAgentId; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustCountry"
    private TextField txtCustCountry; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustHomePhone"
    private TextField txtCustHomePhone; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustBusPhone"
    private TextField txtCustBusPhone; // Value injected by FXMLLoader
    @FXML // fx:id="txtCustEmail"
    private TextField txtCustEmail; // Value injected by FXMLLoader
    @FXML // fx:id="btnSave"
    private Button btnSave; // Value injected by FXMLLoader
    @FXML // fx:id="btnEdit"
    private Button btnEdit; // Value injected by FXMLLoader
    @FXML // fx:id="btnNew"
    private Button btnNew; // Value injected by FXMLLoader
    @FXML // fx:id="btnAdd"
    private Button btnAdd; // Value injected by FXMLLoader
    @FXML // fx:id="btnCancel"
    private Button btnCancel; // Value injected by FXMLLoader
    @FXML // fx:id="btnDelete"
    private Button btnDelete; // Value injected by FXMLLoader
    @FXML // fx:id="txtConsole"
    private TextArea txtConsole; // Value injected by FXMLLoader

    /*@FXML
    void ShowAgentInfo(MouseEvent event) {}
    @FXML
    void cancelAll(ActionEvent event) {}
    @FXML
    void commitAdd(ActionEvent event) {}
    @FXML
    void deleteCustomer(ActionEvent event) {}
    @FXML
    void enableAdd(ActionEvent event) {}
    @FXML
    void enableSave(ActionEvent event) {}
    @FXML
    void saveEdit(ActionEvent event) {}*/
    /*@FXML // This method is called by the FXMLLoader when initialization is complete
     void initialize() {

        assert CustomersListView != null : "fx:id=\"CustomersListView\" was not injected: check your FXML file 'customers.fxml'.";
        assert customerInfo != null : "fx:id=\"customerInfo\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustomerId != null : "fx:id=\"txtCustomerId\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustFirstName != null : "fx:id=\"txtCustFirstName\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustLastName != null : "fx:id=\"txtCustLastName\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustAddress != null : "fx:id=\"txtCustAddress\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustCity != null : "fx:id=\"txtCustCity\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustProv != null : "fx:id=\"txtCustProv\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustPostal != null : "fx:id=\"txtCustPostal\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtAgentId != null : "fx:id=\"txtAgentId\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustCountry != null : "fx:id=\"txtCustCountry\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustHomePhone != null : "fx:id=\"txtCustHomePhone\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustBusPhone != null : "fx:id=\"txtCustBusPhone\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtCustEmail != null : "fx:id=\"txtCustEmail\" was not injected: check your FXML file 'customers.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'customers.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'customers.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'customers.fxml'.";
        assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'customers.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'customers.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'customers.fxml'.";
        assert txtConsole != null : "fx:id=\"txtConsole\" was not injected: check your FXML file 'customers.fxml'.";
    }//initialize*/

    /**
     * Populate ListView of Customers on application start.
     */
    public void initialize() {
        try {
            RefreshCustomersListView();
        } catch (Exception err) {
            txtConsole.setText("Error on initialization: " + err.getMessage());
        }
    }//initialize

    /**
     * Retrieves all customers from the database.
     */
    public List<Object> getCustomers() {
        try (Connection connection = database.OpenConnection()) {
            ResultSet result = database.Select("*").Table("customers").Args("ORDER BY CustFirstName").ExecuteQuery(connection);
            return SQLHelper.CreateList(result, Customer.class);
        } catch (Exception err) {
            txtConsole.setText("Error retrieving data: " + err.getMessage());
            return null;
        }
    }//getAllCustomers

    /**
     * Populates the Customer text fields with values after a customer is selected on the ListView.
     */
    public void displayCustomerInfo() {
        Customer selectedCustomer = (Customer)CustomersListView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            return;
        }
        txtCustomerId.setText(Integer.toString(selectedCustomer.getCustomerId()));
        txtCustFirstName.setText(selectedCustomer.getCustAddress());
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
    }//displayCustomerInfo

    /**
     * Creates a new Customer object from the text fields and tries to save this data to the database.
     */
    public void saveEdit() {
        try {
            Customer currentCustomer = CreateTempCustomer();
            //Attempt to create and run a SQL update statement on the database.
            try (Connection connection = database.OpenConnection()) {
                if (database.Table("customers").ExecuteUpdate(connection, currentCustomer, currentCustomer.getPrimaryKey())) {
                    RefreshCustomersListView();
                    //initialize();
                    txtConsole.setText("Edit saved to database.");
                }
                else {
                    txtConsole.setText("Saving edit failed.");
                }
            } catch (Exception err) {
                txtConsole.setText("Database error: " + err.getMessage());
            }
        } catch (Exception err) {
            txtConsole.setText("Error while creating Customer object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage());
        }
        disableSave();
    }

    /**
     * Adds a new Customer to the database.
     */
    public void commitAdd() {
        try {
            Customer currentCustomer = CreateNewCustomer();

            //Attempt to create and run a SQL update statement on the database.
            try (Connection connection = database.OpenConnection()) {
                if (database.Table("customers").ExecuteInsert(connection, currentCustomer)) {
                    RefreshCustomersListView();
                    //initialize();
                    txtConsole.setText("Customer added to database.");
                } else {
                    txtConsole.setText("Adding Customer failed.");
                }
            } catch (Exception err) {
                txtConsole.setText("Database error: " + err.getMessage());
            }

        } catch (Exception err) {
            txtConsole.setText("Error while creating Customer object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage());
        }
        disableAdd();
    }

    //Disables add button, enables edit and new button.
    public void disableAdd() {
        btnAdd.setDisable(true);
        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);
        customerInfo.setStyle("-fx-background-color: transparent");
        disableTextEdit();
    }

    /**
     * Enables editing of an agent's information on the form. Disables edit button and enables save button.
     */
    public void enableSave() {
        System.out.println("yes working");
        txtConsole.setText("yes working");
        btnSave.setDisable(false);

        btnEdit.setDisable(true);
        btnNew.setDisable(true);
        btnDelete.setDisable(true);

        customerInfo.setStyle("-fx-background-color: #FFFACD");

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
    public void enableAdd() {
        btnAdd.setDisable(false);
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        btnNew.setDisable(true);
        customerInfo.setStyle("-fx-background-color: #FFFACD");
        enableTextEdit();
        clearFields();
    }

    //Enables all text fields in form to be edited.
    public void enableTextEdit() {
        for (Node node : customerInfo.getChildren()) {
            if (node instanceof TextField && node != txtAgentId) {
                ((TextField) node).setEditable(true);
            }
        }
    }

    //Disables editing in all text fields in form.
    public void disableTextEdit() {
        for (Node node : customerInfo.getChildren()) {
            if (node instanceof TextField && node != txtAgentId) {
                ((TextField) node).setEditable(false);
            }
        }
    }

    private void RefreshCustomersListView() {
       CustomersListView.setItems(FXCollections.observableArrayList(getCustomers()));
    }
//test
    /*private ObservableList<Customer> observableArrayList(List<Object> customers) {

    }*/

    private Customer CreateNewCustomer() {
        return new Customer(
                0, //customerID = auto generate
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

    //Clears all text fields on the form.
    private void clearFields() {
        for (Node node : customerInfo.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setText("");
            }
        }
    }

}//class CustomersController
