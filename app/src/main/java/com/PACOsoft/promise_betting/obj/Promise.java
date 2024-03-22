package com.PACOsoft.promise_betting.obj;

import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class Promise {
    private int bettingMoney = 0;//총 베팅액
    private String date = "";//약속 날짜
    private int numOfPlayer = 0;//약속 인원수
    private String promiseKey = ""; //약속 고유 키
    private String promiseName = "";//약속 이름
    private String promisePlace = "";//약속 장소
    private ArrayList<PromisePlayer> promisePlayers = new ArrayList<>();
    private String time = "";//약속 시간
    private int vote = 0;//투표 찬성 수


    public Promise() {
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

    public ArrayList<PromisePlayer> getPromisePlayer() {
        return promisePlayers;
    }

    public void setPromisePlayer(ArrayList<PromisePlayer> promisePlayers) {
        this.promisePlayers = promisePlayers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getPromisePlace() {
        return promisePlace;
    }

    public void setPromisePlace(String promisePlace) {
        this.promisePlace = promisePlace;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getbettingMoney() {
        return bettingMoney;
    }

    public void setbettingMoney(int bettingMoney) {
        this.bettingMoney = bettingMoney;
    }

}
