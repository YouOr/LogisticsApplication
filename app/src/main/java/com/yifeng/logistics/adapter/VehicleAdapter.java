package com.yifeng.logistics.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.logistics.R;
import com.yifeng.logistics.activity.map.ShowMap;
import com.yifeng.logistics.bean.Vehicle;

import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {
    private List<Vehicle> mVehicleList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ran_No, ran_Name;
        LinearLayout Ly_vehicle;

        public ViewHolder(View view) {
            super(view);
            ran_No = (TextView) view.findViewById(R.id.ran_No);
            ran_Name = (TextView) view.findViewById(R.id.ran_Name);
            Ly_vehicle = (LinearLayout) view.findViewById(R.id.Ly_vehicle);
        }
    }

    public VehicleAdapter(List<Vehicle> VehicleList, Context context) {
        mVehicleList = VehicleList;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ranking_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ran_No.setText(mVehicleList.get(position).getvID());
        holder.ran_Name.setText(mVehicleList.get(position).getUserName() + "");
        holder.Ly_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(mContext, ShowMap.class);
                intent.putExtra("Longitude", mVehicleList.get(position).getLongitude());
                intent.putExtra("Latitude", mVehicleList.get(position).getLatitude());
                intent.putExtra("time", mVehicleList.get(position).getTime());
                intent.putExtra("vID", mVehicleList.get(position).getvID());
                intent.putExtra("ID", mVehicleList.get(position).getID());
                if (mVehicleList.get(position).getPassWord() == null || mVehicleList.get(position).getPassWord().equals("")) {
                    mContext.startActivity(intent);
                } else {
                    final EditText inputServer = new EditText(mContext);
                    inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                    inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("该用户信息已加密,请输入密码").setView(inputServer).setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(inputServer.getText().toString().equals(mVehicleList.get(position).getPassWord()))
                            {
                                Log.i("TTYY",inputServer.getText()+"-"+mVehicleList.get(position).getPassWord());
                                mContext.startActivity(intent);
                            }else
                            {
                                Log.i("TTYY",inputServer.getText()+"-"+mVehicleList.get(position).getPassWord());
                                Toast.makeText(mContext,"密码不正确",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVehicleList.size();
    }


}
