package com.yifeng.logistics.activity.user;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.yifeng.logistics.Base.BaseActivity;
import com.yifeng.logistics.R;
import com.yifeng.logistics.activity.MainActivity;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.dbhelp.MyDatabaseHelper;
import com.yifeng.logistics.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/17.
 */
public class StartActivity extends BaseActivity {
    private MyDatabaseHelper dbHelper;
    private ImageView welcomeImg = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_start_action);
        setTranslucentStatus(R.color.white);
        welcomeImg = (ImageView) this.findViewById(R.id.welcome_img);
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(2500);// 设置动画显示时间
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());

    }
    private class AnimationImpl implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            welcomeImg.setBackgroundResource(R.drawable.welcome_img);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            init(); // 动画结束后跳转到别的页面
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }
    private void init() {
        if (qureyUser() == 0) {
            Intent intent=new Intent(StartActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            LoginAPI(Constant.openid, "111");
        }
    }

    private int qureyUser() {
        dbHelper = new MyDatabaseHelper(StartActivity.this, "Logistics_History", null, 1);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("User", new String[]{"UserName", "NickName", "HeadPic"}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Constant.openid = cursor.getString(cursor.getColumnIndex("UserName"));
            Constant.nickname = cursor.getString(cursor.getColumnIndex("NickName"));
            Constant.headimgurl = cursor.getString(cursor.getColumnIndex("HeadPic"));
        }
        return cursor.getCount();
    }
    @Override
    public void getResult(String result, int type) {
        super.getResult(result, type);
        if (type == Constant.API_REQUEST_SUCCESS & !TextUtils.isEmpty(result)) {
            try {
                JSONObject jsb = new JSONObject(result);
                Constant.CURRENT_ID=jsb.getInt("ID");
                Constant.vehicleNo=jsb.getString("VID");
                Constant.headPic=jsb.getString("HeadImage");
                Constant.vPassWord=jsb.getString("PassWord");
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.DisplayToast(StartActivity.this, Constant.TOAST_MEG_ANALYZE_ERROR);
            }
        }
    }
}
