package app.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import data.DBContext;
import data.SQLHelper;
import data.entity.Agent;
import data.entity.Booking;
import data.entity.BookingDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class BookingController {

    String url = "jdbc:mysql://localhost:3306/travelexperts";
    String username = "root";
    String password = "";

    //Instantiates database object.
    DBContext database = new DBContext(url, username, password);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<BookingDetail> lvBooking;

    @FXML
    private TextField BookingDetailId;

    @FXML
    private TextField ItineraryNo;

    @FXML
    private TextField Description;

    @FXML
    private TextField Destination;

    @FXML
    private TextField BasePrice;

    @FXML
    private TextField AgencyCommision;

    @FXML
    private TextField BookingId;

    @FXML
    private TextField RegionId;

    @FXML
    private TextField ClassID;

    @FXML
    private TextField FeeId;

    @FXML
    private TextField ProductSupplierId;

    @FXML
    private TextField TripStart;

    @FXML
    private TextField TripEnd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClear;

    @FXML
    private GridPane BookingInfo;

    @FXML TextArea txtConsole;
    @FXML
    void initialize() {
        RefreshBookingListView();
        btnAdd.setDisable(true);
        btnSave.setDisable(true);
    }

    public List<BookingDetail> fetchBooking() {
        try (Connection connection = database.OpenConnection()) {
            ResultSet rs = database.Select("*").Table("bookingdetails").ExecuteQuery(connection);
            ObservableList<BookingDetail> list = FXCollections.observableArrayList();
            while (rs.next())
            {
                list.add(new BookingDetail(rs.getInt("BookingDetailid"),
                        rs.getDouble("ItineraryNo"),
                        rs.getString("TripStart"),
                        rs.getString("TripEnd"),
                        rs.getString("Description"),
                        rs.getString("Destination"),
                        rs.getDouble("BasePrice"),
                        rs.getDouble("AgencyCommission"),
                        rs.getInt("BookingId"),
                        rs.getString("RegionId"),
                        rs.getString("ClassId"),
                        rs.getString("FeeId"),
                        rs.getInt("ProductSupplierId")
                        ));
            }
            return list;
        } catch (Exception err) {
           txtConsole.setText("Error retrieving data: " + err.getMessage());
            return null;
        }

    }
    public void ShowBookingDetail() {
        BookingDetail b = (BookingDetail) lvBooking.getSelectionModel().getSelectedItem();

        if (b == null) {
            return;
        }

        BookingDetailId.setText(b.getBookingDetailId() + "");
        ItineraryNo.setText(b.getItineraryNo() + "");
        TripStart.setText(b.getTripStart() + "");
        TripEnd.setText(b.getTripEnd() + "");
        Description.setText(b.getDescription());
        Destination.setText(b.getDestination());
        BasePrice.setText(b.getBasePrice() + "");
        AgencyCommision.setText(b.getAgencyCommission()+"");
        BookingId.setText(b.getBookingId() +"");
        RegionId.setText(b.getRegionId()+"");
        ClassID.setText(b.getClassId());
        FeeId.setText(b.getFeeId());
        ProductSupplierId.setText(b.getProductSupplierId()+"");
    }
    /**
     * Creates a new Agent object from the text fields and tries to save this data to the database.
     */
    public void saveEdit() {
        try {
            BookingDetail b = CreateTempBooking();

            //Attempt to create and run a SQL update statement on the database.
            try (Connection connection = database.OpenConnection()) {
                if (database.Table("bookingdetails").ExecuteUpdate(connection, b, b.getPrimaryKey())) {
                    RefreshBookingListView();
                    txtConsole.setText("Edit saved to database.");
                } else {
                   txtConsole.setText("Saving edit failed.");
                }
            } catch (Exception err) {
               txtConsole.setText("Database error: " + err.getMessage());
            }

        } catch (Exception err) {
           txtConsole.setText("Error while creating Booking object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage());
        }

        disableSave();
    }

    /**
     * Adds a new agent to the database.
     */
    public void commitAdd() {
        try {
            BookingDetail b = CreateNewBooking();

            //Attempt to create and run a SQL update statement on the database.
            try (Connection connection = database.OpenConnection()) {
                if (database.Table("bookingdetails").ExecuteInsert(connection, b)) {
                    RefreshBookingListView() ;
                    txtConsole.setText("New Booking added to database.");
                } else {
                    txtConsole.setText("Adding new Booking failed.");
                }
            } catch (Exception err) {
                txtConsole.setText("Database error: " + err.getMessage());
            }

        } catch (Exception err) {
           txtConsole.setText("Error while creating Booking object. (Did you leave a text field empty?)\n" + err.getClass() + ", " + err.getMessage());
        }

        disableAdd();
    }

    /**
     * Operation to delete a selected agent from the database.
     */
    public void deleteBooking() {
        BookingDetail b = (BookingDetail) lvBooking.getSelectionModel().getSelectedItem();

        if (b == null) {
            txtConsole.setText("No booking specified.");
            return;
        }

        try (Connection connection = database.OpenConnection()) {
            if (database.Table("bookingdetails").ExecuteDelete(connection, b)) {
                RefreshBookingListView();
                txtConsole.setText("Booking deleted from database.");
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
    private void RefreshBookingListView() {
        lvBooking.setItems(FXCollections.observableArrayList(fetchBooking()));
    }

    private BookingDetail CreateTempBooking() {
        return new BookingDetail(
                Integer.parseInt(BookingDetailId.getText()),
                Double.parseDouble(ItineraryNo.getText()),
                TripStart.getText(),
                TripEnd.getText(),
                Description.getText(),
                Destination.getText(),
                Double.parseDouble(BasePrice.getText()),
                Double.parseDouble(AgencyCommision.getText()),
                Integer.parseInt(BookingId.getText()),
                RegionId.getText(),
                ClassID.getText(),
                FeeId.getText(),
                Integer.parseInt(ProductSupplierId.getText())
        );
    }

    private BookingDetail CreateNewBooking() {
        return new BookingDetail(
                0,
                Double.parseDouble(ItineraryNo.getText()),
                TripStart.getText(),
                TripEnd.getText(),
                Description.getText(),
                Destination.getText(),
                Double.parseDouble(BasePrice.getText()),
                Double.parseDouble(AgencyCommision.getText()),
                Integer.parseInt(BookingId.getText()),
                RegionId.getText(),
                ClassID.getText(),
                FeeId.getText(),
                Integer.parseInt(ProductSupplierId.getText())
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

        BookingInfo.setStyle("-fx-background-color: #FFFACD");

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

        BookingInfo.setStyle("-fx-background-color: #FFFACD");

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

        BookingInfo.setStyle("-fx-background-color: transparent");

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

        BookingInfo.setStyle("-fx-background-color: transparent");

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

        BookingInfo.setStyle("-fx-background-color: transparent");

        clearFields();
        disableTextEdit();
    }

    /**
     * Enables all text fields in form to be edited.
     */
    public void enableTextEdit() {
        for (Node node : BookingInfo.getChildren()) {
            if (node instanceof TextField && node != BookingDetailId) {
                ((TextField) node).setEditable(true);
            }
        }
    }

    /**
     * Disables editing in all text fields in form.
     */
    public void disableTextEdit() {
        for (Node node : BookingInfo.getChildren()) {
            if (node instanceof TextField && node != BookingDetailId) {
                ((TextField) node).setEditable(false);
            }
        }
    }

    /**
     * Clears all text fields on the form.
     */
    private void clearFields() {
        for (Node node : BookingInfo.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setText("");
            }
        }
    }
}