package com.mukundvis.twitnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.constants.ApiConstants;
import com.mukundvis.twitnews.fragments.ObPg1Fragment;
import com.mukundvis.twitnews.fragments.ObPg2Fragment;
import com.mukundvis.twitnews.models.LoginResponse;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager pager;
    // private CirclePageIndicator pageIndicator;
    private Button btnLogin;
    private ImageButton ibNextSlide;
    private TwitterLoginButton loginButton;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_next_slide:
                pager.setCurrentItem(1);
                ibNextSlide.setVisibility(View.GONE);
                break;
        }
    }

    public interface LoginService {
        @FormUrlEncoded
        @POST(ApiConstants.URL_LOGIN)
        void login(
                @Field("user_id") long userId,
                @Field("username") String username,
                @Field("auth_token") String authToken,
                @Field("auth_token_secret") String authTokenSecret,
                 retrofit.Callback<LoginResponse> callback
        );
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void getViewReferences() {
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        pager = (ViewPager) findViewById(R.id.pager);
        btnLogin = getButton(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        ibNextSlide = (ImageButton) findViewById(R.id.ib_next_slide);
        ibNextSlide.setOnClickListener(this);
        // pageIndicator = (CirclePageIndicator) findViewById(R.id.circles);
    }

    @Override
    protected void extractArguments(Bundle arguments) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPrefs().isLoggedIn()) {
            goToDashboard();
            this.finish();
            return;
        }
        pager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        // pageIndicator.setViewPager(pager);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                String authToken = result.data.getAuthToken().token;
                String authTokenSecret = result.data.getAuthToken().secret;
                final long userId = result.data.getUserId();
                String username = result.data.getUserName();

                final RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ApiConstants.END_POINT_V1)
                        .build();

                LoginService service = restAdapter.create(LoginService.class);
                service.login(userId, username, authToken, authTokenSecret, new retrofit.Callback<LoginResponse>() {
                    @Override
                    public void success(LoginResponse loginResponse, Response response) {
                        String deviceId = loginResponse.deviceId;
                        getPrefs().setLoginInfo(userId, deviceId).commit();
                        MainActivity.this.finish();
                        goToDashboard();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(MainActivity.this, "Login error. Try later.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    private void goToDashboard() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        MainActivity.this.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private static final String TITLE_KEYWORDS = "Keywords";
        private static final String TITLE_TAXONOMIES = "Taxonomy";

        private static final int NUM_PAGES = 2;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ObPg1Fragment();
                default:
                    return new ObPg2Fragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // If article is not there, do not show the article tab
            switch (position) {
                case 0:
                    return TITLE_KEYWORDS;
                default:
                    return TITLE_TAXONOMIES;
            }
        }
    }

}
