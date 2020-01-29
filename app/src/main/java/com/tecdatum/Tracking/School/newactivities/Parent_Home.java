package com.tecdatum.Tracking.School.newactivities;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tecdatum.Tracking.School.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.tecdatum.Tracking.School.newactivities.SplashScreen.MY_PREFS_NAME;

public class Parent_Home extends AppCompatActivity {
    ActionBar toolbar;
    private static final float END_SCALE = 0.7f;
    Toolbar toolbar1;
    String UserName;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__parent);
        textView =findViewById(R.id.textView);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        SharedPreferences bb = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        UserName = bb.getString("UserName", "");
        textView.setText("Welcome  "+ UserName);
//        HomeActivity_Parent fr = new HomeActivity_Parent();
//        opennfrag(fr);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    //  toolbar.setTitle("Dashboard");
//                    HomeActivity_Parent fr = new HomeActivity_Parent();
//                    opennfrag(fr);
                    return true;
                case R.id.navigation_qv:





            }
            return false;
        }
    };
//    private void opennfrag(final HomeActivity_Parent fragment) {
//       FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.tabFrameLayout, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//    }
}
