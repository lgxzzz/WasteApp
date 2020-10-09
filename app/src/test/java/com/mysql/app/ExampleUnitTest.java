package com.mysql.app;

import android.content.Context;

import com.mysql.app.bean.Evaluation;
import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    testManger testManger = new testManger();
    @Test
    public void register() {
        System.out.println("test register");
        User mUser = new User();
        mUser.setRole("user");
        mUser.setEmail("456@qq.com");
        mUser.setPassword("456");
        mUser.setRepeatPassword("456");
        mUser.setUserName("aimi");
        testManger.registerTestUser(mUser, new testManger.IListener() {
            @Override
            public void onSuccess() {
                System.out.println("register success");
            }

            @Override
            public void onError(String error) {
                System.out.println("register fail:"+error);
            }
        });
    }

    @Test
    public void login(){
        System.out.println("test login");
        String email = "456@qq.com";
        String password ="456";
        testManger.login(email, password, new testManger.IListener() {
            @Override
            public void onSuccess() {
                System.out.println("login success");
            }

            @Override
            public void onError(String error) {
                System.out.println("login fail:"+error);
            }
        });
    }



    Waste mWaste = new Waste();
    String id = getRandomWaste_ID();



    //生成随机userid
    public String getRandomWaste_ID(){
        String strRand="W" ;
        for(int i=0;i<10;i++){
            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return strRand;
    }

    @Test
    public void deleteWaste(){
        System.out.println("test delete Waste");
        Waste mWaste = new Waste();
        mWaste.setId(id);
        testManger.deleteWaset(mWaste, new testManger.IListener() {
            @Override
            public void onSuccess() {
                System.out.println("test delete Waste success");
            }

            @Override
            public void onError(String error) {
                System.out.println("test delete Waste fail:"+error);
            }
        });
    }


    @Test
    public void updateWaste(){
        System.out.println("test update Waste");
        mWaste.setName("bottle");
        mWaste.setTime(System.currentTimeMillis());
        mWaste.setBarCode("78901234");
        mWaste.setDescription("this is a new Waste水电费水电费爽肤sjfdlsjdfsjfsjfshfshfdfdsfdsfsdjf;sdjf;djsffsfs;js;fjds;lfjdsl;fjdsfdls;fjs;fjs;fjs;jfs;fjls;lfj;fjs;");
        mWaste.setType("Hazardous Waste");
        mWaste.setId("W9008069439");
        testManger.updateWaste(mWaste, new testManger.IListener() {
            @Override
            public void onSuccess() {
                System.out.println("test update Waste success");
            }

            @Override
            public void onError(String error) {
                System.out.println("test update Waste fail:"+error);
            }
        });
    }


    @Test
    public void insertWaste(){
        System.out.println("test insert Waste");
        mWaste.setName("bottle");
        mWaste.setTime(System.currentTimeMillis());
        mWaste.setBarCode("123456789");
        mWaste.setDescription("this is a new Waste");
        mWaste.setType("Hazardous Waste");
        mWaste.setUserId("9787121060953");
        mWaste.setId(id);
        testManger.insertWaste(mWaste, new testManger.IListener() {
            @Override
            public void onSuccess() {
                System.out.println("test insert Waste success");
            }

            @Override
            public void onError(String error) {
                System.out.println("test insert Waste fail:"+error);
            }
        });
    }

    @Test
    public void getWastesByUser(){
        System.out.println("test getWastes By User");
        User mUser = new User();
        mUser.setUserId("9787302164289");

        testManger.getWastesByUser(mUser, new testManger.IWasteListener() {


            @Override
            public void onSuccess(String wastes) {
                System.out.println("test getWastes By User success");
                System.out.println(wastes);
            }

            @Override
            public void onError(String error) {
                System.out.println("test getWastes By User fail:"+error);
            }
        });
    }

    @Test
    public void insertSearchHis(){
        System.out.println("test insert SearchHis");
        User mUser = new User();
        mUser.setUserId("9787302164289");

        String key = "water";

        testManger.insertSearchHis(mUser, key, new testManger.IListener() {
            @Override
            public void onSuccess() {
                System.out.println("test insert SearchHis success");
            }

            @Override
            public void onError(String error) {
                System.out.println("test insert SearchHis fail:"+error);
            }
        });
    }

    @Test
    public void insertEvaluation(){
        System.out.println("test insert Evaluation");

        Evaluation evaluation = new Evaluation();
        evaluation.setWasteId("W9008069439");
        evaluation.setUserId("9787302164289");
        evaluation.setComment("test comment");
        testManger.insertEvaluation(evaluation, new testManger.IListener() {
            @Override
            public void onSuccess() {
                System.out.println("test insert Evaluation success");
            }

            @Override
            public void onError(String error) {
                System.out.println("test insert Evaluation fail:"+error);
            }
        });
    }

    @Test
    public void insertSearchWasteHis(){
        System.out.println("test insert SearchWasteHis");

        testManger.insertSearchWasteHis("9787302164289", "W9008069439", new testManger.IListener() {
            @Override
            public void onSuccess() {
                System.out.println("test insert SearchWasteHis success");
            }

            @Override
            public void onError(String error) {
                System.out.println("test insert SearchWasteHis fail:"+error);
            }
        });
    }

    @Test
    public void queryEvaluations(){
        System.out.println("test query Evaluations");
        mWaste.setId("W9008069439");
        testManger.queryEvaluations(mWaste, new testManger.IEvaListener() {
            @Override
            public void onSuccess(String evaluations) {
                System.out.println("test query Evaluations success");
                System.out.println(evaluations);
            }

            @Override
            public void onError(String error) {
                System.out.println("test query Evaluations fail:"+error);
            }
        });
    }
}