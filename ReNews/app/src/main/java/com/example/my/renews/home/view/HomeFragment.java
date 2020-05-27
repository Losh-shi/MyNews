package com.example.my.renews.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.my.renews.R;
import com.example.my.renews.base.BaseFragment;
import com.example.my.renews.main.MainActivity;

public class HomeFragment extends BaseFragment {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static HomeFragment newInstance() {
        // todo: Fragmentation 保存数据
        return new HomeFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 加载/恢复 childFragment
//        SupportFragment firstFragment = findFragment(HomeFragment.class);
//        if (firstFragment == null) {
//            mFragments[HOME_FRAGMENT] = HomeFragment.newInstance();
//            mFragments[WEATHER_FRAGMENT] = WeatherFragment.newInstance();
//            mFragments[PERSON_FRAGMENT] = PersonFragment.newInstance();
//
//            loadMultipleRootFragment(R.id.fl_container, 0,
//                    mFragments[HOME_FRAGMENT],
//                    mFragments[WEATHER_FRAGMENT],
//                    mFragments[PERSON_FRAGMENT]);
//        }
//        else {
//            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
//
//            // 这里我们需要拿到mFragments的引用
//            mFragments[HOME_FRAGMENT] = firstFragment;
//            mFragments[WEATHER_FRAGMENT] = findFragment(WeatherFragment.class);
//            mFragments[PERSON_FRAGMENT] = findFragment(PersonFragment.class);
//        }
    }
}
