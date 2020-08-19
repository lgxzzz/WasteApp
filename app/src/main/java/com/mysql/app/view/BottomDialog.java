package com.mysql.app.view;

import android.app.Dialog;
import android.content.Context;

public class BottomDialog extends Dialog {
    public BottomDialog(Context context) {
        super(context);
        dialog = new Dialog(oThis,R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(oThis).inflate(R.layout.fast_trade_view, null);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity( Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BottomDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);


    }
}
