package com.zyght.trayectoseguro;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.zyght.trayectoseguro.config.ResourcesConstants;
import com.zyght.trayectoseguro.driver_services.Travel;
import com.zyght.trayectoseguro.handler.AddTravelAPIHandler;
import com.zyght.trayectoseguro.network.ResponseActionDelegate;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Arley Mauricio Duarte on 3/21/17.
 */

public class STListFragment extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResponseActionDelegate {
    MapView mMapView;
    private GoogleMap googleMap;
    private Context context;
    LatLng latLng;
    private Travel travel;

    private Polyline mMutablePolyline;
    List<LatLng> points = new ArrayList<>();
    Marker mCurrLocation;
    LocationRequest mLocationRequest;


    GoogleApiClient mGoogleApiClient;


    private static final int INITIAL_STROKE_WIDTH_PX = 15;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps_f, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        context = getActivity().getBaseContext();




        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    setUpMap();
                } else {
                    Toast.makeText(getActivity(), "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);


                    }
                }

                /*
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/

                buildGoogleApiClient();

                mGoogleApiClient.connect();
            }
        });


        final AppCompatButton button = (AppCompatButton) rootView.findViewById(R.id.report_button);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ResourcesConstants.startTravel = false;

                if(button.getText().equals(getString(R.string.start))){

                    travel = Travel.getInstance();
                    travel.clear();

                    Intent i = new Intent(getActivity(), SurveyActivity.class);
                    startActivity(i);
                    button.setText(getString(R.string.finish));

                }else{

                    //Intent i = new Intent(getActivity(), SummaryActivity.class);
                    //startActivity(i);
                    reportOnClick();
                    button.setText(getString(R.string.start));
                }


            }


        });

        return rootView;
    }

    private void reportOnClick() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
// Add the buttons

        builder.setTitle("Guardar");
        builder.setMessage("¿Desea guardar y enviar la información de su trayecto?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                send();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();






    }

    private void send(){
        Gson gson = new Gson();

        String answers = gson.toJson(travel.getAnswers());
        String logs = gson.toJson(travel.getLocationLogs());

        Toast.makeText(context,logs,Toast.LENGTH_SHORT).show();

        AddTravelAPIHandler resourceHandler = new AddTravelAPIHandler(answers, logs);
        resourceHandler.setRequestHandle(this, getActivity());
    }





    private void setUpMap() {
        // Enable MyLocation Layer of Google Map


        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        /* Get Current Location */


        Location myLocation = locationManager.getLastKnownLocation(provider);


        if(myLocation != null){
            double latitude = myLocation.getLatitude();

            // Get longitude of the current location
            double longitude = myLocation.getLongitude();

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(1));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!"));
        }

        // Get latitude of the current location

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

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
        mCurrLocation = googleMap.addMarker(markerOptions);

        if(ResourcesConstants.startTravel){
            if(points.size() > 0){
                LatLng lastPosition = points.get(points.size()-1);
                float[] results = new float[1];
                Location.distanceBetween(lastPosition.latitude, lastPosition.longitude, latLng.latitude, latLng.longitude, results);

                Toast.makeText(context,"Location Changed :"+results[0],Toast.LENGTH_SHORT).show();

                if(results[0] > 1.0){
                    points.add(latLng);
                    //TODO:
                    points.add(latLng);
                    updatePolyline();

                    //travel.addLocation(location);

                }


            }else{
                points.add(latLng);

            }

        }



    }

    private void updatePolyline(){

        mMutablePolyline =   googleMap.addPolyline(new PolylineOptions()
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //  Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            googleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocation = googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
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
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }









    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(context,"onConnectionSuspended",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context,"onConnectionFailed",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void didSuccessfully(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void didNotSuccessfully(String message) {
        Toast.makeText(context,"onConnectionFailed:"+message,Toast.LENGTH_SHORT).show();
    }
}
