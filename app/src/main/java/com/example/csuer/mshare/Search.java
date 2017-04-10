package com.example.csuer.mshare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by csuer on 2017/4/7.
 */

public class Search extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
        Toast.makeText(this,"本功能需要联网",Toast.LENGTH_SHORT).show();
        WebView webView=(WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        try{
            webView.loadUrl("http://jwctest.its.csu.edu.cn/Logon.do?method=logon");
        }
      catch (Exception e)
      {
          Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
          finish();
      }
    }
}
