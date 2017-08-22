package com.zyght.trayectoseguro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.LinearLayout;

import com.zyght.trayectoseguro.config.ResourcesConstants;
import com.zyght.trayectoseguro.driver_services.DriverTracker;
import com.zyght.trayectoseguro.entity.Answer;
import com.zyght.trayectoseguro.entity.Question;
import com.zyght.trayectoseguro.entity.QuestionBLL;
import com.zyght.trayectoseguro.entity.Travel;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class SurveyActivity extends AppCompatActivity {

    private ArrayList<QuestionView> questionViews =new ArrayList<>();

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

        fillQuestions();

    }

    private void fillQuestions(){
        QuestionBLL questionBLL = QuestionBLL.getInstance();

        LinearLayout customListLayout = (LinearLayout) this.findViewById(R.id.linear_layout);


        for(Question question : questionBLL.getQuestions()){
            QuestionView customListView = new QuestionView(this.getApplicationContext(), question);

            questionViews.add(customListView);
            customListLayout.addView(customListView);
        }
    }

    private void reportOnClick(View v) {
        ResourcesConstants.startTravel = true;

        Travel travel = Travel.getInstance();



        for(QuestionView questionView : questionViews){
            Question question = questionView.getQuestion();

            Answer answer = new Answer();
            answer.setQuestionId(question.getId());
            answer.setValue(questionView.getResponse());

            travel.addAnswer(answer);


        }

        DriverTracker.getInstance().setTripStatus(DriverTracker.ON_TRIP);
        finish();

    }
}
