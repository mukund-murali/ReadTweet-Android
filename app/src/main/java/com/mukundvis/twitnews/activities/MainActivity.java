package com.mukundvis.twitnews.activities;

import android.os.Bundle;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.constants.ApiConstants;
import com.mukundvis.twitnews.models.LoginResponse;
import com.twitter.sdk.android.core.Callback;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, HowItWorksActivity.class);
        startActivity(intent);
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

    private TwitterLoginButton loginButton;
    private Button btnKnowMore;

    @Override
    protected int getDefaultLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void getViewReferences() {
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        btnKnowMore = getButton(R.id.btn_know_more);
        btnKnowMore.setOnClickListener(this);
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

}
