package com.yifeng.logistics.activity.user;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.logistics.Base.BaseActivity;
import com.yifeng.logistics.R;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.dbhelp.MyDatabaseHelper;
import com.yifeng.logistics.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/16.
 */
public class UpdateVehicle extends BaseActivity implements View.OnClickListener {
    private TextView txt_center,txt_righe;
    private EditText et_Vehicle;
    private ImageView img_delete;
    private LinearLayout rl_back;
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update_vehicle);
        setTranslucentStatus(R.color.blue);
        txt_center= (TextView) findViewById(R.id.txt_center);
        txt_righe= (TextView) findViewById(R.id.txt_righe);
        et_Vehicle= (EditText) findViewById(R.id.et_Vehicle);
        img_delete= (ImageView) findViewById(R.id.img_delete);
        rl_back= (LinearLayout) findViewById(R.id.rl_back);
        init();
    }

    private void init() {
        txt_center.setText("修改车牌号");
        txt_righe.setText("保存");
        et_Vehicle.setText(getIntent().getStringExtra("No"));
        img_delete.setOnClickListener(this);
        rl_back.setOnClickListener(this);
        txt_righe.setOnClickListener(this);
        if(!et_Vehicle.getText().equals(""))
        {
            img_delete.setVisibility(View.VISIBLE);
        }else
        {
            img_delete.setVisibility(View.INVISIBLE);
        }
        et_Vehicle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_Vehicle.getText().toString().length() > 0) {
                    img_delete.setVisibility(View.VISIBLE);
                } else {
                    img_delete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_delete:
                et_Vehicle.setText("");
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.txt_righe:
                UpdateVehicleNoAPI(Constant.openid,Constant.vehicleNo,et_Vehicle.getText().toString());

                break;
        }
    }

    @Override
    public void getResult(String result, int type) {
        super.getResult(result, type);
        if (type == Constant.API_REQUEST_SUCCESS & !TextUtils.isEmpty(result)) {
            try {
                JSONObject jsb = new JSONObject(result);
                if (jsb.getString("Message").equals("1")) {
                    Constant.vehicleNo=et_Vehicle.getText().toString();
                    dbHelper = new MyDatabaseHelper(UpdateVehicle.this, "Logistics_History", null, 1);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("VID", et_Vehicle.getText().toString());
                    db.update("PersonalSet", values, null, null);
                    finish();
                } else {
                    Toast.makeText(UpdateVehicle.this,"网络错误",Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.DisplayToast(UpdateVehicle.this, Constant.TOAST_MEG_ANALYZE_ERROR);
            }
        }

    }
}
