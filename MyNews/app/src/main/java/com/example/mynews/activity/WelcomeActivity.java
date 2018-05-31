package com.example.mynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.mynews.R;

/**
 * WelcomeActivity
 *
 * @author ggz
 * @date 2018/1/11
 */
public class WelcomeActivity extends AppCompatActivity {

    private TextView mSignTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // ActivityCollector
        ActivityCollector.addActivity(this);

        initView();

        initAnimation();
    }

    private void initView() {
        mSignTv = findViewById(R.id.tv_sign);

        TextView passTv = findViewById(R.id.tv_pass);
        passTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(3000);
        mSignTv.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
