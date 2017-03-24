package com.zyght.trayectoseguro;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.zyght.trayectoseguro.R;
import com.zyght.trayectoseguro.entity.Answer;
import com.zyght.trayectoseguro.entity.Travel;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private Travel travel;

    private Polyline mMutablePolyline;

    Marker mCurrLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    private GoogleMap mGoogleMap;
    private LinearLayout filter_section;
    private Button takeRide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


            /*
    This is called before initializing the map because the map needs permissions(the cause of the crash)
    */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M ) {
            checkPermission();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //filter_section = (LinearLayout)findViewById(R.id.location_layout);
        //takeRide = (Button)findViewById(R.id.take_ride);
        // addListenerOnButton();

        travel = Travel.getInstance();

        Answer ans = new Answer();
        ans.setQuestionId(1);
        ans.setValue("true");


        Answer ans2 = new Answer();
        ans2.setQuestionId(2);
        ans2.setValue("false");

        travel.addAnswer(ans);
        travel.addAnswer(ans2);

        final AppCompatButton button = (AppCompatButton) findViewById(R.id.report_button);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                reportOnClick(v);
            }


        });

    }

    public void askPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ){
        }

    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            //start an intent to the class above. Do this because the app does not have enough permissions

        }
    }

    private static final int INITIAL_STROKE_WIDTH_PX = 5;




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(MapsActivity.this, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);

            }
        }

        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        //Show My Location Button on the map
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Add a marker in Sydney and move the camera
        //  LatLng sydney = new LatLng(-34, 151);
        // mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        buildGoogleApiClient();

        mGoogleApiClient.connect();



    }

    List<LatLng> points = new ArrayList<>();

    @Override
    public void onLocationChanged(Location location) {
        //remove previous current location marker and add new one at current position
        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocation = mGoogleMap.addMarker(markerOptions);




        if(points.size() > 0){
            LatLng lastPosition = points.get(points.size()-1);
            float[] results = new float[1];
            Location.distanceBetween(lastPosition.latitude, lastPosition.longitude, latLng.latitude, latLng.longitude, results);

            Toast.makeText(this,"Location Changed :"+results[0],Toast.LENGTH_SHORT).show();

            if(results[0] > 10.0){
                points.add(latLng);

                updatePolyline();
            }


        }else{
            points.add(latLng);

        }



        travel.addLocationLog(latLng);



    }

    private static final LatLng AKL = new LatLng(-37.006254, 174.783018);
    private static final LatLng JFK = new LatLng(40.641051, -73.777485);
    private static final LatLng LAX = new LatLng(33.936524, -118.377686);
    private static final LatLng LHR = new LatLng(51.471547, -0.460052);


    private void updatePolyline(){

        mMutablePolyline =   mGoogleMap.addPolyline(new PolylineOptions()
               // .add(LHR, AKL, LAX, JFK, LHR)
                .width(INITIAL_STROKE_WIDTH_PX)
                .color(Color.RED)
                .geodesic(true)
        );



        //points.add(AKL);
        //points.add(JFK);
        //points.add(LAX);

        mMutablePolyline.setPoints(points);
       // mMutablePolyline.notify();
    }


    private void reportOnClick(View v) {
        Gson gson = new Gson();

        String travelJson = gson.toJson(travel);

        Toast.makeText(this,"onConnectionFailed"+travelJson,Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, SummaryActivity.class);
        startActivity(i);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //  Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocation = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);

    }


    protected synchronized void buildGoogleApiClient() {
        // Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }









    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();

    }


}
