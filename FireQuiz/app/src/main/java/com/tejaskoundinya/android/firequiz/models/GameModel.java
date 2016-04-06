package com.tejaskoundinya.android.firequiz.models;

/**
 * Created by Tejas Koundinya on 09-01-2016.
 */
public class GameModel {
    private long id;
    private PlayerModel gameMaster;
    private PlayerModel gameSlave;
    private boolean generateNewQuestion;
    private QuestionModel question;
    private boolean didGameEnd;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PlayerModel getGameMaster() {
        return gameMaster;
    }

    public void setGameMaster(PlayerModel gameMaster) {
        this.gameMaster = gameMaster;
    }

    public PlayerModel getGameSlave() {
        return gameSlave;
    }

    public void setGameSlave(PlayerModel gameSlave) {
        this.gameSlave = gameSlave;
    }

    public boolean isGenerateNewQuestion() {
        return generateNewQuestion;
    }

    public void setGenerateNewQuestion(boolean generateNewQuestion) {
        this.generateNewQuestion = generateNewQuestion;
    }

    public QuestionModel getQuestion() {
        return question;
    }

    public void setQuestion(QuestionModel question) {
        this.question = question;
    }

    public boolean isDidGameEnd() {
        return didGameEnd;
    }

    public void setDidGameEnd(boolean didGameEnd) {
        this.didGameEnd = didGameEnd;
    }
}
