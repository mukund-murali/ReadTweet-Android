package com.mukundvis.twitnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.mukundvis.twitnews.MyApplication;
import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.prefs.SharedPrefs;
import com.mukundvis.twitnews.utils.Utils;

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
        showSnackIfInternetNA();
    }

    protected RecyclerView getRecyclerView(int id) {
        return (RecyclerView) findViewById(id);
    }

    protected LinearLayout getLinearLayout(int id) {
        return (LinearLayout) findViewById(id);
    }

    protected CheckBox getCheckBox(int id) {
        return (CheckBox) findViewById(id);
    }

    protected View getRootView() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    protected void showSnackIfInternetNA() {
        if (Utils.isOnline(this)) {
            return;
        }
        Snackbar.make(getRootView(), R.string.internet_na, Snackbar.LENGTH_LONG)
                .setAction(R.string.goto_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                    }
                })
                .show();
    }

    protected Button getButton(int id) {
        return (Button) findViewById(id);
    }

    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }
}
