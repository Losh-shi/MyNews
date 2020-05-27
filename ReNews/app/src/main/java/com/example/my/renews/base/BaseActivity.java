package com.example.my.renews.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity extends SupportActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutViewId());
        unbinder = ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResId) {
        if (layoutResId == -1) {
            return;
        }
        super.setContentView(layoutResId);
    }

    protected abstract int getLayoutViewId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();
}
