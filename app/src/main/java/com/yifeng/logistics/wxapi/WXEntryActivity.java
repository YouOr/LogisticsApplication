package com.yifeng.logistics.wxapi;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yifeng.logistics.Base.BaseActivity;
import com.yifeng.logistics.R;
import com.yifeng.logistics.activity.MainActivity;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.dbhelp.MyDatabaseHelper;
import com.yifeng.logistics.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/2.
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    private String APP_ID = "wx0b91c4502ca92fe8";// 微信开放平台申请到的app_id
    private String APP_SECRET = "856389f5f839d0d6279cc71662bcca4f";//微信平台申请的app secret
    private boolean accesss_token_effective;//access_token是否有效
    private String openId = "";//openId
    private String refresh_token;
    private String access_token = "";
    private String unionid;
    private int dataType = 0;
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(R.color.white);
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.handleIntent(getIntent(), this);
        //必须将应用注册到微信
        api.registerApp(APP_ID);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                getWeChatTagAPI(APP_ID, APP_SECRET, code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                result = "发送返回";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();

                finish();
                break;
        }
    }

    @Override
    public void getResult(String result, int type) {
        super.getResult(result, type);
        if (type == Constant.API_REQUEST_SUCCESS & !TextUtils.isEmpty(result)) {
            try {
                JSONObject jsb = new JSONObject(result);
                if (dataType == 1) {
                    Constant.openid = jsb.getString("openid");
                    Constant.nickname = jsb.getString("nickname");
                    Constant.language = jsb.getString("language");
                    Constant.city = jsb.getString("city");
                    Constant.province = jsb.getString("province");
                    Constant.country = jsb.getString("country");
                    Constant.headimgurl = jsb.getString("headimgurl");
                    dataType = 3;
                    LoginAPI(jsb.getString("openid"), "111");

                } else if (dataType == 0) {
                    access_token = jsb.getString("access_token");
                    refresh_token = jsb.getString("refresh_token");
                    openId = jsb.getString("openid");
                    unionid = jsb.getString("unionid");
                    dataType = 1;
                    getWeChatInfoAPI(access_token, openId);
                } else {
                    try {
                        dbHelper = new MyDatabaseHelper(WXEntryActivity.this, "Logistics_History", null, 1);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        ContentValues value = new ContentValues();
                        values.put("UserName", Constant.openid);
                        values.put("NickName", Constant.nickname);
                        values.put("HeadPic", Constant.headimgurl);
                        value.put("User", Constant.openid);
                        value.put("State", 0);
                        value.put("Disturb", 0);
                        value.put("VID", jsb.getString("VID"));
                        value.put("StartTime", "18:00");
                        value.put("EndTime", "09:00");
                        db.insert("User", null, values);
                        Cursor cursor = db.query("PersonalSet", new String[]{"User"}, null, null, null, null, null, null);
                        if(cursor.getCount()==0)
                        {
                            db.insert("PersonalSet", null, value);
                        }else
                        {
                            ContentValues value11 = new ContentValues();
                            value11.put("VID", jsb.getString("VID"));
                            db.update("PersonalSet", value11, null, null);
                        }
                        Constant.CURRENT_ID = jsb.getInt("ID");
                        Constant.vehicleNo=jsb.getString("VID");
                        Constant.headPic=jsb.getString("HeadImage");
                        Constant.vPassWord=jsb.getString("PassWord");
                        Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.DisplayToast(this, Constant.TOAST_MEG_ANALYZE_ERROR);
            }
        }
    }
}
