package com.example.my.renews.welcome;

import android.os.Handler;

import com.example.my.renews.welcome.listener.AdsDataListener;

/**
 * @author GGZ
 */
public class WelcomeInteractor implements WelcomeContract.Model {

    private String mAdsData;

    private WelcomeInteractor() {
    }

    private static class WelcomeInteractorHolder {
        private static final WelcomeInteractor sInstance = new WelcomeInteractor();
    }

    public static WelcomeInteractor getInstance() {
        return WelcomeInteractorHolder.sInstance;
    }

    @Override
    public void requestAdsData(AdsDataListener listener) {
        // 网络请求耗时操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 网络请求结果
                boolean result = true;
                if (result) {
                    // 保存去缓存
                    mAdsData = "广告 1";
                    listener.onCompleted(mAdsData);
                } else {
                    listener.onFailed();
                }
            }
        }, 2000);
    }

    public void saveAdsDataToCache(String data) {
        // 把请求回来的数据保存去缓存
    }

    public String getAdsDataFromCache() {
        String data;
        boolean result = true;
        if (result) {
            // 有缓存
            data = mAdsData;
        } else {
            // 没有缓存
            data = "欢迎";
        }
        return data;
    }
}
