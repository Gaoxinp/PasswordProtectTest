package com.example.passwordprotecttest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 高信朋 on 2016/11/8.
 */

public class Db extends SQLiteOpenHelper {
    public Db(Context context) {
        super(context,"db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE msg(_id INTEGER PRIMARY KEY AUTOINCREMENT,version VARVHAR(8) DEFAULT \"\",dlock VARCHAR(8) DEFAULT \"\",plock VARCHAR(9) DEFAULT \"\",diylock VERCHAR(20) DEFAULT \"\",usewhat VARCHAR(8) DEFAULT \"0\")");
        db.execSQL("CREATE TABLE station(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT DEFAULT \"\",url TEXT DEFAULT \"\",account TEXT DEFAULT \"\",password TEXT DEFAULT \"\",comment TEXT DEFAULT \"\")");
        db.execSQL("CREATE TABLE chat(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT DEFAULT \"\",url TEXT DEFAULT \"\",account TEXT DEFAULT \"\",password TEXT DEFAULT \"\",comment TEXT DEFAULT \"\")");
        db.execSQL("CREATE TABLE game(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT DEFAULT \"\",url TEXT DEFAULT \"\",account TEXT DEFAULT \"\",password TEXT DEFAULT \"\",comment TEXT DEFAULT \"\")");
        db.execSQL("CREATE TABLE mail(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT DEFAULT \"\",url TEXT DEFAULT \"\",account TEXT DEFAULT \"\",password TEXT DEFAULT \"\",comment TEXT DEFAULT \"\")");
        db.execSQL("CREATE TABLE pay(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT DEFAULT \"\",url TEXT DEFAULT \"\",account TEXT DEFAULT \"\",password TEXT DEFAULT \"\",comment TEXT DEFAULT \"\")");
        db.execSQL("CREATE TABLE software(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT DEFAULT \"\",url TEXT DEFAULT \"\",account TEXT DEFAULT \"\",password TEXT DEFAULT \"\",comment TEXT DEFAULT \"\")");
        db.execSQL("CREATE TABLE other(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT DEFAULT \"\",url TEXT DEFAULT \"\",account TEXT DEFAULT \"\",password TEXT DEFAULT \"\",comment TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
