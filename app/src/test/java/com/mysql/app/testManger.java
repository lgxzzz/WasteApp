package com.mysql.app;

import android.content.Context;
import android.util.Log;

import com.mysql.app.bean.Evaluation;
import com.mysql.app.bean.SearchHis;
import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;
import com.mysql.app.data.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class testManger {
    public User mUser;
    public static testManger instance;

//    private static final String REMOTE_IP = "10.0.2.2";
    private static final String REMOTE_IP = "192.168.1.173";
    private static final String URL = "jdbc:mysql://" + REMOTE_IP + ":3306/sys";
//    private static final String URL = "jdbc:mysql://" + REMOTE_IP + ":3306/test_db";
    private static final String USER = "root";
    private static final String PASSWORD = "lgx199010170012";
    private Connection conn;


    public static testManger getInstance(){
        return instance;
    };
    public testManger(){
        conn = Util.openConnection(URL, USER, PASSWORD);
    }

    //用户登陆
    public void login(String email, String password, IListener listener){
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

    //注册用户
    public void registerTestUser(User user,IListener listener){
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
    };

    //注册用户
    public void registerUser(User user,IListener listener){
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
            ps.setString(5, waste.getUserId());
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
            listener.onError("insert waste fail！");
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

    public void updateWaste(Waste waste,IListener listener){
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

    public void updateWasteScore(Waste waste,String scroe){
        // 插入数据的 sql 语句
        //(WASTE_ID, WASTE_NAME,WASTE_TYPE,WASTE_DES,USER_ID,WASTE_BARCODE,WASTE_SCORE,CREAT_TIME) values (?,?,?,?,?,?,?,?)";
        String insert_user_sql = "update Waste set WASTE_SCORE= ? where WASTE_ID = ?";
        PreparedStatement ps = null;
        if (conn == null) {
            return;
        }
        try {
            ps = conn.prepareStatement(insert_user_sql);
            // 为两个 ? 设置具体的值
            ps.setString(1, scroe);
            ps.setString(2, waste.getId());
            // 执行语句
            int x = ps.executeUpdate();
            if (x!=-1){

            }else{

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

    public void deleteWaset(Waste waste,IListener listener){
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


    //获取所有垃圾数据
    public void getWastesByUser(User user,testManger.IWasteListener listener){
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
            ps.setString(1, user.getUserId());
            // 执行语句
            rs = ps.executeQuery();
            if (rs!=null){
                // 展开结果集数据库
                JSONArray jsonArray = new JSONArray();
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

                    JSONObject object = new JSONObject();
                    object.put("WASTE_ID",WASTE_ID);
                    object.put("WASTE_NAME",WASTE_NAME);
                    object.put("WASTE_TYPE",WASTE_TYPE);
                    object.put("WASTE_DES",WASTE_DES);
                    object.put("WASTE_BARCODE",WASTE_BARCODE);
                    object.put("WASTE_SCORE",WASTE_SCORE);
                    object.put("CREAT_TIME",CREAT_TIME);

                    jsonArray.put(object);
                }
                listener.onSuccess(jsonArray.toString());
            }
            // 完成后关闭
            rs.close();
        } catch (Exception e) {
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

    //生成随机userid
    public String getRandomSEARCH_ID(){
        String strRand="S" ;
        for(int i=0;i<10;i++){
            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return strRand;
    }

    //生成随机userid
    public String getRandomEVA_ID(){
        String strRand="E" ;
        for(int i=0;i<10;i++){
            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return strRand;
    }

    //生成随机userid
    public String getRandomScore_ID(){
        String strRand="S" ;
        for(int i=0;i<10;i++){
            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return strRand;
    }

    public String getRandomSearchWasteHis_ID(){
        String strRand="SW" ;
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
        public void onSuccess(String wastes);
        public void onError(String error);
    };

    public interface ISearchHisListener{
        public void onSuccess(List<SearchHis> searchHis);
        public void onError(String error);
    };

    public interface IEvaListener{
        public void onSuccess(List<Evaluation> evaluations);
        public void onError(String error);
    };
}
