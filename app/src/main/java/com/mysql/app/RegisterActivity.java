package com.mysql.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.mysql.app.bean.User;
import com.mysql.app.data.DBManger;

import java.util.ArrayList;
import java.util.List;

/***
 * 注册用户activity
 *
 * */
public class RegisterActivity extends AppCompatActivity {

    private EditText mNameEd;
    private EditText mPassWordEd;
    private EditText mRepeatPassWordEd;
    private EditText mEmailEd;
    private Button mRegBtn;
    private User mUser;
    private Spinner mRoleSp;
    private String mSelectRole="";

    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    public void init(){
        mUser = new User();


        mNameEd = findViewById(R.id.reg_name_ed);
        mPassWordEd = findViewById(R.id.reg_password_ed);
        mRepeatPassWordEd = findViewById(R.id.reg_repeat_password_ed);
        mEmailEd = findViewById(R.id.reg_email_ed);
        mRegBtn = findViewById(R.id.reg_btn);
        mRoleSp = findViewById(R.id.user_role);

        final List<String> mRoles =new ArrayList<>();
        mRoles.add("User");
        mRoles.add("Administrator");

        //选择用户角色
        SpinnerAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mRoles);
        mRoleSp.setAdapter(adapter);
        mSelectRole = mRoles.get(0);

        mRoleSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectRole = mRoles.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        //输入邮箱
        mEmailEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mUser.setEmail(editable.toString());
            }
        });

        //点击注册，判断用户名或者密码是否不为空
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser.getUserName()==null){
                    Toast.makeText(RegisterActivity.this,"username is empty！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mUser.getEmail()==null){
                    Toast.makeText(RegisterActivity.this,"Email is empty！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mUser.getPassword()==null){
                    Toast.makeText(RegisterActivity.this,"password is empty！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mUser.getRepeatPassword()==null){
                    Toast.makeText(RegisterActivity.this,"repeat password is empty！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!mUser.getRepeatPassword().equals(mUser.getPassword())){
                    Toast.makeText(RegisterActivity.this,"Inconsistent password！", Toast.LENGTH_LONG).show();
                    return;
                }
                mUser.setRole(mSelectRole);
                DBManger.getInstance(RegisterActivity.this).registerUser(mUser, new DBManger.IListener() {
                    @Override
                    public void onSuccess() {
                        User user = DBManger.getInstance(getBaseContext()).mUser;
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError(String error) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this,error, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
            }
        });
    }
}
