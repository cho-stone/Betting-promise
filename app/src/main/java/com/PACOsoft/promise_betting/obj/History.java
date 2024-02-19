package com.PACOsoft.promise_betting.obj;

public class History {
    private String promiseName = "";//약속 이름
    private String promiseKey = "";//약속 고유 번호
    private int numOfPlayer = 0;//약속 인원수
    private int PrizeMoney = 0;//내가 받는 금액
    private String date = "";//약속 날짜+시간

    public History() {
    }

    public String getPromiseKey() {
        return promiseKey;
    }

    public void setPromiseKey(String promiseKey) {
        this.promiseKey = promiseKey;
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

    public int getPrizeMoney() {
        return PrizeMoney;
    }

    public void setPrizeMoney(int prizeMoney) {
        PrizeMoney = prizeMoney;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
