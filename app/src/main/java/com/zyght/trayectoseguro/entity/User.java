package com.zyght.trayectoseguro.entity;

/**
 * Created by Arley Mauricio Duarte on 3/27/17.
 */

public class User {
    private String id;
    private String username;
    private int speed_limit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSpeedLimit() {
        return speed_limit;
    }
}
