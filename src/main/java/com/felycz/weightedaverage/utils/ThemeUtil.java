package com.felycz.weightedaverage.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.felycz.weightedaverage.R;

public final class ThemeUtil extends AppCompatActivity{
    public static void setAppTheme(Activity a) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(a);
        int mTheme = sp.getInt("theme", 0);
        if (mTheme == 0) {
            a.setTheme(R.style.Default);
        }
        else if(mTheme == 1) {
            a.setTheme(R.style.Dark);
        }
        else if(mTheme == 2){
            a.setTheme(R.style.Light);
        }
    }
}