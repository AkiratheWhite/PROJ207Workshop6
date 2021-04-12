package data.entity;

import java.util.ArrayList;
import java.util.List;

public interface Entity {
     public default List<Object> AllProps() throws IllegalAccessException {
        return new ArrayList<>();
    }
}
