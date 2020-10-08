package com.mysql.app.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.mysql.app.R;
import com.mysql.app.WasteEditActivity;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;

public class AdminDialog extends Dialog {
    public Waste mWaste;
    public View mOperateView;
    public View mConfirmView;
    public Button mCancelBtn;
    public Button mDeteleBtn;
    public Button mConfirmBtn;
    public Button mCancelBtn1;
    public AdminDialog(Context context) {
        super(context,R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.admin_dialog, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity( Gravity.CENTER);
        mCancelBtn = findViewById(R.id.cancel_btn);
        mDeteleBtn = findViewById(R.id.delete_btn);
        mOperateView = findViewById(R.id.operate_layout);
        mConfirmView = findViewById(R.id.confirm_layout);
        mConfirmBtn = findViewById(R.id.confirm_btn);
        mCancelBtn1 = findViewById(R.id.cancel1_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mDeteleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOperateView.setVisibility(View.GONE);
                mConfirmView.setVisibility(View.VISIBLE);

            }
        });

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManger.getInstance(getContext()).deleteWaset(mWaste, new DBManger.IListener() {
                    @Override
                    public void onSuccess() {
                        if (listener!=null){
                            listener.onDelete();
                        }
                        dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        if (listener!=null){
                            listener.onError(error);
                        }
                        dismiss();
                    }
                });
            }
        });

        mCancelBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public AdminDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AdminDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);


    }

    public void setWaste(Waste mWaste){
        this.mWaste = mWaste;
    }


    IAdminDialogListener listener;
    public interface IAdminDialogListener{
        public void onDelete();
        public void onError(String error);
    }


    public void setListener(IAdminDialogListener listener) {
        this.listener = listener;
    }
}
