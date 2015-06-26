package com.mukundvis.twitnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.mukundvis.twitnews.MyApplication;
import com.mukundvis.twitnews.prefs.SharedPrefs;

/**
 * Created by mukundvis on 21/06/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    abstract protected int getDefaultLayout();

    abstract protected void getViewReferences();

    abstract protected void extractArguments(Bundle arguments);

    SharedPrefs getPrefs() {
        return MyApplication.getInstance().getPrefs();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_PROGRESS);
        int defaultLayout = getDefaultLayout();
        if (defaultLayout != 0) {
            setContentView(defaultLayout);
            getViewReferences();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extractArguments(extras);
        }
    }

    protected RecyclerView getRecyclerView(int id) {
        return (RecyclerView) findViewById(id);
    }
}
