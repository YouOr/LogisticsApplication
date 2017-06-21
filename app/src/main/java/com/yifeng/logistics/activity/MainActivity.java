package com.yifeng.logistics.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yifeng.logistics.Base.BaseFragmentActivity;
import com.yifeng.logistics.R;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.dbhelp.MyDatabaseHelper;
import com.yifeng.logistics.fragment.FirstFragment;
import com.yifeng.logistics.fragment.SecondFragment;
import com.yifeng.logistics.fragment.ThirdFragment;
import com.yifeng.logistics.layout.NoScrollViewPager;
import com.yifeng.logistics.service.RunSS;
import com.yifeng.logistics.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {
    public FragmentPagerAdapter mAdapter;
    public List<Fragment> mFragments = new ArrayList();
    private FirstFragment mTab01;
    private SecondFragment mTab02;
    private ThirdFragment mTab03;
    private long exitTime = 0;
    private RelativeLayout rl_one, rl_two, rl_three;
    private TextView t1,t2,t3;
    private NoScrollViewPager containerViewPager;
    private MyDatabaseHelper dbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTranslucentStatus(R.color.blue);
        init();
        updateServiceType();
        isIgnoreBatteryOption(MainActivity.this);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        containerViewPager.setAdapter(mAdapter);
        containerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setTabSelection(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setTabSelection(0);
    }
    public void updateServiceType()
    {
        dbHelper = new MyDatabaseHelper(MainActivity.this, "Logistics_History", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PersonalSet", new String[]{"State"}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Constant.State = cursor.getInt(cursor.getColumnIndex("State"));
        }
        Intent intentService=new Intent(this,RunSS.class);

        if(Constant.State==0)
        {
            Log.i("fdsfds",Constant.State+"--");
//            intentService.putExtra("vID", Constant.vehicleNo);
            startService(intentService);

        }else
        {
            Log.i("fdsfds",Constant.State+"--");
            stopService(intentService);
        }
    }
    /**
     * 针对N以上的Doze模式
     *
     * @param activity
     */
    public static void isIgnoreBatteryOption(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//               intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    activity.startActivityForResult(intent, 12);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setLike(int id,int orderone)
    {
        mTab01.Oklike(id, orderone);
    }
    public void showDiscuss(int id)
    {
        mTab01.showEdt(id);
    }
    public void openMedia(String url)
    {
        mTab01.startMedia(url);
    }
    public void closeMedia()
    {
        mTab01.closeMedia();
    }
    public void deleteMessageM(String num)
    {
        mTab01.deleteMessageB(num);
    }
    private void init() {
        rl_one = (RelativeLayout) findViewById(R.id.txt_fm_one);
        rl_two = (RelativeLayout) findViewById(R.id.txt_fm_two);
        rl_three = (RelativeLayout) findViewById(R.id.txt_fm_three);
        t1= (TextView) findViewById(R.id.one_txt);
        t2= (TextView) findViewById(R.id.two_txt);
        t3= (TextView) findViewById(R.id.three_txt);
        containerViewPager = (NoScrollViewPager) findViewById(R.id.containerViewPager);
//        containerViewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
        rl_one.setOnClickListener(this);
        rl_two.setOnClickListener(this);
        rl_three.setOnClickListener(this);

        mTab01 = new FirstFragment();
        mTab02 = new SecondFragment();
        mTab03 = new ThirdFragment();
        mFragments.add(mTab01);
        mFragments.add(mTab02);
        mFragments.add(mTab03);

    }

    private void setTabSelection(int index) {
        switch (index) {
            case 0:
                containerViewPager.setCurrentItem(0, false);
                changeBottomStyle();
                t1.setTextColor(ContextCompat.getColor(this, R.color.black));
                break;
            case 1:
                containerViewPager.setCurrentItem(1, false);
                changeBottomStyle();
                t2.setTextColor(ContextCompat.getColor(this, R.color.black));
                break;
            case 2:
                containerViewPager.setCurrentItem(2, false);
                changeBottomStyle();
                t3.setTextColor(ContextCompat.getColor(this, R.color.black));
                break;
        }
    }
    private void changeBottomStyle()
    {
        t1.setTextColor(ContextCompat.getColor(this,R.color.white));
        t2.setTextColor(ContextCompat.getColor(this, R.color.white));
        t3.setTextColor(ContextCompat.getColor(this, R.color.white));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        //监听返回
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.DisplayToast(this, "再按一次退出！");
                exitTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
            return true;
        }
        //监听音量-
        else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            audio.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);

            return  true;

        }
        //监听音量+
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            audio.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            return true;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_fm_one:
                setTabSelection(0);
                break;
            case R.id.txt_fm_two:
                setTabSelection(1);
                break;
            case R.id.txt_fm_three:
                setTabSelection(2);
                break;
        }
    }

}
