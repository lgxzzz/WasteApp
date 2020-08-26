package com.mysql.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mysql.app.R;
import com.mysql.app.bean.Evaluation;
import com.mysql.app.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class EvaAdapter extends BaseAdapter {

    Context mContext;
    List<Evaluation> mMsgInfos = new ArrayList<>();

    public EvaAdapter(Context mContext, List<Evaluation> mMsgInfos){
        this.mContext = mContext;
        this.mMsgInfos = mMsgInfos;
    }

    public void setData(List<Evaluation> mMsgInfos){
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
        final Evaluation msgInfo = mMsgInfos.get(i);
        EvaAdapter.ViewHoler holer = null;
        if (view == null){
            holer = new EvaAdapter.ViewHoler();
            view = LayoutInflater.from(mContext).inflate(R.layout.comment_item,null);
            holer.mName = (TextView) view.findViewById(R.id.username_tv);
            holer.mComment = (TextView) view.findViewById(R.id.comment_tv);
            holer.mScore = (TextView) view.findViewById(R.id.score_tv);
            holer.mTime = (TextView) view.findViewById(R.id.comment_time_tv);
            view.setTag(holer);
        }else{
            holer = (EvaAdapter.ViewHoler) view.getTag();
        }
        holer.mName.setText(msgInfo.getmUser().getUserName());
        holer.mComment.setText(msgInfo.getComment());
        holer.mScore.setText(msgInfo.getmScore().getScore());
        holer.mTime.setText(DateUtil.parseTime(msgInfo.getTime()));
        return view;
    }

    class ViewHoler{
        TextView mName;
        TextView mComment;
        TextView mTime;
        TextView mScore;
    }
}
