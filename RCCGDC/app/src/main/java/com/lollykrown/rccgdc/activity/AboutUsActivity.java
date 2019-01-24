package com.lollykrown.rccgdc.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lollykrown.rccgdc.R;

public class AboutUsActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProgressBar progress = findViewById(R.id.progress);

        webView = findViewById(R.id.about_us);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setBuiltInZoomControls(false);
        ws.setAllowFileAccess(true);
        ws.setAppCacheEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView v, final String url) {
                return false;
            }

            @Override
            public void onPageStarted(final WebView v, final String url, final Bitmap favicon){
                progress.setVisibility(View.VISIBLE);
                super.onPageStarted(v, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView v, final String url) {
                progress.setVisibility(View.GONE);
                super.onPageFinished(v, url);
            }
        });

        webView.loadUrl("http://www.rccg.org/who-we-are/history/");
    }

    @Override
    public void onBackPressed(){
        if (webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

