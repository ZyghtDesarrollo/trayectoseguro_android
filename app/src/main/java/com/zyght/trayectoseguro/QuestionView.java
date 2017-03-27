package com.zyght.trayectoseguro;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zyght.trayectoseguro.entity.Question;

/**
 * Created by Arley Mauricio Duarte on 3/27/17.
 */

public class QuestionView extends FrameLayout {

    private Context context;
    private Question question;
    private ToggleButton toggleButton;

    public QuestionView(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public QuestionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();

    }

    public QuestionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();

    }

    public QuestionView(Context context, Question question) {
        super(context);
        this.question = question;
        this.context = context;

        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.question, null);
        TextView questionText = (TextView) view.findViewById(R.id.textView);
        questionText.setText(question.getTitle());
        toggleButton = (ToggleButton) view.findViewById(R.id.toggleText);


        addView(view);
    }


    public Question getQuestion() {
        return question;
    }

    public String getResponse() {

        String toggleButtonS = toggleButton.getText().toString();

        if(toggleButtonS.equals("Si")){
            return "true";
        }else{
            return "false";
        }


    }
}
