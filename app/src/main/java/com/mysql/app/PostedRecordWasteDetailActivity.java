package com.mysql.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mysql.app.adapter.EvaAdapter;
import com.mysql.app.bean.Evaluation;
import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;
import com.mysql.app.view.UserDialog;
import com.mysql.app.view.TitleView;

import java.util.List;


/***
 * 垃圾详细信息
 *
 * */
public class PostedRecordWasteDetailActivity extends Activity{

    private TitleView mTitleView;
    private ListView mEvaListView;
    private EvaAdapter mAdapter;
    private TextView mNameTv;
    private TextView mTypeTv;
    private TextView mDescriptionTv;
    private TextView mBarCodeTv;
    private TextView mScoreTv;
    private TextView mUserTv;
    private EditText mCommentEd;
    private Button mSendBtn;
    private UserDialog mUserDialog;
    private Handler mHandler = new Handler();
    public static Waste mWaste;
    public User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_waste_info);
        mUser= DBManger.getInstance(this).mUser;
        mWaste = (Waste) getIntent().getExtras().getSerializable("waste");
        init();
        initData();
    }

    public void init(){
        mUserDialog = new UserDialog(this);
        mTitleView = findViewById(R.id.title_view);
        mEvaListView = findViewById(R.id.comments_listview);
        mNameTv = findViewById(R.id.waste_name_tv);
        mTypeTv = findViewById(R.id.waste_type_tv);
        mDescriptionTv = findViewById(R.id.description_tv);
        mScoreTv = findViewById(R.id.score_tv);
        mBarCodeTv = findViewById(R.id.waste_barcode_tv);
        mUserTv = findViewById(R.id.user_tv);
        mCommentEd = findViewById(R.id.send_comments_ed);
        mSendBtn = findViewById(R.id.send_comments_btn);

        mTitleView.setTitle("Posted Waste Information");
        mTitleView.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleView.setOtherBtn("Manage", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserDialog.show();
            }
        });

        mNameTv.setText(mWaste.getName());
        mTypeTv.setText(mWaste.getType());
        mDescriptionTv.setText(mWaste.getDescription());
        mScoreTv.setText(mWaste.getScore());
        mBarCodeTv.setText(mWaste.getBarCode());

        mUserDialog.setWaste(mWaste);
        mUserDialog.setListener(new UserDialog.IBottomDialogListener() {
            @Override
            public void onDelete() {
                PostedRecordWasteDetailActivity.this.finish();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initData(){
        DBManger.getInstance(this).queryEvaluations(mWaste, new DBManger.IEvaListener() {
            @Override
            public void onSuccess(List<Evaluation> evaluations) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (evaluations!=null){
                            mAdapter = new EvaAdapter(PostedRecordWasteDetailActivity.this,evaluations);
                            mEvaListView.setAdapter(mAdapter);

                        }
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });


    }

}
