package com.mukundvis.twitnews.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.adapters.KeywordRecyclerAdapter;
import com.mukundvis.twitnews.constants.ApiConstants;
import com.mukundvis.twitnews.models.KeywordWithRelevance;
import com.mukundvis.twitnews.models.KeywordResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mukundvis on 26/06/15.
 */
public class KnowledgeBaseActivity extends BaseActivity {

    private static final String DEBUG_TAG = KnowledgeBaseActivity.class.getSimpleName();

    GetKeywordsService service;
    RecyclerView rv;

    public interface GetKeywordsService {
        @GET(ApiConstants.URL_GET_KEYWORDS)
        void getKeywords(
                @Query("device_id") String deviceId,
                @Query("user_id") long userId,
                retrofit.Callback<KeywordResponse> callback
        );
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.kb;
    }

    @Override
    protected void getViewReferences() {
        rv = getRecyclerView(R.id.rv);
    }

    @Override
    protected void extractArguments(Bundle arguments) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView.LayoutManager mLayoutManager = new android.support.v7.widget.LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.END_POINT_V2)
                .build();
        service = restAdapter.create(GetKeywordsService.class);
        service.getKeywords(getPrefs().getDeviceId(), getPrefs().getUserId(), new Callback<KeywordResponse>() {
            @Override
            public void success(KeywordResponse keywordResponse, Response response) {
                List<KeywordWithRelevance> keywords = keywordResponse.getUserKeywords();
                if (keywords == null) {
                    return;
                }
                for (KeywordWithRelevance keyword: keywords) {
                    Log.e(DEBUG_TAG, keyword.toString());
                }
                KeywordRecyclerAdapter adapter = new KeywordRecyclerAdapter(keywords);
                rv.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
