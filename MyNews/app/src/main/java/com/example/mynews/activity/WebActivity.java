package com.example.mynews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.mynews.R;

/**
 * WebActivity
 *
 * @author ggz
 * @date 2018/2/6
 */
public class WebActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "WebActivity";

    private WebView webView;
    private ImageView goback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        // ActivityCollector
        ActivityCollector.addActivity(this);

        String url = getIntent().getStringExtra("url");


        webView = (WebView) findViewById(R.id.web_webview);
//        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
//        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条
        webView.loadUrl(url);

        goback = (ImageView) findViewById(R.id.web_goback_iv);
        goback.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 优先使用缓存
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.web_goback_iv:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();   //返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {

        webView.getSettings().setJavaScriptEnabled(false);

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        webView.destroy();
        ActivityCollector.removeActivity(this);

        super.onDestroy();
    }
}
