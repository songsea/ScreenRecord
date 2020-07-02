package com.sea.screenrecord.tile;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import com.sea.screenrecord.RecorderView;
import com.sea.screenrecord.ScreenRecordService;
import com.sea.screenrecord.ScreenRecordingActivity;
import java.lang.reflect.Method;

public class ScreenRecordTileService extends TileService {
    public static final String TAG = "ScreenRecordTileService";
    public ScreenRecordTileService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestory");
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.d(TAG,"onTileAdded");
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        Log.d(TAG,"onTileRemoved");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(TAG,"onStartListening");
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.d(TAG,"onStopListening");
    }

    @Override
    public void onClick() {
        super.onClick();
        Log.d(TAG,"onClick");
        if (RecorderView.getInstance(getApplication()).isRecording()) {
            stopRecorder();
        } else {
            startRecorder();
        }
        collapseStatusBar(this.getApplicationContext());

    }


    public void updateStates() {
        //获取自定义的Tile.
        Tile tile = getQsTile();
        if (tile == null) {
            return;
        }
        Log.d(TAG, "Tile state: " + tile.getState());
        switch (tile.getState()) {
            case Tile.STATE_ACTIVE:
                //当前状态是开，设置状态为关闭.
                tile.setState(Tile.STATE_INACTIVE);
                //更新快速设置面板上的图块的颜色，状态为关.
                tile.updateTile();
                //do close somethings.
                break;
            case Tile.STATE_UNAVAILABLE:
                break;
            case Tile.STATE_INACTIVE:
                //当前状态是关，设置状态为开.
                tile.setState(Tile.STATE_ACTIVE);
                //更新快速设置面板上的图块的颜色，状态为开.
                tile.updateTile();
                //do open somethings.
                break;
            default:
                break;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return super.onBind(intent);

    }

    public void startRecorder() {
        Intent intent = new Intent(this, ScreenRecordingActivity.class);
        startActivity(intent);
    }
    public void stopRecorder(){
        Intent service = new Intent(this, ScreenRecordService.class);
        stopService(service);
    }
    public static void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;

            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
    }
