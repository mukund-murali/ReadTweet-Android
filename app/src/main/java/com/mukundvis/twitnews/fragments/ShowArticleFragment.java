package com.mukundvis.twitnews.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mukundvis.twitnews.R;

/**
 * Created by mukundvis on 28/06/15.
 */
public class ShowArticleFragment extends BaseFragment {

    private static final String KEY_URL = "url";

    private String url;

    private WebView webview;
    private ProgressBar pb;
    private TextView tv;

    public static ShowArticleFragment getInstance(String url) {
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        ShowArticleFragment f = new ShowArticleFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    protected View getDefaultLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.show_article_fragment, container, false);
    }

    @Override
    protected void getViewReferences(View parent) {
        tv = (TextView) parent.findViewById(R.id.tv_info);
        pb = (ProgressBar) parent.findViewById(R.id.pb);
        webview = (WebView) parent.findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb.setVisibility(View.VISIBLE);
                if (newProgress > 0){
                    pb.setProgress(newProgress);
                }
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void extractArguments(Bundle arguments) {
        url = arguments.getString(KEY_URL);
    }

    @Override
    protected void initViews() {
        if (url != null) {
            webview.loadUrl(url);
        } else {
            tv.setText("Article not available.");
        }
    }
}
