package com.example.passwordprotecttest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    private int typeIndex;
    private int groupIndex;
    private int childID;
    private String name;
    private String url;
    private String account;
    private String password;
    private String comment;
    private String[] itemType = new String[]{"网址网站", "聊天工具", "游戏账号", "电子邮箱", "支付账号", "软件账号", "其他"};
    private String[] style = new String[]{"station", "chat", "game", "mail", "pay", "software", "other"};
    private Spinner spinner;
    private EditText name_et;
    private EditText url_et;
    private EditText account_et;
    private EditText password_et;
    private EditText comment_et;
    private FloatingActionButton fab;
    private Context context = DetailActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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
        fab = (FloatingActionButton) findViewById(R.id.fab_detail_ok);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        groupIndex = bundle.getInt("groupIndex");
        childID = bundle.getInt("childID");
        typeIndex = bundle.getInt("type");
        name = bundle.getString("name");
        url = bundle.getString("url");
        account = bundle.getString("account");
        password = bundle.getString("password");
        comment = bundle.getString("comment");

        setSpinner();
        setEditText();
    }

    /**
     * 设置Spinner的内容，并设置为不可编辑
     */
    private void setSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_detail);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.record_new_item_style, itemType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(typeIndex);
        spinner.setDropDownVerticalOffset(Dp2pxANDpx2dp.dip2px(context, 50));
        spinner.setEnabled(false);
    }

    /**
     * 设置EditText的内容，并设置为不可编辑
     */
    private void setEditText() {
        name_et = (EditText) findViewById(R.id.name_et_detail);
        url_et = (EditText) findViewById(R.id.url_et_detail);
        account_et = (EditText) findViewById(R.id.account_et_detail);
        password_et = (EditText) findViewById(R.id.password_et_detail);
        comment_et = (EditText) findViewById(R.id.commment_et_detail);

        name_et.setText(name);
        url_et.setText(url);
        account_et.setText(account);
        password_et.setText(password);
        comment_et.setText(comment);

        name_et.setEnabled(false);
        url_et.setEnabled(false);
        account_et.setEnabled(false);
        password_et.setEnabled(false);
        comment_et.setEnabled(false);
    }

    public void editItem(View view) {
        if (spinner.isEnabled()) {
            Toast.makeText(context, "已经处于编辑状态，无需再次点击", Toast.LENGTH_SHORT).show();

        } else {

            spinner.setEnabled(true);
            name_et.setEnabled(true);
            url_et.setEnabled(true);
            account_et.setEnabled(true);
            password_et.setEnabled(true);
            comment_et.setEnabled(true);
            fab.setVisibility(FloatingActionButton.VISIBLE);
        }

    }

    public void updateItem(View view) {
        String type = "";
        String sName = name_et.getText().toString();
        String sUrl = url_et.getText().toString();
        String sAccount = account_et.getText().toString();
        String sPassword = password_et.getText().toString();
        String sComment = comment_et.getText().toString();
        Db db = new Db(context);
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (spinner.getSelectedItemId() != groupIndex) {
            dbWrite.delete(style[groupIndex], "_id=?", new String[]{"" + childID});
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
            cv.put("name", sName);
            cv.put("url", sUrl);
            cv.put("account", sAccount);
            cv.put("password", sPassword);
            cv.put("comment", sComment);
            dbWrite.insert(type, null, cv);
            dbWrite.close();
        } else {
            cv.put("name", sName);
            cv.put("url", sUrl);
            cv.put("account", sAccount);
            cv.put("password", sPassword);
            cv.put("comment", sComment);
            dbWrite.update(style[groupIndex], cv, "_id=?", new String[]{"" + childID});
            dbWrite.close();
        }
        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
