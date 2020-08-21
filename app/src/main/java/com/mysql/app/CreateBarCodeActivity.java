package com.mysql.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mysql.app.util.BarCodeUtil;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class CreateBarCodeActivity extends Activity {
    private ImageView mBarCode;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_create_barcode);

        mBarCode = findViewById(R.id.barcode_img);

        Bitmap bitmap = BarCodeUtil.createBarcode(this,"12345678911",800,200);
        mBarCode.setImageBitmap(bitmap);
    }

}