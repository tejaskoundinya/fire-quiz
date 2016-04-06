package com.tejaskoundinya.android.firequiz.models;

/**
 * Created by Tejas Koundinya on 09-01-2016.
 */
public class PlayerModel {
    private String name;
    private int score;
    private OptionModel playerOption;
    private boolean didPlayerAnswer;
    private boolean playerAnswerCorrect;

    public boolean isDidPlayerAnswer() {
        return didPlayerAnswer;
    }

    public void setDidPlayerAnswer(boolean didPlayerAnswer) {
        this.didPlayerAnswer = didPlayerAnswer;
    }

    public boolean isPlayerAnswerCorrect() {
        return playerAnswerCorrect;
    }

    public void setPlayerAnswerCorrect(boolean playerAnswerCorrect) {
        this.playerAnswerCorrect = playerAnswerCorrect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public OptionModel getPlayerOption() {
        return playerOption;
    }

    public void setPlayerOption(OptionModel playerOption) {
        this.playerOption = playerOption;
    }
}
