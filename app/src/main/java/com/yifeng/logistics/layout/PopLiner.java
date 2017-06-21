package com.yifeng.logistics.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017-01-08.
 */
public class PopLiner extends LinearLayout {
    public PopLiner(Context context) {
        super(context);
    }

    public PopLiner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopLiner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PopLiner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //when the softinput display
            //处理事件

        }
        return super.dispatchKeyEventPreIme(event);
    }
}
