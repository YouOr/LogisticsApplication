package com.yifeng.logistics.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


/**
 * Created by Pan_ on 2015/2/2.
 */
public class NineGridlayout extends GridView {


    public NineGridlayout(Context context) {
        super(context);
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NineGridlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
