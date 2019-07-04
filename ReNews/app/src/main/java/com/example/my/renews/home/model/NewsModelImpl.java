package com.example.my.renews.home.model;

import com.example.my.renews.home.bean.News;
import com.example.my.renews.home.listener.DataListener;

import java.util.ArrayList;
import java.util.List;

public class NewsModelImpl implements NewsModel {

    private List<News> mNewsList = new ArrayList<>();

    public NewsModelImpl() {
        super();
        // 假数据
        initData();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            String newsName = "News " + String.valueOf(i);
            String newsContent = "Content " + String.valueOf(i);
            News news = new News(newsName, newsContent);
            mNewsList.add(news);
        }
    }

    @Override
    public void loadNews(DataListener listener) {
        List<News> list = new ArrayList<>();
        // 操作数据库
        for (News news : mNewsList) {
            list.add(news);
        }
        // TODO 处理耗时操作

        if (list != null) {
            listener.onCompleted(list);
        } else {
            listener.onFailed();
        }
    }

    @Override
    public void saveNews(News news) {

    }
}
