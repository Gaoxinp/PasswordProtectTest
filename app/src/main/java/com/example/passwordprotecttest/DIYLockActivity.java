package com.example.passwordprotecttest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.passwordprotecttest.StaticValues.diyLock;

public class DIYLockActivity extends AppCompatActivity {
    private String flag;
    private Context context = DIYLockActivity.this;
    private EditText password_et;
    private View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (password_et.getText().length() != 0) {
                    String password = password_et.getText().toString();
                    if (password.equals(diyLock)) {
                        //自动打开软键盘
                        imm = (InputMethodManager) context
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromInputMethod();
//                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
//                                InputMethodManager.HIDE_IMPLICIT_ONLY);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        Intent intent;
                        switch (flag) {
                            case "normal":
                                intent = new Intent(context, GuideActivity.class);
                                startActivity(intent);
                                break;
                            case "openScreen":
                                finish();
                                break;
                        }
                        finish();
                    } else {
                        Toast.makeText(context, "密码错误，请重试", Toast.LENGTH_SHORT).show();
                        password_et.setText("");
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                } else {
                    Toast.makeText(context, "请先输入密码", Toast.LENGTH_SHORT).show();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                getApplicationContext().sendBroadcast(new Intent("ExitAPP"));
            }
            return false;
        }
    };
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diylock);
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
        password_et = (EditText) findViewById(R.id.password_diylock_et);
        password_et.setOnKeyListener(keyListener);
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        //自动打开软键盘
        imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(password_et, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                0);

    }
}
