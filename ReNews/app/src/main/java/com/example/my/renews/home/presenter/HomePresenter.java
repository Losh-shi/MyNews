package com.example.my.renews.home.presenter;

import com.example.my.renews.home.bean.News;
import com.example.my.renews.home.listener.DataListener;
import com.example.my.renews.home.model.NewsAPI;
import com.example.my.renews.home.model.NewsAPIImpl;
import com.example.my.renews.home.model.NewsModel;
import com.example.my.renews.home.model.NewsModelImpl;
import com.example.my.renews.home.view.ViewInterface;

import java.util.List;

public class HomePresenter {
    /**
     * View 层接口
     */
    ViewInterface mView;
    /**
     * 数据 Model
     */
    NewsModel mModel = new NewsModelImpl();
    /**
     * 网络 API
     */
    NewsAPI mAPI = new NewsAPIImpl();

    public HomePresenter(ViewInterface viewInterface) {
        mView = viewInterface;
    }

    public void fetchData() {
        // 读取数据库
        mModel.loadNews(new DataListener() {
            @Override
            public void onCompleted(List<News> newsList) {
                // 刷新 View 层界面
            }

            @Override
            public void onFailed() {
                // View 层失败提示
            }
        });
        // 获取网络API的数据
    }
}
