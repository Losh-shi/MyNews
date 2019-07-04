package com.example.my.renews.home.view;

import com.example.my.renews.home.bean.News;

import java.util.List;

public interface ViewInterface {
    void showData(List<News> newsList);

    void showToast(String display);
}
