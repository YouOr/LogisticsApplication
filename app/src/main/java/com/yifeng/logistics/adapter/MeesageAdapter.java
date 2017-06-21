package com.yifeng.logistics.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yifeng.logistics.R;
import com.yifeng.logistics.activity.GetCommunication;
import com.yifeng.logistics.activity.imagepager.ImagePagerActivity;
import com.yifeng.logistics.bean.Communication;
import com.yifeng.logistics.bean.Constant;
import com.yifeng.logistics.bean.Image;
import com.yifeng.logistics.layout.CustomImageView;
import com.yifeng.logistics.layout.NineGridlayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
public class MeesageAdapter extends BaseAdapter {
    public Context mContext;
    private LayoutInflater mInflater;
    public List<Communication> personList;
    private int columns;//
    private int rows;//
    DiscussAdapter ap;
    public MeesageAdapter(Context mContext, List<Communication> personList) {
        super();
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.personList = personList;
    }
    public void addData(List<Communication> ftListBean) {
        personList.addAll(ftListBean);
        notifyDataSetChanged();
    }

    public void clearAll() {
        personList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tv1, tv2, tv3, tv4, tv5, tv6, media_time;
        ImageView im_susimg_id, img_Media;
        public NineGridlayout ivMore;
        public ListView ivList;
        CustomImageView imageView;
        RelativeLayout rl_media;
        LinearLayout digCommentBody, discuss_txt, ly1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        List<Image> imageList = new ArrayList<>();
        final ArrayList<String> urls2 = new ArrayList<>();
//        List<Map<String, String>> disCom = new ArrayList<Map<String, String>>();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cfriends_list_item, parent, false);
            holder = new ViewHolder();
            holder.tv1 = (TextView) convertView.findViewById(R.id.com_txt);
            holder.tv2 = (TextView) convertView.findViewById(R.id.Name_txt);
            holder.tv3 = (TextView) convertView.findViewById(R.id.time_txt);
            holder.tv4 = (TextView) convertView.findViewById(R.id.digBtn);
            holder.tv5 = (TextView) convertView.findViewById(R.id.commentBtn);
            holder.tv6 = (TextView) convertView.findViewById(R.id.like_Name);
            holder.ly1 = (LinearLayout) convertView.findViewById(R.id.like_layout);
            holder.ivMore = (NineGridlayout) convertView.findViewById(R.id.iv_ngrid_layout);
            holder.ivList = (ListView) convertView.findViewById(R.id.discussList);
            holder.digCommentBody = (LinearLayout) convertView.findViewById(R.id.digCommentBody);
            holder.discuss_txt = (LinearLayout) convertView.findViewById(R.id.discuss_txt);
            holder.im_susimg_id = (ImageView) convertView.findViewById(R.id.im_susimg_id);
            holder.img_Media = (ImageView) convertView.findViewById(R.id.img_Media);
            holder.imageView = (CustomImageView) convertView.findViewById(R.id.head_Img);
            holder.media_time = (TextView) convertView.findViewById(R.id.media_time);
            holder.rl_media = (RelativeLayout) convertView.findViewById(R.id.rl_media);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if (personList.get(position).getDlike() != 0) {
//            holder.tv4.setText("取消");
//        } else {
//            holder.tv4.setText("赞");
//        }
//        if ((personList.get(position).getDLikeName() != null && !personList.get(position).getDLikeName().equals("")) || personList.get(position).getDlike() == 1) {
//
//            holder.ly1.setVisibility(View.VISIBLE);
//            holder.tv6.setText(personList.get(position).getDLikeName());
//
//        } else {
//            holder.ly1.setVisibility(View.GONE);
//        }
        if (personList.get(position).getPic() != null) {
            String[] aa = personList.get(position).getPic().split(",");
            for (int i = 0; i < aa.length; i++) {
                String url = aa[i];
                urls2.add(url);
                Image image = new Image(url, 250, 250);
                imageList.add(image);
            }
            holder.ivMore.setVisibility(View.VISIBLE);
            generateChildrenLayout(imageList.size());
            holder.ivMore.setNumColumns(columns);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.ivMore.getLayoutParams();
            if (imageList.size() == 1) {
                linearParams.width = 350;
                holder.ivMore.setLayoutParams(linearParams);

            } else if (imageList.size() == 2) {
                linearParams.width = 500;
                holder.ivMore.setLayoutParams(linearParams);
            } else if (imageList.size() == 4) {
                linearParams.width = 500;
                holder.ivMore.setLayoutParams(linearParams);
            } else {
                linearParams.width = 750;
                holder.ivMore.setLayoutParams(linearParams);
            }
            GridViewAdapter ap = new GridViewAdapter(mContext, imageList);
            holder.ivMore.setAdapter(ap);
        } else {
            holder.ivMore.setVisibility(View.GONE);
            holder.ivMore.setAdapter(null);
        }
//        if (personList.get(position).getCommunicationDetailsList() != null) {
//            for (int i = 0; i < personList.get(position).getCommunicationDetailsList().size(); i++) {
//                holder.digCommentBody.setVisibility(View.VISIBLE);
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("DName", personList.get(position).getCommunicationDetailsList().get(i).getDName());
//                map.put("Content", personList.get(position).getCommunicationDetailsList().get(i).getContent());
//                disCom.add(map);
//            }
//        } else {
//            holder.digCommentBody.setVisibility(View.GONE);
//        }
//        ap = new DiscussAdapter(mContext, disCom);
//        holder.ivList.setAdapter(ap);
//        setListViewHeightBasedOnChildren(holder.ivList);//根据innerlistview的高度计算parentlistview item的高度
        holder.tv1.setText(personList.get(position).getText());
        holder.tv2.setText(personList.get(position).getName());
        holder.tv3.setText(personList.get(position).getComTime());
        holder.media_time.setText(personList.get(position).getRecordTime() + "");
//        if (personList.get(position).getHeadPic() != null && !personList.get(position).getHeadPic().toString().equals("")) {
//            holder.imageView.setImageUrl(personList.get(position).getHeadPic());
//        } else {
//            holder.imageView.setImageUrl(Constant.headimgurl);
//        }
        TextPaint tp = holder.tv2.getPaint();
        tp.setFakeBoldText(true);
//        holder.tv4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (personList.get(position).getDlike() != 0) {
//                    personList.get(position).setDLikeName(personList.get(position).getDLikeName().replace(Constant.nickname + "、", ""));
//                    personList.get(position).setDLikeName(personList.get(position).getDLikeName().replace(Constant.nickname, ""));
//                    personList.get(position).setDlike(0);
//                    personList.get(position).setOrder(0);
//                    ((MainActivity) mContext).setLike(position, 2);
//                    notifyDataSetChanged();
//                } else {
//                    if (personList.get(position).getDLikeName() == null) {
//                        personList.get(position).setDLikeName(Constant.nickname);
//                    } else {
//                        personList.get(position).setDLikeName(Constant.nickname + "、" + personList.get(position).getDLikeName());
//                    }
//                    personList.get(position).setOrder(0);
//                    ((MainActivity) mContext).setLike(position, 1);
//                    personList.get(position).setDlike(1);
//                    notifyDataSetChanged();
//                }
//            }
//        });
//        holder.tv5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                personList.get(position).setOrder(0);
//                notifyDataSetChanged();
//                ((MainActivity) mContext).showDiscuss(position);
//            }
//        });
//        holder.im_susimg_id.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (personList.get(position).getOrder() == 1) {
//                    personList.get(position).setOrder(0);
//                } else {
//                    personList.get(position).setOrder(1);
//                }
//
//                notifyDataSetChanged();
//            }
//        });
//        if (personList.get(position).getOrder() == 1) {
//            holder.discuss_txt.setVisibility(View.VISIBLE);
//        } else {
//            holder.discuss_txt.setVisibility(View.INVISIBLE);
//        }
        holder.ivMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constant.CURRENT_Order = 0;
                Intent intent = new Intent(mContext, ImagePagerActivity.class);
                intent.putExtra("No", position + "");
                intent.putExtra("ItemNo", urls2);
                mContext.startActivity(intent);
            }
        });
        if (Constant.INFO_TYPE == 0) {
            holder.im_susimg_id.setVisibility(View.GONE);
        } else {
            holder.im_susimg_id.setVisibility(View.VISIBLE);
        }
        if (personList.get(position).getRecord() == null) {
            holder.rl_media.setVisibility(View.GONE);
        } else {
            holder.rl_media.setVisibility(View.VISIBLE);
        }
        holder.img_Media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personList.get(position).getOpenType() == 0) {
                    personList.get(position).setOpenType(1);
                    notifyDataSetChanged();
                } else {
                    personList.get(position).setOpenType(0);
                    notifyDataSetChanged();
                }

            }
        });
        Handler mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0x1112:
                        personList.get(position).setOpenType(0);
                        notifyDataSetChanged();
                        break;

                }
            }
        };
        if (personList.get(position).getOpenType() == 0) {
            holder.img_Media.setImageResource(R.drawable.mediao);
            ((GetCommunication) mContext).closeMedia();
        } else {
            holder.img_Media.setImageResource(R.drawable.medias);
            ((GetCommunication) mContext).openMedia(personList.get(position).getRecord());
            mHandler.sendEmptyMessageDelayed(0x1112, personList.get(position).getRecordTime()*1000);
        }
        return convertView;
    }

    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }

    }

    /**
     * 计算parentlistview item的高度。
     * 如果不使用此方法，无论innerlistview有多少个item，则只会显示一个item。
     **/
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}