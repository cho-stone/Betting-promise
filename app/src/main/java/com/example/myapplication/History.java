package com.example.myapplication;


public class History {
    private String promiseName;
    private int numOfPlayer;
    private int prize;
    private int PrizeMoney;

    public History(){}
    public String getPromiseName() {
        return promiseName;
    }

    public void setPromiseName(String promiseName) {
        this.promiseName = promiseName;
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public void setNumOfPlayer(int numOfPlayer) {
        this.numOfPlayer = numOfPlayer;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public int getPrizeMoney() {
        return PrizeMoney;
    }

    public void setPrizeMoney(int prizeMoney) {
        PrizeMoney = prizeMoney;
    }

    public int getNumOfPrize() {
        return numOfPrize;
    }

    public void setNumOfPrize(int numOfPrize) {
        this.numOfPrize = numOfPrize;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private int numOfPrize;
    private String data;


}
