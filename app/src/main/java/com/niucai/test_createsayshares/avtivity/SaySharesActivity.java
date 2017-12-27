package com.niucai.test_createsayshares.avtivity;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.niucai.recording.RecordingManager;
import com.niucai.test_createsayshares.R;
import com.niucai.test_createsayshares.utils.DateUtils;
import com.niucai.test_createsayshares.view.CircleProgressBarView;
import com.niucai.test_createsayshares.view.HollowCircle;
import com.niucai.test_createsayshares.view.SolidCircle;

public class SaySharesActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SaySharesActivity";
    private TextView tv_create_title;
    private ImageView iv_SayShares_Back;
    private TextView tv_save;
    private FrameLayout fl_create_head;
    private TextView tv_ZhangJieTitle;
    private TextView tv_XiaoJieTitle;
    private TextView tv_Recording;
    private HollowCircle mHollowCircle;
    private SolidCircle mSolidCircle;
    private TextView tv_start;
    private CircleProgressBarView circle_progress_view;
    private Button but_Paly;
    private Button but_Reset;
    private TextView tv_Reset;
    private TextView tv_Paly;
    private Button but_AddTimeTag;
    private TextView tv_AddTimeTag;

    private volatile boolean isStart;
    private volatile boolean isPlay;
    public volatile boolean isStop;
    private long startRecordTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_shares);

        initView();
        initListener();
    }

    private void initListener() {

    }


    private void initView() {
        tv_create_title = (TextView) findViewById(R.id.tv_create_title);
        iv_SayShares_Back = (ImageView) findViewById(R.id.iv_SayShares_Back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        fl_create_head = (FrameLayout) findViewById(R.id.fl_create_head);
        tv_ZhangJieTitle = (TextView) findViewById(R.id.tv_ZhangJieTitle);
        tv_XiaoJieTitle = (TextView) findViewById(R.id.tv_XiaoJieTitle);
        tv_Recording = (TextView) findViewById(R.id.tv_Recording);
        mHollowCircle = (HollowCircle) findViewById(R.id.mHollowCircle);
        mSolidCircle = (SolidCircle) findViewById(R.id.mSolidCircle);
        tv_start = (TextView) findViewById(R.id.tv_start);
        circle_progress_view = (CircleProgressBarView) findViewById(R.id.circle_progress_view);
        but_Paly = (Button) findViewById(R.id.but_Paly);
        but_Reset = (Button) findViewById(R.id.but_Reset);
        tv_Reset = (TextView) findViewById(R.id.tv_Reset);
        tv_Paly = (TextView) findViewById(R.id.tv_Paly);
        but_AddTimeTag = (Button) findViewById(R.id.but_AddTimeTag);
        tv_AddTimeTag = (TextView) findViewById(R.id.tv_AddTimeTag);

        but_Paly.setOnClickListener(this);
        but_Reset.setOnClickListener(this);
        but_AddTimeTag.setOnClickListener(this);
        iv_SayShares_Back.setOnClickListener(this);

        tv_start.setOnClickListener(this);
        mSolidCircle.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回键
            case R.id.iv_SayShares_Back:
                finish();
                break;
            //播放
            case R.id.but_Paly:
//                circle_progress_view.startProgressAnimation();
                RecordingManager.getInstance().playRecording();

                break;
            //重置
            case R.id.but_Reset:

                break;
            //添加时间标记
            case R.id.but_AddTimeTag:

                break;

            case R.id.tv_start:
                mHollowCircle.setVisibility(View.GONE);
                tv_start.setVisibility(View.GONE);

                circle_progress_view.setVisibility(View.VISIBLE);
                mSolidCircle.setVisibility(View.VISIBLE);
                isStart = true;
                Log.e(TAG, "onClick:准备完毕");

                break;
            //开始录音
            case R.id.mSolidCircle:

                if (isStart) {
                    //申请录制音频的动态权限
                    if (ContextCompat.checkSelfPermission(SaySharesActivity.this, android.Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SaySharesActivity.this, new String[]{
                                android.Manifest.permission.RECORD_AUDIO}, 1);

                    } else {
                        startRecord();
                    }

                } else if (isPlay && (!isStart)) {

                    if (!isStop) {
                        RecordingManager.getInstance().stopRecord();
                        circle_progress_view.pauseProgressAnimation();
                        Log.e(TAG, "onClick:已经暂停");
                        isStop = true;

                    } else if (isStop) {
                        circle_progress_view.resumeProgressAnimation();

                        Log.e(TAG, "onClick:继续动画");
                        isStop = false;
                    }

                }

                break;
        }
    }

    private void startRecord() {
        RecordingManager.getInstance().startRecord();
        Log.e(TAG, "onClick:正在录制");
        startRecordTime = RecordingManager.getInstance().getmStartRecordTime();

        String startRecordTimeString = DateUtils.times(startRecordTime);

        isPlay = true;
        circle_progress_view.setProgressWithAnimation(100);
        circle_progress_view.startProgressAnimation();
        isStart = false;
    }

    /**
     * ⑨重写onRequestPermissionsResult方法
     * 获取动态权限请求的结果,再开启录制音频
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startRecord();
        }else {
            Toast.makeText(this,"用户拒绝了权限",Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
