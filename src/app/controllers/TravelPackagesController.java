package app.controllers;

import data.DBContext;
import data.SQLHelper;
import data.entity.Package;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Changelog:
 * [1.01|Tony Li|05/06/21] - Removed unused event arguments and removed code duplication by extracting to method object.
 */
public class TravelPackagesController {
    String url = "jdbc:mysql://localhost:3306/travelexperts";
    String username = "root";
    String password = "";

    // Instantiates database object.
    DBContext database = new DBContext(url, username, password);

    @FXML
    private ListView<Object> TravelPackagesListView;

    @FXML
    private GridPane grid_travelpackage;

    @FXML
    private TextField txtPackageName;

    @FXML
    private TextField txtPackageID;

    @FXML
    private DatePicker datepicker_PackageStartDate;

    @FXML
    private DatePicker datepicker_PackageEndDate;

    @FXML
    private TextField txtStartTimestamp;

    @FXML
    private TextField txtEndTimestamp;

    @FXML
    private TextField txtAgencyCommission;

    @FXML
    private TextField txtBasePrice;

    @FXML
    private TextArea txtareaPackageDescription;

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
     * Populates the text fields with values after a Travel Package is selected on the ListView.
     */
    @FXML
    void showTravelPackageInfo() {
        Package currentTravelPackage = (Package) TravelPackagesListView.getSelectionModel().getSelectedItem();

        if (currentTravelPackage == null) {
            return;
        }

        txtPackageID.setText(Integer.toString(currentTravelPackage.getPackageId()));
        txtPackageName.setText(currentTravelPackage.getPkgName());

        LocalDateTime start_Date = currentTravelPackage.getPkgStartDate();
        LocalDateTime end_Date = currentTravelPackage.getPkgEndDate();

        LocalTime localTime_startDate = start_Date.toLocalTime();
        LocalTime localTime_endDate = end_Date.toLocalTime();

        // Convert to Local Date
        //	-	To be put in the Date Picker
        LocalDate localstart_date = start_Date.toLocalDate();
        LocalDate localend_date = end_Date.toLocalDate();

        // Test
        // System.out.println(start_Date);
        // System.out.println(end_Date);

        // Local Date Time in String
        //	-	To be put in Text Fields
        String formatted_startDate = String.valueOf(localTime_startDate);
        String formatted_endDate = String.valueOf(localTime_endDate);

        // Set Values for Timestamp to Text Fields
        txtStartTimestamp.setText(formatted_startDate);
        txtEndTimestamp.setText(formatted_endDate);

        // Set Values for Date to Date Picker
        datepicker_PackageStartDate.setValue(localstart_date);
        datepicker_PackageEndDate.setValue(localend_date);

        txtareaPackageDescription.setText(currentTravelPackage.getPkgDesc());
        txtAgencyCommission.setText(String.format("%.2f", currentTravelPackage.getPkgAgencyCommission()));
        txtBasePrice.setText(String.format("%.2f", currentTravelPackage.getPkgBasePrice()));

    }

    /**
     * Clears all text fields, text areas, date time pickers and disables all buttons except 'Edit' and 'New'
     */
    @FXML
    void cancelAll() {
        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);

        btnAdd.setDisable(true);
        btnSave.setDisable(true);

        grid_travelpackage.setStyle("-fx-background-color: transparent");

        clearFields();
        disableTextEdit();
    }

    /**
     * Adds a new Travel Package to the database.
     */
    @FXML
    void commitAdd() {
        try {
            Package currentTravelPackage = CreateNewTravelPackage();

            // Attempt to create and run a SQL update statement on the database.
            try (Connection connection = database.OpenConnection()) {
                if (database.Table("packages").ExecuteInsert(connection, currentTravelPackage)) {
                    RefreshListView();
                    txtConsole.appendText("New Travel Package has been added to database." + '\n');
                } else {
                    txtConsole.appendText("Failed to add new Travel Package." + '\n');
                }
            } catch (Exception err) {
                txtConsole.appendText("Database error: " + err.getMessage() + '\n');
            }

        } catch (Exception err) {
            txtConsole.appendText("Error while creating Travel Package object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage() + "\n");
        }

        disableAdd();
    }

    /**
     * Operation to delete a selected Travel Package from the database.
     */
    @FXML
    void deleteTravelPackage() {
        Package currentTravelPackage = (Package) TravelPackagesListView.getSelectionModel().getSelectedItem();

        if (currentTravelPackage == null) {
            System.out.println("No Travel Package specified.");
            return;
        }

        try (Connection connection = database.OpenConnection()) {
            if (database.Table("packages").ExecuteDelete(connection, currentTravelPackage)) {
                RefreshListView();
                txtConsole.appendText("Travel Package deleted from database." + '\n');
            } else {
                txtConsole.appendText("Delete failed." + '\n');
            }
        } catch (Exception err) {
            txtConsole.appendText("Error deleting record: " + err.getClass() + ", " + err.getMessage() + '\n');
        }
    }

    /**
     * Enables adding of a new Travel Package to the form. Disables 'edit' and 'new' buttons.
     */
    @FXML
    void enableAdd() {
        btnAdd.setDisable(false);

        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        btnNew.setDisable(true);

        grid_travelpackage.setStyle("-fx-background-color: #FFFACD");

        enableTextEdit();
        clearFields();
    }

    /**
     * Enables editing for the Travel Package's information on the form. Disables edit button and enables save button.
     */
    @FXML
    void enableSave() {
        btnSave.setDisable(false);

        btnEdit.setDisable(true);
        btnNew.setDisable(true);
        btnDelete.setDisable(true);

        grid_travelpackage.setStyle("-fx-background-color: #FFFACD");

        enableTextEdit();
    }

    /**
     * Creates a new Travel Package object from the text fields and tries to save this data to the database.
     */
    @FXML
    void saveEdit() {
        try {
            Package currentTravelPackage = CreateTemporaryTravelPackage();

            // Attempt to create and run a SQL update statement on the database.
            try (Connection connection = database.OpenConnection()) {
                if (database.Table("packages").ExecuteUpdate(connection, currentTravelPackage, currentTravelPackage.getPrimaryKey())) {
                    RefreshListView();
                    txtConsole.appendText("Edit saved to database." + '\n');
                } else {
                    txtConsole.appendText("Saving edit failed." + '\n');
                }
            } catch (Exception err) {
                txtConsole.appendText("Database error: " + err.getMessage() + '\n');
            }

        } catch (Exception err) {
            txtConsole.appendText("Error while creating Travel Package object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage() + '\n');
        }

        disableSave();
    }

    @FXML
    void initialize() {
        assert TravelPackagesListView != null : "fx:id=\"TravelPackagesListView\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert grid_travelpackage != null : "fx:id=\"grid_travelpackage\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert txtPackageName != null : "fx:id=\"txtPackageName\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert txtPackageID != null : "fx:id=\"txtPackageID\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert txtAgencyCommission != null : "fx:id=\"txtAgencyCommission\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert datepicker_PackageStartDate != null : "fx:id=\"datepicker_PackageStartDate\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert datepicker_PackageEndDate != null : "fx:id=\"datepicker_PackageEndDate\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert txtStartTimestamp != null : "fx:id=\"txtStartTimestamp\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert txtEndTimestamp != null : "fx:id=\"txtEndTimestamp\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert txtBasePrice != null : "fx:id=\"txtBasePrice\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert txtareaPackageDescription != null : "fx:id=\"txtareaPackageDescription\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert txtConsole != null : "fx:id=\"txtConsole\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'travel_packages.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'travel_packages.fxml'.";

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
        List<Object> result = getAllPackages();
        TravelPackagesListView.setItems(FXCollections.observableArrayList(result));
    }

    /**
     * Retrieves all travel packages from the database.
     */
    public List<Object> getAllPackages() {
        try (Connection connection = database.OpenConnection()) {
            ResultSet result = database.Select("*").Table("packages").Args("ORDER BY PkgName").ExecuteQuery(connection);

            return SQLHelper.CreateList(result, Package.class);
        } catch (Exception err) {
            txtConsole.appendText("getAllPackages() || Error retrieving data: " + err.getMessage() + '\n');
            return null;
        }
    }

    /**
     * Enables all text fields in the form to be edited.
     */
    public void enableTextEdit() {
        for (Node node : grid_travelpackage.getChildren()) {
            if (node instanceof TextField && node != txtPackageID) {
                ((TextField) node).setEditable(true);
            } else if (node instanceof TextArea) {
                ((TextArea) node).setEditable(true);
            } else if (node instanceof DatePicker) {
                ((DatePicker) node).setEditable(true);
            }
        }
    }

    /**
     * Disables editing in all text fields in form.
     */
    public void disableTextEdit() {
        for (Node node : grid_travelpackage.getChildren()) {
            if (node instanceof TextField && node != txtPackageID) {
                ((TextField) node).setEditable(false);
            } else if (node instanceof TextArea) {
                ((TextArea) node).setEditable(false);
            } else if (node instanceof DatePicker) {
                ((DatePicker) node).setEditable(false);
            }
        }
    }

    /**
     * Clears all text fields on the form.
     */
    private void clearFields() {
        for (Node node : grid_travelpackage.getChildren()) {
            if (node instanceof TextField && node != txtPackageID) {
                ((TextField) node).setText("");
                txtPackageID.setText("");
                txtPackageName.setText("");
                txtAgencyCommission.setText("");
                txtareaPackageDescription.setText("");
                txtBasePrice.setText("");
                txtStartTimestamp.setText("");
                txtEndTimestamp.setText("");
            } else if (node instanceof TextArea) {
                ((TextArea) node).setText("");
                txtareaPackageDescription.setText("");
            } else if (node instanceof DatePicker) {
                ((DatePicker) node).setValue(null);
                datepicker_PackageStartDate.setValue(null);
                datepicker_PackageEndDate.setValue(null);
            }
        }
    }

    /**
     * Disables committing the adding of a new Travel Package to the database. Disables add button, enables edit and new button.
     */
    public void disableAdd() {
        btnAdd.setDisable(true);

        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);

        grid_travelpackage.setStyle("-fx-background-color: transparent");

        disableTextEdit();
    }

    /**
     * Create New Travel Package
     */
    private Package CreateNewTravelPackage() {
		SetDateTime setDateTime = new SetDateTime().invoke();
		LocalDateTime localDateTime_StartDate = setDateTime.getLocalDateTime_startDate();
		LocalDateTime localDateTime_EndDate = setDateTime.getLocalDateTime_endDate();

		//System.out.println(localDateTime_StartDate);
        //System.out.println(localDateTime_EndDate);

        return new Package(
                0,
                txtPackageName.getText(),
                localDateTime_StartDate,
                localDateTime_EndDate,
                txtareaPackageDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtBasePrice.getText())),
                BigDecimal.valueOf(Double.parseDouble(txtAgencyCommission.getText()))
        );
    }

	/**
     * Create Temporary Travel Package
     */
    private Package CreateTemporaryTravelPackage() {

    	SetDateTime setDateTime = new SetDateTime().invoke();
		LocalDateTime localDateTime_StartDate = setDateTime.getLocalDateTime_startDate();
		LocalDateTime localDateTime_EndDate = setDateTime.getLocalDateTime_endDate();

        //System.out.println(localDateTime_StartDate);
        //System.out.println(localDateTime_EndDate);

        return new Package(
                Integer.parseInt(txtPackageID.getText()),
                txtPackageName.getText(),
                localDateTime_StartDate,
                localDateTime_EndDate,
                txtareaPackageDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtBasePrice.getText())),
                BigDecimal.valueOf(Double.parseDouble(txtAgencyCommission.getText()))
        );
    }

    /**
     * Disables editing for the Travel Package information on the form. Enables edit button and disables save button.
     */
    public void disableSave() {
        btnSave.setDisable(true);

        btnEdit.setDisable(false);
        btnNew.setDisable(false);
        btnDelete.setDisable(false);

        grid_travelpackage.setStyle("-fx-background-color: transparent");

        disableTextEdit();
    }

	private class SetDateTime {
		private LocalDateTime localDateTime_startDate;
		private LocalDateTime localDateTime_endDate;

		public LocalDateTime getLocalDateTime_startDate() {
			return localDateTime_startDate;
		}

		public LocalDateTime getLocalDateTime_endDate() {
			return localDateTime_endDate;
		}

		public SetDateTime invoke() {
			// Get as Local Dates
			LocalDate localDate_start = datepicker_PackageStartDate.getValue();
			LocalDate localDate_end = datepicker_PackageEndDate.getValue();

			// Get as Local Time
			LocalTime localTime_start = LocalTime.parse(txtStartTimestamp.getText());
			LocalTime localTime_end = LocalTime.parse(txtEndTimestamp.getText());

			// Put Local Date and Local Time to create LocalDateTime
			//	-	To be passed to the SQL Helper
			localDateTime_startDate = LocalDateTime.of(localDate_start, localTime_start);
			localDateTime_endDate = LocalDateTime.of(localDate_end, localTime_end);
			return this;
		}
	}
}
