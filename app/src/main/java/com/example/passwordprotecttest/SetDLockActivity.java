package com.example.passwordprotecttest;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.passwordprotecttest.StaticValues.dLock;
import static com.example.passwordprotecttest.StaticValues.hasDLock;
import static com.example.passwordprotecttest.StaticValues.hasLock;
import static com.example.passwordprotecttest.StaticValues.id;

public class SetDLockActivity extends AppCompatActivity {
    private TextView inputmsg_tv = null;
    private TextView inputAgain_tv = null;
    private EditText[] password_et = null;
    private Context context = SetDLockActivity.this;
    private String password_first;
    private String password_second;
    private InputMethodManager imm;
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

                    if (inputmsg_tv.getVisibility() == TextView.VISIBLE) {
                        password_first = password_et[0].getText().toString() + password_et[1].getText().toString() + password_et[2].getText().toString() + password_et[3].getText().toString();
                        inputmsg_tv.setVisibility(TextView.INVISIBLE);
                        inputAgain_tv.setVisibility(TextView.VISIBLE);
                        password_et[0].requestFocus();
                        password_et[0].setText("");
                        password_et[1].setText("");
                        password_et[2].setText("");
                        password_et[3].setText("");
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    } else {
                        password_second = password_et[0].getText().toString() + password_et[1].getText().toString() + password_et[2].getText().toString() + password_et[3].getText().toString();
                        if (!password_first.equals(password_second)) {
                            inputmsg_tv.setVisibility(TextView.VISIBLE);
                            inputAgain_tv.setVisibility(TextView.INVISIBLE);
                            password_et[0].requestFocus();
                            password_et[0].setText("");
                            password_et[1].setText("");
                            password_et[2].setText("");
                            password_et[3].setText("");
                            Toast.makeText(context, "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        } else {
                            try {
                                PackageManager pm = getPackageManager();
                                PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
                                String version = pi.versionName;
                                Db db = new Db(context);
                                SQLiteDatabase dbWrite = db.getWritableDatabase();
                                ContentValues value = new ContentValues();
                                value.put("version", version);
                                value.put("dlock", password_first);
                                if (hasLock) {

                                    int a = dbWrite.update("msg", value, "_id=?", new String[]{id});
                                    System.out.println("修改了" + a + "条记录");
                                } else {
                                    long a = dbWrite.insert("msg", null, value);
                                    System.out.println("修改了" + a + "条记录");
                                }
                                dbWrite.close();
                                hasDLock = true;
                                hasLock = true;
                                dLock = password_first;
                                finish();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "请输入完整的四位数字密码", Toast.LENGTH_SHORT).show();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dlock);
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
        //初始化提示语
        initmsg();
        password_et = new EditText[4];
        password_et[0] = (EditText) findViewById(R.id.password1_setdlock);
        password_et[1] = (EditText) findViewById(R.id.password2_setdlock);
        password_et[2] = (EditText) findViewById(R.id.password3_setdlock);
        password_et[3] = (EditText) findViewById(R.id.password4_setdlock);
        password_et[0].addTextChangedListener(textChangeListener);
        password_et[1].addTextChangedListener(textChangeListener);
        password_et[2].addTextChangedListener(textChangeListener);
        password_et[3].addTextChangedListener(textChangeListener);
        password_et[0].setOnKeyListener(keyListener);
        password_et[1].setOnKeyListener(keyListener);
        password_et[2].setOnKeyListener(keyListener);
        password_et[3].setOnKeyListener(keyListener);
        //自动打开软键盘
        imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(password_et[0], InputMethodManager.RESULT_SHOWN);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
//                InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void initmsg() {
        inputmsg_tv = (TextView) findViewById(R.id.input_setDLock_tv);
        inputAgain_tv = (TextView) findViewById(R.id.inputAgain_setDLock_tv);
        inputmsg_tv.setVisibility(TextView.VISIBLE);
        inputAgain_tv.setVisibility(TextView.INVISIBLE);
    }
}
