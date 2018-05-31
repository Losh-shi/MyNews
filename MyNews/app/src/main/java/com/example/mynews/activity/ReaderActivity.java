package com.example.mynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mynews.R;
import com.example.mynews.bean.News;

/**
 * ReaderActivity
 *
 * @author ggz
 * @date 2018/2/6
 */
public class ReaderActivity extends AppCompatActivity implements View.OnClickListener {

    private News news;

    private ImageView goback;
    private TextView more;
    private TextView title, pubname, pubdate, content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        // ActivityCollector
        ActivityCollector.addActivity(this);

        news = (News) getIntent().getSerializableExtra("news");

        initView();
    }

    private void initView() {
        goback = (ImageView) findViewById(R.id.reader_goback_iv);
        goback.setOnClickListener(this);
        more = (TextView) findViewById(R.id.reader_more_tv);
        more.setOnClickListener(this);

        title = (TextView) findViewById(R.id.reader_title_tv);
        pubname = (TextView) findViewById(R.id.reader_pubname_tv);
        pubdate = (TextView) findViewById(R.id.reader_pubdate_tv);
        content = (TextView) findViewById(R.id.reader_content_tv);

        title.setText(news.getTitle());
        pubname.setText(news.getPubName());
        pubdate.setText(news.getPubDate());
        content.setText(news.getContent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reader_goback_iv:
                finish();
                break;
            case R.id.reader_more_tv:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", news.getUrl());
//                overridePendingTransition();
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
