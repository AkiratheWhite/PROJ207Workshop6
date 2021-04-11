package data;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultData {

    public static Map<String, Class> TYPE;

    static
    {
        TYPE = new HashMap<String, Class>();

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
     * Populates an inputted ArrayList<Object> based on a ResultSet and Class Type.
     * @param result ResultSet that is returned from an SQL query.
     * @param ObjectList ArrayList that has been initialized and will be used to hold data retrieved from the database.
     * @param EntityType Entity class that corresponds to the database table (e.g. 'Agent' object for 'agents' database table).
     * @throws IllegalArgumentException Throws an IllegalArgument exception if the # of columns in the database table does not match the # of declared attributes in the object class.
     */
    public static void CreateList (ResultSet result, List<Object> ObjectList, Class<?> EntityType) throws Exception {

        //Do nothing if an empty ResultSet is inputted.
        if (result == null) return;

        Class[] ClassArgs = new Class[EntityType.getDeclaredFields().length];
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
                Constructor co = EntityType.getConstructor(ClassArgs);
                ObjectList.add(co.newInstance(objArgs));
            } catch (Exception err) {
                System.out.println("Error generating objects: " + err.getClass());
            }
        }
    }
}