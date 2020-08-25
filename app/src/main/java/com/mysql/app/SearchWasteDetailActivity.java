package com.mysql.app;

import android.app.Activity;
import android.content.Intent;
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
import com.mysql.app.view.BottomDialog;
import com.mysql.app.view.TitleView;

import java.util.List;


/***
 * 垃圾详细信息
 *
 * */
public class SearchWasteDetailActivity extends Activity{

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
    private BottomDialog mBottomDialog;
    private Handler mHandler = new Handler();
    public static Waste mWaste;
    public User mUser;
    public RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_waste_detail);
        mUser= DBManger.getInstance(this).mUser;
        mWaste = (Waste) getIntent().getExtras().getSerializable("waste");
        init();
        initData();
    }

    public void init(){
        mBottomDialog = new BottomDialog(this);
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
        mRatingBar = findViewById(R.id.ratingBar);

        mTitleView.setTitle("Waste Information");
        //根据当前用户角色显示UI
        if (mUser == null){
            //游客
            mTitleView.setOtherBtnVisible(false);
            mCommentEd.setFocusable(false);
            mCommentEd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SearchWasteDetailActivity.this, LoginActivity.class));
                }
            });
            mSendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SearchWasteDetailActivity.this, LoginActivity.class));
                }
            });
        }else if(mUser.getRole().equals("User")){
            //用户
            mTitleView.setOtherBtnVisible(false);
        }else if(mUser.getRole().equals("Administrator")){
            //管理员
            mTitleView.setOtherBtn("Delete", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomDialog.show();
                }
            });
        }
        mTitleView.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNameTv.setText(mWaste.getName());
        mTypeTv.setText(mWaste.getType());
        mDescriptionTv.setText(mWaste.getDescription());
        mScoreTv.setText(mWaste.getScore());
        mBarCodeTv.setText(mWaste.getBarCode());

        mBottomDialog.setWaste(mWaste);
        mBottomDialog.setListener(new BottomDialog.IBottomDialogListener() {
            @Override
            public void onDelete() {
                SearchWasteDetailActivity.this.finish();
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
        List<Evaluation> evaluations = DBManger.getInstance(this).queryEvaluations(mUser);
        if (evaluations!=null){
            mAdapter = new EvaAdapter(this,evaluations);
            mEvaListView.setAdapter(mAdapter);

            Float allScore = 0.0f;
            for (int i=0;i<evaluations.size();i++){
                String score = evaluations.get(i).getEva_score();
                Float sc = Float.parseFloat(score);
                allScore= allScore+sc;
            }
            if (allScore!=0.0f)
            {
                //计算评价得分
                double sroce = allScore/evaluations.size();
                java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0");
                String str = myformat.format(sroce);
                mScoreTv.setText(str);
            }
        }

    }

}
