package com.mysql.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mysql.app.adapter.SearchHisAdapter;
import com.mysql.app.bean.SearchHis;
import com.mysql.app.bean.User;
import com.mysql.app.data.DBManger;

import java.util.List;

public class SearchHisActivity extends Activity {

    Button mCancelBtn;
    Button mSearchBtn;
    EditText mSearchEd;
    SearchHisAdapter mAdapter;
    ListView mListView;
    List<SearchHis> mSearchHis;
    User mUser;
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
        mCancelBtn = findViewById(R.id.back_btn);
        mSearchBtn = findViewById(R.id.search_btn);
        mSearchEd = findViewById(R.id.search_ed);
        mListView = findViewById(R.id.search_his_listview);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mSearchEd.getText().toString();
                if (key!=null&&key.length()>0){
                    searchByKey(key);
                }
            }
        });

    }

    public void initData(){
        mUser = DBManger.getInstance(this).mUser;
        DBManger.getInstance(this).getSearchHisByUser(mUser, new DBManger.ISearchHisListener() {
            @Override
            public void onSuccess(List<SearchHis> searchHis) {
                if (searchHis!=null&&searchHis.size()>0){
                    mSearchHis = searchHis;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new SearchHisAdapter(SearchHisActivity.this,searchHis);
                            mListView.setAdapter(mAdapter);
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String key = mSearchHis.get(i).getSearchKey();
                                    searchByKey(key);
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void searchByKey(String key){
        Intent intent = new Intent(this, SearchResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("searchkey",key);
        bundle.putBoolean("isInsert",true);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
