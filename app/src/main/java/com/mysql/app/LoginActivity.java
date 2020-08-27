package com.mysql.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mysql.app.bean.User;
import com.mysql.app.data.DBManger;


/***
 * 登录activity
 *
 * */
public class LoginActivity extends Activity implements View.OnClickListener{

    private TextView mRegisterView;
    private EditText mNameEd;
    private EditText mPassWordEd;
    private String mName;
    private String mPassWord;
    private Button mLoginBtn;
    private Button mCloseBtn;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){

        mRegisterView = findViewById(R.id.login_to_register_btn);
        mRegisterView.setOnClickListener(this);
        mCloseBtn = findViewById(R.id.close_btn);
        mNameEd = findViewById(R.id.reg_name_ed);
        mPassWordEd = findViewById(R.id.reg_password_ed);
        mCloseBtn.setOnClickListener(this);
        mLoginBtn = findViewById(R.id.reg_login_btn);
        mLoginBtn.setOnClickListener(this);


        mNameEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mName = editable.toString();
            }
        });

        mPassWordEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               mPassWord = editable.toString();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_btn:
                finish();
                break;
            case R.id.login_to_register_btn:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.reg_login_btn:
                //登录操作
                DBManger.getInstance(LoginActivity.this).login(mName, mPassWord, new DBManger.IListener() {
                    @Override
                    public void onSuccess() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"Login Success!", Toast.LENGTH_LONG).show();
                                User user = DBManger.getInstance(getBaseContext()).mUser;
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                            }
                        });

                    }

                    @Override
                    public void onError(String error) {

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                break;
        }
    }
}
