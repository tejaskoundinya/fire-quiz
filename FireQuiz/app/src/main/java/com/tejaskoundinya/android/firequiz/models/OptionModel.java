package com.tejaskoundinya.android.firequiz.models;

/**
 * Created by Tejas Koundinya on 09-01-2016.
 */
public class OptionModel {
    private int optionId;
    private String optionString;
    private boolean correctAnswer;

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionString() {
        return optionString;
    }

    public void setOptionString(String optionString) {
        this.optionString = optionString;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
