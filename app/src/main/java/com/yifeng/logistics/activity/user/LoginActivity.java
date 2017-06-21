package com.yifeng.logistics.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yifeng.logistics.Base.BaseActivity;
import com.yifeng.logistics.R;
import com.yifeng.logistics.bean.Constant;

/**
 * Created by Administrator on 2017/3/1.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout login_weChat, login_qq;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        setContentView(R.layout.layout_login);
        init();
    }

    private void init() {
        login_weChat = (LinearLayout) findViewById(R.id.login_weChat);
        login_qq = (LinearLayout) findViewById(R.id.login_qq);

        login_weChat.setOnClickListener(this);
        login_qq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_weChat:
                startWeChat();
                finish();
                break;
            case R.id.login_qq:

                break;
            default:
        }
    }

    private void startWeChat() {
        if (api != null && api.isWXAppInstalled()) {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "none";
            api.sendReq(req);
        } else {
            Toast.makeText(this, "用户未安装微信", Toast.LENGTH_SHORT).show();
        }
    }
}
