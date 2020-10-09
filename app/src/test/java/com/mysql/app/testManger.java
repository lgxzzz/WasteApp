package com.mysql.app;

import android.content.Context;
import android.util.Log;

import com.mysql.app.bean.Evaluation;
import com.mysql.app.bean.Score;
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
            // 为两个 ? 设置具体的值
            ps.setString(1, waste.getId());
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

    //添加搜索历史关键字
    public void insertSearchHis(User user,String key,IListener listener){
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
                    listener.onSuccess();
                }else{
                    listener.onError("insert searchhis fail");
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
                listener.onSuccess();
            }else{
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
    };

    //添加垃圾搜索记录
    public void insertSearchWasteHis(String user_id,String waste_id,IListener listener){
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
                        listener.onSuccess();
                    }else{
                        listener.onError("");
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

    //查询该垃圾的所有评价
    public void queryEvaluations(Waste waste, IEvaListener listener ){
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
            JSONArray jsonArray = new JSONArray();
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


                    JSONObject object = new JSONObject();
                    object.put("EVA_ID",EVA_ID);
                    object.put("WASTE_ID",WASTE_ID);
                    object.put("USER_ID",USER_ID);
                    object.put("EVA_VAlUE",EVA_VAlUE);
                    object.put("CREAT_TIME",CREAT_TIME);

                    jsonArray.put(object);
                }
                listener.onSuccess(jsonArray.toString());
            }
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
        public void onSuccess(String evaluations);
        public void onError(String error);
    };

    public interface ISearchWasteHisWasteListener{
        public void onSuccess(String wastes);
        public void onError(String error);
    };
}
