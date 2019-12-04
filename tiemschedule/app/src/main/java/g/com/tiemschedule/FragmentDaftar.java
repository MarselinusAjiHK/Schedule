package g.com.tiemschedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.rtner.security.auth.spi.SimplePBKDF2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentDaftar extends Fragment  {
    private static final String TAG = "FragmentDaftar";
    private Button btnDaftar;
    private EditText mNama;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mKonfirmasi;
    private Date c = Calendar.getInstance().getTime();
    public ProgressDialog progress;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    private String formattedDate = df.format(c);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daftar, container, false);
        btnDaftar = (Button) rootView.findViewById(R.id.daftar);
        progress = new ProgressDialog(getActivity());
        setAttribut(rootView);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progress.setMessage("Mendaftar ");
                    progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progress.setIndeterminate(true);
                    progress.show();
                    onClickRegister();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;

    }

    private void setAttribut(View rootView){
        mNama = rootView.findViewById(R.id.nama);
        mEmail = rootView.findViewById(R.id.email);
        mPassword = rootView.findViewById(R.id.password);
        mKonfirmasi = rootView.findViewById(R.id.konfirmasi);
    }

    private void onClickRegister() throws JSONException {

        if(mNama.getText().toString().isEmpty()||
                mEmail.getText().toString().isEmpty()||
                mPassword.getText().toString().isEmpty()||
                mKonfirmasi.getText().toString().isEmpty()){
            progress.dismiss();
            Toast.makeText(getActivity(),"Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else if(!(mPassword.getText().toString().equals( mKonfirmasi.getText().toString()))) {
            progress.dismiss();
            Toast.makeText(getActivity(),"Password tidak cocok", Toast.LENGTH_SHORT).show();
        }else{
                Retrofit.Builder builder = new Retrofit
                        .Builder()
                        .baseUrl("https://tiemschedule.thekingcorp.org/user/")
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit=builder.build();
                ApiUser apiUser = retrofit.create(ApiUser.class);

            String passwordHash = new SimplePBKDF2().deriveKeyFormatted(mPassword.getText().toString());
            Call<UserDAO> UserDAOCall = apiUser.regUser(mNama.getText().toString(),mEmail.getText().toString(),passwordHash);
                UserDAOCall.enqueue(new Callback<UserDAO>() {
                    @Override
                    public void onResponse(Call<UserDAO> call, Response<UserDAO> response)  {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if(response.body().getValue().toString().equals("200")) {
                            progress.dismiss();
                            startIntent();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDAO> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    private void startIntent(){
        Intent intent = new Intent(getActivity(), SuccessActivity.class);
        startActivity(intent);
    }

}
