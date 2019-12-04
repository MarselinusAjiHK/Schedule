package g.com.tiemschedule;

import java.util.List;

public class ValueJadwal {
    List<JadwalDAO> result;
    String value;
    public List<JadwalDAO> getResult(){
        return result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
