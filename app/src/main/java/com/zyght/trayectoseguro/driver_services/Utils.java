package com.zyght.trayectoseguro.driver_services;

import android.util.Log;

/**
 * Created by Arley Mauricio Duarte on 8/7/17.
 */

public class Utils {

    private static String TAG = "Utils";

    public static String getHumanTimeFormatFromMilliseconds(long milliseconds){
        String message = "";

        Log.d(TAG, "getHumanTimeFormatFromMilliseconds:"+milliseconds);

        String hoursText = "horas";
        String minutesText = "minutos";
        String secondsText = "segundos";

        if (milliseconds >= 1000){
            int seconds = (int) (milliseconds / 1000) % 60;
            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
            int days = (int) (milliseconds / (1000 * 60 * 60 * 24));

            if(hours == 1){
                hoursText = "hora";
            }


            if(minutes == 1){
                minutesText = "minuto";
            }

            if(seconds == 1){
                secondsText = "segundo";
            }

            if((days == 0) && (hours != 0)){

                message = String.format("%d %s %d %s %d %s ", hours, hoursText, minutes,minutesText,  seconds, secondsText);

            }else if((hours == 0) && (minutes != 0)){
                message = String.format("%d %s %d %s ", minutes,minutesText,  seconds,secondsText);
            }else if((days == 0) && (hours == 0) && (minutes == 0)){
                message = String.format("%d %s ", seconds, secondsText);
            }else{


                message = String.format("%d día %d %s %d %s %d %s", days, hours, hoursText, minutes, minutesText, seconds, secondsText);
            }
        } else{
            message = "menos de un segundo.";
        }
        return message;
    }
}
