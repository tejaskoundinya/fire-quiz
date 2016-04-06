package com.tejaskoundinya.android.firequiz.models;

import java.util.List;

/**
 * Created by Tejas Koundinya on 09-01-2016.
 */
public class QuestionModel {
    private long id;
    private String question;
    private List<OptionModel> options;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<OptionModel> getOptions() {
        return options;
    }

    public void setOptions(List<OptionModel> options) {
        this.options = options;
    }
}
