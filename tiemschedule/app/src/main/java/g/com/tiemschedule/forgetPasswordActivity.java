package g.com.tiemschedule;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.rtner.security.auth.spi.SimplePBKDF2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class forgetPasswordActivity extends AppCompatActivity {

    private EditText sEmailPengguna;
    private Button btnUbahPassword;
    public ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        sEmailPengguna = (EditText)findViewById(R.id.emailPengguna);
        btnUbahPassword = (Button)findViewById(R.id.ubahPassword);
        progress = new ProgressDialog(forgetPasswordActivity.this);
        btnUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("Mengirim . . ");
                progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progress.setIndeterminate(true);
                progress.show();
                if(sEmailPengguna.getText().toString().isEmpty()){
                    progress.dismiss();
                    Toast.makeText(forgetPasswordActivity.this,"Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    Retrofit.Builder builder = new Retrofit
                            .Builder()
                            .baseUrl("https://tiemschedule.thekingcorp.org/user/")
                            .addConverterFactory(GsonConverterFactory.create());
                    Retrofit retrofit=builder.build();
                    ApiUser apiUser = retrofit.create(ApiUser.class);

                    Call<UserDAO> UserDAOCall = apiUser.forgetPassword(sEmailPengguna.getText().toString());
                    UserDAOCall.enqueue(new Callback<UserDAO>() {
                        @Override
                        public void onResponse(Call<UserDAO> call, Response<UserDAO> response)  {
                            progress.dismiss();
                            Toast.makeText(forgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if(response.body().getValue().toString().equals("200")) {
                                forgetPasswordActivity.super.onBackPressed();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDAO> call, Throwable t) {
                            progress.dismiss();
                            Toast.makeText(forgetPasswordActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });
    }
}
