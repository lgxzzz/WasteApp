package com.mysql.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.mysql.app.adapter.PostNewWasteAdapter;
import com.mysql.app.adapter.SearchWasteAdapter;
import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;
import com.mysql.app.view.TitleView;

import java.util.List;

public class SearchDetailActivity extends Activity {
    private TitleView mTitleView;
    private SearchWasteAdapter mAdapter;
    private ListView mListView;
    private List<Waste> mWastes;
    private User mUser;
    private String mKey;
    private boolean mIsInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_waste_detail);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initView(){
        mTitleView = findViewById(R.id.title_view);
        mTitleView.setTitle("Result");
        mTitleView.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView = findViewById(R.id.waste_search_result_listview);
    }

    public void initData(){
        mUser = DBManger.getInstance(this).mUser;
        mKey = getIntent().getExtras().getString("searchkey");
        mIsInsert = getIntent().getExtras().getBoolean("isInsert");
        searchByKey();
    }

    public void searchByKey(){
        DBManger.getInstance(this).searchWasteByKeyWord(mUser, mKey, mIsInsert,new DBManger.IWasteListener() {
            @Override
            public void onSuccess(List<Waste> wastes) {
                mWastes = wastes;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new SearchWasteAdapter(SearchDetailActivity.this,wastes);
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
