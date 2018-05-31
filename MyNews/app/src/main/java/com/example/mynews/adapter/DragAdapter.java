package com.example.mynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mynews.R;
import com.example.mynews.bean.Channel;
import com.example.mynews.broadcast.BroadcastAction;

import java.util.List;

/**
 * DragAdapter
 *
 * @author ggz
 * @date 2017/12/27
 */

public class DragAdapter extends BaseAdapter {
    private final static String TAG = "DragAdapter";
    private final static boolean IS_LOG = false;


    private Context mContext;
    private List<Channel> mChannelList;
    private int mLayoutId;

    private LocalBroadcastManager mLocalBroadcastManager;


    private boolean isEditState = false;

    private boolean isVisible = true;
    private boolean isChanged = false;

    private int hidePosition = -1;
    private int holdPosition = -1;


    public DragAdapter(Context context, List<Channel> channelList, int layoutId) {
        this.mContext = context;
        this.mChannelList = channelList;
        this.mLayoutId = layoutId;

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this.mContext);
    }

    @Override
    public int getCount() {
        return mChannelList != null ? mChannelList.size() : 0;
    }

    @Override
    public Channel getItem(int position) {
        return mChannelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 在传入的 List 中找出对应 position 的数据对象
        Channel channel = getItem(position);

        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        TextView itemTextTv = view.findViewById(R.id.tv_grid_view_item_text);
        ImageView itemDeleteIv = view.findViewById(R.id.iv_grid_view_item_delete);

        itemTextTv.setText(channel.getName());

        // 如果为编辑模式，则显示删除图标
        if (isEditState) {
            itemDeleteIv.setVisibility(View.VISIBLE);
        } else {
            itemDeleteIv.setVisibility(View.INVISIBLE);
        }
        itemDeleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当点击删除图标，发送本地广播，让接收器处理 UserGridView 和 OtherGridView 的增删
                Intent intent = new Intent(BroadcastAction.DRAG_GRID_VIEW_LOCAL_ACTION);
                intent.putExtra("position", position);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        });

        // 第一个 item ，设置为不可操作状态
        if (position == 0) {
            if (IS_LOG) {
                Log.d(TAG, "------------------------------------");
            }
            itemTextTv.setEnabled(false);
            itemDeleteIv.setVisibility(View.INVISIBLE);
        }

        // 长按时隐藏 item ，使其显示空白
        if (position == hidePosition && !isVisible) {
            view.setVisibility(View.INVISIBLE);
            isVisible = true;
        }

        // 如果该位置是可能被换位的 item ，则显示像提示一样的预选边框
        if (position == holdPosition && isChanged) {
            itemTextTv.setText("");
            itemDeleteIv.setVisibility(View.INVISIBLE);
            isChanged = false;
        }

        if (IS_LOG) {
            Log.d(TAG, position + " : "
                    + " id " + channel.getId()
                    + " name " + channel.getName()
                    + " orderNum " + channel.getOrderNum());
        }

        return view;
    }

    /**
     * 添加新闻频道
     */
    public void addItem(Channel channel) {
        mChannelList.add(channel);
        notifyDataSetChanged();
    }

    /**
     * 删除新闻频道
     */
    public void removeItem(int position) {
        mChannelList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 新闻频道的换位
     */
    public void exchangeItem(int dragPosition, int dropPosition) {
        holdPosition = dropPosition;
        Channel dragItem = getItem(dragPosition);
        if (IS_LOG) {
            Log.d(TAG, " -- exchangeItem()  startPosition = " + dragPosition
                    + " endPosition = " + dropPosition);
        }
        if (dragPosition < dropPosition) {
            mChannelList.add(dropPosition + 1, dragItem);
            mChannelList.remove(dragPosition);
        } else {
            mChannelList.add(dropPosition, dragItem);
            mChannelList.remove(dragPosition + 1);
        }
        isChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 隐藏 item
     */
    public void hideItem(int position) {
        hidePosition = position;
        isVisible = false;
        notifyDataSetChanged();
    }

    /**
     * 设置编辑模式
     */
    public void setEditStatus(boolean b) {
        isEditState = b;
        notifyDataSetChanged();
    }
}
