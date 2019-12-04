package g.com.tiemschedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import de.rtner.security.auth.spi.SimplePBKDF2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class pengaturan_sandi extends AppCompatActivity {

    private String sId;
    private String sEmail;
    private String sPassword;

    private EditText passwordL;
    private EditText password;
    private EditText passwordKB;

    private Button mUpdatePassword;
    public ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_sandi);
        Bundle extras = getIntent().getExtras();
        sId = extras.getString("id");
        sEmail = extras.getString("email");
        sPassword = extras.getString("password");
        progress = new ProgressDialog(pengaturan_sandi.this);
        passwordL = (EditText) findViewById(R.id.txtPassLama);
        password = (EditText) findViewById(R.id.txtPassBaru);
        passwordKB = (EditText) findViewById(R.id.txtPassBaruConfirm);

        mUpdatePassword = (Button) findViewById(R.id.btnUbahPassConfirm);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progress.setMessage("Mengubah . . ");
                    progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progress.setIndeterminate(true);
                    progress.show();
                    onClickUpdatePassword();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onBackPressed();
        return true;

    }

    private void onClickUpdatePassword() throws JSONException {
        if(passwordL.getText().toString().isEmpty()||
                password.getText().toString().isEmpty()||
                passwordKB.getText().toString().isEmpty()){

            progress.dismiss();
            Toast.makeText(pengaturan_sandi.this,"Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else if(!(password.getText().toString().equals( passwordKB.getText().toString()))) {

            progress.dismiss();
            Toast.makeText(pengaturan_sandi.this,"Password tidak cocok", Toast.LENGTH_SHORT).show();
        }else if(new SimplePBKDF2().verifyKeyFormatted(this.sPassword, passwordL.getText().toString())){

            Retrofit.Builder builder = new Retrofit
                    .Builder()
                    .baseUrl("https://tiemschedule.thekingcorp.org/user/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            ApiUser apiUser = retrofit.create(ApiUser.class);
            String passwordHash = new SimplePBKDF2().deriveKeyFormatted(password.getText().toString());
            Call<UserDAO> UserDAOCall = apiUser.updatePasswordUser(passwordHash,this.sId);
            UserDAOCall.enqueue(new Callback<UserDAO>() {
                @Override
                public void onResponse(Call<UserDAO> call, Response<UserDAO> response)  {

                    progress.dismiss();
                    Toast.makeText(pengaturan_sandi.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if(response.body().getValue().toString().equals("200")) {
                        startIntent();
                    }
                }

                @Override
                public void onFailure(Call<UserDAO> call, Throwable t) {

                    progress.dismiss();
                    Toast.makeText(pengaturan_sandi.this, t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else{

            progress.dismiss();
            Toast.makeText(pengaturan_sandi.this,"Password salah silahkan coba lagi ", Toast.LENGTH_SHORT).show();

        }
    }
    private void startIntent(){

        super.onBackPressed();
    }
}
