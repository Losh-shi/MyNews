package com.example.my.renews;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.my.renews.home.HomeFragment;
import com.example.my.renews.person.PersonFragment;
import com.example.my.renews.weather.WeatherFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int HOME_FRAGMENT = 0;
    private static final int WEATHER_FRAGMENT = 1;
    private static final int PERSON_FRAGMENT = 2;
    private SupportFragment[] mFragments = new SupportFragment[3];

//    @BindViews({R.id.ll_tab_bar_home, R.id.ll_tab_bar_weather, R.id.ll_tab_bar_person})
//    List<LinearLayout> tabBarLlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            mFragments[HOME_FRAGMENT] = HomeFragment.newInstance();
            mFragments[WEATHER_FRAGMENT] = WeatherFragment.newInstance();
            mFragments[PERSON_FRAGMENT] = PersonFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, HOME_FRAGMENT,
                    mFragments[HOME_FRAGMENT],
                    mFragments[WEATHER_FRAGMENT],
                    mFragments[PERSON_FRAGMENT]);
        }
        initView();
    }

    private void initView() {

    }

    @OnClick({R.id.ll_tab_bar_home, R.id.ll_tab_bar_weather, R.id.ll_tab_bar_person})
    public void onViewClicked(View view) {
        int to = HOME_FRAGMENT;
        switch (view.getId()) {
            case R.id.ll_tab_bar_home:
                to = HOME_FRAGMENT;
                break;
            case R.id.ll_tab_bar_weather:
                to = WEATHER_FRAGMENT;
                break;
            case R.id.ll_tab_bar_person:
                to = PERSON_FRAGMENT;
                break;
            default:
                break;
        }
        showHideFragment(mFragments[to]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
