package com.yifeng.logistics.bean;

/**
 * Created by ssm on 2015/12/8.
 */
public class Constant {
        public static String  APP_ID="wx0b91c4502ca92fe8";
        public static int CURRENT_ID = 52;
        public static String openid = "";
        public static String nickname = "";
        public static String language = "";
        public static String city = "";
        public static String province = "";
        public static String country = "";
        public static String headimgurl = "";
        public static String vehicleNo="";
        public static String headPic="";
        public static int INFO_TYPE=0;
        public static int State=0;
        public static String vPassWord="";


        public static int CURRENT_Order = 0;
        public static final String SP_AVATAR_NAME = "sp_avatar_name";
        public static final String SP_AVATAR_PATH = "sp_avatar_path";
        public static final String INTENT_AVATAR_PATH = "intent_avatar_path";
        //是否登陆成功
        public static Boolean IS_LOGIN = false;

        public static int keyboardHeight=0;

        //用户类型，用户？业代?
        public static Boolean IS_EMP = true;

        public static final String CONFIG_INTENT_TITLE_NAME = "CONFIG_INTENT_TITLE_NAME";
        public static final String CONFIG_INTENT_LOAD_URL = "CONFIG_INTENT_LOAD_URL";

        public static final String TOAST_MEG_ERROR_EMPTY = "填写信息不能为空!";
        public static final String TOAST_MEG_DIFFERENT_PASS = "两次密码不一致!";
        public static final String TOAST_MEG_ORINGE_PASS_ERROR = "原始密码不正确!";
        public static final String TOAST_MEG_PASS_LENGTH = "密码长度6-18位!";

        public static final String TOAST_MEG_LOGIN_SUCCESS = "登陆成功!";


        public static final String TOAST_MEG_EMPTY_DATA = "无数据返回,请重试!";
        public static final String TOAST_MEG_ERROR_PASS = "两次密码不一致或密码长度小于6位!";
        public static final String TOAST_MEG_ERROR_ORGIN = "原密码错误!";
        public static final String TOAST_MEG_MODIFY_SUCCESS = "修改密码成功,退出重新登录!";
        public static final String TOAST_MEG_MODIFY_FAIL = "修改密码失败!";

        public static final String TOAST_MEG_NETWORK_NULL = "当前网络未连接!";
        public static final String TOAST_MEG_NETWORK_ERROR = "网络请求异常!";
        public static final String TOAST_MEG_ANALYZE_ERROR = "数据解析异常!";
        public static final String TOAST_MEG_NO_MORE_DATA = "已经到底啦!";

        public static final int MODIFY_REPORT_SUCCESS_RESULTCODE = 1;


        //    public static int API_ANALYZE_SUCCESS = 201;
//    public static int API_ANALYZE_FAILURE = 202;
        public static int API_REQUEST_SUCCESS = 200;
        public static int API_REQUEST_FAILURE = 404;

        public static int API_REQUEST_CODE_SUCCESS = 201;
        public static int API_ERROR_REBACK = 204;//错误的返回内容

        public static final String PREFERRENTCE_IS_REMENEBER_PASS = "PREFERRENTCE_IS_REMENEBER_PASS";





}
