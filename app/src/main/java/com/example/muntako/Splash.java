package com.example.muntako;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Window;
import android.webkit.WebView;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        Thread logotimer = new Thread() {
            public void run() {
                try {
                    int logotimer = 0;
                    while (logotimer < 5000) {
                        sleep(50);
                        logotimer = logotimer + 100;
                    }
                    startActivity(new Intent(getBaseContext(), Utama.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        logotimer.start();
        WebView load = (WebView)findViewById(R.id.webView);
        load.getSettings().setJavaScriptEnabled(true);
        load.loadDataWithBaseURL("file:///android_asset/", "<html><center><img src=\"progress.gif\" width=30%>" +
                "</html>", "text/html", "utf-8", "");
    }
}
