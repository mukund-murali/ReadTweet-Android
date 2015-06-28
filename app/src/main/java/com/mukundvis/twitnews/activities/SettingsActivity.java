package com.mukundvis.twitnews.activities;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.prefs.SharedPrefs;

/**
 * Created by mukundvis on 28/06/15.
 */
public class SettingsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    SharedPrefs prefs;

    CheckBox cbDisplayImages;

    @Override
    protected int getDefaultLayout() {
        return R.layout.settings_activity;
    }

    @Override
    protected void getViewReferences() {
        cbDisplayImages = getCheckBox(R.id.cb_display_image);
        cbDisplayImages.setOnCheckedChangeListener(this);
    }

    @Override
    protected void extractArguments(Bundle arguments) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getPrefs();
        if (prefs.shouldShowImages()) {
            cbDisplayImages.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        prefs.setShouldShowImages(isChecked).commit();
    }
}
