package com.mysql.app.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.mysql.app.R;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;

public class SearchWasteHisDialog extends Dialog {
    public Waste mWaste;
    public Button mConfirmBtn;
    public Button mCancelBtn1;
    public SearchWasteHisDialog(Context context) {
        super(context,R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.confirm_dialog, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity( Gravity.CENTER);
        mConfirmBtn = findViewById(R.id.confirm_btn);
        mCancelBtn1 = findViewById(R.id.cancel1_btn);

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManger.getInstance(getContext()).deleteSearcWasteHis(mWaste, new DBManger.IListener() {
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

    public SearchWasteHisDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SearchWasteHisDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);


    }

    public void setWaste(Waste mWaste){
        this.mWaste = mWaste;
    }


    ISearchWasteHisDialogListener listener;
    public interface ISearchWasteHisDialogListener{
        public void onDelete();
        public void onError(String error);
    }


    public void setListener(ISearchWasteHisDialogListener listener) {
        this.listener = listener;
    }
}
