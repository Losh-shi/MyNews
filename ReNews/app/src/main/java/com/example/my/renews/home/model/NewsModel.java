package com.example.my.renews.home.model;

import com.example.my.renews.home.bean.News;
import com.example.my.renews.home.listener.DataListener;

public interface NewsModel {
    void loadNews(DataListener listener);

    void saveNews(News news);
}
