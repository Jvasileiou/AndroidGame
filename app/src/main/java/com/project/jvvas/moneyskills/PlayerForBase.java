package com.project.jvvas.moneyskills;

public class PlayerForBase
{
    public int playerNo;
    public String opponentID;
    public boolean gameReady;

    public PlayerForBase()
    {

    }


    public PlayerForBase(int playerNo, String opponentID, boolean gameReady)
    {
        this.playerNo = playerNo;
        this.gameReady = gameReady;
        this.opponentID = opponentID;
    }

}