package data;

public class Data {
    private Object data;
    private Class classType;

    public Data(Object data, Class classType) {
        this.data = data;
        this.classType = classType;
    }

    public Class getClassType() {
        return classType;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        if (data == null) {
            return "";
        } else {
            return data.toString();
        }
    }
}
