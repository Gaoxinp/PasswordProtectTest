package com.example.passwordprotecttest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
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

public class SafePageActivity extends AppCompatActivity {
    private TextView msg_tv = null;
    private TextView dLockSet_tv = null;
    private TextView pLockSet_tv = null;
    private TextView diyLockSet_tv = null;
    private TextView dLockReset_tv = null;
    private TextView pLockReset_tv = null;
    private TextView diyLockReset_tv = null;
    private Switch dlockSwitch = null;
    private Switch plockSwitch = null;
    private Switch diylockSwitch = null;
    private Context context = SafePageActivity.this;
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Db db = new Db(context);
            SQLiteDatabase dbWrite = db.getWritableDatabase();
            SQLiteDatabase dbRead = db.getWritableDatabase();
            Cursor cursor = dbRead.query("msg",null,null,null,null,null,null);
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex("_id"))+"";
            ContentValues cv = new ContentValues();
            if (dlockSwitch == buttonView){
                if (isChecked){
                    plockSwitch.setChecked(false);
                    diylockSwitch.setChecked(false);
                    useWhat = 1;
                    cv.put("usewhat",useWhat);
                }else{
                    useWhat = 0;
                    cv.put("usewhat",useWhat);
                }
            }
            if (plockSwitch == buttonView){
                if (isChecked){
                    dlockSwitch.setChecked(false);
                    diylockSwitch.setChecked(false);
                    useWhat = 2;
                    cv.put("usewhat",useWhat);
                }else{
                    useWhat = 0;
                    cv.put("usewhat",useWhat);
                }
            }
            if (diylockSwitch == buttonView){
                if (isChecked){
                    dlockSwitch.setChecked(false);
                    plockSwitch.setChecked(false);
                    useWhat = 3;
                    cv.put("usewhat",useWhat);
                }else{
                    useWhat = 0;
                    cv.put("usewhat",useWhat);
                }
            }
            int a = dbWrite.update("msg",cv,"_id=?",new String[]{id});
            System.out.println("修改了"+a+"条记录");
            cursor.close();
            dbRead.close();
            dbWrite.close();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ColorSwitchStyle);
        setContentView(R.layout.activity_safe_page);
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
        dLockSet_tv = (TextView) findViewById(R.id.dLockSet_safepage_tv);
        dLockReset_tv = (TextView) findViewById(R.id.dLockReset_safepage_tv);
        pLockSet_tv = (TextView) findViewById(R.id.pLockSet_safepage_tv);
        pLockReset_tv = (TextView) findViewById(R.id.pLockReset_safepage_tv);
        diyLockSet_tv = (TextView) findViewById(R.id.diyLockSet_safepage_tv);
        diyLockReset_tv = (TextView) findViewById(R.id.diyLockReset_safepage_tv);
        //为提示信息的TextView提示信息
        setMsgTV();
        //设置或者更改密码锁的TextView设置课件或者不可见，根据是否设置过密码锁
        setVisibleTV();
        //设置三个Switch
        setSwitch();

    }

    private void setSwitch() {
        dlockSwitch = (Switch) findViewById(R.id.switch1);
        plockSwitch = (Switch) findViewById(R.id.switch2);
        diylockSwitch = (Switch) findViewById(R.id.switch3);
        if (!hasDLock){
            dlockSwitch.setEnabled(false);
        }else {
            dlockSwitch.setEnabled(true);
        }
        if (!hasPLock){
            plockSwitch.setEnabled(false);
        }else {
            plockSwitch.setEnabled(true);
        }
        if (!hasDiyLock){
            diylockSwitch.setEnabled(false);
        }else {
            diylockSwitch.setEnabled(true);
        }
        if (useWhat ==1){
            dlockSwitch.setChecked(true);
        }else if (useWhat == 2){
            plockSwitch.setChecked(true);
        }else if (useWhat == 3){
            diylockSwitch.setChecked(true);
        }
        dlockSwitch.setOnCheckedChangeListener(checkedChangeListener);
        plockSwitch.setOnCheckedChangeListener(checkedChangeListener);
        diylockSwitch.setOnCheckedChangeListener(checkedChangeListener);
    }

    /**
     * 根据是否已经设置了各种锁，来设置各个TextView是否可见
     */
    private void setVisibleTV() {

        if(!hasLock){
            dLockSet_tv.setVisibility(TextView.VISIBLE);
            dLockReset_tv.setVisibility(TextView.INVISIBLE);
            pLockSet_tv.setVisibility(TextView.VISIBLE);
            pLockReset_tv.setVisibility(TextView.INVISIBLE);
            diyLockSet_tv.setVisibility(TextView.VISIBLE);
            diyLockReset_tv.setVisibility(TextView.INVISIBLE);


        }else{


                if(hasDLock){
                    dLockSet_tv.setVisibility(TextView.INVISIBLE);
                    dLockReset_tv.setVisibility(TextView.VISIBLE);
                }else{
                    dLockSet_tv.setVisibility(TextView.VISIBLE);
                    dLockReset_tv.setVisibility(TextView.INVISIBLE);
                }
                if(hasPLock){
                    pLockSet_tv.setVisibility(TextView.INVISIBLE);
                    pLockReset_tv.setVisibility(TextView.VISIBLE);
                }else{
                    pLockSet_tv.setVisibility(TextView.VISIBLE);
                    pLockReset_tv.setVisibility(TextView.INVISIBLE);
                }
                if(hasDiyLock){
                    diyLockSet_tv.setVisibility(TextView.INVISIBLE);
                    diyLockReset_tv.setVisibility(TextView.VISIBLE);
                }else{
                    diyLockSet_tv.setVisibility(TextView.VISIBLE);
                    diyLockReset_tv.setVisibility(TextView.INVISIBLE);
                }

        }
    }

    /**
     * 重写此方法用来更新页面
     */
    @Override
    protected void onResume() {
        super.onResume();
        reFrash();
    }
    /**
    * 刷新布局
    */
    private void reFrash(){
        setMsgTV();
        setVisibleTV();
        setSwitch();
    }

    /**
     * 设置显示提示的文本内容
     */
    private void setMsgTV() {
        msg_tv = (TextView) findViewById(R.id.msg_tv_safepage);
        if (hasLock){
            msg_tv.setText("您已设置密码，您还可以：");
        }else{
            msg_tv.setText("您尚未设置任何密码，您可以：");
        }

    }
    //设置数字锁
    public void setDLock(View v){
        Intent intent = new Intent(context,SetDLockActivity.class);
        startActivity(intent);

    }
    //设置图案锁
    public void setPLock(View v){

    }
    //设置自定义密码锁
    public void setDiyLock(View v){
        Intent intent = new Intent(context,SetDIYLockActivity.class);
        startActivity(intent);
    }
    //修改数字锁
    public void changeDLock(View v){
        Intent intent = new Intent(context,ResetDLockActivity.class);
        startActivity(intent);
    }
    //修改图案锁
    public void changePLock(View v){

    }
    //修改自定义密码锁
    public void changeDiyLock(View v){
        Intent intent = new Intent(context,ResetDIYLockActivity.class);
        startActivity(intent);
    }

}
