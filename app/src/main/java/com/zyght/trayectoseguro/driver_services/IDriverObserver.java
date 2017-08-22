package com.zyght.trayectoseguro.driver_services;

/**
 * Created by Arley Mauricio Duarte on 8/2/17.
 */

public interface IDriverObserver {

     void updateSpeed(Double speed);
     void updateTripStatus();
}
