package com.example.muntako;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Jadwal_Dokter extends AppCompatActivity {
    private WebView mWebview;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        setContentView(R.layout.activity_jadwal_dokter);
        mWebview  = new WebView(this);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        pd = new ProgressDialog(Jadwal_Dokter.this);
        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.setMessage(getResources().getString(R.string.tunggu_msg));
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        mWebview.loadUrl(Config.JADWAL);
        setContentView(mWebview );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}