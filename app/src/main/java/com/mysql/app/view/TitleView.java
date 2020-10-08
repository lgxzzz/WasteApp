package com.mysql.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mysql.app.R;


public class TitleView extends RelativeLayout {
    private Button mBackBtn;
    private Button mOtherBtn;
    private TextView mTitleView;

    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.title_view,this,true);
        mBackBtn = findViewById(R.id.title_back_btn);
        mTitleView = findViewById(R.id.title_content);
        mOtherBtn = findViewById(R.id.other_btn);
    }

    public void setBackBtnVisible(boolean visible){
        mBackBtn.setVisibility(visible? View.VISIBLE: View.GONE);
    }

    public void setTitle(String title){
        mTitleView.setText(title);
    }

    public void setOnBackListener(OnClickListener listener){
        mBackBtn.setOnClickListener(listener);
    }

    public void setOtherBtnVisible(boolean isVisible){
        mOtherBtn.setVisibility(isVisible?VISIBLE:GONE);
    }

    public void setOtherBtn(String text,OnClickListener listener){
        mOtherBtn.setVisibility(VISIBLE);
        mOtherBtn.setText(text);
        mOtherBtn.setOnClickListener(listener);
    }
}
