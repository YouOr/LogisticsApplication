package com.yifeng.logistics.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.callback.GetResultCallBack;
import com.yifeng.logistics.network.AppClient;
import com.yifeng.logistics.util.NetWorkUtil;
import com.yifeng.logistics.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public abstract class BaseFragment extends Fragment implements GetResultCallBack {
//	@InjectView(R.id.top_title_tv)
//	TextView top_title_tv;

    private String TAG = "BaseFragment";

    public View view;
    private Context mContext;

    public Dialog loadDialog;
    public TextView top_title_tv;

    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        return view;
    }

    private void isNetConnect() {
        if (NetWorkUtil.isNetWorkConnected(getActivity())) {

        } else if (getActivity() != null) {
            ToastUtil.DisplayToast(getActivity(), Constant.TOAST_MEG_NETWORK_NULL);
        }
    }

    @Override
    public void getResult(String result, int type) {
        Log.i(TAG, "BaseFragment,result=" + result + ",type=" + type);
        if (type == Constant.API_REQUEST_FAILURE) {
            ToastUtil.DisplayToast(getActivity(), Constant.TOAST_MEG_NETWORK_ERROR, Toast.LENGTH_LONG);
        }
        if (type == Constant.API_ERROR_REBACK) {

        }
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
    public void cloesKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),
                    0);
        }
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
    public void getRanKingInfoAPI(String order){
        isNetConnect();
        AppClient.getRanKingInfo(order, this);
    }
    public void downloadSpeFileAPI(String url,String destFileDir){
        isNetConnect();
        AppClient.downloadSpeFile(url, destFileDir, this);
    }
    public void upFileAPI(String Data,String Name){
        isNetConnect();
        AppClient.upFile(Data, Name, this);
    }
 public void getGetCommunicationInfAPI(String userid,String routeno)
 {
     isNetConnect();
     AppClient.getGetCommunicationInf(userid, routeno, this);
 }
    public void getVehicleInfoAPI(String vid)
    {
        isNetConnect();
        AppClient.getVehicleInfo(vid, this);
    }
    public void ExecuteLikeAPI(String UserID, String ID,String Order,String Type){
        isNetConnect();
        AppClient.ExecuteLike(UserID, ID, Order, Type, this);
    }
    public void SubmitAnnounceAPI(String VID, String UserID,String Discuss){
        isNetConnect();
        AppClient.SubmitAnnounce(VID, UserID, Discuss, this);
    }
    public void DeleteMessageAPI(String ID){
        isNetConnect();
        AppClient.delteMessage(ID, this);
    }
    public void updateVehicleStateAPI(String vID,String state){
        isNetConnect();
        AppClient.updateVehicleState(vID, state, this);
    }
    public void updateVehiclePassWordAPI(String vID,String passWord){
        isNetConnect();
        AppClient.updateVehiclePassWord(vID, passWord, this);
    }

}
