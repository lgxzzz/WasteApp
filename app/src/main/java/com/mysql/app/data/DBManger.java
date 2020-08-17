package com.mysql.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBManger {
    private Context mContext;
    public User mUser;
    public static  DBManger instance;

    private static final String REMOTE_IP = "10.0.2.2";
    private static final String URL = "jdbc:mysql://" + REMOTE_IP + ":3306/test_db";
    private static final String USER = "root";
    private static final String PASSWORD = "lgx199010170012";
    private Connection conn;

    public static DBManger getInstance(Context mContext){
        if (instance == null){
            instance = new DBManger(mContext);
        }
        return instance;
    };

    public DBManger(final Context mContext){
        this.mContext = mContext;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //链接
                conn = Util.openConnection(URL, USER, PASSWORD);
            }
        }).start();

    }


    //用户登陆
    public void login(String email, String password, IListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 插入数据的 sql 语句
                String insert_user_sql = "select * from User where USER_EMAIL = ? and USER_PASSWORD = ?";
                PreparedStatement ps = null;
                if (conn == null) {
                    listener.onError("connect sql fail");
                    return;
                }
                try {
                    ResultSet rs = null;
                    ps = conn.prepareStatement(insert_user_sql);
                    String userid = getRandomUSER_ID();
                    // 为两个 ? 设置具体的值
                    ps.setString(1, email);
                    ps.setString(2, password);
                    // 执行语句
                    rs = ps.executeQuery();
                    if (rs!=null){

                        if (rs.next()) {
                            String USER_ID = rs.getString("USER_ID");
                            String USER_NAME = rs.getString("USER_NAME");
                            String USER_EMAIL = rs.getString("USER_EMAIL");
                            String USER_ROLE = rs.getString("USER_ROLE");

                            mUser = new User();
                            mUser.setUserId(USER_ID);
                            mUser.setUserName(USER_NAME);
                            mUser.setEmail(USER_EMAIL);
                            mUser.setRole(USER_ROLE);
                            listener.onSuccess();
                        }else{
                            listener.onError("Wrong password");
                        }

                    }else{
                        listener.onError("The user does not exist");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    //修改用户信息
    public void updateUser(User user,IListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 更新 sql 语句
                String update_user_sql = "update User set USER_PASSWORD =?,USER_EMAIL=? where USER_NAME =?";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {
                    ps = conn.prepareStatement(update_user_sql);
                    // 为两个 ? 设置具体的值
                    ps.setString(1, user.getPassword());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, mUser.getUserName());
                    // 执行语句
                    int x = ps.executeUpdate();
                    if (x!=-1){
                        mUser = user;
                        listener.onSuccess();
                    }else{
                        listener.onError("update fail！");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    //注册用户
    public void registerUser(User user,IListener listener){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //判断用户是否存在
                    if (isUserExist(user)){
                        listener.onError("The user name is already registered！");
                        return;
                    }

                    // 插入数据的 sql 语句
                    String insert_user_sql = "insert into User (USER_ID, USER_NAME,USER_PASSWORD,USER_EMAIL,USER_ROLE) values (?,?,?,?,?)";
                    PreparedStatement ps = null;
                    if (conn == null) {
                        return;
                    }
                    try {
                        ps = conn.prepareStatement(insert_user_sql);
                        String userid = getRandomUSER_ID();
                        // 为两个 ? 设置具体的值
                        ps.setString(1, userid);
                        ps.setString(2, user.getUserName());
                        ps.setString(3, user.getPassword());
                        ps.setString(4, user.getEmail());
                        ps.setString(5, user.getRole());
                        // 执行语句
                        int x = ps.executeUpdate();
                        if (x!=-1){
                            mUser = user;
                            mUser.setUserId(userid);
                            listener.onSuccess();
                        }else{
                            listener.onError("Register fail！");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (ps != null) {
                            try {
                                ps.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
    };

    public boolean isUserExist(User user){
        // 插入数据的 sql 语句
        String insert_user_sql = "select * from User where USER_EMAIL = ?";
        PreparedStatement ps = null;
        if (conn == null) {
            return false;
        }
        try {
            ResultSet rs = null;
            ps = conn.prepareStatement(insert_user_sql);
            String userid = getRandomUSER_ID();
            // 为两个 ? 设置具体的值
            ps.setString(1, user.getUserName());
            // 执行语句
            rs = ps.executeQuery();
            if (rs!=null){
                while (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    public void insertWaset(Waste waste,IListener listener){

    }

    String pattern = "yyyy-MM-dd HH:mm:ss";
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }


    public String getDateTime(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    //生成随机userid
    public String getRandomUSER_ID(){
        String strRand="U" ;
        for(int i=0;i<10;i++){
            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return strRand;
    }

    public interface IListener{
        public void onSuccess();
        public void onError(String error);
    };


}