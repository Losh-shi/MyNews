package com.example.my.renews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.fl_content)
    public FrameLayout mFrameLayout;

    @BindViews({R.id.ll_tab_bar_home, R.id.ll_tab_bar_weather, R.id.ll_tab_bar_person})
    public List<LinearLayout> tabBarLlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.ll_tab_bar_home, R.id.ll_tab_bar_weather, R.id.ll_tab_bar_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tab_bar_home:
                break;
        }
    }
}
