package data;

import data.entity.Entity;

import java.sql.*;

/**
 * Code written by: Tony (Zongzheng) Li
 * Last Modified:
 */
public class MySQL {
    private final String url;
    private final String username;
    private final String password;
    private String select;
    private String table;
    private String args;

    /**
     * Constructor for new database object.
     * @param url Database connection string.
     * @param username Username for access member.
     * @param password Password for access member.
     */
    public MySQL (String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Opens the connection to the database.
     */
    public Connection OpenConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Builder utility to add SELECT values to SQL query.
     * @param select Columns to return in query.
     * @return Returns this object with the SELECT parameters set.
     */
    public MySQL Select(String select) {
        this.select = select;
        return this;
    }

    /**
     * Builder utility to add FROM values to SQL query.
     * @param table Table to retrieve data from.
     * @return Returns this object with the FROM parameter set.
     */
    public MySQL Table(String table) {
        this.table = table;
        return this;
    }

    /**
     * Builder utility to add WHERE, ORDER BY, and any other arguments after FROM statement to SQL query.
     * @param args Additional SQL arguments to be passed.
     * @return Returns this object with extra arguments set.
     */
    public MySQL Args(String args) {
        this.args = args;
        return this;
    }

    /**
     * Executes an SQL statement if arguments have been set.
     * @return ResultSet corresponding to the SQL query executed.
     * @throws SQLException
     */
    public ResultSet ExecuteQuery(Connection connection) throws SQLException {
        String sql;

        if (this.args.equals("")) {
            sql = String.format("SELECT %s FROM %s", select, table);
        } else {
            sql = String.format("SELECT %s FROM %s %s", select, table, args);
        }

        return connection.createStatement().executeQuery(sql);
    }

    /**
     * Method to execute an update statement on a table.
     * @param connection Connection object created from MySQL instance.
     * @param DataClass The object class that corresponds to the records in the table you're trying to update.
     * @param PrimaryKey The primary key that corresponds to the record you're trying to update.
     * @return True if updated succeeded, false if update failed.
     * @throws IllegalAccessException Uses the Entity interface's method to scan private properties, can potentially throw an IllegalAccessException.
     */
    public boolean ExecuteUpdate (Connection connection, Entity DataClass, int PrimaryKey) throws IllegalAccessException {
        boolean UpdateSucceeded = false; //Assume the update has not succeeded first.

        //If the user has not used the builder function to specify a table, automatically fail the update.
        if (this.table.equals("")) {
            System.out.println("No table specified.");
            return false;
        }

        try {
            //Uses the database metadata to find primary key of the specified table table.
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rs = dbMetaData.getPrimaryKeys(null, null, this.table);
            rs.next();

            //Executes a query that will return an empty ResultSet. Obtains metadata from the specified table.
            String MetaDataSql = String.format("SELECT * FROM %s WHERE 0 = 1", this.table);
            ResultSetMetaData metaData = connection.createStatement().executeQuery(MetaDataSql).getMetaData();

            //Creates SQL UPDATE statement using the SQLHelperClass
            String UpdateSQL = String.format("UPDATE %s SET %s WHERE %s = %s", this.table, SQLHelper.CreateUpdateStatement(metaData, DataClass), rs.getString("COLUMN_NAME"), PrimaryKey);

            //Executes the SQL UPDATE statement.
            PreparedStatement statement = connection.prepareStatement(UpdateSQL);
            statement.executeUpdate();
            UpdateSucceeded = true;

        } catch (SQLException err) {
            System.out.println("Error: " + err.getClass() + ", " + err.getMessage());
        }

        return UpdateSucceeded;
    }
}
