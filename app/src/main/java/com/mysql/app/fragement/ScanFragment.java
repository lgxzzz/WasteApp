package com.mysql.app.fragement;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mysql.app.R;
import com.mysql.app.ScanActivity;
import com.mysql.app.SearchResultActivity;
import com.mysql.app.util.QRCodeParseUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.VIBRATOR_SERVICE;


/***
 * 扫描界面
 *
 * */
public class ScanFragment extends Fragment {

    private QRCodeView mQRCodeView;
    private Button mLightBtn;
    private Button mPhotoBtn;
    private boolean isOpenFlash = false;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragement_scan, container, false);
        initView(view);

        return view;
    }

    public static ScanFragment getInstance() {
        return new ScanFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mQRCodeView.startSpot();
            }
        }, 2000);
    }

    public void initView(View view){
        mQRCodeView = (ZXingView) view.findViewById(R.id.zxingview);
        mLightBtn = (Button) view.findViewById(R.id.open_flashlight);
        mPhotoBtn = (Button) view.findViewById(R.id.open_photo);
        mQRCodeView.changeToScanBarcodeStyle(); //扫二维码
        mQRCodeView.setDelegate(new QRCodeView.Delegate() {

            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.d("二维码扫描结果", "result:" + result);
                //扫描得到结果震动一下表示
                vibrate();

                Intent intent = new Intent(getContext(), SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("searchkey",result);
                bundle.putBoolean("isInsert",false);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                Toast.makeText(getContext(), "open camera fail！", Toast.LENGTH_SHORT).show();
            }
        });

        mLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpenFlash){
                    mQRCodeView.openFlashlight();
                    isOpenFlash  = true;
                }else{
                    isOpenFlash = false;
                    mQRCodeView.closeFlashlight();
                }
            }
        });

        mPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

//        Intent intent = new Intent(getContext(), SearchResultActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("searchkey","22131232");
//        bundle.putBoolean("isInsert",false);
//        intent.putExtras(bundle);
//        getActivity().startActivity(intent);
    };

    @Override
    public void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        //强制手机摄像头镜头朝向前边
        //mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect(); //显示扫描方框

    }

    @Override
    public void onStop() {
        mQRCodeView.stopCamera();
        mQRCodeView.closeFlashlight();
        isOpenFlash = false;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mQRCodeView.onDestroy();
        mQRCodeView.closeFlashlight();
        isOpenFlash = false;
        super.onDestroy();
    }

    //震动
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: //从相册图片后返回的uri
                    //启动裁剪
                    Uri uri= data.getData();
                    parsePhoto(uri.getPath());
                    break;
            }
        }
    }


    /**
     * 启动线程解析二维码图片
     *
     * @param path
     */
    private void parsePhoto(String path) {
        //启动线程完成图片扫码
        new QrCodeAsyncTask(getActivity(), path).execute(path);
    }

    /**
     * AsyncTask 静态内部类，防止内存泄漏
     */
    class QrCodeAsyncTask extends AsyncTask<String, Integer, String> {
        private WeakReference<Activity> mWeakReference;
        private String path;

        public QrCodeAsyncTask(Activity activity, String path) {
            mWeakReference = new WeakReference<>(activity);
            this.path = path;
        }

        @Override
        protected String doInBackground(String... strings) {
            // 解析二维码/条码
            return QRCodeParseUtils.syncDecodeQRCode(path);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //识别出图片二维码/条码，内容为s
            if (null == s) {
                Toast.makeText(getContext(), "No barcode found!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getContext(), SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("searchkey",s);
                bundle.putBoolean("isInsert",false);
                intent.putExtras(bundle);
                ScanFragment.this.getActivity().startActivity(intent);
            }
        }
    }

    /**
     * 处理图片二维码解析的数据
     *
     * @param s
     */
    public void handleQrCode(String s) {

    }


}
