package com.yifeng.logistics.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yifeng.logistics.dbhelp.MyDatabaseHelper;
import com.yifeng.logistics.network.AppClient;
import com.yifeng.logistics.util.NetWorkUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/22.
 */
public class RunSS extends Service {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new LocationListener();
    PowerManager.WakeLock wakeLock = null;
    String vID = "";
    Date startT, endT, nowT;
    private MyDatabaseHelper dbHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//创建点击跳转的Intent
//        Intent foregroundIntent = new Intent(RunSS.this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, foregroundIntent, 0);
//        //构建通知
//        Notification.Builder builder = new Notification.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("马上到")
//                .setContentText("处于定位状态，如要取消请隐身")
//                .setContentIntent(pendingIntent);//设置跳转的Intent到通知中
//        //构建通知
//        Notification notification = builder.build();
//        startForeground(1, notification);
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, RunSS.class.getName());
//        wakeLock.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        dbHelper = new MyDatabaseHelper(RunSS.this, "Logistics_History", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PersonalSet", new String[]{"VID", "StartTime", "EndTime", "Disturb",}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                vID = cursor.getString(cursor.getColumnIndex("VID"));
                if (cursor.getInt(cursor.getColumnIndex("Disturb")) == 0) {
                    startT = df.parse("18:00");
                    endT = df.parse("09:00");
                } else {
                    startT = df.parse(cursor.getString(cursor.getColumnIndex("StartTime")));
                    endT = df.parse(cursor.getString(cursor.getColumnIndex("EndTime")));
                }
                nowT = df.parse(df.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (nowT.getTime() <= startT.getTime() && nowT.getTime() > endT.getTime()) {
            if (NetWorkUtil.isNetWorkConnected(RunSS.this)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setMap();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }else
            {
                Log.i("RunSS","没有网络");
            }
        }
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int aMin = 5 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + aMin;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        if (Build.VERSION.SDK_INT > 22) {
            manager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        } else {
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }
        flags = START_STICKY ;
        return super.onStartCommand(intent, flags, startId);
    }

    public class LocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            AppClient.upLocation(vID, String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
            mLocationClient.stop();

            if (wakeLock != null) {
                wakeLock.release();
                wakeLock = null;
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    private void setMap() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        int span = 0;
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(false);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(option);
        //开始定位
        mLocationClient.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}
