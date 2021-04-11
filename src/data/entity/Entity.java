package data.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Entity {
    public PreparedStatement CreateUpdateSQL(Connection connection) throws SQLException;
}
