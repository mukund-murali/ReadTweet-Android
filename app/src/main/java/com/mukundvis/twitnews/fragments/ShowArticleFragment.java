package com.mukundvis.twitnews.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.constants.ApiConstants;
import com.mukundvis.twitnews.models.ArticleResponse;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mukundvis on 28/06/15.
 */
public class ShowArticleFragment extends BaseFragment implements View.OnClickListener {

    public interface GetCleansedArticleService {
        @GET(ApiConstants.URL_GET_CLEAN_ARTICLE)
        void getArticle(
                @Query("token") String token,
                @Query("url") String url,
                @Query("timeout") int timeoutInMillis,
                retrofit.Callback<ArticleResponse> callback
        );
    }

    private static final int API_TIMEOUT_MILLIS = 5000;
    private static final String API_TOKEN = "339e6c3483c1beb15cba1523ee615f55";

    private static final String IMG_WIDTH_STYLE = "<style>img{display: inline;height: auto;max-width: 100%;}</style>";

    private static final String KEY_URL = "url";

    private int cardPadding;
    private String url;

    private WebView webview;
    private ProgressBar pb, pbCircle;
    private TextView tv, tvArticleTitle;
    private Button btnShowOriginal;
    private LinearLayout llLoading;

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
        tvArticleTitle = (TextView) parent.findViewById(R.id.tv_article_title);
        tv = (TextView) parent.findViewById(R.id.tv_info);
        pb = (ProgressBar) parent.findViewById(R.id.pb);
        pbCircle = (ProgressBar) parent.findViewById(R.id.pb_circle);
        btnShowOriginal = (Button) parent.findViewById(R.id.btn_show_original);
        btnShowOriginal.setOnClickListener(this);
        llLoading = (LinearLayout) parent.findViewById(R.id.ll_loading);

        webview = (WebView) parent.findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        // webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb.setVisibility(View.VISIBLE);
                if (newProgress > 0) {
                    pb.setProgress(newProgress);
                }
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                }
            }
        });
    }

    void downloadArticle(final String url) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.DIFFBOT_ENDPOINT)
                .build();
        GetCleansedArticleService service = restAdapter.create(GetCleansedArticleService.class);
        tv.setText("Trying to be smart.");
        llLoading.setVisibility(View.VISIBLE);
        service.getArticle(API_TOKEN,
                url, API_TIMEOUT_MILLIS, new Callback<ArticleResponse>() {
                    @Override
                    public void success(ArticleResponse articleResponse, Response response) {
                        // issues encountered: url is a PDF, in that case, directly load the website.
                        // If we were able to get a stripped down version, show it, - This should be the case most of the time.
                        // else load the webpage.
                        if (articleResponse != null && articleResponse.hasArticle()) {
                            ArticleResponse.Article article = articleResponse.objects.get(0);
                            String title = article.title;
                            if (!TextUtils.isEmpty(title)) {
                                tvArticleTitle.setVisibility(View.VISIBLE);
                                tvArticleTitle.setText(title);
                            }
                            if (TextUtils.isEmpty(article.html)) {
                                showWebpage(url);
                            } else {
                                Log.e(DEBUG_TAG, article.title);
                                btnShowOriginal.setVisibility(View.VISIBLE);
                                webview.setPadding(cardPadding, cardPadding, cardPadding, cardPadding);
                                webview.loadData(IMG_WIDTH_STYLE + article.html, "text/html", "UTF-8");
                            }
                        } else {
                            showWebpage(url);
                        }
                        llLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        llLoading.setVisibility(View.GONE);
                        error.printStackTrace();
                        webview.loadUrl(url);
                    }
                });
    }

    @Override
    protected void extractArguments(Bundle arguments) {
        url = arguments.getString(KEY_URL);
    }

    boolean shouldDirectlyLoadUrl(String url) {
        if (url.endsWith(".pdf")) {
            return true;
        }
        return false;
    }

    @Override
    protected void initViews() {
        if (url != null) {
            if (shouldDirectlyLoadUrl(url)) {
                showWebpage(url);
            } else {
                downloadArticle(url);
            }
        } else {
            Toast.makeText(getActivity(), "Article not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void showWebpage(String url) {
        tvArticleTitle.setVisibility(View.GONE);
        webview.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        showWebpage(url);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardPadding = (int) getResources().getDimension(R.dimen.card_padding);
    }
}
