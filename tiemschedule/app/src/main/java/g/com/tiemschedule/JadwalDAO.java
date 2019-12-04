package g.com.tiemschedule;

public class JadwalDAO {

    public String jadwal;
    public String waktu;
    public String tanggal;
    public String tempat;
    public String id;
    public String prioritas;
    String value;
    String message;

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(String prioritas) {
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

    public JadwalDAO(String jadwal, String waktu, String tanggal, String tempat, String id, String prioritas) {
        this.jadwal = jadwal;
        this.waktu = waktu;
        this.tanggal = tanggal;
        this.tempat = tempat;
        this.id = id;
        this.prioritas = prioritas;
    }
}
