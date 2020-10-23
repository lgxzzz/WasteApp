package com.mysql.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;

import com.mysql.app.bean.User;
import com.mysql.app.data.DBManger;
import com.mysql.app.fragement.AboutFragment;
import com.mysql.app.fragement.ScanFragment;
import com.mysql.app.fragement.SearchFragment;
import com.mysql.app.util.FragmentUtils;


/***
 * 主页activity
 *
 * */
public class MainActivity extends BaseActivtiy {

    private BottomNavigationView mBottomMenu;
    User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        requestPermissions();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

    }

    public void init(){
        mUser = DBManger.getInstance(this).mUser;
        mBottomMenu = findViewById(R.id.person_bottom_menu);

        mBottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showFragment(item.getItemId());
                return true;
            }
        });
        if (mUser!=null){
            mBottomMenu.setSelectedItemId(R.id.bottom_menu_about);
            showFragment(R.id.bottom_menu_about);
        }else{
            mBottomMenu.setSelectedItemId(R.id.bottom_menu_scan);
            showFragment(R.id.bottom_menu_scan);
//            mBottomMenu.setSelectedItemId(R.id.bottom_menu_search);
//            showFragment(R.id.bottom_menu_search);
        }


    }


    /**
     * 根据id显示相应的页面
     * @param menu_id
     */
    private void showFragment(int menu_id) {
        switch (menu_id){
            case R.id.bottom_menu_scan:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, ScanFragment.getInstance(),R.id.main_frame);
                break;
            case R.id.bottom_menu_about:
                if (mUser!=null){
                    FragmentUtils.replaceFragmentToActivity(fragmentManager, AboutFragment.getInstance(),R.id.main_frame);
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                break;
            case R.id.bottom_menu_search:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, SearchFragment.getInstance(),R.id.main_frame);
                break;
        }
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
