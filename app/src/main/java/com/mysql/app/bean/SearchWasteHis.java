package com.mysql.app.bean;

import java.io.Serializable;

public class SearchWasteHis implements Serializable{
    private String Id;
    private String UserId;
    private String WasteId;
    private long Time;
    Waste mWaste;

    public Waste getmWaste() {
        return mWaste;
    }

    public void setmWaste(Waste mWaste) {
        this.mWaste = mWaste;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getWasteId() {
        return WasteId;
    }

    public void setWasteId(String wasteId) {
        WasteId = wasteId;
    }
}
