package com.mysql.app.fragement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysql.app.R;


/***
 * 扫描界面
 *
 * */
public class ScanFragment extends Fragment {

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
        initData();
    }

    public void initView(View view){

    };

    public void initData() {


    }




}
