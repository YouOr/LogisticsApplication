package com.yifeng.logistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yifeng.logistics.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/20.
 */
public class DiscussAdapter extends BaseAdapter {
    public Context mContext;
    private LayoutInflater mInflater;
    public List<Map<String, String>> disCom = new ArrayList<Map<String, String>>();

    public DiscussAdapter(Context mContext, List<Map<String, String>> disCom) {
        super();
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.disCom = disCom;
    }
    @Override
    public int getCount() {
        return disCom.size();
    }

    @Override
    public Object getItem(int position) {
        return disCom.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView tv1,tv2;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_discuss_item, null);
            holder = new ViewHolder();
            holder.tv1 = (TextView) convertView.findViewById(R.id.disName);
            holder.tv2 = (TextView) convertView.findViewById(R.id.disCom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv1.setText(disCom.get(position).get("DName"));
        holder.tv2.setText(disCom.get(position).get("Content"));
        return convertView;
    }
}
