package com.sea.screenrecord;

import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.sea.screenrecord.view.TimerTextView;

public class RecorderView {
    private WindowManager mWM;
    private WindowManager.LayoutParams mWMParams;
    private Context mContext;
    private Application application;
    private LinearLayout mMainView;
    private TimerTextView mRecordTime;
    private Boolean isRecorder = false;
    public static String TAG = "RecorderView";
    public static RecorderView instance;

    private RecorderView(Application application) {
        this.mContext = application.getApplicationContext();
        this.application = application;
    }

    public static RecorderView getInstance(Application application) {
        if (instance == null) {
            instance = new RecorderView(application);
        }
        return instance;
    }

    public void showRecord() {
        initView();
        initParams();
        try {
            mWM.addView(mMainView, mWMParams);
            mRecordTime.startTimer();
            isRecorder = true;
        } catch (Exception e) {
            Log.e(TAG, "添加视图异常：" + e.toString());
        }
    }

    public void hideRecord() {
        try {
            mRecordTime.stopTimer();
            mWM.removeView(mMainView);
            isRecorder = false;
        } catch (Exception e) {
            Log.e(TAG, "remove 视图异常：" + e.toString());
        }
    }

    private void initView() {
        mMainView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.recordview, null);
        mRecordTime = mMainView.findViewById(R.id.tv_time);

    }
    private void initParams() {
        mWM =  (WindowManager)application.getSystemService(Context.WINDOW_SERVICE);
        mWMParams = new WindowManager.LayoutParams();
        //noinspection ResourceType
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWMParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWMParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        // 设置悬浮求的背景为透明的
        mWMParams.format = PixelFormat.RGBA_8888;// 表示透明，下面可以看见
        mWMParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        // 设置固定在视图的中上部
        mWMParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 宽高都设置为50 , 改变
        mWMParams.width = 100;
        mWMParams.height = mWMParams.width;
    }

    public  Boolean isRecording() {
        return isRecorder;
    }
}
