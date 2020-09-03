package com.mysql.app;

import android.app.Application;

import com.mysql.app.data.DBManger;

public class MysqlApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库相关
        DBManger dbManger = DBManger.getInstance(this);
    }
}
