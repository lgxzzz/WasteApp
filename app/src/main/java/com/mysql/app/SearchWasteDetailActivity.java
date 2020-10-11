package com.mysql.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mysql.app.adapter.EvaAdapter;
import com.mysql.app.bean.Evaluation;
import com.mysql.app.bean.Score;
import com.mysql.app.bean.User;
import com.mysql.app.bean.Waste;
import com.mysql.app.data.DBManger;
import com.mysql.app.view.AdminDialog;
import com.mysql.app.view.UserDialog;
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
    private AdminDialog mAdminDialog;
    private Handler mHandler = new Handler();
    public static Waste mWaste;
    public User mUser;
    public RatingBar mRatingBar;
    public String mCurrentScore = "0";

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
        mAdminDialog = new AdminDialog(this);
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
            //判断是否该垃圾的发布的用户,是则不能评价
            if(mUser.getUserId().equals(mWaste.getUserId())){
                mCommentEd.setFocusable(false);

            }else{
                mSendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = mCommentEd.getText().toString();
                        if (comment.length()!=0){
                            sendComment(comment);
                        }
                    }
                });
            }
        }else if(mUser.getRole().equals("Administrator")){
            //管理员
            mTitleView.setOtherBtn("Delete", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdminDialog.show();
                }
            });

            mAdminDialog.setWaste(mWaste);
            mAdminDialog.setListener(new AdminDialog.IAdminDialogListener() {
                @Override
                public void onDelete() {
                    SearchWasteDetailActivity.this.finish();
                }

                @Override
                public void onError(String error) {

                }
            });

            mSendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = mCommentEd.getText().toString();
                    if (comment.length()!=0){
                        sendComment(comment);
                    }
                }
            });

        }

        mTitleView.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    updateRateValue(rating+"");
            }
        });

        mNameTv.setText(mWaste.getName());
        mTypeTv.setText(mWaste.getType());
        mDescriptionTv.setText(mWaste.getDescription());
        mScoreTv.setText(mWaste.getScore());
        mBarCodeTv.setText(mWaste.getBarCode());
        mUserTv.setText(mWaste.getmUser().getEmail());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initData(){
        getWasteRate();
        getEvaluations();
    }

    //添加评论消息
    public void sendComment(String comment){

        Evaluation evaluation = new Evaluation();
        evaluation.setComment(comment);
        evaluation.setUserId(mUser.getUserId());
        evaluation.setWasteId(mWaste.getId());

        DBManger.getInstance(SearchWasteDetailActivity.this).insertEvaluation(evaluation, new DBManger.IListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    //获取评论列表
    public void getEvaluations(){
        DBManger.getInstance(this).queryEvaluations(mWaste, new DBManger.IEvaListener() {
            @Override
            public void onSuccess(List<Evaluation> evaluations) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (evaluations!=null){
                            mAdapter = new EvaAdapter(SearchWasteDetailActivity.this,evaluations);
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

    //更新评分
    public void updateRateValue(String value){
        DBManger.getInstance(this).updateScore(mUser, mWaste, value, new DBManger.IListener() {
            @Override
            public void onSuccess() {
                initData();

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    //获取该用户当前的该垃圾的评分
    public void getWasteRate(){
        DBManger.getInstance(this).queryWasteScores(mWaste, new DBManger.IScoreListener() {

            @Override
            public void onSuccess(List<Score> scores) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (scores.size()!=0){
                            Float allScore = 0.0f;
                            for (int i=0;i<scores.size();i++){
                                Score tempScore = scores.get(i);
                                String score = scores.get(i).getScore();
                                Float sc = Float.parseFloat(score);
                                allScore= allScore+sc;
                                if (mWaste.getUserId().equals(tempScore.getUserId())){
                                    mRatingBar.setRating(sc);
                                }
                            }
                            if (allScore!=0.0f)
                            {
                                //计算评价得分
                                double sroce = allScore/scores.size();
                                java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0");
                                mCurrentScore = myformat.format(sroce);
                                mScoreTv.setText(mCurrentScore);
                                DBManger.getInstance(SearchWasteDetailActivity.this).updateWasteScore(mWaste,mCurrentScore);
                            }
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
