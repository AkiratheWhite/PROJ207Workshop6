package data.entity;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Code written by: Tony (Zongzheng) Li
 * Last Modified:
 */

public interface Entity {
    default HashMap<String, Object> allProps() throws IllegalAccessException {
        HashMap<String, Object> Props = new HashMap<>();

        for (Field Property : Agent.class.getDeclaredFields()) {
            Property.setAccessible(true);
            Props.put(Property.getName(), Property.get(this));
        }

        return Props;
    }

    Object getPrimaryKey();
}
