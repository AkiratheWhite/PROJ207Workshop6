package app.controllers;

import data.DBContext;
import data.SQLHelper;
import data.entity.Product;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

/**
	* Code written by: Texin Otinguey
	*/

public class ProductsController {
				
				String url = "jdbc:mysql://localhost:3306/travelexperts";
				String username = "root";
				String password = "";
				
				// Instantiates database object.
				DBContext database = new DBContext(url, username, password);
				
				@FXML
				private ResourceBundle resources;
				
				@FXML
				private URL location;
				
				@FXML
				private ListView<Object> listView_Products;
				
				@FXML
				private GridPane gridPane_Product;
				
				@FXML
				private TextField txtProductID;
				
				@FXML
				private TextField txtProductName;
				
				@FXML
				private TextArea txtConsole;
				
				@FXML
				private Button btnSave;
				
				@FXML
				private Button btnAdd;
				
				@FXML
				private Button btnEdit;
				
				@FXML
				private Button btnNew;
				
				@FXML
				private Button btnDelete;
				
				@FXML
				private Button btnCancel;
				
				/**
					* Populates the Product text fields with values after a Product is selected on the ListView.
					*/
				@FXML
				void onMouseClick_ListViewProduct(MouseEvent event) {
								Product currentProduct = (Product) listView_Products.getSelectionModel().getSelectedItem();
								
								if (currentProduct == null) {
												return;
								}
								
								txtProductID.setText(Integer.toString(currentProduct.getProductId()));
								txtProductName.setText(currentProduct.getProdName());
				}
				
				/**
					* Clears all text fields and disables all buttons except 'Edit' and 'New'
					*/
				@FXML
				void cancelAll(ActionEvent event) {
								btnEdit.setDisable(false);
								btnNew.setDisable(false);
								btnDelete.setDisable(false);
								
								btnAdd.setDisable(true);
								btnSave.setDisable(true);
								
								gridPane_Product.setStyle("-fx-background-color: transparent");
								
								clearFields();
								disableTextEdit();
				}
				
				/**
					* Adds a new product to the database.
					*/
				@FXML
				void commitAdd(ActionEvent event) {
								try {
												Product currentProduct = CreateNewProduct();
												
												//Attempt to create and run a SQL update statement on the database.
												try (Connection connection = database.OpenConnection()) {
																if (database.Table("products").ExecuteInsert(connection, currentProduct)) {
																				RefreshListView();
																				txtConsole.setText("New product added to database.");
																} else {
																				txtConsole.setText("Adding new product failed.");
																}
												} catch (Exception err) {
																txtConsole.setText("Database error: " + err.getMessage());
												}
												
								} catch (Exception err) {
												txtConsole.setText("Error while creating Product object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage());
								}
								
								disableAdd();
				}
				
				/**
					* Operation to delete a selected product from the database.
					*/
				@FXML
				void deleteProduct(ActionEvent event) {
								Product currentProduct = (Product) listView_Products.getSelectionModel().getSelectedItem();
								
								if (currentProduct == null) {
												System.out.println("No product specified.");
												return;
								}
								
								try (Connection connection = database.OpenConnection()) {
												if (database.Table("products").ExecuteDelete(connection, currentProduct)) {
																RefreshListView();
																txtConsole.setText("Product deleted from database.");
												} else {
																txtConsole.setText("Delete failed.");
												}
								} catch (Exception err) {
												txtConsole.setText("Error deleting record: " + err.getClass() + ", " + err.getMessage());
								}
				}
				
				/**
					* Enables adding of a new product to the form. Disables 'edit' and 'new' buttons.
					*/
				@FXML
				void enableAdd(ActionEvent event) {
								btnAdd.setDisable(false);
								
								btnDelete.setDisable(true);
								btnEdit.setDisable(true);
								btnNew.setDisable(true);
								
								gridPane_Product.setStyle("-fx-background-color: #FFFACD");
								
								enableTextEdit();
								clearFields();
				}
				
				/**
					* Enables editing of Product information on the form. Disables edit button and enables save button.
					*/
				@FXML
				void enableSave(ActionEvent event) {
								btnSave.setDisable(false);
								
								btnEdit.setDisable(true);
								btnNew.setDisable(true);
								btnDelete.setDisable(true);
								
								gridPane_Product.setStyle("-fx-background-color: #FFFACD");
								
								enableTextEdit();
				}
				
				/**
					* Creates a new Product object from the text fields and tries to save this data to the database.
					*/
				@FXML
				void saveEdit(ActionEvent event) {
								try {
												Product currentProduct = CreateTemporaryProduct();
												
												//Attempt to create and run a SQL update statement on the database.
												try (Connection connection = database.OpenConnection()) {
																if (database.Table("products").ExecuteUpdate(connection, currentProduct, currentProduct.getPrimaryKey())) {
																				RefreshListView();
																				txtConsole.appendText("Edit saved to database." + '\n');
																} else {
																				txtConsole.appendText("Saving edit failed." + '\n');
																}
												} catch (Exception err) {
																txtConsole.appendText("Database error: " + err.getMessage() + '\n');
												}
												
								} catch (Exception err) {
												txtConsole.appendText("Error while creating Product object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage() + '\n');
								}
								
								disableSave();
				}
				
				@FXML
				void initialize() {
								assert listView_Products != null : "fx:id=\"listView_Products\" was not injected: check your FXML file 'products.fxml'.";
								assert gridPane_Product != null : "fx:id=\"gridPane_Product\" was not injected: check your FXML file 'products.fxml'.";
								assert txtProductID != null : "fx:id=\"txtProductID\" was not injected: check your FXML file 'products.fxml'.";
								assert txtProductName != null : "fx:id=\"txtProductName\" was not injected: check your FXML file 'products.fxml'.";
								assert txtConsole != null : "fx:id=\"txtConsole\" was not injected: check your FXML file 'products.fxml'.";
								assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'products.fxml'.";
								assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'products.fxml'.";
								assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'products.fxml'.";
								assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'products.fxml'.";
								assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'products.fxml'.";
								assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'products.fxml'.";
								
								try {
												RefreshListView();
								} catch (Exception err) {
												txtConsole.appendText("Error on initialization: " + err.getMessage() + '\n');
								}
				}
				
				
				
				
				
				// ================================ Methods ================================ //
				/**
					* Refreshes List View
					*/
				private void RefreshListView() {
								listView_Products.setItems(FXCollections.observableArrayList(getAllProducts()));
				}
				
				/**
					* Retrieves all Products from the database.
					*/
				public List<Object> getAllProducts() {
								try (Connection connection = database.OpenConnection()) {
												ResultSet result = database.Select("*").Table("products").Args("ORDER BY ProdName").ExecuteQuery(connection);
												
												return SQLHelper.CreateList(result, Product.class);
								} catch (Exception err) {
												txtConsole.appendText("getAllProducts() || Error retrieving data: " + err.getMessage() + '\n');
												return null;
								}
				}
				
				/**
					* Enables all text fields in the form to be edited.
					*/
				public void enableTextEdit() {
								for (Node node : gridPane_Product.getChildren()) {
												if (node instanceof TextField && node != txtProductID) {
																((TextField) node).setEditable(true);
												}
								}
				}
				
				/**
					* Disables editing in all text fields in form.
					*/
				public void disableTextEdit() {
								for (Node node : gridPane_Product.getChildren()) {
												if (node instanceof TextField && node != txtProductID) {
																((TextField) node).setEditable(false);
												}
								}
				}
				
				/**
					* Clears all text fields on the form.
					*/
				private void clearFields() {
								for (Node node : gridPane_Product.getChildren()) {
												if (node instanceof TextField) {
																((TextField) node).setText("");
												}
								}
				}
				
				/**
					* Disables committing the adding of a new Product to the database. Disables add button, enables edit and new button.
					*/
				public void disableAdd() {
								btnAdd.setDisable(true);
								
								btnEdit.setDisable(false);
								btnNew.setDisable(false);
								btnDelete.setDisable(false);
								
								gridPane_Product.setStyle("-fx-background-color: transparent");
								
								disableTextEdit();
				}
				
				/**
					* Create Product
					*/
				private Product CreateNewProduct() {
								return new Product(
																0,
																txtProductName.getText()
								);
				}
				
				/**
					* Create Temporary Product
					*/
				private Product CreateTemporaryProduct() {
								return new Product(
																Integer.parseInt(txtProductID.getText()),
																txtProductName.getText()
								);
				}
				
				/**
					* Disables editing for the Product information on the form. Enables edit button and disables save button.
					*/
				public void disableSave() {
								btnSave.setDisable(true);
								
								btnEdit.setDisable(false);
								btnNew.setDisable(false);
								btnDelete.setDisable(false);
								
								gridPane_Product.setStyle("-fx-background-color: transparent");
								
								disableTextEdit();
				}
				
				/**
					* Operation to delete a selected product from the database.
					*/
				public void deleteProduct() {
								Product currentProduct = (Product) listView_Products.getSelectionModel().getSelectedItem();
								
								if (currentProduct == null) {
												System.out.println("No Product specified.");
												return;
								}
								
								try (Connection connection = database.OpenConnection()) {
												if (database.Table("products").ExecuteDelete(connection, currentProduct)) {
																RefreshProductsListView();
																txtConsole.setText("Product deleted from database.");
												} else {
																txtConsole.setText("Delete failed.");
												}
								} catch (Exception err) {
												txtConsole.setText("Error deleting record: " + err.getClass() + ", " + err.getMessage() + '\n');
								}
				}
				
				/**
					* Refreshes the Product List View.
					*/
				private void RefreshProductsListView() {
								listView_Products.setItems(FXCollections.observableArrayList(getAllProducts()));
				}
}
