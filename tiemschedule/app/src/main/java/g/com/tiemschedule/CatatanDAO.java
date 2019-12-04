package g.com.tiemschedule;


public class CatatanDAO {
    public String catatan;
    public String id;
    public String prioritas;
    String value = "";
    String message = "";

    public CatatanDAO(String catatan, String id, String prioritas) {
        this.catatan = catatan;
        this.id = id;
        this.prioritas = prioritas;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getIds() {
        return id;
    }

    public void setIds(String id) {
        this.id = id;
    }

    public String getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(String prioritas) {
        this.prioritas = prioritas;
    }
}
