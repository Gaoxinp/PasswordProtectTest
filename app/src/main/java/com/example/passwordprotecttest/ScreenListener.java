package com.example.passwordprotecttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/**
 * Created by 高信朋 on 2016/11/21.
 * 监听屏幕亮，灭，解锁
 */

public class ScreenListener {
    private Context context;
    private ScreenBroadcastReceiver screenBroadcastReceiver;
    private ScreenStateListener stateListener;

    public ScreenListener(Context context) {
        this.context = context;
        screenBroadcastReceiver = new ScreenBroadcastReceiver();
    }

    /*
    * Screen状态广播接收者
    * */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action;

        /*
        * 当接收到广播时将会运行这个方法
        * */
        @Override
        public void onReceive(Context context, Intent intent) {

            action = intent.getAction();
            switch (action) {
                case Intent.ACTION_SCREEN_ON:   //开屏
                    stateListener.onScreenOn();
                    break;
                case Intent.ACTION_SCREEN_OFF:  //锁屏
                    stateListener.onScreenoff();
                    break;
                case Intent.ACTION_USER_PRESENT:    //解锁
                    stateListener.onUserPrisent();
                    break;
                case "ExitAPP":         //退出APP
                    stateListener.exitAPP();
                    break;
            }
        }
    }

    /*
    * 启动screen状态广播接收器
    * */
    private void registListener() {
        IntentFilter filter = new IntentFilter();   //filter将会设置监听的地点，当有操作就会发出广播
        filter.addAction("ExitAPP");
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        context.registerReceiver(screenBroadcastReceiver, filter);
    }

    /*
    * 当不需要监听屏幕时，使用此方法可以解除绑定
    * */
    public void unregistListener() {
        context.unregisterReceiver(screenBroadcastReceiver);
    }

    /*
    * 获取屏幕状态并直接运行该状态下的操作
    * */
    private void getScreenState() {
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (manager.isScreenOn()) {
            if (stateListener != null) {
                stateListener.onScreenOn();
            }
        } else {
            if (stateListener != null) {
                stateListener.onScreenoff();
            }
        }

    }

    /*
    * 直接使用类2的对象调用此函数，开始监听屏幕
    * */
    public void begin(ScreenStateListener stateListener) {
        this.stateListener = stateListener;
        registListener();
        getScreenState();
    }

    /*
    * 提供给用户，用来设置屏幕解锁，锁屏的操作
    * */
    public interface ScreenStateListener {
        public void onScreenOn();

        public void onScreenoff();

        public void onUserPrisent();

        public void exitAPP();
    }
}
