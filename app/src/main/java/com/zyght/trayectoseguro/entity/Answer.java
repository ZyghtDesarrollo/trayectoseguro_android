package com.zyght.trayectoseguro.entity;

/**
 * Created by Arley Mauricio Duarte on 3/21/17.
 */

public class Answer {
    private int question_id;
    private String value;

    public int getQuestionId() {
        return question_id;
    }

    public void setQuestionId(int questionId) {
        this.question_id = questionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
