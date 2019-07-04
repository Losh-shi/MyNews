package com.example.my.renews.home.listener;

import com.example.my.renews.home.bean.News;

import java.util.List;

public interface DataListener {

    void onCompleted(List<News> newsList);

    void onFailed();
}
