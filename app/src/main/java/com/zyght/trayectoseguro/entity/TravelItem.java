package com.zyght.trayectoseguro.entity;

/**
 * Created by Arley Mauricio Duarte on 3/27/17.
 */

public class TravelItem {
    private String id;
    private String date;
    private String appuser;
    private String max_speed;
    private String average_speed;
    private String distance;
    private String duration;
    private String speed_violation;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppuser() {
        return appuser;
    }

    public void setAppuser(String appuser) {
        this.appuser = appuser;
    }

    public String getMaxSpeed() {
        return max_speed;
    }

    public String getAverageSpeed() {
        return average_speed;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getSpeedViolation() {
        return speed_violation;
    }
}
