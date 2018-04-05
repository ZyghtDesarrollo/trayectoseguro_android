package com.zyght.trayectoseguro.driver_services;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zyght.trayectoseguro.entity.PhoneUsageLog;
import com.zyght.trayectoseguro.entity.TravelItem;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Arley Mauricio Duarte on 7/28/17.
 */

public class DriverTracker {

    private static double SPEED_LIMIT = 25;

    private static final int STROKE_WIDTH_ROUTE_LINE_PX = 15;



    public static final int ON_TRIP = 3;

    public static final int CLOSED_TRIP = 5;
    private static final String TAG = "DriverTracker";

    private Date startDate = new Date();
    private Date endDate = new Date();

    private Context context;


    private ArrayList<Double> speedRecords = new ArrayList<>();
    private ArrayList<Double> distanceRecords = new ArrayList<>();
    private static final DriverTracker ourInstance = new DriverTracker();
    private int tripStatus = 0;
    private final Object MUTEX = new Object();
    private ArrayList<IDriverObserver> driverObservers = new ArrayList<>();
    private double tripTimeSeconds = 0;

    private double maxSpeed = 0;

    private ArrayList<Location> locations = new ArrayList<>();
    private GoogleMap googleMap;

    public static DriverTracker getInstance() {
        return ourInstance;
    }

    private DriverTracker() {
    }


    public void setSpeedLimit(int speedLimit) {

        this.SPEED_LIMIT = speedLimit;

        Log.i(TAG, "setSpeedLimit: " + SPEED_LIMIT);
    }


    public void register(IDriverObserver observer) {
        synchronized (MUTEX) {
            if (!driverObservers.contains(observer)) driverObservers.add(observer);
        }
    }

    public void unregister(IDriverObserver obj) {
        synchronized (MUTEX) {
            driverObservers.remove(obj);
        }
    }




    public void notifySpeeding() {


        Log.i(TAG, "notifySpeeding:" + tripStatus);

        for (IDriverObserver obj : driverObservers) {
            obj.notifySpeeding();
        }

    }

    public void stopAlarm() {


        Log.i(TAG, "notifySpeeding:" + tripStatus);

        for (IDriverObserver obj : driverObservers) {
            obj.stopAlarm();
        }

    }



    public int getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(int tripStatus) {
        if (tripStatus == ON_TRIP) {
            startDate = new Date(System.currentTimeMillis());
        }
        if (tripStatus == CLOSED_TRIP) {
            endDate = new Date(System.currentTimeMillis());
        }
        this.tripStatus = tripStatus;
    }


    private boolean isOverSpeedLimit(double speed_kph) {
        boolean over = false;

        if (speed_kph > SPEED_LIMIT) {
            addSpeedingCount();
            notifySpeeding();
            over = true;

        } else {
            stopAlarm();
        }

        return over;
    }

    public void setContext(Context context) {
        this.context = context;
        // mp = MediaPlayer.create(context, R.raw.speed_alarm);
    }


    private double currentLatitude =  0.0;
    private double currentLongitude =  0.0;



    protected void addLocation(Location location) {
        if (location != null && tripStatus == ON_TRIP) {
            locations.add(location);
            updateRoute(location);
            updateSpeed();

            double speed =getSpeed();

            isOverSpeedLimit(speed);


            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            Travel.getInstance().addLocationLog(location,speed);

        }
    }

    public void updateSpeed() {


        for (IDriverObserver obj : driverObservers) {
            obj.updateSpeed(getSpeed());
        }

    }

    private Polyline mMutablePolyline;
    private List<LatLng> points = new ArrayList<>();

    public void drawRoute(GoogleMap googleMap) {
        this.googleMap = googleMap;


    }

    public void refreshRouteScreen(GoogleMap googleMap) {
        if (googleMap != null) {


            mMutablePolyline = googleMap.addPolyline(new PolylineOptions()
                    // .add(LHR, AKL, LAX, JFK, LHR)
                    .width(STROKE_WIDTH_ROUTE_LINE_PX)
                    .color(Color.BLUE)
                    .geodesic(true)
            );
            mMutablePolyline.setPoints(points);

        }
    }

    List<Polyline> polylines = new ArrayList<Polyline>();


    private void updateRoute(Location location) {


        if (googleMap != null) {
            if (mMutablePolyline != null) {
                mMutablePolyline.remove();
            }
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            points.add(latLng);


            mMutablePolyline = googleMap.addPolyline(new PolylineOptions()
                    .width(STROKE_WIDTH_ROUTE_LINE_PX)
                    .color(Color.BLUE)
                    .geodesic(true)
            );
            mMutablePolyline.setPoints(points);
            polylines.add(mMutablePolyline);


        }
    }


    public double getSpeed() {
        double speed_kph = 0d;

        if (locations.size() > 1) {
            Location l2 = locations.get(locations.size() - 1);
            Location l1 = locations.get(locations.size() - 2);

            double dist = distance(l1.getLatitude(), l2.getLatitude(), l1.getLongitude(), l2.getLongitude(), 0d, 0d);
            double time_s = (l2.getTime() - l1.getTime()) / 1000.0;
            double speed_mps = dist / time_s;
            speed_kph = (speed_mps * 3600.0) / 1000.0;

            tripTimeSeconds += time_s * 1000;

            speedRecords.add(speed_kph);
            distanceRecords.add(dist);


        }

        if (maxSpeed < speed_kph && speed_kph < 120.0) {
            maxSpeed = speed_kph;
            Log.i(TAG, "Max Speed:" + maxSpeed);

        }
        return speed_kph;
    }


    public void clear() {

        for (Polyline line : polylines) {
            line.remove();
        }

        polylines.clear();

        locations.clear();
        speedRecords.clear();
        distanceRecords.clear();
        tripTimeSeconds = 0d;
        points.clear();
        if (mMutablePolyline != null) {
            mMutablePolyline.remove();
        }
        speedingCount = 0;

        refreshRouteScreen(googleMap);


        maxSpeed = 0;


        for (IDriverObserver obj : driverObservers) {
            obj.updateSpeed(0.d);
        }

        backgroundCount = 0;
        backLogs.clear();
        currentTravelItem = null;

    }

    public int AverageSpeed() {
        Double averageSpeed = new Double(0d);


        for (Double speed : speedRecords) {
            averageSpeed += speed;

        }

        if (speedRecords.size() > 0) {
            averageSpeed = averageSpeed / speedRecords.size();
        }

        return (int) (double) averageSpeed;
    }


    public String getDistanceKM() {
        Double distanceKM = new Double(0d);
        Double distanceMeters = new Double(0d);


        for (Double dist : distanceRecords) {
            distanceMeters += dist;

        }

        distanceKM = distanceMeters / 1000;

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distanceKM);


    }


    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public double distance(double lat1, double lat2, double lon1,
                           double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public String getTripTime() {


        //  Double timeHours  = tripTimeSeconds/3600;

        //DecimalFormat df = new DecimalFormat("0.00");
        return Utils.getHumanTimeFormatFromMilliseconds((long) tripTimeSeconds);
    }

    public String getStartDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        return dateFormatter.format(startDate);
    }

    public String getEndDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        return dateFormatter.format(endDate);
    }






    public String getMaxSpeed() {
        // return String.valueOf(maxSpeed);

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(maxSpeed);
    }


    private int speedingCount = 0;

    private void addSpeedingCount() {
        speedingCount++;
        Log.i(TAG, "addSpeedingCount"+speedingCount);

    }

    public String getSpeeding() {
        return String.valueOf(speedingCount);
    }


    private ArrayList<PhoneUsageLog>  backLogs = new ArrayList<>();

    private int backgroundCount = 0;

    public void addBackgroundCount() {

        if (tripStatus == ON_TRIP) {


            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date today = Calendar.getInstance().getTime();

            String reportDate = df.format(today);

            backgroundCount++;

            Log.i(TAG, "addBackgroundCount:"+ backgroundCount);

            PhoneUsageLog log = new PhoneUsageLog();
            log.setDate(reportDate);
            log.setLatitude(currentLatitude);
            log.setLongitude(currentLongitude);

            backLogs.add(log);
        }


    }


    public ArrayList<PhoneUsageLog> getPhoneUsageLogs(){
        return backLogs;
    }

    TravelItem currentTravelItem;
    public void setCurrentTravelItem(TravelItem currentTravelItem) {
        this.currentTravelItem = currentTravelItem;

    }

    public TravelItem getCurrentTravelItem() {
        return currentTravelItem;
    }
}
