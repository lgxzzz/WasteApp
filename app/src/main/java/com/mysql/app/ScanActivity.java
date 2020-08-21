package com.mysql.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends Activity {
    private QRCodeView mQRCodeView;
    private Activity activity;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        activity = this;
        setContentView(R.layout.activity_scan);

        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.changeToScanBarcodeStyle(); //扫二维码
        mQRCodeView.setDelegate(new QRCodeView.Delegate() {

            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.d("二维码扫描结果", "result:" + result);

                Toast.makeText(activity, result, Toast.LENGTH_LONG).show();

                //扫描得到结果震动一下表示
                vibrate();

                //获取结果后三秒后，重新开始扫描
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mQRCodeView.startSpot();
                    }
                }, 3000);
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                Toast.makeText(activity, "打开相机错误！", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.start_spot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.startSpot();
                Toast.makeText(activity, "startSpot", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.stop_spot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.stopSpot();
                Toast.makeText(activity, "stopSpot", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.open_flashlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.openFlashlight();
                Toast.makeText(activity, "openFlashlight", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.close_flashlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.closeFlashlight();
                Toast.makeText(activity, "closeFlashlight", Toast.LENGTH_SHORT).show();
            }
        });

//        findViewById(R.id.scan_qrcode).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                mQRCodeView.changeToScanQRCodeStyle();
//                Toast.makeText(activity,"changeToScanQRCodeStyle",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        //强制手机摄像头镜头朝向前边
        //mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect(); //显示扫描方框
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    //震动
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}