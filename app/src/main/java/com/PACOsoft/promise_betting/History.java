package com.PACOsoft.promise_betting;


public class History {
    private String promiseName;//약속 이름
    private int numOfPlayer;//참가 인원
    private int prize;//등수
    private int PrizeMoney;//총 배팅 금액
    private int numOfPrize;//이긴 사람 수
    private String date;//약속 날짜+시간

    public History() {
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
