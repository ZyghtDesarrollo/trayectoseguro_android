package com.zyght.trayectoseguro;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.util.Log;

import com.zyght.trayectoseguro.driver_services.DriverTracker;



/**
 * Created by arley on 4/3/18.
 */

public class ArchLifecycleApp  extends Application implements LifecycleObserver {

    private static String TAG = "ArchLifecycleApp";

    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        //App in background

        DriverTracker.getInstance().addBackgroundCount();


    }
}
