package com.coreimagine.chatandmisson.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.coreimagine.chatandmisson.R;
import com.coreimagine.chatandmisson.utils.KeyboardUtil;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
    }
    private void initView(){
        webView = findViewById(R.id.webView);
        webView.getSettings().setSupportZoom(false); // support zoom
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(false);// 可任意比例缩放
        webView.getSettings().setLoadWithOverviewMode(false);
        // 开启web和js通信的通道。
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.loadUrl("http://47.100.118.90:60201/CI_1/index.php/Webchat/");

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                KeyboardUtil.hideKeyboard(WebActivity.this, webView);

            }
        });
    }
}
