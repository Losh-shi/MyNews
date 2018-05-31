package com.example.mynews.adapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.example.mynews.fragment.IndexChildFragment;
import com.example.mynews.bean.Channel;

import java.util.List;


/**
 * PagerAdapter
 *
 * @author ggz
 * @date 2018/1/14
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final static String TAG = "PagerAdapter";

    private FragmentManager mFragmentManager;
    private List<Channel> mChannelList;

    // 保存每个 Fragment 的 Tag ，刷新页面的依据
//    private SparseArray<String> tags = new SparseArray<>();

    public PagerAdapter(FragmentManager fm, List<Channel> list) {
        super(fm);
        mFragmentManager = fm;
        mChannelList = list;
    }

    @Override
    public int getCount() {
        return mChannelList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int channelId = mChannelList.get(position).getId();
        Log.d(TAG, " -- instantiateItem()  position " + position + " channelId " + channelId);
//        IndexChildFragment fragment = (IndexChildFragment) super.instantiateItem(container,position);
//        String tag = fragment.getTag();
//        tags.put(position, tag);
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        // 根据 position 值，判断是哪个新闻频道(channelId)
        int channelId = mChannelList.get(position).getId();
        Log.d(TAG, " -- getItem()  position " + position + " channelId " + channelId);
        // 构建新闻列表模块(ChildFragment)
        IndexChildFragment fragment = new IndexChildFragment();
        // 传入 channelId
        Bundle bundle = new Bundle();
        bundle.putInt("channelId", channelId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        Log.d(TAG, " -- getItemPosition()");
//        IndexChildFragment fragment = (IndexChildFragment) object;
//        //如果 Fragment 对应的 Tag 存在，则不进行刷新
//        if (tags.indexOfValue(fragment.getTag()) > -1) {
//            return super.getItemPosition(object); // POSITION_UNCHANGED
//        }
        return POSITION_NONE;
    }


}
