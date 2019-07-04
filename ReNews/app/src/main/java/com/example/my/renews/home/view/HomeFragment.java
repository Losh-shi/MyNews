package com.example.my.renews.home.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my.renews.MainActivity;
import com.example.my.renews.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

public class HomeFragment extends SupportFragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Unbinder unbinder;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
