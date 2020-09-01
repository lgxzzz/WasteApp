package com.mysql.app.data;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.mysql.app.MainActivity;
import com.mysql.app.PostNewWasteActivity;
import com.mysql.app.RegisterActivity;
import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;

public class TestMgr {

    public static TestMgr instance;
    private static Handler mHandler = new Handler();
    private static Context mContext;

    public static TestMgr getInstance(Context context) {
        if (instance==null){
            instance = new TestMgr(context);
        }
        return instance;
    }

    public TestMgr(Context context){
        this.mContext= context;
    }

    //测试注册用户
    public static  void testRegisterUser(){
        User mUser = new User();
        mUser.setRole("user");
        mUser.setEmail("456@qq.com");
        mUser.setPassword("456");
        mUser.setRepeatPassword("456");
        mUser.setUserName("aimi");

        if (mUser.getUserName()==null){
            Toast.makeText(mContext,"username is empty！", Toast.LENGTH_LONG).show();
            return;
        }
        if (mUser.getEmail()==null){
            Toast.makeText(mContext,"Email is empty！", Toast.LENGTH_LONG).show();
            return;
        }
        if (mUser.getPassword()==null){
            Toast.makeText(mContext,"password is empty！", Toast.LENGTH_LONG).show();
            return;
        }
        if (mUser.getRepeatPassword()==null){
            Toast.makeText(mContext,"repeat password is empty！", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mUser.getRepeatPassword().equals(mUser.getPassword())){
            Toast.makeText(mContext,"Inconsistent password！", Toast.LENGTH_LONG).show();
            return;
        }

        DBManger.getInstance(mContext).registerUser(mUser, new DBManger.IListener() {
            @Override
            public void onSuccess() {
                User user = DBManger.getInstance(mContext).mUser;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"register success!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,error, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    //测试登陆
    public static void testLogin(){
        String email = "456@qq.com";
        String password ="456";
        DBManger.getInstance(mContext).login(email,password, new DBManger.IListener() {
            @Override
            public void onSuccess() {
                User user = DBManger.getInstance(mContext).mUser;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"login success!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,error, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    //测试添加新的垃圾
    public static void testInsertNewWaste(){
        Waste mWaste = new Waste();
        mWaste.setName("bottle");
        mWaste.setTime(System.currentTimeMillis());
        mWaste.setBarCode("123456789");
        mWaste.setDescription("this is a new Waste");
        mWaste.setType("Hazardous Waste");
        if (mWaste.getName()==null){
            Toast.makeText(mContext,"Waste name is a required field and cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (mWaste.getName()!=null&&mWaste.getName().length()==0){
            Toast.makeText(mContext,"Waste name is a required field and cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (mWaste.getName()!=null&& mWaste.getName().length()>8){
            Toast.makeText(mContext,"Waste name input should not exceed 8 characters.", Toast.LENGTH_LONG).show();
            return;
        }
        if (mWaste.getType()==null || mWaste.getType().length() == 0){
            Toast.makeText(mContext,"Waste type is Must be chosen", Toast.LENGTH_LONG).show();
            return;
        }
//                if (mWaste.getBarCode()==null){
//                    Toast.makeText(PostNewWasteActivity.this,"Waste barcode is a required field and cannot be empty", Toast.LENGTH_LONG).show();
//                    return;
//                }
        if (mWaste.getDescription()!=null&& mWaste.getDescription().length()>100){
            Toast.makeText(mContext,"Waste disposal description input should not exceed 100 characters.", Toast.LENGTH_LONG).show();
            return;
        }
        if (mWaste.getBarCode()!=null&& mWaste.getBarCode().length()>13){
            Toast.makeText(mContext,"Waste barcode identification code input should not exceed 13 integers", Toast.LENGTH_LONG).show();
            return;
        }
        DBManger.getInstance(mContext).insertWaste(mWaste, new DBManger.IListener() {
            @Override
            public void onSuccess() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"Successful post", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    //测试更改垃圾内容
    public static void testUpdateWaste(){
        Waste mWaste = new Waste();
        mWaste.setName("bottle");
        mWaste.setTime(System.currentTimeMillis());
        mWaste.setBarCode("78901234");
        mWaste.setDescription("this is a new Waste");
        mWaste.setType("Hazardous Waste");
        if (mWaste.getName()==null){
            Toast.makeText(mContext,"Waste name is a required field and cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (mWaste.getName()!=null&&mWaste.getName().length()==0){
            Toast.makeText(mContext,"Waste name is a required field and cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (mWaste.getName()!=null&& mWaste.getName().length()>8){
            Toast.makeText(mContext,"Waste name input should not exceed 8 characters.", Toast.LENGTH_LONG).show();
            return;
        }
        if (mWaste.getType()==null || mWaste.getType().length() == 0){
            Toast.makeText(mContext,"Waste type is Must be chosen", Toast.LENGTH_LONG).show();
            return;
        }
        if (mWaste.getDescription()!=null&& mWaste.getDescription().length()>100){
            Toast.makeText(mContext,"Waste disposal description input should not exceed 100 characters.", Toast.LENGTH_LONG).show();
            return;
        }
        if (mWaste.getBarCode()!=null&& mWaste.getBarCode().length()>13){
            Toast.makeText(mContext,"Waste barcode identification code input should not exceed 13 integers", Toast.LENGTH_LONG).show();
            return;
        }
        DBManger.getInstance(mContext).updateWaste(mWaste, new DBManger.IListener() {
            @Override
            public void onSuccess() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"Successful re-post", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    //测试更改垃圾内容
    public static void testDeteleWaste(){
        Waste mWaste = new Waste();
        mWaste.setId("W2492592372");
        DBManger.getInstance(mContext).deleteWaste(mWaste, new DBManger.IListener() {
            @Override
            public void onSuccess() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"delete waste Successful", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
