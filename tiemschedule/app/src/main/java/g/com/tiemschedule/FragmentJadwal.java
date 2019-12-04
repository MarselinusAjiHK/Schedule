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

public class FragmentJadwal extends Fragment {
    View myView;
    public JadwalManagement jdwlManagement;
    private List<JadwalDAO> mListJadwal= new ArrayList<>();
    private RecyclerView recyclerView;
    public RecycleAdapterJadwal recycleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String sId;

    public TextView alertJadwal;
    public String dataListJadwal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.jadwal, container, false);
        ((dashboardActivity)getActivity()).setTitleCustom("Jadwal");
        sId = ((dashboardActivity)getActivity()).getIdUser();
        alertJadwal = (TextView) myView.findViewById(R.id.textAlertJadwal);
        recyclerView = (RecyclerView) myView.findViewById(R.id.jadwal_view);
        jdwlManagement = new JadwalManagement(getActivity().getApplicationContext());
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
            mListJadwal = jdwlManagement.getLastJadwal();
        }
        recycleAdapter = new RecycleAdapterJadwal(getActivity(), mListJadwal);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        try{
            recyclerView.setAdapter(recycleAdapter);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        setRecycleView();

        return myView;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    private void setRecycleView(){
        Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl("https://tiemschedule.thekingcorp.org/jadwal/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiJadwal apiJadwal= retrofit.create(ApiJadwal.class);
        Call<ValueJadwal> jadwalDAOCall= apiJadwal.getJadwal(this.getsId());

        jadwalDAOCall.enqueue(new Callback<ValueJadwal>() {
            @Override
            public void onResponse(Call<ValueJadwal> call, Response<ValueJadwal> response) {
                recycleAdapter.notifyDataSetChanged();
                jdwlManagement.createJadwalData(response.body().getResult());

                if(response.body().getResult().get(0).getId().equals("-1")){
                    alertJadwal.setVisibility(View.VISIBLE);
                    jdwlManagement.logoutUser();
                }else {
                    alertJadwal.setVisibility(View.INVISIBLE);
                    recycleAdapter = new RecycleAdapterJadwal(getActivity(), response.body().getResult());
                    recyclerView.setAdapter(recycleAdapter);
                }
            }

            @Override
            public void onFailure(Call<ValueJadwal> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}