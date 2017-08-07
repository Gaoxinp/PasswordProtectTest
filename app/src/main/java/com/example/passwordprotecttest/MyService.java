package com.example.passwordprotecttest;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Xml;
import android.widget.Toast;

import com.example.model.Item;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by 高信朋 on 2017/8/6.
 */

public class MyService extends Service {
    private Context context = MyService.this;

    private MyBinder mBinder = new MyBinder();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                MyToast.toast(context, "恢复完成", Toast.LENGTH_SHORT);
            }
            if (msg.what == 1) {
                MyToast.toast(context, "当前手机默认路径下未发现备份文件，请确定该文件存在且文件名称未被修改", Toast.LENGTH_SHORT);
            }
        }
    };

    /**
     * 创建MyBinder类，并在其中创建方法，供Activity中接收到MyBinder对象后调用该方法来访问Service的状态
     */
    public class MyBinder extends Binder {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new MyThread(mHandler).start();

    }

    class MyThread extends Thread {
        private Handler mThreadHandler;

        MyThread(Handler handler) {
            mThreadHandler = handler;
        }

        @Override
        public void run() {
            super.run();


            try {
                File inputFile = new File("sdcard/backup.xml");
                if (inputFile.exists()) {
////                  如果备份文件存在，可以备份，则首先清空表记录
                    Db db = new Db(context);
                    SQLiteDatabase dbWrite = db.getWritableDatabase();
                    dbWrite.beginTransaction();
                    dbWrite.execSQL("DELETE FROM station;");
                    dbWrite.execSQL("update sqlite_sequence set seq=0 where name=\"station\";");
                    dbWrite.execSQL("DELETE FROM chat;");
                    dbWrite.execSQL("update sqlite_sequence set seq=0 where name=\"chat\";");
                    dbWrite.execSQL("DELETE FROM game;");
                    dbWrite.execSQL("update sqlite_sequence set seq=0 where name=\"game\";");
                    dbWrite.execSQL("DELETE FROM mail;");
                    dbWrite.execSQL("update sqlite_sequence set seq=0 where name=\"mail\";");
                    dbWrite.execSQL("DELETE FROM pay;");
                    dbWrite.execSQL("update sqlite_sequence set seq=0 where name=\"pay\";");
                    dbWrite.execSQL("DELETE FROM software;");
                    dbWrite.execSQL("update sqlite_sequence set seq=0 where name=\"software\";");
                    dbWrite.execSQL("DELETE FROM other;");
                    dbWrite.execSQL("update sqlite_sequence set seq=0 where name=\"other\";");

                    dbWrite.setTransactionSuccessful();
                    dbWrite.endTransaction();
//                    然后解析xml文件并增加数据
                    XmlPullParser pullParser = Xml.newPullParser();
                    FileInputStream fis = new FileInputStream(inputFile);
                    pullParser.setInput(fis, "utf-8");

                    ArrayList<Item> arrayList = new ArrayList<Item>();
                    Item item = new Item();
//                     这里获取节点的类型
                    int type = pullParser.getEventType();
//                     这里开始解析，当时XmlPullParser.END_DOCUMENT节点的时候就解析完成了
                    while (type != XmlPullParser.END_DOCUMENT) {
//                                    判断是否是开始节点，包含pwpt、station、item、name、url、account、password、comment、chat、game、mail、pay、software、other
                        if (type == XmlPullParser.START_TAG) {
                            switch (pullParser.getName()) {
                                case "item":
                                    item = new Item();
                                    break;
                                case "name":
                                    item.setName(pullParser.nextText());
                                    break;
                                case "url":
                                    item.setUrl(pullParser.nextText());
                                    break;
                                case "account":
                                    item.setAccount(pullParser.nextText());
                                    break;
                                case "password":
                                    item.setPassword(pullParser.nextText());
                                    break;
                                case "comment":
                                    item.setComment(pullParser.nextText());
                                    break;
                                case "station":
                                    arrayList = new ArrayList<Item>();
                                    break;
                                case "chat":
                                    arrayList = new ArrayList<Item>();
                                    break;
                                case "game":
                                    arrayList = new ArrayList<Item>();
                                    break;
                                case "mail":
                                    arrayList = new ArrayList<Item>();
                                    break;
                                case "pay":
                                    arrayList = new ArrayList<Item>();
                                    break;
                                case "software":
                                    arrayList = new ArrayList<Item>();
                                    break;
                                case "other":
                                    arrayList = new ArrayList<Item>();
                                    break;
                            }
//                           如果是XmlPullParser.END_TAG节点，包括item、station、chat、game、mail、pay、software、other、
                        } else if (type == XmlPullParser.END_TAG) {
                            switch (pullParser.getName()) {
                                case "item":
                                    arrayList.add(item);
                                    break;
                                case "station":
                                    recoverDb("station", arrayList);
                                    break;
                                case "chat":
                                    recoverDb("chat", arrayList);
                                    break;
                                case "game":
                                    recoverDb("game", arrayList);
                                    break;
                                case "mail":
                                    recoverDb("mail", arrayList);
                                    break;
                                case "pay":
                                    recoverDb("pay", arrayList);
                                    break;
                                case "software":
                                    recoverDb("software", arrayList);
                                    break;
                                case "other":
                                    recoverDb("other", arrayList);
                                    break;
                            }
                        }
                        type = pullParser.next();
                    }
//                    MyToast.toast(context, "恢复完成", Toast.LENGTH_SHORT);
                    Message message = new Message();
                    message.what = 0;
                    mThreadHandler.sendMessage(message);
                    if (fis != null) {
                        fis.close();
                    }
                } else {
                    Message message = new Message();
                    message.what = 1;
                    mThreadHandler.sendMessage(message);
//                    MyToast.toast(context, "当前手机默认路径下未发现备份文件，请确定该文件存在且文件名称未被修改", Toast.LENGTH_SHORT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 恢复数据到SQLite
         *
         * @param type
         * @param arrayList
         */
        private void recoverDb(String type, ArrayList<Item> arrayList) {
            Db db = new Db(context);
            SQLiteDatabase dbWrite = db.getWritableDatabase();
            dbWrite.beginTransaction();
            ContentValues values;
            for (Item item : arrayList) {
                values = new ContentValues();
                values.put("name", item.getName());
                values.put("url", item.getUrl());
                values.put("account", item.getAccount());
                values.put("password", item.getPassword());
                values.put("comment", item.getComment());
                dbWrite.insert(type, null, values);
            }
            dbWrite.setTransactionSuccessful();
            dbWrite.endTransaction();

            dbWrite.close();
        }
    }



}
