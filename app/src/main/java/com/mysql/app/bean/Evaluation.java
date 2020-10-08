package com.mysql.app.bean;

public class Evaluation {
    String id;
    String WasteId;
    String UserId;
    String Comment;
    long Time;

    User mUser;
    Waste mWaste;
    Score mScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWasteId() {
        return WasteId;
    }

    public void setWasteId(String wasteId) {
        WasteId = wasteId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public Waste getmWaste() {
        return mWaste;
    }

    public void setmWaste(Waste mWaste) {
        this.mWaste = mWaste;
    }

    public Score getmScore() {
        return mScore;
    }

    public void setmScore(Score mScore) {
        this.mScore = mScore;
    }
}
