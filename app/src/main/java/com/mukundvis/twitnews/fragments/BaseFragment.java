package com.mukundvis.twitnews.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mukundvis.twitnews.MyApplication;
import com.mukundvis.twitnews.prefs.SharedPrefs;

/**
 * Created by mukundvis on 28/06/15.
 */
public abstract class BaseFragment extends Fragment{

    public final String DEBUG_TAG = this.getClass().getSimpleName();

    abstract protected View getDefaultLayout(LayoutInflater inflater, ViewGroup container);

    abstract protected void getViewReferences(View parent);

    abstract protected void extractArguments(Bundle arguments);

    protected abstract void initViews();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        if (extras != null) {
            extractArguments(extras);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = getDefaultLayout(inflater, container);
        if (layout != null) {
            getViewReferences(layout);
            initViews();
        }
        return layout;
    }

    protected SharedPrefs getPrefs() {
        return MyApplication.getInstance().getPrefs();
    }

}
