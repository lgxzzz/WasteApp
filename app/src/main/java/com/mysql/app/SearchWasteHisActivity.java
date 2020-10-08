package com.mysql.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.mysql.app.adapter.SearchWasteHisAdapter;
import com.mysql.app.bean.SearchWasteHis;
import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;
import com.mysql.app.view.LeftSwipeMenuRecyclerView;
import com.mysql.app.view.SearchWasteHisDialog;
import com.mysql.app.view.TitleView;

import java.util.List;


/***
 * 查看搜索的垃圾列表
 *
 * */
public class SearchWasteHisActivity extends Activity{

    private TitleView mTitleView;
    private LeftSwipeMenuRecyclerView mListView;
    private SearchWasteHisAdapter mAdapter;
    private List<SearchWasteHis> mSearchWasteHis;
    private SearchWasteHisDialog mDialog;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_record);
        initView();

    }

    public void initView(){
        mTitleView = findViewById(R.id.title_view);
        mTitleView.setTitle("History Record");
        mTitleView.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView = findViewById(R.id.waste_posted_record_listview);
        mDialog = new SearchWasteHisDialog(this);
        mDialog.setListener(new SearchWasteHisDialog.ISearchWasteHisDialogListener() {
            @Override
            public void onDelete() {
                initData();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initData(){
        User user = DBManger.getInstance(this).mUser;
         DBManger.getInstance(this).getSearchWasteHisByUser(user, new DBManger.ISearchWasteHisListener() {
            @Override
            public void onSuccess(List<SearchWasteHis> searchWasteHis) {
                mSearchWasteHis = searchWasteHis;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        mListView.setLayoutManager(new LinearLayoutManager(SearchWasteHisActivity.this));
                        mAdapter = new SearchWasteHisAdapter(SearchWasteHisActivity.this,searchWasteHis);
                        mListView.setAdapter(mAdapter);
                        mListView.setOnItemActionListener(new LeftSwipeMenuRecyclerView.OnItemActionListener() {
                            //点击
                            @Override
                            public void OnItemClick(int position) {
                                Waste waste = mSearchWasteHis.get(position).getmWaste();
                                Intent intent = new Intent(SearchWasteHisActivity.this, SearchWasteDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("waste", waste);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                            //置顶
                            @Override
                            public void OnItemTop(int position) {


                            }
                            //删除
                            @Override
                            public void OnItemDelete(int position) {
                                Waste waste = mSearchWasteHis.get(position).getmWaste();
                                mDialog.setWaste(waste);
                                mDialog.show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });

    }
}
