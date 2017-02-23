package cz.fely.weightedaverage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.view.View;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.SubjectEightFragment;
import cz.fely.weightedaverage.SubjectFiveFragment;
import cz.fely.weightedaverage.SubjectFourFragment;
import cz.fely.weightedaverage.SubjectNineFragment;
import cz.fely.weightedaverage.SubjectOneFragment;
import cz.fely.weightedaverage.SubjectSevenFragment;
import cz.fely.weightedaverage.SubjectSixFragment;
import cz.fely.weightedaverage.SubjectTenFragment;
import cz.fely.weightedaverage.SubjectThreeFragment;
import cz.fely.weightedaverage.SubjectTwoFragment;

public class PreferencesUtil {
    public static SharedPreferences mPrefs;
    public static String one, two, three, four, five, six, seven, eight, nine, ten;

    public static void reloadPref(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        //Weight
        boolean weight = mPrefs.getBoolean("pref_key_general_weight", true);
        if(SubjectOneFragment.view != null | SubjectTwoFragment.view != null | SubjectThreeFragment
                .view != null | SubjectFourFragment.view != null | SubjectFiveFragment.view !=
                null | SubjectSixFragment.view != null | SubjectSevenFragment.view != null |
                SubjectEightFragment.view != null | SubjectNineFragment.view != null |
                SubjectTenFragment.view != null) {
            if (weight) {
                SubjectOneFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectTwoFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectThreeFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectFourFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectFiveFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectSixFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectSevenFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectEightFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectNineFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectTenFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
            } else {
                SubjectOneFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectTwoFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectThreeFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectFourFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectFiveFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectSixFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectSevenFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectEightFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectNineFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectTenFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void getTabNames(Context ctx){
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        //Names
        one = mPrefs.getString("pref_key_subject_name_one", ctx.getResources().getString(R.string
                        .nameOneDefault));
        two = mPrefs.getString("pref_key_subject_name_two", ctx.getResources().getString(R.string
                        .nameTwoDefault));
        three = mPrefs.getString("pref_key_subject_name_three", ctx.getResources().getString(R.string
                        .nameThreeDefault));
        four = mPrefs.getString("pref_key_subject_name_four", ctx.getResources().getString(R.string
                        .nameFourDefault));
        five = mPrefs.getString("pref_key_subject_name_five", ctx.getResources().getString(R.string
                        .nameFiveDefault));
        six = mPrefs.getString("pref_key_subject_name_six", ctx.getResources().getString(R.string
                        .nameSixDefault));
        seven = mPrefs.getString("pref_key_subject_name_seven", ctx.getResources().getString(R.string
                .nameSevenDefault));
        eight = mPrefs.getString("pref_key_subject_name_eight", ctx.getResources().getString(R.string
                .nameEightDefault));
        nine = mPrefs.getString("pref_key_subject_name_nine", ctx.getResources().getString(R.string
                .nameNineDefault));
        ten = mPrefs.getString("pref_key_subject_name_ten", ctx.getResources().getString(R.string
                .nameTenDefault));
    }
}
