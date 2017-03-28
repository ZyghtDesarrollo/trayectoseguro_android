package com.zyght.trayectoseguro.entity;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Arley Mauricio Duarte on 3/21/17.
 */

public class Travel {


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

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    private ArrayList<Answer> answers = new ArrayList<>();




    public ArrayList<LocationLog> getLocationLogs() {
        return locationLogs;
    }


    public void addAnswer(Answer answer){
        answers.add(answer);
    }

    public void addLocationLog(LatLng latLng) {
        LocationLog log = new LocationLog();
        log.setLatitude(latLng.latitude);
        log.setLongitude(latLng.longitude);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Date today = Calendar.getInstance().getTime();

        String reportDate = df.format(today);


        log.setDate(reportDate);

        locationLogs.add(log);
    }

    public void clear() {
        locationLogs.clear();
        answers.clear();
    }
}
