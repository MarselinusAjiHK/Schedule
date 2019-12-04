package g.com.tiemschedule;

import java.util.List;

public class ValueCatatan {
    List<CatatanDAO> result;
    String value;
    public List<CatatanDAO> getResult(){
        return result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
