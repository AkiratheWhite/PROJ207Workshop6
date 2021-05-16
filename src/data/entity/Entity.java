package data.entity;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Code written by: Tony (Zongzheng) Li
 * Changelog:
 * [1.01|Tony Li|05/06/21] - Change Agent.class to this.getClass() to prevent method errors.
 * Last modified on (MM/DD/YY): 05/14/21
 */

public interface Entity {

    //Returns a HashMap containing all the property names and values of an instance of an Entity.
    default HashMap<String, Object> allProps() throws IllegalAccessException {
        HashMap<String, Object> Props = new HashMap<>();

        for (Field Property : this.getClass().getDeclaredFields()) {
            Property.setAccessible(true);
            Props.put(Property.getName(), Property.get(this));
        }

        return Props;
    }

    Object getPrimaryKey();
}