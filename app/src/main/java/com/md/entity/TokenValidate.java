package com.md.entity;

import java.util.Date;

public class TokenValidate {

    private int userId;
    private String token;
    private Date requesttime;
    private Date deadline;

    public TokenValidate() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TokenValidate(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(Date requesttime) {
        this.requesttime = requesttime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }


}
