package g.com.tiemschedule;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    SessionManagement session;

    private static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private String idUser;
    private String emailUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: Starting");
        session = new SessionManagement(getApplicationContext());


        if(session.isLoggedIn()){
            Intent intent = new Intent( MainActivity.this, dashboardActivity.class);
            startActivity(intent);
        }

        mSectionPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentDaftar(), "Daftar");
        adapter.addFragment(new FragmentMain(), "Masuk");
        viewPager.setAdapter(adapter);
    }
}
