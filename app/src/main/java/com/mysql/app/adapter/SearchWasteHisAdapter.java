package com.mysql.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysql.app.R;
import com.mysql.app.bean.SearchWasteHis;
import com.mysql.app.bean.Waste;
import com.mysql.app.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class SearchWasteHisAdapter extends RecyclerView.Adapter<SearchWasteHisAdapter.Holder> {

    List<SearchWasteHis> mMsgInfos = new ArrayList<>();
    public Context mContext;
    public SearchWasteHisAdapter(Context mContext, List<SearchWasteHis> mMsgInfos){
        this.mMsgInfos = mMsgInfos;
        this.mContext = mContext;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final SearchWasteHis msgInfo = mMsgInfos.get(position);

        Waste waste = msgInfo.getmWaste();
        holder.mName.setText(waste.getName());
        holder.mType.setText(waste.getType());
        holder.mScore.setText(waste.getScore());
        holder.mTime.setText(DateUtil.parseTime(msgInfo.getTime()));
    }


    @Override
    public int getItemCount() {
        return mMsgInfos.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        public LinearLayout llLayout;
        public TextView mName;
        public TextView mType;
        public TextView mScore;
        public TextView mTime;
        public TextView tvDelete;
        public TextView tvTop;

        public Holder(View itemView) {
            super(itemView);

            llLayout= (LinearLayout) itemView.findViewById(R.id.llLayout);
            tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            tvTop = (TextView) itemView.findViewById(R.id.tvTop);
            mName = (TextView) itemView.findViewById(R.id.waste_name_tv);
            mType = (TextView) itemView.findViewById(R.id.waste_type_tv);
            mScore = (TextView) itemView.findViewById(R.id.score_tv);
            mTime = (TextView) itemView.findViewById(R.id.waste_time_tv);
        }

    }

}