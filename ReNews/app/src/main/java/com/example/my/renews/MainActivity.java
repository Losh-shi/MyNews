package com.example.my.renews;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my.renews.home.view.HomeFragment;
import com.example.my.renews.person.PersonFragment;
import com.example.my.renews.weather.WeatherFragment;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends SupportActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int HOME_FRAGMENT = 0;
    private static final int WEATHER_FRAGMENT = 1;
    private static final int PERSON_FRAGMENT = 2;

    private SupportFragment[] mFragments = new SupportFragment[3];
    private int mPosition = HOME_FRAGMENT;

    @BindViews({R.id.iv_tab_bar_home, R.id.iv_tab_bar_weather, R.id.iv_tab_bar_person})
    List<ImageView> mTabBarIvList;

    @BindViews({R.id.tv_tab_bar_home, R.id.tv_tab_bar_weather, R.id.tv_tab_bar_person})
    List<TextView> mTabBarTvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            initData();
        }
        initView();
    }

    private void initData() {
        mFragments[HOME_FRAGMENT] = HomeFragment.newInstance();
        mFragments[WEATHER_FRAGMENT] = WeatherFragment.newInstance();
        mFragments[PERSON_FRAGMENT] = PersonFragment.newInstance();

        loadMultipleRootFragment(R.id.fl_container, HOME_FRAGMENT,
                mFragments[HOME_FRAGMENT],
                mFragments[WEATHER_FRAGMENT],
                mFragments[PERSON_FRAGMENT]);
    }

    private void initView() {
        updateTabBarStatus(mPosition);
    }

    @OnClick({R.id.ll_tab_bar_home, R.id.ll_tab_bar_weather, R.id.ll_tab_bar_person})
    public void onViewClicked(View view) {
        mPosition = HOME_FRAGMENT;
        switch (view.getId()) {
            case R.id.ll_tab_bar_home:
                mPosition = HOME_FRAGMENT;
                break;
            case R.id.ll_tab_bar_weather:
                mPosition = WEATHER_FRAGMENT;
                break;
            case R.id.ll_tab_bar_person:
                mPosition = PERSON_FRAGMENT;
                break;
            default:
                break;
        }
        showHideFragment(mFragments[mPosition]);
        updateTabBarStatus(mPosition);
    }

    private void updateTabBarStatus(int position) {
        for (int i = 0; i < mTabBarIvList.size(); i++) {
            mTabBarIvList.get(i).setSelected(false);
            mTabBarTvList.get(i).setSelected(false);
        }
        mTabBarIvList.get(position).setSelected(true);
        mTabBarTvList.get(position).setSelected(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
