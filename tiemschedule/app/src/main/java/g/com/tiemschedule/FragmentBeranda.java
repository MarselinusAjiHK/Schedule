package g.com.tiemschedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentBeranda extends Fragment {
    View myView;
    private String sId;
    public String dataListCatatan;
    public String dataListJadwal;
    private List<CatatanDAO> mListCatatan = new ArrayList<>();
    private List<JadwalDAO> mListJadwal= new ArrayList<>();
    private RecyclerView recyclerViewCatatan;
    private RecyclerView recyclerViewJadwal;
    public RecycleAdapterCatatan recycleAdapterCatatan;
    public RecycleAdapterJadwal recycleAdapterJadwal;
    private RecyclerView.LayoutManager layoutManager;

    public BerandaManagement berandaManagement;
    public TextView alert;
    public TextView alertJadwal;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.beranda, container, false);

        ((dashboardActivity)getActivity()).setTitleCustom("Hari Ini");
        sId = ((dashboardActivity)getActivity()).getIdUser();
        alert = (TextView) myView.findViewById(R.id.textAlert);
        alertJadwal = (TextView) myView.findViewById(R.id.textAlertJadwal);

        recyclerViewCatatan = (RecyclerView) myView.findViewById(R.id.catatan_beranda_view);
        berandaManagement = new BerandaManagement(getActivity().getApplicationContext());

        try{
            dataListCatatan = mListCatatan.get(0).getIds();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            dataListCatatan = "-1";
        }
        if(dataListCatatan.equals("-1")){
            alert.setVisibility(View.VISIBLE);
        }else{
            alert.setVisibility(View.INVISIBLE);
            mListCatatan = berandaManagement.getLastCatatan();

        }



        recycleAdapterCatatan = new RecycleAdapterCatatan(getActivity(), mListCatatan);
        RecyclerView.LayoutManager mLayoutManagerCatatan = new LinearLayoutManager(getActivity());
        recyclerViewCatatan.setLayoutManager(mLayoutManagerCatatan);
        recyclerViewCatatan.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCatatan.setAdapter(recycleAdapterCatatan);

        try{
            dataListJadwal = mListJadwal.get(0).getId();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            dataListJadwal = "-1";
        }

        if(dataListJadwal.equals("-1")){
            alertJadwal.setVisibility(View.VISIBLE);
        }else{
            alertJadwal.setVisibility(View.INVISIBLE);
            mListJadwal = berandaManagement.getLastJadwal();
        }

        recyclerViewJadwal = (RecyclerView) myView.findViewById(R.id.jadwal_beranda_view);
        recycleAdapterJadwal = new RecycleAdapterJadwal(getActivity(), mListJadwal);
        RecyclerView.LayoutManager mLayoutManagerJadwal = new LinearLayoutManager(getActivity());
        recyclerViewJadwal.setLayoutManager(mLayoutManagerJadwal);
        recyclerViewJadwal.setItemAnimator(new DefaultItemAnimator());

        try{
            recyclerViewJadwal.setAdapter(recycleAdapterJadwal);
        }catch(NullPointerException e){
            e.printStackTrace();
        }



        setRecycleViewCatatan();
        setRecycleViewJadwal();
        return myView;
    }


    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    private void setRecycleViewCatatan(){
        Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl("https://tiemschedule.thekingcorp.org/catatan/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiCatatan apiCatatan= retrofit.create(ApiCatatan.class);
        Call<ValueCatatan> catatanDAOCall = apiCatatan.getPrioritas(this.getsId());

        catatanDAOCall.enqueue(new Callback<ValueCatatan>() {
            @Override
            public void onResponse(Call<ValueCatatan> call, Response<ValueCatatan> response) {
                recycleAdapterCatatan.notifyDataSetChanged();
                berandaManagement.createCatatanData(response.body().getResult());
                if(response.body().getResult().get(0).getIds().equals("-1")){
                    alert.setVisibility(View.VISIBLE);
                    berandaManagement.clearCatatan();
                }else{
                    alert.setVisibility(View.INVISIBLE);
                    recycleAdapterCatatan = new RecycleAdapterCatatan(getActivity(),response.body().getResult());
                    recyclerViewCatatan.setAdapter(recycleAdapterCatatan);
                }

            }

            @Override
            public void onFailure(Call<ValueCatatan> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setRecycleViewJadwal(){
        Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl("https://tiemschedule.thekingcorp.org/jadwal/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiJadwal apiJadwal= retrofit.create(ApiJadwal.class);
        Call<ValueJadwal> jadwalDAOCall= apiJadwal.getByDate(this.getsId());

        jadwalDAOCall.enqueue(new Callback<ValueJadwal>() {
            @Override
            public void onResponse(Call<ValueJadwal> call, Response<ValueJadwal> response) {
                recycleAdapterJadwal.notifyDataSetChanged();
                berandaManagement.createJadwalData(response.body().getResult());
                if(response.body().getResult().get(0).getId().equals("-1")){
                    alertJadwal.setVisibility(View.VISIBLE);
                    berandaManagement.clearJadwal();
                }else{
                    alertJadwal.setVisibility(View.INVISIBLE);
                    recycleAdapterJadwal = new RecycleAdapterJadwal(getActivity(),response.body().getResult());
                    recyclerViewJadwal.setAdapter(recycleAdapterJadwal);
                }

            }

            @Override
            public void onFailure(Call<ValueJadwal> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
