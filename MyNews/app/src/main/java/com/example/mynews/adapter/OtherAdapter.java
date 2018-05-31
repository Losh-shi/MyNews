package com.example.mynews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.mynews.R;
import com.example.mynews.bean.Channel;

import java.util.List;

/**
 * Created by ggz on 2017/12/27.
 */

public class OtherAdapter extends BaseAdapter {
    private final static String TAG = "OtherAdapter";

    private Context context;
    private List<Channel> channelList;
    private int layoutId;

    /** TextView 频道内容 */
    private TextView gv_item_tv;
    /** 是否可见 */
    boolean isVisible = true;
    /** 要删除的position */
    public int remove_position = -1;



    public OtherAdapter(Context context, List<Channel> channelList, int resourceId) {
        this.context = context;
        this.channelList = channelList;
        this.layoutId = resourceId;
    }

    @Override
    public int getCount() {
        return channelList != null ? channelList.size() : 0 ;
    }

    @Override
    public Channel getItem(int position) {
        return channelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View view = LayoutInflater.from(context).inflate(layoutId, null);

        Channel channel = getItem(position);

        gv_item_tv = (TextView) view.findViewById(R.id.tv_grid_view_item_text);
        gv_item_tv.setText(channel.getName());



        return view;
    }

    /** 添加频道列表 */
    public void addItem(Channel channel) {
        channelList.add(channel);
        notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove(int position) {
        remove_position = position;
        channelList.remove(remove_position);
        notifyDataSetChanged();
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }


    public List<Channel> getNewChannelList () {
        return channelList;
    }

}
