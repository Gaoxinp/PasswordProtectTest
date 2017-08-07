package com.example.passwordprotecttest;

import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Item;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SysSetActivity extends AppCompatActivity {
    private Context context = SysSetActivity.this;
    private RecyclerView recyclerView;
    private MyRecycleViewAdapter mAdapter;
    private String[] itemText = {"备份数据", "恢复备份"};
    private int[] itemImg = {R.drawable.backup_img, R.drawable.recover_img};

    private String[] type = new String[]{"station", "chat", "game", "mail", "pay", "software", "other"};

    private MyService.MyBinder mBinder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (MyService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_sysSet);
//        设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        设置适配器
        mAdapter = new MyRecycleViewAdapter(context, itemImg, itemText);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                自定义Toast
//                Toast toast = new Toast(context);
//                View v = LayoutInflater.from(context).inflate(R.layout.toast,null,false);
//                toast.setView(v);
//                TextView toast_tv = (TextView) v.findViewById(R.id.toast_tv);
//                toast_tv.setText("item"+position);
//                toast.show();
                switch (position) {
//                    如果点击了“备份数据”
                    case 0:
                        try {
                            Db db = new Db(context);
                            SQLiteDatabase dbReader = db.getReadableDatabase();
                            XmlSerializer serializer = Xml.newSerializer();
                            File outputFile = new File("sdcard/backup.xml");
                            FileOutputStream fos = new FileOutputStream(outputFile);
                            serializer.setOutput(fos, "utf-8");
//                            生成xml文件头结点，第一个参数设置头结点里面的写明的编码格式，第二个参数指定该xml是否被其他文件约束，true则表示独立
                            serializer.startDocument("utf-8", true);
//                            生成头标签，就是最外层的标签
                            serializer.startTag(null, "pwpt");
                            Cursor[] cursors = new Cursor[type.length];
                            for (int i = 0; i < type.length; i++) {
                                cursors[i] = dbReader.query(type[i], null, null, null, null, null, null);
                                serializer.startTag(null, type[i]);
                                while (cursors[i].moveToNext()) {
                                    serializer.startTag(null, "item");

                                    serializer.startTag(null, "name");
                                    serializer.text(cursors[i].getString(cursors[i].getColumnIndex("name")));
                                    serializer.endTag(null, "name");
                                    serializer.startTag(null, "url");
                                    serializer.text(cursors[i].getString(cursors[i].getColumnIndex("url")));
                                    serializer.endTag(null, "url");
                                    serializer.startTag(null, "account");
                                    serializer.text(cursors[i].getString(cursors[i].getColumnIndex("account")));
                                    serializer.endTag(null, "account");
                                    serializer.startTag(null, "password");
                                    serializer.text(cursors[i].getString(cursors[i].getColumnIndex("password")));
                                    serializer.endTag(null, "password");
                                    serializer.startTag(null, "comment");
                                    serializer.text(cursors[i].getString(cursors[i].getColumnIndex("comment")));
                                    serializer.endTag(null, "comment");

                                    serializer.endTag(null, "item");
                                }

                                serializer.endTag(null, type[i]);

                            }
//                            生成头标签，就是最外层的标签
                            serializer.endTag(null, "pwpt");
//                            告知xml序列化器文档结束了
                            serializer.endDocument();
                            for (Cursor cursor : cursors) {
                                if (cursor != null) {
                                    cursor.close();
                                }
                            }
                            dbReader.close();
                            db.close();
                            fos.close();
                            MyToast.toast(context, "备份完成", Toast.LENGTH_SHORT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
//                    如果点击了“恢复备份”
                    case 1:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("警告").setIcon(getResources().getDrawable(R.mipmap.jinggao)).setMessage("此操作首先会清空您的数据库，是否继续？").setPositiveButton("继续，任性", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, MyService.class);
                                context.bindService(intent, conn, BIND_AUTO_CREATE);
//                                ValueAnimator animation = ValueAnimator.ofInt(1,1000);
//                                animation.setDuration(1000000);
//                                animation.setInterpolator(new LinearInterpolator());
//                                final TextView tv = (TextView) findViewById(R.id.tv_sysset);
//                                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                    @Override
//                                    public void onAnimationUpdate(ValueAnimator animation) {
//                                        int v=(Integer) animation.getAnimatedValue();
//                                        tv.setText(v+"");
//                                    }
//                                });
//                                animation.start();



                            }
                        }).setNegativeButton("容我想想", null).setCancelable(false).show();
                        break;
                }
            }
        });


//        设置分割线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
//        设置动画效果
        TranslateAnimation animation = new TranslateAnimation(200f, 0f, 0f, 0f);
        AlphaAnimation animation1 = new AlphaAnimation(0f, 1.0f);
        AnimationSet set = new AnimationSet(true);
        set.setDuration(500);
        set.addAnimation(animation);
        set.addAnimation(animation1);
        LayoutAnimationController con = new LayoutAnimationController(set);
        con.setOrder(LayoutAnimationController.ORDER_RANDOM);

        recyclerView.setLayoutAnimation(con);
        recyclerView.startLayoutAnimation();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(conn);
    }
}
