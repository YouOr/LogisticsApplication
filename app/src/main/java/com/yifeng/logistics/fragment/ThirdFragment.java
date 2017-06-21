package com.yifeng.logistics.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.logistics.R;
import com.yifeng.logistics.activity.business.SetTime;
import com.yifeng.logistics.activity.user.LoginActivity;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.dbhelp.MyDatabaseHelper;
import com.yifeng.logistics.layout.Switch;

/**
 * Created by Administrator on 2017/2/27.
 */
public class ThirdFragment extends BaseFragment implements View.OnClickListener{
    private LinearLayout ll_exit;
    private RelativeLayout ll_locationTime;
    private MyDatabaseHelper dbHelper;
    private TextView center_one_title;
    private Switch sh_StateThree;
    private String seTime;
    private int Disturb=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_three, container, false);
        ll_exit= (LinearLayout) view.findViewById(R.id.ll_exit);
        ll_locationTime= (RelativeLayout) view.findViewById(R.id.ll_locationTime);
        center_one_title= (TextView) view.findViewById(R.id.center_one_title);
        sh_StateThree= (Switch) view.findViewById(R.id.sh_StateThree);
        dbHelper = new MyDatabaseHelper(getActivity(), "Logistics_History", null, 1);
        intit();
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)//表示当前界面为可见状态
        {

        }else
        {

        }
    }
    private void intit() {
        ll_exit.setOnClickListener(this);
        ll_locationTime.setOnClickListener(this);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PersonalSet", new String[]{"StartTime", "EndTime","Disturb",}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            seTime=cursor.getString(cursor.getColumnIndex("StartTime"))+"-"+cursor.getString(cursor.getColumnIndex("EndTime"));
            Disturb=cursor.getInt(cursor.getColumnIndex("Disturb"));
        }
        if(Disturb==0)
        {
            sh_StateThree.setChecked(false);
            center_one_title.setText("");
        }else
        {
            sh_StateThree.setChecked(true);
            center_one_title.setText(seTime);
        }
        sh_StateThree.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch s, boolean isChecked) {
                ContentValues values = new ContentValues();
                if (isChecked) {
                    values.put("Disturb", 1);
                    center_one_title.setText(seTime);
                    Disturb=1;
                } else {
                    values.put("Disturb", 0);
                    center_one_title.setText("");
                    Disturb=0;
                }
                db.update("PersonalSet", values, "User = ?", new String[]{Constant.openid});
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ll_exit:
                deleteUser();
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.ll_locationTime:
                if(Disturb==0)
                {
                    Toast.makeText(getActivity(),"请先开启勿扰模式",Toast.LENGTH_SHORT).show();
                }else
                {
                    Intent ii=new Intent(getActivity(), SetTime.class);
                    startActivity(ii);
                }
                break;
        }
    }
    private void deleteUser()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("User", null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PersonalSet", new String[]{"StartTime", "EndTime","Disturb",}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            center_one_title.setText(cursor.getString(cursor.getColumnIndex("StartTime"))+"-"+cursor.getString(cursor.getColumnIndex("EndTime")));
        }
    }
}
