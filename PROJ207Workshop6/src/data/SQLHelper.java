package data;

import data.entity.Entity;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class SQLHelper {

    public static Map<String, Class<?>> TYPE;

    static
    {
        TYPE = new HashMap<>();

        TYPE.put("INT", Integer.class);
        TYPE.put("INTEGER", Integer.class);
        TYPE.put("TINYINT", Byte.class);
        TYPE.put("SMALLINT", Short.class);
        TYPE.put("BIGINT", Long.class);
        TYPE.put("REAL", Float.class);
        TYPE.put("FLOAT", Double.class);
        TYPE.put("DOUBLE", Double.class);
        TYPE.put("DECIMAL", BigDecimal.class);
        TYPE.put("NUMERIC", BigDecimal.class);
        TYPE.put("BOOLEAN", Boolean.class);
        TYPE.put("CHAR", String.class);
        TYPE.put("VARCHAR", String.class);
        TYPE.put("LONGVARCHAR", String.class);
        TYPE.put("DATE", Date.class);
        TYPE.put("TIME", Time.class);
        // ...
    }

    /**
     * Populates an inputted List<?> based on a ResultSet and Class Type.
     * @param result ResultSet that is returned from an SQL query.
     * @param EntityType Entity class that corresponds to the database table (e.g. 'Agent' object for 'agents' database table).
     * @throws IllegalArgumentException Throws an IllegalArgument exception if the # of columns in the database table does not match the # of declared attributes in the object class.
     */
    public static List<Object> CreateList (ResultSet result, Class<?> EntityType) throws Exception {
        List<Object> ObjList = new ArrayList<>();

        //Do nothing if an empty ResultSet is inputted.
        if (result == null) return null;

        Class<?>[] ClassArgs = new Class[EntityType.getDeclaredFields().length];
        ResultSetMetaData metaData = result.getMetaData();
        int NumOfCol = metaData.getColumnCount();

        //Throw exception if the object class is mismatched to the database table.
        if (EntityType.getDeclaredFields().length != metaData.getColumnCount()) {
            throw new IllegalArgumentException("Entity provided does not match table meta data.");
        }

        //Gets all of the class types for the properties in each column.
        for(int i = 0; i < NumOfCol; i++)
        {
            ClassArgs[i] = TYPE.get(metaData.getColumnTypeName(i+1));
        }

        //Adds records from the ResultSet to the List<Object>.
        while (result.next()) {
            //Array to hold all of the constructor arguments.
            Object[] objArgs = new Object[metaData.getColumnCount()];

            //Populating the object constructor arguments using HashMap.
            for(int i = 0; i < NumOfCol; i++)
            {
                objArgs[i] = result.getObject(i+1);
            }

            //Attempt to create a new instance of the object and add it to the ArrayList.
            try {
                Constructor<?> co = EntityType.getConstructor(ClassArgs);
                ObjList.add(co.newInstance(objArgs));
            } catch (Exception err) {
                System.out.println("Error generating objects: " + err.getClass());
            }
        }

        return ObjList;
    }

    /**
     * Method to create an update statement using a database table's metadata and an object with properties that corresponds to the table.
     * @param metaData Metadata supplied by the MySQL class.
     * @param DataType The object with the class that is mapped to the metadata of the table (e.g. 'Agent' object is matched to 'agents' table.)
     * @return A string for the SET clause of a SQL UPDATE statement.
     * @throws SQLException Custom SQLException to be thrown if the object's properties do not match the table's metadata.
     * @throws IllegalAccessException Uses the Entity interface's method to scan private properties, can potentially throw an IllegalAccessException.
     */
    public static String CreateUpdateStatement (ResultSetMetaData metaData, Entity DataType) throws SQLException, IllegalAccessException {

        //Throws an exception on object property and metadata mismatch.
        if (DataType.getClass().getDeclaredFields().length != metaData.getColumnCount()) {
            throw new SQLException("Error while updating table: Mismatch between entity and table.");
        }

        StringBuilder sb = new StringBuilder();

        //Creates a string for a SET clause in a SQL UPDATE statement.
        //WARNING! Assumes that first column in the table is the primary key.
        for (int i = 2; i <= metaData.getColumnCount(); i++) {
            //Removes the comma from the string if it is the very last column to be added.
            if (i == metaData.getColumnCount()) {
                if (DataType.allProps().get(metaData.getColumnName(i)).getClass() == String.class) {
                    sb.append(String.format("%s='%s'", metaData.getColumnName(i), DataType.allProps().get(metaData.getColumnName(i))));
                } else {
                    sb.append(String.format("%s=%s", metaData.getColumnName(i), DataType.allProps().get(metaData.getColumnName(i))));
                }
            } else {
                if (DataType.allProps().get(metaData.getColumnName(i)).getClass() == String.class) {
                    sb.append(String.format("%s='%s',", metaData.getColumnName(i), DataType.allProps().get(metaData.getColumnName(i))));
                } else {
                    sb.append(String.format("%s=%s,", metaData.getColumnName(i), DataType.allProps().get(metaData.getColumnName(i))));
                }
            }
        }

        return sb.toString();
    }

    /**
     * Method to create an insert statement using a database table's metadata and an object with properties that corresponds to the table.
     * @param metaData Metadata supplied by the MySQL class.
     * @param DataType The object with the class that is mapped to the metadata of the table (e.g. 'Agent' object is matched to 'agents' table.)
     * @return A string array with 2 elements. The first index represents the columns to update, the second index represents the values to insert.
     * @throws SQLException Custom SQLException to be thrown if the object's properties do not match the table's metadata.
     * @throws IllegalAccessException Uses the Entity interface's method to scan private properties, can potentially throw an IllegalAccessException.
     */
    public static String[] CreateInsertStatement (ResultSetMetaData metaData, Entity DataType) throws SQLException, IllegalAccessException {

        //Throws an exception on object property and metadata mismatch.
        if (DataType.getClass().getDeclaredFields().length != metaData.getColumnCount()) {
            throw new SQLException("Error while updating table: Mismatch between entity and table.");
        }

        String[] InsertArgs = new String[2];
        StringBuilder intoSB = new StringBuilder("(");
        StringBuilder valuesSB = new StringBuilder("(");

        //Creates string that specifies which columns to update.
        for (int i = 2; i <= metaData.getColumnCount(); i++) {
            if (i == metaData.getColumnCount()) {
                intoSB.append(String.format("%s)", metaData.getColumnName(i)));
            } else {
                intoSB.append(String.format("%s, ", metaData.getColumnName(i)));
            }
        }
        InsertArgs[0] = intoSB.toString();

        //Creates string that specifies which values to insert into the columns.
        for (int i = 2; i <= metaData.getColumnCount(); i++) {
            if (i == metaData.getColumnCount()) {
                if (DataType.allProps().get(metaData.getColumnName(i)).getClass() == String.class) {
                    valuesSB.append(String.format("'%s')", DataType.allProps().get(metaData.getColumnName(i))));
                } else {
                    valuesSB.append(String.format("%s)", DataType.allProps().get(metaData.getColumnName(i))));
                }
            } else {
                if (DataType.allProps().get(metaData.getColumnName(i)).getClass() == String.class) {
                    valuesSB.append(String.format("'%s',", DataType.allProps().get(metaData.getColumnName(i))));
                } else {
                    valuesSB.append(String.format("%s,", DataType.allProps().get(metaData.getColumnName(i))));
                }
            }
        }
        InsertArgs[1] = valuesSB.toString();

        return InsertArgs;
    }
}
