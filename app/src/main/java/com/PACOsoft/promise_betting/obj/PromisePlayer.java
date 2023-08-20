package com.PACOsoft.promise_betting.obj;

import java.io.Serializable;

public class PromisePlayer {
    private boolean arrival = false;//도착여부
    private int bettingMoney = 0;//배팅 금액
    private String nickName = "";//방에 있는 사람 닉네임
    private String playerID = "";//방에 있는 사람 아이디
    private int ranking = 0; //이 사람의 랭킹
    private double x = 0.0;//현재 이 사람이 위치한 x
    private double y = 0.0;//현재 이 사람이 위치한 y

    public PromisePlayer() {
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setBettingMoney(int bettingMoney) {
        this.bettingMoney = bettingMoney;
    }

    public int getBettingMoney() {
        return bettingMoney;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getX() {
        return x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getY() {
        return y;
    }

    public void setArrival(boolean arrival) {
        this.arrival = arrival;
    }

    public boolean getArrival() {
        return arrival;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getRanking() {
        return ranking;
    }
}
