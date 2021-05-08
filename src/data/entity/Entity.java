package data.entity;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Code written by: Tony (Zongzheng) Li
 * Changelog:
 * [1.01|Tony Li|05/06/21] - Change Agent.class to this.getClass() to prevent method errors.
 */

public interface Entity {

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