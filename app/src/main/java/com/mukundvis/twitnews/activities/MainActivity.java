package com.mukundvis.twitnews.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mukundvis.twitnews.MyApplication;
import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.models.LoginResponse;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import android.content.Intent;
import android.widget.Toast;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends BaseActivity {

    public interface GitHubService {
        @FormUrlEncoded
        @POST("/login")
        void login(
                @Field("user_id") long userId,
                @Field("username") String username,
                @Field("auth_token") String authToken,
                @Field("auth_token_secret") String authTokenSecret,
                 retrofit.Callback<LoginResponse> callback
        );
    }

    public static final String HOST = "http://192.168.1.18:3000/";
    public static final String END_POINT = HOST + "api/v1";


    private TwitterLoginButton loginButton;

    @Override
    protected int getDefaultLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void getViewReferences() {
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
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
                        .setEndpoint(END_POINT)
                        .build();

                GitHubService service = restAdapter.create(GitHubService.class);
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
