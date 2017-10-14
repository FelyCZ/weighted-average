package cz.fely.weightedaverage.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import cz.fely.weightedaverage.abstracts.AppCompatPreferenceActivity;
import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.utils.ThemeUtil;
import cz.fely.weightedaverage.utils.WipeDataUtil;

@SuppressWarnings("deprecation")
public class SettingsActivity extends AppCompatPreferenceActivity {
    private boolean recreate = false;
    static final String prefTheme = "pref_key_general_theme";
    static final String prefWipe = "pref_key_general_wipe";
    public static final String prefOkMark = "prefs_overview_okmark";
    public static final String prefFfMark = "prefs_overview_ffmark";
    static final String prefBadMark = "prefs_overview_badmark";
    static EditTextPreference etpOkMark, etpFfMark;
    String et1S, et2S, et3S, et4S, et5S, et6S, et7S, et8S, et9S, et10S, et11S, et12S, et13S, et14S;
    EditTextPreference et1, et2, et3, et4, et5, et6, et7, et8, et9, et10, et11, et12, et13, et14;
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

            } else if (preference instanceof RingtonePreference) {
                if (TextUtils.isEmpty(stringValue)) {
                 //   preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        preference.setSummary(null);
                    } else {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        initToolbar();
        addPreferencesFromResource(R.xml.prefs);
        Activity a = this;

        ListPreference lp = (ListPreference) findPreference(prefTheme);
        lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_general_theme", String.valueOf(newValue)).apply();
                lp.setValue(String.valueOf(newValue));
                lp.getEntry();
                ThemeUtil.reloadTheme(a);
                return true;
            }
        });

        Preference p = findPreference(prefWipe);
        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(preference.getContext());
                builder.setTitle(R.string.titleWipeData);
                builder.setMessage(R.string.processCannotBeUndone);
                builder.setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WipeDataUtil.getInstance().clearApplicationData();
                    }
                });
                builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return false;
            }
        });

        etpOkMark = (EditTextPreference) findPreference(prefOkMark);
        etpFfMark = (EditTextPreference) findPreference(prefFfMark);
        Preference etpBadMark = findPreference(prefBadMark);
        Preference prefRestoreDefault = findPreference("prefs_key_general_restore");

        etpOkMark.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int newAvg =  Integer.parseInt(String.valueOf(newValue));
                if(newAvg >= Integer.parseInt(etpFfMark.getText())){
                    Toast.makeText(getApplicationContext(), R.string.toast_error_settings_bad_avg, Toast.LENGTH_SHORT).show();
                }
                else if(newAvg < 1){
                    Toast.makeText(getApplicationContext(), R.string.toast_error_settings_zero_avg, Toast.LENGTH_SHORT).show();
                }
                else{
                    return true;
                }
                return false;
            }
        });

        etpFfMark.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int newAvg = Integer.parseInt(String.valueOf(newValue));
                if(newAvg > 4){
                    Toast.makeText(getApplicationContext(), R.string.toast_error_settings_big_avg, Toast.LENGTH_SHORT).show();
                }
                else if(newAvg <= Integer.parseInt(etpOkMark.getText())){
                    Toast.makeText(getApplicationContext(), R.string.toast_error_settings_small_avg, Toast.LENGTH_SHORT).show();
                }
                else {
                    return true;
                }
                return false;
            }
        });

        prefRestoreDefault.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_general_order", "desc");
                editor.putString("prefs_overview_okmark", "2");
                editor.putString("prefs_overview_ffmark", "3");
                editor.putBoolean("pref_key_general_weight", true);
                editor.putBoolean("autoComplete", true);
                editor.putString("pref_key_general_theme", "0");
                editor.apply();
                Toast.makeText(getApplicationContext(), R.string.restored, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;
            }
        });
    }

    public static int getAvgFromPreference(String key) {
        if (key.equals(prefOkMark)) {
            return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(MainActivity.context).getString(prefOkMark, "2"));
        }
        else if (key.equals(prefFfMark)){
            return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(MainActivity.context).getString(prefFfMark, "3"));
        }
        else return 0;
    }

    private void initToolbar() {
        Toolbar toolbar;

        ViewGroup root = (ViewGroup) findViewById(android.R.id.list).getParent().getParent().getParent();
        toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.tool_bar, root, false);
        root.addView(toolbar, 0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(recreate){
                startActivity(new Intent(this, MainActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
            else
                finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume (){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(recreate) {
            finish();
            startActivity(new Intent(this, MainActivity.class).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else
            finish();
        super.onBackPressed();
    }
}