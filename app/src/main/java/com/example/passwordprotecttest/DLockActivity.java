package com.example.passwordprotecttest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.passwordprotecttest.StaticValues.dLock;

public class DLockActivity extends AppCompatActivity {
    private Context context = DLockActivity.this;
    private InputMethodManager imm;
    private EditText[] password_et = null;
    private String flag;
    private TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {


            if (password_et[0].hasFocus()) {
                if (s.length() == 1) {

                    password_et[1].requestFocus();
                }
            } else if (password_et[1].hasFocus()) {
                if (s.length() == 1) {

                    password_et[2].requestFocus();
                }
            } else if (password_et[2].hasFocus()) {
                if (s.length() == 1) {

                    password_et[3].requestFocus();
                }
            } else if (password_et[3].hasFocus()) {
            }
        }
    };
    private View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                EditText view = (EditText) v;
                if (view == password_et[3]) {
                    if (view.getText().length() == 1) {
                        view.setText("");
                    } else {
                        password_et[2].requestFocus();
                        password_et[2].setText("");
                    }
                } else if (view == password_et[2]) {
                    if (view.getText().length() == 1) {
                        view.setText("");
                    } else {
                        password_et[1].requestFocus();
                        password_et[1].setText("");
                    }

                } else if (view == password_et[1]) {
                    if (view.getText().length() == 1) {
                        view.setText("");
                    } else {
                        password_et[0].requestFocus();
                        password_et[0].setText("");
                    }

                } else if (view == password_et[0]) {
                    view.setText("");

                }
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (password_et[0].getText().length() == 1 && password_et[1].getText().length() == 1 && password_et[2].getText().length() == 1 && password_et[3].getText().length() == 1) {
                    String password = password_et[0].getText().toString() + password_et[1].getText().toString() + password_et[2].getText().toString() + password_et[3].getText().toString();
                    if (password.equals(dLock)) {
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
                        password_et[0].requestFocus();
                        password_et[0].setText("");
                        password_et[1].setText("");
                        password_et[2].setText("");
                        password_et[3].setText("");
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                } else {
                    Toast.makeText(context, "请输入完整的四位数字密码", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlock);
        password_et = new EditText[4];
        password_et[0] = (EditText) findViewById(R.id.password1_dlock);
        password_et[1] = (EditText) findViewById(R.id.password2_dlock);
        password_et[2] = (EditText) findViewById(R.id.password3_dlock);
        password_et[3] = (EditText) findViewById(R.id.password4_dlock);
        password_et[0].addTextChangedListener(textChangeListener);
        password_et[1].addTextChangedListener(textChangeListener);
        password_et[2].addTextChangedListener(textChangeListener);
        password_et[3].addTextChangedListener(textChangeListener);
        password_et[0].setOnKeyListener(keyListener);
        password_et[1].setOnKeyListener(keyListener);
        password_et[2].setOnKeyListener(keyListener);
        password_et[3].setOnKeyListener(keyListener);
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        //自动打开软键盘
        imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                0);
    }
}
