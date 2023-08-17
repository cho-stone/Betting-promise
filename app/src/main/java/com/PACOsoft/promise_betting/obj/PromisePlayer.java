package com.PACOsoft.promise_betting.obj;

import java.io.Serializable;

public class PromisePlayer implements Serializable {
    private String playerID;//방에 있는 사람 아이디
    private String nickName;//방에 있는 사람 닉네임
    private int bettingMoney;//배팅 금액
    private double x;//현재 이 사람이 위치한 x
    private double y;//현재 이 사람이 위치한 y
    private boolean arrival;//도착여부
    private int ranking; //이 사람의 랭킹

    public PromisePlayer(String playerID,String nickName,int bettingMoney,double x, double y,boolean arrival,int ranking) {
        this.playerID = playerID;
        this.nickName = nickName;
        this.bettingMoney = bettingMoney;
        this.x = x;
        this.y = y;
        this.arrival = arrival;
        this.ranking = ranking;

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
