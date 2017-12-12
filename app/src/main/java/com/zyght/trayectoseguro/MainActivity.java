package com.zyght.trayectoseguro;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;


import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.zyght.trayectoseguro.entity.TravelItem;

public class MainActivity extends AppCompatActivity implements TravelFragment.OnListFragmentInteractionListener {

   // private TextView mTextMessage;

    private SupportMapFragment mSupportMapFragment;

    private void initialiseMap() {



        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        MapViewFragment fra = new MapViewFragment();

        fragmentTransaction.replace(R.id.content, fra);
        fragmentTransaction.commit();


        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initialiseList() {



        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        TravelFragment fra =   TravelFragment.newInstance(1);

        fragmentTransaction.replace(R.id.content, fra);
        fragmentTransaction.commit();




    }


    private void initializeProfile() {



        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        ProfileFragment fra =   new ProfileFragment();

        fragmentTransaction.replace(R.id.content, fra);
        fragmentTransaction.commit();




    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    initializeProfile();
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);

                    initialiseMap();

                    return true;
                case R.id.navigation_notifications:
                    initialiseList();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




       // mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initializeProfile();


    }

    @Override
    public void onListFragmentInteraction(TravelItem item) {

    }
}
