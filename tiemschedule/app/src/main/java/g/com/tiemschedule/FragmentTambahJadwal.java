package g.com.tiemschedule;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentTambahJadwal extends Fragment {

    private Switch mPrioritas;
    private Button mSimpan;
    private Button mBatal;
    private EditText mKegiatan;
    private EditText mMulai;
    private EditText mTanggal;
    private EditText mTempat;
    private android.support.design.widget.TextInputLayout layout;
    public int sPrioritas = 0;
    public String sId;
    View myView;
    Calendar myCalendar = Calendar.getInstance();
    public ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.tambah_jadwal, container, false);
        setAttribut(myView);
        ((dashboardActivity)getActivity()).setTitleCustom("Tambah Jadwal");
        sId = ((dashboardActivity)getActivity()).getIdUser();
        progress = new ProgressDialog(getActivity());
        mSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progress.setMessage("Mengirim jadwal . . ");
                    progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progress.setIndeterminate(true);
                    progress.show();
                    onClickAddJadwal();
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
        mTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();

            }
        });

        mTanggal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePicker();
                }
            }
        });

        mMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

       mMulai.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               if(hasFocus){
                   showTimePicker();
               }
           }
       });

        return myView;
    }

    private void setAttribut(View rootView){
        mKegiatan = (EditText) rootView.findViewById(R.id.kegiatan);
        mMulai = (EditText) rootView.findViewById(R.id.Mulai);
        mTanggal = (EditText) rootView.findViewById(R.id.tanggal);
        mTempat = (EditText) rootView.findViewById(R.id.tempat);
        mSimpan = (Button) rootView.findViewById(R.id.simpan);
        mBatal = (Button) rootView.findViewById(R.id.batal);
        mPrioritas = (Switch) rootView.findViewById(R.id.checkberanda);
        layout = rootView.findViewById(R.id.inputTanggalLayout);
    }
    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public EditText getmTanggal() {
        return mTanggal;
    }

    public void setmTanggal(String dataTanggal) {
        this.mTanggal.setText("coba");
    }

    private void onClickAddJadwal() throws JSONException {

        if(mKegiatan.getText().toString().isEmpty() || mMulai.getText().toString().isEmpty() || mTanggal.getText().toString().isEmpty() || mTempat.getText().toString().isEmpty()){

            progress.dismiss();
            Toast.makeText(getActivity(),"Jadwal tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else if(sPrioritas == -1) {

            progress.dismiss();
            Toast.makeText(getActivity(),"Prioritas belum ditentukan", Toast.LENGTH_SHORT).show();
        }else{
            Retrofit.Builder builder = new Retrofit
                    .Builder()
                    .baseUrl("https://tiemschedule.thekingcorp.org/jadwal/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            ApiJadwal apiJadwal = retrofit.create(ApiJadwal.class);


            Call<JadwalDAO> jadwalDAOCall = apiJadwal.addJadwal(mKegiatan.getText().toString(),mMulai.getText().toString(),mTanggal.getText().toString(),mTempat.getText().toString(),sPrioritas,this.getsId());
            jadwalDAOCall.enqueue(new Callback<JadwalDAO>() {
                @Override
                public void onResponse(Call<JadwalDAO> call, Response<JadwalDAO> response) {

                    progress.dismiss();
                    if(response.body().getValue().toString().equals("200")) {
                        replaceFragment();
                    }
                }

                @Override
                public void onFailure(Call<JadwalDAO> call, Throwable t) {

                    progress.dismiss();
                    Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    private void replaceFragment(){
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelay, new FragmentJadwal());
        ft.commit();
    }

    public void showDatePicker() {
        new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mTanggal.setText(new StringBuilder().append(year).append("-")
                    .append(monthOfYear+1).append("-").append(dayOfMonth));

        }

    };
    private void showTimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String jam = Integer.toString(selectedHour);
                String menit = Integer.toString(selectedMinute);
                if(selectedHour < 10){
                    jam = "0"+jam;
                }
                if(selectedMinute < 10){
                    menit = "0"+menit;
                }

                mMulai.setText( jam + ":" + menit);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
