package com.tejaskoundinya.android.firequiz.models;

/**
 * Created by Tejas Koundinya on 09-01-2016.
 */
public class GameWaitModel {
    private long id;
    private String gameMaster;
    private String gameSlave;
    private String gameName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGameMaster() {
        return gameMaster;
    }

    public void setGameMaster(String gameMaster) {
        this.gameMaster = gameMaster;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameSlave() {
        return gameSlave;
    }

    public void setGameSlave(String gameSlave) {
        this.gameSlave = gameSlave;
    }
}
