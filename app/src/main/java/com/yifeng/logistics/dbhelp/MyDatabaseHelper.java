package com.yifeng.logistics.dbhelp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Administrator on 2017-03-11.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_HISTORY = "create table History(ID integer primary key autoincrement,History_Text text)";
    private static final String CREATE_USER = "create table User(ID integer primary key autoincrement,UserName text,NickName text,HeadPic text)";
    private static final String CREATE_PersonalSet = "create table PersonalSet(User text,State int,VID text,StartTime text,EndTime text,Disturb int)";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_PersonalSet);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
