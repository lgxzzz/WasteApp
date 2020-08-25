package com.mysql.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mysql.app.R;
import com.mysql.app.SearchDetailActivity;
import com.mysql.app.bean.SearchHis;

import java.util.ArrayList;
import java.util.List;

public class SearchHisAdapter extends BaseAdapter {

    Context mContext;
    List<SearchHis> mMsgInfos = new ArrayList<>();

    public SearchHisAdapter(Context mContext, List<SearchHis> mMsgInfos){
        this.mContext = mContext;
        this.mMsgInfos = mMsgInfos;
    }

    public void setData(List<SearchHis> mMsgInfos){
        this.mMsgInfos = mMsgInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMsgInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return mMsgInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final SearchHis msgInfo = mMsgInfos.get(i);
        SearchHisAdapter.ViewHoler holer = null;
        if (view == null){
            holer = new SearchHisAdapter.ViewHoler();
            view = LayoutInflater.from(mContext).inflate(R.layout.search_his_item,null);
            holer.mKeyword = (TextView) view.findViewById(R.id.keyword_tv);
            view.setTag(holer);
        }else{
            holer = (SearchHisAdapter.ViewHoler) view.getTag();
        }
        holer.mKeyword.setText(msgInfo.getSearchKey());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("searchkey",msgInfo.getSearchKey());
                bundle.putBoolean("isInsert",false);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHoler{
        TextView mKeyword;
    }
}
