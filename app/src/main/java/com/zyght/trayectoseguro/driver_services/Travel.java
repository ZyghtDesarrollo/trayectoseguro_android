package com.zyght.trayectoseguro.driver_services;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.zyght.trayectoseguro.entity.Answer;
import com.zyght.trayectoseguro.entity.LocationLog;
import com.zyght.trayectoseguro.entity.Summary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Arley Mauricio Duarte on 3/21/17.
 */

public class Travel {

    private static  String TAG = "Travel";

    private double maxSpeed = 0;
    private Summary summary;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public static Travel getOurInstance() {
        return ourInstance;
    }

    public void setLocationLogs(ArrayList<LocationLog> locationLogs) {
        this.locationLogs = locationLogs;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    private static final Travel ourInstance = new Travel();

    public static Travel getInstance() {
        return ourInstance;
    }

    private Travel() {
    }

    private ArrayList<LocationLog> locationLogs = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    private ArrayList<Answer> answers = new ArrayList<>();


    public ArrayList<LocationLog> getLocationLogs() {
        return locationLogs;
    }


    public void addAnswer(Answer answer) {
        answers.add(answer);
    }



    protected void addLocationLog(Location location, double speed) {

        double latitude = location.getLatitude();

        // Get longitude of the current location
        double longitude = location.getLongitude();


        LocationLog log = new LocationLog();
        log.setLatitude(latitude);
        log.setLongitude(longitude);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Date today = Calendar.getInstance().getTime();

        String reportDate = df.format(today);

        log.setTimeStamp(System.currentTimeMillis());
        log.setDate(reportDate);

        log.setSpeed(speed);

        Log.i(TAG, "addLocationLog: Speed "+speed+", Latitude {"+latitude+"} Longitude {"+longitude+"}");

        locationLogs.add(log);
    }







    public void clear() {
        locationLogs.clear();
        locations.clear();
        answers.clear();

    }



}
