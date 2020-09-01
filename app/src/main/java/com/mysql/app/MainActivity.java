package com.mysql.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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


    }

    public void init(){

        mBottomMenu = findViewById(R.id.person_bottom_menu);

        mBottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showFragment(item.getItemId());
                return true;
            }
        });


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

    @Override
    protected void onResume() {
        super.onResume();
        mUser = DBManger.getInstance(this).mUser;
        if (mUser!=null){
            showFragment(R.id.bottom_menu_about);
        }else{
            showFragment(R.id.bottom_menu_scan);
        }

    }
}
