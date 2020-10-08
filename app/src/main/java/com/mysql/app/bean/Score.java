package com.mysql.app.bean;

import java.io.Serializable;

public class Score implements Serializable {
    public String Id;
    public String UserId;
    public String WasteId;
    public String Score = "0";
    public long Time = 0;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getWasteId() {
        return WasteId;
    }

    public void setWasteId(String wasteId) {
        WasteId = wasteId;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }
}
