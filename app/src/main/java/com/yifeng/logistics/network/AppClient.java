package com.yifeng.logistics.network;

import android.util.Log;

import com.squareup.okhttp.Request;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.callback.GetResultCallBack;
import com.yifeng.logistics.util.OkHttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ssm
 */
public class AppClient {


    public static GetResultCallBack mCallback;
    public static final String TAG = "AppClient";
    public static final String BASE_URL = "http://app.simingtang.com/app";
    public static final String KEY = "B2F145B12F23554185991E471C9C0BB11";
    //URL
    public static final String API_POST_BASE_URL = "http://115.159.125.251/ashx/LogisticsApp.ashx?";
    //获取排行信息
    public static final String API_GET_RanKingInfo = "http://139.196.173.107/ashx/app0.ashx?key=%s&method=NewRanking&Order=%s";
    //获取微信用户标记
    public static final String API_GET_Tag ="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    //获取微信用户信息
    public static final String API_GET_WeChatInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    //获取交流圈信息
    public static final String API_GET_CommunicationInf="http://115.159.125.251/ashx/LogisticsApp.ashx?key=%s&method=GetCommunicationInf&UserID=%s&PageNo=%s";
    //获取交流圈信息
    public static final String API_GET_VehicleInfo="http://115.159.125.251/ashx/LogisticsApp.ashx?key=%s&method=GetVehicleInfo&VID=%s";

    //登录
    public static void Login(String UserName, String RegistrationID, GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "CheckUser");
        map.put("UserName",UserName);
        map.put("RegistrationID", RegistrationID);
        post(API_POST_BASE_URL, map);
    }

    public static void getRanKingInfo( String order, GetResultCallBack callBack) {
        mCallback = callBack;
        String URL = "";
        URL = String.format(API_GET_RanKingInfo,KEY,order);
        get(URL);
    }
    //获取微信用户标记
    public static void getWeChatTag( String appid,String secret,String code, GetResultCallBack callBack) {
        mCallback = callBack;
        String URL = "";
        URL = String.format(API_GET_Tag,appid,secret,code);
        get(URL);
    }
    //获取微信用户信息
    public static void getWeChatInfo( String access_token,String openid, GetResultCallBack callBack) {
        mCallback = callBack;
        String URL = "";
        URL = String.format(API_GET_WeChatInfo,access_token,openid);
        get(URL);
    }
    //下载spx压缩文件
    public static void downloadSpeFile(String url,String destFileDir,GetResultCallBack callBack)
    {
        mCallback = callBack;
        downloadAsyn(url, destFileDir);
    }
    //上传spx压缩文件
    public static void upFile(String Date, String Name,  GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "UpMediaFile");
        map.put("Date", Date);
        map.put("Name", Name);
        post(API_POST_BASE_URL, map);
    }
    public static void getGetCommunicationInf( String userid,String routeno, GetResultCallBack callBack) {
        mCallback = callBack;
        String URL = "";
        URL = String.format(API_GET_CommunicationInf,KEY,userid,routeno);
        get(URL);
    }
    public static void getVehicleInfo( String vid, GetResultCallBack callBack) {
        mCallback = callBack;
        String URL = "";
        URL = String.format(API_GET_VehicleInfo,KEY,vid);
        get(URL);
    }
    //提交点赞
    public static void ExecuteLike(String UserID, String ID,String Order,String Type, GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "ExecuteLike");
        map.put("UserID",UserID);
        map.put("ID", ID);
        map.put("Order", Order);
        map.put("Type", Type);
        post(API_POST_BASE_URL, map);
    }
    //提交评论
    public static void SubmitAnnounce(String VID, String UserID,String Discuss, GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "SubmitAnnounce");
        map.put("VID",VID);
        map.put("UserID", UserID);
        map.put("Discuss", Discuss);
        post(API_POST_BASE_URL, map);
    }
    //修改车牌号
    public static void UpdateVehicleNo(String UserName, String VID,String NewVID, GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "UpdateVehicleNo");
        map.put("UserName",UserName);
        map.put("VID", VID);
        map.put("NewVID", NewVID);
        post(API_POST_BASE_URL, map);
    }
    //上传图片
    public static void upImg(String imgname,String imgdata, GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "uploadingImgAndroid");
        map.put("imageData",imgdata);
        map.put("imgName",imgname);
        post(API_POST_BASE_URL, map);
    }
    //上传位置
    public static void upLocation(String vID,String longitude,String latitude) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "UpLocation");
        map.put("VID",vID);
        map.put("Longitude",longitude);
        map.put("Latitude",latitude);
        map.put("Device","Android");
        post(API_POST_BASE_URL, map);
    }
    //发布信息
    public static void AddSpeech(String UserName, String Text,String ImageSrc,String RecordSrc,String RecordTime,String Device,String RecordFile, GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "AddSpeech");
        map.put("UserName",UserName);
        map.put("Text", Text);
        map.put("ImageSrc", ImageSrc);
        map.put("RecordSrc", RecordSrc);
        map.put("RecordTime", RecordTime);
        map.put("Device", Device);
        map.put("RecordFile", RecordFile);
        post(API_POST_BASE_URL, map);
    }
    //删除信息
    public static void delteMessage(String ID,GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "DeleteMessage");
        map.put("ID",ID);
        post(API_POST_BASE_URL, map);
    }
    //删除信息
    public static void updateVehicleState(String VID,String State,GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "UpdateVehicleState");
        map.put("VID",VID);
        map.put("State",State);
        post(API_POST_BASE_URL, map);
    }
    //加密车辆信息
    public static void updateVehiclePassWord(String VID,String PassWord,GetResultCallBack callback) {
        mCallback = callback;
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", KEY);
        map.put("method", "UpdateVehiclePassWord");
        map.put("VID",VID);
        map.put("PassWord",PassWord);
        post(API_POST_BASE_URL, map);
    }
    public static void get(String url) {
        String REQUEST_URL = url;
        Log.i(TAG, "get.url=" + url);
        OkHttpClientManager.getAsyn(REQUEST_URL, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                mCallback.getResult(request.toString(), Constant.API_REQUEST_FAILURE);
            }

            @Override
            public void onResponse(String u) {
                mCallback.getResult(u, Constant.API_REQUEST_SUCCESS);
            }
        });
    }
    public static void downloadAsyn(String url,String destFileDir)
    {
        String REQUEST_URL = url;
        Log.i(TAG, "get.url=" + url);
        OkHttpClientManager.DownloadDelegate.downloadAsyn(REQUEST_URL, destFileDir, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                mCallback.getResult(request.toString(), Constant.API_REQUEST_FAILURE);
            }

            @Override
            public void onResponse(String u) {
                mCallback.getResult(u, Constant.API_REQUEST_SUCCESS);
            }
        });
    }

    public static void get(String url, final int type) {
        String REQUEST_URL = url;
        Log.i(TAG, "get.url=" + url);
        OkHttpClientManager.getAsyn(REQUEST_URL, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                Log.i(TAG, "get.onError=" + request.toString());
                mCallback.getResult(request.toString(), Constant.API_REQUEST_FAILURE);
            }

            @Override
            public void onResponse(String u) {
                Log.i(TAG, "get.onResponse=" + u);
                mCallback.getResult(u, type);
            }
        });
    }

    public static void post(String url, Map<String, String> map) {
        Log.i(TAG, "post,url=" + url + ",map=" + map);
        OkHttpClientManager.postAsyn(url, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.i(TAG, "Error=" + request.toString());
                mCallback.getResult(request.toString(), Constant.API_REQUEST_FAILURE);
            }

            @Override
            public void onResponse(String response) {
                try {
//                    if (AppClient.isParse(response)) {
                    Log.i(TAG, "onResponse1=" + response.toString());
                    mCallback.getResult(response, Constant.API_REQUEST_SUCCESS);
                   /* } else {
                        Log.i(TAG, "onResponse2=" + response.toString());
                        mCallback.getResult(response, Constant.API_ERROR_REBACK);
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void post(String url, Map<String, String> map, final int type) {
        Log.i(TAG, "post,url=" + url + ",map=" + map);
        OkHttpClientManager.postAsyn(url, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.i(TAG, "Error=" + request.toString());
                mCallback.getResult(request.toString(), Constant.API_REQUEST_FAILURE);
            }

            @Override
            public void onResponse(String response) {
                try {
//                    if (AppClient.isParse(response)) {
                    Log.i(TAG, "onResponse1=" + response.toString());
                    mCallback.getResult(response, type);
                    /*} else {
                        Log.i(TAG,"onResponse2="+response.toString());
                        mCallback.getResult(response, Constant.API_ERROR_REBACK);
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    ////////判断是否解析
    public static boolean isParse(String response) {
        JSONObject object = null;
        boolean result = false;

        try {
            object = new JSONObject(response);
//            int status = object.getInt("ID");
            if (object.has("ID")) {
                return true;
            } else {
                return false;
            }
           /* if (1 == status) {
                return true;
            } else {
                return false;
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
//            ContentUtil.makeLog("zy", "json解析错误");
        }
        return result;
    }


}
