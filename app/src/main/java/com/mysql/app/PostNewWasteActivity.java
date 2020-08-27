package com.mysql.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;
import com.mysql.app.view.TitleView;

import java.util.ArrayList;
import java.util.List;

/***
 * 提交新垃圾信息
 *
 * */
public class PostNewWasteActivity extends AppCompatActivity {

    private EditText mNameEd;
    private EditText mDescriptionEd;
    private EditText mBarCodeEd;
    private Button mScanBtn;
    private Button mPostBtn;
    private User mUser;
    private Spinner mTypeSp;
    private String mSelectType="";
    private Waste mWaste;

    private TitleView mTitleView;

    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_waste);

        init();
    }

    public void init(){
        mWaste = new Waste();

        mUser = DBManger.getInstance(this).mUser;

        mNameEd = findViewById(R.id.waste_name_ed);
        mTypeSp = findViewById(R.id.waste_type_ed);
        mPostBtn = findViewById(R.id.post_new_waste_btn);
        mBarCodeEd = findViewById(R.id.waste_barcode_ed);
        mDescriptionEd = findViewById(R.id.waste_description_ed);
        mScanBtn = findViewById(R.id.scan_btn);

        mTitleView = findViewById(R.id.title_view);
        mTitleView.setTitle("Post New Waste");
        mTitleView.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final List<String> mTypes =new ArrayList<>();
        mTypes.add("");
        mTypes.add("Hazardous Waste");
        mTypes.add("Recyclable Waste");
        mTypes.add("Household Food Waste");
        mTypes.add("Residual Waste");

        SpinnerAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mTypes);
        mTypeSp.setAdapter(adapter);
        mSelectType = mTypes.get(0);

        mTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectType = mTypes.get(position);
                mWaste.setType(mSelectType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //输入垃圾名称
        mNameEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mWaste.setName(editable.toString());
            }
        });
        //输入描述
        mBarCodeEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mWaste.setBarCode(editable.toString());
            }
        });
        //输入描述
        mDescriptionEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mWaste.setDescription(editable.toString());
            }
        });

        mScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PostNewWasteActivity.this, ScanActivity.class), 1);
            }
        });

        //点击添加新的垃圾种类
        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWaste.getName()==null){
                    Toast.makeText(PostNewWasteActivity.this,"Waste name is a required field and cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mWaste.getName()!=null&& mWaste.getName().length()>8){
                    Toast.makeText(PostNewWasteActivity.this,"Waste name input should not exceed 8 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mWaste.getType()==null || mWaste.getType().length() == 0){
                    Toast.makeText(PostNewWasteActivity.this,"Waste type is Must be chosen", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mWaste.getBarCode()==null){
                    Toast.makeText(PostNewWasteActivity.this,"Waste barcode is a required field and cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mWaste.getDescription()!=null&& mWaste.getDescription().length()>100){
                    Toast.makeText(PostNewWasteActivity.this,"Waste disposal description input should not exceed 100 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mWaste.getBarCode()!=null&& mWaste.getBarCode().length()>13){
                    Toast.makeText(PostNewWasteActivity.this,"Waste barcode identification code input should not exceed 13 integers", Toast.LENGTH_LONG).show();
                    return;
                }
                mWaste.setType(mSelectType);
                DBManger.getInstance(PostNewWasteActivity.this).insertWaste(mWaste, new DBManger.IListener() {
                    @Override
                    public void onSuccess() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PostNewWasteActivity.this,"Successful post", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PostNewWasteActivity.this,error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            String result = data.getExtras().getString("barcode");//得到新Activity 关闭后返回的数据
            if (result.length()!=0)
                mBarCodeEd.setText(result);
        }
    }
}
