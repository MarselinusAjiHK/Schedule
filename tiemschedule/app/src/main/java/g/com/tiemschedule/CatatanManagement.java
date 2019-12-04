package g.com.tiemschedule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CatatanManagement {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "CatatanTiemSchedulePref";
    public static final String KEY_LISTCATATAN = "catatan";
    public static final String KEY_PRIORITAS = "prioritas";
    public static final String KEY_ID = "id";
    public static final String KEY_SIZE = "size";

    public CatatanManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createCatatanData(List<CatatanDAO> mListCatatan){
        List<CatatanDAO> mList = new ArrayList<>();
        mList = mListCatatan;
        editor.putInt(KEY_SIZE, mList.size());
        for(int i = 0;i < mList.size(); i++){
            editor.putString(KEY_LISTCATATAN+i, mList.get(i).getCatatan());
            editor.putString(KEY_PRIORITAS+i, mList.get(i).getPrioritas());
            editor.putString(KEY_ID+i, mList.get(i).getIds());
        }
        editor.commit();
    }

    public List<CatatanDAO> getLastCatatan(){
        List<CatatanDAO> catatanDAOList = new ArrayList<>();

        int size = pref.getInt(KEY_SIZE, 0);
        for(int i=0;i<size;i++){
            CatatanDAO cttnDAO = new CatatanDAO(pref.getString(KEY_LISTCATATAN+i, null), pref.getString(KEY_ID+i,null), pref.getString(KEY_PRIORITAS+i,null));
            catatanDAOList.add(cttnDAO);
        }
        return catatanDAOList;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
}
