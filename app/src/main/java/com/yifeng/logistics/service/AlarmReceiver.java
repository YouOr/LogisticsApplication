package com.yifeng.logistics.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yifeng.logistics.dbhelp.MyDatabaseHelper;

/**
 * Created by why on 2016/4/20.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private String action = null;
    private MyDatabaseHelper dbHelper;
    private int sss=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper = new MyDatabaseHelper(context, "Logistics_History", null, 1);
         SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PersonalSet", new String[]{"State"}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            sss=cursor.getInt(cursor.getColumnIndex("State"));
        }
        if(sss==0) {
            Intent i = new Intent(context, RunSS.class);
            context.startService(i);
            Log.i("fdsfs", "收到广播");
        }
    }
}
