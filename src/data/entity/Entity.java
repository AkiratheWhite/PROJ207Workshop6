package data.entity;

import java.util.HashMap;

/**
 * Code written by: Tony (Zongzheng) Li
 * Last Modified:
 */

public interface Entity {
    HashMap<String, Object> allProps() throws IllegalAccessException;
    Object getPrimaryKey();
}
