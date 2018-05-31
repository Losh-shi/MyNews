package com.example.mynews.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynews.R;
import com.example.mynews.adapter.NewsListAdapter;
import com.example.mynews.bean.News;
import com.example.mynews.http.NewsHttp;
import com.example.mynews.manager.NewsManager;
import com.example.mynews.view.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * SearchActivity
 *
 * @author ggz
 * @date 2018/3/5
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchActivity";

    private static final String KEY_HISTORY_TAG = "historyTag";

    private MaterialEditText mMaterialEditText;

    private LinearLayout mSearchHistoryLl;
    private FlowLayout mFlowLayout;

    private SmartRefreshLayout mRefreshLayout;
    private NewsListAdapter mAdapter;

    private NewsManager mNewsManager;
    private List<News> mNewsList;
    private String mKeyWord;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // ActivityCollector
        ActivityCollector.addActivity(this);

        initView();

        initData();

        initMaterialEditText();

        initRecyclerView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 返回
        ImageView goBackIv = findViewById(R.id.iv_search_go_back);
        goBackIv.setOnClickListener(this);
        // 提交
        TextView commitTv = findViewById(R.id.tv_search_commit);
        commitTv.setOnClickListener(this);

        // 刷新控件
        mRefreshLayout = findViewById(R.id.smart_refresh_layout_search);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadmoreListener(mOnLoadMoreListener);

        // 搜索历史
        mSearchHistoryLl = findViewById(R.id.ll_search_history);
        AnimationSet animationSet = (AnimationSet) AnimationUtils
                .loadAnimation(this, R.anim.view_translate_in);
        mSearchHistoryLl.startAnimation(animationSet);
        // FlowLayout
        mFlowLayout = findViewById(R.id.flow_layout_history_tag);
        mFlowLayout.setOnTagClickListener(new FlowLayout.OnTagClickListener() {
            @Override
            public void onClick(String text) {
                mMaterialEditText.setText(text);
                search();
            }
        });
        // 清空标签
        ImageView clearTagIv = findViewById(R.id.iv_clear_tag);
        clearTagIv.setOnClickListener(this);
    }


    private void initData() {
        // 读取 SharedPreferences 里面的 History Tag
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String json = sp.getString(KEY_HISTORY_TAG, null);
        if (json != null) {
            List<String> list = new Gson().fromJson(json, new TypeToken<List<String>>() {
            }.getType());
            mFlowLayout.setListData(list);
        }

        // 初始化新闻数据管理器
        mNewsManager = new NewsManager(this);
        mNewsList = new ArrayList<>();
    }

    private void initMaterialEditText() {
        mMaterialEditText = findViewById(R.id.et_search_input);
        mMaterialEditText.requestFocus();
        showKeyboard(true);

        mMaterialEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return false;
            }
        });

        mMaterialEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (TextUtils.isEmpty(content)) {
                    // 隐藏列表
                    mRefreshLayout.setVisibility(View.GONE);
                    // 显示搜索记录
                    AnimationSet animationSet = (AnimationSet) AnimationUtils
                            .loadAnimation(SearchActivity.this, R.anim.view_translate_in);
                    mSearchHistoryLl.startAnimation(animationSet);
                }
            }
        });
    }

    private void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (isShow) {
            imm.showSoftInput(mMaterialEditText, 0);
        } else {
            imm.hideSoftInputFromWindow(mMaterialEditText.getWindowToken(), 0);
        }
    }

    /**
     * 初始化搜索结果列表
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_search_result);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new NewsListAdapter(this, mNewsList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, WebActivity.class);
                intent.putExtra("url", mNewsList.get(position).getUrl());
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

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_go_back:
                finish();
                break;

            case R.id.tv_search_commit:
                search();
                break;

            case R.id.iv_clear_tag:
                mFlowLayout.cleanTag();
                mFlowLayout.editMode(false);
                break;

            default:
                break;
        }
    }

    private void search() {
        // 获取关键词
        mKeyWord = mMaterialEditText.getText().toString();
        if (!TextUtils.isEmpty(mKeyWord)) {
            // 添加标签
            mFlowLayout.addTag(mKeyWord);
            // 重置数据
            mNewsList.clear();
            // 数据请求
            requestData(mKeyWord, "0");
            // 显示列表
            mRefreshLayout.setVisibility(View.VISIBLE);
        }
        showKeyboard(false);
    }

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
                requestData(mKeyWord, pageTokenStr);
            } else {
                refreshlayout.finishLoadmoreWithNoMoreData();
            }
        }
    };

    /**
     * OkHttp 请求数据
     */
    private void requestData(String keyWord, String pageTokenStr) {

        String url = NewsHttp.getUrl(33, keyWord, pageTokenStr);

        NewsHttp.sendRequestWithOkHttp(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishLoadmore(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String str = response.body().string();
                Log.d(TAG, "body " + str);

                // 解析 Json 数据
                parseJson(str);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishLoadmore(true);
                        mAdapter.notifyDataSetChanged();

                        if (mNewsList.size() == 0) {
                            Toast.makeText(SearchActivity.this, "没有找到相关内容", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 数据解析
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

                News news = new News(33, pageToken, id, url, title, content, pubName, pubDate);
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

            mNewsList.addAll(tempList);

            Log.d(TAG, "pageToken : " + pageTokenStr + " listSize : " + tempList.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 双击返回键退出应用
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mFlowLayout.getIsEditMode()) {
                mFlowLayout.editMode(false);
                return true;
            } else {
                if (!TextUtils.isEmpty(mMaterialEditText.getText())) {
                    mMaterialEditText.setText("");
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

        // History Tag 存入 SharedPreferences
        List<String> list = mFlowLayout.getHistoryTagList();
        String json = new Gson().toJson(list);
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(KEY_HISTORY_TAG, json);
        editor.apply();

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }
}
