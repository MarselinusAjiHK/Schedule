package g.com.tiemschedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import de.rtner.security.auth.spi.SimplePBKDF2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentMain extends Fragment {
    private static final String TAG = "FragmentMain";
    private Button btnSubmit;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mKlikDisini;
    SessionManagement session;
    public ProgressDialog progress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        session = new SessionManagement(getActivity());
        btnSubmit = (Button) rootView.findViewById(R.id.submit);
        mEmail = (EditText) rootView.findViewById(R.id.email);
        mPassword = (EditText) rootView.findViewById(R.id.password);
        mKlikDisini = (TextView) rootView.findViewById(R.id.klikDisini);
        progress = new ProgressDialog(getActivity());
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("Masuk . . ");
                progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progress.setIndeterminate(true);
                progress.show();
                login();
            }
        });
        mKlikDisini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),forgetPasswordActivity.class);
                startActivity(i);
            }
        });
        return rootView;

    }

    public void login(){
        if(mEmail.getText().toString().isEmpty()||
                mPassword.getText().toString().isEmpty()){
            progress.dismiss();
            Toast.makeText(getActivity(),"Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else{
            Retrofit.Builder builder = new Retrofit
                    .Builder()
                    .baseUrl("https://tiemschedule.thekingcorp.org/user/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            ApiUser apiUser = retrofit.create(ApiUser.class);


            Call<UserDAO> UserDAOCall = apiUser.loginUser(mEmail.getText().toString());
            UserDAOCall.enqueue(new Callback<UserDAO>() {
                @Override
                public void onResponse(Call<UserDAO> call, Response<UserDAO> response) {
                    if(response.body().getValue().equals("200")){
                        if(response.body().getStatus().equals("1")){
                            if(new SimplePBKDF2().verifyKeyFormatted(response.body().getPassword(), mPassword.getText().toString()))
                            {
                                progress.dismiss();
                                Intent intent = new Intent( getActivity(), dashboardActivity.class);
                                session.createLoginSession(response.body().getId(), mEmail.getText().toString(), response.body().getNama(), response.body().getPassword());
                                Toast.makeText(getActivity(),"Selamat Datang "+response.body().getNama(), Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"Password salah", Toast.LENGTH_SHORT).show();

                            }

                        }else{
                            progress.dismiss();
                            Toast.makeText(getActivity(),"Email Anda belum diverfikasi", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        progress.dismiss();
                    }

                }
                @Override
                public void onFailure(Call<UserDAO> call, Throwable t) {
                    progress.dismiss();
                    Toast.makeText(getActivity(),"Coba lagi", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
