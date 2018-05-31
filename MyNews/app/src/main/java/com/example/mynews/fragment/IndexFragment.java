package com.example.mynews.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mynews.activity.MainActivity;
import com.example.mynews.R;
import com.example.mynews.activity.SearchActivity;
import com.example.mynews.adapter.DragAdapter;
import com.example.mynews.adapter.OtherAdapter;
import com.example.mynews.adapter.PagerAdapter;
import com.example.mynews.bean.Channel;
import com.example.mynews.manager.ChannelManager;
import com.example.mynews.broadcast.BroadcastAction;
import com.example.mynews.view.DragGridView;
import com.example.mynews.view.OtherGridView;

import java.util.List;

/**
 * IndexFragment
 *
 * @author ggz
 * @date 2018/1/14
 */

public class IndexFragment extends Fragment {
    private static final String TAG = "IndexFragment";

    private View mView;
    private MainActivity mActivity;

    /**
     * 侧滑菜单栏
     */
    private DrawerLayout mDrawerLayout;

    /**
     * 新闻频道
     */
    private ChannelManager mChannelManager;
    private List<Channel> mUserChannelList;
    private List<Channel> mOtherChannelList;

    /**
     * 顶部导航栏
     */
    private HorizontalScrollView mHorizontalScrollView;
    private RadioGroup mRadioGroup;


    /**
     * ViewPager
     * 加载 ChildFragment
     */
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;


    /**
     * PopupWindow
     * 频道管理页面
     */
    private PopupWindow mPopupWindow;
    private TextView mPwEditModeTv;
    private boolean isEditState = false;

    /**
     * DragGridView
     * 我的频道栏
     */
    private DragGridView userGridView;
    private DragAdapter dragAdapter;

    /**
     * OtherGridView
     * 其他频道栏
     */
    private OtherGridView otherGridView;
    private OtherAdapter otherAdapter;


    /**
     * 本地广播管理器
     */
    private LocalBroadcastManager mLocalBroadcastManager;
    private GridViewLocalReceiver mGridViewLocalReceiver;


    public IndexFragment() {
        super();
        Log.d(TAG, " -- IndexFragment()");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_index, container, false);
        mActivity = (MainActivity) getActivity();

        // 初始化控件
        initView(mView);

        // 初始化数据
        initChannelList();

        // 初始化顶部频道标签
        initTopTab(mView);

        // 初始化新闻列表
        initViewPager(mView);

        // 初始化本地广播
        initLocalBroadcast();


        return mView;
    }

    /**
     * 初始化控件
     */
    private void initView(View v) {
        // 侧滑菜单栏
        mDrawerLayout = mActivity.findViewById(R.id.drawer_layout);

        // 菜单栏
        ImageView indexCategoryIv = v.findViewById(R.id.iv_index_category);
        indexCategoryIv.setOnClickListener(mOnClickListener);

        // 搜索
        ImageView indexSearchIv = v.findViewById(R.id.iv_index_search);
        indexSearchIv.setOnClickListener(mOnClickListener);

        // 频道管理
        ImageView indexTopTabIv = v.findViewById(R.id.iv_index_top_tab);
        indexTopTabIv.setOnClickListener(mOnClickListener);
    }

    /**
     * 点击事件监听器
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_index_category:
                    // 打开侧滑菜单栏
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    break;

                case R.id.iv_index_search:
                    Intent intent = new Intent(mActivity, SearchActivity.class);
                    mActivity.startActivityForResult(intent, MainActivity.REQUEST_CODE_SEARCH);
                    break;

                case R.id.iv_index_top_tab:
                    // 打开频道管理页面
                    showPopupWindow();
                    break;

                default:
            }
        }
    };

    /**
     * 初始化新闻频道数据
     */
    private void initChannelList() {
        mChannelManager = new ChannelManager(mActivity);

        mUserChannelList = mChannelManager.getUserChannelList();
        mOtherChannelList = mChannelManager.getOtherChannelList();
    }


    /**
     * 初始化顶部导航栏
     * HorizontalScrollView + RadioGroup
     */
    private void initTopTab(View v) {
        mHorizontalScrollView = v.findViewById(R.id.hsv_index_top_tab);

        mRadioGroup = v.findViewById(R.id.rg_index_top_tab);
        mRadioGroup.removeAllViews();

        // 添加 RadioButton(新闻频道)
        int count = mUserChannelList.size();
        for (int i = 0; i < count; i++) {
            RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(
                    R.layout.radiobutton_tab, mRadioGroup, false);
            radioButton.setId(i);
            radioButton.setText(mUserChannelList.get(i).getName());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);

            mRadioGroup.addView(radioButton, params);
        }
        mRadioGroup.check(0);

        // 顶部导航栏控制 ViewPager 的切换
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mViewPager.setCurrentItem(checkedId);
            }
        });
    }

    /**
     * 初始化新闻列表
     * ViewPager + Fragment
     */
    private void initViewPager(View v) {
        mViewPager = v.findViewById(R.id.vp_index_content);

        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), mUserChannelList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // TODO : TabLine 跟随滑动效果
//            Log.d(TAG, position + "  " + positionOffset + "  " + positionOffsetPixels);
//            RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams)
// index_tab_line_iv.getLayoutParams();
//            layoutParams.leftMargin = (int) (positionOffset * width + ???);
//            indexTabLineIv.setLayoutParams(layoutParams);
        }

        @Override
        public void onPageSelected(int position) {
            // 获取 RadioButton
            RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
            // 获取 RadioButton 的左边距离
            int left = radioButton.getLeft();
            // 获取 RadioButton 的宽度
            int width = radioButton.getMeasuredWidth();
            // 获取屏幕宽度
            Display display = getActivity().getWindow().getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int screenWidth = metrics.widthPixels;
            // 计算 HorizontalScrollView 偏移距离
            int len = left + width / 2 - screenWidth / 2;
            mHorizontalScrollView.smoothScrollTo(len, 0);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 新闻频道管理页面
     * PopupWindow
     */
    private void showPopupWindow() {
        // 获取屏幕的宽高像素
        Display display = getActivity().getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        View pw = LayoutInflater.from(mActivity).inflate(R.layout.popupwindow_channel, null);

        mPopupWindow = new PopupWindow(pw, screenWidth, screenHeight / 10 * 9, true);
        mPopupWindow.setContentView(pw);

        // 初始化用户频道列表和其他频道列表的 GridView
        initUserGridView(pw);
        initOtherGridView(pw);

        // 默认：非编辑模式
        isEditState = false;
        dragAdapter.setEditStatus(false);
        userGridView.setEditStatus(false);

        // 编辑按钮的点击事件
        mPwEditModeTv = pw.findViewById(R.id.tv_pw_edit_mode);
        mPwEditModeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditState) {
                    isEditState = true;
                    dragAdapter.setEditStatus(true);
                    userGridView.setEditStatus(true);
                    mPwEditModeTv.setText("完成");

                } else {
                    isEditState = false;
                    dragAdapter.setEditStatus(false);
                    userGridView.setEditStatus(false);
                    mPwEditModeTv.setText("编辑");

                }
            }
        });

        // 关闭按钮的点击事件
        ImageView pwCloseIv = pw.findViewById(R.id.iv_pw_close);
        pwCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭 PopupWindow
                mPopupWindow.dismiss();
            }
        });


        // 外部可点击，即点击 PopupWindow 以外的区域，PopupWindow 消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);

        // 设置启动关闭动画
        mPopupWindow.setAnimationStyle(R.style.PopupWindowAnim);

        // 定位
        View locationView = LayoutInflater.from(mActivity).inflate(R.layout.activity_main, null);
        mPopupWindow.showAtLocation(locationView, Gravity.BOTTOM, 0, 0);

        // OnDismiss 监听器
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPagerAdapter.notifyDataSetChanged();
                // 重新初始化 TopTab
                initTopTab(IndexFragment.this.mView);
            }
        });
    }

    /**
     * 初始化我的频道栏
     */
    private void initUserGridView(final View v) {
        userGridView = v.findViewById(R.id.userGridView);
        dragAdapter = new DragAdapter(mActivity, mUserChannelList, R.layout.grid_view_item);
        userGridView.setAdapter(dragAdapter);

        // 长按事件监听器
        userGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 1) {
                    isEditState = true;
                    dragAdapter.setEditStatus(true);
                    userGridView.setEditStatus(true);
                    userGridView.prepareDrag();
                    mPwEditModeTv.setText("完成");
                }
                return true;
            }
        });


        // 点击事件监听器
        userGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isEditState) {
                    // 非编辑模式，单击跳转该频道
                    // 关闭 PopupWindow
                    mPopupWindow.dismiss();

                    mViewPager.setCurrentItem(position);
                }
            }
        });
    }

    /**
     * 初始化其他频道栏
     */
    private void initOtherGridView(View v) {
        otherGridView = v.findViewById(R.id.otherGridView);
        otherAdapter = new OtherAdapter(mActivity, mOtherChannelList, R.layout.grid_view_item);
        otherGridView.setAdapter(otherAdapter);

        otherGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEditState) {
                    // 发送本地广播，让接收器处理 UserGridView 和 OtherGridView 的增删
                    Intent intent = new Intent(BroadcastAction.OTHER_GRID_VIEW_LOCAL_ACTION);
                    intent.putExtra("position", position);
                    mLocalBroadcastManager.sendBroadcast(intent);
                }
            }
        });
    }

    /**
     * 注册本地广播接收器
     */
    private void initLocalBroadcast() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(mActivity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.DRAG_GRID_VIEW_LOCAL_ACTION);
        intentFilter.addAction(BroadcastAction.OTHER_GRID_VIEW_LOCAL_ACTION);
        mGridViewLocalReceiver = new GridViewLocalReceiver();
        mLocalBroadcastManager.registerReceiver(mGridViewLocalReceiver, intentFilter);
    }

    /**
     * 本地广播接收器
     * 处理 UserGridView 和 OtherGridView 的增删
     */
    private class GridViewLocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();
            final int position = intent.getIntExtra("position", -1);
            if (str.equals(BroadcastAction.DRAG_GRID_VIEW_LOCAL_ACTION)) {
                Channel channel = mUserChannelList.get(position);
                Log.d(TAG, " -- onReceive()  DRAG_GRID_VIEW_LOCAL_ACTION " + channel.getName());
                dragAdapter.removeItem(position);
                otherAdapter.addItem(channel);
            }
            if (str.equals(BroadcastAction.OTHER_GRID_VIEW_LOCAL_ACTION)) {
                Channel channel = mOtherChannelList.get(position);
                Log.d(TAG, " -- onReceive()  OTHER_GRID_VIEW_LOCAL_ACTION " + channel.getName());
                otherAdapter.remove(position);
                dragAdapter.addItem(channel);
            }
        }
    }

    public void updateData() {
        mPagerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, " -- onDestroyView()");

        // 注销本地广播
        mLocalBroadcastManager.unregisterReceiver(mGridViewLocalReceiver);

        // 更新 频道数据 的数据库
        mChannelManager.updateDB(mUserChannelList, 1);
        mChannelManager.updateDB(mOtherChannelList, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " -- onDestroy()");
    }
}
