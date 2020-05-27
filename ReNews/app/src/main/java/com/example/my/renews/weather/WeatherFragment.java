package com.example.my.renews.weather;

import com.example.my.renews.base.BaseFragment;
import com.example.my.renews.main.MainActivity;
import com.example.my.renews.R;

public class WeatherFragment extends BaseFragment {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_weather;
    }
}
