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
 * 搜索界面
 *
 * */
public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragement_search, container, false);
        initView(view);

        return view;
    }

    public static SearchFragment getInstance() {
        return new SearchFragment();
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
