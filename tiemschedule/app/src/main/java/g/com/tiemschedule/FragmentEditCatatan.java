package g.com.tiemschedule;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.content.Intent;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentEditCatatan extends Fragment {

    View myView;
    private Button mSimpan;
    private Button mBatal;
    public EditText mCatatan;
    private Switch mPrioritas;
    public int sPrioritas = 0;
    public String Prioritas;
    public String sId;
    public String id_catatan;
    public String catatan;
    public ProgressDialog progress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.catatan_tambah, container, false);
        mSimpan = (Button) myView.findViewById(R.id.simpan);
        mBatal = (Button) myView.findViewById(R.id.batal);
        sId = ((dashboardActivity)getActivity()).getIdUser();
        ((dashboardActivity)getActivity()).setTitleCustom("Edit Catatan");
        progress = new ProgressDialog(getActivity());
        setAttribut(myView);
        setValue(myView);
        mSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progress.setMessage("Mengirim Catatan . . ");
                    progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progress.setIndeterminate(true);
                    progress.show();
                    onClickAddCatatan();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment();
            }
        });
        if (mPrioritas != null) {
            mPrioritas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sPrioritas = 1;
                    } else {
                        sPrioritas = 0;
                    }
                }
            });
        }
        return myView;
    }

    public String getId_catatan() {
        return id_catatan;
    }

    public void setId_catatan(String id_catatan) {
        this.id_catatan = id_catatan;
    }

    public String getsId() {
        return id_catatan;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getPrioritas() {
        return Prioritas;
    }

    public void setPrioritas(String prioritas) {
        Prioritas = prioritas;
    }

    private void setAttribut(View rootView){
        mCatatan = (EditText) rootView.findViewById(R.id.inputCatatan);
        mPrioritas = (Switch) rootView.findViewById(R.id.checkberanda);
    }
    private void setValue(View rootView){
        mCatatan.setText(catatan);
        if(Prioritas.equals("1")){
            mPrioritas.setChecked(true);
            sPrioritas = 1;
        }else{
            mPrioritas.setChecked(false);
            sPrioritas = 0;
        }
    }

    private void onClickAddCatatan() throws JSONException {

        if(mCatatan.getText().toString().isEmpty()){
            progress.dismiss();
            Toast.makeText(getActivity(),"Catatan tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else if(sPrioritas == -1) {
            progress.dismiss();
            Toast.makeText(getActivity(),"Beranda belum ditentukan", Toast.LENGTH_SHORT).show();
        }else{
            Retrofit.Builder builder = new Retrofit
                    .Builder()
                    .baseUrl("https://tiemschedule.thekingcorp.org/catatan/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            ApiCatatan apiCatatan = retrofit.create(ApiCatatan.class);


            Call<CatatanDAO> catatanDAOCall = apiCatatan.updateCatatan(mCatatan.getText().toString(),sPrioritas,this.getId_catatan());
            catatanDAOCall.enqueue(new Callback<CatatanDAO>() {
                @Override
                public void onResponse(Call<CatatanDAO> call, Response<CatatanDAO> response) {
                    progress.dismiss();
                    if(response.body().getValue().toString().equals("200")) {
                        replaceFragment();
                    }
                }

                @Override
                public void onFailure(Call<CatatanDAO> call, Throwable t) {
                    progress.dismiss();

                    Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_SHORT).show();


                }
            });
        }
    }
    private void replaceFragment(){
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelay, new FragmentCatatan());
        ft.commit();
    }
}