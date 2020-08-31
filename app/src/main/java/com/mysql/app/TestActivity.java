package com.mysql.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mysql.app.data.TestMgr;

/**
 *
 * 测试activity
 * */
public class TestActivity extends Activity {

    public Button mRegisterBtn;
    public Button mLoginBtn;
    public Button mInsertWasteBtn;
    public Button mUpdateWasteBtn;
    public Button mDeleteWasteBtn;

    TestMgr mTestMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTestMgr = TestMgr.getInstance(this);

        mRegisterBtn = findViewById(R.id.test_register_btn);
        mLoginBtn = findViewById(R.id.test_login_btn);
        mInsertWasteBtn = findViewById(R.id.test_insert_waste_btn);
        mUpdateWasteBtn = findViewById(R.id.test_update_waste_btn);
        mDeleteWasteBtn = findViewById(R.id.test_delete_waste_btn);


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTestMgr.testRegisterUser();
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTestMgr.testLogin();
            }
        });

        mInsertWasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTestMgr.testInsertNewWaste();
            }
        });

        mUpdateWasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTestMgr.testUpdateWaste();
            }
        });

        mDeleteWasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTestMgr.testDeteleWaste();
            }
        });
    }
}
