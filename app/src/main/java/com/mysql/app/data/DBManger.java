package com.mysql.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mysql.app.bean.Evaluation;
import com.mysql.app.bean.Score;
import com.mysql.app.bean.SearchHis;
import com.mysql.app.bean.SearchWasteHis;
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
    private static final String REMOTE_IP = "192.168.1.101";
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

    public void updateWasteScore(Waste waste,String scroe){
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }

    public void deleteWaset(Waste waste,IListener listener){
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
    public void queryEvaluations(Waste waste,IEvaListener listener ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Evaluation> evaluations = new ArrayList<>();
                // 插入数据的 sql 语句
                String sql = "select * from Evaluation where WASTE_ID = ?";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {

                    ResultSet rs = null;
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, waste.getId());
                    // 执行语句
                    rs = ps.executeQuery();
                    if (rs!=null){
                        // 展开结果集数据库
                        while(rs.next()){
                            // 通过字段检索
                            String EVA_ID = rs.getString("EVA_ID");
                            String WASTE_ID = rs.getString("WASTE_ID");
                            String USER_ID = rs.getString("USER_ID");
                            String EVA_VAlUE = rs.getString("EVA_VAlUE");
                            long CREAT_TIME = rs.getBigDecimal("CREAT_TIME").longValue();

                            User user = queryUserById(USER_ID);
                            Score score = queryScore(USER_ID,WASTE_ID);

                            Evaluation evaluation = new Evaluation();
                            evaluation.setId(EVA_ID);
                            evaluation.setWasteId(WASTE_ID);
                            evaluation.setUserId(USER_ID);
                            evaluation.setComment(EVA_VAlUE);
                            evaluation.setTime(CREAT_TIME);
                            evaluations.add(evaluation);
                            evaluation.setmUser(user);
                            evaluation.setmScore(score);
                        }
                        listener.onSuccess(evaluations);
                    }
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

    //查询该垃圾的所有评分
    public void queryWasteScores(Waste waste,IScoreListener listener ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Score> scores = new ArrayList<>();
                // 插入数据的 sql 语句
                String sql = "select * from Score where WASTE_ID = ?";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {

                    ResultSet rs = null;
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, waste.getId());
                    // 执行语句
                    rs = ps.executeQuery();
                    if (rs!=null){
                        // 展开结果集数据库
                        while(rs.next()){
                            // 通过字段检索
                            String SCORE_ID = rs.getString("SCORE_ID");
                            String WASTE_ID = rs.getString("WASTE_ID");
                            String USER_ID = rs.getString("USER_ID");
                            String SCORE_VALUE = rs.getString("SCORE_VALUE");
                            long CREAT_TIME = rs.getBigDecimal("CREAT_TIME").longValue();

                            Score score = new Score();
                            score.setId(SCORE_ID);
                            score.setWasteId(WASTE_ID);
                            score.setUserId(USER_ID);
                            score.setScore(SCORE_VALUE);
                            score.setTime(CREAT_TIME);
                            scores.add(score);
                        }
                        listener.onSuccess(scores);
                    }
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

    //获取所有垃圾数据
    public void getWastesByUser(User user,IWasteListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Waste> wastes = new ArrayList<>();
                // 插入数据的 sql 语句
                String sql = "select * from Waste where USER_ID = ? ORDER BY CREAT_TIME DESC";
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

    //获取所有垃圾数据
    public Waste getWastesById(String waste_id){
        Waste waste = new Waste();
        // 插入数据的 sql 语句
        String sql = "select * from Waste where WASTE_ID = ?";
        PreparedStatement ps = null;
        if (conn == null) {
            return null;
        }
        try {

            ResultSet rs = null;
            ps = conn.prepareStatement(sql);
            ps.setString(1,waste_id);
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

                    waste.setId(WASTE_ID);
                    waste.setName(WASTE_NAME);
                    waste.setType(WASTE_TYPE);
                    waste.setDescription(WASTE_DES);
                    waste.setUserId(USER_ID);
                    waste.setBarCode(WASTE_BARCODE);
                    waste.setScore(WASTE_SCORE);
                    waste.setTime(CREAT_TIME);
                }
            }
            // 完成后关闭
            rs.close();
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
        return waste;
    }

    //获取该用户的历史搜索关键字
    public void getSearchHisByUser(User user,ISearchHisListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (user== null){
                    listener.onError("please login before...");
                    return;
                }
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
                    ps.setString(1, user.getUserId());
                    // 执行语句
                    rs = ps.executeQuery();
                    if (rs!=null){
                        // 展开结果集数据库
                        while(rs.next()){
                            // 通过字段检索
                            String SEARCH_ID = rs.getString("SEARCH_ID");
                            String USER_ID = rs.getString("USER_ID");
                            String SEARCH_KEY = rs.getString("SEARCH_KEY");
                            long CREAT_TIME = rs.getBigDecimal("CREAT_TIME").longValue();

                            SearchHis searchHis1 = new SearchHis();
                            searchHis1.setUserId(user.getUserId());
                            searchHis1.setId(SEARCH_ID);
                            searchHis1.setSearchKey(SEARCH_KEY);
                            searchHis1.setTime(CREAT_TIME);
                            searchHis.add(searchHis1);
                        }
                        listener.onSuccess(searchHis);
                    }
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

    //根据关键字搜索垃圾，如果不是游客，则添加该搜索关键字记录
    public void searchWasteByKeyWord(User user,String key,boolean isInsert,IWasteListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isInsert){
                    insertSearchHis(user,key);
                }
                List<Waste> wastes = new ArrayList<>();
                // 插入数据的 sql 语句
                String sql = "select * from Waste where WASTE_NAME like ? or WASTE_TYPE like ? or WASTE_DES like ? or WASTE_BARCODE like ?";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {

                    ResultSet rs = null;
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, "%"+key+"%");
                    ps.setString(2, "%"+key+"%");
                    ps.setString(3, "%"+key+"%");
                    ps.setString(4, "%"+key+"%");
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

    //添加搜索历史关键字
    public void insertSearchHis(User user,String key){
        if(user!=null){
            // 插入数据的 sql 语句
            String insert_sql = "insert into SearchHis (SEARCH_ID,USER_ID, SEARCH_KEY,CREAT_TIME) values (?,?,?,?)";
            PreparedStatement ps = null;
            if (conn == null) {
                return;
            }
            try {
                ps = conn.prepareStatement(insert_sql);
                String search_id = getRandomSEARCH_ID();
                // 为两个 ? 设置具体的值
                ps.setString(1, search_id);
                ps.setString(2, user.getUserId());
                ps.setString(3, key);
                ps.setLong(4, System.currentTimeMillis());
                int x = ps.executeUpdate();
                if (x!=-1){
                    Log.e("lgx","insert searchhis sucess");
                }else{
                    Log.e("lgx","insert searchhis fail");
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
    };

    //添加评论
    public void insertEvaluation(Evaluation evaluation,IListener listener){
       new Thread(new Runnable() {
           @Override
           public void run() {
               // 插入数据的 sql 语句
               String insert_sql = "insert into Evaluation (EVA_ID,WASTE_ID, USER_ID,EVA_VAlUE,CREAT_TIME) values (?,?,?,?,?)";
               PreparedStatement ps = null;
               if (conn == null) {
                   return;
               }
               try {
                   ps = conn.prepareStatement(insert_sql);
                   String eva_id = getRandomEVA_ID();
                   // 为两个 ? 设置具体的值
                   ps.setString(1, eva_id);
                   ps.setString(2, evaluation.getWasteId());
                   ps.setString(3, evaluation.getUserId());
                   ps.setString(4, evaluation.getComment());
                   ps.setLong(5, System.currentTimeMillis());
                   int x = ps.executeUpdate();
                   if (x!=-1){
                       Log.e("lgx","insert eva sucess");
                       listener.onSuccess();
                   }else{
                       Log.e("lgx","insert eva fail");
                       listener.onError("");
                   }
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
    };

    //根据id查询用户
    public User queryUserById(String id){
            User user = new User();
            // 插入数据的 sql 语句
            String sql = "select * from User where USER_ID = ?";
            PreparedStatement ps = null;
            if (conn == null) {
                return user;
            }
            try {
                ResultSet rs = null;
                ps = conn.prepareStatement(sql);
                // 为两个 ? 设置具体的值
                ps.setString(1, id);
                // 执行语句
                rs = ps.executeQuery();
                if (rs!=null){

                    if (rs.next()) {
                        String USER_ID = rs.getString("USER_ID");
                        String USER_NAME = rs.getString("USER_NAME");
                        String USER_EMAIL = rs.getString("USER_EMAIL");
                        String USER_ROLE = rs.getString("USER_ROLE");

                        user.setUserId(USER_ID);
                        user.setUserName(USER_NAME);
                        user.setEmail(USER_EMAIL);
                        user.setRole(USER_ROLE);
                    }else{

                    }

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
            return user;
    };

    //根据id查询用户
    public Score queryScore(String user_id,String waste_id){
        Score score = new Score();
        // 插入数据的 sql 语句
        String sql = "select * from Score where USER_ID = ? and WASTE_ID = ?";
        PreparedStatement ps = null;
        if (conn == null) {
            return score;
        }
        try {
            ResultSet rs = null;
            ps = conn.prepareStatement(sql);
            // 为两个 ? 设置具体的值
            ps.setString(1, user_id);
            ps.setString(2, waste_id);
            // 执行语句
            rs = ps.executeQuery();
            if (rs!=null){

                if (rs.next()) {
                    String SCORE_ID = rs.getString("SCORE_ID");
                    String WASTE_ID = rs.getString("WASTE_ID");
                    String USER_ID = rs.getString("USER_ID");
                    String SCORE_VALUE = rs.getString("SCORE_VALUE");

                    score.setUserId(USER_ID);
                    score.setScore(SCORE_VALUE);
                    score.setWasteId(WASTE_ID);
                    score.setId(SCORE_ID);
                }else{

                }

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
        return score;
    };

    //查询分数
    public void insertScore(User user,Waste waste,String value,IListener listener){
        // 更新 sql 语句
        String insert_user_sql = "insert into Score (SCORE_ID,WASTE_ID,USER_ID, SCORE_VALUE,CREAT_TIME) values (?,?,?,?,?)";
        PreparedStatement ps = null;
        if (conn == null) {
            return;
        }
        try {
            String score_id = getRandomScore_ID();

            ps = conn.prepareStatement(insert_user_sql);
            // 为两个 ? 设置具体的值
            ps.setString(1, score_id);
            ps.setString(2, waste.getId());
            ps.setString(3, user.getUserId());
            ps.setString(4, value);
            ps.setLong(5, System.currentTimeMillis());
            // 执行语句
            int x = ps.executeUpdate();
            if (x!=-1){
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

    //修改评分
    public void updateScore(User user,Waste waste,String value,IListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isScoreExist(user,waste)){
                    insertScore(mUser,waste,value,listener);
                }else{
                    // 更新 sql 语句
                    String update_user_sql = "update Score set SCORE_VALUE =? where USER_ID = ? and WASTE_ID = ?";
                    PreparedStatement ps = null;
                    if (conn == null) {
                        return;
                    }
                    try {
                        ps = conn.prepareStatement(update_user_sql);
                        // 为两个 ? 设置具体的值
                        ps.setString(1, value);
                        ps.setString(2, user.getUserId());
                        ps.setString(3, waste.getId());
                        // 执行语句
                        int x = ps.executeUpdate();
                        if (x!=-1){
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
        }
        }).start();
    }

    //是否该用户已经评论过该垃圾
    public boolean isScoreExist(User user,Waste waste){
        // 插入数据的 sql 语句
        String insert_user_sql = "select * from Score where USER_ID = ? and WASTE_ID = ?";
        PreparedStatement ps = null;
        if (conn == null) {
            return false;
        }
        try {
            ResultSet rs = null;
            ps = conn.prepareStatement(insert_user_sql);
            // 为两个 ? 设置具体的值
            ps.setString(1, user.getUserId());
            ps.setString(2, waste.getId());
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

    //根据用户获取搜索记录
    public void getSearchWasteHisByUser(User user,ISearchWasteHisListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<SearchWasteHis> searchWasteHis = new ArrayList<>();
                // 插入数据的 sql 语句
                String sql = "select * from SearcWasteHis where USER_ID = ?";
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
                        while(rs.next()){
                            String SEACH_WASTE_HIS_ID = rs.getString("SEACH_WASTE_HIS_ID");
                            String WASTE_ID = rs.getString("WASTE_ID");
                            String USER_ID = rs.getString("USER_ID");
                            long CREAT_TIME = rs.getBigDecimal("CREAT_TIME").longValue();

                            Waste waste = getWastesById(WASTE_ID);

                            SearchWasteHis searchWasteHis1 = new SearchWasteHis();
                            searchWasteHis1.setId(SEACH_WASTE_HIS_ID);
                            searchWasteHis1.setWasteId(WASTE_ID);
                            searchWasteHis1.setUserId(USER_ID);
                            searchWasteHis1.setTime(CREAT_TIME);
                            searchWasteHis1.setmWaste(waste);
                            searchWasteHis.add(searchWasteHis1);
                        }
                        listener.onSuccess(searchWasteHis);
                    }
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

    //添加垃圾搜索记录
    public void insertSearchWasteHis(String user_id,String waste_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isSearchWasteBefore(user_id,waste_id)){
                    return ;
                }
                // 更新 sql 语句
                String insert_user_sql = "insert into SearcWasteHis (SEACH_WASTE_HIS_ID,WASTE_ID,USER_ID,CREAT_TIME) values (?,?,?,?)";
                PreparedStatement ps = null;
                if (conn == null) {
                    return;
                }
                try {
                    String searchWasteHis_id = getRandomSearchWasteHis_ID();

                    ps = conn.prepareStatement(insert_user_sql);
                    // 为两个 ? 设置具体的值
                    ps.setString(1, searchWasteHis_id);
                    ps.setString(2, waste_id);
                    ps.setString(3,user_id);
                    ps.setLong(4, System.currentTimeMillis());
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
        }).start();
    }

    //是否该用户已经搜索过该垃圾
    public boolean isSearchWasteBefore(String user_id,String waste_id){
        // 插入数据的 sql 语句
        String insert_user_sql = "select * from SearcWasteHis where USER_ID = ? and WASTE_ID = ?";
        PreparedStatement ps = null;
        if (conn == null) {
            return false;
        }
        try {
            ResultSet rs = null;
            ps = conn.prepareStatement(insert_user_sql);
            // 为两个 ? 设置具体的值
            ps.setString(1,user_id);
            ps.setString(2, waste_id);
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
        public void onSuccess(List<Waste> wastes);
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

    public interface IScoreListener{
        public void onSuccess(List<Score> scores);
        public void onError(String error);
    };

    public interface ISearchWasteHisListener{
        public void onSuccess(List<SearchWasteHis> searchWasteHis);
        public void onError(String error);
    };
}
