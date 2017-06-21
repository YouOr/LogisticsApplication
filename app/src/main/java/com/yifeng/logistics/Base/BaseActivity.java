package com.yifeng.logistics.Base;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.callback.GetResultCallBack;
import com.yifeng.logistics.network.AppClient;
import com.yifeng.logistics.util.NetWorkUtil;
import com.yifeng.logistics.util.SystemBarTintManager;
import com.yifeng.logistics.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * BaseActivity
 */
public class BaseActivity extends Activity implements GetResultCallBack {

    public ImageView back_iv;
    public TextView title_tv;
    public TextView right_tv;

    public LayoutInflater layoutInflater;
    public View view;

    public Dialog loadDialog;
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public void setBaseTitle(String mTitle, int rTitle) {
        title_tv.setText(mTitle);
        if (rTitle == 0) {
        } else {
            right_tv.setVisibility(View.VISIBLE);
            right_tv.setText(rTitle);
        }
    }


    /*
     * 设置状态栏背景状态
	 */
    public void setTranslucentStatus(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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

    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] bt = baos.toByteArray();
        String photoStr = byte2hex(bt);
        return photoStr;

    }

    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }


        }
        return sb.toString();
    }
    public static Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    public static Bitmap upImage(String path) throws FileNotFoundException {
        InputStream is = new FileInputStream(path);
        //2.为位图设置100K的缓存
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTempStorage = new byte[100 * 1024];

        //3.设置位图颜色显示优化方式
        //ALPHA_8：每个像素占用1byte内存（8位）
        //ARGB_4444:每个像素占用2byte内存（16位）
        //ARGB_8888:每个像素占用4byte内存（32位）
        //RGB_565:每个像素占用2byte内存（16位）
        //Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
//        opts.inPurgeable = true;
        //5.设置位图缩放比例
        //width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
        opts.inSampleSize = 4;
        //6.设置解码位图的尺寸信息
//        opts.inInputShareable = true;
        //7.解码位图
        Bitmap btp = BitmapFactory.decodeStream(is, null, opts);
        return btp;
    }
    public static String File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = byte2hex(buffer);
        return data;
    }
    public static String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }
    private void isNetConnect() {
        if (NetWorkUtil.isNetWorkConnected(this)) {
        } else if (this != null) {
            ToastUtil.DisplayToast(this, Constant.TOAST_MEG_NETWORK_NULL);
        }
    }


    @Override
    public void getResult(String result, int type) {
        Log.i(TAG, "BaseActivity,result=" + result + ",type=" + type);
        if (type == Constant.API_REQUEST_FAILURE) {
            ToastUtil.DisplayToast(this, Constant.TOAST_MEG_NETWORK_ERROR, Toast.LENGTH_LONG);
        }
        if (TextUtils.isEmpty(result)) {
            ToastUtil.DisplayToast(this, Constant.TOAST_MEG_EMPTY_DATA);
        }
    }

    public void getWeChatInfoAPI(String access_token, String openid) {
        isNetConnect();
        AppClient.getWeChatInfo(access_token, openid, this);
    }

    public void getWeChatTagAPI(String appid, String secret, String code) {
        isNetConnect();
        AppClient.getWeChatTag(appid, secret, code, this);
    }

    public void UpdateVehicleNoAPI(String userName, String vID, String newVid) {
        isNetConnect();
        AppClient.UpdateVehicleNo(userName, vID, newVid, this);
    }
    public void upImgAPI(String imgname, String imgdata){
        isNetConnect();
        AppClient.upImg(imgname, imgdata, BaseActivity.this);
    }
    public  void AddSpeechAPI(String UserName, String Text,String ImageSrc,String RecordSrc,String RecordTime,String Device,String RecordFile) {
        isNetConnect();
        AppClient.AddSpeech(UserName, Text, ImageSrc, RecordSrc, RecordTime, Device, RecordFile, BaseActivity.this);
    }
    public  void LoginAPI(String UserName, String RegistrationID) {
        isNetConnect();
        AppClient.Login(UserName, RegistrationID, BaseActivity.this);
    }
    public void getGetCommunicationInfAPI(String userid,String routeno)
    {
        isNetConnect();
        AppClient.getGetCommunicationInf(userid, routeno, this);
    }
    public void downloadSpeFileAPI(String url,String destFileDir){
        isNetConnect();
        AppClient.downloadSpeFile(url, destFileDir, this);
    }
}
