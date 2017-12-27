package com.niucai.recording;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Qi on 2017/12/26.
 */

public class RecordingManager {
    private static RecordingManager mRecordingManager;

    private static final String TAG = "RecordingManager";
    private ExecutorService mExecutorService;
    private MediaRecorder mMediaRecorder;
    private File mAudioFile;

    private Handler mMainThreadHandler;
    //主线程和后台播放线程同步
    private volatile boolean mIsPlaying;
    private long mStartRecordTime, mStopRecordTime;
    private MediaPlayer mMediaPlayer;


    private RecordingManager() {
        //创建单线程 录音 JNI函数不具备线程安全性，所以要用单线程
        mExecutorService = Executors.newSingleThreadExecutor();
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public static RecordingManager getInstance() {
        if (mRecordingManager == null) {
            synchronized (RecordingManager.class) {
                if (mRecordingManager == null) {
                    mRecordingManager = new RecordingManager();
                }
            }
        }
        return mRecordingManager;
    }


    /**
     * 播放的逻辑
     */
    public void playRecording() {
        //检查当前状态，防止重复播放
        if (mAudioFile != null && !mIsPlaying) {
            //设置当前播放状态
            mIsPlaying = true;

            //提交后台任务，开始播放
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    doPlay(mAudioFile);
                }
            });
        }
    }


    /**
     * 开始录音
     */
    public void startRecord() {

        //提交后台任务，执行录音逻辑
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //释放之前录音的 MediaRecorder
                releaseRecorder();

                //执行录音逻辑，如果失败提示用户
                if (!doStart()) {
                    recordFail();
                }
            }
        });
    }


    /**
     * 实际播放的逻辑
     *
     * @param mAudioFile
     */
    private void doPlay(File mAudioFile) {
        //配置播放器 mediaPlayer
        mMediaPlayer = new MediaPlayer();

        try {
            //设置声音文件
            mMediaPlayer.setDataSource(mAudioFile.getAbsolutePath());

            //设置监听回调
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放结束,释放播放器
                    stopPlay();
                }
            });

            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    //提示
                    playFail();

                    //释放播放器
                    stopPlay();

                    //错误已经处理 返回true
                    return true;
                }
            });

            //配置音量
            mMediaPlayer.setVolume(1, 1);
            //是否循环
            mMediaPlayer.setLooping(false);

            //准备，开始
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            //异常处理，防止闪退

            //提示
            playFail();

            //释放播放器
            stopPlay();
        }

    }


    /**
     * 启动录音逻辑
     *
     * @return
     */
    private boolean doStart() {
        try {
            //创建 MediaRecorder
            mMediaRecorder = new MediaRecorder();

            //创建录音文件
            mAudioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/iMoocDemo" +
                    System.currentTimeMillis() + ".m4a");
            //创建父目录和文件
            mAudioFile.getParentFile().mkdirs();
            mAudioFile.createNewFile();

            //配置 MediaRecorder

            //从麦克风采集
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            //保存文件为 MP4格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            //设置所有Android系统都支持的采样频率
            mMediaRecorder.setAudioSamplingRate(44100);

            //文件的编码 通用的 AAC编码格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            //所音质比较好偶读频率
            mMediaRecorder.setAudioEncodingBitRate(96000);

            //设置录音文件的位置
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());

            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            //记录开始录音的时间，统计时长
            setmStartRecordTime(System.currentTimeMillis());

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            Log.e(TAG, "doStart: " + e.toString());
            //捕获异常，避免闪退 返回false 提醒用户失败
            return false;
        }

        return true;
    }

    /**
     * 停止录音
     */
    public void stopRecord() {

        //提交后台任务，执行录音逻辑
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //执行停止录音逻辑，失败就提醒
                if (!doStop()) {
                    recordFail();
                }

                //释放MediaRecorder
                releaseRecorder();
            }
        });

    }

    /**
     * 停止录音的逻辑
     *
     * @return
     */
    private boolean doStop() {

        try {
            //停止录音
            mMediaRecorder.stop();

            //记录停止的时间
            setmStopRecordTime(System.currentTimeMillis());

            //只接受超过3秒的录音，在 UI显示出来
            final int secound = (int) ((mStopRecordTime - mStartRecordTime) / 1000);
            if (secound > 3) {
                //在主线程显示UI
                mMainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        mTvLog.setText("\n录音成功" + secound + "秒");
                    }
                });
            }

            //停止成功

        } catch (RuntimeException e) {
            e.printStackTrace();

            //捕获异常，避免闪退 返回false 提醒用户失败
            return false;
        }

        return true;
    }

    /**
     * 停止播放的逻辑
     */
    private void stopPlay() {
        //重置播放状态
        mIsPlaying = false;

        //释放播放器
        if (mMediaPlayer != null) {
            //重置监听器，防止内存泄漏
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);

            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();

            mMediaPlayer = null;
        }

    }


    /**
     * 录音错误的处理
     */
    private void recordFail() {
        mAudioFile = null;

        //需要在主线程给提示的 Toast
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(FileActivity.this, "录音失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 释放MediaRecorder
     */
    private void releaseRecorder() {
        //检查 MediaRecorder 不为 null
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * 提醒播放失败
     */
    private void playFail() {
        //主线程 提示

        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(FileActivity.this, "播放失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public long getmStartRecordTime() {
        return mStartRecordTime;
    }

    public void setmStartRecordTime(long mStartRecordTime) {
        this.mStartRecordTime = mStartRecordTime;
    }

    public long getmStopRecordTime() {
        return mStopRecordTime;
    }

    public void setmStopRecordTime(long mStopRecordTime) {
        this.mStopRecordTime = mStopRecordTime;
    }

}
