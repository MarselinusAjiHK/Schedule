package g.com.tiemschedule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrioritasManagement {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "PrioritasTiemSchedulePref";

    public static final String KEY_JADWAL = "jadwal";
    public static final String KEY_WAKTU = "waktu";
    public static final String KEY_TANGGAL = "tanggal";
    public static final String KEY_TEMPAT = "tempat";
    public static final String KEY_ID = "id";
    public static final String KEY_PRIORITAS = "prioritas";
    public static final String KEY_SIZE = "size";

    public PrioritasManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createJadwalData(List<JadwalDAO> mListJadwal){
        editor.putInt(KEY_SIZE, mListJadwal.size());
        for(int i = 0;i < mListJadwal.size(); i++){
            editor.putString(KEY_JADWAL+i, mListJadwal.get(i).getJadwal());
            editor.putString(KEY_TANGGAL+i, mListJadwal.get(i).getTanggal());
            editor.putString(KEY_WAKTU+i, mListJadwal.get(i).getWaktu());
            editor.putString(KEY_TEMPAT+i, mListJadwal.get(i).getTempat());
            editor.putString(KEY_PRIORITAS+i, mListJadwal.get(i).getPrioritas());
            editor.putString(KEY_ID+i, mListJadwal.get(i).getId());
        }
        editor.commit();
    }

    public List<JadwalDAO> getLastJadwal(){
        List<JadwalDAO> jadwalDAOList = new ArrayList<>();

        int size = pref.getInt(KEY_SIZE, 0);
        for(int i=0;i<size;i++){
            JadwalDAO jadwalDAO= new JadwalDAO(pref.getString(KEY_JADWAL+i, null), pref.getString(KEY_WAKTU+i, null), pref.getString(KEY_TANGGAL+i, null),pref.getString(KEY_TEMPAT+i, null), pref.getString(KEY_ID+i, null), pref.getString(KEY_PRIORITAS+i, null));
            jadwalDAOList.add(jadwalDAO);
        }
        return jadwalDAOList;
    }
    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

}
