package com.yifeng.logistics.Base;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yifeng.logistics.util.SystemBarTintManager;


/**
 * BaseActivity
 */
public class BaseFragmentActivity extends FragmentActivity {

    public ImageView back_iv;
    public TextView title_tv;
    public TextView right_tv;

    public LayoutInflater layoutInflater;
    public View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBaseView();
    }

    public void initBaseView(){
//        back_iv = (ImageView)findViewById(R.id.top_back_iv);
//        title_tv = (TextView)findViewById(R.id.top_title_tv);
//        right_tv = (TextView)findViewById(R.id.top_right_tv);

    }

    /*
     * 结束事件
     */
    protected void initBackButton(int resid) {
        ImageView btnBack = (ImageView) findViewById(resid);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*
     * 设置标题
     */
    public void setBaseTitle(int mTitle,int rTitle){
        title_tv.setText(mTitle);
        if(rTitle == 0){
        }else{
            right_tv.setVisibility(View.VISIBLE);
            right_tv.setText(rTitle);
        }
    }

    /*
	 * 设置状态栏背景状态
	 */
    public void setTranslucentStatus(int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);//状态栏无背景
    }

}
