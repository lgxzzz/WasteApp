package com.mysql.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mysql.app.adapter.SearchHisAdapter;
import com.mysql.app.bean.SearchHis;
import com.mysql.app.data.DBManger;

import java.util.List;

public class SearchHisActivity extends Activity {

    Button mCancelBtn;
    Button mSearchBtn;
    EditText mSearchEd;
    SearchHisAdapter mAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_his);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initView(){
        mCancelBtn = findViewById(R.id.cancel_btn);
        mSearchBtn = findViewById(R.id.search_btn);
        mSearchEd = findViewById(R.id.search_ed);
        mListView = findViewById(R.id.search_his_listview);

    }

    public void initData(){
//        DBManger.getInstance(this).getSearchHisByUser();
//
//        List<SearchHis> mSearchHis =
    }
}
