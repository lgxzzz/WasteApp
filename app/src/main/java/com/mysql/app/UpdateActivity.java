package com.mysql.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mysql.app.bean.User;
import com.mysql.app.data.DBManger;

/***
 * 更新用户activity
 *
 * */
public class UpdateActivity extends AppCompatActivity {

    private EditText mNameEd;
    private EditText mPassWordEd;
    private EditText mRepeatPassWordEd;
    private EditText mTelEd;
    private Button mRegBtn;
    private User mUser;

    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        init();
    }

    public void init(){
        mUser = DBManger.getInstance(this).mUser;


        mNameEd = findViewById(R.id.reg_name_ed);
        mPassWordEd = findViewById(R.id.reg_password_ed);
        mRepeatPassWordEd = findViewById(R.id.reg_repeat_password_ed);
        mTelEd = findViewById(R.id.reg_phone_ed);
        mRegBtn = findViewById(R.id.reg_btn);
        mNameEd.setHint(mUser.getUserName());

        //输入姓名
        mNameEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mUser.setUserName(editable.toString());
            }
        });
        //输入密码
        mPassWordEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mUser.setPassword(editable.toString());
            }
        });
        //输入重复密码
        mRepeatPassWordEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mUser.setRepeatPassword(editable.toString());
            }
        });


        //点击注册，判断用户名或者密码是否不为空
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mUser.getUserName()==null){
//                    Toast.makeText(UpdateActivity.this,"用户名不能为空！", Toast.LENGTH_LONG).show();
//                    return;
//                }
                if (mUser.getPassword()==null){
                    Toast.makeText(UpdateActivity.this,"密码不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mUser.getRepeatPassword()==null){
                    Toast.makeText(UpdateActivity.this,"重复密码不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!mUser.getRepeatPassword().equals(mUser.getPassword())){
                    Toast.makeText(UpdateActivity.this,"两次密码不一致！", Toast.LENGTH_LONG).show();
                    return;
                }

                DBManger.getInstance(UpdateActivity.this).updateUser(mUser, new DBManger.IListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError(String error) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UpdateActivity.this,error, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
            }
        });
    }
}
