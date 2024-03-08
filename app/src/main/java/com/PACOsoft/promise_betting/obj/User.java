package com.PACOsoft.promise_betting.obj;

public class User {
    private String profile = "";//프로필 사진
    private String UID = "";//해시값
    private String id = "";//아이디
    private String nickName = "";//닉네임
    private int account = 0;//계좌 잔액
    private String promiseKey = "";//약속 고유 코드 리스트
    private String historyKey = "";//약속 기록 고유 코드 리스트
    private String friendsUID = "";//친구 아이디 리스트
//    private String attendDate = "";//출석체크한 날
//    private Boolean isAttend = false;//출석체크 여부


    public User() {
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getPromiseKey() {
        return promiseKey;
    }

    public void setPromiseKey(String promiseKey) {
        this.promiseKey = promiseKey;
    }

    public String getHistoryKey() {
        return historyKey;
    }

    public void setHistoryKey(String historyKey) {
        this.historyKey = historyKey;
    }

    public String getFriendsUID() {
        return friendsUID;
    }

    public void setFriendsUID(String friendsUID) {
        this.friendsUID = friendsUID;
    }

//    public String getAttendDate() {
//        return attendDate;
//    }
//
//    public void setAttendDate(String attendDate) {
//        this.attendDate = attendDate;
//    }
//
//    public Boolean getIsAttend() {
//        return isAttend;
//    }
//
//    public void setIsAttend(Boolean isAttend) {
//        this.isAttend = isAttend;
//    }
}
