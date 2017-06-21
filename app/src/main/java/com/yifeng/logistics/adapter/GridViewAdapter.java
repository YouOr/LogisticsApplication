package com.yifeng.logistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yifeng.logistics.R;
import com.yifeng.logistics.bean.Image;
import com.yifeng.logistics.layout.CustomImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-01-10.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    List<Image> url=new ArrayList<>();
    public GridViewAdapter(Context mContext, List<Image> url) {
        super();
        this.mContext = mContext;
        this.url = url;

    }
    @Override
    public int getCount() {
        return url.size();
    }
    @Override
    public Object getItem(int position) {
        if (url == null) {
            return null;
        } else {
            return this.url.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        CustomImageView imageView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.image_pager_itme, null, false);
            holder.imageView = (CustomImageView)convertView.findViewById(R.id.iv_oneimage);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

            holder.imageView.setImageUrl(url.get(position).getUrl());

        return convertView;
    }
}
