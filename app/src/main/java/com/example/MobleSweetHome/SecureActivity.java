package com.example.MobleSweetHome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class SecureActivity extends AppCompatActivity {
    Button btn1,btn2;
    private WebView webView;
    private WebSettings webSettings;
    private String url = "http://112.221.103.174:8091/?action=stream"; // 라즈베리파이 웹캠 링크
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure);

        setting();
        btn1.setOnClickListener(MENU);
        btn2.setOnClickListener(Police_Call);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }

    public void setting() {
        btn1 = (Button) findViewById(R.id.secure_menu);
        btn2 = (Button) findViewById(R.id.call112);
        webView=(WebView)findViewById(R.id.cctv);
    }

    View.OnClickListener MENU = new View.OnClickListener() { // 메뉴
        @Override
        public void onClick(View view) {
            finish();
        }
    };
    View.OnClickListener Police_Call = new View.OnClickListener() { // 신고하기
        @Override
        public void onClick(View view) {
            Uri uri = Uri.parse("tel:112");
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
}