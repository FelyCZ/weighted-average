package cz.fely.weightedaverage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import cz.fely.weightedaverage.R;

public final class ThemeUtil extends AppCompatActivity{
    public static void setAppTheme(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String themeString = sp.getString("pref_key_general_theme", "0");
        int mTheme = Integer.parseInt(themeString);
        if (mTheme == 0) {
            context.setTheme(R.style.Light);
        }
        else if(mTheme == 1) {
            context.setTheme(R.style.Dark);
        }
        else if(mTheme == 2){
            context.setTheme(R.style.Dark_Black);
        }
    }
}