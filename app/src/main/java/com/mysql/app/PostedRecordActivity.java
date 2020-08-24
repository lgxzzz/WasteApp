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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mysql.app.adapter.WasteAdapter;
import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;
import com.mysql.app.view.TitleView;

import java.util.List;


/***
 * 查看发布列表
 *
 * */
public class PostedRecordActivity extends Activity{

    private TitleView mTitleView;
    private ListView mListView;
    private WasteAdapter mAdapter;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_record);
        initView();

    }

    public void initView(){
        mTitleView = findViewById(R.id.title_view);
        mTitleView.setTitle("Posted Record");
        mTitleView.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView = findViewById(R.id.waste_posted_record_listview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initData(){
        User user = DBManger.getInstance(this).mUser;
         DBManger.getInstance(this).getWastesByUser(user, new DBManger.IWasteListener() {
            @Override
            public void onSuccess(List<Waste> wastes) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new WasteAdapter(PostedRecordActivity.this,wastes);
                        mListView.setAdapter(mAdapter);
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });

    }
}
