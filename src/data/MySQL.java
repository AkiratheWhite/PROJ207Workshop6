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

        if (this.args == "" || this.args == null) {
            sql = String.format("SELECT %s FROM %s", select, table);
        } else {
            sql = String.format("SELECT %s FROM %s %s", select, table, args);
        }

        return connection.createStatement().executeQuery(sql);
    }

    /**
     * Executes and SQL update statement on the table that has been set using the arguments that have been set.
     * @throws SQLException
     */
    public void ExecuteUpdate (Connection connection, Entity entity) throws SQLException {
       PreparedStatement statement = entity.CreateUpdateSQL(connection);
       statement.executeUpdate();
    }
}
