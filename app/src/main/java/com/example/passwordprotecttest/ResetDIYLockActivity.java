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

public class ResetDIYLockActivity extends AppCompatActivity {
    private EditText oldPass_et;
    private Context context = ResetDIYLockActivity.this;
    private View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (oldPass_et.getText().length() != 0) {
                    String password = oldPass_et.getText().toString();
                    if (password.equals(diyLock)) {
                        Intent intent = new Intent(context, SetDIYLockActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(context, "原密码验证错误，请重新输入", Toast.LENGTH_SHORT).show();
                        oldPass_et.setText("");
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                } else {
                    Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
            return false;
        }
    };
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_diylock);
        imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
        oldPass_et = (EditText) findViewById(R.id.password_resetDiylock_et);
        oldPass_et.setOnKeyListener(keyListener);
    }
}
