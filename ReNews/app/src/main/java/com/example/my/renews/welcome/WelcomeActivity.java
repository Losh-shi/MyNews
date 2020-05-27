package com.example.my.renews.welcome;

import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.my.renews.R;
import com.example.my.renews.base.BaseActivity;
import com.example.my.renews.main.MainActivity;

import butterknife.BindView;

public class WelcomeActivity extends BaseActivity implements WelcomeContract.View {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private WelcomePresenter mPresenter;

    @BindView(R.id.tv_advertising)
    TextView mAdvertisingTv;

    @BindView(R.id.tv_skip)
    TextView mSkipTv;

    @Override
    protected int getLayoutViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        mPresenter = new WelcomePresenter(this);
        mPresenter.fetchAdsData();
        mPresenter.fetchUserData();
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showAds(String data) {
        mAdvertisingTv.setText(data);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                navigateToHome();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mAdvertisingTv.startAnimation(alphaAnimation);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        startActivity(intent);
        finish();
    }
}
