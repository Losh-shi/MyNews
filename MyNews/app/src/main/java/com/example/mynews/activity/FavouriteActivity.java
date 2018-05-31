package com.example.mynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.mynews.R;
import com.example.mynews.adapter.NewsListAdapter;
import com.example.mynews.bean.News;
import com.example.mynews.manager.NewsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * FavouriteActivity
 *
 * @author ggz
 * @date 2018/5/13
 */
public class FavouriteActivity extends AppCompatActivity implements View.OnClickListener {

    private NewsManager mNewsManager;

    private List<News> mFavouriteList;

    private NewsListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        // ActivityCollector
        ActivityCollector.addActivity(this);

        initView();

        initData();

        initRecyclerView();

    }

    private void initView() {
        ImageView goBackIv = findViewById(R.id.iv_favourite_go_back);
        goBackIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_favourite_go_back:
                finish();
                break;

            default:
        }
    }

    private void initData() {
        mNewsManager = new NewsManager(this);

        String sectionStr = "isFavourite = 1";
        mFavouriteList = mNewsManager.selectDB(sectionStr);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_favourite_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new NewsListAdapter(this, mFavouriteList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(FavouriteActivity.this, WebActivity.class);
                intent.putExtra("url", mFavouriteList.get(position).getUrl());
                startActivity(intent);
            }
        });

        mAdapter.setOnDataChangeListener(new NewsListAdapter.OnDataChangeListener() {
            @Override
            public void onDataChange(int operationType, News news) {
                switch (operationType) {
                    case 0:
                        // 状态没变
                        break;
                    case 1:
                        // 更新数据
                        mNewsManager.updateDB(news);

                        mFavouriteList.remove(news);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        // 删除数据
                        mNewsManager.deleteDB(news);
                        break;
                    default:
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }
}
