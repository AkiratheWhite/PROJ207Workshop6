package app.controllers.util;

public class TableProperty {
    public String Name;
    public Object Value;

    public TableProperty (String _Name, Object _Value) {
        this.Name = _Name;
        this.Value = _Value;
    }

    public String getName() {
        return Name;
    }

    public Object getValue() {
        return Value;
    }
}
