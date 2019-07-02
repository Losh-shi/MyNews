package com.example.my.renews;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.my.renews.home.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportActivity;

public class MainActivity extends SupportActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.fl_container)
    public FrameLayout mFrameLayout;

//    @BindViews({R.id.ll_tab_bar_home, R.id.ll_tab_bar_weather, R.id.ll_tab_bar_person})
//    public List<LinearLayout> tabBarLlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {

    }

    @OnClick({R.id.ll_tab_bar_home, R.id.ll_tab_bar_weather, R.id.ll_tab_bar_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tab_bar_home:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_tab_bar_weather:
                Toast.makeText(this, "weather", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_tab_bar_person:
                Toast.makeText(this, "person", Toast.LENGTH_SHORT).show();
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
