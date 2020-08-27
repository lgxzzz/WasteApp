package com.mysql.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends Activity {
    private QRCodeView mQRCodeView;
    private Button mCancelBtn;
    private Activity activity;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        activity = this;
        requestPermissions();
        setContentView(R.layout.activity_scan);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }



        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mCancelBtn = (Button) findViewById(R.id.cancel_btn);
        mQRCodeView.changeToScanBarcodeStyle(); //扫二维码
        mQRCodeView.setDelegate(new QRCodeView.Delegate() {

            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.d("二维码扫描结果", "result:" + result);

                Toast.makeText(activity, result, Toast.LENGTH_LONG).show();

                //扫描得到结果震动一下表示
                vibrate();

                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("barcode", result);
                //设置返回数据
                ScanActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ScanActivity.this.finish();

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
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("barcode", "");
                //设置返回数据
                ScanActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ScanActivity.this.finish();
            }
        });
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
    protected void onResume() {
        super.onResume();
        //获取结果后三秒后，重新开始扫描
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mQRCodeView.startSpot();
            }
        }, 2000);
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

    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                            },0x0010);
                }

                if(permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}