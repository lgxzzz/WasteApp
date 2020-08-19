package com.mysql.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysql.app.R;
import com.mysql.app.WasteInformationActivity;
import com.mysql.app.bean.Waste;
import com.mysql.app.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class WasteAdapter extends BaseAdapter {

    Context mContext;
    List<Waste> mMsgInfos = new ArrayList<>();

    public WasteAdapter(Context mContext, List<Waste> mMsgInfos){
        this.mContext = mContext;
        this.mMsgInfos = mMsgInfos;
    }

    public void setData(List<Waste> mMsgInfos){
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
        final Waste msgInfo = mMsgInfos.get(i);
        WasteAdapter.ViewHoler holer = null;
        if (view == null){
            holer = new WasteAdapter.ViewHoler();
            view = LayoutInflater.from(mContext).inflate(R.layout.posted_record_item,null);
            holer.mName = (TextView) view.findViewById(R.id.waste_name_tv);
            holer.mType = (TextView) view.findViewById(R.id.waste_name_tv);
            holer.mScore = (TextView) view.findViewById(R.id.waste_name_tv);
            holer.mTime = (TextView) view.findViewById(R.id.waste_name_tv);
            view.setTag(holer);
        }else{
            holer = (WasteAdapter.ViewHoler) view.getTag();
        }
        holer.mName.setText(msgInfo.getName());
        holer.mType.setText(msgInfo.getType());
        holer.mScore.setText(msgInfo.getScore());
        holer.mTime.setText(DateUtil.parseTime(msgInfo.getTime()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WasteInformationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", msgInfo);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHoler{
        TextView mName;
        TextView mType;
        TextView mTime;
        TextView mScore;
    }
}