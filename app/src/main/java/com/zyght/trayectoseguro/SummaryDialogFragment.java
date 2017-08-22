package com.zyght.trayectoseguro;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.zyght.trayectoseguro.driver_services.DriverTracker;

/**
 * Created by Arley Mauricio Duarte on 3/28/17.
 */

public class SummaryDialogFragment extends DialogFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment

        View view = inflater.inflate(R.layout.summay_dialog, container, false);
        TextView velocity = (TextView) view.findViewById(R.id.textView22);
        TextView distance = (TextView) view.findViewById(R.id.textView12);
        TextView duration = (TextView) view.findViewById(R.id.textView2);

        final DriverTracker driverTracker =  DriverTracker.getInstance();

        velocity.setText(String.valueOf(driverTracker.AverageSpeed()));
        distance.setText(String.valueOf(driverTracker.getDistanceKM()));
        duration.setText(String.valueOf(driverTracker.getTripTime()));


        TextView maxSpeed = (TextView) view.findViewById(R.id.textViewMaxSpeed);
        maxSpeed.setText(String.valueOf(driverTracker.getMaxSpeed()));


        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                dismiss();
                driverTracker.clear();

                return true;
            }
        });

        return view;




        }




    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


}
