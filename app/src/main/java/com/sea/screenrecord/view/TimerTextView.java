package com.sea.screenrecord.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Formatter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTextView extends androidx.appcompat.widget.AppCompatTextView {
    public static final String TAG = "TimerTextView";
    /**
     * 是否还在进行及时
     */
    private boolean isRun = false;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 计时器
     */
    private Timer mTimer;
    /**
     * 秒数
     */
    private int second;
    public static final int SET_TEXT=111;

    Handler mHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==SET_TEXT){
                Log.e(TAG,"设置计时文字:"+second);
                setTimerText(timeFormat(second));
            }
        }
    };

    public TimerTextView(Context context) {
        super(context);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    public boolean isRun() {
        return isRun;
    }

    /**
     * 开始计时
     */
    public void startTimer() {
        Log.e("record","开始录制计时");
        if(null==mTimer){
            mTimer = new Timer(true);
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                second++;
                mHandler.sendEmptyMessageAtTime(SET_TEXT,0);
            }
        },500,1000); // 由于启动录屏需要将近一秒钟
        isRun=true;
    }

    /**
     * 恢复计时器
     */
    public void resumeTimer(){
        startTimer();
    }
    /**
     * 暂停计时
     */
    public void pauseTimer() {
        isRun=false;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }
    /**
     * 完成及时,恢复初始化
     */
    public void stopTimer() {
        pauseTimer();
        second=0;
    }


    private void setTimerText(String time){
        this.setText(time);
    }
    /**
     * 时间转换器
     * 格式："mm:ss"
     * @return  计时
     */
    private String timeFormat(int second) {
        int seconds = second % 60;
        int minutes = (second / 60) % 60;
        int hours = second / 3600;
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.getDefault());
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
