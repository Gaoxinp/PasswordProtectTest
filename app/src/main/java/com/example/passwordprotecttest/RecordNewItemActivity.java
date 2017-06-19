package com.example.passwordprotecttest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class RecordNewItemActivity extends AppCompatActivity {
    private Spinner spinner = null;
    private EditText name = null;
    private EditText url = null;
    private EditText account = null;
    private EditText password = null;
    private EditText comment = null;
    /**
     * 下拉列表的数据，共有6条："网址网站","聊天工具","游戏账号","支付账号","软件账号","其他"
     */
    private String[] newItemStyle = null;
    private Context context = RecordNewItemActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new_item);
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
        setSpinnerContent();
        name = (EditText) findViewById(R.id.name_et_new_item);
        url = (EditText) findViewById(R.id.url_et_new_item);
        account = (EditText) findViewById(R.id.account_et_new_item);
        password = (EditText) findViewById(R.id.password_et_new_item);
        comment = (EditText) findViewById(R.id.commment_et_new_item);

    }

    /**
     * 设置spinner里面的内容以及使用R.layout.record_new_item_style布局文件来设置格式
     */
    private void setSpinnerContent() {
        spinner = (Spinner) findViewById(R.id.spinner);
        newItemStyle = new String[]{"网址网站", "聊天工具", "游戏账号", "电子邮箱", "支付账号", "软件账号", "其他"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecordNewItemActivity.this, R.layout.record_new_item_style, newItemStyle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setDropDownVerticalOffset(Dp2pxANDpx2dp.dip2px(RecordNewItemActivity.this, 50));


    }

    public void insertNewItem(View view) {
        String type = "";
        String sName = name.getText().toString();
        String sUrl = url.getText().toString();
        String sAccount = account.getText().toString();
        String sPassword = password.getText().toString();
        String sComment = comment.getText().toString();
        switch (spinner.getSelectedItem().toString()) {
            case "网址网站":
                type = "station";
                break;
            case "聊天工具":
                type = "chat";
                break;
            case "游戏账号":
                type = "game";
                break;
            case "电子邮箱":
                type = "mail";
                break;
            case "支付账号":
                type = "pay";
                break;
            case "软件账号":
                type = "software";
                break;
            case "其他":
                type = "other";
                break;
            default:
                break;
        }
        System.out.println("type=" + type + ",url=" + sUrl + ",account=" + sAccount + ",password=" + sPassword + ",comment=" + sComment);

        Db db = new Db(context);
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", sName);
        values.put("url", sUrl);
        values.put("account", sAccount);
        values.put("password", sPassword);
        values.put("comment", sComment);
        dbWrite.insert(type, null, values);
        dbWrite.close();
        Intent intent = new Intent();
        intent.putExtra("type", type);
        setResult(RESULT_OK, intent);
        finish();
    }
}
