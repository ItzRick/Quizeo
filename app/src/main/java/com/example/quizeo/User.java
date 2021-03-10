package com.example.quizeo;

public class User {

    private String nickName;

    private int userId;

    public User(String nickName, int userId) {
        this.nickName = nickName;
        this.userId = userId;
    }

    public User() {
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public int getUserId() {
        return userId;
    }
}
