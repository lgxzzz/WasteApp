package com.mysql.app.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mysql.app.LoginActivity;
import com.mysql.app.R;
import com.mysql.app.SearchHisActivity;


/***
 * 搜索界面
 *
 * */
public class SearchFragment extends Fragment {

    private EditText mSearchEd;

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
        mSearchEd = view.findViewById(R.id.search_ed);
        mSearchEd.setFocusable(false);
        mSearchEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchHisActivity.class));
            }
        });
    };

    public void initData() {


    }




}
