package com.mysql.app.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mysql.app.LoginActivity;
import com.mysql.app.PostNewWasteActivity;
import com.mysql.app.PostedRecordActivity;
import com.mysql.app.R;
import com.mysql.app.bean.User;
import com.mysql.app.data.DBManger;


/***
 * 个人信息界面
 *
 * */
public class AboutFragment extends Fragment {

    TextView mUserRoleTv;

    Button mUserBtn;
    Button mLoginOutBtn;
    Button mCloseBtn;
    Button mPostNewBtn;
    Button mPostRecordBtn;
    User mUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragement_about, container, false);
        initView(view);

        return view;
    }

    public static AboutFragment getInstance() {
        return new AboutFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void initView(View view){

        mLoginOutBtn = view.findViewById(R.id.loginout_btn);

        mUserRoleTv = view.findViewById(R.id.user_role_tv);
        mUserBtn = view.findViewById(R.id.user_btn);
        mPostNewBtn = view.findViewById(R.id.post_new_waste_btn);
        mPostRecordBtn = view.findViewById(R.id.posted_record_btn);
    };

    public void initData() {
        mUser = DBManger.getInstance(getContext()).mUser;
        if (mUser!=null){
            mUserBtn.setText(mUser.getRole()+":"+mUser.getEmail());
        }else{
            mUserBtn.setText("USER");
        }

        mUserRoleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser == null){
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });

        mUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser == null){
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });

        mPostNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PostNewWasteActivity.class));
            }
        });
        mPostRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PostedRecordActivity.class));
            }
        });

        mLoginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser != null){
                    DBManger.getInstance(getContext()).mUser = null;
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });


    }




}
