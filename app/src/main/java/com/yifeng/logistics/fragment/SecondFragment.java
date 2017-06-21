package com.yifeng.logistics.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yifeng.logistics.R;
import com.yifeng.logistics.adapter.VehicleAdapter;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.bean.Vehicle;
import com.yifeng.logistics.dbhelp.MyDatabaseHelper;
import com.yifeng.logistics.util.ToastUtil;
import com.yifeng.logistics.util.WeiboDialogUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Administrator on 2017/2/27.
 */
public class SecondFragment extends BaseFragment {
    private SearchView mSearchView;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private MyDatabaseHelper dbHelper;
    private Dialog mWeiboDialog;
    private List<Vehicle> VehicleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VehicleAdapter vap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_two, container, false);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mListView = (ListView) view.findViewById(R.id.listView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recly_ranking);
        return view;
    }

    private void init() {
        dbHelper = new MyDatabaseHelper(getActivity(), "Logistics_History", null, 1);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("History", new String[]{"History_Text"}, null, null, null, null, "ID desc", "10");
        final String[] mStrs = new String[cursor.getCount()];
        int i = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mStrs[i] = cursor.getString(0);
                i++;
            }
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mStrs);
            mListView.setAdapter(adapter);

        }
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                Cursor cursor = db.rawQuery("select History_Text from History where History_Text=?", new String[]{query});
                if (cursor.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    values.put("History_Text", query);
                    db.insert("History", null, values);
                }
                getVehicleInfoAPI(query);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    mListView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(newText)) {
                        adapter.getFilter().filter(newText);
                    } else {
                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mStrs);
                        mListView.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchView.setQuery(mStrs[position], false);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)//表示当前界面为可见状态
        {
            init();
            mSearchView.onActionViewExpanded();
        } else {

        }
    }

    @Override
    public void getResult(String result, int type) {
        super.getResult(result, type);
        if (type == Constant.API_REQUEST_SUCCESS & !TextUtils.isEmpty(result)) {
            try {
                Type listType = new TypeToken<LinkedList<Vehicle>>() {
                }.getType();
                Gson gson = new Gson();
                VehicleList = gson.fromJson(result, listType);
                if (VehicleList == null) {
                    ToastUtil.DisplayToast(getActivity(), "没有查到结果");
                } else {
                    SetVehicleInfo();
                }
            } catch (Exception e) {
                Log.i("TTSS", "Error:" + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private void SetVehicleInfo() {
        mListView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        String[] mStrss = new String[VehicleList.size()];
        for (int i = 0; i < VehicleList.size(); i++) {
            mStrss[i] = VehicleList.get(i).getvID() + "(" + VehicleList.get(i).getUserName() + ")";
        }
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            vap = new VehicleAdapter(VehicleList, getActivity());
            recyclerView.setAdapter(vap);
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        mSearchView.setQuery("", false);
        cloesKeyboard();
        super.onPause();
    }

    @Override
    public void onStop() {
        mSearchView.setQuery("", false);
        cloesKeyboard();
        super.onStop();
    }
}
