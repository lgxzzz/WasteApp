package com.mysql.app;

import android.app.Application;

import com.mysql.app.data.DBManger;

public class MysqlApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();;;;;;;;
        DBManger dbManger = DBManger.getInstance(this);
    }
}
