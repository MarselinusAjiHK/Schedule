package g.com.tiemschedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentKalender extends Fragment {
    View myView;
    private String sId;
    private RecyclerView recyclerViewJadwal;
    public RecycleAdapterJadwal recycleAdapterJadwal;
    private List<JadwalDAO> mListJadwal= new ArrayList<>();
    public String dataListJadwal;
    public JadwalManagement jadwalManagement;
    public CalendarView calendarView;
    List<EventDay> events = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.kalender, container, false);
        ((dashboardActivity)getActivity()).setTitleCustom("Kalender");
        sId = ((dashboardActivity)getActivity()).getIdUser();


        jadwalManagement = new JadwalManagement(getActivity().getApplicationContext());
        getEvent();


        calendarView = (CalendarView) myView.findViewById(R.id.calendarView);
        calendarView.showCurrentMonthPage();
        return myView;
    }
    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
    public void getEvent(){
        Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl("https://tiemschedule.thekingcorp.org/jadwal/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiJadwal apiJadwal= retrofit.create(ApiJadwal.class);
        Call<ValueJadwal> jadwalDAOCall= apiJadwal.getDateById(this.getsId());

        jadwalDAOCall.enqueue(new Callback<ValueJadwal>() {
            @Override
            public void onResponse(Call<ValueJadwal> call, Response<ValueJadwal> response) {

                jadwalManagement.createJadwalData(response.body().getResult());
                if(response.body().getResult().get(0).getId().equals("-1")){
                    jadwalManagement.logoutUser();
                }else{
                    setEvent(response.body().getResult());
                }

            }

            @Override
            public void onFailure(Call<ValueJadwal> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setEvent(List<JadwalDAO> data){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();

        for(int i =0;i<data.size();i++){
            try {
                convertedDate = dateFormat.parse(data.get(i).getTanggal());
                events.add(new EventDay(toCalendar(convertedDate), R.drawable.shape_default));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        calendarView.setEvents(events);

    }
}