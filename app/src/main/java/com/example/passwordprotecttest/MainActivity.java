package com.example.passwordprotecttest;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import static com.example.passwordprotecttest.StaticValues.dLock;
import static com.example.passwordprotecttest.StaticValues.diyLock;
import static com.example.passwordprotecttest.StaticValues.hasDLock;
import static com.example.passwordprotecttest.StaticValues.hasDiyLock;
import static com.example.passwordprotecttest.StaticValues.hasLock;
import static com.example.passwordprotecttest.StaticValues.hasPLock;
import static com.example.passwordprotecttest.StaticValues.id;
import static com.example.passwordprotecttest.StaticValues.pLock;
import static com.example.passwordprotecttest.StaticValues.useWhat;

public class MainActivity extends AppCompatActivity {
    private TextView time = null;
    private Context context = MainActivity.this;
    private Cursor cursor = null;

    /*
    *
    * 将使用Handler自带的定时器*/
    Handler handler = new Handler();
    /*
    *
    * 初始显示的剩余时间是5秒*/
    private int delayTime = 2;
    /*
    *
    * 延时时间为1000毫秒 即1秒*/
    private int del = 1000;
    /*
    *
    * 自定义定时器
    *
    * */
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                if (delayTime != -1) {

                    handler.postDelayed(this, del);
                    time.setText(Integer.toString(delayTime--) + "s");

                } else {
                    Intent intent = new Intent();
                    switch (useWhat) {
                        case 0:
                            intent.setClass(MainActivity.this, GuideActivity.class);
                            break;
                        case 1:
                            intent.setClass(MainActivity.this, DLockActivity.class);
                            break;
                        case 2:
                            intent.setClass(MainActivity.this, PLockActivity.class);
                            break;
                        case 3:
                            intent.setClass(MainActivity.this, DIYLockActivity.class);
                            break;
                    }
                    intent.putExtra("flag", "normal");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("exception...");
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = (TextView) findViewById(R.id.time);
        getStaticValues();
        //设置Activity全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler.postDelayed(runnable, del);
    }

    private void getStaticValues() {
        Db db = new Db(context);
        SQLiteDatabase dbResd = db.getReadableDatabase();

        cursor = dbResd.query("msg", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("_id")) + "";
            dLock = cursor.getString(cursor.getColumnIndex("dlock"));
            pLock = cursor.getString(cursor.getColumnIndex("plock"));
            diyLock = cursor.getString(cursor.getColumnIndex("diylock"));
            useWhat = Integer.parseInt(cursor.getString(cursor.getColumnIndex("usewhat")));
            if ("".equals(dLock)) {
                hasDLock = false;
            } else {
                hasDLock = true;
            }
            if ("".equals(pLock)) {
                hasPLock = false;
            } else {
                hasPLock = true;
            }
            if ("".equals(diyLock)) {
                hasDiyLock = false;
            } else {
                hasDiyLock = true;
            }
            hasLock = true;
            System.out.println("hasLock=" + hasLock + ",hasdLock=" + hasDLock + "haspLock=" + hasPLock + "hasDiyLock=" + hasDiyLock + ",dlock=" + dLock + ",plock=" + pLock + ",diylock=" + diyLock + ",usewhat=" + useWhat);
        } else {
            id = "";
            hasLock = false;
            hasDLock = false;
            hasPLock = false;
            hasDiyLock = false;
            dLock = "";
            pLock = "";
            diyLock = "";
            useWhat = 0;
            System.out.println("hasLock=" + hasLock + ",hasdLock=" + hasDLock + "haspLock=" + hasPLock + "hasDiyLock=" + hasDiyLock + ",dlock=" + dLock + ",plock=" + pLock + ",diylock=" + diyLock + ",usewhat=" + useWhat);
        }
    }
}
