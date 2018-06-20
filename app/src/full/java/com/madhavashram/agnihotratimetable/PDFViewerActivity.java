package com.madhavashram.agnihotratimetable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.madhavashram.agnihotratimetable.utils.CommonUtils;
import com.madhavashram.agnihotratimetable.views.AbstractActivity;

public class PDFViewerActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdfviewer);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String pdfFilePath = getIntent().getStringExtra(CommonUtils.PDF_FILE_TAG);

        WebView webView = findViewById(R.id.pdfViewer);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=".concat(pdfFilePath));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                CommonUtils.showProgressDialog(PDFViewerActivity.this, null);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CommonUtils.dismissProgressDialog(PDFViewerActivity.this);
            }
        });

    }

}
