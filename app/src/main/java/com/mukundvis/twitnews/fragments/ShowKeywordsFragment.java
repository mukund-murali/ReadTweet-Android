package com.mukundvis.twitnews.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.activities.BaseActivity;
import com.mukundvis.twitnews.adapters.KeywordRecyclerAdapter;
import com.mukundvis.twitnews.constants.ApiConstants;
import com.mukundvis.twitnews.models.KeywordResponse;
import com.mukundvis.twitnews.models.KeywordWithRelevance;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mukundvis on 28/06/15.
 */
public class ShowKeywordsFragment extends BaseFragment{

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
    protected View getDefaultLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.keywords_fragment, container, false);
    }

    @Override
    protected void getViewReferences(View parent) {
        rv = (RecyclerView) parent.findViewById(R.id.rv);
    }

    @Override
    protected void extractArguments(Bundle arguments) {

    }

    @Override
    protected void initViews() {
        RecyclerView.LayoutManager mLayoutManager = new android.support.v7.widget.LinearLayoutManager(getActivity());
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
                KeywordRecyclerAdapter adapter = new KeywordRecyclerAdapter(keywords);
                rv.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static Fragment getInstance() {
        return new ShowKeywordsFragment();
    }
}
