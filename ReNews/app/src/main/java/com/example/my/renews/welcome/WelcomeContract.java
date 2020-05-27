package com.example.my.renews.welcome;

import com.example.my.renews.welcome.listener.AdsDataListener;

public interface WelcomeContract {

    interface Model {
        void requestAdsData(AdsDataListener listener);
    }

    interface View {
        void showAds(String data);

        void navigateToHome();
    }

    interface Presenter {
        void fetchAdsData();

        void fetchUserData();
    }
}
