package com.example.my.renews;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.my.renews.home.HomeFragment;
import com.example.my.renews.person.PersonFragment;
import com.example.my.renews.weather.WeatherFragment;

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

    @BindView(R.id.fl_container)
    FrameLayout mFrameLayout;

//    @BindViews({R.id.ll_tab_bar_home, R.id.ll_tab_bar_weather, R.id.ll_tab_bar_person})
//    List<LinearLayout> tabBarLlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());
        }
        initView();
    }

    private void initView() {

    }

    private void showView(int to) {
        SupportFragment supportFragment = null;
        switch (to) {
            case HOME_FRAGMENT:
                supportFragment = findFragment(HomeFragment.class);
                if (null == supportFragment) {
                    start(HomeFragment.newInstance());
                } else {
                    showHideFragment(supportFragment);
                }
                break;
            case WEATHER_FRAGMENT:
                supportFragment = findFragment(WeatherFragment.class);
                if (null == supportFragment) {
                    start(WeatherFragment.newInstance());
                } else {
                    showHideFragment(supportFragment);
                }
                break;
            case PERSON_FRAGMENT:
                supportFragment = findFragment(PersonFragment.class);
                if (null == supportFragment) {
                    start(PersonFragment.newInstance());
                } else {
                    showHideFragment(supportFragment);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
//        return new DefaultHorizontalAnimator();
        // 设置自定义动画
//        return new FragmentAnimator(enter, exit, popEnter, popExit);
    }

    @OnClick({R.id.ll_tab_bar_home, R.id.ll_tab_bar_weather, R.id.ll_tab_bar_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tab_bar_home:
                showView(HOME_FRAGMENT);
                break;
            case R.id.ll_tab_bar_weather:
                showView(WEATHER_FRAGMENT);
                break;
            case R.id.ll_tab_bar_person:
                showView(PERSON_FRAGMENT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
