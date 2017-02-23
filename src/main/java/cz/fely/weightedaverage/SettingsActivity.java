package cz.fely.weightedaverage;


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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import cz.fely.weightedaverage.utils.ThemeUtil;

public class SettingsActivity extends AppCompatPreferenceActivity {
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
                    preference.setSummary(R.string.pref_ringtone_silent);

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
        setSummariesEditText();
    }

    private void setSummariesEditText() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager pm = getPreferenceManager();
        EditTextPreference one, two, three, four, five, six, seven, eight, nine, ten;
        String oneS, twoS, threeS, fourS, fiveS, sixS, sevenS, eightS, nineS, tenS;
        one = (EditTextPreference) pm.findPreference("pref_key_subject_name_one");
        two = (EditTextPreference) pm.findPreference("pref_key_subject_name_two");
        three = (EditTextPreference) pm.findPreference("pref_key_subject_name_three");
        four = (EditTextPreference) pm.findPreference("pref_key_subject_name_four");
        five = (EditTextPreference) pm.findPreference("pref_key_subject_name_five");
        six = (EditTextPreference) pm.findPreference("pref_key_subject_name_six");
        seven = (EditTextPreference) pm.findPreference("pref_key_subject_name_seven");
        eight = (EditTextPreference) pm.findPreference("pref_key_subject_name_eight");
        nine = (EditTextPreference) pm.findPreference("pref_key_subject_name_nine");
        ten = (EditTextPreference) pm.findPreference("pref_key_subject_name_ten");

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
    }

    private void initToolbar() {
        Toolbar toolbar;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.list).getParent().getParent().getParent();
            toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.tool_bar, root, false);
            root.addView(toolbar, 0);
        } else {
            toolbar = null;
        }

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
        super.onBackPressed();
    }
}