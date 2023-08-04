package com.PACOsoft.promise_betting;

public class Promise {
    private String promiseCode; //약속 고유 코드
    private String promiseName;//약속 이름
    private int numOfPlayer;//약속 인원수
    private int bettingMoney;//총 배팅 금액
    private String nickName;//닉네임
    private String position;//사람별 위치
    private int ranking;//사람별 순위
    private int arrival;//사람별 도착 여부
    private String date;//약속 날짜 + 시간
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

    public int getBettingMoney() {
        return bettingMoney;
    }

    public void setBettingMoney(int bettingMoney) {
        this.bettingMoney = bettingMoney;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

}
