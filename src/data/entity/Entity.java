package data.entity;

import java.util.HashMap;

public interface Entity {
    default HashMap<String, Object> AllProps() throws IllegalAccessException {
        return new HashMap<>();
    }
}
