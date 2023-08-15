package com.PACOsoft.promise_betting.obj;

import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class Promise implements Serializable {
    private String promiseCode; //약속 고유 코드
    private String promiseName;//약속 이름
    private int numOfPlayer;//약속 인원수
    private ArrayList<PromisePlayer> promisePlayers;
    private String date;//약속 날짜
    private String time;//약속 시간
    private String promisePlace;//약속 장소
    private int vote;//투표 찬성 수

    public Promise() {
    }

    public String getPromiseCode() {
        return promiseCode;
    }

    public void setPromiseCode(String promiseCode) {
        this.promiseCode = promiseCode;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) { this.time = time; }

    public String getTime() { return time; }

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

}
