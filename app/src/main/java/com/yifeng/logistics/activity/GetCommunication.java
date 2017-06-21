package com.yifeng.logistics.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.yifeng.logistics.Base.BaseActivity;
import com.yifeng.logistics.R;
import com.yifeng.logistics.adapter.MeesageAdapter;
import com.yifeng.logistics.bean.Communication;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.util.AudioFileUtils;
import com.yifeng.logistics.util.PlayerUtils;
import com.yifeng.logistics.util.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/21.
 */
public class GetCommunication extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ListView lv_communication;
    private SwipeRefreshLayout cSwipeRefresh;
    private TextView loginName,txt_center;
    private String mediaUrl = "";
    private boolean isBottom = false;
    private LinearLayout rl_back;
    private ImageView imageView;
    PlayerUtils mPlayer;
    private int page = 1,iscik=0;
    private List<Communication> communicationList = new ArrayList<>();
    MeesageAdapter ap;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x1118:
                    page=1;
                    ap.clearAll();
                    getData("1");
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_communication);
        setTranslucentStatus(R.color.blue);
        lv_communication = (ListView) findViewById(R.id.lv_communication);
        cSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.cSwipeRefresh);
        rl_back= (LinearLayout) findViewById(R.id.rl_back);
        txt_center= (TextView) findViewById(R.id.txt_center);
        mPlayer = new PlayerUtils();
        getData("1");
        setAdapter();
        init();
    }

    private void init() {

        cSwipeRefresh.setOnRefreshListener(this);
        lv_communication.addHeaderView(layoutInflater.from(GetCommunication.this).inflate(R.layout.layout_list_head, null));
        imageView= (ImageView) findViewById(R.id.imageView);
        loginName= (TextView) findViewById(R.id.loginName);
        loginName.setText(getIntent().getStringExtra("vID"));
        txt_center.setText("车主信息");
        try {
            Picasso.with(GetCommunication.this).load(Constant.headPic).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lv_communication.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = lv_communication.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        Log.d("ListView", "##### 滚动到顶部 #####");
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = lv_communication.getChildAt(lv_communication.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == lv_communication.getHeight()) {
                        Log.d("ListView", "##### 滚动到底部 ######");
                        if (!isBottom) {
                            iscik = 0;
                            page++;
                            getData(String.valueOf(page));
                        }
                    }
                }
            }
        });
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(0x1118, 2000);
    }

    private void getData(String cur_page) {
        getGetCommunicationInfAPI(String.valueOf(getIntent().getIntExtra("ID", 0)), cur_page);
    }
    private void setAdapter() {
        ap = new MeesageAdapter(GetCommunication.this, communicationList);
        lv_communication.setAdapter(ap);
    }
    public void openMedia(String url) {
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
                if(iscik==0) {
                    Type listType = new TypeToken<LinkedList<Communication>>() {
                    }.getType();
                    Gson gson = new Gson();
                    LinkedList<Communication> data = gson.fromJson(result, listType);
                    if (data != null) {
                        ap.addData(data);
                    } else {
                        ToastUtil.DisplayToast(GetCommunication.this, Constant.TOAST_MEG_ANALYZE_ERROR);
                    }
                    cSwipeRefresh.setRefreshing(false);
                }else
                {
                    iscik=0;
                    mPlayer = new PlayerUtils();
                    mPlayer.setUrlPrepare(mediaUrl);
                    mPlayer.play();
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        if (result.equals("[]")) {
            isBottom = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayer = new PlayerUtils();
        mPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer = new PlayerUtils();
        mPlayer.stop();
    }
}
