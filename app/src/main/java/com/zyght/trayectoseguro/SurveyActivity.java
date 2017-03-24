package com.zyght.trayectoseguro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.zyght.trayectoseguro.config.ResourcesConstants;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        final AppCompatButton button = (AppCompatButton) findViewById(R.id.report_button);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                reportOnClick(v);
            }


        });

    }

    private void reportOnClick(View v) {
        ResourcesConstants.startTravel = true;

        finish();

    }
}
