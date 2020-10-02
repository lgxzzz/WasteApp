package com.mysql.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mysql.app.bean.Evaluation;
import com.mysql.app.bean.SearchHis;
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

//    private static final String REMOTE_IP = "10.0.2.2";
//    private static final String REMOTE_IP = "192.168.1.101";
    private static final String REMOTE_IP = "172.20.10.2";
//    private static final String URL = "jdbc:mysql://" + REMOTE_IP + ":3306/sys";
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

    public DBManger(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //链接
                conn = Util.openConnection(URL, USER, PASSWORD);
            }
        }).start();

    }
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
                if (conn == null) {
                    listener.onError("connect sql fail");
                    return;
                }
                //判断用户是否存在
                if (!isUserExist(email)){
                    listener.onError("The user does not exist");
                    return;
                }

                // 查询 sql 语句
                String insert_user_sql = "select * from User where USER_EMAIL = ? and USER_PASSWORD = ?";
                PreparedStatement ps = null;

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
                    if (isUserExist(user.getEmail())){
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

    //判断用户是否存在
    public boolean isUserExist(String email){
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
            ps.setString(1,email);
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

    //添加新垃圾
    public void insertWaste(Waste waste,IListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //判断该垃圾是否已存在
                if (isWasteExist(waste)){
                    listener.onError("The waste is already exist！");
                    return;
                }
                // 插入数据的 sql 语句
                String insert_user_sql = "insert into Waste (WASTE_ID, WASTE_NAME,WASTE_TYPE,WASTE_DES,USER_ID,WASTE_BARCODE,WASTE_SCORE,CREAT_TIME) values (?,?,?,?,?,?,?,?)";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {
                    ps = conn.prepareStatement(insert_user_sql);
                    String userid = getRandomUSER_ID();
                    // 为两个 ? 设置具体的值
                    ps.setString(1, getRandomWaste_ID());
                    ps.setString(2, waste.getName());
                    ps.setString(3, waste.getType());
                    ps.setString(4, waste.getDescription());
                    ps.setString(5, mUser.getUserId());
                    ps.setString(6, waste.getBarCode());
                    ps.setString(7, waste.getScore());
                    ps.setLong(8, System.currentTimeMillis());
                    // 执行语句
                    int x = ps.executeUpdate();
                    if (x!=-1){
                        listener.onSuccess();
                    }else{
                        listener.onError("insert waste fail！");
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

    public void updateWaste(Waste waste,IListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 插入数据的 sql 语句
                //(WASTE_ID, WASTE_NAME,WASTE_TYPE,WASTE_DES,USER_ID,WASTE_BARCODE,WASTE_SCORE,CREAT_TIME) values (?,?,?,?,?,?,?,?)";
                String insert_user_sql = "update Waste set WASTE_NAME = ?,WASTE_TYPE = ?, WASTE_DES = ?,WASTE_BARCODE = ?,WASTE_SCORE= ? where WASTE_ID = ?";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {
                    ps = conn.prepareStatement(insert_user_sql);
                    String userid = getRandomUSER_ID();
                    // 为两个 ? 设置具体的值
                    ps.setString(1, waste.getName());
                    ps.setString(2, waste.getType());
                    ps.setString(3, waste.getDescription());
                    ps.setString(4, waste.getBarCode());
                    ps.setString(5, waste.getScore());
                    ps.setString(6, waste.getId());
                    // 执行语句
                    int x = ps.executeUpdate();
                    if (x!=-1){
                        listener.onSuccess();
                    }else{
                        listener.onError("insert waste fail！");
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

    public void deleteWaste(Waste waste,IListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 插入数据的 sql 语句
                String insert_user_sql = "delete from Waste where WASTE_ID =?";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {
                    ps = conn.prepareStatement(insert_user_sql);
                    // 为两个 ? 设置具体的值
                    ps.setString(1, waste.getId());
                    // 执行语句
                    int x = ps.executeUpdate();
                    if (x!=-1){
                        listener.onSuccess();
                    }else{
                        listener.onError("delete waste fail！");
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

    public boolean isWasteExist(Waste waste){
        // 插入数据的 sql 语句
        String insert_user_sql = "select * from Waste where WASTE_ID = ?";
        PreparedStatement ps = null;
        if (conn == null) {
            return false;
        }
        try {
            ResultSet rs = null;
            ps = conn.prepareStatement(insert_user_sql);
            String userid = getRandomUSER_ID();
            // 为两个 ? 设置具体的值
            ps.setString(1, waste.getId());
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

    //查询该垃圾的所有评价
    public List<Evaluation> queryEvaluations(User user){
        return null;
    }

    //获取所有垃圾数据
    public void getWastesByUser(User user,IWasteListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Waste> wastes = new ArrayList<>();
                // 插入数据的 sql 语句
                String sql = "select * from Waste where USER_ID = ?";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {

                    ResultSet rs = null;
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, mUser.getUserId());
                    // 执行语句
                    rs = ps.executeQuery();
                    if (rs!=null){
                        // 展开结果集数据库
                    while(rs.next()){
                        // 通过字段检索
                        String WASTE_ID = rs.getString("WASTE_ID");
                        String WASTE_NAME = rs.getString("WASTE_NAME");
                        String WASTE_TYPE = rs.getString("WASTE_TYPE");
                        String WASTE_DES = rs.getString("WASTE_DES");
                        String USER_ID = rs.getString("USER_ID");
                        String WASTE_BARCODE = rs.getString("WASTE_BARCODE");
                        String WASTE_SCORE = rs.getString("WASTE_SCORE");
                        long CREAT_TIME = rs.getBigDecimal("CREAT_TIME").longValue();

                        Waste waste = new Waste();
                        waste.setId(WASTE_ID);
                        waste.setName(WASTE_NAME);
                        waste.setType(WASTE_TYPE);
                        waste.setDescription(WASTE_DES);
                        waste.setUserId(USER_ID);
                        waste.setBarCode(WASTE_BARCODE);
                        waste.setScore(WASTE_SCORE);
                        waste.setTime(CREAT_TIME);
                        wastes.add(waste);
                        }
                        listener.onSuccess(wastes);
                    }

//                    ps = conn.prepareStatement(sql);
//                    ps.setString(1, user.getUserId());
//                    // 执行语句
//                    ResultSet rs = ps.executeQuery(sql);
//
//
                    // 完成后关闭
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    listener.onError("");
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

    //获取该用户的历史搜索关键字
    public void getSearchHisByUser(User user,ISearchHisListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SearchHis> searchHis = new ArrayList<>();
                // 插入数据的 sql 语句
                String sql = "select * from SearchHis where USER_ID = ?";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {

                    ResultSet rs = null;
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, mUser.getUserId());
                    // 执行语句
                    rs = ps.executeQuery();
                    if (rs!=null){
                        // 展开结果集数据库
                        while(rs.next()){
                            // 通过字段检索
                            String SEARCH_ID = rs.getString("SEARCH_ID");
                            String USER_ID = rs.getString("WASTE_NAME");
                            String SEARCH_KEY = rs.getString("WASTE_TYPE");
                            long CREAT_TIME = rs.getBigDecimal("CREAT_TIME").longValue();

                            SearchHis searchHis1 = new SearchHis();
                            searchHis1.setUserId(mUser.getUserId());
                            searchHis1.setId(SEARCH_ID);
                            searchHis1.setSearchKey(SEARCH_KEY);
                            searchHis1.setTime(CREAT_TIME);
                            searchHis.add(searchHis1);
                        }
                        listener.onSuccess(searchHis);
                    }

//                    ps = conn.prepareStatement(sql);
//                    ps.setString(1, user.getUserId());
//                    // 执行语句
//                    ResultSet rs = ps.executeQuery(sql);
//
//
                    // 完成后关闭
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    listener.onError("");
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

    //生成随机userid
    public String getRandomWaste_ID(){
        String strRand="W" ;
        for(int i=0;i<10;i++){
            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return strRand;
    }
    public interface IListener{
        public void onSuccess();
        public void onError(String error);
    };
    public interface IWasteListener{
        public void onSuccess(List<Waste> wastes);
        public void onError(String error);
    };

    public interface ISearchHisListener{
        public void onSuccess(List<SearchHis> searchHis);
        public void onError(String error);
    };

}
