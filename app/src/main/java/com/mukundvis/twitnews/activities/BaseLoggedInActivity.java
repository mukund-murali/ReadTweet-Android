package com.mukundvis.twitnews.activities;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by mukundvis on 21/06/15.
 */
public abstract class BaseLoggedInActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getPrefs().isLoggedIn()) {
            this.finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
