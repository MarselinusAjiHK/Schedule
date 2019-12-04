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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentCatatan extends Fragment {
    View myView;
    public CatatanManagement cttnManagemen;
    public List<CatatanDAO> mListCatatan = new ArrayList<>();
    private RecyclerView recyclerView;
    public RecycleAdapterCatatan recycleAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String sId;

    public TextView alert;
    public String dataListCatatan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.catatan, container, false);

        ((dashboardActivity)getActivity()).setTitleCustom("Catatan");
        sId = ((dashboardActivity)getActivity()).getIdUser();
        alert = (TextView) myView.findViewById(R.id.textAlert);


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
            mListCatatan = cttnManagemen.getLastCatatan();

        }

        cttnManagemen = new CatatanManagement(getActivity().getApplicationContext());
        recyclerView = (RecyclerView) myView.findViewById(R.id.catatan_view);
        recycleAdapter = new RecycleAdapterCatatan(getActivity(), getmListCatatan());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        try{
            recyclerView.setAdapter(recycleAdapter);
        }catch (NullPointerException e){
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
                .baseUrl("https://tiemschedule.thekingcorp.org/catatan/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiCatatan apiCatatan= retrofit.create(ApiCatatan.class);
        Call<ValueCatatan> catatanDAOCall = apiCatatan.getCatatan(this.getsId());

        catatanDAOCall.enqueue(new Callback<ValueCatatan>() {
            @Override
            public void onResponse(Call<ValueCatatan> call, Response<ValueCatatan> response) {
                recycleAdapter.notifyDataSetChanged();
                setmListCatatan(response.body().getResult());
                cttnManagemen.createCatatanData(getmListCatatan());
                if(response.body().getResult().get(0).getIds().equals("-1")){
                    alert.setVisibility(View.VISIBLE);
                    cttnManagemen.logoutUser();
                }else {
                    alert.setVisibility(View.INVISIBLE);
                    recycleAdapter = new RecycleAdapterCatatan(getActivity(), getmListCatatan());
                    recyclerView.setAdapter(recycleAdapter);
                }
            }

            @Override
            public void onFailure(Call<ValueCatatan> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<CatatanDAO> getmListCatatan() {
        return mListCatatan;
    }

    public void setmListCatatan(List<CatatanDAO> mListCatatan) {
        this.mListCatatan = mListCatatan;
    }
}