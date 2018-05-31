package com.example.mynews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mynews.activity.MainActivity;
import com.example.mynews.R;
import com.example.mynews.activity.WebActivity;
import com.example.mynews.adapter.NewsListAdapter;
import com.example.mynews.bean.News;
import com.example.mynews.http.NewsHttp;
import com.example.mynews.manager.NewsManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * IndexChildFragment
 *
 * @author ggz
 * @date 2017/12/6
 */

public class IndexChildFragment extends Fragment {
    private static final String TAG = "IndexChildFragment";

    View mView;
    private MainActivity mActivity;

    private int mChannelId = -1;

    private RefreshLayout mRefreshLayout;

    private NewsManager mNewsManager;
    private SparseArray<List<News>> mSparseArray;
    private List<News> mNewsList;

    private NewsListAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, " -- onCreate() ");

        mChannelId = getArguments().getInt("channelId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, " -- onCreateView() " + mChannelId);

        mView = inflater.inflate(R.layout.child_fragment_index, container, false);
        mActivity = (MainActivity) getActivity();

        // 初始化下拉刷新控件
        initView(mView);

        // 初始化数据
        initData();

        // 初始化新闻列表
        initRecyclerView(mView);

        return mView;
    }

    /**
     * 初始化下拉刷新控件
     */
    private void initView(View v) {
        mRefreshLayout = v.findViewById(R.id.smart_refresh_layout_child_fragment);
        // 下拉刷新
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        // 上拉加载
        mRefreshLayout.setOnLoadmoreListener(mOnLoadMoreListener);
    }


    /**
     * 初始化数据
     */
    private void initData() {

        mNewsManager = new NewsManager(mActivity);
        mSparseArray = new SparseArray<>();

        String sectionStr = "channelId = " + mChannelId;
        List<News> newList = mNewsManager.selectDB(sectionStr);

        if (newList.size() <= 0) {
            // 没有数据，触发下拉刷新
            mRefreshLayout.autoRefresh();
        } else {
            // 有数据，根据 pageToken 值分组
            for (News news : newList) {
                int pageToken = news.getPageToken();
                List<News> list = mSparseArray.get(pageToken, null);
                if (list == null) {
                    list = new ArrayList<>();
                    mSparseArray.append(pageToken, list);
                }
                list.add(news);
            }
        }
    }

    /**
     * 初始化新闻列表
     */
    private void initRecyclerView(View v) {

        mNewsList = new ArrayList<>();
        for (int i = 0; i < mSparseArray.size(); i++) {
            int key = mSparseArray.keyAt(i);
            mNewsList.addAll(0, mSparseArray.get(key));
        }

        RecyclerView recyclerView = v.findViewById(R.id.rv_new_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new NewsListAdapter(mActivity, mNewsList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("url", mNewsList.get(position).getUrl());
                mActivity.startActivity(intent);
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
                        break;
                    case 2:
                        // 删除数据
                        List<News> list = mSparseArray.get(news.getPageToken(), null);
                        if (list != null){
                            list.remove(news);
                        }
                        mNewsManager.deleteDB(news);
                        break;
                    default:
                }
            }
        });
    }

    /**
     * 下拉刷新
     */
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            requestData(mChannelId, "0");
        }
    };

    /**
     * 上拉加载更多
     */
    private OnLoadmoreListener mOnLoadMoreListener = new OnLoadmoreListener() {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            int size = mNewsList.size();
            if (size > 0) {
                int pageToken = mNewsList.get(size - 1).getPageToken();
                String pageTokenStr = String.valueOf(pageToken);
                requestData(mChannelId, pageTokenStr);
            } else {
                refreshlayout.finishLoadmoreWithNoMoreData();
            }
        }
    };

    /**
     * OkHttp 请求数据
     */
    private void requestData(int channelId, String pageTokenStr) {

        String url = NewsHttp.getUrl(channelId, pageTokenStr, null);
        Log.d(TAG, "url " + url);

        NewsHttp.sendRequestWithOkHttp(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败，结束刷新状态
                e.printStackTrace();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefresh(false);
                        mRefreshLayout.finishLoadmore(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功，取出数据
                String str = response.body().string();
                Log.d(TAG, "body " + str);
                // 解析 Json 数据
                parseJson(str);

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefresh(true);
                        mRefreshLayout.finishLoadmore(true);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    /**
     * 解析数据
     */
    private void parseJson(String jsonData) {

        List<News> tempList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String pageTokenStr = jsonObject.getString("pageToken");
            int pageToken = Integer.parseInt(pageTokenStr);
            String data = jsonObject.getString("data");
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String url = jsonObject.getString("url");
                String title = jsonObject.getString("title");
                String content = jsonObject.getString("content");
                String pubName = jsonObject.getString("posterScreenName");
                String pubDate = jsonObject.getString("publishDateStr");
                String imageUrls = jsonObject.getString("imageUrls");

                News news = new News(mChannelId, pageToken, id, url, title, content, pubName, pubDate);

                if (!imageUrls.equals("null")) {
                    List<String> imageList = new ArrayList<>();
                    JSONArray imageUrlsArray = new JSONArray(imageUrls);
                    for (int j = 0; j < imageUrlsArray.length(); j++) {
                        imageList.add(imageUrlsArray.getString(j));
                    }
                    news.setImageList(imageList);
                }

                tempList.add(news);
            }
            Log.d(TAG, "pageToken : " + pageTokenStr + " listSize : " + tempList.size());

            // 跟据 pageToken 值，再次判断数据是否获取
            List<News> list = mSparseArray.get(pageToken, null);
            if (list == null) {
                // 新数据，记录，并添加进数据库
                mSparseArray.append(pageToken, tempList);
                mNewsManager.insertDB(tempList);

                // 下拉刷新，重置列表数据
                if (mNewsList.size() > 0) {
                    if (pageToken > mNewsList.get(0).getPageToken()) {
                        mNewsList.clear();
                    }
                }
                mNewsList.addAll(tempList);
            } else {
                mNewsList.addAll(list);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, " -- onDestroyView() " + mChannelId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " -- onDestroy() " + mChannelId);
    }
}
