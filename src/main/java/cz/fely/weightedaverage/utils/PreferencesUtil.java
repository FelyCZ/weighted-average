package cz.fely.weightedaverage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.preference.PreferenceManager;
import android.view.View;

import java.text.DecimalFormat;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.subjects.SubjectEightFragment;
import cz.fely.weightedaverage.subjects.SubjectFiveFragment;
import cz.fely.weightedaverage.subjects.SubjectFourFragment;
import cz.fely.weightedaverage.subjects.SubjectNineFragment;
import cz.fely.weightedaverage.subjects.SubjectOneFragment;
import cz.fely.weightedaverage.subjects.SubjectSevenFragment;
import cz.fely.weightedaverage.subjects.SubjectSixFragment;
import cz.fely.weightedaverage.subjects.SubjectTenFragment;
import cz.fely.weightedaverage.subjects.SubjectThreeFragment;
import cz.fely.weightedaverage.subjects.SubjectTwoFragment;

import static cz.fely.weightedaverage.MainActivity.mDbAdapterStatic;

public final class PreferencesUtil {
    private static SharedPreferences mPrefs;
    public static String overview;
    public static String one, two, three, four, five, six, seven, eight, nine, ten;
    public static String oneAvg, twoAvg, threeAvg, fourAvg, fiveAvg, sixAvg, sevenAvg, eightAvg, nineAvg, tenAvg;
    public static double sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8, sum9, sum10;

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
        overview = ctx.getResources().getString(R.string.overView);
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

    public static void getTabAverages(){
        Cursor cursor;
        DecimalFormat formater;

        cursor = mDbAdapterStatic.averageFromMarks();
        sum1 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        oneAvg = String.valueOf(formater.format(sum1));

        cursor = mDbAdapterStatic.averageFromMarks2();
        sum2 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        twoAvg = String.valueOf(formater.format(sum2));

        cursor = mDbAdapterStatic.averageFromMarks3();
        sum3 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        threeAvg = String.valueOf(formater.format(sum3));

        cursor = mDbAdapterStatic.averageFromMarks4();
        sum4 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        fourAvg = String.valueOf(formater.format(sum4));

        cursor = mDbAdapterStatic.averageFromMarks5();
        sum5 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        fiveAvg = String.valueOf(formater.format(sum5));

        cursor = mDbAdapterStatic.averageFromMarks6();
        sum6 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        sixAvg = String.valueOf(formater.format(sum6));

        cursor = mDbAdapterStatic.averageFromMarks7();
        sum7 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        sevenAvg = String.valueOf(formater.format(sum7));

        cursor = mDbAdapterStatic.averageFromMarks8();
        sum8 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        eightAvg = String.valueOf(formater.format(sum8));

        cursor = mDbAdapterStatic.averageFromMarks9();
        sum9 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        nineAvg = String.valueOf(formater.format(sum9));

        cursor = mDbAdapterStatic.averageFromMarks10();
        sum10 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        tenAvg = String.valueOf(formater.format(sum10));
    }
}
