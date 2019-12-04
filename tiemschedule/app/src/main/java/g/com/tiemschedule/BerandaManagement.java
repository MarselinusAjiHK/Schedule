package g.com.tiemschedule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BerandaManagement {

    SharedPreferences prefCatatan;
    SharedPreferences prefJadwal;
    Editor editorCatatan;
    Editor editorJadwal;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME_CATATAN = "BerandaCatatanTiemSchedulePref";
    private static final String PREF_NAME_JADWAL = "BerandaJadwalTiemSchedulePref";
    public static final String KEY_LISTCATATAN = "catatan";
    public static final String KEY_PRIORITAS = "prioritas";
    public static final String KEY_ID = "id";
    public static final String KEY_SIZE = "size";

    public static final String KEY_JADWAL = "jadwal";
    public static final String KEY_WAKTU = "waktu";
    public static final String KEY_TANGGAL = "tanggal";
    public static final String KEY_TEMPAT = "tempat";

    public BerandaManagement(Context context){
        this._context = context;
        prefCatatan = _context.getSharedPreferences(PREF_NAME_CATATAN, PRIVATE_MODE);
        prefJadwal = _context.getSharedPreferences(PREF_NAME_JADWAL, PRIVATE_MODE);
        editorCatatan = prefCatatan.edit();
        editorJadwal = prefJadwal.edit();
    }


    public void createCatatanData(List<CatatanDAO> mListCatatan){
        if(mListCatatan.get(0).getIds().equals("-1")){
            this.clearCatatan();
        }else{
            editorCatatan.putInt(KEY_SIZE, mListCatatan.size());
            for(int i = 0;i < mListCatatan.size(); i++){
                editorCatatan.putString(KEY_LISTCATATAN+i, mListCatatan.get(i).getCatatan());
                editorCatatan.putString(KEY_PRIORITAS+i, mListCatatan.get(i).getPrioritas());
                editorCatatan.putString(KEY_ID+i, mListCatatan.get(i).getIds());
            }
            editorCatatan.commit();
        }

    }

    public List<CatatanDAO> getLastCatatan(){
        List<CatatanDAO> catatanDAOList = new ArrayList<>();
        int size = prefCatatan.getInt(KEY_SIZE, 0);
        for(int i=0;i<size;i++){
            CatatanDAO cttnDAO = new CatatanDAO(prefCatatan.getString(KEY_LISTCATATAN+i, null), prefCatatan.getString(KEY_ID+i,null), prefCatatan.getString(KEY_PRIORITAS+i,null));
            catatanDAOList.add(cttnDAO);
        }

        return catatanDAOList;
    }

    public void createJadwalData(List<JadwalDAO> mListJadwal){
        editorJadwal.putInt(KEY_SIZE, mListJadwal.size());
        for(int i = 0;i < mListJadwal.size(); i++){
            editorJadwal.putString(KEY_JADWAL+i, mListJadwal.get(i).getJadwal());
            editorJadwal.putString(KEY_TANGGAL+i, mListJadwal.get(i).getTanggal());
            editorJadwal.putString(KEY_WAKTU+i, mListJadwal.get(i).getWaktu());
            editorJadwal.putString(KEY_TEMPAT+i, mListJadwal.get(i).getTempat());
            editorJadwal.putString(KEY_PRIORITAS+i, mListJadwal.get(i).getPrioritas());
            editorJadwal.putString(KEY_ID+i, mListJadwal.get(i).getId());
        }
        editorJadwal.commit();
    }

    public List<JadwalDAO> getLastJadwal(){
        List<JadwalDAO> jadwalDAOList = new ArrayList<>();

        int size = prefJadwal.getInt(KEY_SIZE, 0);
        for(int i=0;i<size;i++){
            JadwalDAO jadwalDAO= new JadwalDAO(prefJadwal.getString(KEY_JADWAL+i, null), prefJadwal.getString(KEY_WAKTU+i, null), prefJadwal.getString(KEY_TANGGAL+i, null),prefJadwal.getString(KEY_TEMPAT+i, null), prefJadwal.getString(KEY_ID+i, null), prefJadwal.getString(KEY_PRIORITAS+i, null));
            jadwalDAOList.add(jadwalDAO);
        }
        return jadwalDAOList;
    }
    public void logoutUser(){
        editorJadwal.clear();
        editorJadwal.commit();
        editorCatatan.clear();
        editorCatatan.commit();
    }

    public void clearCatatan(){
        editorCatatan.clear();
        editorCatatan.commit();
    }
    public void clearJadwal(){
        editorJadwal.clear();
        editorJadwal.commit();
    }
}
