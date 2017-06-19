package com.example.passwordprotecttest;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.passwordprotecttest.StaticValues.diyLock;
import static com.example.passwordprotecttest.StaticValues.hasDiyLock;
import static com.example.passwordprotecttest.StaticValues.hasLock;
import static com.example.passwordprotecttest.StaticValues.id;

public class SetDIYLockActivity extends AppCompatActivity {
    private EditText password_et;
    private Context context = SetDIYLockActivity.this;
    private TextView inputPass_tv, inputAgain_tv;
    private InputMethodManager imm;
    private String inputPass, againPass;
    private View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (password_et.getText().length() != 0) {
                    if (inputPass_tv.getVisibility() == TextView.VISIBLE) {
                        inputPass = password_et.getText().toString();
                        inputPass_tv.setVisibility(TextView.INVISIBLE);
                        inputAgain_tv.setVisibility(TextView.VISIBLE);
                        password_et.setText("");
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    } else {
                        againPass = password_et.getText().toString();
                        if (!inputPass.equals(againPass)) {
                            inputPass_tv.setVisibility(TextView.VISIBLE);
                            inputAgain_tv.setVisibility(TextView.INVISIBLE);
                            Toast.makeText(context, "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                            password_et.setText("");
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        } else {
                            try {
                                PackageManager pm = getPackageManager();
                                PackageInfo pi = null;
                                pi = pm.getPackageInfo(getPackageName(), 0);
                                String version = pi.versionName;
                                Db db = new Db(context);
                                SQLiteDatabase dbWrite = db.getWritableDatabase();
                                ContentValues value = new ContentValues();
                                value.put("version", version);
                                value.put("diylock", inputPass);
                                if (hasLock) {
                                    int a = dbWrite.update("msg", value, "_id=?", new String[]{id});
                                    System.out.println("修改了" + a + "条记录");
                                } else {
                                    long a = dbWrite.insert("msg", null, value);
                                    System.out.println("修改了" + a + "条记录");
                                }
                                dbWrite.close();
                                hasDiyLock = true;
                                hasLock = true;
                                diyLock = inputPass;
                                finish();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_diylock);
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
        initmsg();
        password_et.setOnKeyListener(keyListener);
        //自动打开软键盘
        imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(password_et, InputMethodManager.RESULT_SHOWN);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
//                InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                0);
    }

    private void initmsg() {
        inputPass_tv = (TextView) findViewById(R.id.input_setDIYLock_tv);
        inputAgain_tv = (TextView) findViewById(R.id.inputAgain_setDIYLock_tv);
        inputPass_tv.setVisibility(TextView.VISIBLE);
        inputAgain_tv.setVisibility(TextView.INVISIBLE);
    }
}
