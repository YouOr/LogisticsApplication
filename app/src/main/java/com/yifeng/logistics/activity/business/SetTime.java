package com.yifeng.logistics.activity.business;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yifeng.logistics.Base.BaseActivity;
import com.yifeng.logistics.R;
import com.yifeng.logistics.dbhelp.MyDatabaseHelper;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/3/24.
 */
public class SetTime extends BaseActivity implements View.OnClickListener{
    private TextView txt_center,start_Time,end_Tiem,txt_righe;
    private LinearLayout rl_back;
    private int year, monthOfYear, dayOfMonth, hourOfDay, minute;
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(R.color.blue);
        setContentView(R.layout.layout_setime);
        txt_center= (TextView) findViewById(R.id.txt_center);
        start_Time= (TextView) findViewById(R.id.start_Time);
        end_Tiem= (TextView) findViewById(R.id.end_Tiem);
        rl_back= (LinearLayout) findViewById(R.id.rl_back);
        txt_righe= (TextView) findViewById(R.id.txt_righe);
        init();
    }

    private void init() {
        txt_center.setText("设置时间段");
        txt_righe.setText("保存");
        start_Time.setOnClickListener(this);
        end_Tiem.setOnClickListener(this);
        rl_back.setOnClickListener(this);
        txt_righe.setOnClickListener(this);
        dbHelper = new MyDatabaseHelper(SetTime.this, "Logistics_History", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PersonalSet", new String[]{"StartTime", "EndTime",}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            start_Time.setText(cursor.getString(cursor.getColumnIndex("StartTime")));
            end_Tiem.setText(cursor.getString(cursor.getColumnIndex("EndTime")));
        }

    }
    private void updateTime(final int order)
    {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(SetTime.this, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                String HH=hourOfDay<10?"0"+hourOfDay:""+hourOfDay;
                String mm=minute<10?"0"+minute:""+minute;
                if(order==1)
                {

                    start_Time.setText( HH + ":" + mm);
                }else
                {
                    end_Tiem.setText(HH + ":" + mm);
                }

            }
        }, hourOfDay, minute, true);

        timePickerDialog.show();
    }
    private void saveTime()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StartTime", start_Time.getText().toString());
        values.put("EndTime", end_Tiem.getText().toString());
        db.update("PersonalSet", values, null, null);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_back:
                finish();
                break;
            case R.id.start_Time:
                updateTime(1);
                break;
            case R.id.end_Tiem:
                updateTime(0);
                break;
            case R.id.txt_righe:
                saveTime();
                Toast.makeText(SetTime.this,"更改成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
