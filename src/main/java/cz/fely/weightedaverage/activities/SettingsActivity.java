package cz.fely.weightedaverage.activities;

import android.app.Activity;
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

                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

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

    /*private void initET(){
        et1S = "pref_key_subject_name_one";
        et2S = "pref_key_subject_name_two";
        et3S = "pref_key_subject_name_three";
        et4S = "pref_key_subject_name_four";
        et5S = "pref_key_subject_name_five";
        et6S = "pref_key_subject_name_six";
        et7S = "pref_key_subject_name_seven";
        et8S = "pref_key_subject_name_eight";
        et9S = "pref_key_subject_name_nine";
        et10S = "pref_key_subject_name_ten";
        et11S = "pref_key_subject_name_eleven";
        et12S = "pref_key_subject_name_twelve";
        et13S = "pref_key_subject_name_tt";
        et14S = "pref_key_subject_name_ft";

        et1 = (EditTextPreference) findPreference(et1S);
        et2 = (EditTextPreference) findPreference(et2S);
        et3 = (EditTextPreference) findPreference(et3S);
        et4 = (EditTextPreference) findPreference(et4S);
        et5 = (EditTextPreference) findPreference(et5S);
        et6 = (EditTextPreference) findPreference(et6S);
        et7 = (EditTextPreference) findPreference(et7S);
        et8 = (EditTextPreference) findPreference(et8S);
        et9 = (EditTextPreference) findPreference(et9S);
        et10 = (EditTextPreference) findPreference(et10S);
        et11 = (EditTextPreference) findPreference(et11S);
        et12 = (EditTextPreference) findPreference(et12S);
        et13 = (EditTextPreference) findPreference(et13S);
        et14 = (EditTextPreference) findPreference(et14S);

        et1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_one", String.valueOf(newValue)).apply();
                et1.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_two", String.valueOf(newValue)).apply();
                et2.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_three", String.valueOf(newValue)).apply();
                et3.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et4.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_four", String.valueOf(newValue)).apply();
                et4.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et5.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_five", String.valueOf(newValue)).apply();
                et5.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et6.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_six", String.valueOf(newValue)).apply();
                et6.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et7.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_seven", String.valueOf(newValue)).apply();
                et7.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et8.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_eight", String.valueOf(newValue)).apply();
                et8.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et9.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_nine", String.valueOf(newValue)).apply();
                et9.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et10.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_ten", String.valueOf(newValue)).apply();
                et10.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et11.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_eleven", String.valueOf(newValue)).apply();
                et7.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et12.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_twelve", String.valueOf(newValue)).apply();
                et8.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et13.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_tt", String.valueOf(newValue)).apply();
                et9.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
        et14.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_subject_name_ft", String.valueOf(newValue)).apply();
                et10.setText(String.valueOf(newValue));
                setSummariesEditText();
                return false;
            }
        });
    }*/

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
             /*   SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("pref_key_general_theme", String.valueOf(newValue)).apply();
                lp.setValue(String.valueOf(newValue));
                lp.getEntry();*/
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

    @SuppressWarnings("deprecation")
    private void setSummariesEditText() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
     /*   EditTextPreference one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, tt, ft;
        String oneS, twoS, threeS, fourS, fiveS, sixS, sevenS, eightS, nineS, tenS, elevenS, twelveS, ttS, ftS;
        one = (EditTextPreference) findPreference("pref_key_subject_name_one");
        two = (EditTextPreference) findPreference("pref_key_subject_name_two");
        three = (EditTextPreference) findPreference("pref_key_subject_name_three");
        four = (EditTextPreference) findPreference("pref_key_subject_name_four");
        five = (EditTextPreference) findPreference("pref_key_subject_name_five");
        six = (EditTextPreference) findPreference("pref_key_subject_name_six");
        seven = (EditTextPreference) findPreference("pref_key_subject_name_seven");
        eight = (EditTextPreference) findPreference("pref_key_subject_name_eight");
        nine = (EditTextPreference) findPreference("pref_key_subject_name_nine");
        ten = (EditTextPreference) findPreference("pref_key_subject_name_ten");
        eleven = (EditTextPreference) findPreference("pref_key_subject_name_eleven");
        twelve = (EditTextPreference) findPreference("pref_key_subject_name_twelve");
        tt = (EditTextPreference) findPreference("pref_key_subject_name_tt");
        ft = (EditTextPreference) findPreference("pref_key_subject_name_ft");

        oneS = mPrefs.getString("pref_key_subject_name_one", getResources().getString(R.string
                .nameOneDefault));
        twoS = mPrefs.getString("pref_key_subject_name_two", getResources().getString(R.string
                .nameTwoDefault));
        threeS = mPrefs.getString("pref_key_subject_name_three", getResources().getString(R.string
                .nameThreeDefault));
        fourS = mPrefs.getString("pref_key_subject_name_four", getResources().getString(R.string
                .nameFourDefault));
        fiveS = mPrefs.getString("pref_key_subject_name_five", getResources().getString(R.string
                .nameFiveDefault));
        sixS = mPrefs.getString("pref_key_subject_name_six", getResources().getString(R.string
                .nameSixDefault));
        sevenS = mPrefs.getString("pref_key_subject_name_seven", getResources().getString(R.string
                .nameSevenDefault));
        eightS = mPrefs.getString("pref_key_subject_name_eight", getResources().getString(R.string
                .nameEightDefault));
        nineS = mPrefs.getString("pref_key_subject_name_nine", getResources().getString(R.string
                .nameNineDefault));
        tenS = mPrefs.getString("pref_key_subject_name_ten", getResources().getString(R.string
                .nameTenDefault));
        elevenS = mPrefs.getString("pref_key_subject_name_eleven", getResources().getString(R.string
                .nameElevenDefault));
        twelveS = mPrefs.getString("pref_key_subject_name_twelve", getResources().getString(R.string
                .nameTwelveDefault));
        ttS = mPrefs.getString("pref_key_subject_name_tt", getResources().getString(R.string
                .nameTtDefault));
        ftS = mPrefs.getString("pref_key_subject_name_ft", getResources().getString(R.string
                .nameFtDefault));

        one.setSummary(oneS);
        two.setSummary(twoS);
        three.setSummary(threeS);
        four.setSummary(fourS);
        five.setSummary(fiveS);
        six.setSummary(sixS);
        seven.setSummary(sevenS);
        eight.setSummary(eightS);
        nine.setSummary(nineS);
        ten.setSummary(tenS);
        eleven.setSummary(elevenS);
        twelve.setSummary(twelveS);
        tt.setSummary(ttS);
        ft.setSummary(ftS);*/
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
            startActivity(new Intent(this, MainActivity.class).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
        finish();
        MainActivity.man.recreate();
        super.onBackPressed();
    }
}