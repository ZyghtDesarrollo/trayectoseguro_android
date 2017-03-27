package com.zyght.trayectoseguro.entity;

import java.util.ArrayList;

/**
 * Created by Arley Mauricio Duarte on 3/27/17.
 */

public class QuestionBLL {

    private ArrayList<Question> questions = new ArrayList<>();

    private static final QuestionBLL ourInstance = new QuestionBLL();

    public static QuestionBLL getInstance() {
        return ourInstance;
    }

    private QuestionBLL() {
    }

    public void clear() {
        questions.clear();
    }

    public void add(Question question){
        questions.add(question);
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
