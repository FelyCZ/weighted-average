package cz.fely.weightedaverage.utils;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import cz.fely.weightedaverage.R;

public final class ThemeUtil extends AppCompatActivity{
    static int mTheme = 0;
    private static int[] THEMES = new int[] {
            R.style.Light,
            R.style.Dark,
            R.style.Dark_Black };

    private ThemeUtil() {
    }

    public static int getSelectTheme(Context ctx) {
        int theme = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(ctx).getString
                ("pref_key_general_theme", "0"));
        return (theme >= 0 && theme < THEMES.length) ? theme : 0;
    }

    public static void setTheme(Activity activity) {
        mTheme = getSelectTheme(activity);
        activity.setTheme(THEMES[mTheme]);
    }

    public static void reloadTheme(Activity activity) {
        int theme = getSelectTheme(activity);
        if (theme != mTheme)
            activity.recreate();
    }
}