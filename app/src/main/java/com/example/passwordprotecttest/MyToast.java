package com.example.passwordprotecttest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.passwordprotecttest.R.layout.toast;

/**
 * Created by 高信朋 on 2017/8/6.
 */

public class MyToast extends Toast {
    private static Toast mToast;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public MyToast(Context context) {
        super(context);
    }

    /**
     * 自定义Toast
     * @param context 上下文
     * @param content 显示的内容
     * @param duration 显示时长：Toast.LENGTH_SHORT或者Toast.LENGTH_LONG
     */
    public static void toast(Context context,String content,int duration) {
        mToast = new Toast(context);
        View v = LayoutInflater.from(context).inflate(toast, null, false);
        mToast.setView(v);
        TextView toast_tv = (TextView) v.findViewById(R.id.toast_tv);
        toast_tv.setText(content);
        mToast.setDuration(duration);
        mToast.show();
    }
}
