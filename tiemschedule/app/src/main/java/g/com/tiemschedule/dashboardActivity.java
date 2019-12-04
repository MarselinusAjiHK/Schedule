package g.com.tiemschedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class dashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SessionManagement session;
    BerandaManagement berandaManagement;
    CatatanManagement catatanManagement;
    JadwalManagement jadwalManagement;
    PrioritasManagement prioritasManagement;

    public static boolean doubleBackToExitPressedOnce = false;
    public static boolean doubleBackToLogOut = false;
    private String TitleCustom = "Hari Ini";
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    public String emailUser;
    public String idUser;
    public String namaUser;
    public String passwordUser;
    public ImageView mProfilePengguna;
    public TextView mNamaPengguna;
    public TextView mEmailPengguna;

    public TextView mTextFab1;
    public TextView mTextFab2;

    public Bitmap bitmapImage;
    android.app.FragmentManager fragmentManager = getFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManagement(getApplicationContext());
        catatanManagement = new CatatanManagement(getApplicationContext());
        jadwalManagement = new JadwalManagement(getApplicationContext());
        prioritasManagement = new PrioritasManagement(getApplicationContext());
        berandaManagement = new BerandaManagement(getApplicationContext());
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();


        // name
        idUser = user.get(SessionManagement.KEY_NAME);
        emailUser = user.get(SessionManagement.KEY_EMAIL);
        namaUser = user.get(SessionManagement.KEY_ID);
        passwordUser = user.get(SessionManagement.KEY_PASSWORD);


        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        mTextFab1 = (TextView) findViewById(R.id.textfab1);
        mTextFab2 = (TextView) findViewById(R.id.textfab2);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChange("1");
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChange("2");
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChange("3");
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        mNamaPengguna = (TextView)headerView.findViewById(R.id.textNamaPengguna);
        mEmailPengguna = (TextView)headerView.findViewById(R.id.textEmailPengguna);

        mProfilePengguna = (ImageView)headerView.findViewById(R.id.imageView);

       setNamaPenggunaNav(namaUser);
        mEmailPengguna.setText(emailUser);
        setProfilePengguna();
        fragmentManager.beginTransaction().replace(R.id.framelay, new FragmentBeranda()).commit();
    }
    public void setNamaPenggunaNav(String nama){
        mNamaPengguna.setText(nama);
    }
    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public void setProfilePengguna(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        storageRef.child("images/"+getIdUser()+".jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmapImage = Bitmap.createScaledBitmap(bmp, 400,400, false);
                setImageProfileNavbar(bitmapImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                bitmapImage = null;
            }
        });
    }

    public void setImageProfileNavbar(Bitmap bitmap){
        mProfilePengguna.setImageBitmap(bitmap);
        this.setBitmapImage(bitmap);
    }
    public String getTitleCustom() {
        return TitleCustom;
    }

    public void setTitleCustom(String titleCustom) {
        getSupportActionBar().setTitle(titleCustom);
    }

    public boolean isDoubleBackToExitPressedOnce() {
        return doubleBackToExitPressedOnce;
    }

    public void setDoubleBackToExitPressedOnce(boolean doubleBackToExitPressedOnce) {
        this.doubleBackToExitPressedOnce = doubleBackToExitPressedOnce;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void onClickChange(String id) {

        switch (id){
            case "1":
                if (mTextFab1.getVisibility() == View.VISIBLE) {
                    mTextFab1.setVisibility(View.INVISIBLE);
                    mTextFab2.setVisibility(View.INVISIBLE);
                }else {
                    mTextFab1.setVisibility(View.VISIBLE);
                    mTextFab2.setVisibility(View.VISIBLE);
                }
                animateFAB();
                break;
            case "2":

                fragmentManager.beginTransaction().replace(R.id.framelay,  new FragmentTambahCatatan()).commit();
                animateFAB();
                break;
            case "3":
                fragmentManager.beginTransaction().replace(R.id.framelay,  new FragmentTambahJadwal()).commit();
                animateFAB();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Tekan Back lagi untuk menutup aplikasi", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }


    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_beranda) {
            fragmentManager.beginTransaction().replace(R.id.framelay,new FragmentBeranda()).commit();
        } else if (id == R.id.nav_jadwal) {
            fragmentManager.beginTransaction().replace(R.id.framelay,new FragmentJadwal()).commit();
        } else if (id == R.id.nav_prioritas) {
            fragmentManager.beginTransaction().replace(R.id.framelay,new FragmentPrioritas()).commit();

        } else if (id == R.id.nav_kalender) {
            fragmentManager.beginTransaction().replace(R.id.framelay,new FragmentKalender()).commit();
        } else if (id == R.id.nav_catatan) {
            fragmentManager.beginTransaction().replace(R.id.framelay,new FragmentCatatan()).commit();
        }else if (id == R.id.nav_pengaturan) {
            fragmentManager.beginTransaction().replace(R.id.framelay,new FragmentPengaturan()).commit();
        }else if (id == R.id.nav_keluar) {
            AlertDialog diaBox = AskOption();
            diaBox.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(dashboardActivity.this)
                //set message, title, and icon
                .setTitle("Keluar")
                .setMessage("Apakah anda ingin keluar dari akun ini ?")

                .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        catatanManagement.logoutUser();
                        jadwalManagement.logoutUser();
                        prioritasManagement.logoutUser();
                        berandaManagement.logoutUser();
                        session.logoutUser();
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }
}
