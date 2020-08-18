package com.mysql.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.mysql.app.view.TitleView;


/***
 * 垃圾详细信息
 *
 * */
public class WasteInformationActivity extends Activity{

    private TitleView mTitleView;
    private ListView mEvaListView;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waste_info);
        init();
    }

    public void init(){
        mTitleView = findViewById(R.id.title_view);
        mTitleView.setTitle("Waste Information");
        mTitleView.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleView.setOtherBtn("regulate", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void initData(){
        List<Evaluation> evaluations = DBManager.getInstance(this).queryEvaluations(null,
                "tch_name =? and course_name =?",new String[]{mTchName,mCourseName},null,null,"id desc");
        if (evaluations==null){
            return;
        }
        mAdapter = new EvaAdapter(this,evaluations);
        mEvaListView.setAdapter(mAdapter);

        Float allScore = 0.0f;
        for (int i=0;i<evaluations.size();i++){
            String score = evaluations.get(i).getEva_score();
            if (score.equals("4.5")||score.equals("5.0"))
            {
                mEvaScores[4]=++mEvaScores[4];
            }else if(score.equals("3.5")||score.equals("4.0"))
            {
                mEvaScores[3]=++mEvaScores[3];
            }else if(score.equals("2.5")||score.equals("3.0"))
            {
                mEvaScores[2]=++mEvaScores[2];
            }else if(score.equals("1.5")||score.equals("2.0"))
            {
                mEvaScores[1]=++mEvaScores[1];
            }else if(score.equals("0.0")||score.equals("1.0"))
            {
                mEvaScores[0]=++mEvaScores[0];
            }
            Float sc = Float.parseFloat(score);
            allScore= allScore+sc;
        }
        if (allScore!=0.0f)
        {
            double sroce = allScore/evaluations.size();
            java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(sroce);

            mEvaScore.setText(str);
        }
    }
}
