package com.example.muntako;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Bantuan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        setContentView(R.layout.activity_bantuan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebView browser = (WebView) findViewById(R.id.bantuan);
        browser.getSettings().setJavaScriptEnabled(true);

        browser.loadUrl("file:///android_asset/bantuan.php");
    }
}
