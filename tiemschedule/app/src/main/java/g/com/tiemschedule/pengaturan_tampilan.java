package g.com.tiemschedule;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class pengaturan_tampilan extends AppCompatActivity {

    Button btn_tampilan;
    private String sNama;
    private String sId;
    private String sEmail;
    private String sPassword;
    SessionManagement session;
    public ProgressDialog progress;

    private EditText mNama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_tampilan);
        progress = new ProgressDialog(pengaturan_tampilan.this);
        Bundle extras = getIntent().getExtras();
        sId = extras.getString("id");
        sEmail = extras.getString("email");
        sNama = extras.getString("nama");
        sPassword = extras.getString("password");
        mNama = (EditText) findViewById(R.id.txtTampilanBaru);
        mNama.setText(sNama);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_tampilan = (Button)findViewById(R.id.btnUbahNama);

        btn_tampilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    progress.setMessage("Mengubah . . ");
                    progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progress.setIndeterminate(true);
                    progress.show();
                    onClickUpdateName();
                }catch(JSONException e) {
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

    private void onClickUpdateName() throws JSONException {
        if(mNama.getText().toString().isEmpty()){
            progress.dismiss();
            Toast.makeText(pengaturan_tampilan.this,"Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else{
            Retrofit.Builder builder = new Retrofit
                    .Builder()
                    .baseUrl("https://tiemschedule.thekingcorp.org/user/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            ApiUser apiUser = retrofit.create(ApiUser.class);


            Call<UserDAO> UserDAOCall = apiUser.updateUser(mNama.getText().toString(),this.sId);
            UserDAOCall.enqueue(new Callback<UserDAO>() {
                @Override
                public void onResponse(Call<UserDAO> call, Response<UserDAO> response)  {

                    Toast.makeText(pengaturan_tampilan.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    if(response.body().getValue().toString().equals("200")) {
                        startIntent();
                    }
                }

                @Override
                public void onFailure(Call<UserDAO> call, Throwable t) {
                    progress.dismiss();
                    Toast.makeText(pengaturan_tampilan.this, t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    private void startIntent(){
        session = new SessionManagement(pengaturan_tampilan.this);
        session.createLoginSession(sId,sEmail,mNama.getText().toString(),sPassword);
        super.onBackPressed();
    }
}
