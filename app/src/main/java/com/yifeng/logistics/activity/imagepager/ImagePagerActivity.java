package com.yifeng.logistics.activity.imagepager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yifeng.logistics.R;


public class ImagePagerActivity extends FragmentActivity {
    ImageViewPagerAdapter adapter;
    TextView tv1;
    HackyViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_pager);
        tv1= (TextView) findViewById(R.id.indicator);
        pager = (HackyViewPager) findViewById(R.id.pager);
        tv1.setText(Integer.parseInt(getIntent().getStringExtra("No"))+1 + "/" + getIntent().getStringArrayListExtra("ItemNo").size());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv1.setText(position + 1 + "/" + getIntent().getStringArrayListExtra("ItemNo").size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), getIntent().getStringArrayListExtra("ItemNo"));
        pager.setAdapter(adapter);
        pager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("No")));
    }

}
