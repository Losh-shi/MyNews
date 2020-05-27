package com.example.my.renews.welcome;

import com.example.my.renews.welcome.listener.AdsDataListener;

public class WelcomePresenter implements WelcomeContract.Presenter {
    private WelcomeContract.View mView;
    private WelcomeInteractor mModel;

    public WelcomePresenter(WelcomeContract.View welcomeView) {
        mView = welcomeView;
        mModel = WelcomeInteractor.getInstance();
    }

    public void onDestroy() {
        // View 周期结束
        mView = null;
    }

    private void refreshAdsData() {
        // 从缓存读取
        String data = mModel.getAdsDataFromCache();
        if (mView != null) {
            mView.showAds(data);
        }
    }

    @Override
    public void fetchAdsData() {
        mModel.requestAdsData(new AdsDataListener() {
            @Override
            public void onCompleted(String data) {
                // 保存数据去缓存，用于下次获取
                mModel.saveAdsDataToCache(data);
                refreshAdsData();
            }

            @Override
            public void onFailed() {
                refreshAdsData();
            }
        });
    }

    @Override
    public void fetchUserData() {
        // 查询并预加载 Home 数据
    }
}
