package com.example.group5_onlinetourbookingsystem.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.example.group5_onlinetourbookingsystem.R;

public class WebViewActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);

        String paymentUrl = getIntent().getStringExtra("paymentUrl");
        if (paymentUrl != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();
                    if (url.contains("vnp_ResponseCode")) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("vnp_ResponseCode", url.split("vnp_ResponseCode=")[1].split("&")[0]);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                        return true;
                    }
                    return false;
                }
            });
            webView.loadUrl(paymentUrl);
        }
    }
}
