package com.yifeng.logistics.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.yifeng.logistics.R;
import com.yifeng.logistics.activity.MainActivity;
import com.yifeng.logistics.activity.media.AddMessage;
import com.yifeng.logistics.activity.user.UpdateVehicle;
import com.yifeng.logistics.adapter.cFriendsAdapter;
import com.yifeng.logistics.bean.Communication;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.dbhelp.MyDatabaseHelper;
import com.yifeng.logistics.layout.PopLiner;
import com.yifeng.logistics.layout.SwipeListLayout;
import com.yifeng.logistics.layout.Switch;
import com.yifeng.logistics.util.AudioFileUtils;
import com.yifeng.logistics.util.CommonUtils;
import com.yifeng.logistics.util.PlayerUtils;
import com.yifeng.logistics.util.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/27.
 */
public class FirstFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private Dialog mWeiboDialog;
    private TextView loot, txt_UdNo, myLicensePlate,vPassWord;
    private ImageView headPic, img_add;
    private Switch sh_State;
    private PopLiner editTextBodyLl;
    private String imagePath;
    private ListView mListView;
    private TextView myName;
    private EditText circleEt;
    LinearLayout root;
    ImageView sendIv;
    private int iscik = 0;
    private List<Communication> communicationList = new ArrayList<>();
    private Set<SwipeListLayout> sets = new HashSet();
    cFriendsAdapter ap;
    private boolean hasFetchData = false;
    private int cur_page = 1;
    private int currentKeyboardH;
    private int screenHeight;
    private int editTextBodyHeight;
    private int heightDifference = 800;
    int itemid = 0;
    int y;
    private String mediaUrl = "";
    private MyDatabaseHelper dbHelper;
    PlayerUtils mPlayer;
    // 缓冲区字节大小
    private int bufferSizeInBytes = 0;
    private SwipeRefreshLayout mSwipeLayout;
    private boolean isBottom = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x111:
                    cur_page = 1;
                    ap.clearAll();
                    getData("1");
                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_one, container, false);
        headPic = (ImageView) view.findViewById(R.id.myHeadPic);
        myName = (TextView) view.findViewById(R.id.myName);
        mListView = (ListView) view.findViewById(R.id.mListView);
        sendIv = (ImageView) view.findViewById(R.id.sendIv);
        circleEt = (EditText) view.findViewById(R.id.circleEt);
        editTextBodyLl = (PopLiner) view.findViewById(R.id.editTextBodyLl);
        root = (LinearLayout) view.findViewById(R.id.root);
        loot = (TextView) view.findViewById(R.id.loot);
        txt_UdNo = (TextView) view.findViewById(R.id.txt_UdNo);
        myLicensePlate = (TextView) view.findViewById(R.id.myLicensePlate);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        img_add = (ImageView) view.findViewById(R.id.img_add);
        sh_State = (Switch) view.findViewById(R.id.sh_State);
        vPassWord= (TextView) view.findViewById(R.id.vPassWord);
        mSwipeLayout.setOnRefreshListener(this);
        mPlayer = new PlayerUtils();
        Constant.INFO_TYPE = 0;
        setAdapter();
        init();
        return view;

    }
    public void deleteMessageB(String num)
    {
        iscik=999;
        DeleteMessageAPI(num);
    }
    private void init() {
        if(Constant.vPassWord==null||Constant.vPassWord.equals(""))
        {
            vPassWord.setText("未加密");
        }else
        {
            vPassWord.setText("已加密");
        }
        vPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setNegativeButton("不加密", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                iscik=1788;
                                updateVehiclePassWordAPI(Constant.vehicleNo,"");
                                Constant.vPassWord="";
                                vPassWord.setText("未加密");
                            }
                        }
                ).setPositiveButton("加密", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText inputServer = new EditText(getActivity());
                        inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("选择加密后,其他用户在搜索到您后需输入正确密码才可以访问您的位置与公告牌.").setView(inputServer)
                                .setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                iscik=1788;
                                updateVehiclePassWordAPI(Constant.vehicleNo, inputServer.getText().toString());
                                Constant.vPassWord=inputServer.getText().toString();
                                vPassWord.setText("已加密");
                            }
                        });
                        builder.show();
                    }
                }).show();

            }
        });
        sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleEt.getText().equals("")) {
                    Toast.makeText(getActivity(), "评论不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Communication.CommunicationDetails discussDetailsList = new Communication.CommunicationDetails(Constant.nickname, circleEt.getText().toString());
                    try {
                        if (communicationList.get(itemid).getCommunicationDetailsList() == null) {
                            List<Communication.CommunicationDetails> list = new ArrayList<Communication.CommunicationDetails>();
                            list.add(discussDetailsList);
                            Communication footprintBean = new Communication(communicationList.get(itemid).getID(), communicationList.get(itemid).getName(), communicationList.get(itemid).getText(), communicationList.get(itemid).getPic(),
                                    communicationList.get(itemid).getRecord(), communicationList.get(itemid).getRecordTime(), communicationList.get(itemid).getDlike(), communicationList.get(itemid).getComTime(), list);
                            communicationList.set(itemid, footprintBean);
                            ap.notifyDataSetChanged();
                            editTextBodyLl.setVisibility(View.GONE);
                            CommonUtils.hideSoftInput(circleEt.getContext(), circleEt);
                            iscik = 1;
                            SubmitAnnounceAPI(String.valueOf(communicationList.get(itemid).getID()), String.valueOf(Constant.CURRENT_ID), circleEt.getText().toString());
                            circleEt.setText("");
                        } else {
                            communicationList.get(itemid).getCommunicationDetailsList().add(discussDetailsList);
                            ap.notifyDataSetChanged();

                            editTextBodyLl.setVisibility(View.GONE);
                            CommonUtils.hideSoftInput(circleEt.getContext(), circleEt);
                            iscik = 1;
                            SubmitAnnounceAPI(String.valueOf(communicationList.get(itemid).getID()), String.valueOf(Constant.CURRENT_ID), circleEt.getText().toString());
                            circleEt.setText("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        circleEt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                root.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = root.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.d("hhhkkk", "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);
                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }
                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = editTextBodyLl.getHeight();
                if (keyboardH < 200) {//说明是隐藏键盘的情况
                    editTextBodyLl.setVisibility(View.GONE);

                    return;
                }
                //获取屏幕的高度
                int screenHeight = getActivity().getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                heightDifference = screenHeight - r.bottom;
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mListView.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        Log.d("ListView", "##### 滚动到顶部 #####");
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mListView.getChildAt(mListView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListView.getHeight()) {
                        Log.d("ListView", "##### 滚动到底部 ######");
                        if (!isBottom) {
                            iscik = 0;
                            cur_page++;
                            getData(String.valueOf(cur_page));
                        }
                    }
                }

            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;

                }
            }
        });
        txt_UdNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateVehicle.class);
                intent.putExtra("No", myLicensePlate.getText());
                startActivity(intent);
            }
        });
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMessage.class);
                startActivity(intent);
            }
        });

        Picasso.with(getActivity()).load(Constant.headimgurl).into(headPic);
        myName.setText(Constant.nickname);
        updateServiceState();
        sh_State.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch s, boolean isChecked) {
                if (isChecked) {
                    updateUserState(1);
                } else {
                    updateUserState(0);
                }
            }
        });
    }

    private void updateUserState(int order) {
        iscik=1001;
        updateVehicleStateAPI(Constant.vehicleNo,String.valueOf(order));
        dbHelper = new MyDatabaseHelper(getActivity(), "Logistics_History", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("State", order);
        db.update("PersonalSet", values, "User = ?", new String[]{Constant.openid});
        Constant.State = order;
        ((MainActivity) getActivity()).updateServiceType();
    }

    private void updateServiceState() {
        if (Constant.State == 0) {
            sh_State.setChecked(false);
        } else {
            sh_State.setChecked(true);
        }

    }

    public void showEdt(final int position) {
        y = screenHeight - heightDifference - getStatusBarHeight() - loot.getHeight() - circleEt.getHeight();
        mListView.smoothScrollToPositionFromTop(position + 2, y);
        itemid = position;
        circleEt.requestFocus();
        editTextBodyLl.setVisibility(View.VISIBLE);
        CommonUtils.showSoftInput(circleEt.getContext(), circleEt);
    }

    public void Oklike(int id, int orderone) {
        iscik = 1;
        ExecuteLikeAPI(String.valueOf(Constant.CURRENT_ID), String.valueOf(communicationList.get(id).getID()), String.valueOf(orderone), "2");
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)//表示当前界面为可见状态
        {
            if (hasFetchData) {

            } else {
                getData("1");
            }
        } else {

        }
    }

    public void startMedia(String url) {
        iscik = 2;
        mediaUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SpeexTest/recordFile/" + getFileName(url);
        downloadSpeFileAPI(url, AudioFileUtils.getFileBasePath());
    }

    public void closeMedia() {
        mPlayer.stop();
    }

    @Override
    public void getResult(String result, int type) {
        super.getResult(result, type);
        if (type == Constant.API_REQUEST_SUCCESS & !TextUtils.isEmpty(result)) {
            try {
                if (iscik == 0) {
                    Type listType = new TypeToken<LinkedList<Communication>>() {
                    }.getType();
                    Gson gson = new Gson();
                    LinkedList<Communication> data = gson.fromJson(result, listType);
                    if (data != null) {
                        isBottom = false;
                        ap.addData(data);
                    } else {
                        ToastUtil.DisplayToast(getActivity(), Constant.TOAST_MEG_ANALYZE_ERROR);
                    }
                    mSwipeLayout.setRefreshing(false);
                } else if (iscik == 2) {
                    iscik = 0;
                    new Thread(new loadFilePlay()).start();
                }else if(iscik==1001)
                {
                    iscik=0;
                }else if(iscik==1788)
                {
                    iscik=0;
                }
                else {
                    iscik=0;
                }
            } catch (Exception e) {
                Log.i("TTSS", "Error:" + e.getMessage());
                e.printStackTrace();
            }

        }
        if (result.equals("[]")) {
            isBottom = true;
        }
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(0x111, 2000);
    }

    class loadFilePlay implements Runnable {

        @Override
        public void run() {
//            bufferSizeInBytes = AudioRecord.getMinBufferSize(AudioFileUtils.AUDIO_SAMPLE_RATE, AudioFileUtils.CHANNEL_CONFIG, AudioFileUtils.AUDIO_FORMAT);
//            AudioFileUtils.spx2Wav(mediaUrl, AudioFileUtils.getDecodeWavFilePath(), bufferSizeInBytes);
            mPlayer = new PlayerUtils();
            mPlayer.setUrlPrepare(mediaUrl);
            mPlayer.play();
        }
    }

    private void getData(String cur_page) {
        hasFetchData = true;
        getGetCommunicationInfAPI(String.valueOf(Constant.CURRENT_ID), cur_page);
    }

    private void setAdapter() {
        ap = new cFriendsAdapter(getActivity(), communicationList);
        mListView.setAdapter(ap);
    }

    private int qureyUser() {
        dbHelper = new MyDatabaseHelper(getActivity(), "Logistics_History", null, 1);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("User", new String[]{"UserName"}, null, null, null, null, null, null);
        return cursor.getCount();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayer = new PlayerUtils();
        mPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer = new PlayerUtils();
        mPlayer.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.CURRENT_Order == 1) {
            Constant.CURRENT_Order = 0;
            mHandler.sendEmptyMessageDelayed(0x111, 2000);
        }
        myLicensePlate.setText(Constant.vehicleNo);
    }
}
