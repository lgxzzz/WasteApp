package com.mysql.app.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.mysql.app.R;
import com.mysql.app.WasteEditActivity;
import com.mysql.app.bean.Waste;

public class BottomDialog extends Dialog {
    public Waste mWaste;
    public Button mEditBtn;
    public Button mCancelBtn;
    public Button mDeteleBtn;
    public BottomDialog(Context context) {
        super(context,R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_dialog, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity( Gravity.BOTTOM);
        mEditBtn = findViewById(R.id.edit_btn);
        mCancelBtn = findViewById(R.id.cancel_btn);
        mDeteleBtn = findViewById(R.id.delete_btn);

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(),WasteEditActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("waste",mWaste);
                intent.putExtras(b);
                getContext().startActivity(intent);
                dismiss();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mDeteleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BottomDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);


    }

    public void setWaste(Waste mWaste){
        this.mWaste = mWaste;
    }
}
