package com.zyght.trayectoseguro.entity;

/**
 * Created by Arley Mauricio Duarte on 3/21/17.
 */

public class Answer {
    private int questionId;
    private String value;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
