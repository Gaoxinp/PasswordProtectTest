package com.example.passwordprotecttest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SysSetActivity extends AppCompatActivity {
    private Context context = SysSetActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_set);
        new ScreenListener(context).begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenoff() {

            }

            @Override
            public void onUserPrisent() {

            }

            @Override
            public void exitAPP() {
                finish();
            }
        });
    }

}
